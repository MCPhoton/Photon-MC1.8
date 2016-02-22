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
import org.mcphoton.Difficulty;
import org.mcphoton.Gamemode;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class RespawnPacket extends SendablePacket {

	public static final String DEFAULT = JoinPacket.DEFAULT, FLAT = JoinPacket.FLAT, LARGE_BIOMES = JoinPacket.LARGE_BIOMES, AMPLIFIED = JoinPacket.AMPLIFIED, DEFAULT_1_1 = JoinPacket.DEFAULT_1_1;

	public int dimension;
	public Difficulty difficulty;
	public Gamemode gamemode;//hardcore flag not included in this packet
	public String worldType;//same as JoinPacket

	public RespawnPacket(int dimension, Difficulty difficulty, Gamemode gamemode, String worldType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.worldType = worldType;
	}

	public RespawnPacket(int dimension, Difficulty difficulty, Gamemode gamemode) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.worldType = DEFAULT;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeInt(dimension);
		dest.write(difficulty.ordinal());
		dest.write(gamemode.ordinal());
		dest.writeString(worldType);
	}

	@Override
	public int id() {
		return 0x07;
	}

	@Override
	public int maxDataSize() {
		return 6 + worldType.length() * 4;
	}

}
