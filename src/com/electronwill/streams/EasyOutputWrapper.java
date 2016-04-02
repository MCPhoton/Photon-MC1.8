package com.electronwill.streams;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A non buffered wrapper around an OutputStream.
 * 
 * @author TheElectronWill
 * 		
 */
public final class EasyOutputWrapper extends EasyOutputStream {
	
	private final OutputStream out;
	
	public EasyOutputWrapper(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
}
