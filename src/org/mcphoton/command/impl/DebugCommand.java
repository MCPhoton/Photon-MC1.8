package org.mcphoton.command.impl;

import org.mcphoton.command.CommandExecutor;
import org.mcphoton.command.CommandSender;
import org.mcphoton.core.Photon;
import org.mcphoton.entity.impl.OnlinePlayer;

/**
 * The debug command, that enable or disable debug in the console and in the log.
 * 
 * @author ElectronWill
 * 		
 */
public class DebugCommand extends CommandExecutor {
	
	public DebugCommand() {
		super("debug", "enables or disables the display of debug messages in the server's console");
	}
	
	@Override
	public void onCommand(CommandSender sender, String... args) {
		if (args.length == 0) {
			sender.sendMessage("This command needs one argument: true = enable debug / false = disable");
			return;
		}
		if (sender instanceof OnlinePlayer) {
			OnlinePlayer p = (OnlinePlayer) sender;
			if (p.hasPermission("debug")) {
				Photon.log.setDebugEnabled(Boolean.parseBoolean(args[0]));
			}
		} else {
			Photon.log.setDebugEnabled(Boolean.parseBoolean(args[0]));
		}
	}
	
}
