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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.entity.Entity;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;

/**
 * Displays the animation of the item flying towards the player.
 *
 * @author ElectronWill
 */
public final class CollectItemPacket extends SendablePacket {
	
	/**
	 * Id of the entity collecting the item.
	 */
	public int collectedId;
	/**
	 * Id of the item being collected.
	 */
	public int collectorId;
	
	public CollectItemPacket(int collectedId, int collectorId) {
		this.collectedId = collectedId;
		this.collectorId = collectorId;
	}
	
	public CollectItemPacket(OnlinePlayer player, Entity item) {
		this.collectedId = item.getEntityId();
		this.collectorId = player.getEntityId();
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(collectedId);
		dest.writeVarInt(collectorId);
	}
	
	@Override
	public int maxDataSize() {
		return 10;
	}
	
	@Override
	public int id() {
		return 0x0D;
	}
	
}
