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
 * Contains informations about an enchantment that is applied to an item.
 *
 * @author ElectronWill
 */
public final class AppliedEnchantment {

	private final ItemStack item;
	private final int level;
	private final EnchantmentType type;

	public AppliedEnchantment(ItemStack item, EnchantmentType type, int level) {
		this.item = item;
		this.level = level;
		this.type = type;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getLevel() {
		return level;
	}

	public EnchantmentType getType() {
		return type;
	}

}
