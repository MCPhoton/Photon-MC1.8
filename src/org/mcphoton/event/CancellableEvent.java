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
 * Represents an event triggered when something occur in the game. This type of event may be cancelled by listeners. If
 * an event is cancelled it will finally have no effect. Note that listeners can uncancel previously cancelled events.
 *
 * @author ElectronWill
 * @param <L> Listener class which is notified when an event occur.
 */
public abstract class CancellableEvent extends PhotonEvent {
	
	/**
	 * True if this event is cancelled.
	 */
	protected volatile boolean cancelled = false;
	
	/**
	 * Defines if this event should be cancelled or not. If the event is cancelled it will not occur on the server.
	 *
	 * @param cancelled
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	/**
	 * Returns <code>true</code> if this event is cancelled, <code>false</code> otherwise.
	 *
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
}
