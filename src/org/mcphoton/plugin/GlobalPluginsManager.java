/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Pluginublic License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PluginARTICULAR PluginURPluginOSE. See the GNU Affero General
 * Pluginublic License for more details.
 *
 * You should have received a copy of the GNU Affero General Pluginublic License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mcphoton.security.AccessPermit;
import com.electronwill.collections.Bag;
import com.electronwill.collections.OpenList;
import com.electronwill.collections.SimpleBag;

/**
 * Manages all the plugins and all the SpecificPluginsManagers.
 *
 * @author ElectronWill
 */
@SuppressWarnings("unchecked")
public final class GlobalPluginsManager {
	
	private static final List<Exception> EXCEPTIONS = new OpenList<>();
	private static final Bag<Plugin> LOADED_PLUGINS = new SimpleBag<>();
	private static final List<SpecificPluginsManager<?>> SPECIFIC_PM = new OpenList<>();
	
	static AccessPermit getAccessPermit(SpecificPluginsManager<?> spm, String name, String version, String authors, File directory,
			File configFile) {
		// TODO en fonction de la config de sécurité.
		return null;
	}
	
	/**
	 * Disables a plugin. If the plugin is already disabled this method has no effect.
	 */
	public static synchronized void disable(Plugin plugin) {
		if (!plugin.isEnabled()) {
			return;
		}
		plugin.spm.disable(plugin);
	}
	
	/**
	 * Enables a plugin. If the plugin is already enabled this method has no effect.
	 */
	public static synchronized void enable(Plugin plugin) {
		if (plugin.isEnabled()) {
			return;
		}
		plugin.spm.enable(plugin);
	}
	
	/**
	 * Gets the plugins that has been loaded by the given SpecificPluginsManager.
	 * 
	 * @param spm the SpecificPluginManager
	 * @return the plugins that this specific manager loaded.
	 */
	public static synchronized <P extends Plugin> Iterable<P> getLoadedPlugins(SpecificPluginsManager<? extends P> spm) {
		return () -> new Iterator<P>() {
			private P next = findNext();
			private int i;
			
			private P findNext() {
				for (; i < LOADED_PLUGINS.size(); i++) {
					Plugin p = LOADED_PLUGINS.get(i);
					if (p.spm == spm) {
						return (P) p;
					}
				}
				return null;
			}
			
			@Override
			public boolean hasNext() {
				return next != null;
			}
			
			@Override
			public P next() {
				P p = next;
				next = findNext();
				return p;
			}
		};
	}
	
	/**
	 * Gets the enabled plugins that has been loaded by the given SpecificPluginsManager.
	 * 
	 * @param spm the SpecificPluginManager
	 * @return the plugins that this specific manager enabled.
	 */
	public static synchronized <P extends Plugin> Iterable<P> getEnabledPlugins(SpecificPluginsManager<? extends P> spm) {
		return () -> new Iterator<P>() {
			private P next = findNext();
			private int i;
			
			private P findNext() {
				for (; i < LOADED_PLUGINS.size(); i++) {
					Plugin p = LOADED_PLUGINS.get(i);
					if (p.isEnabled() && p.spm == spm) {
						return (P) p;
					}
				}
				return null;
			}
			
			@Override
			public boolean hasNext() {
				return next != null;
			}
			
			@Override
			public P next() {
				P p = next;
				next = findNext();
				return p;
			}
		};
	}
	
	/**
	 * Gets the disabled plugins that has been loaded by the given SpecificPluginsManager.
	 * 
	 * @param spm the SpecificPluginManager
	 * @return the plugins that this specific manager disabled.
	 */
	public static synchronized <P extends Plugin> Iterable<P> getDisabledPlugins(SpecificPluginsManager<? extends P> spm) {
		return () -> new Iterator<P>() {
			private P next = findNext();
			private int i;
			
			private P findNext() {
				for (; i < LOADED_PLUGINS.size(); i++) {
					Plugin p = LOADED_PLUGINS.get(i);
					if (p.spm == spm) {
						return (P) p;
					}
				}
				return null;
			}
			
			@Override
			public boolean hasNext() {
				return next != null;
			}
			
			@Override
			public P next() {
				P p = next;
				next = findNext();
				return p;
			}
		};
	}
	
	/**
	 * Gets all the disabled plugins.
	 */
	public static synchronized Iterator<Plugin> getDisabledPlugins() {
		return LOADED_PLUGINS.stream().filter((p) -> !p.isEnabled()).iterator();
	}
	
	/**
	 * Gets all the enabled plugins.
	 */
	public static synchronized Iterator<Plugin> getEnabledPlugins() {
		return LOADED_PLUGINS.stream().filter((p) -> p.isEnabled()).iterator();
	}
	
	/**
	 * Gets all the exceptions that occured within the GlobalPluginsManager since the last clear.
	 *
	 * @param clear <code>true</code> to clear the exceptions list.
	 * @return the exceptions list.
	 */
	public static synchronized List<Exception> getExceptions(boolean clear) {
		if (!clear) {
			return Collections.unmodifiableList(EXCEPTIONS);
		}
		List<Exception> exceptions = new ArrayList<>();
		exceptions.addAll(EXCEPTIONS);
		EXCEPTIONS.clear();
		return exceptions;
	}
	
