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

import java.util.UUID;

/**
 * Implements calculations about some weird things in minecraft, for example the yaw that does not follow classical
 * trigonometry rules.
 *
 * @author ElectronWill
 */
public final class MCUtils {
	
	private MCUtils() {}
	
	/**
	 *
	 * @param standX standing at point (x coordinate)
	 * @param standZ standing at point (z coordinate)
	 * @param lookX looking towards point (x coordinate)
	 * @param lookZ looking towards point (z coordinate)
	 * @return the yaw in degrees
	 */
	public static float calculateYaw(int standX, int standZ, int lookX, int lookZ) {
		int l = lookX - standX;
		int w = lookZ - standZ;
		double c = Math.sqrt(l * l + w * w);
		double alpha1 = -Math.asin(l / c) / Math.PI * 180;
		double alpha2 = Math.acos(w / c) / Math.PI * 180;
		if (alpha2 > 90) {
			return (float) (180 - alpha1);
		} else {
			return (float) alpha1;
		}
	}
	
	public static Vector3D calculateUnitVector(float yaw, float pitch) {
		float x = (float) (-Math.cos(pitch) * Math.sin(yaw));
		float y = (float) -Math.sin(yaw);
		float z = (float) (Math.cos(pitch) * Math.cos(yaw));
		return new Vector3D(x, y, z);
	}
	
	public static UUID generateUidFromName(String name) {
		char[] chars = name.toCharArray();
		long lsb = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			lsb |= (c << 2 * i);// appends the bits
		}
		return new UUID(0, lsb);
	}
	
}
