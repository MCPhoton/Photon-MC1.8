/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.electronwill.nbt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for reading and dumping NBT data.
 *
 * @author TheElectronWill
 */
public final class NBT {
	
	private NBT() {}
	
	/**
	 * NBT Tag ID (takes place before the tag payload).
	 *
	 * @see http://wiki.vg/NBT
	 * @see http://minecraft.gamepedia.com/NBT_format
	 */
	public static final byte TAG_END = 0, TAG_BYTE = 1, TAG_SHORT = 2, TAG_INT = 3, TAG_LONG = 4, TAG_FLOAT = 5, TAG_DOUBLE = 6,
			TAG_BYTE_ARRAY = 7, TAG_STRING = 8, TAG_LIST = 9, TAG_COMPOUND = 10, TAG_INT_ARRAY = 11;
			
	/**
	 * Magic value byte of a compressed file.
	 */
	public static final byte GZIP_MAGIC_1 = (byte) 0x1f, GZIP_MAGIC_2 = (byte) 0x8b, ZLIB_MAGIC = (byte) 0x78;
	
	/**
	 * Serializes a TagCompound into an OutputStream.
	 *
	 * @param compound
	 * @param out
	 * @throws java.io.IOException
	 */
	public static void dump(TagCompound compound, OutputStream out) throws IOException {
		NBTWriter writer = new NBTWriter(out);
		writer.write(compound);
		writer.close();
	}
	
	public static TagCompound debugLoad(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		return load(input, true);
	}
	
	/**
	 * Parses a file containing a NBT Tag_Compound. First the encoding of the file is detected, and
	 * a decompression method is chosen if needed.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound load(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		return load(input);
	}
	
	/**
	 * Parses the data of an InputStream containing a NBT Tag_Compound. First the encoding of the
	 * stream is detected, and a decompression method is chosen if needed. This method behaves
	 * exactly as {@link #load(java.io.InputStream, false)}.
	 *
	 * @param in InputStream to read data from
	 * @return the parsed nbt data, as a {@link TagCompound}.
	 * @throws IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound load(InputStream in) throws IOException {
		InputStream cin = CompressionDetector.getStream(in);
		NBTParser parser = new NBTParser(cin);
		return parser.parse();
	}
	
	/**
	 * Parses the a data of an InputStream containing a NBT Tag_Compound. This method does not
	 * support compressed data.
	 *
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound loadRaw(InputStream in) throws IOException {
		NBTParser parser = new NBTParser(in);
		return parser.parse();
	}
	
	/**
	 * Parses the a data of an InputStream containing one or more NBT Tag_Compound. First the
	 * encoding of the stream is detected, and a decompression method is chosen if needed.
	 *
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static List<TagCompound> loadAll(InputStream in) throws IOException {
		InputStream cin = CompressionDetector.getStream(in);
		NBTParser parser = new NBTParser(cin);
		return parser.parseAll();
	}
	
	/**
	 * Parses the a data of an InputStream containing one or more NBT Tag_Compound. This method does
	 * not support compressed data.
	 *
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static List<TagCompound> loadAllRaw(InputStream in) throws IOException {
		NBTParser parser = new NBTParser(in);
		return parser.parseAll();
	}
	
	/**
	 * Parses the data of an InputStream containing a NBT Tag_Compound. First the encoding of the
	 * stream is detected, and a decompression method is chosen if needed.
	 *
	 * @param in InputStream to read data from
	 * @param debug <code>true</code> if we should use {@link NBTParserDebug} instead of
	 *        {@link NBTParser}.
	 * @return the parsed nbt data, as a {@link TagCompound}.
	 * @throws IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound load(InputStream in, boolean debug) throws IOException {
		InputStream cin = CompressionDetector.getStream(in);
		NBTParser parser = debug ? new NBTParserDebug(cin) : new NBTParser(cin);
		return parser.parse();
	}
	
	/**
	 * Parses the a data of a byte array containing a NBT Tag_Compound. This method does not support
	 * compressed data.
	 *
	 * @param input
	 * @return
	 * @throws java.io.IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound load(byte[] input) throws IOException {
		NBTParser parser = new NBTParser(input);
		return parser.parse();
	}
	
	/**
	 * Parses the a data of a ByteBuffer containing a NBT Tag_Compound. This method does not support
	 * compressed data.
	 *
	 * @param input
	 * @return
	 * @throws java.io.IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound load(ByteBuffer input) throws IOException {
		NBTParser parser = new NBTParser(input);
		return parser.parse();
	}
	
	/**
	 * Get the corresponding nbt tag id of an objet, as a single byte.
	 *
	 * @param o the object
	 * @return the NBT tag id of this object
	 */
	public static byte getTagId(final Object o) {
		if (o == null) {
			return TAG_END;
		}
		if (o instanceof int[]) {
			return TAG_INT_ARRAY;
		}
		if (o instanceof byte[]) {
			return TAG_BYTE_ARRAY;
		}
		if (o instanceof HashMap) {
			return TAG_COMPOUND;
		}
		if (o instanceof List) {
			return TAG_LIST;
		}
		Class<?> class1 = o.getClass();
		if (class1 == String.class) {
			return TAG_STRING;
		}
		if (class1 == int.class || class1 == Integer.class) {
			return TAG_INT;
		}
		if (class1 == byte.class || class1 == Byte.class) {
			return TAG_BYTE;
		}
		if (class1 == short.class || class1 == Short.class) {
			return TAG_SHORT;
		}
		if (class1 == long.class || class1 == Long.class) {
			return TAG_LONG;
		}
		if (class1 == float.class || class1 == Float.class) {
			return TAG_FLOAT;
		}
		if (class1 == double.class || class1 == Double.class) {
			return TAG_DOUBLE;
		}
		throw new NBTException("Illegal NBT object type: " + o.getClass().getCanonicalName());
	}
	
}
