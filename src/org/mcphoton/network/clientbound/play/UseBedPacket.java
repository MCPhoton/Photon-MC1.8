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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.Location;
import org.mcphoton.util.ProtocolData;

/**
 * Tells that a player goes to bed. This is sent to all nearby players including of course the one
 * going to bed.
 *
 * @author ElectronWill
 */
public final class UseBedPacket extends SendablePacket {

	/**
	 * The player who's going to bed.
	 */
	public OnlinePlayer player;

	/**
	 * Bed's location.
	 */
	public int x, y, z;

	public UseBedPacket(OnlinePlayer player, int x, int y, int z) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public UseBedPacket(OnlinePlayer player, Location bedLocation) {
		this(player, bedLocation.getBlockX(), bedLocation.getBlockY(), bedLocation.getBlockZ());
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(player.getType().getId());
		dest.writeLong(ProtocolData.encodePosition(x, y, z));
	}

	@Override
	public int id() {
		return 0x0A;
	}

	@Override
	public int maxDataSize() {
		return 13;
	}

}
