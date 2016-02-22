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
package org.mcphoton.event.impl;

import org.mcphoton.event.PhotonEvent;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;

/**
 * When a packet is received and is going to be processed, just before its {@link ReceivablePacket#handle()} is called.
 *
 * @author ElectronWill
 */
public class PacketReceiveEvent extends PhotonEvent {
	
	private ReceivablePacket packet;
	private final ClientInfos source;
	
	public PacketReceiveEvent(ReceivablePacket packet, ClientInfos conn) {
		this.packet = packet;
		this.source = conn;
	}
	
	/**
	 * Gets the packet received from the client. It will be processed if the event is not cancelled.
	 */
	public ReceivablePacket getPacket() {
		return packet;
	}
	
	/**
	 * Gets the client who sent the packet.
	 */
	public ClientInfos getSource() {
		return source;
	}
	
	/**
	 * Sets the packet that will be processed (if this event is not cancelled).
	 */
	public void setPacket(ReceivablePacket packet) {
		this.packet = packet;
	}
	
}
