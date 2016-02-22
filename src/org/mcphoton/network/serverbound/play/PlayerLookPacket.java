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
public final class PlayerLookPacket extends ReceivablePacket {

	public float yaw, pitch;
	public boolean onGround;

	public PlayerLookPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		yaw = in.readFloat();
		pitch = in.readFloat();
		onGround = (in.readByte() == 1);
	}

	@Override
	public int id() {
		return 0x05;
	}

	@Override
	public void handle() {
		//TODO update Player
		//TODO notify other players
		//TODO notify entities -> does the Player look at an Enderman? -> !!
	}

}
