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
package org.mcphoton.command.impl;

import org.mcphoton.command.CommandExecutor;
import org.mcphoton.command.CommandSender;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.impl.OnlinePlayer;

/**
 *
 * @author ElectronWill
 */
public class StopCommand extends CommandExecutor {
	
	private static volatile boolean called = false;// true if the shutdown command has been called by an authorized user
	
	public StopCommand() {
		super("stop", "stops the server");
	}
	
	@Override
	public void onCommand(CommandSender sender, String... args) {
		if (sender instanceof OnlinePlayer) {
			OnlinePlayer p = (OnlinePlayer) sender;
			if (p.hasPermission("shutdown")) {
				called = true;
				try {
					Photon.stop();
				} catch (Throwable ex) {
					sender.sendMessage("ERROR - An error occured while trying to shutdown the server!");
					Photon.log.error(ex);
				}
			} else {
				// p.sendMessage("...");
			}
		} else {
			called = true;
			try {
				Photon.stop();
			} catch (Throwable ex) {
				sender.sendMessage("ERROR - An error occured while trying to shutdown the server!");
				Photon.log.error(ex);
			}
			System.exit(0);
		}
	}
	
	public static boolean hasBeenCalled() {
		return called;
	}
	
}
