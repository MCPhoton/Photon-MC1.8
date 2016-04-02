package com.electronwill.streams;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * An InputStream designed to read data types of the MC Protocol. ProtocolInputStreams aren't thread-safe.
 * 
 * @author TheElectronWill
 */
public abstract class EasyInputStream extends InputStream {
	
	/**
	 * Reads the next byte and returns false if it is equal to 0, true otherwise.
	 *
	 * @return the next boolean
	 * @throws IOException
	 */
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}
	
	/**
	 * Reads the next byte.
	 *
	 * @return the next byte
	 * @throws IOException if it is impossible to read the next byte
	 */
	public byte readByte() throws IOException {
		final int read = read();
		if (read == -1) {
			throw new EOFException();
		}
		return (byte) read;
	}
	
	/**
	 * Reads the next 8 bytes as a double.
	 * 
	 * @return
	 * @throws IOException
	 */
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	
	/**
	 * Reads the next 4 bytes as a float.
	 * 
	 * @return
	 * @throws IOException
	 */
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}
	
	/**
	 * Reads the next 4 bytes into an int value.
	 *
	 * @return the int value
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		int i1 = read(), i2 = read(), i3 = read(), i4 = read();
		if ((i1 | i2 | i3 | i3) < 0)
			throw new EOFException();
		return ((i1 << 24) + (i2 << 16) + (i3 << 8) + i4);
	}
	
	/**
	 * Reads the next 8 bytes into a long value.
	 *
	 * @return
	 * @throws IOException
	 */
	//@formatter:off
	public long readLong() throws IOException {
        return (((long)readByte() << 56) +
                ((long)(readByte() & 0xff) << 48) +
                ((long)(readByte() & 0xff) << 40) +
                ((long)(readByte() & 0xff) << 32) +
                ((long)(readByte() & 0xff) << 24) +
                ((readByte() & 0xff) << 16) +
                ((readByte() & 0xff) <<  8) +
                (readByte() & 0xff));
	}
	//@formatter:on
	
	/**
	 * Reads the next 2 bytes into a short value.
	 *
	 * @return the short value
	 * @throws IOException
	 */
	public short readShort() throws IOException {
		int i1 = read(), i2 = read();
		if ((i1 | i2) < 0)
			throw new EOFException();
		return (short) ((i1 << 8) + i2);
	}
	
	/**
	 * Reads a String encoded in UTF-8 and prefixed with a varint indicating its size (in bytes).
	 *
	 * @return the String
	 * @throws IOException
	 */
	public String readString() throws IOException {
		int length = readVarInt();
		byte[] utfBytes = new byte[length];
		read(utfBytes);
		return new String(utfBytes, StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads the next byte as an unsigned number.
	 * 
	 * @return an integer containing the value of the unsigned number
	 * @throws IOException
	 */
	public int readUnsignedByte() throws IOException {
		int i = read();
		if (i < 0)
			throw new EOFException();
		return i;
	}
	
	/**
	 * Reads the next 2 bytes as an unsigned number.
	 * 
	 * @return an integer containing the value of the unsigned number
	 * @throws IOException
	 */
	public int readUnsignedShort() throws IOException {
		int i1 = read(), i2 = read();
		if ((i1 | i2) < 0)
			throw new EOFException();
		return (i1 << 8) + i2;
	}
	
	/**
	 * Reads a VarInt.
	 *
	 * @return the int value
	 * @throws IOException
	 */
	public int readVarInt() throws IOException {
		int shift = 0, i = 0;
		while (true) {
			byte b = readByte();
			i |= (b & 0x7F) << shift;// Remove sign bit and shift to get the next 7 bits
			shift += 7;
			if (b >= 0) {// varint prefix = 0 means it's the last part.
				return i;
			}
		}
	}
	
	/**
	 * Reads a VarLong.
	 *
	 * @return the long value
	 * @throws IOException
	 */
	public long readVarLong() throws IOException {
		int shift = 0;
		long l = 0;
		while (true) {
			byte b = readByte();
			l |= (long) (b & 0x7F) << shift;
			shift += 7;
			if (b >= 0) {// varint prefix = 0 means it's the last part.
				return l;
			}
		}
	}
}
