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
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A map implementations that maps integers to values. Values are stored in an array that grows when necessary. IndexMap
 * is a good choice if there are very few gap between keys. It is for example used in the
 * {@link org.photon.packets.PacketRegistry} .
 * <p>
 * About null values: IndexMap does not handle null values. A null value is considered as "no value set". Moreover,
 * putting a <code>null</code> value with <code>put(index, null)</code> does absolutely nothing.
 * </p>
 * <p>
 * <b>This class is NOT Thread-safe!</b>
 * </p>
 *
 * @author TheElectronWill
 * @param <E>
 */
public final class IndexMap<E> extends AbstractMap<Integer, E> {
	
	/**
	 * The array that contains values. Indexes are the keys.
	 */
	private Object[] array;
	
	/**
	 * The number of values in the map.
	 */
	private int size;
	private Collection<E> collection;
	
	/**
	 * Creates a new IndexMap with an initial capacity of 10.
	 */
	public IndexMap() {
		this.array = new Object[10];
	}
	
	/**
	 * Creates a new IndexMap with the given initial capacity.
	 *
	 * @param initialCapacity
	 */
	public IndexMap(int initialCapacity) {
		this.array = new Object[initialCapacity];
	}
	
	/**
	 * Creates a new IndexMap with the given underlying array. Any change to this array is reflected in the map and
	 * vice-versa.
	 *
	 * @param array
	 */
	public IndexMap(Object[] array) {
		this.array = array;
	}
	
	/**
	 * Creates a new IntArrayMap that contains the keys-values pairs of the given map.
	 *
	 * @param map
	 */
	public IndexMap(Map<Integer, E> map) {
		this.array = new Object[map.size()];
		putAll(map);
	}
	
	/**
	 * Returns the underlying array that contains this map's values. Any change to the array is reflected in the map and
	 * vice-versa.
	 *
	 * @return the underlying array
	 */
	public Object[] array() {
		return array;
	}
	
	@Override
	public void clear() {
		Arrays.fill(array, null);
		size = 0;
	}
	
