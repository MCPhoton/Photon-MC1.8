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
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.mcphoton.entity.impl.OnlinePlayer;

/**
 * Stores informations about a connected client.
 *
 * @author ElectronWill
 */
public final class ClientInfos {
	
	private static final Map<SocketChannel, ClientInfos> INFOS = Collections.synchronizedMap(new HashMap<>());
	
	static ClientInfos get(InetSocketAddress address) throws IOException {
		synchronized (INFOS) {// sync needed because we use the map's iterator
			for (Entry<SocketChannel, ClientInfos> entry : INFOS.entrySet()) {
				SocketChannel sc = entry.getKey();
				if (sc.getRemoteAddress().equals(address)) {
					return entry.getValue();
				}
			}
		}
		return null;
	}
	
	static ClientInfos get(SocketChannel sc) {
		return INFOS.get(sc);
	}
	
	static ClientInfos init(ConnectionState state, SocketChannel channel) throws IOException {
		ClientInfos infos = new ClientInfos(state, channel);
		infos.packetReader = new SimplePacketReader(infos, channel);
		infos.packetWriter = new SimplePacketWriter(infos, channel);
		INFOS.put(channel, infos);
		return infos;
	}
	
	static ClientInfos init(ConnectionState state, SocketChannel channel, PacketReader packetReader, PacketWriter packetWriter)
			throws IOException {
		ClientInfos infos = new ClientInfos(state, channel);
		infos.packetReader = packetReader;
		infos.packetWriter = packetWriter;
		INFOS.put(channel, infos);
		return infos;
	}
	
	static ClientInfos remove(SocketChannel sc) {
		return INFOS.remove(sc);
	}
	
	// Infos/state fields:
	private volatile ConnectionState state;// the current connection state
	private volatile OnlinePlayer player = null;// the corresponding player. May be null!
	private final InetSocketAddress address;// the client's ip address
	private volatile int compressionThreshold = -1;// the size limit above which packets are compressed
	
	// Network IO fields:
	private final SocketChannel channel;// the channel connected to the client
	private volatile PacketReader packetReader;// the packet reader used to read incoming packets
	private volatile PacketWriter packetWriter;// the packet writer used to write outgoing packets
	
	private ClientInfos(ConnectionState state, SocketChannel channel) throws IOException {
		this.state = state;
		this.channel = channel;
		this.address = (InetSocketAddress) channel.getRemoteAddress();
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public ConnectionState getState() {
		return state;
	}
	
	public void setState(ConnectionState state) {
		this.state = state;
	}
	
	PacketReader getPacketReader() {
		return packetReader;
	}
	
	void setPacketReader(PacketReader packetReader) {
		this.packetReader = packetReader;
	}
	
	PacketWriter getPacketWriter() {
		return packetWriter;
	}
	
	void setPacketWriter(PacketWriter packetWriter) {
		this.packetWriter = packetWriter;
	}
	
	public OnlinePlayer getPlayer() {
		return player;
	}
	
	public void setPlayer(OnlinePlayer p) {
		this.player = p;
	}
	
	public boolean isClientOnline() {
		return player != null;
	}
	
	public int getCompressionThreshold() {
		return compressionThreshold;
	}
	
	public void setCompressionThreshold(int compressionThreshold) {
		this.compressionThreshold = compressionThreshold;
	}
	
	SocketChannel getSocketChannel() {
		return channel;
	}
	
}
