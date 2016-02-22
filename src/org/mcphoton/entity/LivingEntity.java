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
package org.mcphoton.entity;

import java.util.Optional;
import com.electronwill.concurrent.AtomicFloat;

/**
 * Abstract class for all living entities.
 *
 * @author ElectronWill
 */
public abstract class LivingEntity extends Entity implements MobEntity {
	
	protected volatile Optional<String> customName = Optional.empty();
	protected final AtomicFloat hp = new AtomicFloat(20f);
	protected volatile float pitch, yaw;
	
	@Override
	public float getPitch() {
		return pitch;
	}
	
	@Override
	public float getYaw() {
		return yaw;
	}
	
	@Override
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	@Override
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public Optional<String> getCustomName() {
		return customName;
	}
	
	public boolean hasCustomName() {
		return customName.isPresent();
	}
	
	public void setCustomName(String customName) {
		this.customName = Optional.ofNullable(customName);
	}
	
	public void heal(float amount) {
		hp.addAndGet(amount);
	}
	
	public void setHp(float hp) {
		this.hp.set(hp);
	}
	
	public float getHp() {
		return hp.get();
	}
	
	@Override
	public boolean isMob() {
		return true;
	}
	
	@Override
	public boolean isObject() {
		return false;
	}
	
}
