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

/**
 * Represents an executable command, that can be used by a Player in the chat or in the server's
 * console.
 *
 * @author ElectronWill
 */
public abstract class CommandExecutor {

	private static final Map<String, CommandExecutor> COMMAND_NAMES = new HashMap<>();

	public static synchronized CommandExecutor get(String commandName) {
		return COMMAND_NAMES.get(commandName);
	}

	/**
	 * Tries to register a CommandExecutor.
	 *
	 * @return true if successfully registered, false if a command with this name has already been
	 * registered.
	 */
	public static synchronized boolean register(CommandExecutor cmd) {
		return COMMAND_NAMES.putIfAbsent(cmd.getCommand(), cmd) == null;
	}

	/**
	 * Tries to register a CommandExecutor with zero, one or more aliase(s).
	 *
	 * @return the number of command + aliases successfully registered.
	 */
	public static synchronized int register(CommandExecutor cmd, String... aliases) {
		int count = 0;
		if (aliases != null && aliases.length != 0) {
			for (int i = 0; i < aliases.length; i++) {
				String alias = aliases[i];
				if (COMMAND_NAMES.putIfAbsent(alias, cmd) == null) {
					count++;
				}
			}
		}
		if (COMMAND_NAMES.putIfAbsent(cmd.getCommand(), cmd) == null) {
			count++;
		}
		return count;
	}

	/**
	 * Unregisters a CommandExecutor.
	 *
	 * @return true if it was unregistered, false if it was not registered.
	 */
	public static synchronized boolean unregister(CommandExecutor cmd) {
		return COMMAND_NAMES.remove(cmd.getCommand(), cmd);
	}

	/**
	 * Command's name.
	 */
	protected final String command;

	/**
	 * Command's description.
	 */
	protected final String description;

	/**
	 * Creates a new CommandExecutor for the command with the given cmd. <b>Implementations must use
	 * super(name, description) in their constructor to set the command's name and description.</b>
	 *
	 * @param cmdName the command's cmd.
	 * @param description the command's description
	 */
	protected CommandExecutor(final String cmdName, final String description) {
		this.command = cmdName;
		this.description = description;
	}

	/**
	 * Called when this command is executed with the given arguments.
	 *
	 * @param sender who executed the command.
	 * @param args command's arguments
	 */
	public abstract void onCommand(CommandSender sender, String... args);

	/**
	 * Returns the command's cmd.
	 *
	 * @return
	 */
	public final String getCommand() {
		return command;
	}

	/**
	 * Returns the command's description.
	 *
	 * @return
	 */
	public final String getDescription() {
		return description;
	}

}
