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
 * Sent to the client when an object spawns. Objects are things like projectiles, vehicles, falling objects, items, xp
 * bottles or activated tnt. <b>You also need to send the EntityMetadataPacket for the entity</b>.
 *
 * @author ElectronWill
 */
public final class SpawnObjectPacket extends SendablePacket {
	
	/**
	 * An object's type.
	 */
	public static final byte BOAT = 1, ITEM_STACK = 2, MINECART = 10, TNT_ACTIVATED = 50, ENDER_CRYSTAL = 51, ARROW = 60, SNOWBALL = 61,
			EGG = 62, FIREBALL = 63, FIRECHARGE = 64, ENDERPEARL = 65, WITHER_SKULL = 66, FALLING_OBJECT = 70, ITEM_FRAME = 71,
			EYE_ENDER = 72, POTION = 73, DRAGON_EGG = 74, XP_BOTTLE = 75, FIREWORK_ROCKET = 76, LEASH_KNOT = 77, ARMOR_STAND = 78,
			FISHING_FLOAT = 90;
			
	public Entity object;
	/**
	 * Fixed-point coordinate.
	 */
	public int x, y, z;
	public byte pitch, yaw;
	
	public SpawnObjectPacket(Entity object, int x, int y, int z, byte pitch, byte yaw) {
		this.object = object;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws Exception {
		dest.writeVarInt(object.getType().getId());
		dest.write(object.getType().getId());
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(z);
		dest.write(pitch);
		dest.write(yaw);
		MetadataOutputStream mos = new MetadataOutputStream(dest);
		// TODO object.writeMetadataTo(mos);
	}
	
	@Override
	public int id() {
		return 0x0E;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
}
