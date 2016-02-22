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
import org.mcphoton.entity.Entity;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class SpawnPaintingPacket extends SendablePacket {

	public Entity entity;
	/**
	 * The name of the painting. <b>It can have a length of 13 maxi.</b>
	 */
	public String paintingName;
	public int centerX, centerY, centerZ;
	public byte direction;

//        TODO constructor(PaintingEntity pe)
//
	/**
	 * Create a new SpawnPaintingPacket with the specified parameters. <b>The painting's name can
	 * have a length of 13 maxi.</b>
	 *
	 * @param entity
	 * @param paintingName The name of the painting. <b>It can have a length of 13 maxi.</b>
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param direction
	 */
	public SpawnPaintingPacket(Entity entity, String paintingName, int centerX, int centerY, int centerZ, byte direction) {
		this.entity = entity;
		this.paintingName = paintingName;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.direction = direction;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		if (paintingName.length() > 27) {
			throw new IllegalArgumentException("Painting's name length must be SMALLER than 13!");
		}
		dest.writeVarInt(entity.getType().getId());
		dest.writeString(paintingName);
		dest.writeLong(ProtocolData.encodePosition(centerX, centerY, centerZ));
		dest.write(direction);
	}

	@Override
	public int id() {
		return 0x10;
	}

	@Override
	public int maxDataSize() {
		return 27;
	}

}
