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
import java.util.List;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.Events;
import org.mcphoton.event.impl.PacketSendEvent;
import com.electronwill.streams.EasyOutputStream;

/**
 * A packet that can be sent by the server. As packets are created and sent "on demand" in one Thread this class don't
 * need to be Thread-safe.
 *
 * @author ElectronWill
 */
public abstract class SendablePacket extends MCPacket {
	
	/**
	 * Returns the highest possible size of the packet's data. If the maximum is unkown, returns
	 * <code>Integer.MAX_VALUE</code>
	 */
	public abstract int maxDataSize();
	
	public final void sendTo(ClientInfos client) throws IOException {
		PacketSendEvent event = new PacketSendEvent(this, client);
		Events.notifyListeners(event);
		if (!event.isCancelled()) {
			PhotonPacketSender.sendData(event.getPacket(), event.getDestination());
		}
	}
	
	public final void sendTo(ClientInfos client, Runnable onSendingComplete) throws IOException {
		PacketSendEvent event = new PacketSendEvent(this, client);
		Events.notifyListeners(event);
		if (!event.isCancelled()) {
			PhotonPacketSender.sendData(event.getPacket(), event.getDestination(), onSendingComplete);
		}
	}
	
	public final void sendTo(List<ClientInfos> clients) throws IOException {
		for (ClientInfos client : clients) {
			sendTo(client);
		}
	}
	
	public final void sendTo(OnlinePlayer player) throws IOException {
		sendTo(player.getClientInfos());
	}
	
	public final void sendTo(OnlinePlayer player, Runnable onSendingComplete) throws IOException {
		sendTo(player.getClientInfos(), onSendingComplete);
	}
	
	/**
	 * Writes the data of this packet to the specified output. Implementations do NOT need to care about the size of the
	 * Stream, it is handled by the PacketWriter based on {@link #maxDataSize()}.
	 * <p>
	 * <b>The packet's data does NOT contains the length of the data NOR the packet's id.</b>
	 * </p>
	 */
	public abstract void writeTo(EasyOutputStream out) throws Exception;
	
}
