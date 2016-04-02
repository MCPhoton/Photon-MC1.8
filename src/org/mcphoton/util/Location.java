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
package org.mcphoton.util;

import org.mcphoton.world.World;

/**
 * A location defined by a World and 3 coordinates: X, Y, Z. This class is Thread-safe.
 *
 * @author ElectronWill
 */
public class Location {
	
	protected volatile World world;
	protected volatile double x, y, z;
	
	public Location(World world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location(String locationString) {
		String[] parts = locationString.split(",");
		this.world = World.get(parts[0]);
		this.x = Integer.parseInt(parts[1]);
		this.y = Integer.parseInt(parts[2]);
		this.z = Integer.parseInt(parts[3]);
		
	}
	
	/**
	 * Gets a String representation of this Location: world,x,y,z
	 */
	@Override
	public String toString() {
		StringBuilder mcs = new StringBuilder();
		mcs.append(world.getName()).append(',').append(x).append(',').append(y).append(',').append(z);
		return mcs.toString();
	}
	
	/**
	 * Returns a new Location that is the result of a move defined by the given Vector, starting at this location.
	 *
	 * @param vector
	 */
	public final Location apply(Vector3D vector) {
		double nx = x + vector.x;
		double ny = y + vector.y;
		double nz = z + vector.z;
		return new Location(world, nx, ny, nz);
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public double getX() {
		return x;
	}
	
	public int getBlockX() {
		return (int) x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getBlockY() {
		return (int) y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public int getBlockZ() {
		return (int) z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
}
