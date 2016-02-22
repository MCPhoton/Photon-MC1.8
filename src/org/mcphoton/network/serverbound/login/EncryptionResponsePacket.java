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
package org.mcphoton.network.serverbound.login;

import com.electronwill.streams.EasyInputStream;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;

/**
 *
 * @author ElectronWill
 */
public final class EncryptionResponsePacket extends ReceivablePacket {

	public byte[] sharedSecret, verifyToken;

	public EncryptionResponsePacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);

		final int sharedSecretLength = in.readVarInt();
		sharedSecret = new byte[sharedSecretLength];
		in.read(sharedSecret);

		final int verifyTokenLength = in.readVarInt();
		verifyToken = new byte[verifyTokenLength];
		in.read(verifyToken);
	}

	@Override
	public int id() {
		return 1;
	}

	@Override
	public void handle() throws Throwable {
	}

}
