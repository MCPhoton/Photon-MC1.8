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
package org.mcphoton.entity;

/**
 * An interface for entities considered as "objects".
 *
 * @author ElectronWill
 */
public interface ObjectEntity {

	/**
	 * Gets the "pitch" of this object, i.e. its rotation.
	 */
	float getPitch();

	/**
	 * Gets the "yaw" of this object, i.e. its rotation.
	 */
	float getYaw();

	/**
	 * Gets the "pitch" of this object, i.e. its rotation.
	 */
	void setPitch(float pitch);

	/**
	 * Gets the "yaw" of this object, i.e. its rotation.
	 */
	void setYaw(float yaw);

	default boolean isObject() {
		return true;
	}

	default boolean isMob() {
		return false;
	}

}
