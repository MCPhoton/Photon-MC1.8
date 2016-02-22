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

/**
 * An interface for wearable items, like pieces of armor.
 *
 * @author ElectronWill
 */
public interface Wearable {

	/**
	 * Returns in what inventory slot this item can be placed. This method is
	 * valid only with
	 * <b>Player inventory</b>.
	 *
	 * @see http://wiki.vg/Inventory#Inventory
	 * @return the inventory slot which this item can be placed into.
	 */
	int getCompatibleSlot();

	/**
	 * Checks if this item may be placed in the given player's inventory slot.
	 *
	 * @param slot player's inventory slot id.
	 * @see http://wiki.vg/Inventory#Inventory
	 * @return true if it may be placed here, or false it if may not.
	 */
	boolean isCompatible(int slot);
}
