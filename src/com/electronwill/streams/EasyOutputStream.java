package com.electronwill.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * An OutputStream designed to write data types of the MC Protocol. ProtocolOutputStreams aren't thread-safe.
 * 
 * @author TheElectronWill
 */
public abstract class EasyOutputStream extends OutputStream {
	
	/**
	 * Writes a boolean as a single byte. Its value is 1 if true and 0 if false.
	 */
	public void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}
	
	public void writeChar(char v) throws IOException {
		write(v >> 8);
		write(v);
	}
	
	public void writeChar(int v) throws IOException {
		writeChar((char) v);
	}
	
	public void writeDouble(double d) throws IOException {
		writeLong(Double.doubleToLongBits(d));
	}
	
	public void writeFloat(float f) throws IOException {
		writeInt(Float.floatToIntBits(f));
	}
	
	public void writeInt(int i) throws IOException {
		write(i >> 24);
		write(i >> 16);
		write(i >> 8);
		write(i);
	}
	
	public void writeLong(long l) throws IOException {
		write((byte) (l >> 56));
		write((byte) (l >> 48));
		write((byte) (l >> 40));
		write((byte) (l >> 32));
		write((byte) (l >> 24));
		write((byte) (l >> 16));
		write((byte) (l >> 8));
		write((byte) l);
	}
	
	public void writeShort(int v) throws IOException {
		writeShort((short) v);
	}
	
	public void writeShort(short s) throws IOException {
		write(s >> 8);
		write(s);
	}
	
	/**
	 * Writes a String with the UTF-8 charset, prefixed with its size (in bytes) as a VarInt. <b>The flush() method is
	 * NOT called on the <tt>dest</tt> OutputStream.</b>
	 *
	 * @param s
	 * @return the number of bytes written
	 * @throws java.io.IOException
	 */
	public int writeString(String s) throws IOException {
		int count = 0;
		byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
		count += writeVarInt(bytes.length);
		write(bytes);
		count += bytes.length;
		return count;
	}
	
	/**
	 * Writes a signed integer with the "VarInt" format.
	 *
	 * @param n the int to encode
	 * @return the number of bytes written, maximum 5
	 * @throws java.io.IOException
	 */
	public int writeVarInt(int n) throws IOException {
		int count = 0;
		while ((n & 0xFFFF_FF80) != 0) {// While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);// Discard bit sign and set msb to 1 (VarInt byte prefix).
			write(data);
			n >>>= 7;
			count++;
		}
		write((byte) n);
		return count + 1;
	}
	
	/**
	 * Encodes a signed long with the "VarInt" format into the given ByteBuffer.
	 *
	 * @param n the int to encode
	 * @return the number of bytes written, maximum 10
	 * @throws java.io.IOException
	 */
	public int writeVarLong(long n) throws IOException {
		int count = 0;
		while ((n & 0xFFFF_FFFF_FFFF_FF80l) != 0) {// While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);// Discard bit sign and set msb to 1 (VarInt byte prefix).
			write(data);
			n >>>= 7;
			count++;
		}
		write((byte) n);
		return count + 1;
	}
	
}
