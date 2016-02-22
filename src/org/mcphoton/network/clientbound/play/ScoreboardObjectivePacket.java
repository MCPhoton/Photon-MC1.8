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
public final class ScoreboardObjectivePacket extends SendablePacket {

	/**
	 * Possible mode.
	 */
	public static final byte CREATE = 0, REMOVE = 1, UPDATE = 2;

	public String objectiveName;
	public byte mode;
	public Optional<String> objectiveValue, type;

	public ScoreboardObjectivePacket(String objectiveName, byte mode, String objectiveValue, String type) {
		if (mode == 1) {
			throw new IllegalArgumentException("Invalid parameters for mode " + mode + ". objectiveValue and type are PROHIBITED.");
		}
		this.objectiveName = objectiveName;
		this.mode = mode;
		this.objectiveValue = Optional.of(objectiveValue);
		this.type = Optional.of(type);
	}

	public ScoreboardObjectivePacket(String objectiveName, byte mode) {
		if (mode != 1) {
			throw new IllegalArgumentException("Invalid parameters for mode " + mode + ". objectiveValue and type are REQUIRED.");
		}
		this.objectiveName = objectiveName;
		this.mode = mode;
		this.objectiveValue = Optional.empty();
		this.type = Optional.empty();
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(objectiveName);
		dest.write(mode);
		if (mode != 1) {
			dest.writeString(objectiveValue.get());
			dest.writeString(type.get());
		}
	}

	@Override
	public int id() {
		return 0x3B;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

}
