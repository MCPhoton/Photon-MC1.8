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
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class WindowPropertyPacket extends SendablePacket {

	/**
	 * Furnace property.
	 */
	public static final short FURNACE_PROGRESS = 0, FURNACE_FIRE_ICON = 1;
	/**
	 * Enchantment table property.
	 */
	public static final short LEVEL_REQUIREMENT_TOP = 0, LEVEL_REQUIREMENT_MIDDLE = 1, LEVEL_REQUIREMENT_BOTTOM = 2, TOOLTIP_TOP = 4, TOOLTIP_MIDDLE = 5, TOOLTIP_BOOTOM = 6;
	/**
	 * Beacon property.
	 */
	public static final short POWER_LEVEL = 0, FIRST_POTION_EFFECT = 1, SECOND_POTION_EFFECT = 2;
	/**
	 * Anvil property.
	 */
	public static final short REPAIR_COST = 0;
	/**
	 * Brewing stand property.
	 */
	public static final short BREW_TIME = 0;

	public byte windowId;
	public short property, value;

	public WindowPropertyPacket(byte windowId, short property, short value) {
		this.windowId = windowId;
		this.property = property;
		this.value = value;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(windowId);
		dest.writeShort(property);
		dest.writeShort(value);
	}

	@Override
	public int id() {
		return 0x31;
	}

	@Override
	public int maxDataSize() {
		return 5;
	}

}
