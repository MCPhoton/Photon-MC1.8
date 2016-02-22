package org.mcphoton.network;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.mcphoton.core.Photon;
import org.mcphoton.core.ThreadManager;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.Events;
import org.mcphoton.event.impl.PacketReceiveEvent;
import com.electronwill.concurrent.Constant;

/**
 * Receives the packet from the clients.
 * 
 * @author ElectronWill
 * 		
 */
public final class ReceiverThread implements ThreadManager {
	
	private class Receiver extends Thread {
		
		public Receiver() {
			super("ReceiverThread");
		}
		
		@Override
		public void run() {
			while (run) {
				try {
					int selected = selector.select();
					if (selected == 0)
						continue;
					final Set<SelectionKey> selectedKeys = selector.selectedKeys();
					final Iterator<SelectionKey> iterator = selectedKeys.iterator();
					
					try {
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							// Processes the key:
							
							if (key.isAcceptable()) {// a new client tries to connect
								Photon.log.debug("ReceiverThread: NEW CLIENT -> accept");
								final SocketChannel channel = ssc.accept();
								channel.configureBlocking(false);
								channel.register(selector, SelectionKey.OP_READ);
								ClientInfos.init(ConnectionState.INIT, channel);
							}
							
							if (key.isReadable()) {// a connected client sends some data
								// Photon.log.debug("ReceiverThread: DATA RECEIVED -> read");
								final SocketChannel channel = (SocketChannel) key.channel();
								final ClientInfos client = ClientInfos.get(channel);
								
								if (!channel.isOpen()) {// channel closed
									Photon.log.debug("ReceiverThread: CHANNEL CLOSED -> remove");
									key.cancel();
									ClientInfos.remove(channel);
									OnlinePlayer p = client.getPlayer();
									if (p != null)
										Photon.removePlayer(p);
								}
								
								final PacketReader packetReader = client.getPacketReader();
								
								// Reads and processes all the received packets:
								PacketReceiveEvent event;
								while ((event = packetReader.readNextEvent()) != null) {
									try {
										Events.notifyListeners(event);// notifies the listeners (they may change the
																		// packet)
									} catch (Throwable error) {
										Photon.log.error(error, "Error while notifying event's listeners");
									}
									try {
										// Photon.log.debug("ReceiverThread: Handling packet");
										event.getPacket().handle();// reacts to the packet
									} catch (Throwable error) {
										Photon.log.error(error, "Error while handling the received packet: " + event.getPacket());
									}
								}
								
								if (packetReader.isEndOfStream()) {// end of stream reached
									Photon.log.debug("ReceiverThread: END OF STREAM  (" + client.getAddress() + ") -> remove");
									channel.close();
									key.cancel();
									ClientInfos.remove(channel);
									OnlinePlayer p = client.getPlayer();
									if (p != null)
										Photon.removePlayer(p);
								}
								
							}
							
						} // end of while(iterator.hasNext())
					} catch (Exception ex) {
						Photon.log.errorFrom(ex, "ReceiverThread");
					} finally {
						selectedKeys.clear();
					}
					
				} catch (Exception e) {
					Photon.log.errorFrom(e, "ReceiverThread");
				}
			}
			Photon.log.debug("ReceiverThread stopped");
		}
	}
	
	private static final Constant<ReceiverThread> instance = new Constant<>();
	
	public static ReceiverThread createInstance(final InetSocketAddress bindAddress) throws Exception {
		ReceiverThread t = new ReceiverThread(bindAddress);
		instance.init(t);
		return t;
	}
	
	static ReceiverThread instance() {
		return instance.get();
	}
	
	private volatile boolean run;
	
	private final Selector selector;
	private final ServerSocketChannel ssc;
	private final Thread t = new Receiver();
	
	private ReceiverThread(InetSocketAddress bindAddress) throws Exception {
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(bindAddress);
		selector = Selector.open();
	}
	
	@Deprecated
	@Override
	public void forciblyStop() {
		t.stop();
		run = false;
	}
	
	@Override
	public boolean isRunning() {
		return t.isAlive();
	}
	
	@Override
	public boolean shouldBeRunning() {
		return run;
	}
	
	@Override
	public void start() throws ClosedChannelException {
		run = true;
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		t.start();
	}
	
	@Override
	public void stop() {
		run = false;
	}
	
}
