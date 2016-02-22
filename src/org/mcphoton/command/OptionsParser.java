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
import java.util.List;

/**
 * The command's options parser. The method
 * {@link #parse(org.photon.commands.PossibleOptions, java.lang.String...)} takes a PossibleOptions
 * object, and an array of String, and returns a ParsedOptions object that contains the parsed
 * options (as ParsedOption objects) and the remaining (i.e. non-parsed) arguments.
 *
 * @author ElectronWill
 */
public final class OptionsParser {

	public static List<String> parseCommandLine(String cmdString) {
		final List<String> l = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean quotes = false;
		for (int i = 0; i < cmdString.length(); i++) {
			char c = cmdString.charAt(i);
			if (c == '"') {
				quotes = !quotes;
			} else if (c == ' ') {
				if (quotes) {
					sb.append(c);
				} else {
					l.add(sb.toString());
					sb = new StringBuilder();
				}
			} else {
				sb.append(c);
				if (i == cmdString.length() - 1) {
					l.add(sb.toString());
					sb = new StringBuilder();
				}

			}
		}
		return l;
	}

	public static ParsedOptions parse(PossibleOptions possibleOptions, String... args) {
		final ParsedOptions po = new ParsedOptions();
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];
			if (arg.length() >= 2 && arg.charAt(0) == '-') {
				if (arg.charAt(1) == '-') {//Long option
					final String oName = arg.substring(2);
					Option o = possibleOptions.get(oName);
					if (o != null) {//We found a valid option
						ParsedOption parsedOption = parseOption(po, o, i, args);
						if (parsedOption == null) {//This option needs some parameter(s)!
							StringBuilder sb = new StringBuilder();
							sb.append("The option ").append(o).append(" needs at least one parameter!");
							po.error(sb.toString());
							return po;
						}
						po.add(oName, parsedOption);
					}
				} else {//Short option(s)
					for (int j = 0; j < arg.length(); j++) {
						final char c = arg.charAt(j);
						final String oName = String.valueOf(j);
						Option o = possibleOptions.get(oName);
						if (o != null) {
							if ((o.needsParameter() || o.takesMultipleParameters()) && j != arg.length() - 1) {//This option cannot be grouped!
								StringBuilder sb = new StringBuilder();
								sb.append("The option ").append(o).append(" needs at least one parameter!");
								po.error(sb.toString());
								return po;
							}
							ParsedOption parsedOption = parseOption(po, o, i, args);
							if (parsedOption == null) {//This option needs some parameter(s)!
								StringBuilder sb = new StringBuilder();
								sb.append("The option ").append(o).append(" needs at least one parameter!");
								po.error(sb.toString());
								return po;
							}
							po.add(oName, parsedOption);
						}
					}
				}
			} else {
				po.add(arg);
			}
			po.add(arg);

		}
		return po;
	}

	public static ParsedOption parseOption(ParsedOptions po, Option o, int i, String... args) {
		ParsedOption parsedOption;
		if (o.takesMultipleParameters()) {//This option takes multiple parameters
			if (i == args.length - 1) {
				return null;
			}
			final List<String> params = new ArrayList<>(5);
			do {
				final String nextArg = args[i++];
				if (nextArg.charAt(0) == '-') {
					break;
				}
				params.add(nextArg);
			} while (i < args.length);
			parsedOption = new ParsedOption(o, params);

		} else if (o.needsParameter()) {//This option takes a single parameter
			if (i == args.length - 1) {
				return null;
			}
			final String nextArg = args[i++];
			parsedOption = new ParsedOption(o, nextArg);

		} else {//This option does not takes any parameter
			parsedOption = new ParsedOption(o);
		}
		return parsedOption;
	}

}
