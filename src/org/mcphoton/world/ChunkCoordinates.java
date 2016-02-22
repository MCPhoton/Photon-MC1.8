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

import org.mcphoton.util.Location;

/**
 * Represents the coordinates of a map chunk. They are NOT the same as the coordinates of the first block in the chunk !
 */
public final class ChunkCoordinates implements Comparable<ChunkCoordinates> {
	
	/**
	 * Returns the chunk coordinates of the given block XZ.
	 *
	 * @param blockX
	 * @param blockZ
	 * @return
	 */
	public static ChunkCoordinates ofBlock(int blockX, int blockZ) {
		return new ChunkCoordinates(blockX >> 4, blockZ >> 4);// >> 4 means divide by 16
	}
	
	/**
	 * Returns the chunk coordinates of the given location XZ.
	 *
	 * @param locX
	 * @param locZ
	 * @return
	 */
	public static ChunkCoordinates ofLocation(double locX, double locZ) {
		return new ChunkCoordinates((int) locX / 16, (int) locZ / 16);
	}
	
	/**
	 * Returns the chunk coordinates of the given location.
	 *
	 * @param l
	 * @return
	 */
	public static ChunkCoordinates ofLocation(Location l) {
		return new ChunkCoordinates(l.getBlockX() >> 4, l.getBlockZ() >> 4);
	}
	
	private final int x;
	
	private final int z;
	
	public ChunkCoordinates(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	@Override
	public int compareTo(ChunkCoordinates o) {
		return x == o.x ? z - o.z : x - o.x;// Compares x first, then z if x are the same.
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChunkCoordinates)) {
			return false;
		}
		ChunkCoordinates cc = (ChunkCoordinates) obj;
		return x == cc.x && z == cc.z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + this.x;
		hash = 47 * hash + this.z;
		return hash;
	}
	
}
