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
import java.util.List;
import org.mcphoton.entity.Entity;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class DestroyEntitiesPacket extends SendablePacket {
	
	public Entity[] entities;
	
	public DestroyEntitiesPacket(List<Entity> entities) {
		this.entities = entities.toArray(new Entity[entities.size()]);
	}
	
	public DestroyEntitiesPacket(Entity... entities) {
		this.entities = entities;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(entities.length);
		for (Entity e : entities) {
			dest.writeVarInt(e.getEntityId());
		}
	}
	
	@Override
	public int maxDataSize() {
		return 5 * entities.length + 5;
	}
	
	@Override
	public int id() {
		return 0x13;
	}
	
}
