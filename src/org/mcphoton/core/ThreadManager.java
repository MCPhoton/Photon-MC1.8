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

/**
 * Abstract class for classes that manage an internal Thread.
 *
 * @author ElectronWill
 */
public interface ThreadManager {
	
	/**
	 * Definitely stops the thread as soon as possible.
	 * 
	 * @throws Exception
	 */
	void stop() throws Exception;
	
	/**
	 * Definitely stops the thread as soon as possible. If the Thread does not stop after <code>maxWait</code>
	 * milliseconds elapsed, it is forcibly stopped.
	 * 
	 * @param maxWait the maximum time to wait, in milliseconds.
	 * @throws Exception
	 */
	default void stop(long maxWait) throws Exception {
		stop();
		long t0 = System.currentTimeMillis();
		while (isRunning()) {
			if (System.currentTimeMillis() - t0 >= maxWait) {
				forciblyStop();
				return;
			}
		}
	}
	
	/**
	 * Starts the thread.
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;
	
	/**
	 * Checks if the thread is currently running.
	 *
	 * @return true if it is running, false if it is stopped/paused.
	 */
	boolean isRunning();
	
	/**
	 * Checks if the thread should be running.
	 * 
	 * @return true if the start() method has been called and the stop() method hasn't been called yet.
	 */
	boolean shouldBeRunning();
	
	/**
	 * Forces the thread to immediatly and definitely stop.
	 *
	 * @deprecated this method is dangerous because it usually calls the {@link Thread#stop()} method.
	 * @see {@link Thread#stop()}
	 */
	@Deprecated
	void forciblyStop();
	
}
