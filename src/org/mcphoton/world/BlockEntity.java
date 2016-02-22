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

import org.mcphoton.entity.Entity;
import org.mcphoton.entity.impl.OnlinePlayer;
import com.electronwill.streams.Writeable;

/**
 * An "entity" related to a block. One entity is created per block that require it. A block entity can store individual
 * data and have special actions.
 *
 * @author ElectronWill
 */
public abstract class BlockEntity implements Writeable {
	
	protected final World w;
	protected final int x, y, z;
	
	public BlockEntity(World w, int x, int y, int z) {
		super();
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	/**
	 * Called when this block is broken.
	 */
	public void onBroken() {}
	
	/**
	 * Called when an entity moves and enter in this block (if it isn't solid) or on its upper face.
	 *
	 * @param me the entity
	 */
	public void onEnter(Entity me) {}
	
	/**
	 * Called when a player interacts with this block entity.
	 *
	 * @param p the player
	 * @param button the interaction's control used (right click, middle click, left click).
	 */
	public void onInteract(OnlinePlayer p, InteractButton button) {}
	
	/**
	 * Called when an entity moves and leaves this block (if it isn't solid) or its upper face.
	 *
	 * @param me the entity
	 */
	public void onLeave(Entity me) {}
	
	/**
	 * Called when this block is placed.
	 */
	public void onPlaced() {}
	
	/**
	 * Called when a player who's on a block sneaks.
	 *
	 * @param p the player
	 */
	public void onSneak(OnlinePlayer p) {}
	
}
