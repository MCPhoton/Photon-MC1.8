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

/**
 * Base class for all plugins.
 *
 * @author ElectronWill
 */
public abstract class Plugin {
	
	protected final File directory, configFile;
	protected final String name, version, authors;
	final SpecificPluginsManager spm;
	
	/**
	 * Creates a new PhotonPlugin.
	 *
	 * @param name the plugin's name
	 * @param version the plugin's version
	 * @param directory the plugin's data directory
	 * @param configFile the plugin's config file
	 */
	public Plugin(SpecificPluginsManager spm, String name, String version, String authors, File directory, File configFile) {
		this.spm = spm;
		this.name = name;
		this.version = version;
		this.authors = authors;
		this.directory = directory;
		this.configFile = configFile;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof Plugin)) {
			return false;
		}
		Plugin p = (Plugin) obj;
		return name.equals(p.getName());
	}
	
	/**
	 * Gets the file contaning the plugin's configuration. This file may not yet exist.
	 */
	public File getConfigFile() {
		return configFile;
	}
	
	/**
	 * Gets the directory containing the plugin data's files. The directory may not yet exist.
	 */
	public File getDirectory() {
		return directory;
	}
	
	/**
	 * Gets the plugin's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the plugin's version String.
	 */
	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Checks if this plugin is currently enabled.
	 *
	 * @return true if it is enabled.
	 */
	public abstract boolean isEnabled();
	
	/**
	 * Called when this plugin is disabled.
	 */
	public abstract void onDisable();
	
	/**
	 * Called when this plugin is enabled. During this phase all registries are CLOSED and <b>you CANNOT add new blocks,
	 * entities, items, packets, etc.</b>
	 * <p>
	 * Several plugins may be enabled in different Threads at the same time. However it is guaranteed that if a plugin
	 * has dependencies, then they are enabled before the dependent plugin.
	 */
	public abstract void onEnable();
	
	/**
	 * Called when this plugin is loaded. During this phase all registries are open and <b>you can add new blocks,
	 * entities, items, packets, etc.</b> All plugins are loaded one by one in the same Thread.
	 */
	public void onLoad() {
		// By default this method does nothing.
	}
	
}
