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
package org.mcphoton.network.serverbound.play;

import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;
import com.electronwill.streams.EasyInputStream;

/**
 * Sent when the client connects or when its settings change.
 *
 * @author ElectronWill
 */
public final class ClientSettingsPacket extends ReceivablePacket {
	
	public String locale;
	public byte viewDistance, chatMode, skinParts;
	public boolean chatColors;
	
	public ClientSettingsPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		locale = in.readString();
		viewDistance = in.readByte();
		chatMode = in.readByte();
		chatColors = in.readBoolean();
		skinParts = in.readByte();
	}
	
	@Override
	public int id() {
		return 0x15;
	}
	
	@Override
	public void handle() throws Exception {
		// ... what should we do now?
	}
	
}
