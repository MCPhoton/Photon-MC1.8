package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.streams.EasyOutputStream;

/**
 * A region of 16x256x16 blocks, organized in 16 sections of 16x16x16 blocks. Implementations of Chunk must be
 * Thread-safe.
 * 
 * @author ElectronWill
 * 		
 */
public abstract class Chunk {
	
	private final World world;
	private final ChunkCoordinates coords;
	
	public Chunk(World w, int x, int z) {
		this.world = w;
		coords = new ChunkCoordinates(x, z);
	}
	
	public ChunkCoordinates coordinates() {
		return coords;
	}
	
	public int x() {
		return coords.getX();
	}
	
	public int z() {
		return coords.getZ();
	}
	
	public World world() {
		return world;
	}
	
	/**
	 * Checks if the blocks in this chunk have a skylight level.
	 * 
	 * @return true if the blocks have a skylight level, false if they don't.
	 */
	public abstract boolean hasSkylight();
	
	/**
	 * Gets the id of the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the block's full id
	 */
	public abstract int getBlockId(int x, int y, int z);
	
	/**
	 * Sets the id of the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate param id the block's full id
	 */
	public abstract void setBlockId(int x, int y, int z, int id);
	
	/**
	 * Gets the level of the light emitted by the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the level of the light emitted by the block
	 */
	public abstract int getBlockEmittedLight(int x, int y, int z);
	
	/**
	 * Sets the level of the light emitted by the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @param light the light level to set
	 */
	public abstract void setBlockEmittedLight(int x, int y, int z, int light);
	
	/**
	 * Gets the level of the light received from the sky by the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the level of the light emitted by the block
	 */
	public abstract int getBlockSkylight(int x, int y, int z);
	
	/**
	 * Sets the level of the light received from the sky by the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate param light the light level to set
	 */
	public abstract void setBlockSkylight(int x, int y, int z, int light);
	
	/**
	 * Gets the BlockEntity associated with the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the BlockEntity associated with the block, or null if the block has no BlockEntity.
	 */
	public abstract BlockEntity getBlockEntity(int x, int y, int z);
	
	/**
	 * Puts the given BlockEntity to this section. If this section contains a BlockEntity with the same coordinates, it
	 * is removed.
	 * 
	 * @param blockEntity the BlockEntity.
	 */
	public abstract void putBlockEntity(BlockEntity blockEntity);
	
	/**
	 * Removes any BlockEntity associated with the block at the given position.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * 		
	 */
	public abstract void removeBlockEntity(int x, int y, int z);
	
	/**
	 * Gets the id of the biome of the given XZ column.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the biome's id
	 */
	public abstract int getBiomeId(int x, int z);
	
	/**
	 * Sets the id of the biome of the given XZ column.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param z chunk-relative Z coordinate
	 * @param id the id to set
	 */
	public abstract void setBiomeId(int x, int z, int id);
	
	/**
	 * Captures the state of the block at the given position. The returned BlockState contains consistent information
	 * about the block.
	 * 
	 * @param x chunk-relative X coordinate
	 * @param y chunk-relative Y coordinate
	 * @param z chunk-relative Z coordinate
	 * @return the block's current state
	 */
	public abstract BlockState captureBlockState(int x, int y, int z);
	
	/**
	 * Writes the data of this Chunk to the given EasyOutputStream. The data is formatted the same way as in the
	 * ChunkDataPacket, and may change between versions of the game.
	 * <h2>Current data format (MC 1.8)</h2>
	 * <p>
	 * <ol>
	 * <li>short mask: indicates by a 1 which sections are present</li>
	 * <li>varint length: length of the rest of the data, below</li>
	 * <li>short[] blocksIds</li>
	 * <li>byte[] emittedLight: 1/2 byte per block</li>
	 * <li>byte[] skyLight: 1/2 byte per block</li>
	 * <li>byte[] biomes: 1 byte per biome, present only if <code>writeBiomes</code> is true</li>
	 * </ol>
	 * </p>
	 * 
	 * @param writeBiomes if the biomes are written
	 */
	public abstract void writeTo(EasyOutputStream out, boolean writeBiomes) throws IOException;
	
}
