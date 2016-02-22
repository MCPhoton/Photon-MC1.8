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
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class LoginSuccessPacket extends SendablePacket {
	
	public String playerUid, playerName;
	
	public LoginSuccessPacket(OnlinePlayer p) {
		playerUid = p.getAccountId().toString();
		playerName = p.getName();
	}
	
	public LoginSuccessPacket(String playerUuid, String playerName) {
		this.playerUid = playerUuid.toString();
		this.playerName = playerName;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(playerUid);
		dest.writeString(playerName);
	}
	
	@Override
	public int id() {
		return 2;
	}
	
	@Override
	public int maxDataSize() {
		return playerUid.length() + playerName.length() * 4 + 10;// playerUuid is 1 byte per char (US-ASCII), UTF-8 is
																	// max 4 bytes per char, one VarInt => max 5 bytes
	}
	
}
