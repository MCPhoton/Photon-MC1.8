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
package com.electronwill.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A bag that has a maximum size limit. It can grow but not bigger than the limit. The delete operation is in constant
 * time because we just move the last element to fill the gap.
 *
 * @author TheElectronWill
 * @param <E>
 */
public final class LimitedBag<E> extends Bag<E> {
	
	private class BagIterator implements Iterator<E> {
		
		private int pos = 0;
		
		@Override
		public boolean hasNext() {
			return pos < size;
		}
		
		@Override
		public E next() {
			return (E) array[pos++];
		}
		
	}
	private Object[] array;
	private int size = 0;
	private int capacityIncrement;
	
	private int limit;
	
	/**
	 * Constructs a new Bag with an initial capacity of ten and an increment of 2.
	 */
	public LimitedBag() {
		this(10, 2);
	}
	
	/**
	 * Creates a new SimpleBag with the given initialCapacity and size limit, and a default increment of 2.
	 *
	 * @param initialCapacity
	 * @param limit maximal size
	 */
	public LimitedBag(int initialCapacity, int limit) {
		this(initialCapacity, limit, 2);
	}
	
	/**
	 * Creates a new LimitedBag with the given initialCapacity, increment and size limit.
	 *
	 * @param initialCapacity
	 * @param limit maximal size
	 * @param capacityIncrement
	 */
	public LimitedBag(int initialCapacity, int limit, int capacityIncrement) {
		array = new Object[initialCapacity];
		this.capacityIncrement = capacityIncrement;
		this.limit = limit;
	}
	
	@Override
	public boolean add(E e) {
		if (size == array.length) {
			if (size == limit) {
				throw new IndexOutOfBoundsException("LimitedBag has reached its size limit of " + limit);
			}
			array = Arrays.copyOf(array, size + capacityIncrement);
		}
		array[size++] = e;
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E o : c) {
			add(o);
		}
		return true;
	}
	
	@Override
	public void clear() {
		array = new Object[10];
	}
	
	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			Object e = array[i];
			if (e.equals(o)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public E get(int index) {
		if (index < size) {
			return (E) array[index];
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}
	
	/**
	 * Returns the size limit.
	 */
	public int getLimit() {
		return limit;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns true if this LimitedBag is full, i.e. if the size limit has been reached.
	 *
	 * @return true if it is full, false if we can still add elements.
	 */
	public boolean isFull() {
		return size == limit;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new BagIterator();
	}
	
	@Override
	public void remove(int index) {
		array[index] = array[--size];
		array[size] = null;
	}
	
	@Override
	public boolean remove(Object o) {
		for (int j = 0; j < size; j++) {
			Object element = array[j];
			if (element.equals(o)) {
				remove(j);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object o : c) {
			remove(o);
		}
		return true;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				remove(o);
			}
		}
		return true;
	}
	
	/**
	 * Sets the size limit.
	 */
	public void setLimit(int newLimit) {
		this.limit = newLimit;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(array, size);
	}
	
	@Override
	public void trimToSize() {
		array = Arrays.copyOf(array, size);
	}
	
	@Override
	public E tryGet(int index) {
		if (index < size) {
			return (E) array[index];
		}
		return null;
	}
	
}