	/**
	 * Returns true if this map contains a mapping for the specified key. There can be at most one mapping per key.
	 *
	 * @param key the key
	 * @return true if this Map contains a mapping for that key, false if it does not.
	 */
	public boolean containsKey(int key) {
		return key > -1 && key < array.length && array[key] != null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (key instanceof Integer) {
			Integer i = (Integer) key;
			return array.length < i && array[i] != null;
		}
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for (Object o : array) {
			if (o.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public EntrySet entrySet() {
		return new EntrySet();
	}
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 *
	 * @param key the key
	 * @return the associated value, or null if there is no mapping for that key.
	 */
	@Override
	public E get(Object key) {
		if (key instanceof Integer) {
			Integer i = (Integer) key;
			return i < array.length ? (E) array[i] : null;
		}
		return null;
	}
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 *
	 * @param key the key
	 * @return the associated value, or null if there is no mapping for that key.
	 */
	public E get(int key) {
		return key < array.length ? (E) array[key] : null;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public E getOrDefault(Object key, E defaultValue) {
		if (key instanceof Integer) {
			Integer i = (Integer) key;
			return i < array.length ? (E) array[i] : defaultValue;
		}
		return defaultValue;
	}
	
	/**
	 * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the
	 * key.
	 *
	 * @param key the key
	 * @param defaultValue the value to be returned if this map contains no mapping forthe key
	 * @return the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the
	 *         key
	 */
	public E getOrDefault(int key, E defaultValue) {
		return key < array.length ? (E) array[key] : defaultValue;
	}
	
	/**
	 * If the specified key is not already associated with a value associates it with the given value and returns null,
	 * else returns the current value.
	 *
	 * @param key the key
	 * @param value value to be associated with the specified key
	 * @return
	 * @returnthe previous value associated with the specified key, or null if there was no mapping for the key.
	 */
	@Override
	public E putIfAbsent(Integer key, E value) {
		return putIfAbsent(key.intValue(), value);
	}
	
	/**
	 * If the specified key is not already associated with a value associates it with the given value and returns null,
	 * else returns the current value.
	 *
	 * @param key the key
	 * @param value value to be associated with the specified key
	 * @return
	 * @returnthe previous value associated with the specified key, or null if there was no mapping for the key.
	 */
	public E putIfAbsent(int key, E value) {
		if (value == null) {
			return get(key);
		}
		E prev;
		if (array.length < key) {
			array = Arrays.copyOf(array, key + 1);
			prev = null;
		} else {
			prev = (E) array[key];
		}
		if (prev == null) {
			array[key] = value;
			size++;
		}
		return prev;
	}
	
	@Override
	public boolean replace(Integer key, E oldValue, E newValue) {
		return replace(key.intValue(), oldValue, newValue);
	}
	
	/**
	 * Replaces the entry for the specified key only if currently mapped to the specified value.
	 *
	 * @param key the key
	 * @param oldValue value expected to be associated with the specified key
	 * @param newValue value to be associated with the specified key
	 * @return true if the value was replaced
	 */
	public boolean replace(int key, E oldValue, E newValue) {
		if (newValue == null) {
			return false;
		}
		if (array.length < key) {
			return false;
		}
		E prev = (E) array[key];
		if (prev.equals(oldValue)) {
			array[key] = newValue;
			size++;
			return true;
		}
		return false;
	}
	
	/**
	 * Replaces the entry for the specified key only if it is currently mapped to some value.
	 *
	 * @param key the key
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping for the key.
	 */
	@Override
	public E replace(Integer key, E value) {
		return replace(key.intValue(), value);
	}
	
	/**
	 * Replaces the entry for the specified key only if it is currently mapped to some value.
	 *
	 * @param key the key
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping for the key.
	 */
	public E replace(int key, E value) {
		if (value == null) {
			return get(key);
		}
		if (array.length < key) {
			return null;
		}
		E prev = (E) array[key];
		if (prev != null) {
			array[key] = value;
			size++;
		}
		return prev;
	}
	
	@Override
	public Collection<E> values() {
		if (collection == null) {
			collection = new AbstractCollection<E>() {
				@Override
				public void clear() {
					IndexMap.this.clear();
				}
				
				@Override
				public boolean contains(Object v) {
					return IndexMap.this.containsValue(v);
				}
				
				@Override
				public boolean isEmpty() {
					return IndexMap.this.isEmpty();
				}
				
				@Override
				public Iterator<E> iterator() {
					return new ValueIterator();
				}
				
				@Override
				public int size() {
					return IndexMap.this.size();
				}
			};
		}
		return collection;
	}
	
	/**
	 * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
	 * the key, the old value is replaced by the specified value.
	 *
	 * @param key the key
	 * @param value the associated value
	 * @return the previous value if there is one, or null if there is none.
	 */
	@Override
	public E put(Integer key, E value) {
		if (value == null) {
			return get(key);
		}
		final E prev;
		if (key < array.length) {
			prev = (E) array[key];
		} else {
			array = Arrays.copyOf(array, key + 1);
			prev = null;
			size++;
		}
		array[key] = value;
		return prev;
	}
	
	/**
	 * Removes the mapping for a key from this map if it is present.
	 *
	 * Returns the value to which this map previously associated the key, or null if the map contained no mapping for
	 * the key.
	 *
	 * @param key the key
	 * @return the previous value if there is one or null if there is none.
	 */
	@Override
	public E remove(Object key) {
		if (key instanceof Integer) {
			Integer i = (Integer) key;
			E prev;
			if (array.length < i) {
				return null;
			}
			prev = (E) array[i];
			if (prev != null) {
				size--;
				array[i] = null;
			}
			return prev;
		}
		return null;
	}
	
	/**
	 * Removes the mapping for a key from this map if it is present.
	 *
	 * Returns the value to which this map previously associated the key, or null if the map contained no mapping for
	 * the key.
	 *
	 * @param key the key
	 * @return the previous value if there is one or null if there is none.
	 */
	public E remove(int key) {
		E prev;
		if (array.length < key) {
			return null;
		}
		prev = (E) array[key];
		if (prev != null) {
			size--;
			array[key] = null;
		}
		return prev;
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		if (key instanceof Integer) {
			Integer i = (Integer) key;
			remove(i.intValue(), value);
		}
		return false;
	}
	
	/**
	 * Removes the entry for the specified key only if it is currently mapped to the specified value.
	 *
	 * @param key the key
	 * @param value the associated value
	 * @return true if it was removed, false if it doesn't exist.
	 */
	public boolean remove(int key, Object value) {
		if (value == null) {
			return false;
		}
		E prev;
		if (array.length < key) {
			return false;
		}
		prev = (E) array[key];
		if (prev.equals(value)) {
			size--;
			array[key] = null;
			return true;
		}
		return false;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	public final class EntryIterator implements Iterator<Entry<Integer, E>> {
		
		private int cursor = -1;
		private IntEntry next;
		
		@Override
		public boolean hasNext() {
			while (cursor < array.length) {
				Object value = array[++cursor];
				if (value != null) {
					next = new IntEntry(cursor);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public IntEntry next() {
			return next;
		}
		
	}
	
	public final class ValueIterator implements Iterator<E> {
		
		private int cursor = -1;
		private E next;
		
		@Override
		public boolean hasNext() {
			while (cursor < array.length) {
				Object value = array[++cursor];
				if (value != null) {
					next = (E) value;
					return true;
				}
			}
			return false;
		}
		
		@Override
		public E next() {
			return next;
		}
		
	}
	
	public final class EntrySet extends AbstractSet<Entry<Integer, E>> {
		
		public EntrySet() {}
		
		@Override
		public EntryIterator iterator() {
			return new EntryIterator();
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (o instanceof Entry) {
				Entry<Integer, E> entry = (Entry) o;
				return IndexMap.this.get(entry.getKey()) == entry.getValue();
			}
			return false;
		}
		
		@Override
		public boolean add(Entry<Integer, E> e) {
			put(e.getKey(), e.getValue());
			return true;
		}
		
		@Override
		public boolean remove(Object o) {
			if (o instanceof Entry) {
				Entry<Integer, E> entry = (Entry) o;
				return IndexMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
	}
	
	public final class IntEntry implements Entry<Integer, E> {
		
		private final int key;
		
		public IntEntry(final int key) {
			this.key = key;
		}
		
		public int key() {
			return key;
		}
		
		@Override
		public Integer getKey() {
			return key;
		}
		
		@Override
		public E getValue() {
			return (E) array[key];
		}
		
		@Override
		public E setValue(E value) {
			return put(key, value);
		}
		
	}
	
}
