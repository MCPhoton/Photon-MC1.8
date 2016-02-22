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
package org.mcphoton.entity.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import org.mcphoton.Gamemode;
import org.mcphoton.Player;
import org.mcphoton.command.CommandSender;
import org.mcphoton.entity.EntityType;
import org.mcphoton.entity.LivingEntity;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.util.Location;
import org.mcphoton.world.Chunk;

/**
 * An online player.
 * 
 * @author ElectronWill
 */
public class OnlinePlayer extends LivingEntity implements Player, CommandSender {
	
	private final ClientInfos clientInfos;
	private final String name;
	private final UUID uid;
	private final Collection<Chunk> chunks = new LinkedList<Chunk>();
	private volatile Gamemode gamemode;
	private volatile Location spawnLocation, respawnLocation;
	
	public OnlinePlayer(ClientInfos infos, UUID uid, String name, Gamemode gamemode, Location spawn, Location respawn) {
		this.clientInfos = infos;
		this.uid = uid;
		this.name = name;
		this.gamemode = gamemode;
		this.spawnLocation = spawn;
		this.respawnLocation = respawn;
	}
	
	@Override
	public UUID getAccountId() {
		return uid;
	}
	
	/**
	 * Gets the chunks currently loaded for/by this player.
	 */
	public Collection<Chunk> getLoadedChunks() {
		return chunks;
	}
	
	public ClientInfos getClientInfos() {
		return clientInfos;
	}
	
	@Override
	public Gamemode getGamemode() {
		return gamemode;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Location getRespawnLocation() {
		return respawnLocation;
	}
	
	@Override
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	@Override
	public EntityType getType() {
		return null;
	}
	
	@Override
	public String getTypeName() {
		return "player";
	}
	
	public final boolean hasPermission(String permission) {
		return true;// TODO check permission
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void sendMessage(CharSequence msg) {
		// TODO
	}
	
	@Override
	public void sendMessage(ChatMessage msg) {
		// TODO
	}
	
	@Override
	public void setGamemode(Gamemode mode) {
		this.gamemode = mode;
	}
	
	@Override
	public void setRespawnLocation(Location newLocation) {
		respawnLocation = newLocation;
	}
	
	@Override
	public void setSpawnLocation(Location newLocation) {
		spawnLocation = newLocation;
	}
	
}
