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
package org.mcphoton.network.serverbound.login;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ConnectionState;
import org.mcphoton.network.ReceivablePacket;
import org.mcphoton.network.clientbound.login.LoginSuccessPacket;
import org.mcphoton.network.clientbound.play.ChunkDataPacket;
import org.mcphoton.network.clientbound.play.JoinPacket;
import org.mcphoton.network.clientbound.play.KeepAlivePacket;
import org.mcphoton.network.clientbound.play.PlayerAbilitiesPacket;
import org.mcphoton.network.clientbound.play.PlayerPositionAndLookPacket;
import org.mcphoton.network.clientbound.play.PluginMessagePacket;
import org.mcphoton.network.clientbound.play.SetDifficultyPacket;
import org.mcphoton.network.clientbound.play.SpawnPositionPacket;
import org.mcphoton.security.AccessDeniedException;
import org.mcphoton.security.AccessPermit;
import org.mcphoton.security.RootPermit;
import org.mcphoton.util.Location;
import org.mcphoton.util.MCUtils;
import org.mcphoton.world.Chunk;
import com.electronwill.streams.ByteArrayOutputStream;
import com.electronwill.streams.EasyInputStream;

/**
 *
 * @author ElectronWill
 */
public final class LoginStartPacket extends ReceivablePacket {
	
	private static final AccessPermit internalPermit;
	
	static {
		AccessPermit p = null;
		try {
			p = new RootPermit();
		} catch (AccessDeniedException ex) {
			ex.printStackTrace();
		}
		internalPermit = p;
	}
	
	public final String playerName;
	
	public LoginStartPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		playerName = in.readString();
	}
	
	@Override
	public int id() {
		return 0;
	}
	
	@Override
	public void handle() throws Throwable {
		// TODO encryption + authentification (includes getting the player's UUID)
		UUID uid = MCUtils.generateUidFromName(playerName);
		
		// Creates the Player object:
		// TODO Player knownPlayer = Photon.getKnownPlayers().get(uid);
		Location spawn = Photon.getSpawn();
		OnlinePlayer onlinePlayer = new OnlinePlayer(client, uid, playerName, spawn.getWorld().getGamemode(), spawn, spawn);
		
		// TODO Photon.getKnownPlayers().put(uid, onlinePlayer);
		Photon.addPlayer(onlinePlayer);
		
		// Login success:
		LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket(uid.toString(), playerName);
		loginSuccessPacket.sendTo(client);
		client.setState(ConnectionState.PLAY);
		client.setPlayer(onlinePlayer);
		
		// Spawn player entity:
		spawn.getWorld().spawnEntity(onlinePlayer, spawn.getX(), spawn.getY(), spawn.getZ());
		
		// Join game:
		JoinPacket joinPacket = new JoinPacket(onlinePlayer);
		joinPacket.sendTo(client);
		
		// Server brand:
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.writeString("Photon");
		PluginMessagePacket pmPacket = new PluginMessagePacket("MC|Brand", baos.toByteArray());
		pmPacket.sendTo(client);
		
		Location playerSpawn = onlinePlayer.getSpawnLocation();
		playerSpawn.setY(80);
		
		// Difficulty:
		SetDifficultyPacket difficultyPacket = new SetDifficultyPacket(playerSpawn.getWorld().getDifficulty());
		difficultyPacket.sendTo(client);
		
		// Spawn position:
		SpawnPositionPacket spawnPositionPacket = new SpawnPositionPacket(playerSpawn);
		spawnPositionPacket.sendTo(client);
		
		// Position and look:
		PlayerPositionAndLookPacket positionAndLookPacket = new PlayerPositionAndLookPacket(playerSpawn.getX(), playerSpawn.getY(),
				playerSpawn.getZ(), 0, 0, (byte) 0);
		positionAndLookPacket.sendTo(client);
		
		// Abilities:
		PlayerAbilitiesPacket abilitiesPacket = new PlayerAbilitiesPacket(1f, 1f, true, true, true, true);
		abilitiesPacket.sendTo(client);
		
		// Map chunks:
		// onlinePlayer.handleMove(playerSpawn.getBlockX(), playerSpawn.getBlockZ(), true);
		for (int cx = playerSpawn.getBlockX() / 32 - 2; cx <= playerSpawn.getBlockX() / 32 + 2; cx++) {
			for (int cz = playerSpawn.getBlockZ() / 32 - 2; cz <= playerSpawn.getBlockZ() / 32 + 2; cz++) {
				Chunk chunk = playerSpawn.getWorld().getChunk(cx, cz, true, internalPermit);
				ChunkDataPacket chunkPacket = new ChunkDataPacket(chunk, true);
				chunkPacket.sendTo(client);
			}
		}
		
		// Keep Alive:
		Runnable r = () -> {
			KeepAlivePacket kap = new KeepAlivePacket(1);
			try {
				kap.sendTo(client);
			} catch (Exception e) {
				if (client.isClientOnline())
					Photon.log.error(e, "Unable to send KeepAlivePacket");
				return;
			}
		};
		Photon.executorService().scheduleWithFixedDelay(r, 0, 15, TimeUnit.SECONDS);
	}
	
	@Override
	public String toString() {
		return "LoginStartPacket: " + playerName;
	}
	
}
