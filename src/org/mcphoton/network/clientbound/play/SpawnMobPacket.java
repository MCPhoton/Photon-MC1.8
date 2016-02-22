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

import org.mcphoton.entity.Entity;
import org.mcphoton.entity.MetadataOutputStream;
import org.mcphoton.network.SendablePacket;

/**
 * Sent when a mob spawns.
 *
 * @author ElectronWill
 */
public final class SpawnMobPacket extends SendablePacket {
	
	public Entity mob;
	/**
	 * Fixed-point coordinate.
	 */
	public int x, y, z;
	public short vx, vy, vz;
	public byte pitch, yaw, headPitch;
	
	public SpawnMobPacket(Entity mob, int x, int y, int z, byte pitch, byte yaw, byte headPitch) {
		this.mob = mob;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
		this.headPitch = headPitch;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws Exception {
		dest.writeVarInt(mob.getEntityId());
		dest.write(mob.getType().getId());
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(z);
		dest.write(yaw);
		dest.write(pitch);
		dest.write(headPitch);// CAUTION: pitch and yaw are not sent in the same order as in the SpawnObjectPacket!!
		dest.writeShort(vx);
		dest.writeShort(vy);
		dest.writeShort(vz);
		MetadataOutputStream mos = new MetadataOutputStream(dest);
		// TODO mob.writeMetadataTo(mos);
	}
	
	@Override
	public int id() {
		return 0x0F;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
}
