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
package org.mcphoton.core;

/**
 * Represents something in the game that needs to be sometimes updated.
 *
 * @author ElectronWill
 */
public interface Updateable {

	/**
	 * Updates this updateable. This method is called by an UpdateThread every 1/20 second
	 * (i.e. 50ms), or randomly if {@link #updatesRandomly()} returns true.
	 */
	void update() throws Exception;

	/**
	 * Checks if this Updateable's {@link #update()} method can be called. <br />
	 * If this method returns {@code false} then the {@link #update()} method will not be called
	 * by the ThreadZone this thing belongs to.
	 *
	 * @return {@code true} if this entity/block can be updated
	 */
	boolean canUpdate();

	/**
	 * Check if this Updateable updates randomly instead of every tick.
	 *
	 * @return {@code true} if this Updateable updates randomly, and not every tick.
	 */
	default boolean updatesRandomly() {
		return false;
	}

	/**
	 * Checks if this Updateable is valid. An invalid Updateable is no longer updated.
	 *
	 * @return {@code true} if this Updateable is still valid
	 */
	boolean isValid();

	/**
	 * Invalidates this Updateable. On the next tick it won't be updated any longer. It cannot be
	 * revalidated.
	 */
	void invalidate();
}
