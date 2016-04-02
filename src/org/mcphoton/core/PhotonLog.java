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

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import org.mcphoton.messaging.Color;
import org.mcphoton.util.Log;

/**
 * Handles the server's log.
 * 
 * @author ElectronWill
 */
public final class PhotonLog extends Log {
	
	private static final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
			.appendValue(DAY_OF_MONTH, 2)
			.appendLiteral('-')
			.appendValue(MONTH_OF_YEAR, 2)
			.appendLiteral('-')
			.appendValue(YEAR, 4)
			.toFormatter();
			
	private static final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
			.appendLiteral('[')
			.appendValue(HOUR_OF_DAY, 2)
			.appendLiteral(':')
			.appendValue(MINUTE_OF_HOUR, 2)
			.appendLiteral(':')
			.appendValue(SECOND_OF_MINUTE, 2)
			.appendLiteral(']')
			.toFormatter();
			
	private final Path logLinkPath = new File(Photon.baseDir, "server.log").toPath();
	
	private File logFile;
	private BufferedWriter logFileWriter;
	private final PrintWriter consoleWriter;
	private volatile boolean debug = false;
	
	private final StringBuilder line = new StringBuilder(100);
	private int lastDay;// the last day of month
	
	public PhotonLog(PrintWriter consoleWriter) throws IOException {
		LocalDate currentDate = LocalDate.now();
		this.consoleWriter = consoleWriter;
		this.logFile = getLogFile(currentDate);
		this.logFileWriter = new BufferedWriter(new FileWriter(logFile, true));// appends to the file
		if (!Files.isSymbolicLink(logLinkPath)) {// creates a link so it's easier and faster to find the log file
			Files.deleteIfExists(logLinkPath);
			Files.createSymbolicLink(logLinkPath, logFile.toPath());
		}
		this.lastDay = currentDate.getDayOfMonth();
	}
	
	synchronized void consoleCommand(CharSequence cmd) {
		LocalDateTime dateTime = LocalDateTime.now();
		timeFormatter.formatTo(dateTime, line);
		line.append(" >>> ").append(cmd);
		try {
			logFileWriter.write(line.toString());
			logFileWriter.newLine();
		} catch (IOException ex) {
			error(ex, "Unable to write console command in the log file!");;
		} finally {
			line.setLength(0);
		}
	}
	
	@Override
	public void debug(CharSequence msg) {
		if (debug)
			formatted("(++)", "Photon", msg);
	}
	
	@Override
	public void error(CharSequence msg) {
		formatted("(!!)", "Photon", msg);
	}
	
	@Override
	public void info(CharSequence msg) {
		formatted("(--)", "Photon", msg);
	}
	
	@Override
	public void warning(CharSequence msg) {
		formatted("(WW)", "Photon", msg);
	}
	
	@Override
	public synchronized void error(Throwable t) {
		errorFrom(t, "Photon");
	}
	
	@Override
	public synchronized void error(Throwable t, CharSequence msg) {
		error(msg);
		error(t);
	}
	
	public synchronized void errorFrom(Throwable t, String source) {
		final String message = t.getLocalizedMessage();
		if (message == null)
			formatted("(!!)", source, t.getClass().getName());
		else
			formatted("(!!)", source, t.getClass().getName() + ": " + message);
		StackTraceElement[] stack = t.getStackTrace();
		if (stack.length == 0) {
			t.fillInStackTrace();
		}
		final StringBuilder mcs = new StringBuilder();
		final int offset = (stack[0].getClassName().equals(PhotonLog.class.getName())) ? 1 : 0;
		for (int i = offset; i < stack.length; i++) {
			StackTraceElement element = stack[i];
			mcs.append("\tat ").append(element);
			raw(mcs);
			mcs.setLength(0);
		}
		final Throwable cause = t.getCause();
		if (cause != null) {
			errorFrom(cause, source);
		}
	}
	
	public synchronized void errorFrom(Throwable t, String source, CharSequence msg) {
		error(msg);
		errorFrom(t, source);
	}
	
	/**
	 * Flushes the writers of this log to ensure all the messages are written.
	 */
	public void flush() throws IOException {
		consoleWriter.flush();
		logFileWriter.flush();
	}
	
	/**
	 * Logs a message with the date, level and source inserted at its start. Like this:<br />
	 * {@code [dd-mm-yyy] (lv) Source: Message text}
	 *
	 * @param msg the message
	 */
	public void formatted(CharSequence level, CharSequence source, CharSequence msg) {
		LocalDateTime dateTime = LocalDateTime.now();
		synchronized (this) {
			timeFormatter.formatTo(dateTime, line);
			line.append(' ').append(level).append(' ').append(source).append(": ").append(msg);
			raw(line, dateTime);
			line.setLength(0);
		}
	}
	
	public void formatted(CharSequence level, CharSequence source, CharSequence msg, Color color) {
		LocalDateTime dateTime = LocalDateTime.now();
		synchronized (this) {
			line.append(color.ansi);// set color
			timeFormatter.formatTo(dateTime, line);
			line.append(' ').append(level).append(' ').append(source).append(": ").append(msg);
			line.append("\u001B[0m");// reset
			raw(line, dateTime);
			line.setLength(0);
		}
	}
	
	@Override
	public synchronized void raw(StringBuilder line) {
		LocalDateTime dateTime = LocalDateTime.now();
		raw(line, dateTime);
	}
	
	public synchronized void raw(StringBuilder line, LocalDateTime dateTime) {
		if (line != null && line.length() > 0) {
			try {
				int day = dateTime.getDayOfMonth();
				if (day != lastDay) {
					logFileWriter.flush();
					File newLogFile = getLogFile(dateTime);
					logFileWriter.close();
					logFileWriter = new BufferedWriter(new FileWriter(newLogFile, true));// appends to the file
					logFile = newLogFile;
					Files.deleteIfExists(logLinkPath);
					Files.createSymbolicLink(logLinkPath, logFile.toPath());
					lastDay = day;
				}
				logFileWriter.write(line.toString());
				logFileWriter.newLine();
			} catch (Exception ex) {
				consoleWriter.write("[??:??:??] (!!) Photon : Unable to write in the log file. " + ex);
			}
			consoleWriter.write(line.toString());
			consoleWriter.println();
		}
	}
	
	private File getLogFile(TemporalAccessor date) {
		String formatted = dateFormatter.format(date);
		return new File(Photon.logsDir, formatted + ".log");
	}
	
	/**
	 * Checks if debug messages are displayed.
	 * 
	 * @return true if debug messages are displayed, false otherwise.
	 */
	public boolean isDebugEnabled() {
		return debug;
	}
	
	/**
	 * Sets if debug messages are displayed of not.
	 * 
	 * @param debugEnabled true to show all debug messages, false to hide all of them.
	 */
	public void setDebugEnabled(boolean debugEnabled) {
		this.debug = debugEnabled;
	}
}
