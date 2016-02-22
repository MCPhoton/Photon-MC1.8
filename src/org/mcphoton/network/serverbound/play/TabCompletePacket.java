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
package org.mcphoton.network.serverbound.play;

import java.util.Optional;
import com.electronwill.streams.EasyInputStream;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class TabCompletePacket extends ReceivablePacket {

	public String text;
	public Optional<Integer> lookAtX, lookAtY, lookAtZ;

	public TabCompletePacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		text = in.readString();
		boolean hasPosition = (in.readByte() == 1);
		if (hasPosition) {
			long pos = in.readLong();
			lookAtX = Optional.of(ProtocolData.decodePositionX(pos));
			lookAtY = Optional.of(ProtocolData.decodePositionY(pos));
			lookAtZ = Optional.of(ProtocolData.decodePositionZ(pos));
		} else {
			lookAtX = Optional.empty();
			lookAtY = Optional.empty();
			lookAtZ = Optional.empty();
		}
	}

	@Override
	public int id() {
		return 0x14;
	}

	@Override
	public void handle() {
		//TODO send autocompletion packet
	}

}
