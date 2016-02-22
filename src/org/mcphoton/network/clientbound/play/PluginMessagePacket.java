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
public final class PluginMessagePacket extends SendablePacket {

	public String channel;
	public byte[] data;
	public int off, len;

	public PluginMessagePacket(String channel, byte[] data) {
		this(channel, data, 0, data.length);
	}

	public PluginMessagePacket(String channel, byte[] data, int off, int len) {
		this.channel = channel;
		this.data = data;
		this.off = off;
		this.len = len;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(channel);
		dest.write(data, off, len);
	}

	@Override
	public int id() {
		return 0x3F;
	}

}
