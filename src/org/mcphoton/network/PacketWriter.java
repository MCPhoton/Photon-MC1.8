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

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Writes packets to a SocketChannel associated to a client.
 *
 * @author ElectronWill
 */
public abstract class PacketWriter {
	
	protected final ClientInfos client;
	protected final SocketChannel channel;// Channel connected to the client
	
	public PacketWriter(ClientInfos client, SocketChannel channel) {
		this.client = client;
		this.channel = channel;
	}
	
	/**
	 * Writes the packet's data to the Writer's internal storage.<br>
	 * Note: You should prefer the {@link #writeNow(SendablePacket)} method for better performance with not too large
	 * packets.
	 */
	public abstract void write(SendablePacket packet) throws Exception;
	
	/**
	 * Writes the packet's data to the Writer's internal storage. When the packet sending will be completed (by the
	 * {@link #flush()} method), i.e. after all its data have been written to the SocketChannel, the given runnable will
	 * be run.<br>
	 * Note: You should prefer the {@link #writeNow(SendablePacket, Runnable)} method for better performance with not
	 * too large packets.
	 * 
	 * @param packet the packet to send
	 * @param onSendingComplete the runnable to run just after the packet's sending is completed
	 */
	public abstract void write(SendablePacket packet, Runnable onSendingComplete) throws Exception;
	
	/**
	 * Tries to write the packet's data to the SocketChannel immediatly, if possible. If it's not possible, writes it to
	 * the Writer's internal storage.
	 * 
	 * @return true if the packet was completely written to the SocketChannel, false otherwise
	 */
	public abstract boolean writeNow(SendablePacket packet) throws Exception;
	
	/**
	 * Tries to write the packet's data to the SocketChannel immediatly, if possible. If it's not possible, writes it to
	 * the Writer's internal storage. When the packet sending will be completed (by this method or by {@link #flush()}),
	 * i.e. after all its data have been written to the SocketChannel, the given runnable will be run.
	 * 
	 * @param packet the packet to send
	 * @param onSendingComplete the runnable to run just after the packet's sending is completed
	 * @return true if the packet was completely written to the SocketChannel, false otherwise
	 */
	public abstract boolean writeNow(SendablePacket packet, Runnable onSendingComplete) throws Exception;
	
	/**
	 * Flushes the data stored by the Writer. This method MAY not flush everything in a row, in which case it returns
	 * false. If the channel is closed, it returns false too.
	 * 
	 * @return true if all the data is written, false otherwise
	 * @throws IOException
	 */
	public abstract boolean flush() throws IOException;
	
}
