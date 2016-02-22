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
 * This thread runs when the java VM exits.
 *
 * @author ElectronWill
 */
public final class ShutdownHook extends Thread {
	
	@Override
	public void run() {
		try {
			if (!Photon.stopped) {
				Photon.stop();
			} else {
				Photon.log.info("Java VM shutdowns now.");
				Photon.log.flush();
			}
		} catch (Throwable thro) {}
	}
	
}
