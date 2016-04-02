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
import java.util.ListIterator;

/**
 * A double list. <b>A DoubleList is NOT Thread-safe!</b>
 *
 * @author TheElectronWill
 */
public final class DoubleList {
	
	private final int capacityIncrement;
	private double[] values;
	private int size = 0;
	
	/**
	 * Creates a new DoubleList with an initial capacity of 10.
	 */
	public DoubleList() {
		this.values = new double[10];
		this.capacityIncrement = 2;
	}
	
	/**
	 * Creates a new DoubleList with the given underlying array. Any change to the array is reflected to the list and
	 * vice-versa. <b>This relation does no longer work if the list grow.</b>
	 *
	 * @param values
	 */
	public DoubleList(double... values) {
		this.values = values;
		this.capacityIncrement = 2;
	}
	
	/**
	 * Creates a new OpenList that containsAt all the values of the given collection.
	 *
	 * @param values
	 */
	public DoubleList(Collection<Double> values) {
		this.capacityIncrement = 2;
		this.values = new double[values.size()];
		for (double b : values) {
			add(b);
		}
	}
	
	public DoubleList(int capacityIncrement, double... values) {
		this.values = values;
		this.capacityIncrement = capacityIncrement;
	}
	
	public DoubleList(int capacityIncrement, Collection<Double> values) {
		this.values = new double[values.size()];
		for (double b : values) {
			add(b);
		}
		this.capacityIncrement = capacityIncrement;
	}
	
	/**
	 * Creates a new OpenList with the given initial capacity.
	 *
	 * @param cap
	 */
	public DoubleList(int cap) {
		this.values = new double[cap];
		this.capacityIncrement = 2;
	}
	
	public DoubleList(int cap, int capacityIncrement) {
		this.values = new double[cap];
		this.capacityIncrement = capacityIncrement;
	}
	
	/**
	 * Sets the doubleernal array that containsAt the data of this list.
	 *
	 * @param values
	 */
	public void setValues(double... values) {
		this.values = values;
	}
	
	/**
	 * Returns the doubleernal array that containsAt the data of this list. Modifications to this array will change the
	 * list's content directly.
	 *
	 * @return
	 */
	public double[] getValues() {
		return values;
	}
	
	public boolean add(double e) {
		if (size == values.length) {
			values = Arrays.copyOf(values, size + capacityIncrement);
		}
		values[size++] = e;
		return true;
	}
	
	public void add(int index, double element) {
		rangeCheck(index);
		values = Arrays.copyOf(values, size + capacityIncrement);
		System.arraycopy(values, index, values, index + 1, size - index);
		values[index] = element;
		size++;
	}
	
	private void rangeCheck(int index) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}
	
	public boolean addAll(Collection<Double> c) {
		boolean changed = true;
		for (double e : c) {
			changed = changed | add(e);
		}
		return changed;
	}
	
	public boolean addAll(int index, Collection<Double> c) {
		rangeCheck(index);
		
		Object[] cValues = c.toArray();
		final int numNew = cValues.length;
		values = Arrays.copyOf(values, size + numNew);
		
		final int numMoved = size - index;
		if (numMoved > 0) {
			System.arraycopy(values, index, values, index + numNew, numMoved);
		}
		
		System.arraycopy(cValues, 0, values, index, numNew);
		size += numNew;
		return numNew != 0;
	}
	
	public void clear() {
		this.values = new double[size];
	}
	
	public boolean contains(Object o) {
		if (!(o instanceof Number)) {
			return false;
		}
		for (Object v : values) {
			if (v.equals(o)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(double b) {
		for (int i = 0; i < size; i++) {
			double e = values[i];
			if (e == b) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if this list contains an element at the specified position.
	 *
	 * @param index
	 * @return true if an element exists at the given index, false otherwise.
	 */
	public boolean containsAt(int index) {
		return index > 0 && index < size;
	}
	
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}
	
	public double get(int index) {
		rangeCheck(index);
		return values[index];
	}
	
	public int indexOf(double b) {
		for (int i = 0; i < values.length; i++) {
			double v = values[i];
			if (v == b) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public Iterator<Double> iterator() {
		return new DoubleListIterator();
	}
	
	public double lastIndexOf(double b) {
		for (int i = size - 1; i > -1; i--) {
			double v = values[i];
			if (b == v) {
				return i;
			}
		}
		return -1;
	}
	
	public ListIterator<Double> listIterator() {
		return new DoubleListIterator();
	}
	
	private final class DoubleListIterator implements ListIterator<Double> {
		
		private int cursor;
		
		public DoubleListIterator() {
			this.cursor = 0;
		}
		
		public DoubleListIterator(int cursor) {
			this.cursor = cursor;
		}
		
		@Override
		public void add(Double e) {
			DoubleList.this.add(cursor, e);
		}
		
		@Override
		public void set(Double e) {
			DoubleList.this.set(cursor, e);
		}
		
		@Override
		public boolean hasNext() {
			return cursor < size;
		}
		
		@Override
		public boolean hasPrevious() {
			return cursor > 0;
		}
		
		@Override
		public Double next() {
			return values[cursor++];
		}
		
		@Override
		public int nextIndex() {
			return cursor;
		}
		
		@Override
		public Double previous() {
			return values[--cursor];
		}
		
		@Override
		public int previousIndex() {
			return cursor - 1;
		}
		
		@Override
		public void remove() {
			DoubleList.this.remove(cursor);
		}
		
	}
	
	public ListIterator<Double> listIterator(int index) {
		return new DoubleListIterator(index);
	}
	
	public boolean remove(double b) {
		for (int i = 0; i < size; i++) {
			double v = values[i];
			if (v == b) {
				remove(i);
				return true;
			}
		}
		return false;
	}
	
	public double removeAt(int index) {
		rangeCheck(index);
		double previous = values[index];
		int n = size - index - 1;
		if (n > 0) {
			System.arraycopy(values, index + 1, values, index, n);
		}
		return previous;
	}
	
	public boolean removeAll(Collection<Double> c) {
		boolean changed = false;
		for (Double o : c) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}
	
	public boolean retainAll(Collection<Double> c) {
		boolean changed = false;
		for (int i = 0; i < size; i++) {
			double value = values[i];
			if (!contains(value)) {
				changed = true;
				remove(i);
			}
		}
		return changed;
	}
	
	public double size() {
		return size;
	}
	
	public double set(int index, double element) {
		rangeCheck(index);
		final double prev = values[index];
		values[index] = element;
		return prev;
	}
	
	/**
	 * Sets the element at the specified position in this list to the specified element. If the values exists it is
	 * replaced. If the index is greater than the list's capacity then the list grows.
	 *
	 * @param index
	 * @param element
	 */
	public void setOrAdd(int index, double element) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		if (index >= values.length) {
			values = Arrays.copyOf(values, index + 1);
		}
		values[index] = element;
	}
	
	/**
	 * Returns the capacity of this list.
	 *
	 * @return
	 */
	public double capacity() {
		return values.length;
	}
	
	public DoubleList subList(int fromIndex, int toIndex) {
		return new DoubleList(Arrays.copyOfRange(values, fromIndex, toIndex));
	}
	
	public double[] toArray() {
		return Arrays.copyOf(values, size);
	}
	
}
