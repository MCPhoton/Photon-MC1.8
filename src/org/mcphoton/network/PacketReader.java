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
package org.mcphoton.network;

import java.nio.channels.SocketChannel;
import org.mcphoton.event.impl.PacketReceiveEvent;

/**
 * A PacketReader is used to abstract how packets are read by the server. Implementations may for example uncompress the
 * data if necessary.
 *
 * @author ElectronWill
 */
public abstract class PacketReader {
	
	protected final ClientInfos client;
	protected final SocketChannel channel;// Channel connected to the client
	protected volatile boolean eos = false;
	
	public PacketReader(ClientInfos client, SocketChannel channel) {
		this.client = client;
		this.channel = channel;
	}
	
	/**
	 * Checks if the PacketReader has reached the end of the stream.
	 * 
	 * @return true if it has reached the end of stream, false otherwise
	 */
	public boolean isEndOfStream() {
		return eos;
	}
	
	/**
	 * Reads the next available packet.
	 * 
	 * @return the next packet, or null if no packet is available for now
	 */
	public abstract ReceivablePacket readNext() throws Exception;
	
	/**
	 * Reads the next available packet and returns the corresponding event.
	 * 
	 * @return the event corresponding to the next packet, or null if no packet is available for now
	 * @throws Exception
	 */
	public PacketReceiveEvent readNextEvent() throws Exception {
		ReceivablePacket packet = readNext();
		if (packet == null)
			return null;
		return new PacketReceiveEvent(packet, client);
	}
	
}
