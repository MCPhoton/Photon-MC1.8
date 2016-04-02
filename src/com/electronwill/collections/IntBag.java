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

/**
 * A reziseable int array. The delete operation is in constant time because we just move the last element to fill the
 * gap.
 *
 * @author TheElectronWill
 */
public final class IntBag {
	
	private int[] array;
	private int size = 0;
	private int capacityIncrement;
	
	/**
	 * Constructs a new IntBag with an initial capacity of ten and an increment of 2.
	 */
	public IntBag() {
		this(10, 2);
	}
	
	public IntBag(int initialCapacity) {
		this(initialCapacity, 2);
	}
	
	public IntBag(int initialCapacity, int capacityIncrement) {
		this.array = new int[initialCapacity];
		this.capacityIncrement = capacityIncrement;
	}
	
	public void add(int n) {
		if (size == array.length) {
			array = Arrays.copyOf(array, size + capacityIncrement);
		}
		array[size++] = n;
	}
	
	public void remove(int index) {
		array[index] = array[--size];
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(int n) {
		for (int i = 0; i < size; i++) {
			int b = array[i];
			if (b == n) {
				return true;
			}
		}
		return false;
	}
	
	public int get(int index) {
		return array[index];
	}
	
	public int[] toArray() {
		return Arrays.copyOf(array, size);
	}
	
}
