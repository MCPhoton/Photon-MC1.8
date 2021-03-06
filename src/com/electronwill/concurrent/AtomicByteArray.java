/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.electronwill.concurrent;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * A byte array in which elements may be updated atomically. This class internally uses
 * {@link AtomicIntegerArray}.
 *
 * @author TheElectronWill
 */
public final class AtomicByteArray {

	/**
	 * The array containing the data.
	 */
	public final AtomicIntegerArray array;

	/**
	 *
	 * @param capacity
	 * @see AtomicIntegerArray#AtomicIntegerArray(int)
	 */
	public AtomicByteArray(int capacity) {
		array = new AtomicIntegerArray(capacity);
	}

	/**
	 *
	 * @param array
	 * @see AtomicIntegerArray#AtomicIntegerArray(int[])
	 */
	public AtomicByteArray(byte[] array) {
		this.array = new AtomicIntegerArray(array.length);
		for (int i = 0; i < array.length; i++) {
			this.array.set(i, array[i]);
		}
	}

	/**
	 * Returns the length of the array.
	 *
	 * @return the length of the array.
	 */
	public final int length() {
		return array.length();
	}

	/**
	 * Gets the current value at position {@code i}.
	 *
	 * @param i the index
	 * @return the current value at the given position
	 * @see AtomicIntegerArray#get(int)
	 */
	public final byte get(int i) {
		return (byte) array.get(i);
	}

	/**
	 * Sets the element at position {@code i} to the given value.
	 *
	 * @param i the index
	 * @param newValue the new value
	 * @see AtomicIntegerArray#set(int, int)
	 */
	public final void set(int i, byte newValue) {
		array.set(i, newValue);
	}

	/**
	 * Sets the element at position {@code i} to the given value.
	 *
	 * @param i the index
	 * @param newValue the new value <b>will be converted to byte</b>
	 * @see AtomicIntegerArray#set(int, int)
	 */
	public final void set(int i, int newValue) {
		array.set(i, (byte) newValue);
	}

	/**
	 * Eventually sets the element at position {@code i} to the given value.
	 *
	 * @param i the index
	 * @param newValue the new value
	 * @see AtomicIntegerArray#lazySet(int, int)
	 */
	public final void lazySet(int i, byte newValue) {
		array.lazySet(i, newValue);
	}

	/**
	 * Atomically sets the element at position {@code i} to the given value and returns the old
	 * value.
	 *
	 * @param i the index
	 * @param newValue the new value
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndSet(int, int)
	 */
	public final byte getAndSet(int i, byte newValue) {
		return (byte) array.getAndSet(i, newValue);
	}

	/**
	 * Atomically sets the element at position {@code i} to the given updated value if the current
	 * value {@code ==} the expected value.
	 *
	 * @param i the index
	 * @param expect the expected value
	 * @param update the new value
	 * @return {@code true} if successful. False return indicates that the actual value was not
	 * equal to the expected value.
	 * @see AtomicIntegerArray#compareAndSet(int, int, int)
	 */
	public final boolean compareAndSet(int i, byte expect, byte update) {
		return array.compareAndSet(i, expect, update);
	}

	/**
	 * Atomically sets the element at position {@code i} to the given updated value if the current
	 * value {@code ==} the expected value.
	 *
	 * <p>
	 * <b>May fail spuriously and does not provide ordering guarantees, so is only rarely a wise
	 * alternative to {@code compareAndSet}</b>
	 *
	 * @param i the index
	 * @param expect the expected value
	 * @param update the new value
	 * @return {@code true} if successful
	 * @see AtomicIntegerArray#weakCompareAndSet(int, int, int)
	 */
	public final boolean weakCompareAndSet(int i, byte expect, byte update) {
		return compareAndSet(i, expect, update);
	}

