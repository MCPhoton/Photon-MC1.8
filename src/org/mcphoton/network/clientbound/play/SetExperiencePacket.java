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
public final class SetExperiencePacket extends SendablePacket {

	/**
	 * The progression of the player experience bar.
	 */
	public float xpBar;
	/**
	 * The player's level.
	 */
	public int level;
	/**
	 * The total amount of experience the player has.
	 */
	public int xpTotal;

	public SetExperiencePacket(float xpBar, int level, int xpTotal) {
		this.xpBar = xpBar;
		this.level = level;
		this.xpTotal = xpTotal;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeFloat(xpBar);
		dest.writeVarInt(level);
		dest.writeVarInt(xpTotal);
	}

	@Override
	public int id() {
		return 0x1F;
	}

	@Override
	public int maxDataSize() {
		return 14;
	}

}
