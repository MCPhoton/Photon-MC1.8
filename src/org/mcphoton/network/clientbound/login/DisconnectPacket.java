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
package org.mcphoton.network.clientbound.login;

import java.io.IOException;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class DisconnectPacket extends SendablePacket {
	
	public ChatMessage reason;
	
	public DisconnectPacket(ChatMessage reason) {
		this.reason = reason;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(reason.toString());
	}
	
	@Override
	public int id() {
		return 0;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
}
