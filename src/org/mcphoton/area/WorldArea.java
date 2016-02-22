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
import org.mcphoton.world.World;

/**
 * Represents an area of a game's world.
 *
 * @author ElectronWill
 */
public abstract class WorldArea {

	protected World world;

	public World getWorld() {
		return world;
	}

	protected WorldArea(World w) {
		this.world = w;
	}

	public boolean contains(Location l) {
		return l.getWorld().equals(world) && contains(l.getX(), l.getY(), l.getZ());
	}

	public abstract boolean contains(double x, double y, double z);

	public abstract boolean contains(int x, int y, int z);
}
