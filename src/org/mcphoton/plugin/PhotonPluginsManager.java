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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.mcphoton.core.Photon;
import com.electronwill.collections.OpenList;

/**
 * Manages the plugins that extends {@link JavaPlugin}.
 * 
 * @author ElectronWill
 */
public final class PhotonPluginsManager extends SpecificPluginsManager<JavaPlugin> {
	
	public static File pluginConfig(String pluginName) {
		File pluginDir = pluginDir(pluginName);
		return new File(pluginDir, "config.yml");
	}
	
	public static File pluginDir(String pluginName) {
		return new File(Photon.pluginsDir, pluginName);
	}
	
	private final MultiClassLoader mcl = new MultiClassLoader();// ClassLoader that searches through all loaders
	private final Map<String, URLClassLoader> pluginsLoaders = new HashMap<>();// url loader for plugin jars, by name
	private final List<Exception> exceptions = new OpenList<>();// exceptions that occured
	
	@Override
	public boolean canLoadFrom(File f) {
		try (JarFile jar = new JarFile(f)) {
			JarEntry entry = jar.getJarEntry("plugin.properties");
			return entry != null;
		} catch (Throwable t) {
			return false;
		}
	}
	
	@Override
	public synchronized void disable(JavaPlugin plugin) {
		try {
			plugin.onDisable();
			plugin.enabled = false;
		} catch (Exception ex) {
			exceptions.add(ex);
		}
	}
	
	@Override
	public synchronized void enable(JavaPlugin plugin) {
		try {
			plugin.onEnable();
			plugin.enabled = true;
		} catch (Exception ex) {
			exceptions.add(ex);
		}
	}
	
	@Override
	public Iterable<JavaPlugin> getDisabledPlugins() {
		return GlobalPluginsManager.getDisabledPlugins(this);
	}
	
	@Override
	public Iterable<JavaPlugin> getEnabledPlugins() {
		return GlobalPluginsManager.getEnabledPlugins(this);
	}
	
	@Override
	public synchronized List<Exception> getExceptions(boolean clear) {
		List<Exception> l = Collections.unmodifiableList(exceptions);
		if (clear)
			exceptions.clear();
		return l;
	}
	
	@Override
	public Iterable<JavaPlugin> getLoadedPlugins() {
		return GlobalPluginsManager.getLoadedPlugins(this);
	}
	
	@Override
	public JavaPlugin getPlugin(String name) {
		return getPlugin(name, false);
	}
	
	@Override
	public JavaPlugin getPlugin(String name, boolean ignoreCase) {
		Plugin p = GlobalPluginsManager.getPlugin(name, ignoreCase);
		if (p == null || (!(p instanceof JavaPlugin))) {
			return null;
		}
		return (JavaPlugin) p;
	}
	
	@Override
	public boolean isEnabled(JavaPlugin plugin) {
		return plugin.isEnabled();
	}
	
	@Override
	public synchronized boolean isEnabled(String pluginName) {
		JavaPlugin p = getPlugin(pluginName);
		return p != null && p.isEnabled();
	}
	
