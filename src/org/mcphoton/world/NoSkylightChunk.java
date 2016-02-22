package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.streams.EasyOutputStream;

public class NoSkylightChunk extends Chunk {
	
	protected final ChunkSection[] sections;
	protected final byte[] biomes;
	
	public NoSkylightChunk(World w, int x, int z, ChunkSection[] sections, byte[] biomes) {
		super(w, x, z);
		this.sections = sections;
		this.biomes = biomes;
	}
	
	public NoSkylightChunk(World w, int x, int z) {
		super(w, x, z);
		this.sections = new ChunkSection[16];
		this.biomes = new byte[256];
	}
	
	@Override
	public boolean hasSkylight() {
		return false;
	}
	
	/**
	 * Creates a new section at the given index. This must be called in a synchronized block.
	 */
	protected void createSection(int sectionIndex) {
		sections[sectionIndex] = new NoSkylightChunkSection();
	}
	
	@Override
	public int getBlockId(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				return 0;
			return section.getBlockId(x, ySection, z);
		}
	}
	
	@Override
	public void setBlockId(int x, int y, int z, int id) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				createSection(sectionIndex);
			section.setBlockId(x, ySection, z, id);
		}
	}
	
	@Override
	public int getBlockEmittedLight(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				return 0;
			return section.getBlockEmittedLight(x, ySection, z);
		}
	}
	
	@Override
	public void setBlockEmittedLight(int x, int y, int z, int light) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				createSection(sectionIndex);
			section.setBlockEmittedLight(x, ySection, z, light);
		}
	}
	
	@Override
	public int getBlockSkylight(int x, int y, int z) {
		return 0;
	}
	
	@Override
	public void setBlockSkylight(int x, int y, int z, int light) {}
	
	@Override
	public BlockEntity getBlockEntity(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				return null;
			return section.getBlockEntity(x, ySection, z);
		}
	}
	
	@Override
	public void putBlockEntity(BlockEntity blockEntity) {
		int sectionIndex = blockEntity.y() / 16;
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				createSection(sectionIndex);
			section.putBlockEntity(blockEntity);
		}
	}
	
	@Override
	public void removeBlockEntity(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			if (section == null)
				return;
			section.removeBlockEntity(x, ySection, z);
		}
	}
	
	@Override
	public synchronized int getBiomeId(int x, int z) {
		return biomes[x * 16 + z];
	}
	
	@Override
	public synchronized void setBiomeId(int x, int z, int id) {
		biomes[x * 16 + z] = (byte) id;
	}
	
	@Override
	public BlockState captureBlockState(int x, int y, int z) {
		int sectionIndex = y / 16;
		int ySection = y & 15;// y%16
		ChunkSection section;
		synchronized (this) {
			section = sections[sectionIndex];
			return section.captureBlockState(world(), x, ySection, z);
		}
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
					length += 10240;// adds the section's length
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
			// Write biomes:
			if (writeBiomes) {
				out.write(biomes);
			}
		}
	}
	
}
