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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.entity.Entity;
import org.mcphoton.item.ItemStack;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class EntityEquipementPacket extends SendablePacket {

	public ItemStack itemStack;
	public short slot;
	public Entity entity;
	/**
	 * The itemStack's data. This is <code>null</code> until explicitely set.
	 */
	public byte[] itemStackData;

	/**
	 * Creates a new EntityEquipementPacket with the specified parameters.
	 *
	 * @param entity the entity to be equipped
	 * @param slot the slot. 0: held, 1: boots, 2: leggings, 3: chestplate, 4: helmet
	 * @param itemStack the ItemStack to be equipped
	 */
	public EntityEquipementPacket(Entity entity, short slot, ItemStack itemStack) {
		this.entity = entity;
		this.slot = slot;
		this.itemStack = itemStack;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(entity.getType().getId());
		dest.write(itemStack.getAmount());
		dest.writeShort(itemStack.getDamage());
		if (itemStackData == null) {
			itemStack.writeTo(dest);
		} else {
			dest.write(itemStackData);
		}

	}

	@Override
	public int maxDataSize() {
		return itemStackData == null ? Integer.MAX_VALUE : itemStackData.length + 9;
	}

	@Override
	public int id() {
		return 0x04;
	}

}
