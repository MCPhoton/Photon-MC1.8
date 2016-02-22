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

/**
 * The player attacks or right-click an entity.
 *
 * @author ElectronWill
 */
public final class UseEntityPacket extends ReceivablePacket {

	public int targetId, type;
	public Optional<Float> targetX, targetY, targetZ;

	public UseEntityPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		targetId = in.readVarInt();
		type = in.readVarInt();
		if (type == 2) {
			targetX = Optional.of(in.readFloat());
			targetY = Optional.of(in.readFloat());
			targetZ = Optional.of(in.readFloat());
		} else {
			targetX = Optional.empty();
			targetX = Optional.empty();
			targetX = Optional.empty();
		}
	}

	@Override
	public int id() {
		return 0x02;
	}

	@Override
	public void handle() {
		//TODO notify entity
	}

}