	/**
	 * Atomically increments by one the element at index {@code i}.
	 *
	 * @param i the index
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndIncrement(int)
	 */
	public final byte getAndIncrement(int i) {
		return (byte) array.getAndIncrement(i);
	}

	/**
	 * Atomically decrements by one the element at index {@code i}.
	 *
	 * @param i the index
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndDecrement(int)
	 */
	public final byte getAndDecrement(int i) {
		return (byte) array.getAndAdd(i, -1);
	}

	/**
	 * Atomically adds the given value to the element at index {@code i}.
	 *
	 * @param i the index
	 * @param delta the value to add
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndAdd(int, int)
	 */
	public final byte getAndAdd(int i, int delta) {
		return (byte) array.getAndAdd(i, delta);
	}

	/**
	 * Atomically increments by one the element at index {@code i}.
	 *
	 * @param i the index
	 * @return the updated value
	 * @see AtomicIntegerArray#incrementAndGet(int)
	 */
	public final byte incrementAndGet(int i) {
		return (byte) (array.getAndAdd(i, 1) + 1);
	}

	/**
	 * Atomically decrements by one the element at index {@code i}.
	 *
	 * @param i the index
	 * @return the updated value
	 * @see AtomicIntegerArray#decrementAndGet(int)
	 */
	public final byte decrementAndGet(int i) {
		return (byte) (array.getAndAdd(i, -1) - 1);
	}

	/**
	 * Atomically adds the given value to the element at index {@code i}.
	 *
	 * @param i the index
	 * @param delta the value to add
	 * @return the updated value
	 * @see AtomicIntegerArray#addAndGet(int, int)
	 */
	public final byte addAndGet(int i, int delta) {
		return (byte) (array.getAndAdd(i, delta) + delta);
	}

	/**
	 * Atomically updates the element at index {@code i} with the results of applying the given
	 * function, returning the previous value.
	 *
	 * @param i the index
	 * @param updateFunction
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndUpdate(int, java.util.function.IntUnaryOperator)
	 */
	public final byte getAndUpdate(int i, IntUnaryOperator updateFunction) {
		return (byte) array.getAndUpdate(i, updateFunction);
	}

	/**
	 * Atomically updates the element at index {@code i} with the results of applying the given
	 * function, returning the updated value.
	 *
	 * @param i the index
	 * @param updateFunction a side-effect-free function
	 * @return the updated value
	 * @see AtomicIntegerArray#updateAndGet(int, java.util.function.IntUnaryOperator)
	 */
	public final byte updateAndGet(int i, IntUnaryOperator updateFunction) {
		return (byte) (array.updateAndGet(i, updateFunction));
	}

	/**
	 * Atomically updates the element at index {@code i} with the results of applying the given
	 * function to the current and given values, returning the previous value.
	 *
	 * @param i the index
	 * @param x the update value
	 * @param accumulatorFunction
	 * @return the previous value
	 * @see AtomicIntegerArray#getAndAccumulate(int, int, java.util.function.IntBinaryOperator)
	 */
	public final byte getAndAccumulate(int i, int x, IntBinaryOperator accumulatorFunction) {
		return (byte) array.getAndAccumulate(i, x, accumulatorFunction);
	}

	/**
	 * Atomically updates the element at index {@code i} with the results of applying the given
	 * function to the current and given values, returning the updated value.
	 *
	 * @param i the index
	 * @param x the update value
	 * @param accumulatorFunction
	 * @return the updated value
	 * @see AtomicIntegerArray#accumulateAndGet(int, int, java.util.function.IntBinaryOperator)
	 */
	public final byte accumulateAndGet(int i, int x, IntBinaryOperator accumulatorFunction) {
		return (byte) array.accumulateAndGet(i, x, accumulatorFunction);
	}

	/**
	 * Returns representation of the current content of this array.
	 *
	 * @return the representation of this array.
	 * @see AtomicIntegerArray#toString()
	 */
	@Override
	public String toString() {
		return array.toString();
	}

}
