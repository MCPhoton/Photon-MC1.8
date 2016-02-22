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
package org.mcphoton.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contains all the possible options for a command.
 *
 * @author ElectronWill
 */
public final class PossibleOptions {

	/**
	 * All the possible options of this command.
	 */
	private final Map<String, Option> optionsMap = new HashMap<>();

	/**
	 * Length of the largest option's name.
	 */
	private int largestName = 0;

	/**
	 * Returns the length of the largest option's name.
	 *
	 * @return the length of the largest option's name.
	 */
	public int getLargestName() {
		return largestName;
	}

	public Set<Map.Entry<String, Option>> getOptionsEntries() {
		return optionsMap.entrySet();
	}

	/**
	 * Returns the option with the given name, if it exists. Option's name does NOT contain the '-'
	 * at the beginning.
	 *
	 * @param optionName option's name
	 * @return the option, or null if it does not exist.
	 */
	public Option get(String optionName) {
		return optionsMap.get(optionName);
	}

	/**
	 * Checks if the option with the given name exists. Option's name does NOT contain the '-' at
	 * the beginning.
	 *
	 * @param optionName option's name
	 * @return true if this option exists.
	 */
	public boolean has(String optionName) {
		return optionsMap.containsKey(optionName);
	}

	/**
	 * Checks if the option with the given name is required.
	 *
	 * @param optionName option's name
	 * @return true if this option exists and is required, false otherwise.
	 */
	public boolean isRequired(String optionName) {
		Option o = optionsMap.get(optionName);
		return o == null ? false : o.isRequired();
	}

	/**
	 * Registers a new Option with the given name and description. The option is not required and
	 * does not need any parameter.
	 *
	 * @param optionName option's name
	 * @param description option's description
	 */
	public void register(String optionName, String description) {
		Option o = new Option(description);
		register(optionName, o);
	}

	/**
	 * Register a new required Option with the given name and description. The option does not need
	 * any parameter.
	 *
	 * @param optionName option's name
	 * @param description option's description
	 */
	public void registerRequired(String optionName, String description) {
		Option o = new Option(description, true);
		register(optionName, o);
	}

	/**
	 * Register a new Option with the given name.
	 *
	 * @param optionName option's name
	 * @param o option's instance
	 */
	public void register(String optionName, Option o) {
		optionsMap.put(optionName, o);
		if (optionName.length() > largestName) {
			largestName = optionName.length();
		}
	}

	/**
	 * Register a new Option with the given name.
	 *
	 * @param optionName option's name
	 * @param o option's instance
	 */
	public void register(String optionName, OptionGroup o) {
		optionsMap.put(optionName, o);
		if (o.getLargestName() > largestName) {
			largestName = optionName.length();
		}
	}

}
