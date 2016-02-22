/*
 * Copyright (C) 2015 ElectronWill
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
package org.mcphoton;

/**
 * Defines gamemodes.
 *
 * @author ElectronWill
 */
public enum Gamemode {
	
	/**
	 * Survival mode.
	 */
	SURVIVAL(1),
	/**
	 * Creative mode.
	 */
	CREATIVE(2),
	/**
	 * Adventure mode.
	 */
	ADVENTURE(3),
	/**
	 * Spectator mode.
	 */
	SPECTATOR(4),
	/**
	 * Hardcore mode.
	 */
	HARDCORE(9);// equals to survival.withHardcore()
	
	public final byte id;
	
	private Gamemode(int id) {
		this.id = (byte) id;
	}
	
	/**
	 * Gets the id of this mode with the hardcore flag.
	 *
	 * @see org.photon.packets.clientbound.PacketJoin
	 * @deprecated it's a nonsense that the hardcore flag is available for mode other than survival!
	 */
	@Deprecated
	public int withHardcore() {
		return ordinal() | 0x8;
	}
	
}
