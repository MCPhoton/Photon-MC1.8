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
 * A constant <b>positive</b> integer. Once its value has been set it cannot be changed.
 *
 * @author TheElectronWill
 */
public final class IntConstant {
	
	private volatile int value;
	
	public IntConstant() {
		this.value = -1;// not initialized
	}
	
	public IntConstant(int value) {
		this.value = value;// initialized
	}
	
	/**
	 * Initializes the constant. This method can only be called once.
	 *
	 * @param value
	 */
	public void init(int value) {
		if (value == -1) {
			throw new IllegalArgumentException("IntConstant cannot be initialized with a value of -1");
		}
		synchronized (this) {
			if (this.value != -1) {
				throw new IllegalStateException("IntConstant already initialized with a value of: " + value);
			}
			this.value = value;
		}
	}
	
	/**
	 * Gets the value.
	 */
	public int get() {
		return value;
	}
	
	public boolean isInitialized() {
		return value != -1;
	}
	
}
