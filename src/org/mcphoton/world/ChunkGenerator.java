package org.mcphoton.world;

/**
 * Generates block chunks.
 * 
 * @author ElectronWill
 * 		
 */
public abstract class ChunkGenerator {
	
	/**
	 * Generates a chunk (16x256x16 blocks).
	 */
	public abstract Chunk generate(World w, int cx, int cz);
	
}
