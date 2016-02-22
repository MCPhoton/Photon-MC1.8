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
package org.mcphoton.command.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mcphoton.command.CommandExecutor;
import org.mcphoton.command.CommandSender;
import org.mcphoton.command.HelpBuilder;

/**
 *
 * @author ElectronWill
 */
public class ManCommand extends CommandExecutor {

	public ManCommand() {
		super("man", "displays some help about a command");
	}

	/**
	 * Contains generated help pages.
	 */
	private static final Map<CommandExecutor, List<String>> HELP_PAGES = new HashMap<>();

	@Override
	public void onCommand(CommandSender sender, String... args) {
		if (args.length != 1) {
			sender.sendMessage("This command needs one argument!");
			return;
		}
		CommandExecutor cmd = CommandExecutor.get(args[0]);
		if (cmd == null) {
			sender.sendMessage("Unknown command: " + args[0]);
			return;
		}
		List<String> help = HELP_PAGES.get(cmd);
		if (help == null) {
			help = HelpBuilder.buildHelp(cmd);
			HELP_PAGES.put(cmd, help);
		}
		for (String s : help) {
			sender.sendMessage(s);
		}
	}

}
