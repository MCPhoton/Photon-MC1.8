/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.electronwill.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A lock-free queue based on a circular buffer using the "keep one slot open" technique. This implementation is safe
 * <b>only if there is ONE consumer</b>. One Thread only may use {@link #poll()} or {@link #remove()} or
 * {@link #drainTo(java.util.Collection)}, or {@link #take()} at the same time.
 *
 * @author TheElectronWill
 * @param <E> the type of elements held in this collection
 */
@SuppressWarnings("unchecked")
public class StrategicMPSCQueue<E> extends AbstractQueue<E>implements BlockingQueue<E> {
	
	/**
	 * Returns the closest power of two that is bigger than or equal to the given integer.
	 *
	 * @param n a non-null integer
	 * @return the next power of two.
	 */
	private static int nextPowerOfTwo(int n) {
		return Integer.highestOneBit(n - 1) << 1;
	}
	
	/**
	 * Head of the queue. It's not thread-safe because only one consumer modify it.
	 */
	private volatile int head = 0;
	
	/**
	 * Tail of the queue. It's thread-safe.
	 */
	private final AtomicInteger tail = new AtomicInteger();
	
	/**
	 * Bitmask used to replace modulo operations by logical AND.
	 */
	private final int moduloMask;
	
	/**
	 * The queue's data.
	 */
	private final Object[] array;
	
	private WaitStrategy strategy;
	
	private class SCIterator implements Iterator<E> {
		
		private int delta = 0;
		
		@Override
		public boolean hasNext() {
			final int index = (head + delta) & moduloMask;
			return array[index] != null;
		}
		
		@Override
		public E next() {
			final int index = (head + delta) & moduloMask;
			final E next = (E) array[index];
			if (next == null) {
				return null;
			}
			delta++;
			return next;
		}
		
	}
	
	/**
	 * Creates a new SingleConsumerBlockingQueue with a fixed capacity of 16.
	 */
	public StrategicMPSCQueue() {
		array = new Object[16];
		moduloMask = 15;
		strategy = WaitStrategy.LOOP;
	}
	
	public StrategicMPSCQueue(final int capacity) {
		int cap = nextPowerOfTwo(capacity + 1);
		array = new Object[cap];
		moduloMask = cap - 1;
		strategy = WaitStrategy.PARK_NANOS;
	}
	
	public StrategicMPSCQueue(final int capacity, final WaitStrategy strategy) {
		int cap = nextPowerOfTwo(capacity + 1);
		array = new Object[cap];
		moduloMask = cap - 1;
		this.strategy = strategy;
	}
	
	/**
	 * {@inheritDoc} <br>
	 * <b>This iterator does NOT support removing elements.</b>
	 */
	@Override
	public Iterator<E> iterator() {
		return new SCIterator();
	}
	
	@Override
	public boolean isEmpty() {
		return array[head] == null;
	}
	
	@Override
	public void clear() {
		head = 0;
		tail.set(0);
	}
	
	public boolean isFull() {
		// We use the "keep one slot empty" technique to make the difference between the case where
		// the array is full and the case where it's empty.
		return head == ((tail.get() + 1) & moduloMask);
	}
	
	@Override
	public int size() {
		int cTail = tail.get();
		return cTail < head ? cTail + array.length - head : cTail - head;
	}
	
	@Override
	public boolean offer(E e) {
		if (e == null) {
			throw new NullPointerException("Tried to offer an invalid element: null");
		}
		
		// Atomically updates tail:
		int prev = tail.get();
		if (head == ((prev + 1) & moduloMask)) {// queue is full
			return false;
		}
		while (!tail.compareAndSet(prev, (prev + 1) & moduloMask)) {
			prev = tail.get();
			if (head == ((prev + 1) & moduloMask)) {// queue is full
				return false;
			}
			strategy.doWait();
		}
		array[prev] = e;// assignation is atomic. Producers cannot overwrite themselves because
						// different producers will get different "prev" values
		return true;
	}
	
	@Override
	public E poll() {
		final E e = (E) array[head];
		if (e == null) {// queue is empty
			return null;
		}
		array[head] = null;
		head = (head + 1) & moduloMask;// It's ok because only one thread updates the head variable.
		return e;
	}
	
	@Override
	public E peek() {
		final E e = (E) array[head];
		if (e == null) {// queue is empty
			return null;
		}
		return e;
	}
	
	@Override
	public void put(E e) throws InterruptedException {
		boolean success = offer(e);
		while (!success) {
			strategy.doWait();// makes the Thread sleep for a very short time.
			success = offer(e);
		}
	}
	
	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		
		boolean success = offer(e);
		if (success) {
			return true;
		}
		
		long elapsedTime = 0;
		final long t0 = System.nanoTime();
		while (!success && elapsedTime - nanos < 0) {
			strategy.doWait();
			success = offer(e);
			long t1 = System.nanoTime();
			elapsedTime = t1 - t0;
		}
		return success;
	}
	
	@Override
	public E take() throws InterruptedException {
		while (isEmpty()) {
			strategy.doWait();// makes the Thread sleep for a short time.
		}
		return poll();
	}
	
	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		
		E result = poll();
		if (result != null) {
			return result;
		}
		
		long elapsedTime = 0;
		final long t0 = System.nanoTime();
		while (result == null && elapsedTime - nanos < 0) {
			strategy.doWait();
			result = poll();
			long t1 = System.nanoTime();
			elapsedTime = t1 - t0;
		}
		return result;
	}
	
	@Override
	public int remainingCapacity() {
		return array.length - 1 - size();
	}
	
	@Override
	public int drainTo(Collection<? super E> c) {
		if (c == this) {
			throw new IllegalArgumentException("Cannot drain a queue to itself !");
		}
		int count = 0;
		while (!isEmpty()) {
			E e = poll();
			c.add(e);
			count++;
		}
		return count;
	}
	
	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		if (c == this) {
			throw new IllegalArgumentException("Cannot drain a queue to itself !");
		}
		int count = 0;
		while (!isEmpty() && count < maxElements) {
			E e = poll();
			c.add(e);
			count++;
		}
		return count;
	}
	
}
