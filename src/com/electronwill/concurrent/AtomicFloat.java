/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.electronwill.concurrent;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.intBitsToFloat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An atomic float, based on an AtomicInteger.
 *
 * @author TheElectronWill
 */
public final class AtomicFloat extends Number {
	
	private static final long serialVersionUID = 1L;
	private final AtomicInteger bits;
	
	public AtomicFloat() {
		this(0f);
	}
	
	public AtomicFloat(float initialValue) {
		int fBits = floatToIntBits(initialValue);
		bits = new AtomicInteger(fBits);
	}
	
	/**
	 * Adds something to the float value, and then returns it.
	 *
	 * @param delta
	 * @return
	 */
	public float addAndGet(float delta) {
		while (true) {
			final int icurrent = bits.get();
			final float current = intBitsToFloat(icurrent);
			final float next = current + delta;
			final int inext = floatToIntBits(next);
			if (bits.compareAndSet(icurrent, inext)) {
				return next;
			}
		}
	}
	
	@Override
	public byte byteValue() {
		return (byte) get();
	}
	
	/**
	 * Sets the float value if its actual value is the expected one.
	 *
	 * @param expect
	 * @param update
	 * @return
	 */
	public boolean compareAndSet(float expect, float update) {
		return bits.compareAndSet(floatToIntBits(expect), floatToIntBits(update));
	}
	
	/**
	 * Decrements the float value, and then returns it.
	 *
	 * @return
	 */
	public float decrementAndGet() {
		return addAndGet(-1.0f);
	}
	
	@Override
	public double doubleValue() {
		return (double) get();
	}
	
	@Override
	public float floatValue() {
		return get();
	}
	
	/**
	 * Returns the float value.
	 *
	 * @return
	 */
	public float get() {
		return intBitsToFloat(bits.get());
	}
	
	/**
	 * Sets the float value.
	 *
	 * @param newValue
	 */
	public void set(float newValue) {
		bits.set(floatToIntBits(newValue));
	}
	
	/**
	 * Adds something to the float value.
	 *
	 * @param delta
	 * @return
	 */
	public float getAndAdd(float delta) {
		while (true) {
			final int icurrent = bits.get();
			final float current = intBitsToFloat(icurrent);
			final float next = current + delta;
			final int inext = floatToIntBits(next);
			if (bits.compareAndSet(icurrent, inext)) {
				return current;
			}
		}
	}
	
	/**
	 * Adds -1 to the float value.
	 *
	 * @return
	 */
	public float getAndDecrement() {
		return getAndAdd(-1.0f);
	}
	
	/**
	 * Adds 1 to the float value.
	 *
	 * @return
	 */
	public float getAndIncrement() {
		return getAndAdd(1.0f);
	}
	
	/**
	 * Gets and sets the float value.
	 *
	 * @param newValue
	 * @return
	 */
	public float getAndSet(float newValue) {
		return intBitsToFloat(bits.getAndSet(floatToIntBits(newValue)));
	}
	
	/**
	 * Increments the float value, and then returns it.
	 *
	 * @return
	 */
	public float incrementAndGet() {
		return addAndGet(1.0f);
	}
	
	@Override
	public int intValue() {
		return (int) get();
	}
	
	/**
	 * Eventually set the float value.
	 *
	 * @param newValue
	 */
	public void lazySet(float newValue) {
		bits.lazySet(floatToIntBits(newValue));
	}
	
	@Override
	public long longValue() {
		return (long) get();
	}
	
	@Override
	public short shortValue() {
		return (short) get();
	}
	
	/**
	 * Returns a representation of this float number using {@link Float#toString(float)}.
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return Float.toString(get());
	}
	
	/**
	 * Eventually sets the float value if its actual value is the expected one.
	 *
	 * @param expect
	 * @param update
	 * @return
	 */
	public boolean weakCompareAndSet(float expect, float update) {
		return bits.weakCompareAndSet(floatToIntBits(expect), floatToIntBits(update));
	}
	
}
