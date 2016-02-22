package org.mcphoton;

import java.util.UUID;
import org.mcphoton.command.CommandSender;
import org.mcphoton.util.Location;

/**
 * Interface for players, online or offline.
 * 
 * @author ElectronWill
 * 		
 */
public interface Player extends CommandSender {
	
	/**
	 * Checks if this player is currently online.
	 * 
	 * @return true if it is online, false if it is offline
	 */
	boolean isOnline();
	
	/**
	 * Gets the player's account unique id.
	 */
	UUID getAccountId();
	
	/**
	 * Gets the player's name.
	 */
	String getName();
	
	/**
	 * Gets the player's gamemode
	 */
	Gamemode getGamemode();
	
	/**
	 * Sets the player's gamemode.
	 */
	void setGamemode(Gamemode mode);
	
	/**
	 * Gets the last known player's location.
	 */
	Location getLocation();
	
	/**
	 * Gets the player's spawn location.
	 */
	Location getSpawnLocation();
	
	/**
	 * Sets the player's spawn location.
	 */
	void setSpawnLocation(Location l);
	
	/**
	 * Gets the player's respawn location.
	 */
	Location getRespawnLocation();
	
	/**
	 * Sets the player's spawn location.
	 */
	void setRespawnLocation(Location l);
}
