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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Utility class to simplify operations with the data format of the minecraft's protocol.
 *
 * @author ElectronWill
 */
public final class ProtocolData {

	public static final int ENTITY_META_BYTE = 0;
	public static final int ENTITY_META_SHORT = 1;
	public static final int ENTITY_META_INT = 2;
	public static final int ENTITY_META_FLOAT = 3;
	public static final int ENTITY_META_STRING = 4;
	public static final int ENTITY_META_SLOT = 5;
	public static final int ENTITY_META_3INT = 6;
	public static final int ENTITY_META_3FLOAT = 7;

	/**
	 * Writes a signed integer with the "VarInt" format.
	 *
	 * @param n the int to encode
	 * @param dest where to write the bytes
	 * @return the number of bytes written, maximum 5
	 */
	public static int writeVarInt(int n, ByteBuffer dest) {
		int count = 0;
		while ((n & 0xFFFF_FF80) != 0) {//While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);//Discard bit sign and set msb to 1 (VarInt byte prefix).
			dest.put(data);
			n >>>= 7;
			count++;
		}
		dest.put((byte) n);
		return count + 1;
	}

	/**
	 * Writes a signed integer with the "VarInt" format.
	 *
	 * @param n the int to encode
	 * @param dest where to write the bytes
	 * @return the number of bytes written, maximum 5
	 */
	public static int writeVarInt(int n, OutputStream dest) throws IOException {
		int count = 0;
		while ((n & 0xFFFF_FF80) != 0) {//While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);//Discard bit sign and set msb to 1 (VarInt byte prefix).
			dest.write(data);
			n >>>= 7;
			count++;
		}
		dest.write((byte) n);
		return count + 1;
	}

	/**
	 * Encodes a signed long with the "VarInt" format.
	 *
	 * @param n the long to encode
	 * @return the number of bytes written, maximum 10
	 * @throws java.io.IOException
	 */
	public static int writeVarLong(long n, ByteBuffer dest) throws IOException {
		int count = 0;
		while ((n & 0xFFFF_FFFF_FFFF_FF80l) != 0) {//While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);//Discard bit sign and set msb to 1 (VarInt byte prefix).
			dest.put(data);
			n >>>= 7;
			count++;
		}
		dest.put((byte) n);
		return count + 1;
	}

	/**
	 * Encodes a signed long with the "VarInt" format.
	 *
	 * @param n the long to encode
	 * @return the number of bytes written, maximum 10
	 * @throws java.io.IOException
	 */
	public static int writeVarLong(long n, OutputStream dest) throws IOException {
		int count = 0;
		while ((n & 0xFFFF_FFFF_FFFF_FF80l) != 0) {//While we have more than 7 bits (0b0xxxxxxx)
			byte data = (byte) (n | 0x80);//Discard bit sign and set msb to 1 (VarInt byte prefix).
			dest.write(data);
			n >>>= 7;
			count++;
		}
		dest.write((byte) n);
		return count + 1;
	}

	/**
	 * Reads a VarInt.
	 *
	 * @return the int value
	 * @throws java.io.IOException
	 */
	public static int readVarInt(InputStream in) throws IOException {
		int shift = 0, i = 0;
		while (true) {
			byte b = (byte) in.read();
			i |= (b & 0x7F) << shift;//Remove sign bit and shift to get the next 7 bits
			shift += 7;
			if (b >= 0) {//VarInt byte prefix is 0, it means that we just decoded the last 7 bits, therefore we've finished.
				return i;
			}
		}
	}

	/**
	 * Reads a VarLong.
	 *
	 * @return the long value
	 * @throws java.io.IOException
	 */
	public static long readVarLong(InputStream in) throws IOException {
		int shift = 0;
		long l = 0;
		while (true) {
			byte b = (byte) in.read();
			l |= (long) (b & 0x7F) << shift;//On enlève le préfixe 0 ou 1 et on décale les 7 bits restant à la bonne position
			shift += 7;
			if (b >= 0) {//b >= 0 <=> premier bits vaut 0
				return l;
			}
		}
	}

