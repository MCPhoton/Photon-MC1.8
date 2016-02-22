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
import java.util.UUID;
import org.mcphoton.entity.Entity;
import org.mcphoton.network.SendablePacket;
import com.electronwill.collections.OpenList;

/**
 * @author ElectronWill
 */
public final class EntityPropertiesPacket extends SendablePacket {

	public static final String MAX_HEALTH = "generic.maxHealth", FOLLOW_RANGE = "generic.followRange", KNOCKBACK_RESISTANCE = "generic.knockbackResistance",
		MOVE_SPEED = "generic.movementSpeed", ATTACK_DAMAGE = "generic.attackDamage", HORSE_JUMP_STRENGTH = "horse.jumpStrength", ZOMBIE_REINFORCEMENTS = "zombie.spawnReinforcements";

	public Entity entity;
	public OpenList<EntityProperty> properties;

	public EntityPropertiesPacket(Entity entity) {
		this(entity, new OpenList<>(1));
	}

	public EntityPropertiesPacket(Entity entity, OpenList<EntityProperty> properties) {
		this.entity = entity;
		this.properties = properties;
	}

	public static final class PropertyModifier {

		public UUID uid;
		public double amount;
		public byte operation;

		public PropertyModifier(UUID uid, double amount, byte operation) {
			this.uid = uid;
			this.amount = amount;
			this.operation = operation;
		}
	}

	public static final class EntityProperty {

		public String key;
		public double value;
		public OpenList<PropertyModifier> modifiers = new OpenList<>(1);

		public EntityProperty(String key, double value) {
			this.key = key;
			this.value = value;
		}

		public void addModifier(UUID uid, double amount, byte operation) {
			PropertyModifier modifier = new PropertyModifier(uid, amount, operation);
			modifiers.add(modifier);
		}

		public void removeModifier(UUID uid, double amount, byte operation) {
			for (int i = 0; i < modifiers.size(); i++) {
				PropertyModifier modifier = modifiers.get(i);
				if (modifier.uid.equals(uid) && modifier.amount == amount && modifier.operation == operation) {
					modifiers.remove(i);
				}
			}
		}

		public void removeModifier(UUID uid) {
			for (int i = 0; i < modifiers.size(); i++) {
				PropertyModifier modifier = modifiers.get(i);
				if (modifier.uid.equals(uid)) {
					modifiers.remove(i);
				}
			}
		}
	}

	public void removeProperty(String key) {
		for (int i = 0; i < properties.size(); i++) {
			EntityProperty get = properties.get(i);
			if (get.key.equals(key)) {
				properties.remove(i);
				return;
			}
		}
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(entity.getType().getId());
		dest.writeInt(properties.size());
		for (EntityProperty p : properties) {
			dest.writeString(p.key);
			dest.writeDouble(p.value);
			dest.writeVarInt(p.modifiers.size());
			for (PropertyModifier modifier : p.modifiers) {
				dest.writeLong(modifier.uid.getMostSignificantBits());
				dest.writeLong(modifier.uid.getLeastSignificantBits());
				dest.writeDouble(modifier.amount);
				dest.write(modifier.operation);
			}
		}

	}

	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int id() {
		return 0x20;
	}

}
