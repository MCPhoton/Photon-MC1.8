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
package org.mcphoton.world;

import org.mcphoton.security.AccessDeniedException;
import org.mcphoton.security.AccessPermit;
import org.mcphoton.security.RootPermit;

/**
 * Provides access to the informations about a particular block. These infos are always up to date.
 *
 * @author ElectronWill
 */
public final class BlockInfos {
	
	private static final AccessPermit internalPermit;
	
	static {
		AccessPermit p = null;
		try {
			p = new RootPermit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		internalPermit = p;
	}
	
	private volatile Chunk chunk;
	private final int cx, cz;// chunk's coordinates
	private final int rx, rz;// chunk-relative block coordinate
	private final World w;// the block's world
	private final int x, y, z;// absolute coordinates
	
	public BlockInfos(World w, int x, int y, int z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.cx = x >> 4;// x*16
		this.cz = z >> 4;// x*16
		this.rx = x & 15;
		this.rz = z & 15;
	}
	
	private Chunk getChunk() {
		if (!w.hasChunk(cx, cz)) {// chunk has been deleted
			chunk = null;
			return null;
		}
		if (chunk == null) {
			try {
				chunk = w.getChunk(cx, cz, false, internalPermit);
			} catch (AccessDeniedException e) {
				e.printStackTrace();
			}
		}
		return chunk;
	}
	
	/**
	 * Gets the level of the light emitted by the block at the given position.
	 */
	public int getBlockEmittedLight() {
		Chunk c = getChunk();
		if (c == null)
			return 0;
		return c.getBlockEmittedLight(rx, y, rz);
	}
	
	/**
	 * Gets the block's id.
	 */
	public int getBlockId() {
		Chunk c = getChunk();
		if (c == null)
			return 0;
		return c.getBlockId(x, y, z);
	}
	
	/**
	 * Gets the BlockEntity associated with the block, if any, or <code>null</code> if there is none.
	 *
	 * @return the BlockEntity associated with the block
	 */
	public BlockEntity getBlockEntity() {
		Chunk c = getChunk();
		if (c == null)
			return null;
		return c.getBlockEntity(rx, y, rz);
	}
	
	/**
	 * Gets the block's skylight level.
	 */
	public int getBlockSkylight() {
		return chunk.getBlockSkylight(rx, y, rz);
	}
	
	/**
	 * Gets the block's type.
	 */
	public BlockType getType() {
		Chunk c = getChunk();
		if (c == null)
			return BlockType.get("air");
		int id = c.getBlockId(x, y, z);
		return BlockType.get(id);
	}
	
	/**
	 * Gets the block's type name.
	 */
	public String getTypeName() {
		return getType().getName();
	}
	
	/**
	 * Checks if this Block has a skyLight level.
	 */
	public boolean hasSkylight() {
		return w.hasSkylight();
	}
	
	/**
	 * Sets the level of the light emitted by the block.
	 * 
	 * @param level
	 * @param permit
	 */
	public void setBlockEmittedLight(int level, AccessPermit permit) {
		if (!permit.maySetBlockEmittedLight(w, x, y, z, level))
			return;
		Chunk c = getChunk();
		if (c == null)
			return;
		c.setBlockEmittedLight(level, rx, y, rz);
	}
	
	/**
	 * Sets the id of the block.
	 */
	public void setBlockId(int id, AccessPermit permit) {
		if (!permit.maySetBlockId(w, x, y, z, id))
			return;
		Chunk c = getChunk();
		if (c == null) {
			if (id == 0)
				return;
			try {
				c = w.getChunk(cx, cz, true, internalPermit);
			} catch (AccessDeniedException e) {
				e.printStackTrace();
			}
		}
		c.setBlockId(id, x, y, z);
	}
	
	/**
	 * Sets the level of the light received from the sky by the block.
	 */
	public void setBlockSkylight(int level, AccessPermit permit) {
		if (!permit.maySetBlockSkylight(w, x, y, z, level))
			return;
		Chunk c = getChunk();
		if (c == null)
			return;
		c.setBlockSkylight(level, rx, y, rz);
	}
	
	public World world() {
		return w;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int z() {
		return z;
	}
	
}
