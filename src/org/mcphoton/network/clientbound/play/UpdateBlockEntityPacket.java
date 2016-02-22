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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import org.mcphoton.core.Photon;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.ProtocolData;
import com.electronwill.nbt.NBT;
import com.electronwill.nbt.TagCompound;

/**
 *
 * @author ElectronWill
 */
public final class UpdateBlockEntityPacket extends SendablePacket {
	
	/**
	 * Possible action.
	 */
	public static final byte SET_SPAWNER_TYPE = 1, SET_CMD_BLOCK_TEXT = 2, SET_BEACON_POWERS = 3, SET_HEAD_ROTATION = 4,
			SET_FLOWER_POT_TYPE = 5, SET_BANNER_DRAWING = 6;
			
	public int x, y, z;
	public byte action;
	public Optional<TagCompound> nbtTag;
	
	public UpdateBlockEntityPacket(int x, int y, int z, byte action) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.nbtTag = Optional.empty();
	}
	
	public UpdateBlockEntityPacket(int x, int y, int z, byte action, TagCompound nbtTag) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.nbtTag = Optional.of(nbtTag);
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeLong(ProtocolData.encodePosition(x, y, z));
		dest.write(action);
		if (nbtTag.isPresent()) {
			try {
				NBT.dump(nbtTag.get(), (OutputStream) dest);
			} catch (IOException ex) {
				Photon.log.error("Unable to dump the NBT data of the Block entity");
				Photon.log.error(ex);
			}
		} else {
			dest.write(0);
		}
	}
	
	@Override
	public int id() {
		return 0x35;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
}
