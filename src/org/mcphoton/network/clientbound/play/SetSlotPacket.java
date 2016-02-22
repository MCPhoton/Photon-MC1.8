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
import com.electronwill.nbt.NBT;
import com.electronwill.nbt.TagCompound;
import java.io.IOException;

/**
 *
 * @author ElectronWill
 */
public final class SetSlotPacket extends SendablePacket {

	public byte windowId;
	public short slotId;
	public TagCompound slotData;

	public SetSlotPacket(byte windowId, short slotId, TagCompound slotData) {
		this.windowId = windowId;
		this.slotId = slotId;
		this.slotData = slotData;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(windowId);
		dest.writeShort(slotId);
		try {
			NBT.dump(slotData, (OutputStream) dest);
		} catch (IOException ex) {
			Photon.log.error("Unable to dump slot data");
			Photon.log.error(ex);
		}
	}

	@Override
	public int id() {
		return 0x2F;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;//TODO optimize
	}

}
