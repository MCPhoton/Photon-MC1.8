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
package org.mcphoton.network.serverbound;

import java.io.IOException;
import org.mcphoton.core.Photon;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ConnectionState;
import org.mcphoton.network.ReceivablePacket;
import com.electronwill.streams.EasyInputStream;

/**
 * This packet switches the state of the connection.
 *
 * @author ElectronWill
 */
public final class HandshakePacket extends ReceivablePacket {
	
	public String serverAddress;
	public short serverPort;
	public int clientProtocolVersion, requestedNextState;
	
	public HandshakePacket(ClientInfos connection, EasyInputStream in) throws Throwable {
		super(connection, in);
		clientProtocolVersion = in.readVarInt();
		serverAddress = in.readString();
		serverPort = in.readShort();
		requestedNextState = in.readVarInt();
	}
	
	@Override
	public int id() {
		return 0;
	}
	
	@Override
	public void handle() throws IOException {// Set the connection's state:
		if (requestedNextState == 1) {
			client.setState(ConnectionState.STATUS);
		} else if (requestedNextState == 2) {
			client.setState(ConnectionState.LOGIN);
		} else {
			Photon.log.warning("Client " + client.getAddress().toString() + " requested an invalid state: " + requestedNextState);
		}
	}
	
	/**
	 * Implemented for debugging purposes.
	 */
	@Override
	public String toString() {
		return "Client protocol version: " + clientProtocolVersion + "\nServerAddress: " + serverAddress + "\nserverPort: " + serverPort
				+ "\nnextState: " + requestedNextState;
	}
	
}
