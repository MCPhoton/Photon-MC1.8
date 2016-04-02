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

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author TheElectronWill
 */
public class EmptyInputStream extends InputStream {

    @Override
    public int read() throws IOException {
        return -1;
    }

    @Override
    public int available() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return -1;
    }

    @Override
    public long skip(long n) throws IOException {
        return 0;
    }

}
