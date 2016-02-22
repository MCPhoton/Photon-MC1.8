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
package org.mcphoton.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.mcphoton.command.CommandExecutor;
import org.mcphoton.command.CommandSender;
import org.mcphoton.command.OptionsParser;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.messaging.TextChatMessage;

/**
 * Handles the console (or "Terminal", or "Command Line", etc.) that launched the server.
 *
 * @author ElectronWill
 */
public final class ConsoleThread implements ThreadManager, CommandSender {
	
	private class ConsoleProcessor extends Thread {
		
		public ConsoleProcessor() {
			super("ConsoleThread");
		}
		
		@Override
		public void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (run) {// read loop
				try {
					String line = reader.readLine();// blocks until there's a line to read.
					if (line == null)
						return;
						
					// write that in the log file:
					Photon.log.consoleCommand(line);
					
					// parse the command:
					List<String> commandLine = OptionsParser.parseCommandLine(line);
					if (commandLine.isEmpty()) {
						Photon.log.warning("Type a command!");
						continue;
					}
					String cmd = commandLine.get(0);
					String[] args = getCommandArguments(commandLine);
					CommandExecutor executor = CommandExecutor.get(cmd);
					
					// executes the command if it exists:
					if (executor == null) {
						Photon.log.warning("Unknown command!");
					} else {
						executor.onCommand(ConsoleThread.this, args);
					}
				} catch (Throwable t) {
					Photon.log.error(t);
				}
			}
		}
	}
	
	private static String[] getCommandArguments(List<String> lst) {
		final String[] array = new String[lst.size() - 1];
		for (int i = 1; i < lst.size(); i++) {
			String get = lst.get(i);
			array[i - 1] = get;
		}
		return array;
	}
	
	private final Thread t = new ConsoleProcessor();
	
	private boolean run = false;
	
	ConsoleThread() {}
	
	@Override
	public void forciblyStop() {
		t.stop();
		run = false;
	}
	
	@Override
	public boolean shouldBeRunning() {
		return run;
	}
	
	public boolean isRunning() {
		return t.isAlive();
	}
	
	@Override
	public String getName() {
		return "|console";
	}
	
	@Override
	public void sendMessage(CharSequence msg) {
		Photon.log.formatted("(>>)", "Console", msg);
	}
	
	@Override
	public void sendMessage(ChatMessage msg) {
		if (msg instanceof TextChatMessage) {
			TextChatMessage textMsg = (TextChatMessage) msg;
			Photon.log.formatted("(>>)", "Console", ((TextChatMessage) msg).toConsoleString());
		} else {
			Photon.log.formatted((">>"), "Console", msg.toString());
		}
	}
	
	@Override
	public void start() {
		t.start();
		run = true;
	}
	
	@Override
	public void stop() {
		run = false;
	}
	
}
