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
package org.mcphoton.network.serverbound.play;

import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;
import org.mcphoton.util.ProtocolData;
import com.electronwill.streams.EasyInputStream;

/**
 *
 * @author ElectronWill
 */
public final class PlayerDiggingPacket extends ReceivablePacket {
	
	/**
	 * Possible status.
	 */
	public static final byte START_DIGGING = 0, CANCEL_DIGGING = 1, FINISH_DIGGING = 2, DROP_ITEM_STACK = 3, DROP_ITEM = 4;
	/**
	 * Status sent when the player finishes eating or when he/she shoots an arrow.
	 */
	public static final byte FINISH_USE = 5;
	
	/**
	 * Possible face.
	 */
	public static final byte FACE_MINUS_Y = 0, FACE_PLUS_Y = 1, FACE_MINUS_Z = 2, FACE_PLUS_Z = 3, FACE_MINUS_X = 4, FACE_PLUS_X = 5;
	
	public byte status, face;
	public int blockX, blockY, blockZ;
	
	public PlayerDiggingPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		status = in.readByte();
		final long pos = in.readLong();
		blockX = ProtocolData.decodePositionX(pos);
		blockY = ProtocolData.decodePositionY(pos);
		blockZ = ProtocolData.decodePositionZ(pos);
		face = in.readByte();
	}
	
	@Override
	public int id() {
		return 0x07;
	}
	
	@Override
	public void handle() {
		// TODO update Block
		// TODO notify players
	}
	
}
