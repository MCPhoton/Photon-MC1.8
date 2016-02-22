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

import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;
import java.io.IOException;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class WorldBorderPacket extends SendablePacket {

	public Action action;

	public WorldBorderPacket(Action action) {
		this.action = action;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		action.writeTo(dest);
	}

	@Override
	public int id() {
		return 0x44;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	public static abstract class Action implements Writeable {

		public abstract void writeTo(EasyOutputStream dest) throws IOException;
	}

	public static final class SetRadiusAction extends Action {

		public double radius;

		public SetRadiusAction(double radius) {
			this.radius = radius;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(0);
			dest.writeDouble(radius);

		}
	}

	public static final class SetCenterAction extends Action {

		public double oldRadius, newRadius;
		public long speed;

		public SetCenterAction(double oldRadius, double newRadius, long speed) {
			this.oldRadius = oldRadius;
			this.newRadius = newRadius;
			this.speed = speed;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(1);
			dest.writeDouble(oldRadius);
			dest.writeDouble(newRadius);
			dest.writeVarLong(speed);
		}
	}

	public static final class InitializeAction extends Action {

		public double x, z;

		public InitializeAction(double x, double z) {
			this.x = x;
			this.z = z;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(2);
			dest.writeDouble(x);
			dest.writeDouble(z);
		}
	}

	public static final class LerpRadiusAction extends Action {

		public double x, z, oldRadius, newRadius;
		public long speed;
		public int portalTeleportBoundary, warningTime, warningBlocks;

		public LerpRadiusAction(double x, double z, double oldRadius, double newRadius, long speed, int portalTeleportBoundary, int warningTime, int warningBlocks) {
			this.x = x;
			this.z = z;
			this.oldRadius = oldRadius;
			this.newRadius = newRadius;
			this.speed = speed;
			this.portalTeleportBoundary = portalTeleportBoundary;
			this.warningTime = warningTime;
			this.warningBlocks = warningBlocks;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(3);
			dest.writeDouble(x);
			dest.writeDouble(z);
			dest.writeDouble(oldRadius);
			dest.writeDouble(newRadius);
			dest.writeVarLong(speed);
			dest.writeVarInt(warningTime);
			dest.writeVarInt(warningBlocks);
		}
	}

	public static final class SetWarningTimeAction extends Action {

		public int warningTime;

		public SetWarningTimeAction(int warningTime) {
			this.warningTime = warningTime;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(4);
			dest.writeVarInt(warningTime);
		}
	}

	public static final class SetWarningBlocksAction extends Action {

		public int warningBlocks;

		public SetWarningBlocksAction(int warningBlocks) {
			this.warningBlocks = warningBlocks;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(5);
			dest.writeVarInt(warningBlocks);
		}
	}

}
