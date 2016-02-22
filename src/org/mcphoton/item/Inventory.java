/*
 * Copyright (C) 2015 ElectronWill
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
package org.mcphoton.item;

import java.util.List;

/**
 * Interface for any inventory (things that can store stacks of items).
 *
 * @author ElectronWill
 */
public interface Inventory extends List<ItemStack> {

	/**
	 * Returns the ItemStack that is currently in the given slot of this
	 * inventory.
	 *
	 * @param slot inventory's slot number
	 * @return the ItemStack, or null if this slot is empty.
	 */
	@Override
	ItemStack get(int slot);

	/**
	 * Puts the ItemStack in the given slot of this inventory. If the ItemStack
	 * is null then any ItemStack currently in this slot is removed (i.e.
	 * completely deleted).
	 *
	 * @param slot inventory's slot number
	 * @param itemStack the ItemStack. Can be null
	 */
	@Override
	ItemStack set(int slot, ItemStack itemStack);

	/**
	 * Returns the capacity (i.e. the number of slots) of this inventory.
	 *
	 * @return the capacity of this inventory.
	 */
	int getCapacity();

	/**
	 * Checks if this Inventory is full, i.e. if it's impossible to add more items.
	 *
	 * @return true if it full, false otherwise
	 */
	boolean isFull();

}