	@Override
	public synchronized boolean isLoaded(JavaPlugin plugin) {
		for (JavaPlugin p : getLoadedPlugins()) {
			if (p == plugin) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public synchronized boolean isLoaded(String pluginName) {
		for (JavaPlugin p : getLoadedPlugins()) {
			if (p.getName().equals(pluginName)) {
				return true;
			}
		}
		return false;
	}
	
	private synchronized JavaPlugin load(File pluginFile, String mainClass, String name, String version, String authors) throws Exception {
		if (mainClass == null || name == null || version == null || authors == null) {
			throw new UnloadablePluginException("plugin.properties MUST contains main, name, version and author... but it does not!");
		}
		// get the plugin's directory:
		File pluginDir = pluginDir(name);
		File configFile = pluginConfig(name);
		
		// loads the plugin's class:
		URL[] urls = new URL[] { pluginFile.toURI().toURL() };
		URLClassLoader classLoader = new URLClassLoader(urls);
		pluginsLoaders.put(name, classLoader);
		mcl.addLoader(classLoader);// adds the URLClassLoader to the MultiClassLoader to share classes
		Class<?> pluginClass = mcl.loadClass(mainClass);// IMPORTANT : use the MultiClassLoader to load the class, so
														// the plugin's classloader will be mcl <- or NOT ! the plugin's
														// classloader will be the URLClassLoader.
		// TEST
		System.out.println(pluginClass);
		mcl.removeLoader(classLoader);// ClassLoader removed. No need to GC.
		try {
			Class<?> testPluginClass = mcl.loadClass(mainClass);// ClassNotFoundException
			System.out.println(testPluginClass);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		// END TEST
		
		// creates the plugin's instance:
		Constructor<?> pluginConstructor = pluginClass.getConstructor(SpecificPluginsManager.class, String.class, String.class,
				String.class, File.class, File.class);
		JavaPlugin plugin = (JavaPlugin) pluginConstructor.newInstance(this, name, version, authors, pluginDir, configFile);
		addLoaded(plugin);// adds it to the list of loaded plugins
		return plugin;
	}
	
	public synchronized List<JavaPlugin> loadAllFrom(File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("This parameter is not a directory or does not exist: " + dir.getAbsolutePath());
		}
		return loadAllFrom(Arrays.asList(dir.listFiles((File f, String name) -> name.endsWith(".jar"))));
	}
	
	@Override
	public synchronized List<JavaPlugin> loadAllFrom(Collection<File> pluginsFiles) {
		List<JavaPlugin> loaded = new OpenList<>();
		Map<String, File> filesMap = new HashMap<>();
		Map<String, Properties> propsMap = new HashMap<>();
		DependancyResolver<String> resolver = new DependancyResolver<>();
		for (File f : pluginsFiles) {
			Photon.log.debug("PluginsManager is opening " + f.getName());
			try {
				Properties props = loadProperties(f);
				String name = props.getProperty("name");
				filesMap.put(name, f);
				propsMap.put(name, props);
				String depend = props.containsKey("depend") ? props.getProperty("depend") : props.getProperty("depends");
				if (depend == null) {// no dependancy
					resolver.put(name);
				} else if (depend.indexOf(',') == -1) {// one dependancy
					resolver.put(name, depend);
				} else {// multiple dependancies
					String[] dependancies = depend.split(",");
					resolver.put(name, dependancies);
				}
			} catch (Exception ex) {
				Photon.log.error(ex, "Error while opening " + f.getName());
				exceptions.add(ex);
			}
		}
		int expected = resolver.entriesCount();
		List<String> loadOrder = resolver.resolveOrder();
		if (loadOrder.size() < expected) {// some dependancies couldn't be resolved
			for (Map.Entry<String, List<String>> unresolved : resolver.entries()) {// reports each problem
				Exception ex = new UnresolvableDependancyException(unresolved.getKey(), unresolved.getValue());
				exceptions.add(ex);
			}
		}
		for (String name : loadOrder) {
			try {
				File file = filesMap.get(name);
				Properties props = propsMap.get(name);
				String main = props.getProperty("main");
				String version = props.getProperty("version");
				String authors = props.containsKey("author") ? props.getProperty("author") : props.getProperty("authors");
				JavaPlugin plugin = load(file, main, name, version, authors);
				loaded.add(plugin);
			} catch (Exception ex) {
				exceptions.add(ex);
			}
		}
		return loaded;
	}
	
	@Override
	public synchronized JavaPlugin loadFrom(File pluginFile) throws UnloadablePluginException {
		if (!pluginFile.isFile()) {
			throw new IllegalArgumentException("This parameter is not a file or does not exist: " + pluginFile.getAbsolutePath());
		}
		try (JarFile jar = new JarFile(pluginFile)) {// opens the jar file
			// open the plugin.properties file
			JarEntry properties = jar.getJarEntry("plugin.properties");
			InputStream propIn = jar.getInputStream(properties);
			InputStreamReader propReader = new InputStreamReader(propIn, StandardCharsets.UTF_8);
			// load the properties:
			Properties prop = new Properties();
			prop.load(propReader);
			String mainClass = prop.getProperty("main");
			String name = prop.getProperty("name");
			String version = prop.getProperty("version", "1");
			String authors = prop.containsKey("author") ? prop.getProperty("author", "") : prop.getProperty("authors", "");
			String depend = prop.containsKey("depend") ? prop.getProperty("depend") : prop.getProperty("depends");
			if (depend != null) {
				if (depend.indexOf(',') == -1) {// one dependancy
					if (!isLoaded(depend)) {
						throw new UnloadablePluginException("This needed dependancy is unmet: " + depend);
					}
				} else {// multiple dependancies
					String[] depends = depend.split(",");
					StringBuilder unmetMessage = new StringBuilder();
					for (String dep : depends) {
						if (!isLoaded(dep)) {
							unmetMessage.append(dep).append(", ");
						}
					}
					if (unmetMessage.length() != 0) {
						throw new UnloadablePluginException("These dependancies are unmet: " + unmetMessage);
					}
				}
				
			}
			return load(pluginFile, mainClass, name, version, authors);
			
		} catch (Exception ex) {
			throw new UnloadablePluginException(ex);
		}
	}
	
	private Properties loadProperties(File pluginFile) throws IOException {
		try (JarFile jar = new JarFile(pluginFile)) {// opens the jar file
			return loadProperties(jar);
		}
	}
	
	private Properties loadProperties(JarFile pluginJar) throws IOException {
		// open the plugin.properties file
		JarEntry propEntry = pluginJar.getJarEntry("plugin.properties");
		try (InputStream propIn = pluginJar.getInputStream(propEntry)) {
			InputStreamReader propReader = new InputStreamReader(propIn, StandardCharsets.UTF_8);
			// load the properties:
			Properties prop = new Properties();
			prop.load(propReader);
			return prop;
		}
	}
	
	@Override
	public synchronized void unload(JavaPlugin plugin) {
		if (plugin.isEnabled()) {
			disable(plugin);
		}
		URLClassLoader loader = pluginsLoaders.get(plugin.getName());
		mcl.removeLoader(loader);
		pluginsLoaders.remove(plugin.getName());
		removeLoaded(plugin);
	}
	
}
