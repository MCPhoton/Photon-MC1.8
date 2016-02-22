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
import java.util.Optional;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class UpdateScorePacket extends SendablePacket {

	/**
	 * Possible action.
	 */
	public static final byte CREATE_OR_UPDATE = 0, REMOVE = 1;

	public String scoreName, objectiveName;
	public byte action;
	public Optional<Integer> value;

	public UpdateScorePacket(String scoreName, String objectiveName, byte action) {
		this.scoreName = scoreName;
		this.objectiveName = objectiveName;
		this.action = action;
		this.value = Optional.empty();
	}

	public UpdateScorePacket(String scoreName, String objectiveName, byte action, int value) {
		this.scoreName = scoreName;
		this.objectiveName = objectiveName;
		this.action = action;
		this.value = Optional.of(value);
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(scoreName);
		dest.write(action);
		dest.writeString(objectiveName);
		if (action == 0) {
			dest.writeVarInt(value.get());
		}
	}

	@Override
	public int id() {
		return 0x3C;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

}
