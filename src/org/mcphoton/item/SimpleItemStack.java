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

import com.electronwill.streams.EasyOutputStream;
import java.io.IOException;

/**
 * A simple ItemStack without anything extraordinary. It's just another different type of
 * item, that doesn't do anything special.
 *
 * @author ElectronWill
 */
public final class SimpleItemStack extends ItemStack {

	private final ItemType type;

	public SimpleItemStack(ItemType type) {
		super();
		this.type = type;
	}

	public SimpleItemStack(ItemType type, int amount, short damage) {
		super(amount, damage);
		this.type = type;
	}

	public SimpleItemStack(ItemType type, int amount, short damage, Inventory inventory) {
		super(amount, damage, inventory);
		this.type = type;
	}

	@Override
	public ItemType getType() {
		return type;
	}

	@Override
	public void writeTo(EasyOutputStream out) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
