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
package org.mcphoton.entity;

import java.io.IOException;
import java.util.List;
import org.mcphoton.item.AppliedEnchantment;
import org.mcphoton.item.ItemStack;
import com.electronwill.collections.OpenList;
import com.electronwill.nbt.NBTWriter;
import com.electronwill.nbt.TagCompound;
import com.electronwill.streams.EasyOutputStream;

/**
 * An outputstream for writing entity's metadata.
 *
 * @see http://wiki.vg/Entities#Entity_Metadata_Format
 * @author ElectronWill
 */
public final class MetadataOutputStream {
	
	private final EasyOutputStream out;
	
	public MetadataOutputStream(EasyOutputStream out) {
		this.out = out;
	}
	
	public void writeThing(int index, Object thing) throws IOException {
		if (thing instanceof Byte) {
			writeByte(index, (byte) thing);
		} else if (thing instanceof Short) {
			writeShort(index, (short) thing);
		} else if (thing instanceof Integer) {
			writeInt(index, (int) thing);
		} else if (thing instanceof Float) {
			writeFloat(index, (float) thing);
		} else if (thing instanceof String) {
			writeString(index, (String) thing);
		} else if (thing instanceof ItemStack) {
			writeItemStack(index, (ItemStack) thing);
		}
	}
	
	private void writeIdentifier(int index, int type) throws IOException {
		int value = (type << 5 | index & 0x1F) & 0xFF;
		out.write(value);
	}
	
	public void writeByte(int index, byte b) throws IOException {
		writeIdentifier(index, 0);
		out.write(b);
	}
	
	public void writeByte(int index, int b) throws IOException {
		writeByte(index, (byte) b);
	}
	
	public void writeShort(int index, short s) throws IOException {
		writeIdentifier(index, 1);
		out.writeShort(s);
	}
	
	public void writeShort(int index, int s) throws IOException {
		writeShort(index, (short) s);
	}
	
	public void writeInt(int index, int i) throws IOException {
		writeIdentifier(index, 2);
		out.writeInt(i);
	}
	
	public void writeFloat(int index, float f) throws IOException {
		writeIdentifier(index, 3);
		out.writeFloat(f);
	}
	
	public void writeString(int index, String str) throws IOException {
		writeIdentifier(index, 4);
		out.writeString(str);
	}
	
	public void writeEmptySlot(int index) throws IOException {
		writeIdentifier(index, 5);
		out.writeShort(-1);
	}
	
	public void writeInts(int index, int i1, int i2, int i3) throws IOException {
		writeIdentifier(index, 6);
		out.writeInt(i1);
		out.writeInt(i2);
		out.writeInt(i3);
	}
	
	public void writeFloats(int index, float f1, float f2, float f3) throws IOException {
		writeIdentifier(index, 6);
		out.writeFloat(f1);
		out.writeFloat(f2);
		out.writeFloat(f3);
	}
	
	public void writeItemStack(int index, ItemStack item) throws IOException {
		writeItemStack(index, item.getType().getId(), item.getAmount(), item.getDamage(), item.getEnchantmentsArray());
	}
	
	public void writeItemStack(int index, int itemTypeId, int itemCount, int itemDamage, AppliedEnchantment... enchantments)
			throws IOException {
		writeIdentifier(index, 5);
		out.writeShort(itemTypeId);
		out.write(itemCount);// byte
		out.writeShort(itemDamage);
		if (enchantments == null || enchantments.length == 0) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			TagCompound globalCompound = new TagCompound();
			List<TagCompound> enchantsList = new OpenList<>();
			for (AppliedEnchantment enchant : enchantments) {
				TagCompound enchantCompound = new TagCompound();
				enchantCompound.put("id", enchant.getType().getId());
				enchantCompound.put("lvl", enchant.getLevel());
				enchantsList.add(enchantCompound);
			}
			globalCompound.put("ench", enchantsList);
			NBTWriter writer = new NBTWriter(out);
			writer.write(globalCompound);
			writer.flush();// don't close it because it would close out too!
		}
	}
	
	public void writeEnd(int index) throws IOException {
		out.write(127);
	}
	
}
