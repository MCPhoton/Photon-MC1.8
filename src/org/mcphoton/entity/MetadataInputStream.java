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
import org.mcphoton.item.EnchantmentType;
import org.mcphoton.item.ItemStack;
import org.mcphoton.item.ItemType;
import org.mcphoton.util.Location;
import org.mcphoton.util.Rotation;
import com.electronwill.collections.IndexMap;
import com.electronwill.nbt.NBTParser;
import com.electronwill.nbt.TagCompound;
import com.electronwill.streams.EasyInputStream;

/**
 * An outputstream for reading entity's metadata.
 *
 * @see http://wiki.vg/Entities#Entity_Metadata_Format
 * @author ElectronWill
 */
public final class MetadataInputStream {
	
	private final EasyInputStream in;
	private final IndexMap<Object> data = new IndexMap<>();
	
	public MetadataInputStream(EasyInputStream in) {
		this.in = in;
	}
	
	public void read() throws IOException {
		while (in.available() > 0) {
			final byte identifier = in.readByte();
			if (identifier == 127) {
				break;
			}
			int index = identifier & 0x1F;
			int type = identifier >> 5;
			final Object value;
			switch (type) {
				case 0:
					value = in.readByte();
					break;
				case 1:
					value = in.readShort();
					break;
				case 2:
					value = in.readInt();
					break;
				case 3:
					value = in.readFloat();
					break;
				case 4:
					value = in.readString();
					break;
				case 5:
					short typeId = in.readShort();
					byte count = in.readByte();
					short damage = in.readShort();
					boolean hasNbt = in.readBoolean();
					
					ItemType item = null;// TODO ItemType.getItem(typeId);
					ItemStack stack = null;// TODO item.createStack(count, damage);
					
					if (hasNbt) {
						NBTParser parser = new NBTParser(in);
						TagCompound nbt = parser.parse();
						List<TagCompound> compounds = (List) nbt.get("ench");
						for (TagCompound tc : compounds) {
							short enchantId = (short) tc.get("id");
							short enchantLevel = (short) tc.get("lvl");
							AppliedEnchantment enchant = new AppliedEnchantment(stack, EnchantmentType.get(enchantId), enchantLevel);
							stack.addEnchantment(enchant);
						}
					}
					value = stack;
					break;
				case 6:
					int x = in.readInt(), y = in.readInt(), z = in.readInt();
					value = new Location(null, x, y, z);
					break;
				case 7:
					float pitch = in.readFloat(), yaw = in.readFloat(), roll = in.readFloat();
					value = new Rotation(pitch, yaw, roll);
					break;
				default:
					// Invalid type!!
					value = null;
					break;
			}
			data.put(index, value);
		}
	}
	
	public Object get(int index) {
		return data.get(index);
	}
	
	public byte getByte(int index) {
		return (byte) data.get(index);
	}
	
	public short getShort(int index) {
		return (short) data.get(index);
	}
	
	public int getInt(int index) {
		return (int) data.get(index);
	}
	
	public float getFloat(int index) {
		return (float) data.get(index);
	}
	
	public String getString(int index) {
		return (String) data.get(index);
	}
	
	public Location getLocation(int index) {
		return (Location) data.get(index);
	}
	
	public Rotation getRotation(int index) {
		return (Rotation) data.get(index);
	}
	
	public ItemStack getSlot(int index) {
		return (ItemStack) data.get(index);
	}
	
}
