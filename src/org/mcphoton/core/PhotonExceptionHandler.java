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
package org.mcphoton.core;

/**
 * Handles uncaught exceptions.
 *
 * @author ElectronWill
 */
public final class PhotonExceptionHandler implements Thread.UncaughtExceptionHandler {
	
	private final PhotonLog log;
	
	public PhotonExceptionHandler(final PhotonLog log) {
		this.log = log;
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		log.error(e, "Uncaught exception in Thread \"" + t.getName() + '\"');
	}
	
}
