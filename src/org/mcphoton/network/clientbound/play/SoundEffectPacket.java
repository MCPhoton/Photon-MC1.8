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
public final class SoundEffectPacket extends SendablePacket {

	public String soundName;
	public int x, y, z;
	public float volume;
	public byte pitch;

	public SoundEffectPacket(String soundName, int x, int y, int z, float volume, byte pitch) {
		this.soundName = soundName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(soundName);
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(z);
		dest.writeFloat(volume);
		dest.write(pitch);
	}

	@Override
	public int id() {
		return 0x29;
	}

	@Override
	public int maxDataSize() {
		return soundName.length() + 15;
	}

}
