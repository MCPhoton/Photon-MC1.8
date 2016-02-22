package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.streams.EasyOutputStream;

public class OverworldChunk extends NoSkylightChunk {
	
	public OverworldChunk(World w, int x, int z) {
		super(w, x, z);
	}
	
	public OverworldChunk(World w, int x, int z, ChunkSection[] sections, byte[] biomes) {
		super(w, x, z, sections, biomes);
		
	}
	
	@Override
	protected void createSection(int sectionIndex) {
		sections[sectionIndex] = new OverworldChunkSection();
	}
	
	@Override
	public int getBlockSkylight(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		synchronized (this) {
			ChunkSection section = sections[sectionIndex];
			if (section == null)
				return 0;
			return section.getBlockSkylight(x, ySection, z);
		}
	}
	
	@Override
	public void setBlockSkylight(int x, int y, int z, int light) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		synchronized (this) {
			ChunkSection section = sections[sectionIndex];
			if (section == null)
				createSection(sectionIndex);
			section.setBlockSkylight(x, ySection, z, light);
		}
	}
	
	@Override
	public boolean hasSkylight() {
		return true;
	}
	
	@Override
	public void writeTo(EasyOutputStream out, boolean writeBiomes) throws IOException {
		out.writeInt(x());
		out.writeInt(z());
		out.writeBoolean(writeBiomes);
		synchronized (this) {
			int length = writeBiomes ? biomes.length : 0;// the data length
			int mask = 0;// the section bitmask
			for (int i = 0; i < 16; i++) {
				if (sections[i] != null) {
					mask |= 1 << i;
					length += 12288;// adds the section's length
				}
			}
			out.writeShort(mask);
			out.writeVarInt(length);
			// Write blocks ids:
			for (int i = 0; i < 16; i++) {
				ChunkSection section = sections[i];
				if (section != null)
					section.writeBlocksIdsTo(out);
			}
			// Write emitted light level:
			for (int i = 0; i < 16; i++) {
				ChunkSection section = sections[i];
				if (section != null)
					section.writeEmittedLightTo(out);
			}
			// Write skylight level:
			for (int i = 0; i < 16; i++) {
				ChunkSection section = sections[i];
				if (section != null)
					section.writeSkylightTo(out);
			}
			// Write biomes:
			if (writeBiomes) {
				out.write(biomes);
			}
		}
	}
	
}
