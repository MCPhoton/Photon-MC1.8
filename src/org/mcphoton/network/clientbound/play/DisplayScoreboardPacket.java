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
public final class DisplayScoreboardPacket extends SendablePacket {

	/**
	 * Possible board's position.
	 */
	public static final byte LIST = 0, SIDEBAR = 1, BELOW_NAME = 2;

	public byte position;
	public String scoreName;

	public DisplayScoreboardPacket(byte position, String scoreName) {
		this.position = position;
		this.scoreName = scoreName;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(position);
		dest.writeString(scoreName);
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int id() {
		return 0x3D;
	}

}
