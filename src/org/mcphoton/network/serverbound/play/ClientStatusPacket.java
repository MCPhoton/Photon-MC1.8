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
 * Sent when the client is ready to complete the login phase or to respawn.
 *
 * @author ElectronWill
 */
public final class ClientStatusPacket extends ReceivablePacket {

	public static final int RESPAWN = 0, REQUEST_STATS = 1, TAKING_INVENTORY_ACHIVEMENT = 2;

	public int actionId;

	public ClientStatusPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		actionId = in.readVarInt();
	}

	@Override
	public int id() {
		return 0x16;
	}

	@Override
	public void handle() {
		//What should we handle here??
	}

}
