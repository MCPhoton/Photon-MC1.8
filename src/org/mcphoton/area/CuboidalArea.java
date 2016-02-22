/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.area;

import org.mcphoton.util.Location;

/**
 * A cuboidal area.
 *
 * @author ElectronWill
 */
public class CuboidalArea extends WorldArea {

	private int fromX, toX, fromY, toY, fromZ, toZ;

	/**
	 * Creates a new CuboidalArea between l1 and 12. Area borders are calculated using the smallest
	 * and biggest coordinates of l1 and l2. They are inclusives and the world is that of l1.
	 *
	 * @param l1 first location
	 * @param l2 second location
	 */
	public CuboidalArea(Location l1, Location l2) {
		super(l1.getWorld());
		this.fromX = Math.min(l1.getBlockX(), l2.getBlockX());
		this.fromY = Math.min(l1.getBlockY(), l2.getBlockY());
		this.fromZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
		this.toX = Math.max(l1.getBlockX(), l2.getBlockX());
		this.toY = Math.max(l1.getBlockY(), l2.getBlockY());
		this.toZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return (x >= fromX && x <= toX)
			&& (y >= fromY && y <= toY)
			&& (z >= fromZ && z <= toZ);
	}

	@Override
	public boolean contains(int x, int y, int z) {
		return (x >= fromX && x <= toX)
			&& (y >= fromY && y <= toY)
			&& (z >= fromZ && z <= toZ);
	}

}
