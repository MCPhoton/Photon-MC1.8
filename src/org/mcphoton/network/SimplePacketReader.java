package org.mcphoton.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.mcphoton.core.Photon;
import com.electronwill.streams.ByteBufferInputStream;

/**
 * Reads incoming packets that are uncompressed and not encrypted.
 * 
 * @author ElectronWill
 */
public final class SimplePacketReader extends PacketReader {
	
	private ByteBuffer buffer = ByteBuffer.allocateDirect(256);
	private ByteBufferInputStream bufferInput = new ByteBufferInputStream(buffer);
	private int packetLength = -1;
	private boolean failed;
	
	public SimplePacketReader(ClientInfos client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public ReceivablePacket readNext() throws Exception {
		int read = channel.read(buffer);// read some data
		// Photon.log.debug("read " + read);
		if (read == -1) {// end of stream reached
			eos = true;
		}
		
		buffer.flip();// prepare for reading
		// Photon.log.debug("buffer " + buffer.toString());
		
		// Photon.log.debug("previous packetLength " + packetLength);
		if (packetLength == -1) {// get the packet's length if needed
			int varInt = tryReadVarInt();
			if (failed) {
				buffer.position(buffer.limit());
				buffer.limit(buffer.capacity());
				return null;
			}
			if (varInt < 0)
				throw new IOException("Invalid packet's length " + packetLength);
			packetLength = varInt;
			// Photon.log.debug("new packetLength " + packetLength);
			
			if (buffer.remaining() < packetLength) {// the buffer is too small
				ByteBuffer newBuffer = ByteBuffer.allocateDirect(packetLength);// create a bigger buffer
				newBuffer.put(buffer);
				read = channel.read(buffer);// read more data
				Photon.log.debug("read " + read);
				if (read == -1) {
					eos = true;
					return null;
				}
				buffer = newBuffer;
				bufferInput = new ByteBufferInputStream(buffer);
			}
		}
		
		// Photon.log.debug("buffer.limit() " + buffer.limit());
		if (buffer.limit() >= packetLength) {// enough data is available
			int pos0 = buffer.position();
			int packetId = bufferInput.readVarInt();// read the packet's id
			// Photon.log.debug("packetId " + packetId);
			ReceivablePacket packet = ReceivablePacket.construct(client, packetId, bufferInput);
			buffer.position(pos0 + packetLength);// as if the entire packet has been read, even if it's not the case
			buffer.compact();// compact the data = shift it at the beginning of the buffer
			packetLength = -1;// reset state so we'll read the next packet's length
			return packet;
		}
		// Photon.log.debug("return null packet (not enough data to read)");
		return null;
	}
	
	private int tryReadVarInt() {
		buffer.mark();
		int i = 0, shift = 0;
		while (true) {
			if (!buffer.hasRemaining()) {// unable to fully read the VarInt!
				buffer.reset();
				failed = true;
				return i;
			}
			byte b = buffer.get();
			i |= (b & 0x7F) << shift;// Remove sign bit and shift to get the next 7 bits
			shift += 7;
			if (b >= 0) {// b >= 0 -> first bit is 0 -> it's the last VarInt part.
				failed = false;
				return i;
			}
		}
	}
	
}
