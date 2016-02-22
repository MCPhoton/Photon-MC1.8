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
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.network.SendablePacket;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 *
 * @author ElectronWill
 */
public final class CombatEventPacket extends SendablePacket {
	
	public CombatEvent event;
	
	public CombatEventPacket(CombatEvent event) {
		this.event = event;
	}
	
	public static abstract class CombatEvent implements Writeable {
		
		abstract int maxDataSize();
		
		@Override
		public abstract void writeTo(EasyOutputStream out) throws IOException;
	}
	
	public static final class EnterCombatEvent extends CombatEvent {
		
		@Override
		int maxDataSize() {
			return 1;
		}
		
		@Override
		public void writeTo(EasyOutputStream out) throws IOException {
			out.writeVarInt(0);
		}
	}
	
	public static final class EndCombatEvent extends CombatEvent {
		
		public int duration, entityId;
		
		@Override
		int maxDataSize() {
			return 6;
		}
		
		public EndCombatEvent(int duration, int entityId) {
			this.duration = duration;
			this.entityId = entityId;
		}
		
		@Override
		public void writeTo(EasyOutputStream out) throws IOException {
			out.writeVarInt(1);
			out.writeVarInt(duration);
			out.writeVarInt(entityId);
		}
	}
	
	public static final class DeadCombatEvent extends CombatEvent {
		
		public OnlinePlayer player;
		public String message;
		
		@Override
		int maxDataSize() {
			return Integer.MAX_VALUE;
		}
		
		public DeadCombatEvent(OnlinePlayer player, String message) {
			this.player = player;
			this.message = message;
		}
		
		@Override
		public void writeTo(EasyOutputStream out) throws IOException {
			out.writeVarInt(2);
			out.writeVarInt(player.getEntityId());
			out.writeString(message);
		}
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		event.writeTo(dest);
	}
	
	@Override
	public int maxDataSize() {
		return event.maxDataSize();
	}
	
	@Override
	public int id() {
		return 0x42;
	}
	
}
