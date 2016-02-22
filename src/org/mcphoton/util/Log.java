/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.util;

import com.electronwill.text.ModifiableCharSequence;

/**
 * A very simple, fast and efficient logger.
 *
 * @author ElectronWill
 */
public abstract class Log {

	/**
	 * Logs an informative message.
	 *
	 * @param msg the message
	 */
	public abstract void info(CharSequence msg);

	/**
	 * Logs a warning message.
	 *
	 * @param msg the message
	 */
	public abstract void warning(CharSequence msg);

	/**
	 * Logs a debug message.
	 *
	 * @param msg the message
	 */
	public abstract void debug(CharSequence msg);

	/**
	 * Logs an error message.
	 *
	 * @param msg the message
	 */
	public abstract void error(CharSequence msg);

	/**
	 * Logs an error message about a Throwable.
	 *
	 * @param t the Throwable
	 */
	public abstract void error(Throwable t);

	/**
	 * Logs an error message about a Throwable, with an additional message.
	 *
	 * @param t the Throwable
	 * @param msg the message
	 */
	public abstract void error(Throwable t, CharSequence msg);

	/**
	 * Logs a "raw" message: it is written as is. It's up to you to add the date, the level and the source.
	 *
	 * @param msg the message
	 */
	public abstract void raw(ModifiableCharSequence msg);

}
