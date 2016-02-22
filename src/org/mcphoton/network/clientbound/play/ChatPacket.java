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

/**
 *
 * @author ElectronWill
 */
public final class ChatPacket extends SendablePacket {
	
	public String message;
	public byte position;
	
	/**
	 * Creates a new ChatPacket. The message will be displayed in the chat.
	 *
	 * @param msg
	 */
	public ChatPacket(ChatMessage msg) {
		this(msg.toString(), (byte) 0);
	}
	
	/**
	 * Creates a new ChatPacket. The message will be displayed at the specified position.<br>
	 * The position "2" seems not to accept JSON formatted message, but only old style formatting.
	 *
	 * @param message
	 * @param position where the message will be displayed. 0 is normal chat, 1 is system message, 2 is above the action
	 *        bar. 0 and 1 are displayed in the chat box.
	 */
	public ChatPacket(String message, byte position) {
		this.message = message;
		this.position = position;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(message);
		dest.write(position);
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int id() {
		return 0x02;
	}
	
}
