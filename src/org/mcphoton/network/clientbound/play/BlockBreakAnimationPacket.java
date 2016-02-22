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
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.ProtocolData;

/**
 * You can use this to make a break animation appear on ANY block including air block!
 *
 * @author ElectronWill
 */
public final class BlockBreakAnimationPacket extends SendablePacket {
	
	public Entity entity;
	public int x, y, z;
	/**
	 * The destruction's stage, between 0 and 9.
	 */
	public byte destructionStage;
	
	public BlockBreakAnimationPacket(Entity entity, int x, int y, int z, byte destructionStage) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.destructionStage = destructionStage;
	}
	
	@Override
	public int maxDataSize() {
		return 14;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(entity.getType().getId());
		long ePos = ProtocolData.encodePosition(x, y, z);
		dest.writeLong(ePos);
		dest.write(destructionStage);
	}
	
	@Override
	public int id() {
		return 0x25;
	}
	
}
