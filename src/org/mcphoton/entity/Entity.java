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
package org.mcphoton.entity;

import org.mcphoton.util.Location;
import org.mcphoton.world.World;
import com.electronwill.concurrent.Constant;
import com.electronwill.concurrent.IntConstant;

/**
 * An abstract game entity. It can be a player, a animal or a monster, a projectile, an experience orb, ... <br>
 * <b>This class is Thread-safe, and any class that extends it should be Thread-safe too.</b>
 * <p>
 * This class is in the "world" package because:
 * <li>Each entity belongs to a World
 * <li>An entity's id is unique IN ITS WORLD: the same id can be used in different worlds for different entities.
 * <li>It's more practical for giving ID to entities: only the world can assign it (package-private variable) but
 * everybody can see it (public getter).
 * </p>
 *
 * @author ElectronWill
 */
public abstract class Entity {
	
	/**
	 * Entity's unique ID (unique in its world).
	 */
	private final IntConstant id = new IntConstant();
	private final Constant<World> world = new Constant<>();
	
	private volatile double x, y, z;
	
	/**
	 * Gets the unique id of this entity. Entities' ids are unique in each world.
	 */
	public final int getEntityId() {
		return id.get();
	}
	
	/**
	 * Gets the current location of this entity.
	 */
	public final Location getLocation() {
		return new Location(world.get(), x, y, z);
	}
	
	public final World getWorld() {
		return world.get();
	}
	
	public final double getX() {
		return x;
	}
	
	public final double getY() {
		return y;
	}
	
	public final double getZ() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void setId(int entityId) {
		id.init(entityId);
	}
	
	public void setWorld(World w) {
		world.init(w);
	}
	
	public abstract String getTypeName();
	
	public EntityType getType() {
		return EntityType.get(getTypeName());
	}
	
}
