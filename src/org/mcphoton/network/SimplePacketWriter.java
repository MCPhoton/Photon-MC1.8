package org.mcphoton.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import org.mcphoton.core.Photon;
import org.mcphoton.util.ProtocolData;
import com.electronwill.streams.ByteArrayOutputStream;

/**
 * Writes outgoing packets uncompressed and not encrypted. This class is NOT Thread-safe.
 * 
 * @author ElectronWill
 */
public final class SimplePacketWriter extends PacketWriter {
	
	private final Queue<DataSending> sendingQueue = new ArrayDeque<>();
	
	public SimplePacketWriter(ClientInfos client, SocketChannel channel) {
		super(client, channel);
	}
	
	@Override
	public void write(SendablePacket packet) throws Exception {
		write(packet, null);
	}
	
	@Override
	public void write(SendablePacket packet, Runnable onSendingCompleted) throws Exception {
		final ByteArrayOutputStream data = new ByteArrayOutputStream(Math.min(packet.maxDataSize(), 8192));// can grow
		final ByteBuffer lengthBuff = ByteBuffer.allocate(5);
		
		data.writeVarInt(packet.id());// gets packet's id
		packet.writeTo(data);// gets packet's data
		// Photon.log.debug("SimplePacketWriter: data.size() = " + data.size());
		ProtocolData.writeVarInt(data.size(), lengthBuff);// gets data's length
		lengthBuff.flip();
		
		DataSending lengthSending = new DataSending(lengthBuff);
		DataSending dataSending = new DataSending(data.asByteBuffer(), onSendingCompleted);
		sendingQueue.offer(lengthSending);
		sendingQueue.offer(dataSending);
	}
	
	@Override
	public boolean writeNow(SendablePacket packet) throws Exception {
		return writeNow(packet, null);
	}
	
	@Override
	public boolean writeNow(SendablePacket packet, Runnable onSendingCompleted) throws Exception {
		final ByteArrayOutputStream data = new ByteArrayOutputStream(Math.min(packet.maxDataSize(), 8192));// can grow
		final ByteBuffer lengthBuff = ByteBuffer.allocate(5);
		
		data.writeVarInt(packet.id());// gets packet's id
		packet.writeTo(data);// gets packet's data
		// Photon.log.debug("SimplePacketWriter: data.size() = " + data.size());
		ProtocolData.writeVarInt(data.size(), lengthBuff);// gets data's length
		lengthBuff.flip();
		final ByteBuffer dataBuff = data.asByteBuffer();
		
		if (!sendingQueue.isEmpty()) {// we cannot write our packet now: must write those in the queue first
			sendingQueue.offer(new DataSending(lengthBuff));
			sendingQueue.offer(new DataSending(dataBuff, onSendingCompleted));
			return false;
		}
		
		// Tries to write lengthBuff immediately:
		channel.write(lengthBuff);
		if (lengthBuff.hasRemaining()) {// incomplete write
			sendingQueue.offer(new DataSending(lengthBuff));
			sendingQueue.offer(new DataSending(dataBuff, onSendingCompleted));
			return false;
		}
		// Tries to write dataBuff immediately:
		channel.write(dataBuff);
		if (dataBuff.hasRemaining()) {// incomplete write
			// at this point, lengthBuff has already been written, so no need to add it to the queue
			sendingQueue.offer(new DataSending(dataBuff, onSendingCompleted));
			return false;
		}
		return true;
	}
	
	@Override
	public boolean flush() throws IOException {
		while (true) {
			final DataSending ds = sendingQueue.peek();
			if (ds == null)// empty queue
				return true;
			if (!channel.isOpen())// channel closed
				return false;
			final ByteBuffer buff = ds.getBuffer();
			channel.write(buff);
			if (buff.hasRemaining()) { // incomplete write
				return false;
			} else {
				sendingQueue.remove();
				try {
					ds.onCompleted();
				} catch (Throwable error) {
					Photon.log.errorFrom(error, "onCompletedSending Runnable");
					// Let the loop continue
				}
			}
		}
	}
	
}
