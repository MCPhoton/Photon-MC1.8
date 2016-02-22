package org.mcphoton.world;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.mcphoton.Difficulty;
import org.mcphoton.Gamemode;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.Entity;
import org.mcphoton.security.AccessDeniedException;
import org.mcphoton.security.AccessPermit;

/**
 * A world with blocks and entities. Implementations of World must be Thread-safe, and must check the AccessPermit given
 * as method's arguments.
 * 
 * @author ElectronWill
 * 		
 */
public abstract class World {
	
	private static Map<String, World> WORLDS_MAP = new HashMap<>();
	
	public static void register(World w) {
		WORLDS_MAP.put(w.getName(), w);
	}
	
	public static World get(String name) {
		return WORLDS_MAP.get(name);
	}
	
	protected final byte typeId;
	protected volatile String name;
	protected volatile File dir;
	protected volatile double spawnX, spawnY, spawnZ;
	protected volatile Gamemode gamemode;
	protected volatile Difficulty difficulty;
	protected volatile ChunkGenerator generator;
	
	public World(String name, byte typeId, ChunkGenerator generator, Difficulty difficulty, Gamemode gamemode, double spawnX, double spawnY,
			double spawnZ) {
		this.name = name;
		this.dir = new File(Photon.worldsDir, name);
		this.typeId = typeId;// -1: Nether, 0: Overworld, 1: End
		this.generator = generator;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.spawnZ = spawnZ;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		if (WORLDS_MAP.containsKey(name))
			throw new IllegalArgumentException("A world with that name already exists");
		boolean success = dir.renameTo(new File(Photon.worldsDir, name));
		if (success)
			this.name = name;
		return success;
	}
	
	public byte getTypeId() {
		return typeId;
	}
	
	public ChunkGenerator getGenerator() {
		return generator;
	}
	
	public void setGenerator(ChunkGenerator generator) {
		this.generator = generator;
	}
	
	public File getDirectory() {
		return dir;
	}
	
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public Gamemode getGamemode() {
		return gamemode;
	}
	
	public void setGamemode(Gamemode gamemode) {
		this.gamemode = gamemode;
	}
	
	public double getSpawnX() {
		return spawnX;
	}
	
	public double getSpawnY() {
		return spawnY;
	}
	
	public double getSpawnZ() {
		return spawnZ;
	}
	
	public void setSpawnX(double spawnX) {
		this.spawnX = spawnX;
	}
	
	public void setSpawnY(double spawnY) {
		this.spawnY = spawnY;
	}
	
	public void setSpawnZ(double spawnZ) {
		this.spawnZ = spawnZ;
	}
	
	public void setSpawn(double x, double y, double z) {
		this.spawnX = x;
		this.spawnY = y;
		this.spawnZ = z;
	}
	
	/**
	 * Cleans the chunks' storage by removing unused entries.
	 */
	public abstract void cleanChunks();
	
	public Chunk getChunk(int x, int z, boolean createIfNotExists, AccessPermit permit) throws AccessDeniedException {
		ChunkCoordinates coords = new ChunkCoordinates(x, z);
		return getChunk(coords, createIfNotExists, permit);
	}
	
	public abstract Chunk getChunk(ChunkCoordinates coords, boolean createIfNotExists, AccessPermit permit) throws AccessDeniedException;
	
	/**
	 * Adds a chunk to this world, replacing the previous chunk at the same coordinates if any.
	 */
	public abstract void putChunk(Chunk chunk, AccessPermit permit) throws AccessDeniedException;
	
	/**
	 * Checks if a chunk exists.
	 * 
	 * @param x chunk's X coordinate
	 * @param z chunk's Z coordinate
	 * @return true if it exists
	 */
	public boolean hasChunk(int x, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x, z);
		return hasChunk(coords);
	}
	
	/**
	 * Checks if a chunk exists.
	 * 
	 * @param coords chunk's coordinates.
	 * @return true if it exists
	 */
	public abstract boolean hasChunk(ChunkCoordinates coords);
	
	/**
	 * Checks if a chunk is loaded in memory.
	 * 
	 * @param x chunk's X coordinate
	 * @param z chunk's Z coordinate
	 * @return true if it is currently loaded in memory
	 */
	public boolean isChunkLoaded(int x, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x, z);
		return isChunkLoaded(coords);
	}
	
	/**
	 * Checks if a chunk is loaded in memory.
	 * 
	 * @param coords chunk's coordinates
	 * @return true if it is currently loaded in memory
	 */
	public abstract boolean isChunkLoaded(ChunkCoordinates coords);
	
	/**
	 * Gets the id of the block at the given position. If the chunk containing the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, this method will return 0.
	 */
	public abstract int getBlockId(int x, int y, int z);
	
	/**
	 * Sets the id of the block at the given position. If the chunk contanining the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, it will be generated.
	 */
	public abstract void setBlockId(int x, int y, int z, int id, AccessPermit permit);
	
	/**
	 * Gets the id of the block at the given position. If the chunk containing the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, this method will return 0.
	 */
	public abstract int getBlockEmittedLight(int x, int y, int z);
	
	/**
	 * Sets the id of the block at the given position. If the chunk contanining the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, this method won't do anything (the chunk won't be generated).
	 */
	public abstract void setBlockEmittedLight(int x, int y, int z, int level, AccessPermit permit);
	
	/**
	 * Gets the id of the block at the given position. If the chunk containing the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, this method will return 0.
	 */
	public abstract int getBlockSkylight(int x, int y, int z);
	
	/**
	 * Sets the id of the block at the given position. If the chunk contanining the block is not loaded in memory but
	 * exists, it will be loaded; if it does not exist, this method won't do anything (the chunk won't be generated).
	 */
	public abstract void setBlockSkylight(int x, int y, int z, int level, AccessPermit permit);
	
	/**
	 * Gets the type of the block at the given coordinates. If the chunk containing the block is not loaded in memory
	 * but exists, it will be loaded; if it does not exist, this method will return the AIR type.
	 */
	public BlockType getBlockType(int x, int y, int z) {
		int id = getBlockId(x, y, z);
		return BlockType.get(id);
	}
	
	/**
	 * Sets the type of the block at the given coordinates. If the chunk containing the block is not loaded in memory
	 * but exists, it will be loaded; if it does not exist, it will be generated.
	 */
	public void setBlockType(int x, int y, int z, BlockType type, AccessPermit permit) {
		int id = type.getId();
		setBlockId(x, y, z, id, permit);
	}
	
	/**
	 * Checks if the blocks in this world have a skylight level.
	 */
	public abstract boolean hasSkylight();
	
	/**
	 * Adds an entity to this world.
	 */
	protected abstract void addEntity(Entity e);
	
	/**
	 * Removes an entity from this world.
	 */
	protected abstract void removeEntity(Entity e);
	
	/**
	 * Spawns an entity in this world.
	 * 
	 * @param entity the entity
	 * @param x entity's X coordinate
	 * @param y entity's Y coordinate
	 * @param z entity's Z coordinate
	 */
	public void spawnEntity(Entity entity, double x, double y, double z) {
		if (entity.getEntityId() != -1)
			throw new IllegalArgumentException("This entity has already been spawned");
			
		entity.setX(x);
		entity.setY(y);
		entity.setZ(z);
		entity.setWorld(this);
		addEntity(entity);// sets the entity's id
	}
	
	public abstract void forEachEntity(Consumer<Entity> action);
	
}
