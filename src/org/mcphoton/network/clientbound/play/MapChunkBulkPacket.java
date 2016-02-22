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
import java.util.List;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.world.Chunk;
import com.electronwill.collections.OpenList;
import com.electronwill.streams.EasyOutputStream;

/**
 * Sends chunks together for better compression result.
 *
 * @deprecated is useless and will be removed in minecraft 1.9. Use ChunkDataPacket instead.
 * @author ElectronWill
 */
@Deprecated
public final class MapChunkBulkPacket extends SendablePacket {
	
	public boolean skylight;
	public final List<Chunk> chunks;
	
	public MapChunkBulkPacket() {
		this.chunks = new OpenList<>();
	}
	
	public MapChunkBulkPacket(List<Chunk> chunks) {
		this.chunks = chunks;
	}
	
	@Override
	public int id() {
		return 0x26;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public String toString() {
		return "MapChunkBulkPacket - skylight: " + skylight + ", chunk.size(): " + chunks.size();
	}
	
	@Override
	public void writeTo(EasyOutputStream out) throws IOException {
		/*
		out.writeBoolean(skylight);
		out.writeVarInt(chunks.size());
		for (Chunk chunk : chunks) {
			out.writeInt(chunk.x());
			out.writeInt(chunk.z());
			out.writeShort(chunk.sectionsMask());
		}
		for (Chunk chunk : chunks) {
			chunk.writeSectionsTo(out);
			chunk.writeBiomesTo(out);
		}
		*/
	}
	
}
