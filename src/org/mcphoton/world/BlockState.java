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
 * A captured state of a block. These informations may NOT be up to date because they correspond to the block as it was
 * when its state was captured.
 *
 * @author ElectronWill
 */
public final class BlockState {
	
	/**
	 * The BlockEntity associated with the block, <b>may be null</b>.
	 */
	public final BlockEntity entity;
	/**
	 * The block's type.
	 */
	public final BlockType type;
	/**
	 * The block's world.
	 */
	public final World world;
	/**
	 * The block's coordinates.
	 */
	public final int x, y, z;
	
	/**
	 * Creates a new BlockState object.
	 *
	 * @param loc the block's location
	 * @param type the type of the block
	 * @param entity the BlockEntity associated with the block, if any, or <code>null</code> if none.
	 */
	public BlockState(Location loc, BlockType type, BlockEntity entity) {
		this.world = loc.getWorld();
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.type = type;
		this.entity = entity;
	}
	
	/**
	 * Creates a new BlockState object.
	 *
	 * @param w the world the block is in
	 * @param x the x coordinate of the block
	 * @param y the y coordinate of the block
	 * @param z the z coordinate of the block
	 * @param type the type of the block
	 * @param entity the BlockEntity associated with the block, if any, or <code>null</code> if none.
	 */
	public BlockState(World w, int x, int y, int z, BlockType type, BlockEntity entity) {
		this.world = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.entity = entity;
	}
	
}
