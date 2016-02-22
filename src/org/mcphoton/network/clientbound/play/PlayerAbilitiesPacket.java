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
public final class PlayerAbilitiesPacket extends SendablePacket {

	public byte flags;
	public float flySpeed, walkSpeed;

	public PlayerAbilitiesPacket(byte flags, float flySpeed, float walkSpeed) {
		this.flags = flags;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
	}

	public PlayerAbilitiesPacket(float flySpeed, float walkSpeed, boolean creative, boolean flying, boolean canFly, boolean godMode) {
		this.flags = 0;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
		if (creative) {
			flags |= 1;
		}
		if (flying) {
			flags |= 2;
		}
		if (canFly) {
			flags |= 4;
		}
		if (godMode) {
			flags |= 8;
		}
	}

	public void setCreativeMode(boolean creative) {
		if (creative) {
			flags |= 1;
		} else {
			flags &= 0b1110;
		}
	}

	public void setFlying(boolean flying) {
		if (flying) {
			flags |= 2;
		} else {
			flags &= 0b1101;
		}
	}

	public void setCanFly(boolean can) {
		if (can) {
			flags |= 4;
		} else {
			flags &= 0b1011;
		}
	}

	public void setGodMode(boolean god) {
		if (god) {
			flags |= 8;
		} else {
			flags &= 0b0111;
		}
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.write(flags);
		dest.writeFloat(flySpeed);
		dest.writeFloat(walkSpeed);
	}

	@Override
	public int maxDataSize() {
		return 5;
	}

	@Override
	public int id() {
		return 0x39;
	}

}
