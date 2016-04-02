/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.electronwill.nbt;

import static com.electronwill.nbt.NBT.GZIP_MAGIC_1;
import static com.electronwill.nbt.NBT.GZIP_MAGIC_2;
import static com.electronwill.nbt.NBT.ZLIB_MAGIC;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Detects if a file is compressed by reading 2-bytes magic value.
 *
 * @author TheElectronWill
 */
public class CompressionDetector {

    /**
     * Returns true if the file seems to be compressed with Gzip, Zlib zip or simple Zip format.
     *
     * @param file the file to test
     * @return <code>true</code> if the file seems to be compressed, <code>false</code> otherwise.
     * @throws IOException
     */
    public static boolean isCompressed(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        return isCompressed(in);
    }

    /**
     * Returns true if the data at this URL seems to be compressed with Gzip, Zlib zip or simple Zip
     * format.
     *
     * @param url the url to test
     * @return <code>true</code> if the file seems to be compressed, <code>false</code> otherwise.
     * @throws IOException
     */
    public static boolean isCompressed(URL url) throws IOException {
        InputStream in = url.openStream();
        return isCompressed(in);
    }

    private static boolean isCompressed(InputStream in) throws IOException {
        int i1 = in.read();
        int i2 = in.read();
        if (i1 == -1 || i2 == -1) {//End of stream:
            throw new IOException("End of Stream reached before anything interesting could be read !");
        }
        final byte b1 = (byte) i1;//Magic value, byte n°1
        final byte b2 = (byte) i2;//Magic value, byte n°2
        return ((b1 == GZIP_MAGIC_1 && b2 == GZIP_MAGIC_2) || b1 == ZLIB_MAGIC || (b1 == 0x50 && b2 == 0x4B));
    }

    public static InputStream getStream(InputStream in) throws IOException {
        //We read the potential 2-bytes magic value:
        PushbackInputStream pushbackInputStream = new PushbackInputStream(in, 2);
        int i1 = pushbackInputStream.read();
        int i2 = pushbackInputStream.read();
        if (i1 == -1 || i2 == -1) {//End of stream:
            throw new NBTException("End of Stream reached before anything interesting could be read !");
        }
        final byte b1 = (byte) i1;//Magic value, byte n°1
        final byte b2 = (byte) i2;//Magic value, byte n°2
        pushbackInputStream.unread(b2);//unread the n°2
        pushbackInputStream.unread(b1);//unread the n°1
        if (b1 == GZIP_MAGIC_1 && b2 == GZIP_MAGIC_2) {//it's a GZIP compressed stream:
            return new GZIPInputStream(pushbackInputStream);
        } else if (b1 == ZLIB_MAGIC) {//78 01; 78 DC and 78 9C => ZLIB compressed stream.
            return new InflaterInputStream(pushbackInputStream);
        } else {
            return pushbackInputStream;
        }
    }
}
