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
import com.electronwill.collections.OpenList;

/**
 *
 * @author ElectronWill
 */
public final class ExplosionPacket extends SendablePacket {

	/**
	 * Explosion's center coordinates.
	 */
	public float x, y, z;
	public float radius;
	/**
	 * Velocity of the player being pushed by the explosion.
	 */
	public float pvx, pvy, pvz;
	public OpenList<ExplosionRecord> records;

	public static final class ExplosionRecord {

		/**
		 * Relative to the explosion's center.
		 */
		public byte xOffset, yOffset, zOffset;

		public ExplosionRecord(byte xOffset, byte yOffset, byte zOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.zOffset = zOffset;
		}

	}

	public void addRecord(int x, int y, int z) {
		ExplosionRecord record = new ExplosionRecord((byte) x, (byte) y, (byte) z);
		records.add(record);
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		final int count = records.size();
		dest.writeFloat(x);
		dest.writeFloat(y);
		dest.writeFloat(z);
		dest.writeFloat(radius);
		dest.writeInt(count);
		for (ExplosionRecord record : records) {
			dest.write(record.xOffset);
			dest.write(record.yOffset);
			dest.write(record.zOffset);
		}
		dest.writeFloat(pvx);
		dest.writeFloat(pvy);
		dest.writeFloat(pvz);
	}

	@Override
	public int maxDataSize() {
		return 32 + 3 * records.size();
	}

	@Override
	public int id() {
		return 0x27;
	}

}
