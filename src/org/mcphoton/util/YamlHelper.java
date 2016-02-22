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
package org.mcphoton.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * A utility class to use SnakeYAML easily. It can use path with multiple parts, in particular.
 * <h3>How to use paths:</h3>
 * <li>A path refer to a key in the Yaml map. The nodes are separated by a char called the
 * "separator".
 * <li>The separator is a dot '.' by default. Path example: "a.b.keyC"
 * <li>"a.b.keyC" refers to a value "keyC" that is in the map "b" that is in an other map "a", that
 * is in the global map of the Yaml data:<br>
 * a:<br>
 * __b:<br>
 * ____keyC: keyC's value<br>
 * </li>
 *
 * @see https://code.google.com/p/snakeyaml/wiki/Documentation
 * @author ElectronWill
 */
public final class YamlHelper {

	/**
	 * Returns true if the data contains the given path.
	 *
	 * @param data root Map containing all values
	 * @param path value's path
	 * @return true if the Map contains the value, false otherwise.
	 */
	public static boolean contains(Map<String, Object> data, String path) {
		return contains(data, path, '.');
	}

	/**
	 * Returns true if the data contains the given path.
	 *
	 * @param data root Map containing all values
	 * @param path value's path
	 * @param separator the separator used to parse the path
	 * @return true if the Map contains the value, false otherwise.
	 */
	public static boolean contains(Map<String, Object> data, String path, char separator) {
		String[] parts = split(path, separator);
		if (parts.length == 0) {
			Object o = data.get(path);
			if (o == null) {
				return data.containsKey(path);
			}
			return true;
		}
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			Object o = data.get(part);
			if (i == parts.length - 1) {
				if (o == null) {
					return data.containsKey(part);
				}
				return true;
			}
			if (o == null) {
				return false;
			}
			if (o instanceof Map) {
				data = (Map<String, Object>) o;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Returns the requested Object by path, or def if not found.
	 *
	 * @param data root Map
	 * @param path value's path
	 * @param separator path's separator
	 * @param def value returned if not found
	 * @return the value of the given path, or def
	 */
	public static Object get(Map<String, Object> data, String path, char separator, Object def) {
		String[] parts = split(path, separator);
		if (parts.length == 0) {
			Object o = data.get(path);
			if (o == null) {
				return data.containsKey(path) ? null : def;
			}
			return o;
		}
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			Object o = data.get(part);
			if (i == parts.length - 1) {
				if (o == null) {
					return data.containsKey(part) ? null : def;
				}
				return o;
			}
			if (o == null) {
				return def;
			}
			if (o instanceof Map) {
				data = (Map<String, Object>) o;
			} else {
				return def;
			}
		}
		return def;
	}

	/**
	 * Returns the requested Object by path, or null if not found.
	 *
	 * @param data root Map
	 * @param path value's path
	 * @return the value of the given path, or null
	 */
	public static Object get(Map<String, Object> data, String path) {
		return get(data, path, '.', null);
	}

	/**
	 * Returns the requested Date by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static Date getDate(Map<String, Object> data, String path, char separator, Object def) {
		return (Date) get(data, path, separator, def);
	}

	/**
	 * Returns the requested Date by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static Date getDate(Map<String, Object> data, String path) {
		return (Date) get(data, path, '.', null);
	}

	/**
	 * Returns the requested float by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static float getFloat(Map<String, Object> data, String path, char separator, float def) {
		return (Float) get(data, path, separator, def);
	}

	/**
	 * Returns the requested float by path, or -1 if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static float getFloat(Map<String, Object> data, String path) {
		return (Float) get(data, path, '.', -1);
	}

	/**
	 * Returns the requested int by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static int getInteger(Map<String, Object> data, String path, char separator, int def) {
		return (Integer) get(data, path, separator, def);
	}

	/**
	 * Returns the requested int by path, or -1 if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static int getInteger(Map<String, Object> data, String path) {
		return (Integer) get(data, path, '.', -1);
	}

	/**
	 * Returns the requested {@code List<Integer>} by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static List<Integer> getIntegerList(Map<String, Object> data, String path, char separator, List<Integer> def) {
		return (List<Integer>) get(data, path, separator, def);
	}

	/**
	 * Returns the requested {@code List<Integer>} by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static List<Integer> getIntegerList(Map<String, Object> data, String path) {
		return (List<Integer>) get(data, path, '.', null);
	}

	/**
	 * Returns the requested {@code List} by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static List getList(Map<String, Object> data, String path, char separator, List def) {
		return (List) get(data, path, separator, def);
	}

	/**
	 * Returns the requested {@code List} by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static List getList(Map<String, Object> data, String path) {
		return (List) get(data, path, '.', null);
	}

	/**
	 * Returns the requested {@code Map<>} by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static Map<String, Object> getMap(Map<String, Object> data, String path, char separator, Map<String, Object> def) {
		return (Map<String, Object>) get(data, path, separator, def);
	}

	/**
	 * Returns the requested {@code Map} by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static Map<String, Object> getMap(Map<String, Object> data, String path) {
		return (Map<String, Object>) get(data, path, '.', null);
	}

	/**
	 * Returns the requested String by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static String getString(Map<String, Object> data, String path, char separator, String def) {
		return (String) get(data, path, separator, def);
	}

	/**
	 * Returns the requested String by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static String getString(Map<String, Object> data, String path) {
		return (String) get(data, path, '.', null);
	}

	/**
	 * Returns the requested {@code List<String>} by path, or def if not found.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param def
	 * @return
	 */
	public static List<String> getStringList(Map<String, Object> data, String path, char separator, List<String> def) {
		return (List<String>) get(data, path, separator, def);
	}

	/**
	 * Returns the requested {@code List<String>} by path, or null if not found.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static List<String> getStringList(Map<String, Object> data, String path) {
		return (List<String>) get(data, path, '.', null);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing Date value.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static boolean isDate(Map<String, Object> data, String path) {
		return get(data, path) instanceof Date;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing Date value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isDate(Map<String, Object> data, String path, char separator) {
		return get(data, path, separator, null) instanceof Date;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing float value.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static boolean isFloat(Map<String, Object> data, String path) {
		return get(data, path) instanceof Float;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing float value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isFloat(Map<String, Object> data, String path, char separator) {
		return get(data, path, separator, null) instanceof Float;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing int value.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static boolean isInteger(Map<String, Object> data, String path) {
		return get(data, path) instanceof Integer;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing int value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isInteger(Map<String, Object> data, String path, char separator) {
		return get(data, path, separator, null) instanceof Integer;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing List value.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static boolean isList(Map<String, Object> data, String path) {
		return get(data, path) instanceof List;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing List value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isList(Map<String, Object> data, String path, char separator) {
		return get(data, path, separator, null) instanceof List;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing String value.
	 *
	 * @param data
	 * @param path
	 * @return
	 */
	public static boolean isString(Map<String, Object> data, String path) {
		return get(data, path) instanceof String;
	}

	/**
	 * Returns {@code true} if the given path refers to an existing String value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @return
	 */
	public static boolean isString(Map<String, Object> data, String path, char separator) {
		return get(data, path, separator, null) instanceof String;
	}

	/**
	 * Sets the specified path to the given value.
	 *
	 * @param data
	 * @param path
	 * @param separator
	 * @param value
	 * @return the old value, if any.
	 */
	public static Object set(Map<String, Object> data, String path, char separator, Object value) {
		String[] parts = split(path, separator);
		if (parts.length == 0) {
			return data.put(path, value);
		}
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			Object o = data.get(part);
			if (i == parts.length - 1) {
				return data.put(part, value);
			}
			if (o == null) {
				throw new YAMLException("The part " + part + " of the path " + path + " does not exist");
			}
			if (o instanceof Map) {
				data = (Map<String, Object>) o;
			} else {
				throw new YAMLException("The part " + part + " of the path " + path + " is not a Map<String, Object>");
			}
		}
		throw new YAMLException("Unable to set the value at " + path);
	}

