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
package org.mcphoton.network.serverbound.play;

import org.mcphoton.item.ItemStack;
import org.mcphoton.network.ClientInfos;
import org.mcphoton.network.ReceivablePacket;
import com.electronwill.streams.EasyInputStream;

/**
 *
 * @author ElectronWill
 */
public final class PlayerBlockPlacementPacket extends ReceivablePacket {
	
	public int blockX, blockY, blockZ;
	public byte face, cursorX, cursorY, cursorZ;
	public ItemStack heldItem;
	
	public PlayerBlockPlacementPacket(ClientInfos client, EasyInputStream in) throws Throwable {
		super(client, in);
		long pos = in.readLong();
		// TODO read all
		
	}
	
	@Override
	public int id() {
		return 0x08;
	}
	
	@Override
	public void handle() {
		// TODO /!\Warning there is a special case: x,z=-1 and y=255
		// In that case the client does NOT place any block but asks for an update of its held item
		// --> update Player's inventory
		
		// TODO update ChunkData
		// TODO notify Players
		// TODO update entities AI Paths
	}
	
}
