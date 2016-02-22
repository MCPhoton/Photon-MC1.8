package org.mcphoton.world;

import org.mcphoton.Difficulty;
import org.mcphoton.Gamemode;
import org.mcphoton.security.AccessPermit;

public class OverWorld extends NoSkylightWorld {
	
	public OverWorld(String name, int typeId, ChunkGenerator generator, Difficulty difficulty, Gamemode gamemode, double spawnX,
			double spawnY, double spawnZ) {
		super(name, typeId, generator, difficulty, gamemode, spawnX, spawnY, spawnZ);
	}
	
	public OverWorld(String name, Difficulty difficulty, Gamemode gamemode, double spawnX, double spawnY, double spawnZ) {
		super(name, 0, new OverworldChunkGenerator(), difficulty, gamemode, spawnX, spawnY, spawnZ);
	}
	
	public OverWorld(String name, Difficulty difficulty, Gamemode gamemode) {
		super(name, 0, new OverworldChunkGenerator(), difficulty, gamemode, 0, 0, 0);
	}
	
	@Override
	public synchronized int getBlockSkylight(int x, int y, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x / 32, z / 32);
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			if (chunkRef == null)
				return 0;
			Chunk c = chunkRef.get();
			if (c == null) {
				chunks.remove(coords);
				return 0;
			}
			return c.getBlockSkylight(x / 16, y, z / 16);
		}
	}
	
	@Override
	public void setBlockSkylight(int x, int y, int z, int level, AccessPermit permit) {
		if (!permit.maySetBlockSkylight(this, x, y, z, level))
			return;
		ChunkCoordinates coords = new ChunkCoordinates(x / 32, z / 32);
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			Chunk c = chunkRef == null ? null : chunkRef.get();
			if (c == null) {
				// TODO load if file exists
				c = generator.generate(this, x, z);
				chunkRef = new SoftChunkReference(c);
				chunks.put(coords, chunkRef);
			}
			c.setBlockSkylight(x / 16, y, z / 16, level);
		}
	}
	
	@Override
	public boolean hasSkylight() {
		return true;
	}
}
