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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.util.Location;
import org.mcphoton.util.ProtocolData;

/**
 *
 * @author ElectronWill
 */
public final class JoinPacket extends SendablePacket {
	
	public static final String DEFAULT = "default", FLAT = "flat", LARGE_BIOMES = "largeBiomes", AMPLIFIED = "amplified",
			DEFAULT_1_1 = "default_1_1";
			
	public int entityId;
	public byte gamemode, difficulty, worldTypeId;
	public int maxPlayers;
	public String worldTypeString;
	public boolean reducedDebug;
	public boolean changed = true;
	
	public JoinPacket(OnlinePlayer p, boolean reducedDebug) {
		final Location l = p.getLocation();
		gamemode = p.getGamemode().id;
		this.reducedDebug = reducedDebug;
		entityId = p.getEntityId();
		worldTypeId = l.getWorld().getTypeId();
		difficulty = (byte) l.getWorld().getDifficulty().ordinal();
		maxPlayers = Photon.getMaxPlayers();
		worldTypeString = DEFAULT;
	}
	
	public JoinPacket(int entityId, byte gamemode, byte difficulty, byte worldTypeId, int maxPlayers, String worldTypeString,
			boolean reducedDebug) {
		this.entityId = entityId;
		this.gamemode = gamemode;
		this.difficulty = difficulty;
		this.worldTypeId = worldTypeId;
		this.maxPlayers = maxPlayers;
		this.worldTypeString = worldTypeString;
		this.reducedDebug = reducedDebug;
	}
	
	public JoinPacket(OnlinePlayer p) {
		this(p, false);
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeInt(entityId);
		dest.write(gamemode);
		dest.write(worldTypeId);
		dest.write(difficulty);
		dest.write(ProtocolData.asUnsignedByte(maxPlayers));
		dest.writeString(worldTypeString);
		dest.writeBoolean(reducedDebug);
	}
	
	@Override
	public int maxDataSize() {
		return 23;
	}
	
	@Override
	public int id() {
		return 0x01;
	}
}
