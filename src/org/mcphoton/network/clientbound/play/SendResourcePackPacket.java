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

import java.io.IOException;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class SendResourcePackPacket extends SendablePacket {

	public String packUrl, packSha1;

	public SendResourcePackPacket(String packUrl, String packSha1) {
		this.packUrl = packUrl;
		this.packSha1 = packSha1;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(packUrl);
		dest.writeString(packSha1);
	}

	@Override
	public int maxDataSize() {
		return packUrl.length() + packSha1.length() + 10;
	}

	@Override
	public int id() {
		return 0x48;
	}

}
