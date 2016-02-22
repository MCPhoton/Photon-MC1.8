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
package org.mcphoton.event;

/**
 * Represents an order for listening for events. It allows to organize what listener will be notified of an event first.
 * Note that if two listeners are registered with the same order, which one is notified first is undefined and cannot be
 * predicted.
 *
 * @author ElectronWill
 */
public enum ListenOrder {
	
	/**
	 * The listener is notified before all the others.
	 */
	VERY_FIRST,
	/**
	 * The listener is notified before all the others, except the "very first" ones.
	 */
	FIRST,
	/**
	 * The listener is notified before all the others, except the "very first" and "first" ones.
	 */
	NORMAL,
	/**
	 * The listener is notified after the "very first", "first" and "normal" ones.
	 */
	LAST,
	/**
	 * The listener is notified after all the others, except the "watching" ones.
	 */
	VERY_LAST,
	/**
	 * The listener is notified after all the others, but cannot modify nor change the cancelled state of the event.
	 */
	WATCHING;
	
}
