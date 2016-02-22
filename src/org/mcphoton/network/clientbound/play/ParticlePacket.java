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
import java.util.Optional;
import org.mcphoton.network.SendablePacket;
import com.electronwill.collections.IntList;

/**
 *
 * @author ElectronWill
 */
public final class ParticlePacket extends SendablePacket {

	/**
	 * The number of particles to create.
	 */
	public int count;
	/**
	 * Depends of the particle: usually it is null.
	 */
	public Optional<IntList> data;
	public boolean longDistance;

	/**
	 * The particle's id.
	 */
	public int particle;
	public float particleData;
	/**
	 * Particle's coordinates.
	 */
	public float x;
	/**
	 * Added <u>by the client</u> to the coordinate after being multiplied by a random number.
	 */
	public float xOffset;
	public float y;
	public float yOffset;
	public float z;
	public float zOffset;

	public ParticlePacket(int particle, int count, boolean longDistance, float x, float y, float z, float xOffset, float yOffset, float zOffset, float particleData, IntList data) {
		this.particle = particle;
		this.count = count;
		this.longDistance = longDistance;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.particleData = particleData;
		this.data = Optional.of(data);
	}

	public ParticlePacket(int particle, int count, boolean longDistance, float x, float y, float z, float xOffset, float yOffset, float zOffset, float particleData) {
		this.particle = particle;
		this.count = count;
		this.longDistance = longDistance;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.particleData = particleData;
		this.data = Optional.empty();
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeInt(particle);
		dest.writeBoolean(longDistance);
		dest.writeFloat(x);
		dest.writeFloat(y);
		dest.writeFloat(z);
		dest.writeFloat(xOffset);
		dest.writeFloat(yOffset);
		dest.writeFloat(zOffset);
		dest.writeFloat(particleData);
		dest.writeInt(count);
		if (data.isPresent()) {
			IntList il = data.get();
			int[] ints = il.getValues();
			for (int i = 0; i < il.size(); i++) {
				dest.writeVarInt(ints[i]);
			}
		}
	}

	@Override
	public int id() {
		return 0x2A;
	}

	@Override
	public int maxDataSize() {
		return 39;
	}

}
