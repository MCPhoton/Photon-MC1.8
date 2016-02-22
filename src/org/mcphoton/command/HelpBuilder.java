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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Utility class for easily building the help page of a command.
 *
 * @author ElectronWill
 */
public final class HelpBuilder {

	/**
	 * Creates the help page of a simple command. It is just a single line with the command's name
	 * and description.
	 *
	 * @param cmd
	 * @return
	 */
	public static String buildSimpleHelp(CommandExecutor cmd) {
		return cmd.getCommand() + ": " + cmd.getDescription();
	}

	/**
	 * Creates the help page of a simple command. It is just a single line with the command's name
	 * and description.
	 *
	 * @param cmd
	 * @return
	 */
	public static List<String> buildHelp(CommandExecutor cmd) {
		if (cmd instanceof AdvancedCommandExecutor) {
			return buildHelp((AdvancedCommandExecutor) cmd);
		} else {
			ArrayList<String> list = new ArrayList<>(1);
			list.add(buildSimpleHelp(cmd));
			return list;
		}
	}

	/**
	 * Creates the help page of an advanced command. It's a first line with the command name,
	 * description and options list. Then there are one line for each possible option with its own
	 * description. All the option's descriptions are vertically aligned. <b>Each String in the list
	 * represents a single line.</b>
	 *
	 * @param cmd
	 * @return
	 */
	public static List<String> buildHelp(AdvancedCommandExecutor cmd) {
		final Set<Entry<String, Option>> possibleOptions = cmd.getPossibleOptions();
		final ArrayList<String> list = new ArrayList<>();

		//== Build command description ==
		final StringBuilder sb = new StringBuilder(cmd.getCommand().length() + cmd.getDescription().length() + possibleOptions.size() * 2);
		final Iterator<Entry<String, Option>> it = possibleOptions.iterator();
		while (it.hasNext()) {
			final Entry<String, Option> entry = it.next();
			final Option o = entry.getValue();
			if (!o.isRequired()) {//Option or GroupOption is optional
				sb.append('[');
			}
			if (o instanceof OptionGroup) {//A GroupOption, containing several options.
				final OptionGroup group = (OptionGroup) o;
				final Set<Entry<String, Option>> groupOptions = group.getOptionsEntries();
				final Iterator<Entry<String, Option>> itg = possibleOptions.iterator();
				while (itg.hasNext()) {//For each Option in the GroupOption
					Entry<String, Option> gEntry = itg.next();
					Option get = gEntry.getValue();
					if (entry.getKey().length() == 1) {
						sb.append('-');
					} else {
						sb.append("--");
					}
					if (itg.hasNext()) {
						sb.append('|');
					}
					sb.append(entry.getKey());
				}
			} else {//Just an Option
				if (entry.getKey().length() == 1) {
					sb.append('-');
				} else {
					sb.append("--");
				}
				sb.append(entry.getKey());
			}
			if (!o.isRequired()) {//Option or GroupOption is optional
				sb.append(']');
			}
			if (it.hasNext()) {
				sb.append(' ');
			}
		}
		list.add(sb.toString());

		//== Build options description ==
		for (Entry<String, Option> entry : possibleOptions) {
			final StringBuilder sb2 = new StringBuilder();
			if (entry.getKey().length() == 1) {
				sb2.append('-');
			} else {
				sb2.append("--");
			}
			sb2.append(entry.getKey());
			sb2.ensureCapacity(cmd.getLargestOptionName() + 2);
			while (sb2.length() < cmd.getLargestOptionName() + 2) {
				sb2.append(' ');
			}
			sb2.append(entry.getValue().getDesctiption());
			list.add(sb2.toString());
		}
		return list;
	}

}
