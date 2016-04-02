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
package com.electronwill.concurrent;

/**
 * A constant value. Once its value has been set it cannot be changed.
 *
 * @author TheElectronWill
 */
public final class Constant<T> {
	
	private volatile T value;
	
	public Constant() {
		// not initialized
	}
	
	public Constant(T value) {
		this.value = value;// initialized
	}
	
	/**
	 * Initializes this constant. This method can only be called once.
	 *
	 * @param value
	 */
	public synchronized void init(T value) {
		if (this.value != null) {
			throw new IllegalStateException("Constant already initialized!");
		}
		this.value = value;
	}
	
	/**
	 * Gets the value.
	 */
	public T get() {
		return value;
	}
	
}
