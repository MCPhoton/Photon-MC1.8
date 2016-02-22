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

import java.io.IOException;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.world.Chunk;
import com.electronwill.streams.EasyOutputStream;

/**
 * Sends one chunk to the player.
 *
 * @author ElectronWill
 */
public final class ChunkDataPacket extends SendablePacket {
	
	public boolean sendBiomes;// also called "ground-up continuous" but it really means if we send the biomes or not
	public Chunk chunk;
	
	public ChunkDataPacket(Chunk chunk, boolean sendBiomes) {
		this.chunk = chunk;
		this.sendBiomes = sendBiomes;
	}
	
	@Override
	public void writeTo(EasyOutputStream out) throws IOException {
		chunk.writeTo(out, sendBiomes);
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int id() {
		return 0x21;
	}
	
	@Override
	public String toString() {
		return "ChunkDataPacket: x=" + chunk.x() + ", z=" + chunk.z() + ", sendBiomes=" + sendBiomes;
	}
	
}
