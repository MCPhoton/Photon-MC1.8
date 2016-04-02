package com.electronwill.streams;

import java.io.IOException;

/**
 * A stream that discards everything. That is, its methods do absolutely nothing.
 *
 * @author TheElectronWill
 */
public final class BlackHoleStream extends EasyOutputStream {
	
	/**
	 * Does nothing.
	 */
	@Override
	public void write(int b) {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void write(byte[] b) {}
	
	/**
	 * Does nothing.
	 */
	/**
	 * Does nothing.
	 */
	@Override
	public void write(byte[] b, int off, int len) {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeBoolean(boolean v) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeChar(char v) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeChar(int v) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeDouble(double d) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeFloat(float f) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeInt(int i) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeLong(long l) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeShort(int v) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeShort(short s) throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public int writeString(String s) throws IOException {
		return 0;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public int writeVarInt(int n) throws IOException {
		return 0;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public int writeVarLong(long n) throws IOException {
		return 0;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void flush() throws IOException {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void close() throws IOException {}
	
}
