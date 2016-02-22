package org.mcphoton;

import java.io.IOException;
import java.util.UUID;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.util.Location;
import com.electronwill.streams.EasyInputStream;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 * An offline player. This class gives access to several informations about the player. These informations were
 * collected the last time this player connected to the server. Any update to these informations (like gamemode or
 * location) will be taken into account on the next player's connection.
 * 
 * @author ElectronWill
 * 		
 */
public final class OfflinePlayer implements Player, Writeable {
	
	private final UUID uid;
	private final String name;
	private volatile Gamemode gamemode;
	private volatile Location lastLocation, spawnLocation, respawnLocation;
	
	public OfflinePlayer(UUID uid, String name, Gamemode gamemode, Location lastLocation, Location spawnLocation,
			Location respawnLocation) {
		this.uid = uid;
		this.name = name;
		this.gamemode = gamemode;
		this.lastLocation = lastLocation;
		this.spawnLocation = spawnLocation;
		this.respawnLocation = respawnLocation;
	}
	
	public OfflinePlayer(EasyInputStream input) throws IOException {
		this.uid = new UUID(input.readLong(), input.readLong());
		this.name = input.readString();
		this.gamemode = Gamemode.valueOf(input.readString());
		this.lastLocation = new Location(input.readString());
		this.spawnLocation = new Location(input.readString());
		this.respawnLocation = new Location(input.readString());
	}
	
	@Override
	public boolean isOnline() {
		return false;
	}
	
	@Override
	public UUID getAccountId() {
		return uid;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Gamemode getGamemode() {
		return gamemode;
	}
	
	@Override
	public void setGamemode(Gamemode mode) {
		this.gamemode = mode;
	}
	
	@Override
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	@Override
	public void setSpawnLocation(Location l) {
		this.spawnLocation = l;
	}
	
	@Override
	public Location getRespawnLocation() {
		return respawnLocation;
	}
	
	@Override
	public void setRespawnLocation(Location l) {
		this.respawnLocation = l;
	}
	
	@Override
	public Location getLocation() {
		return lastLocation;
	}
	
	public Location getLastLocation() {
		return lastLocation;
	}
	
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}
	
	@Override
	public void writeTo(EasyOutputStream out) throws IOException {
		out.writeLong(uid.getMostSignificantBits());
		out.writeLong(uid.getLeastSignificantBits());
		out.writeString(gamemode.toString());
		out.writeString(lastLocation.toString());
		out.writeString(spawnLocation.toString());
		out.writeString(respawnLocation.toString());
	}
	
	@Override
	public void sendMessage(CharSequence msg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendMessage(ChatMessage msg) {
		// TODO Auto-generated method stub
		
	}
	
}
