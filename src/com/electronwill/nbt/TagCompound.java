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

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a NBT Tag_Compound. Actually it's just a {@code HashMap<String, Object>} with a name.
 *
 * @author TheElectronWill
 */
public class TagCompound extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	private Optional<String> name;
	
	public TagCompound() {
		super();
		this.name = Optional.empty();
	}
	
	public TagCompound(final String name) {
		super();
		this.name = Optional.of(name);
	}
	
	/**
	 * @return the name
	 */
	public Optional<String> getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = Optional.ofNullable(name);
	}
	
	@Override
	public String toString() {
		return name.isPresent() ? "name=" + name + "; data= " + super.toString() : super.toString();
	}
	
}
