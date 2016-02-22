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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.network.SendablePacket;

/**
 * Send it to change the player's slot selection.
 *
 * @author ElectronWill
 */
public final class HeldItemChangePacket extends SendablePacket {

	/**
	 * The selected slot's id between 0 and 8 (inclusive).
	 */
	public byte slot;

	public HeldItemChangePacket(byte slot) {
		this.slot = slot;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(slot);
	}

	@Override
	public int maxDataSize() {
		return 1;
	}

	@Override
	public int id() {
		return 0x09;
	}

}
