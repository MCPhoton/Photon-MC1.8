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

/**
 *
 * @author ElectronWill
 */
public final class EntityLookAndRelativeMovePacket extends SendablePacket {

	public Entity e;
	public byte dx, dy, dz, yaw, pitch;
	public boolean onGround;

	public EntityLookAndRelativeMovePacket(Entity e, byte dx, byte dy, byte dz, byte yaw, byte pitch, boolean onGround) {
		this.e = e;
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(e.getType().getId());
		dest.write(dx);
		dest.write(dy);
		dest.write(dz);
		dest.write(yaw);
		dest.write(pitch);
		dest.writeBoolean(onGround);
	}

	@Override
	public int maxDataSize() {
		return 11;
	}

	@Override
	public int id() {
		return 0x17;
	}

}
