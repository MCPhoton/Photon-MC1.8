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

import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author ElectronWill
 */
public final class UnresolvableDependancyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private final String msg;
	
	public UnresolvableDependancyException(String plugin, List<String> dependancies) {
		StringBuilder mcs = new StringBuilder(plugin);
		mcs.append(" depends on : ");
		for (Iterator iterator = dependancies.iterator(); iterator.hasNext();) {
			String dep = (String) iterator.next();
			mcs.append(dep);
			if (iterator.hasNext())
				mcs.append(", ");
		}
		msg = mcs.toString();
	}
	
	public UnresolvableDependancyException(String plugin, String... dependancies) {
		StringBuilder mcs = new StringBuilder(plugin);
		mcs.append(" depends on : ");
		for (int i = 0; i < dependancies.length; i++) {
			String dep = dependancies[i];
			mcs.append(dep);
			if (i + 1 < dependancies.length)
				mcs.append(", ");
		}
		msg = mcs.toString();
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
	@Override
	public String getLocalizedMessage() {
		return msg;
	}
	
}
