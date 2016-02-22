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

import java.util.Map;
import java.util.Set;

/**
 * An enhanced CommandExecutor that handles complex command's parameters.
 *
 * @author ElectronWill
 */
public abstract class AdvancedCommandExecutor extends CommandExecutor {

	/**
	 * Container for all the command's options.
	 */
	protected final PossibleOptions possibleOptions = new PossibleOptions();

	/**
	 * Creates a new AdvancedCommandExecutor for the given command. <b>Implementations must register
	 * all command's option with the {@link #possibleOptions} field.</b>
	 *
	 * @param cmdName thecommand's name.
	 * @param description the command's description
	 */
	protected AdvancedCommandExecutor(final String cmdName, final String description) {
		super(cmdName, description);
	}

	@Override
	public final void onCommand(CommandSender sender, String... args) {
		ParsedOptions options = OptionsParser.parse(possibleOptions, args);
		onCommand(sender, options, args);
	}

	public final Set<Map.Entry<String, Option>> getPossibleOptions() {
		return possibleOptions.getOptionsEntries();
	}

	public final int getLargestOptionName() {
		return possibleOptions.getLargestName();
	}

	/**
	 * Called when this command is executed with the given options and arguments.
	 *
	 * @param sender
	 * @param options identified command's options
	 * @param args command's arguments and unrecognized options
	 */
	protected abstract void onCommand(CommandSender sender, ParsedOptions options, String... args);

}
