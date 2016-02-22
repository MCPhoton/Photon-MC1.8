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
public final class ChangeGameStatePacket extends SendablePacket {

	public static final byte INVALID_BED = 0, END_RAINING = 1, START_RAINING = 2, CHANGE_GAMEMODE = 3, ENTER_CREDITS = 4, DEMO_MESSAGE = 5, ARROW_HIT = 6, FADE_VALUE = 7, FADE_TIME = 8, PLAYER_MOB_APPEARANCE = 10;
	/**
	 * Gamemode value.
	 */
	public static final float SURVIVAL = 0, CREATIVE = 1, ADVENTURE = 2, SPECTATOR = 3;
	/**
	 * Demo message value.
	 */
	public static final float WELCOME = 0, TELL_MOVEMENTS = 101, TELL_JUMP = 102, TELL_INVENTORY = 103;
	/**
	 * Fade value.
	 */
	public static final float DARK = 0, BRIGHT = 1;

	public byte reason;
	public float value;

	public ChangeGameStatePacket(byte reason, float value) {
		this.reason = reason;
		this.value = value;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(reason);
		dest.writeFloat(value);
	}

	@Override
	public int maxDataSize() {
		return 5;
	}

	@Override
	public int id() {
		return 0x2B;
	}

}
