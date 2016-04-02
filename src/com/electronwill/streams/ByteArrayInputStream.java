package com.electronwill.streams;

import java.io.IOException;

/**
 * A fast but NOT Thread-safe ByteArrayInputStream.
 *
 * @author TheElectronWill
 */
public final class ByteArrayInputStream extends EasyInputStream {
	
	private byte[] buf;
	private int pos, length;
	
	public ByteArrayInputStream(byte[] buf) {
		this.buf = buf;
		this.pos = 0;
		this.length = buf.length;
	}
	
	public ByteArrayInputStream(byte[] buf, int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.length = length;
	}
	
	@Override
	public int available() {
		return length - pos;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void close() {}
	
	@Override
	public int read(byte[] b) {
		if (pos == length) {
			return -1;
		}
		int avail = length - pos;
		int len = b.length > avail ? avail : b.length;
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, 0, len);
		pos += len;
		return len;
	}
	
	@Override
	public byte readByte() {
		return buf[pos++];
	}
	
	@Override
	public int read(byte[] b, int off, int len) {
		if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (len == 0) {
			return 0;
		}
		if (pos == length) {
			return -1;
		}
		int avail = length - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}
	
	@Override
	public int read() {
		return pos < length ? buf[pos++] & 0xff : -1;
	}
	
	public int seek() throws IOException {
		if (pos == length) {
			return -1;
		}
		return buf[pos];
	}
	
	public int seek(byte[] b, int off, int len) {
		if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (pos == length) {
			return -1;
		}
		int avail = length - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		return len;
	}
	
	public int seek(byte[] b) {
		if (pos == length) {
			return -1;
		}
		int avail = length - pos;
		int len = b.length > avail ? avail : b.length;
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, 0, len);
		return len;
	}
	
	@Override
	public long skip(long n) {
		int avail = length - pos;
		if (n > avail) {
			n = avail;
		}
		pos += n;
		return n;
	}
	
}
