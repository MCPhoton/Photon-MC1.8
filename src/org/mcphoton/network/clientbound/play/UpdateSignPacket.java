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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class UpdateSignPacket extends SendablePacket {
	
	/**
	 * Location of the sign.
	 */
	public int x, y, z;
	/**
	 * A line of text in the sign.
	 */
	public ChatMessage line1, line2, line3, line4;
	
	public UpdateSignPacket(int x, int y, int z, ChatMessage line1, ChatMessage line2, ChatMessage line3, ChatMessage line4) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.line4 = line4;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeLong(ProtocolData.encodePosition(x, y, z));
		dest.writeString(line1.toString());
		dest.writeString(line2.toString());
		dest.writeString(line3.toString());
		dest.writeString(line4.toString());
	}
	
	@Override
	public int id() {
		return 0x33;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
}
