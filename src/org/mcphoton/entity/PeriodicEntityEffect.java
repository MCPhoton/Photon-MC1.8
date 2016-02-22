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
 * Represents an entity's status effect that do something based on a periodic delay (in minecraft
 * ticks).
 *
 * @author ElectronWill
 * @param <E>
 */
public abstract class PeriodicEntityEffect<E extends Entity> extends EntityEffect<E> {

	protected PeriodicEntityEffect(E entity, int level) {
		super(entity, level);
	}

	/**
	 * Do what this effect does. This method is called every 50ms (i.e. 20 times per second, i.e.
	 * every minecraft tick) in one of the server's Threads. This method is guaranteed not to be
	 * called in several Threads at the same time.
	 */
	public abstract void effect();

}
