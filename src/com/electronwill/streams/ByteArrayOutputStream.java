package com.electronwill.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * An output stream that writes all data into a byte array. The array automatically grows as data is written to it. A
 * copy of the data can be retrieved using {@link #toByteArray()}, {@link #toByteBuffer()} or {@link #toString()}. The
 * data can be accessed directly (see method javadoc) using {@link #asByteArray()} or {@link #asByteBuffer()}.
 * <p>
 * Closing a ByteArrayOutputStream has no effect. The methods in this class can be called after the stream has been
 * closed without generating an IOException.
 * </p>
 * <p>
 * <b>This class is NOT Thread-safe.</b>
 * </p>
 *
 * @author TheElectronWill
 */
public final class ByteArrayOutputStream extends EasyOutputStream {
	
	private byte[] buf;
	private int length = 0;
	
	/**
	 * Creates a new byte array output stream. The buffer capacity is initially 32 bytes, though its size increases if
	 * necessary.
	 */
	public ByteArrayOutputStream() {
		this.buf = new byte[32];
	}
	
	/**
	 * Creates a new byte array output stream. The buffer capacity is the specified one, though its size increases if
	 * necessary.
	 *
	 * @param initialCapctity
	 */
	public ByteArrayOutputStream(int initialCapctity) {
		this.buf = new byte[initialCapctity];
	}
	
	/**
	 * Returns the underlying byte array that contains the values of this Stream. Modifications to the array are
	 * reflected in the Stream and vice versa. WARNING: the array may be BIGGER than the actual size of the Stream.
	 *
	 * @return
	 */
	public byte[] asByteArray() {
		return buf;
	}
	
	/**
	 * Wraps the content of this stream in a ByteBuffer. Modifications to the ByteBuffer are reflected in the Stream and
	 * vice versa.
	 *
	 * @return a ByteBuffer with its position set to 0
	 */
	public ByteBuffer asByteBuffer() {
		return ByteBuffer.wrap(buf, 0, length);
	}
	
	/**
	 * Returns the current total capacity of the stream.
	 */
	public int capacity() {
		return buf.length;
	}
	
	/**
	 * Clears this stream. A new byte array of size 32 is allocated and the <code>count</code> field is set to zero.
	 */
	public void clear() {
		buf = new byte[32];
		length = 0;
	}
	
	/**
	 * Clears this stream. A new byte array of the specified size is allocated and the <code>count</code> field is set
	 * to zero.
	 *
	 * @param newCapacity the new capacity
	 */
	public void clear(int newCapacity) {
		buf = new byte[newCapacity];
		length = 0;
	}
	
	/**
	 * Does nothing: closing a ByteArrayOutputStream has no effect.
	 */
	@Override
	public void close() {}
	
	/**
	 * Does nothing: flushing a ByteArrayOutputStream has no effect.
	 */
	@Override
	public void flush() {}
	
	/**
	 * Returns the number of bytes that can be written to this Stream without it resizing.
	 */
	public int free() {
		return buf.length - length;
	}
	
	private void grow(int growth) {
		buf = Arrays.copyOf(buf, buf.length + growth);
	}
	
	public void reset() {
		length = 0;
	}
	
	public int size() {
		return length;
	}
	
	/**
	 * Skips n bytes, i.e. increase the length by n without writing anything.
	 * 
	 * @param n the number of bytes to skip.
	 */
	public void skip(int n) {
		length += n;
	}
	
	public byte[] toByteArray() {
		return Arrays.copyOf(buf, length);
	}
	
	/**
	 * Copies the content of this stream in a newly allocated ByteBuffer. There is absolutely NO need to call the flip()
	 * method. The ByteBuffer and the Stream are completely independant.
	 *
	 * @return a ByteBuffer containing what this stream contains.
	 */
	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(Arrays.copyOf(buf, length));
	}
	
	@Override
	public String toString() {
		return new String(buf, 0, length);
	}
	
	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(buf, 0, length, charsetName);
	}
	
	/**
	 * Writes the specified byte to this stream.
	 *
	 * @param b the byte
	 */
	public void write(byte b) {
		if (length >= buf.length) {
			grow(1);
		}
		buf[length++] = b;
	}
	
	@Override
	public void write(byte[] b) {
		if (length + b.length >= buf.length) {
			grow(length + b.length - buf.length);
		}
		System.arraycopy(b, 0, buf, length, b.length);
		length += b.length;
	}
	
	@Override
	public void write(byte[] b, int off, int len) {
		if (off < 0 || len < 0 || off + len > b.length) {
			throw new IndexOutOfBoundsException();
		}
		if (length + len >= buf.length) {
			grow(length + len - buf.length);
		}
		System.arraycopy(b, off, buf, length, len);
		length += len;
	}
	
	@Override
	public void write(int b) {
		write((byte) b);
	}
	
	/**
	 * Writes a byte at the specified position in the stream.
	 *
	 * @param index
	 * @param b
	 */
	public void write(int index, byte b) {
		if (index >= buf.length) {
			buf = Arrays.copyOf(buf, index + 1);
		}
		buf[index] = b;
	}
	
	@Override
	public void writeInt(int i) {
		if (length + 4 >= buf.length) {
			grow(length + 4 - buf.length);
		}
		buf[length++] = (byte) (i >> 24);
		buf[length++] = (byte) (i >> 16);
		buf[length++] = (byte) (i >> 8);
		buf[length++] = (byte) i;
	}
	
	public void writeInts(int[] ints, int off, int len) {
		if (off < 0 || len < 0 || off + len > ints.length) {
			throw new IndexOutOfBoundsException();
		}
		if (length + len * 4 >= buf.length) {
			grow(length + len * 4 - buf.length);
		}
		final int max = off + len;
		for (int j = off; j < max; j++) {
			int i = ints[j];
			buf[length++] = (byte) (i >> 24);
			buf[length++] = (byte) (i >> 16);
			buf[length++] = (byte) (i >> 8);
			buf[length++] = (byte) i;
		}
	}
	
	@Override
	public void writeLong(long l) {
		if (length + 8 >= buf.length) {
			grow(length + 8 - buf.length);
		}
		buf[length++] = (byte) (l >> 56);
		buf[length++] = (byte) (l >> 48);
		buf[length++] = (byte) (l >> 40);
		buf[length++] = (byte) (l >> 32);
		buf[length++] = (byte) (l >> 24);
		buf[length++] = (byte) (l >> 16);
		buf[length++] = (byte) (l >> 8);
		buf[length++] = (byte) l;
	}
	
	@Override
	public void writeShort(short s) {
		if (length + 2 >= buf.length) {
			grow(2);
		}
		buf[length++] = (byte) (s >> 8);
		buf[length++] = (byte) s;
	}
	
	public void writeShorts(short[] shorts, int off, int len) {
		if (off < 0 || len < 0 || off + len > shorts.length) {
			throw new IndexOutOfBoundsException();
		}
		if (length + len * 2 >= buf.length) {
			grow(length + len * 2 - buf.length);
		}
		final int max = off + len;
		for (int i = off; i < max; i++) {
			int s = shorts[i];
			buf[length++] = (byte) (s >> 8);
			buf[length++] = (byte) s;
		}
	}
	
	/**
	 * Writes a part if this Stream to the specified byte array, at the specified offset.
	 *
	 * @param out byte array to write to
	 * @param off offset at which we write the first byte to <code>out</code>
	 * @param len maximum number of bytes to write
	 * @return the number of byte written to <code>out</code>
	 */
	public int writeTo(byte[] out, int off, int len) {
		if (off < 0 || len < 0 | off + len > out.length) {
			throw new IndexOutOfBoundsException();
		}
		if (len > length) {
			len = length;
		}
		System.arraycopy(buf, 0, out, off, len);
		return len;
	}
	
	/**
	 * Writes the complete contents of this stream to the specified ByteBuffer.
	 *
	 * @param out
	 */
	public void writeTo(ByteBuffer out) {
		out.put(buf, 0, length);
	}
	
	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, length);
	}
	
}
