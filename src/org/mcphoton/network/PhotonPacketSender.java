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
package org.mcphoton.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.mcphoton.core.Photon;

/**
 * Sends the packets to the clients.
 *
 * @author ElectronWill
 */
public final class PhotonPacketSender {
	
	private static class SenderThread extends Thread {
		
		public SenderThread() {
			super("SenderThread");
		}
		
		@Override
		public void run() {
			while (run) {
				try {
					synchronized (guard) {}
					int selected = selector.select();
					Photon.log.debug("SenderThread: selected " + selected);
					if (selected == 0)
						continue;
					final Set<SelectionKey> selectedKeys = selector.selectedKeys();
					final Iterator<SelectionKey> iterator = selectedKeys.iterator();
					
					try {
						while (iterator.hasNext()) {
							final SelectionKey key = iterator.next();
							if (key.isWritable()) {
								// A channel is ready to take some data
								final SocketChannel channel = (SocketChannel) key.channel();
								if (!channel.isOpen()) {// the connection is closed
									key.cancel();
									ClientInfos.remove(channel);
								}
								final ClientInfos client = ClientInfos.get(channel);
								final boolean completed = client.getPacketWriter().flush();
								if (completed) {
									key.cancel();
								}
							}
						}
					} catch (Exception ex) {
						Photon.log.errorFrom(ex, "SenderThread");
					} finally {
						selectedKeys.clear();
					}
				} catch (Exception e) {
					Photon.log.error(e, "Unable to take the packet");
				}
			}
			Photon.log.debug("SenderThread stopped");
		}
		
	}
	
	private static volatile boolean run;
	private static final Selector selector;
	private static final SenderThread senderThread = new SenderThread();
	private static final Object guard = new Object();
	
	static {
		Selector s = null;
		try {
			s = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		selector = s;
	}
	
	@Deprecated
	public static void forciblyStop() {
		senderThread.stop();
		run = false;
	}
	
	public static boolean isRunning() {
		return senderThread.isAlive();
	}
	
	static void sendData(SendablePacket packet, ClientInfos client) throws IOException {
		sendData(packet, client, null);
	}
	
	static void sendData(SendablePacket packet, ClientInfos client, Runnable onSendingComplete) throws IOException {
		try {
			PacketWriter pw = client.getPacketWriter();
			boolean fullWrite = pw.writeNow(packet, onSendingComplete);
			if (!fullWrite) {
				synchronized (guard) {
					selector.wakeup();
					client.getSocketChannel().register(selector, SelectionKey.OP_WRITE);
				}
			}
		} catch (Throwable error) {
			throw new IOException("Unable to send the packet", error);
		}
	}
	
	public static void start() {
		run = true;
		senderThread.start();
	}
	
	public static void stop() {
		run = false;
	}
	
	private PhotonPacketSender() {}
	
}
