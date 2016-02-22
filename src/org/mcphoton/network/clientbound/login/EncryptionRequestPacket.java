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
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class EncryptionRequestPacket extends SendablePacket {
	
	/**
	 * Empty since MC 1.7.
	 */
	// public String serverId; Empty since MC 1.7
	public byte[] publicKey, verifyToken;
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		// dest.writeString(serverId);
		dest.writeVarInt(0);// String of size 0 (empty)
		dest.writeVarInt(publicKey.length);
		dest.write(publicKey);
		dest.writeVarInt(verifyToken.length);
		dest.write(verifyToken);
	}
	
	@Override
	public int id() {
		return 1;
	}
	
	@Override
	public int maxDataSize() {
		return publicKey.length + verifyToken.length + 11;
	}
	
}
