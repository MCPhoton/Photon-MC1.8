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
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class EffectPacket extends SendablePacket {

	/**
	 * Sound effect id.
	 */
	public static final int SOUND_CLICK = 1000, SOUND_CLICK_ = 1001, SOUND_BOW = 1002, SOUND_DOOR = 1003, SOUND_FIZZ = 1004, SOUND_DISC = 1005, SOUND_GHAST = 1007, SOUND_FIREBALL = 1008, SOUND_FIREBALL_LOWER = 1009, SOUND_ZOMBIE_WOOD = 1010, SOUND_ZOMBIE_METAL = 1011, SOUND_ZOMBIE_BREAK = 1012, SOUND_WITHER_SPAWN = 1013, SOUND_WITHER_SHOOT = 1014, SOUND_BAT = 1015, SOUND_INFECT = 1016, SOUND_UNFECT = 1017, SOUND_ENDERDRAGON_END = 1018, SOUND_ANVIL_BREAK = 1020, SOUND_ANVIL_USE = 1021, SOUND_ANVIL_LAND = 1022;
	/**
	 * Particle effect id.
	 */
	public static final int PARTICLE_SMOKE = 2000, PARTICLE_BLOCKBREAK = 2001, PARTICLE_SPLASH = 2002, PARTICLE_EYE_OF_ENDER = 2003, PARTICLE_MOB_SPAWN = 2004, PARTICLE_VILLAGER_HAPPY = 2005;
	/**
	 * Smoke's direction.
	 */
	public static final int SOUTH_EAST = 0, SOUTH = 1, SOUTH_WEST = 2, EAST = 3, MIDDLE = 4, WEST = 5, NORTH_EAST = 6, NORTH = 7, NORTH_WEST = 8;

	public int x, y, z;
	public boolean disableRelativeVolume;
	public int effect, data;

	public EffectPacket(int x, int y, int z, boolean disableRelativeVolume, int effect, int data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.disableRelativeVolume = disableRelativeVolume;
		this.effect = effect;
		this.data = data;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeInt(effect);
		dest.writeLong(ProtocolData.encodePosition(x, y, z));
		dest.writeInt(data);
		dest.writeBoolean(disableRelativeVolume);
	}

	@Override
	public int maxDataSize() {
		return 17;
	}

	@Override
	public int id() {
		return 0x28;
	}

}
