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

import org.mcphoton.event.CancellableEvent;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.SendablePacket;

/**
 * When a packet is going to be sent.
 *
 * @author ElectronWill
 */
public final class PacketSendEvent extends CancellableEvent {
	
	private SendablePacket packet;
	private ClientInfos destination;
	
	public PacketSendEvent(SendablePacket packet, ClientInfos destination) {
		this.packet = packet;
		this.destination = destination;
	}
	
	/**
	 * The client who will receive the packet.
	 */
	public ClientInfos getDestination() {
		return destination;
	}
	
	/**
	 * Returns the packet that will be sent to the client (if this event is not cancelled).
	 *
	 * @return the packet
	 */
	public SendablePacket getPacket() {
		return packet;
	}
	
	/**
	 * Sets the packet that will be sent to the client (if this event is not cancelled).
	 *
	 * @param packet the new packet to send
	 */
	public void setPacket(SendablePacket packet) {
		this.packet = packet;
	}
	
}
