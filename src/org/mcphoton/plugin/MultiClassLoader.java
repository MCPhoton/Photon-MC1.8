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
package org.mcphoton.plugin;

import java.util.Collections;
import java.util.List;
import com.electronwill.collections.OpenList;

/**
 * A ClassLoader that allow to "share" classes between plugins. A ClassLoader that is a child of a MultiClassLoader
 * CANNOT be added to this MultiClassLoader because it would create an endless loop. To allow plugins to share classes
 * they MUST be loaded with this MultiClassLoader.
 *
 * @author ElectronWill
 */
public final class MultiClassLoader extends ClassLoader {
	
	private final List<ClassLoader> classLoaders = new OpenList<>();
	
	public synchronized void addLoader(ClassLoader cl) {
		if (cl == this)
			throw new IllegalArgumentException("Cannot add a MultiClassLoader to itself.");
		if (cl.getParent() == this) {
			throw new IllegalArgumentException(
					"Cannot add a ClassLoader that is a child of this MultiClassLoader because it would create an endless loop");
		}
		classLoaders.add(cl);
	}
	
	public synchronized void removeLoader(ClassLoader cl) {
		classLoaders.remove(cl);
	}
	
	public synchronized void removeLoader(final int index) {
		classLoaders.remove(index);
	}
	
	public synchronized List<ClassLoader> getClassLoaders() {
		return Collections.unmodifiableList(classLoaders);
	}
	
	@Override
	public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = findLoadedClass(name);
		if (c != null) {
			return c;
		}
		ClassLoader parent = getParent();
		if (parent != null) {
			try {
				c = parent.loadClass(name);
				if (c != null) {
					return c;
				}
			} catch (ClassNotFoundException e) {
				// ignore and continue
			}
		}
		for (ClassLoader cl : classLoaders) {
			try {
				c = cl.loadClass(name);
				if (c != null) {
					break;
				}
			} catch (ClassNotFoundException e) {
				// ignore and continue
			}
		}
		if (c == null) {
			throw new ClassNotFoundException(name);
		}
		return c;
	}
	
}
