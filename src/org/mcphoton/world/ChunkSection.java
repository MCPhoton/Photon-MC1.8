package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.streams.EasyOutputStream;

/**
 * A 16x16x16 section of a Chunk. It contains data about the blocks: id, emitted light, sky light (optional, only if in
 * overworld), and associated BlockEntity. Implementations of ChunkSection are not required to be Thread-safe - and
 * should not be - because thread-safety is managed by the Chunk.
 * 
 * @author ElectronWill
 * 		
 */
public abstract class ChunkSection {
	
	/**
	 * Gets the id of the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * @return the block's full id
	 */
	public abstract int getBlockId(int x, int y, int z);
	
	/**
	 * Sets the id of the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate param id the block's full id
	 */
	public abstract void setBlockId(int x, int y, int z, int id);
	
	/**
	 * Gets the level of the light emitted by the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * @return the level of the light emitted by the block
	 */
	public abstract int getBlockEmittedLight(int x, int y, int z);
	
	/**
	 * Sets the level of the light emitted by the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * @param light the light level to set
	 */
	public abstract void setBlockEmittedLight(int x, int y, int z, int light);
	
	/**
	 * Gets the level of the light received from the sky by the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * @return the level of the light emitted by the block
	 */
	public abstract int getBlockSkylight(int x, int y, int z);
	
	/**
	 * Sets the level of the light received from the sky by the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate param light the light level to set
	 */
	public abstract void setBlockSkylight(int x, int y, int z, int light);
	
	/**
	 * Gets the BlockEntity associated with the block at the given position.
	 * 
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
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
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * 		
	 */
	public abstract void removeBlockEntity(int x, int y, int z);
	
	/**
	 * Captures the state of the block at the given position. The returned BlockState contains consistent information
	 * about the block.
	 * 
	 * @param w the world of the block
	 * @param x section-relative X coordinate
	 * @param y section-relative Y coordinate
	 * @param z section-relative Z coordinate
	 * @return the block's current state
	 */
	public abstract BlockState captureBlockState(World w, int x, int y, int z);
	
	/**
	 * Writes the data of this ChunkSection to the given EasyOutputStream. The data is formatted the same way as in the
	 * ChunkDataPacket, and may change between versions of the game.
	 */
	public abstract void writeBlocksIdsTo(EasyOutputStream out) throws IOException;
	
	/**
	 * Writes the data of this ChunkSection to the given EasyOutputStream. The data is formatted the same way as in the
	 * ChunkDataPacket, and may change between versions of the game.
	 */
	public abstract void writeEmittedLightTo(EasyOutputStream out) throws IOException;
	
	/**
	 * Writes the data of this ChunkSection to the given EasyOutputStream. The data is formatted the same way as in the
	 * ChunkDataPacket, and may change between versions of the game.
	 */
	public abstract void writeSkylightTo(EasyOutputStream out) throws IOException;
	
}
