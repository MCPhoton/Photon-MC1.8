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

import java.util.AbstractCollection;

/**
 * A reziseable collection based on an array. The delete operation is in constant time because we
 * just move the last element to fill the gap.
 *
 * @author TheElectronWill
 * @param <E>
 */
public abstract class Bag<E> extends AbstractCollection<E> {

	/**
	 * Returns the element at the specified index.
	 *
	 * @param index
	 * @throws ArrayIndexOutOfBoundsException if the specified index is negative or greater than the
	 * size of the bag.
	 * @return
	 */
	public abstract E get(int index);

	/**
	 * Returns the element at the specified index, or null if the index is negative or greater than
	 * the size of the bag.
	 *
	 * @param index
	 * @return
	 */
	public abstract E tryGet(int index);

	/**
	 * Removes the element at the specified index, and moves the last element to this index to "fill
	 * the gap".
	 *
	 * @param index
	 */
	public abstract void remove(int index);

	/**
	 * Trims the capacity of this Bag instance to be the bag's current size. Use this operation to
	 * minimize the storage of an Bag instance.
	 */
	public abstract void trimToSize();

}
