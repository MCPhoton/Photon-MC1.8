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
package org.mcphoton.network.serverbound.play;

import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.Events;
import org.mcphoton.event.impl.PlayerMoveEvent;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;
import com.electronwill.streams.EasyInputStream;

/**
 *
 * @author ElectronWill
 */
public final class PlayerPositionPacket extends ReceivablePacket {
	
	public double x, y, z;
	public boolean onGround;
	
	public PlayerPositionPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		x = in.readDouble();
		y = in.readDouble();
		z = in.readDouble();
		onGround = (in.readByte() == 1);
	}
	
	@Override
	public int id() {
		return 0x04;
	}
	
	@Override
	public void handle() {
		OnlinePlayer player = client.getPlayer();
		// TODO notify event's listeners in the executor thread?
		
		// Creates an event
		PlayerMoveEvent event = new PlayerMoveEvent(player, x, y, z);
		Events.notifyListeners(event);
		
		// Updates player position
		if (!event.isCancelled()) {
			player.setX(event.getNewX());
			player.setY(event.getNewY());
			player.setZ(player.getZ());
			player.setPitch(event.getNewPitch());
			player.setYaw(event.getNewYaw());
		}
	}
	
}
