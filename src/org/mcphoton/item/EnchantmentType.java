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
package org.mcphoton.item;

import com.electronwill.collections.IndexMap;
import com.electronwill.concurrent.IntConstant;
import java.util.HashMap;
import java.util.Map;
import org.mcphoton.core.Photon;

/**
 * Defines a type of item's enchantment.
 *
 * @author ElectronWill
 */
public abstract class EnchantmentType {

	/**
	 * Contains all registered items by name.
	 */
	private static final Map<String, EnchantmentType> NAMES_MAP = new HashMap<>();
	/**
	 * Contains all registered items by id.
	 */
	private static final IndexMap<EnchantmentType> IDS_MAP = new IndexMap<>();
	/**
	 * The next available id.
	 */
	public static volatile int nextId = 0;

	protected static final void checkRegistrationPhase() {
		if (!Photon.isRegistrationPhase()) {
			throw new IllegalStateException("Cannot register while not in a registration phase!");
		}
	}

	/**
	 * Finds the next available id and stores it in the <code>nextId</code> field.
	 */
	private static void findNextId() {
		final Object[] array = IDS_MAP.array();
		for (int i = nextId; i < array.length; i++) {
			if (array[i] == null) {
				nextId = i;
			}
		}
		nextId = array.length;//volatile write to ensure visibility (sort of "synchronization")
	}

	/**
	 * Gets the EnchantmentType with the specified name.
	 *
	 * @param name the type's name
	 * @return the EnchantmentType with the specified name, or <code>null</code> if no
	 * enchantment is registered with this name.
	 */
	public static final EnchantmentType get(final String name) {
		return NAMES_MAP.get(name);
	}

	/**
	 * Gets the EnchantmentType with the specified id.
	 *
	 * @param id the type's id
	 * @return the EnchantmentType with the specified id, or <code>null</code> if no item
	 * is registered with this id.
	 */
	public static final EnchantmentType get(final int id) {
		return IDS_MAP.get(id);
	}

	/**
	 * Registers an item type with the next available id.
	 *
	 * @param item the EnchantmentType to register
	 * @return the id it was registered with, or -1 if it cannot be registered because the
	 * specified name is already used by another EnchantmentType.
	 */
	public static final int register(final EnchantmentType item) {
		checkRegistrationPhase();
		if (NAMES_MAP.containsKey(item.getName())) {
			return -1;
		}
		findNextId();
		item.id.init(nextId);//definitely sets the item type's id.
		IDS_MAP.put(nextId, item);
		NAMES_MAP.put(item.getName(), item);
		return nextId;
	}

	/**
	 * Registers an item type with the specified id.
	 *
	 * @param item the EnchantmentType to register
	 * @param id its unique material's id
	 * @return true if it was successfully registered, or false if it cannot be registered
	 * because the id specified is already used by another EnchantmentType.
	 */
	public static final boolean register(final EnchantmentType item, final int id) {
		checkRegistrationPhase();
		if (IDS_MAP.containsKey(id) || NAMES_MAP.containsKey(item.getName())) {
			return false;
		}
		item.id.init(id);//definitely sets the item type's id.
		IDS_MAP.put(id, item);
		NAMES_MAP.put(item.getName(), item);
		return true;
	}

	/**
	 * Constant id of the EnchantmentType. It is set during the enchantment's registration
	 * and cannot be changed after that.
	 */
	protected final IntConstant id = new IntConstant();

	/**
	 * Gets the unique name of this EnchantmentType.
	 */
	public abstract String getName();

	/**
	 * Gets the unique id of this EnchantmentType.
	 */
	public final int getId() {
		return id.get();
	}

}
