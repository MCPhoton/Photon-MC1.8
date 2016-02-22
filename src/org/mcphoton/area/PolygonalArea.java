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

import java.awt.Polygon;
import org.mcphoton.util.Location;

/**
 * A polygonal area.
 *
 * @author ElectronWill
 */
public class PolygonalArea extends WorldArea {

	private Polygon polygon;
	private int fromY = Integer.MAX_VALUE, toY = -1;

	/**
	 * Creates a new PolygonalArea. The world is that of the first Location parameter. The lower and
	 * upper Y bounds are the lowest and highest Y coordinates of the locations.
	 *
	 * @param locations several locations: the points of the Polygon
	 */
	public PolygonalArea(Location... locations) {
		super(locations[0].getWorld());
		int[] xPoints = new int[locations.length];
		int[] zPoints = new int[locations.length];
		for (int i = 0; i < zPoints.length; i++) {
			Location l = locations[i];
			xPoints[i] = l.getBlockX();
			zPoints[i] = l.getBlockZ();
			int y = l.getBlockY();
			if (y < fromY) {
				fromY = y;
			}
			if (y > toY) {
				toY = y;
			}

		}
		polygon = new Polygon(xPoints, zPoints, locations.length);
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return polygon.contains(x, z) && x >= fromY && x <= toY;
	}

	@Override
	public boolean contains(int x, int y, int z) {
		return polygon.contains(x, z) && x >= fromY && x <= toY;
	}

}
