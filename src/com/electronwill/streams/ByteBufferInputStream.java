package com.electronwill.streams;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author TheElectronWill
 */
public final class ByteBufferInputStream extends EasyInputStream {
	
	private final ByteBuffer buff;
	
	public ByteBufferInputStream(ByteBuffer buff) {
		this.buff = buff;
	}
	
	@Override
	public int available() throws IOException {
		return buff.remaining();
	}
	
	@Override
	public int read() throws IOException {
		return buff.hasRemaining() ? Byte.toUnsignedInt(buff.get()) : -1;
	}
	
	@Override
	public byte readByte() throws IOException {
		return buff.get();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int rem = buff.remaining();
		if (len > rem) {
			len = rem;
		}
		buff.get(b, off, len);
		return len;
	}
	
	@Override
	public long skip(long n) throws IOException {
		int rem = buff.remaining();
		if (n > rem) {
			n = rem;
		}
		buff.position((int) (buff.position() + n));
		return n;
	}
	
	@Override
	public int readInt() throws IOException {
		return buff.getInt();
	}
	
	@Override
	public long readLong() throws IOException {
		return buff.getLong();
	}
	
	@Override
	public short readShort() throws IOException {
		return buff.getShort();
	}
	
	@Override
	public double readDouble() throws IOException {
		return buff.getDouble();
	}
	
	@Override
	public float readFloat() throws IOException {
		return buff.getFloat();
	}
	
}
