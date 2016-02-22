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

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import com.electronwill.collections.IndexMap;
import com.electronwill.streams.EasyInputStream;

/**
 * A minecraft packet that the server receive from the client.
 *
 * @author ElectronWill
 */
public abstract class ReceivablePacket extends MCPacket {
	
	/**
	 * Contains the registered ReceivablePackets for state Play.
	 */
	private static final IndexMap<Constructor<? extends ReceivablePacket>> PLAY_PACKETS = new IndexMap<>(26);
	
	/**
	 * Contains the registered ReceivablePackets for state Init (aka "Handshake").
	 */
	private static final IndexMap<Constructor<? extends ReceivablePacket>> INIT_PACKETS = new IndexMap<>(1);
	
	/**
	 * Contains the registered ReceivablePackets for state Login.
	 */
	private static final IndexMap<Constructor<? extends ReceivablePacket>> LOGIN_PACKETS = new IndexMap<>(2);
	
	/**
	 * Contains the registered ReceivablePackets for state Status.
	 */
	private static final IndexMap<Constructor<? extends ReceivablePacket>> STATUS_PACKETS = new IndexMap<>(2);
	
	/**
	 * Constructs a new ReceivablePacket object corresponding to the given id.
	 *
	 * @param cc client connection
	 * @param id packet's id
	 * @param data packet's data
	 * @return a new ReceivablePacket
	 * @throws java.lang.Exception if a problem occured during packet creation.
	 */
	public static ReceivablePacket construct(ClientInfos cc, int id, EasyInputStream data) throws Exception {
		final Constructor<? extends ReceivablePacket> constructor;
		switch (cc.getState()) {
			case PLAY:
				constructor = PLAY_PACKETS.get(id);
				break;
			case LOGIN:
				constructor = LOGIN_PACKETS.get(id);
				break;
			case STATUS:
				constructor = STATUS_PACKETS.get(id);
				break;
			default:
				constructor = INIT_PACKETS.get(id);
				break;
		}
		if (constructor == null) {
			throw new IllegalArgumentException("Invalid packet's id: " + id);
		}
		return constructor.newInstance(cc, data);
	}
	
	/**
	 * Checks if a packet (standard or custom) is currently registered with the given id.
	 *
	 * @param id
	 * @return
	 */
	public static boolean isRegistered(ConnectionState state, int id) {
		switch (state) {
			case PLAY:
				return PLAY_PACKETS.containsKey(id);
			case LOGIN:
				return LOGIN_PACKETS.containsKey(id);
			case STATUS:
				return STATUS_PACKETS.containsKey(id);
			default:
				return INIT_PACKETS.containsKey(id);
		}
	}
	
	/**
	 * Tries to register a ReceivablePacket with the given id.
	 *
	 * @param id packet's id
	 * @param state
	 * @param packetClass packet's class
	 * @return true if it was successfully registered
	 * @throws NoSuchMethodException if the packet class constructor cannot be found
	 */
	public static boolean register(Class<? extends ReceivablePacket> packetClass, ConnectionState state, int id)
			throws NoSuchMethodException {
		Constructor<? extends ReceivablePacket> constructor = packetClass.getConstructor(ClientInfos.class, EasyInputStream.class);
		return register(constructor, state, id);
	}
	
	/**
	 * Tries to register a ReceivablePacket with the given id.
	 *
	 * @param id packet's id
	 * @param state
	 * @param constructor packet's constructor.
	 * @throws NoSuchMethodException if the packet class constructor cannot be found
	 */
	public static boolean register(Constructor<? extends ReceivablePacket> constructor, ConnectionState state, int id)
			throws NoSuchMethodException {
		if (!org.mcphoton.core.Photon.isRegistrationPhase()) {
			return false;
		}
		switch (state) {
			case PLAY:
				PLAY_PACKETS.put(id, constructor);
				break;
			case LOGIN:
				LOGIN_PACKETS.put(id, constructor);
				break;
			case STATUS:
				STATUS_PACKETS.put(id, constructor);
				break;
			default:
				INIT_PACKETS.put(id, constructor);
				break;
		}
		return true;
	}
	
	/**
	 * Unregisters a custom packet previously registered with the given id, only if it is currently registered with the
	 * given packet's class.
	 *
	 * @param id
	 * @param packetClass
	 * @return true if it was unregistered
	 * @throws java.lang.NoSuchMethodException if the packet class constructor cannot be found
	 */
	public static boolean unregister(Class<? extends ReceivablePacket> packetClass, int id) throws NoSuchMethodException {
		if (!org.mcphoton.core.Photon.isRegistrationPhase()) {
			return false;
		}
		Constructor<? extends ReceivablePacket> constructor = packetClass.getConstructor(ByteBuffer.class);
		return PLAY_PACKETS.remove(id, constructor);
	}
	
	/**
	 * Unregisters a custom packet previously registered with the given id.
	 *
	 * @param state
	 * @param id
	 * @return true if it was unregistered
	 */
	public static boolean unregister(ConnectionState state, int id) {
		if (!org.mcphoton.core.Photon.isRegistrationPhase()) {
			return false;
		}
		return PLAY_PACKETS.remove(id) != null;
	}
	
	/**
	 * Unregisters a custom packet previously registered with the given id, only if it is currently registered with the
	 * given packet's constructor.
	 *
	 * @param id
	 * @param constructor
	 * @return true if it was unregistered
	 */
	public static boolean unregister(Constructor<? extends ReceivablePacket> constructor, int id) {
		if (!org.mcphoton.core.Photon.isRegistrationPhase()) {
			return false;
		}
		return PLAY_PACKETS.remove(id, constructor);
	}
	
	protected final ClientInfos client;
	
	/**
	 * Creates a new ReceivablePacket by using the data of a ByteBuffer. The data does not contains the length of the
	 * packet, nor its length. It's just the data (obviously).
	 *
	 * @param client the client that sent the packet
	 * @param in an EasyInput that contains the entire packet's data
	 * @throws java.lang.Throwable if a problem occurs
	 */
	public ReceivablePacket(ClientInfos client, EasyInputStream in) throws Throwable {
		this.client = client;
	}
	
	/**
	 * Handles this packet: does what it should does when it is received. It typically creates an appropriate Event.
	 * This method is called in the ThreadReceiver.
	 *
	 * @throws java.lang.Throwable if a problem occurs
	 */
	public abstract void handle() throws Throwable;
}
