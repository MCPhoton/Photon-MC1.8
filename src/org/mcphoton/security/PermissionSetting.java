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
package org.mcphoton.security;

import java.util.Objects;

/**
 * An permission setting that contains several informations:
 * <ol>
 * <li>The permission's name</li>
 * <li>If it is granted or denied</li>
 * <li>Who/What set the permission</li>
 *
 * @author ElectronWill
 */
public class PermissionSetting {
	
	private final String permission;
	private volatile boolean granted;
	private final Object source;
	
	/**
	 * Creates a new PermissionSetting.
	 *
	 * @param permission permission's name
	 * @param granted true if the permission is granted, false if it's denied
	 * @param source source of this attachment. You should use the <b>this</b> keyword.
	 */
	public PermissionSetting(String permission, boolean granted, Object source) {
		this.permission = permission;
		this.granted = granted;
		this.source = source;
	}
	
	/**
	 * Create a new PermissionSetting which grants the permission (granted = true).
	 *
	 * @param permission permission's name
	 * @param source source of this attachment. You should use the <b>this</b> keyword.
	 */
	public PermissionSetting(String permission, Object source) {
		this.permission = permission;
		this.granted = true;
		this.source = source;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PermissionSetting)) {
			return false;
		}
		PermissionSetting attachment = (PermissionSetting) obj;
		return attachment.permission.equals(permission) && attachment.source.equals(source);
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + Objects.hashCode(this.permission);
		hash = 67 * hash + Objects.hashCode(this.source);
		return hash;
	}
	
	/**
	 * Checks if this permssion is explicitly granted.
	 */
	public boolean isGranted() {
		return granted;
	}
	
	/**
	 * Checks if this attachment's permission is equals to the given one.
	 */
	public boolean permEquals(String s) {
		return permission.equals(s);
	}
	
	/**
	 * Gets the permission's name.
	 */
	public String permission() {
		return permission;
	}
	
	/**
	 * Returns {@code permission().split("\\.")}.
	 */
	public String[] permParts() {
		return permission.split("\\.");
	}
	
	/**
	 * Sets if this permission is granted (true) or denied (false).
	 *
	 * @param granted true if this permission should be granted, false if it should be denied.
	 */
	public void setGranted(boolean granted) {
		this.granted = granted;
	}
	
	/**
	 * Gets the object who "created" this PermissionSetting.
	 */
	public Object source() {
		return source;
	}
	
}