	/**
	 * Sets the specified path to the given value.
	 *
	 * @param data
	 * @param path
	 * @param value
	 * @return the old value, if any.
	 */
	public static Object set(Map<String, Object> data, String path, Object value) {
		return set(data, path, '.', value);
	}

	/**
	 * Split a String in several parts, each part delimited by the given separator.
	 *
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String[] split(String str, char separator) {
		ArrayList<String> list = new ArrayList<>(4);
		int i;
		while ((i = str.indexOf(separator)) != -1) {
			str = str.substring(i);
			list.add(str);
		}
		return list.toArray(new String[list.size()]);
	}

	private final Map<String, Object> data;
	private char separator = '.';

	/**
	 * Creates a new YamlHelper with the given yaml data.
	 *
	 * @param ymlData
	 */
	public YamlHelper(String ymlData) {
		Yaml yaml = new Yaml();
		data = (Map) yaml.load(ymlData);
	}

	/**
	 * Creates a new YamlHelper that will read the file to get yaml data.
	 *
	 * @param file
	 * @throws java.io.FileNotFoundException
	 */
	public YamlHelper(File file) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		data = (Map<String, Object>) yaml.load(new FileReader(file));
	}

	/**
	 * Creates a new YamlHelper with the given yaml data.
	 *
	 * @param yaml
	 * @param ymlData
	 */
	public YamlHelper(Yaml yaml, String ymlData) {
		data = (Map) yaml.load(ymlData);
	}

	/**
	 * Creates a new YamlHelper that will read the file to get yaml data.
	 *
	 * @param yaml
	 * @param file
	 * @throws java.io.FileNotFoundException
	 */
	public YamlHelper(Yaml yaml, File file) throws FileNotFoundException {
		data = (Map<String, Object>) yaml.load(new FileReader(file));
	}

	/**
	 * Creates a new YamlHelper with the given data Map.
	 *
	 * @param data
	 */
	public YamlHelper(Map<String, Object> data) {
		this.data = data;
	}

	/**
	 * Returns true if the data contains the given path.
	 *
	 * @param path
	 * @return
	 */
	public boolean contains(String path) {
		return contains(data, path, separator);
	}

	/**
	 * Returns the requested Object by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public Object get(String path, Object def) {
		return get(data, path, separator, def);
	}

	/**
	 * Returns the requested Object by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public Object get(String path) {
		return get(data, path, separator, null);
	}

	/**
	 * Returns the requested Date by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public Date getDate(String path, Date def) {
		return getDate(data, path, separator, def);
	}

	/**
	 * Returns the requested Date by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public Date getDate(String path) {
		return getDate(data, path, separator, null);
	}

	/**
	 * Returns the requested String by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public String getString(String path, String def) {
		return getString(data, path, separator, def);
	}

	/**
	 * Returns the requested String by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public String getString(String path) {
		return getString(data, path, separator, null);
	}

	/**
	 * Returns the requested float by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public float getFloat(String path, float def) {
		return getFloat(data, path, separator, def);
	}

	/**
	 * Returns the requested float by path, or -1 if not found.
	 *
	 * @param path
	 * @return
	 */
	public float getFloat(String path) {
		return getFloat(data, path, separator, -1);
	}

	/**
	 * Returns the requested int by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public int getInteger(String path, int def) {
		return getInteger(data, path, separator, def);
	}

	/**
	 * Returns the requested int by path, or -1 if not found.
	 *
	 * @param path
	 * @return
	 */
	public int getInteger(String path) {
		return getInteger(data, path, separator, -1);
	}

	/**
	 * Returns the requested List by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public List getList(String path, List def) {
		return getList(data, path, separator, def);
	}

	/**
	 * Returns the requested List by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public List getList(String path) {
		return getList(data, path, separator, null);
	}

	/**
	 * Returns the requested Map by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public Map<String, Object> getMap(String path, Map<String, Object> def) {
		return getMap(data, path, separator, def);
	}

	/**
	 * Returns the requested Map by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public Map<String, Object> getMap(String path) {
		return getMap(data, path, separator, null);
	}

	/**
	 * Returns the current path separator.
	 *
	 * @return
	 */
	public char getSeparator() {
		return separator;
	}

	/**
	 * Sets the path separator.
	 *
	 * @param separator
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	/**
	 * Returns the requested {@code List<String>} by path, or def if not found.
	 *
	 * @param path
	 * @param def
	 * @return
	 */
	public List getStringList(String path, List<String> def) {
		return getStringList(data, path, separator, def);
	}

	/**
	 * Returns the requested {@code List<String>} by path, or null if not found.
	 *
	 * @param path
	 * @return
	 */
	public List getStringList(String path) {
		return getStringList(data, path, separator, null);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing Date value.
	 *
	 * @param path
	 * @return
	 */
	public boolean isDate(String path) {
		return isDate(data, path, separator);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing float value.
	 *
	 * @param path
	 * @return
	 */
	public boolean isFloat(String path) {
		return isFloat(data, path, separator);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing int value.
	 *
	 * @param path
	 * @return
	 */
	public boolean isInteger(String path) {
		return isInteger(data, path, separator);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing List value.
	 *
	 * @param path
	 * @return
	 */
	public boolean isList(String path) {
		return isList(data, path, separator);
	}

	/**
	 * Returns {@code true} if the given path refers to an existing String value.
	 *
	 * @param path
	 * @return
	 */
	public boolean isString(String path) {
		return isString(data, path, separator);
	}

	/**
	 * Sets the specified path to the given value.
	 *
	 * @param path
	 * @param value
	 * @return
	 */
	public Object set(String path, Object value) {
		return set(data, path, separator, value);
	}

}
