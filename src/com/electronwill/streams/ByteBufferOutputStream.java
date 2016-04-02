package com.electronwill.streams;

import java.nio.ByteBuffer;

/**
 * An output stream that puts all the data into a ByteBuffer. This class is NOT Thread-safe.
 *
 * @author TheElectronWill
 */
public final class ByteBufferOutputStream extends EasyOutputStream {
	
	private final ByteBuffer buff;
	
	public ByteBufferOutputStream(ByteBuffer buff) {
		if (buff.capacity() < 10) {
			throw new IllegalArgumentException("Illegal ByteBuffer's capacity (min is 10): " + buff.capacity());
		}
		this.buff = buff;
	}
	
	public ByteBufferOutputStream(int capacity, boolean direct) {
		if (capacity < 10) {
			throw new IllegalArgumentException("Illegal ByteBuffer's capacity (min is 10): " + capacity);
		}
		this.buff = direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
	}
	
	/**
	 * Clears the underlying ByteBuffer.
	 * 
	 * @see ByteBuffer#clear()
	 */
	public void clear() {
		buff.clear();
	}
	
	/**
	 * Gets the underlying ByteBuffer.
	 */
	public ByteBuffer getBuffer() {
		return buff;
	}
	
	@Override
	public void write(int b) {
		buff.put((byte) b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) {
		buff.put(b, off, len);
	}
	
	@Override
	public void write(byte[] b) {
		buff.put(b);
	}
	
	@Override
	public void writeDouble(double d) {
		buff.putDouble(d);
	}
	
	@Override
	public void writeFloat(float f) {
		buff.putFloat(f);
	}
	
	@Override
	public void writeInt(int i) {
		buff.putInt(i);
	}
	
	@Override
	public void writeLong(long l) {
		buff.putLong(l);
	}
	
	@Override
	public void writeShort(short s) {
		buff.putShort(s);
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void close() {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void flush() {}
	
}
