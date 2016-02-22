package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.collections.Bag;
import com.electronwill.collections.SimpleBag;
import com.electronwill.streams.EasyOutputStream;

public class NoSkylightChunkSection extends ChunkSection {
	
	protected final byte[] ids;
	protected final byte[] emittedLight;
	protected final Bag<BlockEntity> blockEntities;
	
	public NoSkylightChunkSection() {
		ids = new byte[8192];
		emittedLight = new byte[2048];
		blockEntities = new SimpleBag<>();
	}
	
	public NoSkylightChunkSection(byte[] ids, byte[] emittedLight, Bag<BlockEntity> blockEntities) {
		this.ids = ids;
		this.emittedLight = emittedLight;
		this.blockEntities = blockEntities;
	}
	
	@Override
	public int getBlockId(int x, int y, int z) {
		int index = (y << 8 | z << 4 | x) * 2;
		return ids[index];
	}
	
	@Override
	public void setBlockId(int x, int y, int z, int id) {
		int index = (y << 8 | z << 4 | x) * 2;
		final byte b1 = (byte) (id >> 8), b0 = (byte) (id & 0xff);
		ids[index] = b0;
		ids[index + 1] = b1;
	}
	
	@Override
	public int getBlockEmittedLight(int x, int y, int z) {
		final int i = y << 8 | z << 4 | x;
		final int index = i / 2;
		final byte b = emittedLight[index];
		if ((i & 1) == 0) // index is a power of two so blocklight is in the first 4 bits
			return (byte) (b >> 4);
		else
			return (byte) (b & 15);// we take the last 4 bits
	}
	
	@Override
	public void setBlockEmittedLight(int x, int y, int z, int light) {
		final int i = y << 8 | z << 4 | x;
		final int index = i / 2;
		final byte b = emittedLight[index];
		if ((i & 1) == 0)
			emittedLight[index] = (byte) ((light << 4) | (b & 15));
		else
			emittedLight[index] = (byte) ((light & 15) | (b & 240));// 240 is 1111_0000
	}
	
	/**
	 * Returns 0.
	 */
	@Override
	public int getBlockSkylight(int x, int y, int z) {
		return 0;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void setBlockSkylight(int x, int y, int z, int light) {}
	
	@Override
	public BlockEntity getBlockEntity(int x, int y, int z) {
		for (int i = 0; i < blockEntities.size(); i++) {
			BlockEntity be = blockEntities.get(i);
			if (be.y == y && be.x / 16 == x && be.z / 16 == z)
				return be;
		}
		return null;
	}
	
	@Override
	public void putBlockEntity(BlockEntity blockEntity) {
		for (int i = 0; i < blockEntities.size(); i++) {
			BlockEntity be = blockEntities.get(i);
			if (be.x == blockEntity.x && be.y == blockEntity.y && be.z == blockEntity.z) {
				blockEntities.remove(i);
				break;
			}
		}
		blockEntities.add(blockEntity);
	}
	
	@Override
	public void removeBlockEntity(int x, int y, int z) {
		for (int i = 0; i < blockEntities.size(); i++) {
			BlockEntity be = blockEntities.get(i);
			if (be.y == y && be.x / 16 == x && be.z / 16 == z) {
				blockEntities.remove(i);
				return;
			}
		}
	}
	
	@Override
	public BlockState captureBlockState(World w, int x, int y, int z) {
		int id = getBlockId(x, y, z);
		BlockType type = BlockType.get(id);
		BlockEntity entity = getBlockEntity(x, y, z);
		return new BlockState(w, x * 16, y, z * 16, type, entity);
	}
	
	@Override
	public void writeBlocksIdsTo(EasyOutputStream out) throws IOException {
		out.write(ids);
	}
	
	@Override
	public void writeEmittedLightTo(EasyOutputStream out) throws IOException {
		out.write(emittedLight);
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void writeSkylightTo(EasyOutputStream out) throws IOException {}
	
}
