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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.network.SendablePacket;
import org.mcphoton.world.BlockState;
import com.electronwill.collections.ByteList;
import com.electronwill.collections.IntList;

/**
 *
 * @author ElectronWill
 */
public final class MultiBlockChangePacket extends SendablePacket {
	
	public int chunkX, chunkZ;
	/**
	 * Chunk-relative XZ position: the 4 MSB encode the X coordinate, the 4 LSB encode the Z.
	 */
	public ByteList xzPositions;
	public ByteList yPositions;
	/**
	 * Blocks ids. {@code id =  (blockTypeId << 4) | data}
	 */
	public IntList blocks;
	
	public MultiBlockChangePacket(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.xzPositions = new ByteList();
		this.yPositions = new ByteList();
		this.blocks = new IntList();
	}
	
	public MultiBlockChangePacket(int chunkX, int chunkZ, ByteList xzPositions, ByteList yPositions, IntList blocks) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.xzPositions = xzPositions;
		this.yPositions = yPositions;
		this.blocks = blocks;
	}
	
	public void addChange(BlockState bs) {
		byte xzPos = (byte) (((bs.x / chunkX) << 4) | (bs.z / chunkZ));
		byte yPos = (byte) bs.y;
		addChange(xzPos, yPos, bs.type.getId());
	}
	
	public void addChange(byte xzPos, byte yPos, int rawId) {
		xzPositions.add(xzPos);
		yPositions.add(yPos);
		blocks.add(rawId);
	}
	
	public void addChange(int absX, int absY, int absZ, int rawId) {
		byte xzPos = (byte) (((absX / chunkX) << 4) | (absZ / chunkZ));
		byte yPos = (byte) absY;
		xzPositions.add(xzPos);
		yPositions.add(yPos);
		blocks.add(rawId);
	}
	
	@Override
	public int maxDataSize() {
		return blocks.size() * 6 + 13;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		final int count = blocks.size();
		dest.writeInt(chunkX);
		dest.writeInt(chunkZ);
		dest.writeVarInt(count);
		dest.write(xzPositions.getValues(), 0, count);
		dest.write(yPositions.getValues(), 0, count);
		for (int i = 0; i < count; i++) {
			int raw = blocks.get(i);
			dest.writeInt(raw);
		}
	}
	
	@Override
	public int id() {
		return 0x22;
	}
	
}
