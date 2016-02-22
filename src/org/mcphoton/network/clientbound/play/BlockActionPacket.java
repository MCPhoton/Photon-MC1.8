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
 * @see http://wiki.vg/Protocol#Block_Action, http://wiki.vg/Block_Actions
 * @author ElectronWill
 */
public final class BlockActionPacket extends SendablePacket {

	/**
	 * @see http://wiki.vg/Block_Actions
	 */
	public static final byte HARP = 0, DOUBLE_BASS = 1, SNARE_DRUM = 2, CLICKS = 3, BASS_DRUM = 4, DOWN = 0, UP = 1, SOUTH = 2, WEST = 3, NORTH = 4, EAST = 5, PISTON_PUSHING = 0, PISTON_PULLING = 1, CHEST_CLOSED = 0, CHEST_OPEN = 1;

	public int x, y, z;
	public byte b1, b2;
	public int blockType;

	public BlockActionPacket(int x, int y, int z, byte b1, byte b2, int blockType) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.b1 = b1;
		this.b2 = b2;
		this.blockType = blockType;
	}

	@Override
	public int maxDataSize() {
		return 15;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeLong(ProtocolData.encodePosition(x, y, z));
		dest.write(b1);
		dest.write(b2);
		dest.writeVarInt(blockType);
	}

	@Override
	public int id() {
		return 0x24;
	}

}
