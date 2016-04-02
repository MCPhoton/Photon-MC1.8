/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.core;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;
import com.electronwill.streams.BlackHoleStream;

/**
 * Redirects everything to the Photon's log, with the debug level.
 *
 * @author ElectronWill
 */
public final class ErrorLogPrintStream extends PrintStream {
	
	private final StringBuilder line = new StringBuilder();
	
	public ErrorLogPrintStream() {
		super(new BlackHoleStream());
	}
	
	@Override
	public PrintStream append(char c) {
		line.append(c);
		return this;
	}
	
	@Override
	public PrintStream append(CharSequence csq, int start, int end) {
		line.append(csq.subSequence(start, end));
		return this;
	}
	
	@Override
	public PrintStream append(CharSequence csq) {
		line.append(csq);
		return this;
	}
	
	@Override
	public PrintStream format(Locale l, String format, Object... args) {
		return append(String.format(l, format, args));
	}
	
	@Override
	public PrintStream format(String format, Object... args) {
		return append(String.format(format, args));
	}
	
	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		append(String.format(l, format, args));
		return logLine();
	}
	
	private ErrorLogPrintStream logLine() {
		error(line);
		line.setLength(0);
		return this;
	}
	
	private void error(CharSequence cs) {
		Photon.log.formatted("(!!)", "\'unknown\'", cs);
	}
	
	@Override
	public PrintStream printf(String format, Object... args) {
		append(String.format(format, args));
		return logLine();
	}
	
	@Override
	public void println(Object x) {
		println(x.toString());
	}
	
	@Override
	public void println(String x) {
		if (line.length() == 0) {
			error(x);
		} else {
			line.append(x);
			logLine();
		}
	}
	
	@Override
	public void println(char[] x) {
		print(x);
		logLine();
	}
	
	@Override
	public void println(double x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println(float x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println(long x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println(int x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println(char x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println(boolean x) {
		println(String.valueOf(x));
	}
	
	@Override
	public void println() {
		logLine();
	}
	
	@Override
	public void print(Object obj) {
		print(obj.toString());
	}
	
	@Override
	public void print(String s) {
		line.append(s);
	}
	
	@Override
	public void print(char[] s) {
		line.append(s);
	}
	
	@Override
	public void print(double d) {
		line.append(d);
	}
	
	@Override
	public void print(float f) {
		line.append(f);
	}
	
	@Override
	public void print(long l) {
		line.append(l);
	}
	
	@Override
	public void print(int i) {
		line.append(i);
	}
	
	@Override
	public void print(char c) {
		line.append(c);
	}
	
	@Override
	public void print(boolean b) {
		line.append(b);
	}
	
	@Override
	public void write(byte[] buf, int off, int len) {
		line.append(Arrays.copyOfRange(buf, off, off + len));
	}
	
	@Override
	public void write(int b) {
		line.append((char) b);
	}
	
	@Override
	protected void clearError() {}
	
	@Override
	protected void setError() {}
	
	@Override
	public boolean checkError() {
		return false;
	}
	
	@Override
	public void close() {
		flush();
	}
	
	@Override
	public void flush() {
		logLine();
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
}
