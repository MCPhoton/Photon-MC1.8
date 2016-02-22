package org.mcphoton.core.listeners;

import java.util.Collection;
import java.util.Iterator;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.EventHandler;
import org.mcphoton.event.impl.PlayerMoveEvent;
import org.mcphoton.network.clientbound.play.ChunkDataPacket;
import org.mcphoton.security.AccessPermit;
import org.mcphoton.security.RootPermit;
import org.mcphoton.world.Chunk;

/**
 * Sends chunks to the player when he/she moves.
 * 
 * @author ElectronWill
 * 		
 */
public class PlayerMoveListener implements EventHandler<PlayerMoveEvent> {
	
	private static final AccessPermit internalPermit;
	
	static {
		AccessPermit p = null;
		try {
			p = new RootPermit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		internalPermit = p;
	}
	
	@Override
	public void handle(PlayerMoveEvent event) {
		OnlinePlayer player = event.getPlayer();
		int cx = (int) (event.getNewX() / 16);
		int cz = (int) (event.getNewZ() / 16);
		
		player.setX(event.getNewX());
		player.setY(event.getNewY());
		player.setZ(event.getNewZ());
		
		if (cx == event.getCurrentX() / 16 && cz == event.getCurrentZ() / 16)
			return;
			
		Collection<Chunk> chunks = player.getLoadedChunks();
		
		// Cleans unused chunks:
		Iterator<Chunk> it = chunks.iterator();
		while (it.hasNext()) {
			Chunk c = it.next();
			if (Math.abs(c.x() - cx) > Photon.getViewDistance()) {
				it.remove();
			}
		}
		
		// Looks for needed chunks:
		for (int d = 0; d <= Photon.getViewDistance(); d++) {
			for (int relativeX = -d; relativeX <= d; relativeX++) {
				for (int relativeZ = -d; relativeZ <= d; relativeZ++) {
					int neededX = cx + relativeX;
					int neededZ = cz + relativeZ;
					boolean alreadyLoaded = false;
					for (Chunk c : chunks) {
						if (c.x() == neededX && c.z() == neededZ)
							alreadyLoaded = true;
					}
					if (alreadyLoaded)
						continue;
					try {
						Chunk chunk = player.getWorld().getChunk(neededX, neededZ, true, internalPermit);
						chunks.add(chunk);
						ChunkDataPacket packet = new ChunkDataPacket(chunk, true);
						packet.sendTo(player);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
}
