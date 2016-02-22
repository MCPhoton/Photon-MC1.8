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
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class PlayerPositionAndLookPacket extends SendablePacket {

	/**
	 * Defines if values are relative or absolute. A bit 1 indicates a relative value, 0 an absolute
	 * one. In that order: x,y,z,pitch,yaw.
	 */
	public byte relativeFlags;
	public double x, y, z;
	public float yaw, pitch;

	/**
	 * Creates a new PlayerPositionAndLookPacket with the specified parameters.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param yaw
	 * @param pitch
	 * @param relativeFlags Defines which values are relative or absolute. A bit 1 indicates a
	 * relative value, 0 an absolute one. In that order: x,y,z,pitch,yaw.
	 */
	public PlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch, byte relativeFlags) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.relativeFlags = relativeFlags;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeDouble(x);
		dest.writeDouble(y);
		dest.writeDouble(z);
		dest.writeFloat(yaw);
		dest.writeFloat(pitch);
		dest.write(relativeFlags);
	}

	@Override
	public int id() {
		return 0x08;
	}

	@Override
	public int maxDataSize() {
		return 33;
	}

	public void setRelativeFlag(int bit, boolean value) {
		int v = value ? 1 : 0;
		relativeFlags = (byte) (relativeFlags & (v << bit));
	}

}
