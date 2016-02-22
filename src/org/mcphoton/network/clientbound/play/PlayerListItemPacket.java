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
import java.util.UUID;
import org.mcphoton.Gamemode;
import org.mcphoton.network.SendablePacket;
import com.electronwill.collections.OpenList;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 *
 * @author ElectronWill
 */
public final class PlayerListItemPacket extends SendablePacket {

	public Action action;
	public int playerNumber;
	public UUID playerUuid;

	public PlayerListItemPacket(Action action, int playerNumber, UUID playerUuid) {
		this.action = action;
		this.playerNumber = playerNumber;
		this.playerUuid = playerUuid;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(0);
		dest.writeVarInt(playerNumber);
		dest.writeLong(playerUuid.getMostSignificantBits());
		dest.writeLong(playerUuid.getLeastSignificantBits());
		action.writeTo(dest);
	}

	@Override
	public int id() {
		return 0x38;
	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	public static abstract class Action implements Writeable {

		@Override
		public abstract void writeTo(EasyOutputStream out) throws IOException;
	}

	public static final class AddPlayerAction extends Action {

		public Optional<String> displayName;
		public Gamemode gamemode;
		public int ping;
		public String playerName;
		public OpenList<Property> properties;

		public AddPlayerAction(String playerName, OpenList<Property> properties, Gamemode gamemode, int ping) {
			this.playerName = playerName;
			this.properties = properties;
			this.gamemode = gamemode;
			this.ping = ping;
			this.displayName = Optional.empty();
		}

		public AddPlayerAction(String playerName, OpenList<Property> properties, Gamemode gamemode, int ping, String displayName) {
			this.playerName = playerName;
			this.properties = properties;
			this.gamemode = gamemode;
			this.ping = ping;
			this.displayName = Optional.of(displayName);
		}

		public AddPlayerAction(String playerName, Gamemode gamemode, int ping) {
			this.playerName = playerName;
			this.properties = new OpenList<>(2);
			this.gamemode = gamemode;
			this.ping = ping;
			this.displayName = Optional.empty();
		}

		public AddPlayerAction(String playerName, Gamemode gamemode, int ping, String displayName) {
			this.playerName = playerName;
			this.properties = new OpenList<>(2);
			this.gamemode = gamemode;
			this.ping = ping;
			this.displayName = Optional.of(displayName);
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(0);
			dest.writeString(playerName);
			if (properties == null) {
				dest.writeVarInt(0);
			} else {
				dest.writeVarInt(properties.size());
				for (Property p : properties) {
					dest.writeString(p.name);
					dest.writeString(p.value);
					dest.write(p.signature.isPresent() ? 1 : 0);
					if (p.signature.isPresent()) {
						dest.writeString(p.signature.get());
					}
				}
			}
			dest.writeVarInt(gamemode.ordinal());
			dest.writeVarInt(ping);
			dest.write(displayName.isPresent() ? 1 : 0);
			if (displayName.isPresent()) {
				dest.writeString(displayName.get());
			}
		}

	}

	public static final class Property {

		public String name;
		public String value;
		public Optional<String> signature;

		public Property(String name, String value) {
			this.name = name;
			this.value = value;
			this.signature = Optional.empty();
		}

		public Property(String name, String value, String signature) {
			this.name = name;
			this.value = value;
			this.signature = Optional.of(signature);
		}
	}

	public static final class RemovePlayerAction extends Action {

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(4);
		}

	}

	public static final class UpdateDisplayNameAction extends Action {

		public Optional<String> displayName;

		public UpdateDisplayNameAction(Optional<String> displayName) {
			this.displayName = displayName;
		}

		public UpdateDisplayNameAction(String displayName) {
			this.displayName = Optional.of(displayName);
		}

		public UpdateDisplayNameAction() {
			this.displayName = Optional.empty();
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(3);
			dest.writeBoolean(displayName.isPresent());
			if (displayName.isPresent()) {
				dest.writeString(displayName.get());
			}
		}
	}

	public static final class UpdateGamemodeAction extends Action {

		public Gamemode gamemode;

		public UpdateGamemodeAction(Gamemode gamemode) {
			this.gamemode = gamemode;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(1);
			dest.writeVarInt(gamemode.ordinal());
		}

	}

	public static final class UpdateLatencyAction extends Action {

		public int ping;

		public UpdateLatencyAction(int ping) {
			this.ping = ping;
		}

		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(2);
			dest.writeInt(ping);
		}

	}

}