	/**
	 * Gets all the loaded plugins (enabled and disabled ones).
	 */
	public static synchronized Iterable<Plugin> getLoadedPlugins() {
		return LOADED_PLUGINS;
	}
	
	/**
	 * Gets all the currently registered SpecificPluginsManager.
	 */
	public static synchronized List<SpecificPluginsManager<?>> getManagers() {
		return Collections.unmodifiableList(SPECIFIC_PM);
	}
	
	/**
	 * Gets the loaded plugin with that name. It is the same thing as getPlugin(name, false).
	 * 
	 * @see {@link #getPlugin(String, boolean)}
	 */
	public static synchronized Plugin getPlugin(String name) {
		for (Plugin p : LOADED_PLUGINS) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Gets the loaded plugin with that name.
	 */
	public static synchronized Plugin getPlugin(String name, boolean ignoreCase) {
		if (!ignoreCase) {
			return getPlugin(name);
		}
		for (Plugin p : LOADED_PLUGINS) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Checks if a plugin is enabled.
	 */
	public static synchronized boolean isEnabled(Plugin plugin) {
		return plugin.isEnabled();
	}
	
	/**
	 * Checks if a plugin is loaded and enabled.
	 */
	public static synchronized boolean isEnabled(String pluginName) {
		for (Plugin p : LOADED_PLUGINS) {
			if (p.getName().equals(pluginName)) {
				return p.isEnabled();
			}
		}
		return false;
	}
	
	/**
	 * Checks if a plugin is loaded.
	 */
	public static synchronized boolean isLoaded(Plugin plugin) {
		for (Plugin p : LOADED_PLUGINS) {
			if (p == plugin) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a plugin is loaded.
	 */
	public static synchronized boolean isLoaded(String pluginName) {
		for (Plugin p : LOADED_PLUGINS) {
			if (p.getName().equals(pluginName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Loads all the plugins contained in a directory.
	 *
	 * @param pluginsDir the directory
	 * @return a list of plugins
	 */
	public static synchronized List<Plugin> loadAllFrom(File pluginsDir) {
		if (!pluginsDir.isDirectory()) {
			throw new IllegalArgumentException("This parameter is not a directory or does not exist: " + pluginsDir.getAbsolutePath());
		}
		final Map<SpecificPluginsManager<?>, List<File>> fileMap = new HashMap<>();// defines which SPM loads which File
		final List<Plugin> loaded = new OpenList<>();// list of the newly loaded plugins
		for (File file : pluginsDir.listFiles()) {
			if (file.isDirectory()) {// skip directories
				continue;
			}
			for (SpecificPluginsManager<?> spm : SPECIFIC_PM) {// finds the right SPM to load this plugin's file
				if (spm.canLoadFrom(file)) {
					List<File> fileList = fileMap.get(spm);
					if (fileList == null) {
						fileList = new OpenList<>();
						fileMap.put(spm, fileList);
					}
					fileList.add(file);
					break;
				}
			}
		}
		for (Map.Entry<SpecificPluginsManager<?>, List<File>> entry : fileMap.entrySet()) {// loads all the plugins
			SpecificPluginsManager<?> spm = entry.getKey();// the SPM that is able to load the plugin's file
			List<File> files = entry.getValue();// the plugin's file
			try {
				List<? extends Plugin> spmLoaded = spm.loadAllFrom(files);// asks the SPM to load the plugins the
																			// correct way
				loaded.addAll(spmLoaded);// adds theses loaded plugins to the "loaded" list
			} catch (Exception ex) {
				EXCEPTIONS.add(ex);
			}
		}
		return loaded;
	}
	
	/**
	 * Loads a plugin from a file.
	 *
	 * @param pluginFile the file containing the plugin
	 * @return a plugin
	 * @throws org.mcphoton.plugin.UnloadablePluginException
	 */
	public static synchronized Plugin loadFrom(File pluginFile) throws UnloadablePluginException {
		for (SpecificPluginsManager<?> spm : SPECIFIC_PM) {
			if (spm.canLoadFrom(pluginFile)) {
				return spm.loadFrom(pluginFile);
			}
		}
		throw new UnloadablePluginException("No SpecificPluginsManager is able to load a plugin from this file. Is it valid?");
	}
	
	/**
	 * Registers a SpecificPluginsManager.
	 */
	public static synchronized void register(SpecificPluginsManager<?> spm) {
		SPECIFIC_PM.add(spm);
	}
	
	/**
	 * Unloads a plugin, and disables it if it is enabled.
	 */
	public static synchronized void unload(Plugin plugin) {
		plugin.spm.unload(plugin);
	}
	
	/**
	 * Unregisters a SpecificPluginsManager.
	 */
	public static synchronized void unregister(SpecificPluginsManager<?> spm) {
		SPECIFIC_PM.remove(spm);
	}
	
	static void addLoaded(Plugin p) {
		LOADED_PLUGINS.add(p);
	}
	
	static void removeLoaded(Plugin p) {
		LOADED_PLUGINS.remove(p);
	}
	
	private GlobalPluginsManager() {}
	
}
