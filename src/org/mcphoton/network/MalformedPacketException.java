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
package org.mcphoton.network;

import java.io.IOException;

/**
 * Signals that a packet cannot be read because the server did not received the correct amount of
 * bytes. This generally means that not enough bytes were sent by the client.
 *
 * @author ElectronWill
 */
public class MalformedPacketException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MalformedPacketException(int packetId, int expectedLength, int actualLength) {
		super("Invalid packet 0x" + Integer.toHexString(packetId) + ": expected " + expectedLength + " bytes of data, received " + actualLength + ". Difference " + (expectedLength - actualLength));
	}

	public MalformedPacketException(String message) {
		super(message);
	}

	public MalformedPacketException(Throwable cause) {
		super(cause);
	}

	public MalformedPacketException(String message, Throwable cause) {
		super(message, cause);
	}

}
