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

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;
import com.electronwill.collections.Bag;
import com.electronwill.collections.SimpleBag;

/**
 * A Thread responsible for running plugins' tasks and for making game updates.
 *
 * @author ElectronWill
 */
public final class UpdateThread implements ThreadManager {
	
	private static volatile UpdateThread[] threads;
	
	/**
	 * Submits a single Updateable object. They will be assigned to a Thread and updated every tick when needed.
	 */
	public static synchronized void submit(Updateable u) {
		for (int i = 0; i < threads.length; i++) {
			UpdateThread ut = threads[i];
			if (ut.isFast()) {
				ut.toUpdate.add(u);
				break;
			}
		}
	}
	
	/**
	 * Creates a new array of UpdateThreads with the given size, fills it with new UpdateThread objects, and starts
	 * them.
	 *
	 * @param n number of Threads to create.
	 */
	public static synchronized void start(final int n) {
		if (threads != null) {
			throw new IllegalStateException("The UpdateThreads cannot be initialized more than once!");
		}
		threads = new UpdateThread[n];
		for (int i = 0; i < n; i++) {
			UpdateThread t = new UpdateThread(i);
			threads[i] = t;
			t.start();
		}
	}
	
	/**
	 * Adds some new UpdateThreads.
	 */
	public static synchronized void addThreads(int add) {
		final int length = threads.length;
		threads = Arrays.copyOf(threads, length + add);
		for (int i = length; i < threads.length; i++) {
			threads[i] = new UpdateThread(i);
		}
	}
	
	/**
	 * Removes one UpdateThread.
	 */
	public static synchronized void removeOneThread() {
		UpdateThread last = threads[threads.length - 1];
		last.stop();
		threads = Arrays.copyOf(threads, threads.length - 1);
		for (Updateable u : last.toUpdate) {
			submit(u);
		}
	}
	
	private final Bag<Updateable> toUpdate;
	private final Thread t = new Consumer();
	private boolean run = false;
	private volatile long tickDuration = 0;// in ms
	
	private final RandomBoolean randomBoolean = new RandomBoolean();
	private final int id;
	
	private UpdateThread(final int id) {
		this(id, new SimpleBag<>(200, 10));
	}
	
	private UpdateThread(final int id, Bag<Updateable> updateables) {
		this.toUpdate = updateables;
		this.id = id;
	}
	
	/**
	 * Starts this UpdateThread, if not already started. <b>This method is not Thread-safe.</b>
	 */
	@Override
	public void start() {
		t.start();
		run = true;
	}
	
	@Override
	public void stop() {
		run = false;
	}
	
	/**
	 * Stop immediatly this UpdateThread's internal Thread.
	 *
	 * @deprecated <b>This method is dangerous because it calls {@link Thread#stop()}.</b>
	 */
	@Deprecated
	@Override
	public void forciblyStop() {
		t.stop();
		run = false;
	}
	
	@Override
	public boolean shouldBeRunning() {
		return run;
	}
	
	@Override
	public boolean isRunning() {
		return t.isAlive();
	}
	
	/**
	 * Checks if the tickDuration of this Thread is less than the avarage tickDuration of all Threads.
	 *
	 * @return
	 */
	boolean isFast() {
		int sum = 0;
		for (int i = 0; i < threads.length; i++) {
			sum += threads[i].tickDuration;
		}
		return tickDuration * threads.length <= sum;
	}
	
	/**
	 * Gets the duration of the last tick, in nanoseconds.
	 */
	public long tickDuration() {
		return tickDuration;
	}
	
	/**
	 * Internal pseudo-random boolean generator. It randomly generates an int and uses its 32 bits to return 32
	 * booleans: 1 is true, 0 is false. An generated int is only used once. A new one is generated when needed.
	 */
	private class RandomBoolean {
		
		ThreadLocalRandom localRandom = ThreadLocalRandom.current();
		
		public RandomBoolean() {
			n = localRandom.nextLong();
			bit = 0;
		}
		
		private long n;// generated number
		private int bit;// bit index
		
		boolean next() {
			if (bit == 64) {
				n = localRandom.nextLong();
				bit = 0;
			}
			return (n & (1 << bit++)) == 1;
		}
	}
	
	/**
	 * Internal consumer Runnable used to create Threads.
	 */
	private class Consumer extends Thread {
		
		/**
		 * Main game loop. Executes in the following order:
		 * <li>Available plugin tasks
		 * <li>Game updates: entity, blocks or any other thing that need its {@link Updateable#update()} method to be
		 * called.
		 * <li>If there is some remaining time, repeat the following things until the elapsed time is greater than or
		 * equal to 50ms: <br>
		 * if there is more than 10ms remaining: try to sleep for 5ms <br>
		 * else: execute available plugin tasks.
		 */
		@Override
		public void run() {
			while (run) {// Main loop. 1 tick = 50 ms = 50 000 000 ns
				long t0 = System.nanoTime();// Initial time in nanoseconds.
				
				// == Game update ==
				boolean warned = false;
				for (int i = 0; i < toUpdate.size(); i++) {
					if (System.nanoTime() - t0 > 50_000_000 && !warned) {
						warned = true;
						Photon.log.warning("Tick lasts LONGER than 50 milliseconds !\nNumber or remaining Updateables: " + toUpdate.size());
					}
					Updateable u = toUpdate.get(i);
					try {
						if (u.canUpdate()) {// We can update this Updateable now
							if (!u.updatesRandomly()) {
								u.update();// do update
							} else if (randomBoolean.next()) {
								u.update();// do update randomly
							}
						}
					} catch (Throwable t) {
						Photon.log.error(t, "Error in update loop");
					}
				}
				if (warned) {
					continue;
				}
				long t1 = System.nanoTime();
				tickDuration = t1 - t0;
				
				// == Wait for the next tick ==
				long timeElapsed;
				while ((timeElapsed = System.nanoTime() - t0) < 50_000_000) {
					// == Wait ==
					if (timeElapsed < 40_000_000) {// time left > 8ms
						LockSupport.parkNanos(5_000_000);// parks for 5ms
					}
				}
			}
		}
	}
	
}
