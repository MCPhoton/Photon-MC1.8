package com.electronwill.streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * A unbuffered wrapper around an InputStream.
 * 
 * @author TheElectronWill
 * 		
 */
public final class EasyInputWrapper extends EasyInputStream {
	
	private final InputStream in;
	
	public EasyInputWrapper(InputStream in) throws IOException {
		this.in = in;
	}
	
	@Override
	public int available() throws IOException {
		return in.available();
	}
	
	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}
	
	@Override
	public int read() throws IOException {
		return in.read();
	}
	
	@Override
	public byte readByte() throws IOException {
		int read = in.read();
		if (read == -1)
			throw new IOException("End of stream reached");
		return (byte) read;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}
	
}
