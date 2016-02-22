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
public final class SteerVehiclePacket extends ReceivablePacket {

	public float sideways, forward;
	/**
	 * Bit mask: 0x1 jump, 0x2 unmount.
	 */
	public byte flags;

	public SteerVehiclePacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		sideways = in.readFloat();
		forward = in.readFloat();
		flags = in.readByte();
	}

	@Override
	public int id() {
		return 0x0C;
	}

	@Override
	public void handle() {
	}

}
