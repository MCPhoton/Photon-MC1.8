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
package org.mcphoton.plugin;

import java.io.File;
import org.mcphoton.security.AccessPermit;

/**
 * An abstract photon plugin bundled in a jar file. The jar file must contain a file named "plugin.properties"
 * containing the essential plugin's informations.
 *
 * @author ElectronWill
 */
public abstract class JavaPlugin extends Plugin {
	
	/**
	 * true if the plugin is enabled, false otherwise.
	 */
	protected volatile boolean enabled;
	/**
	 * The plugin's log.
	 */
	protected final PluginLog log;
	/**
	 * The plugin's permit, that allows or disallows it to do privileged actions, like modifying blocks or setting
	 * player's permissions.
	 */
	protected final AccessPermit accessPermit;
	
	/**
	 * Creates a new JavaPlugin with the given parameters. It is necessary to make these fields (version, name, etc.)
	 * final.
	 *
	 * @param spm the SpecificPluginsManager that loaded this plugin
	 * @param name the plugin's name, as read from the plugin.properties file
	 * @param version the plugin's version, as read from the plugin.properties file
	 * @param authors the plugin's author(s), as read from the plugin.properties file
	 * @param directory the plugin's directory, determined by the PluginsManager
	 * @param configFile the plugin's config file, determined by the PluginsManager
	 */
	public JavaPlugin(SpecificPluginsManager<?> spm, String name, String version, String authors, File directory, File configFile) {
		super(spm, name, version, authors, directory, configFile);
		log = new PluginLog(name);
		accessPermit = GlobalPluginsManager.getAccessPermit(spm, name, version, authors, directory, configFile);
	}
	
	@Override
	public final boolean isEnabled() {
		return enabled;
	}
	
}
