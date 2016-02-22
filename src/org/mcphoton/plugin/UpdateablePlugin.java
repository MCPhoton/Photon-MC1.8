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
package org.mcphoton.plugin;

/**
 * A PhotonPlugin implementing this interface provides facilities for versioning and updates checks.
 *
 * @author ElectronWill
 */
public interface UpdateablePlugin extends Comparable<UpdateablePlugin> {

	/**
	 * Returns the plugin's version String.
	 *
	 * @return
	 */
	public String getVersion();

	/**
	 * Returns the latest available version of this plugin. Implementations may, for example, get
	 * informations from a remote server.
	 *
	 * @return
	 * @throws java.lang.Exception
	 */
	public String getLatestAvailableVersion() throws Exception;

	/**
	 * Compares two versions. Returns a positive number if versionA is newer, 0 if they are the
	 * same, and a negative number if versionB is newer.
	 *
	 * @param versionA
	 * @param versionB
	 * @return a positive number if versionA is newer, 0 if they are the same, and a negative number
	 * if versionB is newer.
	 */
	public int compareVersions(String versionA, String versionB);

	/**
	 * Compares this plugin's version to another one. Returns a positive number if this plugin's
	 * version is newer, 0 if they are the same, and a negative number if versionB is newer.<br>
	 * Calling this method is the same as
	 * {@code plugin.compareVersions(plugin.getVersion(),versionB)}.
	 *
	 * @param versionB
	 * @return a positive number if this plugin's version is newer, 0 if they are the same, and a
	 * negative number if versionB is newer.
	 */
	public int compareVersions(String versionB);

	/**
	 * Compares this plugin's version to another one.
	 *
	 * @param o
	 * @return
	 */
	@Override
	public default int compareTo(UpdateablePlugin o) {
		return compareVersions(o.getVersion());
	}

}
