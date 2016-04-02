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
package com.electronwill.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * A short list. <b>A Short is NOT Thread-safe!</b>
 *
 * @author TheElectronWill
 */
public final class ShortList {

	private final int capacityIncrement;
	private short[] values;
	private int size = 0;

	/**
	 * Creates a new ShortList with an initial capacity of 10.
	 */
	public ShortList() {
		this.values = new short[10];
		this.capacityIncrement = 2;
	}

	/**
	 * Creates a new ShortList with the given underlying array. Any change to the array is reflected
	 * to the list and vice-versa. <b>This relation does no longer work if the list grow.</b>
	 *
	 * @param values
	 */
	public ShortList(short... values) {
		this.values = values;
		this.capacityIncrement = 2;
	}

	/**
	 * Creates a new ShortList that containsAt all the values of the given collection.
	 *
	 * @param values
	 */
	public ShortList(Collection<Short> values) {
		this.capacityIncrement = 2;
		this.values = new short[values.size()];
		for (short b : values) {
			add(b);
		}
	}

	public ShortList(int capacityIncrement, short... values) {
		this.values = values;
		this.capacityIncrement = capacityIncrement;
	}

	public ShortList(int capacityIncrement, Collection<Short> values) {
		this.values = new short[values.size()];
		for (short b : values) {
			add(b);
		}
		this.capacityIncrement = capacityIncrement;
	}

	/**
	 * Creates a new ShortList with the given initial capacity.
	 *
	 * @param cap
	 */
	public ShortList(int cap) {
		this.values = new short[cap];
		this.capacityIncrement = 2;
	}

	public ShortList(int cap, int capacityIncrement) {
		this.values = new short[cap];
		this.capacityIncrement = capacityIncrement;
	}

	/**
	 * Sets the internal array that containsAt the data of this list.
	 *
	 * @param values
	 */
	public void setValues(short... values) {
		this.values = values;
	}

	/**
	 * Returns the internal array that containsAt the data of this list. Modifications to this array
	 * will change the list's content directly.
	 *
	 * @return
	 */
	public short[] getValues() {
		return values;
	}

	public boolean add(short e) {
		if (size == values.length) {
			values = Arrays.copyOf(values, size + capacityIncrement);
		}
		values[size++] = e;
		return true;
	}

	public void add(int index, short element) {
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

	public boolean addAll(Collection<Short> c) {
		boolean changed = true;
		for (short e : c) {
			changed = changed | add(e);
		}
		return changed;
	}

	public boolean addAll(int index, Collection<Short> c) {
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
		this.values = new short[size];
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

	public boolean contains(short b) {
		for (int i = 0; i < size; i++) {
			short e = values[i];
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

	public short get(int index) {
		rangeCheck(index);
		return values[index];
	}

	public int indexOf(short b) {
		for (int i = 0; i < values.length; i++) {
			short v = values[i];
			if (v == b) {
				return i;
			}
		}
		return -1;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Iterator<Short> iterator() {
		return new ShortListIterator();
	}

	public int lastIndexOf(short b) {
		for (int i = size - 1; i > -1; i--) {
			short v = values[i];
			if (b == v) {
				return i;
			}
		}
		return -1;
	}

	public ListIterator<Short> listIterator() {
		return new ShortListIterator();
	}

	public ListIterator<Short> listIterator(int index) {
		return new ShortListIterator(index);
	}

	public boolean remove(short b) {
		for (int i = 0; i < size; i++) {
			short v = values[i];
			if (v == b) {
				remove(i);
				return true;
			}
		}
		return false;
	}

	public short remove(int index) {
		rangeCheck(index);
		short previous = values[index];
		int n = size - index - 1;
		if (n > 0) {
			System.arraycopy(values, index + 1, values, index, n);
		}
		return previous;
	}

	public boolean removeAll(Collection<Short> c) {
		boolean changed = false;
		for (Short o : c) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}

	public boolean retainAll(Collection<Short> c) {
		boolean changed = false;
		for (int i = 0; i < size; i++) {
			short value = values[i];
			if (!contains(value)) {
				changed = true;
				remove(i);
			}
		}
		return changed;
	}

	public int size() {
		return size;
	}

	public short set(int index, short element) {
		rangeCheck(index);
		final short prev = values[index];
		values[index] = element;
		return prev;
	}

	/**
	 * Sets the element at the specified position in this list to the specified element. If the
	 * values exists it is replaced. If the index is greater than the list's capacity then the list
	 * grows.
	 *
	 * @param index
	 * @param element
	 */
	public void setOrAdd(int index, short element) {
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
	public int capacity() {
		return values.length;
	}

	public ShortList subList(int fromIndex, int toIndex) {
		return new ShortList(Arrays.copyOfRange(values, fromIndex, toIndex));
	}

	public short[] toArray() {
		return values.clone();
	}

	private final class ShortListIterator implements ListIterator<Short> {

		private int cursor;

		public ShortListIterator() {
			this.cursor = 0;
		}

		public ShortListIterator(int cursor) {
			this.cursor = cursor;
		}

		@Override
		public void add(Short e) {
			ShortList.this.add(cursor, e);
		}

		@Override
		public void set(Short e) {
			ShortList.this.set(cursor, e);
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
		public Short next() {
			return values[cursor++];
		}

		@Override
		public int nextIndex() {
			return cursor;
		}

		@Override
		public Short previous() {
			return values[--cursor];
		}

		@Override
		public int previousIndex() {
			return cursor - 1;
		}

		@Override
		public void remove() {
			ShortList.this.remove(cursor);
		}
	}

}
