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
import org.mcphoton.entity.Entity;
import org.mcphoton.network.SendablePacket;

/**
 *
 * @author ElectronWill
 */
public final class EntityStatusPacket extends SendablePacket {

	public static final byte STATUS_1 = 1, HURT = 2, DEAD = 3, IRONGOLEM_THROW_ARMS = 4, TAMING = 5, TAMED = 6, WOLF_SHAKING = 7, EATING = 8, SHEEP_EATING = 9, PLAY_TNT_IGNITE_SOUND = 10, IRONGOLEM_ROSE = 11, VILLAGER_MATING = 12, VILLAGER_ANGRY = 13, VILLAGER_HAPPY = 14, WITCH_PARTICLE = 15, ZOMBIE_CONVERSION = 16, FIREWORK_EXPLODING = 17, ANIMAL_LOVE = 18, RESET_SQUID_ROTATION = 19, EXPLOSION_PARTICLE = 20, GUARDIAN_SOUND = 21, ENABLE_REDUCED_DEBUG = 22, DISABLE_REDUCED_DEBUG = 23;

	public Entity entity;
	public byte status;

	public EntityStatusPacket(Entity entity, byte status) {
		this.entity = entity;
		this.status = status;
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(entity.getType().getId());
		dest.write(status);
	}

	@Override
	public int maxDataSize() {
		return 6;
	}

	@Override
	public int id() {
		return 0x1A;
	}

}
