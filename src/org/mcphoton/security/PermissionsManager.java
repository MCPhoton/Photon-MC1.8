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

/**
 * Interface for classes that are able to handle the permissions of any object on the server.
 *
 * @author ElectronWill
 */
public interface PermissionsManager {
	
	/**
	 * Checks if a permission is granted to an object.
	 */
	boolean hasPermission(Object o, String permission);
	
	/**
	 * Checks if a default value has been set for a permission.
	 */
	boolean isDefaultSet(String permission);
	
	/**
	 * Checks if a permission is explicitely set for an object.
	 */
	boolean isPermissionSet(Object o, String permission);
	
	/**
	 * Sets the default value of a permission.
	 *
	 * @param permission the permission
	 * @param granted true to grant it by default, false to deny it
	 */
	void setDefault(String permission, boolean granted);
	
	/**
	 * Sets some permission of an object.
	 */
	void setPermission(Object o, PermissionSetting setting);
	
	/**
	 * Unsets every permission given by a specific source to an object.
	 *
	 * @return true if it was unset.
	 */
	boolean unsetPermission(Object o, Object source);
	
	/**
	 * Unsets some permission of an object. This will remove the exact given PermissionSetting, not those with the same
	 * permission.
	 *
	 * @return true if it was unset.
	 */
	boolean unsetPermission(Object o, PermissionSetting setting);
	
	/**
	 * Unsets some permission of an object. This will remove any PermissionSetting with the same permission, and so
	 * resets the permission's setting to its default value.
	 *
	 * @return true if it was unset.
	 */
	boolean unsetPermission(Object o, String permission);
	
}