	public static final byte asUnsignedByte(int n) {
		return (byte) ((n) & 0xFF);
	}

	public static final byte encodeEntityMetadataByte(int type, int index) {
		return (byte) (type << 5 | (index & 0x1F));
	}

	/**
	 * Encodes 3D coordinates in a 64 bytes value like this: 26 bytes for X, 12 for Y, 26 for Z.
	 *
	 * @param x X coordinate, points East in game
	 * @param y Y coordinate, points Upward in game
	 * @param z Z coordinate, points South in game
	 * @return an unsigned 64 bytes value containing the 3D position
	 */
	public static final long encodePosition(int x, int y, int z) {
		return ((long) x << 38) | ((long) y << 26) | (z & 0x3FFFFFF);
	}

	/**
	 * Decodes the X coordinate of a 64 bytes value contaning 3D coordinates.
	 *
	 * @param pos
	 * @return
	 */
	public static final int decodePositionX(long pos) {
		return (int) (pos >> 38);
	}

	/**
	 * Decodes the Y coordinate of a 64 bytes value contaning 3D coordinates.
	 *
	 * @param pos
	 * @return
	 */
	public static final int decodePositionY(long pos) {
		return (int) ((pos >> 26) & 0xFFF);
	}

	/**
	 * Decodes the Z coordinate of a 64 bytes value contaning 3D coordinates.
	 *
	 * @param pos
	 * @return
	 */
	public static final int decodePositionZ(long pos) {
		return (int) (pos << 38 >> 38);
	}

	/**
	 * Converts an integer to a 32 bits fixed-point number.
	 *
	 * @param i
	 * @return
	 */
	public static final int toFixedPoint(int i) {
		return i << 5;
	}

	/**
	 * Converts a double to a 32 bits fixed-point number.
	 *
	 * @param d
	 * @return
	 */
	public static final int toFixedPoint(double d) {
		return (int) (d * 32);//32 because 32 = 2⁵, and the fixed-point used by minecraft has a 5 bits fractional part. d*32 is basically equivalent to d << 5
	}

	/**
	 * Converts a float to a 32 bits fixed-point number.
	 *
	 * @param f
	 * @return
	 */
	public static final int toFixedPoint(float f) {
		return (int) (f * 32);
	}

	/**
	 * Converts an angle in degrees to an angle in steps of 1/256 of a full turn.
	 *
	 * @param degrees the angle, in degrees, as a float.
	 * @return the angle, in steps of 1/256 of a full turn, as an unsigned byte.
	 */
	public static final int toRotationStep(float degrees) {
		return (int) ((degrees * 256.0) / 360.0);
	}

	/**
	 * Converts an angle in steps of 1/256 of a full turn to an angle in degrees.
	 *
	 * @param degrees the angle, in degrees, as an unsigned byte
	 * @return the angle, in steps of 1/256 of a full turn, as a float.
	 */
	public static final float toDegrees(int steps) {
		return (float) ((steps * 360.0) / 256.0);
	}

	/**
	 * Converts a fixed-point number to a double.
	 *
	 * @param fixedPoint
	 * @return
	 */
	public static final double fixedPointToDouble(int fixedPoint) {
		return fixedPoint / 32d;
	}

	/**
	 * Converts a fixed-point number to a float.
	 *
	 * @param fixedPoint
	 * @return
	 */
	public static final float fixedPointToFloat(int fixedPoint) {
		return fixedPoint / 32f;
	}

	/**
	 * Converts a fixed-point number to an integer. The resulting integer does not contains the
	 * fractional part.
	 *
	 * @param fixedPoint
	 * @return
	 */
	public static final int fixedPointToInt(int fixedPoint) {
		return fixedPoint >>> 5;
	}

	/**
	 * Encodes a float into a short with the "Velocity" format.
	 *
	 * @param v a velocity, in unit of 1 block per 50ms.
	 * @return a velocity, in units of 1/8000 of a block per 50ms.
	 */
	public static final short encodeVelocity(double v) {
		return (short) (v * 8000f);
	}

	private ProtocolData() {
	}

}
