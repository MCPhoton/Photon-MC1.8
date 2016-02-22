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
import java.util.Collection;
import java.util.List;

/**
 * Manages plugins of a specific type.
 *
 * @author ElectronWill
 * @param
 * 		<P>
 *        Type of plugin this SpecificPluginsManager handles.
 */
public abstract class SpecificPluginsManager<P extends Plugin> {
	
	/**
	 * Adds a plugin as to the GlobalPluginsManager.
	 */
	protected static void addLoaded(Plugin p) {
		GlobalPluginsManager.addLoaded(p);
	}
	
	/**
	 * Removes a plugin from the GlobalPluginsManager.
	 */
	protected static void removeLoaded(Plugin p) {
		GlobalPluginsManager.removeLoaded(p);
	}
	
	/**
	 * Checks if this SpecificPluginsManager may be able to load a plugin from the given file.
	 *
	 * @param f the file
	 * @return true if it may be able to load a plugin, false otherwise.
	 */
	public abstract boolean canLoadFrom(File f);
	
	/**
	 * Disables a plugin.
	 */
	public abstract void disable(P plugin);
	
	/**
	 * Enables a plugin.
	 */
	public abstract void enable(P plugin);
	
	/**
	 * Gets all disabled plugins that belong to this PluginsManager.
	 */
	public abstract Iterable<P> getDisabledPlugins();
	
	/**
	 * Gets all enabled plugins that belong to this PluginsManager.
	 */
	public abstract Iterable<P> getEnabledPlugins();
	
	/**
	 * Gets all the exceptions that occured within this SpecificPluginsManager since the last clear.
	 *
	 * @param clear <code>true</code> to clear the exceptions list.
	 * @return the exceptions list.
	 */
	public abstract List<Exception> getExceptions(boolean clear);
	
	/**
	 * Gets all loaded plugins (enabled and disabled ones) that belong to this PluginsManager.
	 */
	public abstract Iterable<P> getLoadedPlugins();
	
	/**
	 * Gets the loaded plugin with that name. It is the same thing as getPlugin(name, false).
	 * 
	 * @see {@link #getPlugin(String, boolean)}
	 */
	public abstract P getPlugin(String name);
	
	/**
	 * Gets the loaded plugin with that name.
	 */
	public abstract P getPlugin(String name, boolean ignoreCase);
	
	/**
	 * Checks if a plugin is enabled.
	 */
	public abstract boolean isEnabled(P plugin);
	
	/**
	 * Checks if a plugin is enabled.
	 */
	public abstract boolean isEnabled(String pluginName);
	
	/**
	 * Checks if a plugin is loaded.
	 */
	public abstract boolean isLoaded(P plugin);
	
	/**
	 * Checks if a plugin is loaded.
	 */
	public abstract boolean isLoaded(String pluginName);
	
	/**
	 * Loads all the plugins contained in a directory.
	 *
	 * @param pluginsFiles all the plugins files
	 * @return a list of plugins
	 */
	public abstract List<P> loadAllFrom(Collection<File> pluginsFiles);
	
	/**
	 * Loads a plugin from a file.
	 *
	 * @param pluginFile the file containing the plugin
	 * @return a plugin
	 * @throws org.mcphoton.plugin.UnloadablePluginException
	 */
	public abstract P loadFrom(File pluginFile) throws UnloadablePluginException;
	
	/**
	 * Unloads a plugin, and disables it if needed.
	 */
	public abstract void unload(P plugin);
	
}
