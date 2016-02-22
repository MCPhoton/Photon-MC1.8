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

import java.util.UUID;
import org.mcphoton.entity.MetadataOutputStream;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class SpawnPlayerPacket extends SendablePacket {
	
	private final OnlinePlayer player;
	/**
	 * Coordinates. Will be converted in fixed point.
	 */
	public double x, y, z;
	/**
	 * A rotation angle. Will be converted in steps of 1/256 of a full turn.
	 */
	public float yaw, pitch;
	/**
	 * The item the player is currently holding. 0 means "no item".
	 */
	public short currentItem;
	/**
	 * Modified player's metadata. Providing one is OPTIONAL.
	 */
	public byte[] metadata = null;
	
	public SpawnPlayerPacket(OnlinePlayer p, short currentItem) {
		this.player = p;
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
		this.yaw = p.getYaw();
		this.pitch = p.getPitch();
		this.currentItem = currentItem;
	}
	
	public SpawnPlayerPacket(OnlinePlayer player, double x, double y, double z, byte yaw, byte pitch, short currentItem) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.currentItem = currentItem;
	}
	
	public SpawnPlayerPacket(OnlinePlayer player, double x, double y, double z, byte yaw, byte pitch, short currentItem, byte[] metadata) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.currentItem = currentItem;
		this.metadata = metadata;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws Exception {
		dest.writeVarInt(player.getType().getId());
		UUID uid = player.getAccountId();
		dest.writeLong(uid.getMostSignificantBits());
		dest.writeLong(uid.getLeastSignificantBits());
		dest.writeInt(ProtocolData.toFixedPoint(x));
		dest.writeInt(ProtocolData.toFixedPoint(y));
		dest.writeInt(ProtocolData.toFixedPoint(z));
		dest.write(ProtocolData.toRotationStep(yaw));
		dest.write(ProtocolData.toRotationStep(pitch));
		dest.writeShort(currentItem);
		if (metadata == null) {
			MetadataOutputStream mos = new MetadataOutputStream(dest);
			// TODO player.writeMetadataTo(mos);
		} else {
			dest.write(metadata);
		}
	}
	
	@Override
	public int id() {
		return 0x0C;
	}
	
	@Override
	public int maxDataSize() {
		return metadata == null ? Integer.MAX_VALUE : metadata.length + 39;
	}
	
}
