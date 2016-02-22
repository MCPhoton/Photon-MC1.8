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

import com.electronwill.streams.EasyInputStream;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;

/**
 *
 * @author ElectronWill
 */
public final class ResourcePackStatusPacket extends ReceivablePacket {

	public static final int SUCCESS = 0, DECLINED = 1, FAILED = 2, ACCEPTED = 3;

	public String packSha1;
	public int result;

	public ResourcePackStatusPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		packSha1 = in.readString();
		result = in.readVarInt();
	}

	@Override
	public int id() {
		return 0x19;
	}

	@Override
	public void handle() {
		//... what should we do? nothing?
	}

}
