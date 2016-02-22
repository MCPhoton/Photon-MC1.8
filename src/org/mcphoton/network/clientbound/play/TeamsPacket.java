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
import com.electronwill.collections.OpenList;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 *
 * @author ElectronWill
 */
public final class TeamsPacket extends SendablePacket {

	public static final byte CREATE = 0, REMOVE = 1, UPDATE = 2, ADD_PLAYER = 3, REMOVE_PLAYER = 4;

	public String teamName;
	public TeamProperty property;

	public TeamsPacket(String teamName, TeamProperty property) {
		this.teamName = teamName;
		this.property = property;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(teamName);
		property.writeTo(dest);
	}

	@Override
	public int id() {
		return 0x3E;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	public static abstract class TeamProperty implements Writeable {

		@Override
		public abstract void writeTo(EasyOutputStream out) throws IOException;
	}

	public static final class CreateTeamProperty extends TeamProperty {

		public String displayName, prefix, suffix, nameTagVisibility;
		public byte friendlyFire, color;
		public OpenList<String> players;

		public CreateTeamProperty(String displayName, String prefix, String suffix, String nameTagVisibility, byte friendlyFire, byte color, OpenList<String> players) {
			this.displayName = displayName;
			this.prefix = prefix;
			this.suffix = suffix;
			this.nameTagVisibility = nameTagVisibility;
			this.friendlyFire = friendlyFire;
			this.color = color;
			this.players = players;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.write(0);
			dest.writeString(displayName);
			dest.writeString(prefix);
			dest.writeString(suffix);
			dest.write(friendlyFire);
			dest.writeString(nameTagVisibility);
			dest.write(color);
			dest.writeVarInt(players.size());
			for (String s : players) {
				dest.writeString(s);
			}
		}
	}

	public static final class RemoveTeamProperty extends TeamProperty {

		@Override
		public void writeTo(EasyOutputStream out) throws IOException {
			out.write(1);
		}
	}

	public static final class UpdateTeamProperty extends TeamProperty {

		public String displayName, prefix, suffix, nameTagVisibility;
		public byte friendlyFire, color;

		public UpdateTeamProperty(String displayName, String prefix, String suffix, String nameTagVisibility, byte friendlyFire, byte color) {
			this.displayName = displayName;
			this.prefix = prefix;
			this.suffix = suffix;
			this.nameTagVisibility = nameTagVisibility;
			this.friendlyFire = friendlyFire;
			this.color = color;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.write(2);
			dest.writeString(displayName);
			dest.writeString(prefix);
			dest.writeString(suffix);
			dest.write(friendlyFire);
			dest.writeString(nameTagVisibility);
			dest.write(color);
		}
	}

	public static final class AddPlayerProperty extends TeamProperty {

		public OpenList<String> addedPlayers;

		public AddPlayerProperty(OpenList<String> addedPlayers) {
			this.addedPlayers = addedPlayers;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.write(3);
			dest.writeVarInt(addedPlayers.size());
			for (String s : addedPlayers) {
				dest.writeString(s);
			}
		}
	}

	public static final class RemovePlayerProperty extends TeamProperty {

		public OpenList<String> removedPlayers;//Note: Maximum 40 characters per player name.

		public RemovePlayerProperty(OpenList<String> removedPlayers) {
			this.removedPlayers = removedPlayers;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.write(4);
			dest.writeVarInt(removedPlayers.size());
			for (String s : removedPlayers) {
				dest.writeString(s);
			}
		}
	}

}
