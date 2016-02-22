package org.mcphoton.world;

import java.util.Arrays;

/**
 * Generates overworld chunks.
 * 
 * @author ElectronWill
 * 		
 */
public class OverworldChunkGenerator extends ChunkGenerator {
	
	public OverworldChunkGenerator() {}
	
	@Override
	public Chunk generate(World w, int cx, int cz) {
		ChunkSection[] sections = new ChunkSection[16];
		for (int i = 0; i < 5; i++) {
			OverworldChunkSection section = new OverworldChunkSection();
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					for (int z = 0; z < 16; z++) {
						section.setBlockId(x, y, z, 1 << 4);
					}
				}
			}
			sections[i] = section;
		}
		byte[] biomes = new byte[256];
		Arrays.fill(biomes, (byte) 2);
		return new OverworldChunk(w, cx, cz, sections, biomes);
	}
	
}
