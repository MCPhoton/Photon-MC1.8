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

import java.io.OutputStream;
import org.mcphoton.core.Photon;
import org.mcphoton.network.SendablePacket;
import com.electronwill.collections.OpenList;
import com.electronwill.nbt.NBT;
import com.electronwill.nbt.TagCompound;
import java.io.IOException;

/**
 *
 * @author ElectronWill
 */
public final class WindowItemsPacket extends SendablePacket {

	public byte windowId;
	/**
	 * The lots that changed.
	 *
	 * @see http://wiki.vg/Slot_Data
	 */
	public OpenList<TagCompound> slots;

	public WindowItemsPacket(byte windowId) {
		this.windowId = windowId;
		this.slots = new OpenList<>(2, 2);
	}

	public WindowItemsPacket(byte windowId, OpenList<TagCompound> slots) {
		this.windowId = windowId;
		this.slots = slots;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(windowId);
		dest.writeShort(slots.size());
		for (TagCompound tc : slots) {
			try {
				NBT.dump(tc, (OutputStream) dest);
			} catch (IOException ex) {
				Photon.log.error("Unable to parse slot data");
				Photon.log.error(ex);
				return;
			}
		}
	}

	@Override
	public int id() {
		return 0x30;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

}
