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
 * Interface for recipes (crafting recipes, brewing recipes, smelting recipes, etc).
 *
 * @author ElectronWill
 */
public interface Recipe {

	/**
	 * Checks if this recipe works with the given items in that slot order.
	 *
	 * @param items itemsin the order. Depends of the inventory.
	 * @return true if it works, false if it does not corresponds to this recipe.
	 */
	boolean accept(ItemStack[] items);

	/**
	 * Returns the result of this recipe, in the slot order (depends of the inventory).
	 *
	 * @return the result.
	 */
	ItemStack[] getResult();

	/**
	 * Returns the number of ItemStacks needed to make this recipe.
	 *
	 * @return
	 */
	int inputAmount();

}
