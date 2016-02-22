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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an entity's status effect.
 *
 * @author ElectronWill
 * @param <E>
 * @see http://minecraft.gamepedia.com/Status_effect
 */
public abstract class EntityEffect<E extends Entity> {

	/**
	 * The remaining duration of the effect, in seconds.
	 */
	protected AtomicInteger duration;

	/**
	 * The entity this effect applies on.
	 */
	protected E entity;

	/**
	 * The level of the effect.
	 */
	protected AtomicInteger level;

	protected EntityEffect(E entity, int level) {
		if (level < 0) {
			throw new IllegalArgumentException("Negative effect level is NOT valid");
		}
		if (entity == null) {
			throw new IllegalArgumentException("An EntityEffect CANNOT be associated to a null entity");
		}
		this.entity = entity;
		this.level = new AtomicInteger(level);
	}

	/**
	 * Decreases the remaining duration.
	 *
	 * @param decrease
	 */
	public final void decreaseDuration(int decrease) {
		duration.getAndAdd(-decrease);
	}

	/**
	 * Decreases the level of this effect.
	 *
	 * @param decrease
	 */
	public final void decreaseLevel(int decrease) {
		level.getAndAdd(-decrease);
	}

	/**
	 * Returns the remaining duration of this effect.
	 */
	public final int getDuration() {
		return duration.get();
	}

	/**
	 * Modifies the remaining duration of this effect.
	 *
	 * @param newDuration
	 */
	public final void setDuration(int newDuration) {
		duration.set(newDuration);
	}

	/**
	 * Returns the entity this effect applies on.
	 */
	public final E getEntity() {
		return entity;
	}

	/**
	 * Returns the id of this effect.
	 */
	public abstract byte getId();

	/**
	 * Returns the level of this effect. The returned value must be treated as an <b>unsigned
	 * byte</b> because it should never be negative, but instead in the range 0-255.
	 *
	 * @return
	 */
	public final int getLevel() {
		return level.get();
	}

	/**
	 * Sets the level of this effect.
	 *
	 * @param newLevel
	 */
	public final void setLevel(int newLevel) {
		level.set(newLevel);
	}

	/**
	 * Increases the remaining duration.
	 *
	 * @param increase
	 */
	public final void increaseDuration(int increase) {
		duration.getAndAdd(increase);
	}

	/**
	 * Increases the level of this effect.
	 *
	 * @param increase
	 */
	public final void increaseLevel(int increase) {
		level.getAndAdd(increase);
	}

}
