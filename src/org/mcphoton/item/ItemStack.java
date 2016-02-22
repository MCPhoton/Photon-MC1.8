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
package org.mcphoton.item;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.mcphoton.entity.Entity;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.world.BlockInfos;
import org.mcphoton.world.InteractButton;
import com.electronwill.collections.OpenList;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 * A stack of items in an inventory, or on the ground. Contains zero, one or several items of the same type. An subclass
 * of ItemStack can store individual data and additional behavior.
 *
 * @author ElectronWill
 */
public abstract class ItemStack implements Writeable {
	
	private int amount;
	private short damage;
	protected volatile Inventory inventory;
	private final List<AppliedEnchantment> enchantments = new OpenList<>();
	
	protected ItemStack() {
		this(1, (short) 0, null);
	}
	
	protected ItemStack(int amount, short damage) {
		this(amount, damage, null);
	}
	
	protected ItemStack(int amount, short damage, Inventory inventory) {
		this.amount = amount;
		this.damage = damage;
		this.inventory = inventory;
	}
	
	/**
	 * Gets the amount of items in this stack.
	 */
	public synchronized int getAmount() {
		return amount;
	}
	
	public final synchronized void setAmount(int amount) {
		this.amount = amount;
	}
	
	public final synchronized void increaseAmount(int increase) {
		amount += increase;
	}
	
	public final synchronized void decreaseAmount(int decrease) {
		amount -= decrease;
	}
	
	public final List<AppliedEnchantment> getEnchantments() {
		return Collections.unmodifiableList(enchantments);
	}
	
	public final AppliedEnchantment[] getEnchantmentsArray() {
		AppliedEnchantment[] arr = new AppliedEnchantment[enchantments.size()];
		return enchantments.toArray(arr);
	}
	
	public final synchronized void addEnchantment(AppliedEnchantment enchant) {
		enchantments.add(enchant);
	}
	
	public final synchronized void addEnchantment(EnchantmentType enchant, int level) {
		AppliedEnchantment ae = new AppliedEnchantment(this, enchant, level);
		addEnchantment(ae);
	}
	
	public final synchronized int getEnchantmentLevel(EnchantmentType enchant) {
		for (int i = 0; i < enchantments.size(); i++) {
			AppliedEnchantment ae = enchantments.get(i);
			if (ae.getType() == enchant) {
				return ae.getLevel();
			}
		}
		return -1;
	}
	
	public final synchronized AppliedEnchantment getEnchantment(EnchantmentType enchant) {
		for (int i = 0; i < enchantments.size(); i++) {
			AppliedEnchantment ae = enchantments.get(i);
			if (ae.getType() == enchant) {
				return ae;
			}
		}
		return null;
	}
	
	public final synchronized void removeEnchantment(AppliedEnchantment enchant) {
		enchantments.remove(enchant);
	}
	
	public final synchronized void removeEnchantment(EnchantmentType enchant) {
		for (int i = 0; i < enchantments.size(); i++) {
			AppliedEnchantment ae = enchantments.get(i);
			if (ae.getType() == enchant) {
				enchantments.remove(i);
			}
		}
	}
	
	/**
	 * Gets the damage of this stack.
	 */
	public final synchronized short getDamage() {
		return damage;
	}
	
	public final synchronized void setDamage(short damage) {
		this.damage = damage;
	}
	
	public final synchronized void increaseDamage(short increase) {
		this.damage += increase;
	}
	
	public final synchronized void decreaseDamage(short decrease) {
		this.damage -= decrease;
	}
	
	/**
	 * Gets the inventory this stack is stored in.
	 */
	public final Inventory getInventory() {
		return inventory;
	}
	
	public final void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * Gets the type of this stack.
	 */
	public abstract ItemType getType();
	
	/**
	 * Checks if this stack is empty.
	 */
	public final synchronized boolean isEmpty() {
		return amount == 0;
	}
	
	/**
	 * Checks if this ItemStack is stored in an inventory.
	 *
	 * @return true if it is stored in an inventory, false if it is on the ground.
	 */
	public final boolean isStored() {
		return inventory != null;
	}
	
	/**
	 * Writes the data of this ItemStack to the given EasyOutput, in the "slot" protocol format.
	 *
	 * @param out the EasyOutput
	 * @throws IOException
	 */
	@Override
	public void writeTo(EasyOutputStream out) throws IOException {
		// TODO
	}
	
	/**
	 * Called when this item, or an item in this stack, is broken.
	 */
	public void onBroken() {}
	
	/**
	 * Called when the amount of items in this stack changes.
	 *
	 * @param oldAmount the old amount
	 * @param newAmount the new amount
	 * @return the actual new amount
	 */
	public int onAmountChange(int oldAmount, int newAmount) {
		return newAmount;
	}
	
	/**
	 * Called when a player picks this stack up.
	 *
	 * @param picker the player that picked up the item
	 * @param block the block it was on before being picked up
	 */
	public void onPickup(OnlinePlayer picker, BlockInfos block) {}
	
	/**
	 * Called when a player drops this stack.
	 *
	 * @param dropper the player that dropped the item
	 * @param target the block it was dropped on
	 */
	public void onDrop(OnlinePlayer dropper, BlockInfos target) {}
	
	/**
	 * Called when a player uses this item, by right-clicking or left-clicking on an entity.
	 *
	 * @param user the player that uses this
	 * @param button the button used to interact with the block.
	 */
	public void onUse(OnlinePlayer user, Entity target, InteractButton button) {}
	
	/**
	 * Called when a player uses this item, by right-clicking or left-clicking on a block.
	 *
	 * @param user the player that uses this
	 * @param button the button used to interact with the block.
	 */
	public void onUse(OnlinePlayer user, BlockInfos target, InteractButton button) {}
	
}
