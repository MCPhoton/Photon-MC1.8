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

import javafx.scene.shape.Ellipse;
import org.mcphoton.util.Location;

/**
 * A cylindrical area.
 *
 * @author ElectronWill
 */
public class CylindricalArea extends WorldArea {

	/**
	 * Ellipse base of the cylindrical area.
	 */
	private Ellipse ellipse;

	/**
	 * Lower and upper bounds.
	 */
	private int fromY, toY;

	/**
	 * Creates a new CylindricalArea.
	 *
	 * @param center the center of the ellipse
	 * @param radiusX the X radius of the ellipse
	 * @param radiusZ the Z radius of the ellipse
	 * @param fromY lower (or upper) Y bound, included in the area
	 * @param toY upper (or lower) Y bound, included in the area
	 */
	public CylindricalArea(Location center, int radiusX, int radiusZ, int fromY, int toY) {
		super(center.getWorld());
		ellipse = new Ellipse(center.getBlockX(), center.getBlockZ(), radiusX, radiusZ);
		this.fromY = Math.min(fromY, toY);
		this.toY = Math.max(fromY, toY);
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return ellipse.contains(x, z) && x >= fromY && x <= toY;
	}

	@Override
	public boolean contains(int x, int y, int z) {
		return ellipse.contains(x, z) && x >= fromY && x <= toY;
	}

}
