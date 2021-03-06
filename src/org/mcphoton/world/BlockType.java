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
package org.mcphoton.world;

import java.util.HashMap;
import java.util.Map;
import org.mcphoton.core.Photon;
import com.electronwill.collections.IndexMap;
import com.electronwill.concurrent.IntConstant;

/**
 * Defines a type of block.
 *
 * @author ElectronWill
 */
public abstract class BlockType {
	
	private static final Map<String, BlockType> NAMES = new HashMap<>();
	private static final IndexMap<BlockType> IDS = new IndexMap<>();
	private static int nextId = 0;
	
	private static int findNextId() {
		final Object[] array = IDS.array();
		for (int i = nextId; i < array.length; i++) {
			if (array[i] == null) {
				return i;
			}
		}
		return array.length;
	}
	
	public static final BlockType get(String name) {
		synchronized (NAMES) {
			return NAMES.get(name);
		}
	}
	
	public static final BlockType get(int id) {
		synchronized (NAMES) {
			return IDS.get(id);
		}
	}
	
	private final IntConstant id = new IntConstant();
	
	public final void register() {
		if (!Photon.isRegistrationPhase()) {
			throw new IllegalStateException("Cannot register while not in a registration phase!");
		}
		synchronized (NAMES) {
			NAMES.put(getName(), this);
			if (!id.isInitialized()) {
				nextId = findNextId();
				id.init(nextId);
			}
			IDS.put(getId(), this);
		}
	}
	
	public final void registerWithForcedId(int id) {
		if (!Photon.isRegistrationPhase()) {
			throw new IllegalStateException("Cannot register while not in a registration phase!");
		}
		synchronized (NAMES) {
			NAMES.put(getName(), this);
			this.id.init(nextId);
			IDS.put(getId(), this);
		}
	}
	
	public final int getId() {
		return id.get();
	}
	
	public abstract String getName();
}
