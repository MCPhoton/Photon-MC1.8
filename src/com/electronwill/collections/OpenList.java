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

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An "open" list. The internal array that stores data can be directly get or/and set. <b>It is NOT Thread-safe!</b>
 *
 * @author TheElectronWill
 * @param <E>
 */
@SuppressWarnings("all")
public final class OpenList<E> extends AbstractCollection<E>implements List<E> {
	
	private final int capacityIncrement;
	private Object[] values;
	private int size = 0;
	
	/**
	 * Creates a new OpenList with an initial capacity of 10 and a capacity increment of 2.
	 */
	public OpenList() {
		this.values = new Object[10];
		this.capacityIncrement = 2;
	}
	
	/**
	 * Creates a new OpenList with the given underlying array. Any change to the array is reflected to the list and
	 * vice-versa. <b>This relation does no longer work if the list grow.</b>
	 *
	 * @param values
	 */
	public OpenList(E... values) {
		this.values = values;
		this.size = values.length;
		this.capacityIncrement = 2;
	}
	
	/**
	 * Creates a new OpenList that contains all the values of the given collection.
	 */
	public OpenList(Collection<E> values) {
		this.capacityIncrement = 2;
		this.values = values.toArray();
	}
	
	/**
	 * Creates a new OpenList that contains all the values of the given collection.
	 */
	public OpenList(int capacityIncrement, E... values) {
		this.values = values;
		this.capacityIncrement = capacityIncrement;
	}
	
	/**
	 * Creates a new OpenList that contains all the values of the given collection.
	 */
	public OpenList(int capacityIncrement, Collection<E> values) {
		this.values = values.toArray();
		this.capacityIncrement = capacityIncrement;
	}
	
	/**
	 * Creates a new OpenList with the given initial capacity and a capacity increment of 2.
	 */
	public OpenList(int cap) {
		this.values = new Object[cap];
		this.capacityIncrement = 2;
	}
	
	/**
	 * Creates a new OpenList with the given initial capacity and capacity increment.
	 */
	public OpenList(int cap, int capacityIncrement) {
		this.values = new Object[cap];
		this.capacityIncrement = capacityIncrement;
	}
	
	/**
	 * Sets the internal array that containsAt the data of this list.
	 *
	 * @param values
	 */
	public void setValues(E... values) {
		this.values = values;
	}
	
	/**
	 * Returns the internal array that containsAt the data of this list. Modifications to this array will change the
	 * list's content directly.
	 *
	 * @return
	 */
	public Object[] getValues() {
		return values;
	}
	
	@Override
	public boolean add(E e) {
		if (size == values.length) {
			values = Arrays.copyOf(values, size + capacityIncrement);
		}
		values[size++] = e;
		return true;
	}
	
	@Override
	public void add(int index, E element) {
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
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = true;
		for (E e : c) {
			changed = changed | add(e);
		}
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
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
	
	@Override
	public void clear() {
		for (int i = 0; i < values.length; i++) {
			values[i] = null;
		}
		size = 0;
	}
	
	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			if (values[i].equals(o)) {
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
		return index > 0 && index < size && values[index] != null;
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
		rangeCheck(index);
		return (E) values[index];
	}
	
	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < values.length; i++) {
			Object v = values[i];
			if (v.equals(o)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new OpenListIterator();
	}
	
	@Override
	public int lastIndexOf(Object o) {
		for (int i = size - 1; i > -1; i--) {
			Object v = values[i];
			if (v.equals(o)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return new OpenListIterator();
	}
	
	private final class OpenListIterator implements ListIterator<E> {
		
		private int cursor;
		
		public OpenListIterator() {
			this.cursor = 0;
		}
		
		public OpenListIterator(int cursor) {
			this.cursor = cursor;
		}
		
		@Override
		public void add(E e) {
			OpenList.this.add(cursor, e);
		}
		
		@Override
		public void set(E e) {
			OpenList.this.set(cursor, e);
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
		public E next() {
			return (E) values[cursor++];
		}
		
		@Override
		public int nextIndex() {
			return cursor;
		}
		
		@Override
		public E previous() {
			return (E) values[--cursor];
		}
		
		@Override
		public int previousIndex() {
			return cursor - 1;
		}
		
		@Override
		public void remove() {
			OpenList.this.remove(cursor);
		}
		
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return new OpenListIterator(index);
	}
	
	@Override
	public boolean remove(Object o) {
		for (int i = 0; i < size; i++) {
			Object value = values[i];
			if (o.equals(value)) {
				remove(i);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public E remove(int index) {
		rangeCheck(index);
		E previous = (E) values[index];
		int n = size - index - 1;
		if (n > 0) {
			System.arraycopy(values, index + 1, values, index, n);
		}
		values[--size] = null;
		return previous;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for (int i = 0; i < size; i++) {
			Object value = values[i];
			if (!contains(value)) {
				changed = true;
				remove(i);
			}
		}
		return changed;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public E set(int index, E element) {
		rangeCheck(index);
		final Object prev = values[index];
		values[index] = element;
		return (E) prev;
	}
	
	/**
	 * Sets the element at the specified position in this list to the specified element. If the values exists it is
	 * replaced. If the index is greater than the list's capacity then the list grows.
	 */
	public void setOrAdd(int index, E element) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		if (index >= values.length) {
			values = Arrays.copyOf(values, index + 1);
		}
		values[index] = element;
	}
	
	/**
	 * Returns the current capacity of the list.
	 */
	public int capacity() {
		return values.length;
	}
	
	/**
	 * Returns the capacity increment of the list.
	 */
	public int getCapacityIncrement() {
		return capacityIncrement;
	}
	
	@Override
	public OpenList<E> subList(int fromIndex, int toIndex) {
		return new OpenList(Arrays.copyOfRange(values, fromIndex, toIndex));
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(values, size);
	}
	
}
