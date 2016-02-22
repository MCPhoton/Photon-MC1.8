package org.mcphoton.world;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.mcphoton.Difficulty;
import org.mcphoton.Gamemode;
import org.mcphoton.entity.Entity;
import org.mcphoton.security.AccessDeniedException;
import org.mcphoton.security.AccessPermit;
import com.electronwill.collections.IntBag;
import com.electronwill.collections.OpenList;

public class NoSkylightWorld extends World {
	
	protected final Map<ChunkCoordinates, SoftChunkReference> chunks = new HashMap<>(2048);
	protected final ReferenceQueue<Chunk> collectedChunkRefs = new ReferenceQueue<>();
	protected final OpenList<Entity> entities = new OpenList<>(1000, 100);// index = entity's id
	protected final IntBag removedIds = new IntBag(100, 50);// ids of the removed entities. Avoids fragmentation.
	
	public NoSkylightWorld(String name, int typeId, ChunkGenerator generator, Difficulty difficulty, Gamemode gamemode, double spawnX,
			double spawnY, double spawnZ) {
		super(name, (byte) typeId, generator, difficulty, gamemode, spawnX, spawnY, spawnZ);
	}
	
	@Override
	public synchronized void cleanChunks() {
		Reference<? extends Chunk> ref;
		while ((ref = collectedChunkRefs.poll()) != null) {
			SoftChunkReference chunkRef = (SoftChunkReference) ref;
			chunks.remove(chunkRef.coords);
		}
	}
	
	@Override
	public Chunk getChunk(ChunkCoordinates coords, boolean createIfNotExists, AccessPermit permit) throws AccessDeniedException {
		if (!permit.mayGetDirectChunkAccess())
			throw new AccessDeniedException("Not allowed to get direct chunk access");
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			if (chunkRef == null) {// chunk not yet loaded
				if (createIfNotExists) {
					Chunk c = generator.generate(this, coords.getX(), coords.getZ());
					chunkRef = new SoftChunkReference(c, collectedChunkRefs);
					chunks.put(coords, chunkRef);
					return c;
				} else {
					return null;
				}
			}
			Chunk c = chunkRef.get();
			if (c == null) {// chunk garbage collected
				// TODO load chunk
			}
			return c;
		}
	}
	
	@Override
	public void putChunk(Chunk chunk, AccessPermit permit) throws AccessDeniedException {
		if (!permit.mayGetDirectChunkAccess())
			throw new AccessDeniedException("Not allowed to get direct chunk access");
		synchronized (this) {
			SoftChunkReference chunkRef = new SoftChunkReference(chunk, collectedChunkRefs);
			chunks.put(chunk.coordinates(), chunkRef);
		}
	}
	
	@Override
	public synchronized boolean hasChunk(ChunkCoordinates coords) {
		return chunks.containsKey(coords);
	}
	
	@Override
	public int getBlockId(int x, int y, int z) {
		ChunkCoordinates coords = new ChunkCoordinates(x / 32, z / 32);
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			// TODO load if file exists
			if (chunkRef == null)// chunk not loaded
				return 0;
			Chunk c = chunkRef.get();
			if (c == null) {// reference garbage collected
				chunks.remove(coords);
				return 0;
			}
			return c.getBlockId(x / 16, y, z / 16);
		}
	}
	
	@Override
	public void setBlockId(int x, int y, int z, int id, AccessPermit permit) {
		if (!permit.maySetBlockId(this, x, y, z, id))
			return;
		ChunkCoordinates coords = new ChunkCoordinates(x / 32, z / 32);
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			Chunk c = chunkRef == null ? null : chunkRef.get();
			if (c == null) {
				// TODO load if file exists
				c = generator.generate(this, x, z);
				chunkRef = new SoftChunkReference(c, collectedChunkRefs);
				chunks.put(coords, chunkRef);
			}
			c.setBlockId(x / 16, y, z / 16, id);
		}
	}
	
	@Override
	public int getBlockEmittedLight(int x, int y, int z) {
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
			return c.getBlockEmittedLight(x / 16, y, z / 16);
		}
	}
	
	@Override
	public void setBlockEmittedLight(int x, int y, int z, int level, AccessPermit permit) {
		if (!permit.maySetBlockEmittedLight(this, x, y, z, level))
			return;
		ChunkCoordinates coords = new ChunkCoordinates(x / 32, z / 32);
		synchronized (this) {
			SoftChunkReference chunkRef = chunks.get(coords);
			Chunk c = chunkRef == null ? null : chunkRef.get();
			if (c == null) {
				// TODO load if file exists
				c = generator.generate(this, x, z);
				chunkRef = new SoftChunkReference(c, collectedChunkRefs);
				chunks.put(coords, chunkRef);
			}
			c.setBlockEmittedLight(x / 16, y, z / 16, level);
		}
	}
	
	@Override
	public int getBlockSkylight(int x, int y, int z) {
		return 0;
	}
	
	@Override
	public void setBlockSkylight(int x, int y, int z, int level, AccessPermit permit) {}
	
	@Override
	public boolean hasSkylight() {
		return false;
	}
	
	@Override
	protected void addEntity(Entity e) {
		synchronized (entities) {
			if (removedIds.isEmpty()) {// make a new id
				int id = entities.size();
				e.setId(id);
				entities.setOrAdd(id, e);// update entity list
			} else {// reuse an id
				int id = removedIds.get(0);
				e.setId(id);// set entity id
				removedIds.remove(0);// don't reuse the same id twice!
				entities.set(id, e);// update entity list
			}
		}
	}
	
	@Override
	protected void removeEntity(Entity e) {
		synchronized (entities) {
			int id = e.getEntityId();
			entities.set(id, null);// do not shift -> faster but create fragmentation
			removedIds.add(id);// reuse ids -> remove fragmentation
		}
	}
	
	@Override
	public void forEachEntity(Consumer<Entity> action) {
		synchronized (entities) {
			entities.forEach(action);
		}
	}
	
	protected class SoftChunkReference extends SoftReference<Chunk> {
		
		public final ChunkCoordinates coords;
		
		public SoftChunkReference(Chunk referent, ReferenceQueue<? super Chunk> q) {
			super(referent, q);
			this.coords = referent.coordinates();
		}
		
		public SoftChunkReference(Chunk referent) {
			super(referent);
			this.coords = referent.coordinates();
		}
		
	}
	
	@Override
	public boolean isChunkLoaded(ChunkCoordinates coords) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
