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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The result of {@link OptionsParser#parse(<any>, java.lang.String...)}: contains parsed options
 * and non-options parameters.
 *
 * @author ElectronWill
 */
public final class ParsedOptions {

	/**
	 * Recognized command's options.
	 */
	private final Map<String, ParsedOption> options = new HashMap<>();

	/**
	 * Simple command's arguments.
	 */
	private final List<String> args = new ArrayList<>();

	/**
	 * The error, when there is one.
	 */
	private String error;

	ParsedOptions() {
	}

	void add(String name, ParsedOption o) {
		options.put(name, o);
	}

	void add(String arg) {
		args.add(arg);
	}

	public boolean isPresent(String option) {
		return options.containsKey(option);
	}

	public ParsedOption getOption(String name) {
		return options.get(name);
	}

	public int getArgsNumber() {
		return args.size();
	}

	public List<String> getArguments() {
		return args;
	}

	public boolean hasError() {
		return error != null;
	}

	public String getError() {
		return error;
	}

	void error(String description) {
		error = description;
	}

}
