package org.mcphoton.world;

import java.io.IOException;
import com.electronwill.collections.Bag;
import com.electronwill.streams.EasyOutputStream;

public class OverworldChunkSection extends NoSkylightChunkSection {
	
	protected final byte[] skylight;
	
	public OverworldChunkSection() {
		super();
		this.skylight = new byte[2048];
	}
	
	public OverworldChunkSection(byte[] data, byte[] emittedLight, byte[] skylight, Bag<BlockEntity> blockEntities) {
		super(data, emittedLight, blockEntities);
		this.skylight = new byte[2048];
	}
	
	@Override
	public int getBlockSkylight(int x, int y, int z) {
		final int i = y << 8 | z << 4 | x;
		final int index = i / 2;
		final byte b = skylight[index];
		if ((i & 1) == 0) // index is a power of two so blocklight is in the first 4 bits
			return (byte) (b >> 4);
		else
			return (byte) (b & 15);// we take the last 4 bits
	}
	
	@Override
	public void setBlockSkylight(int x, int y, int z, int light) {
		final int i = y << 8 | z << 4 | x;
		final int index = i / 2;
		final byte b = skylight[index];
		if ((i & 1) == 0)
			skylight[index] = (byte) ((light << 4) | (b & 15));
		else
			skylight[index] = (byte) ((light & 15) | (b & 240));// 240 is 1111_0000
	}
	
	@Override
	public void writeSkylightTo(EasyOutputStream out) throws IOException {
		out.write(skylight);
	}
	
}
