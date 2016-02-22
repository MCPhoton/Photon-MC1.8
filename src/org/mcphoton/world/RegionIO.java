package org.mcphoton.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.electronwill.collections.SimpleBag;
import com.electronwill.streams.EasyInputStream;
import com.electronwill.streams.EasyInputWrapper;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.EasyOutputWrapper;

/**
 * Utility class for reading and writing world's regions.
 * <h2>Terminology</h2>
 * <p>
 * 1 section = 16x16x16 blocks<br>
 * 1 chunk = 16 sections = 16x256x16 blocks<br>
 * 1 region = 32x32 chunks = 512x256x512 blocks<br>
 * </p>
 * 
 * @author ElectronWill
 * 		
 */
public final class RegionIO {
	
	public static final int CURRENT_REGION_VERSION = 0;
	
	public static Chunk[] readRegion(World w, int rx, int rz) throws FileNotFoundException, IOException {
		File file = getRegionFile(w, rx, rz);
		return readRegion(w, file);
	}
	
	public static Chunk[] readRegion(World w, File file) throws FileNotFoundException, IOException {
		try (EasyInputWrapper in = new EasyInputWrapper(new FileInputStream(file))) {
			return readRegion(w, in);
		}
	}
	
	public static Chunk[] readRegion(World w, EasyInputStream in) throws IOException {
		final int version = in.readVarInt();
		if (version != CURRENT_REGION_VERSION)
			throw new IOException("Unknown region version " + version);
		final boolean skylight = in.readBoolean();
		final int rx = in.readInt(), rz = in.readInt();
		Chunk[] chunks = new Chunk[1024];
		for (int i = 0; i < 1024; i++) {// read each chunk
			int x = rx * 32 + i / 32, z = rz * 32 + i & 32;
			boolean bool = in.readBoolean();
			if (bool == false) {// no chunk
				chunks[i] = null;
				continue;
			}
			short mask = in.readShort();
			int length = in.readVarInt();
			ChunkSection[] sections = new ChunkSection[16];
			for (int s = 0; s < 16; s++) {// read each section
				if ((mask & (1 << s)) == 0) {// no section
					sections[s] = null;
					continue;
				}
				byte[] blockIds = new byte[8192];
				byte[] blockEmittedLight = new byte[2048];
				byte[] blockSkyLight = skylight ? new byte[2048] : null;
				in.read(blockIds);
				in.read(blockEmittedLight);
				if (skylight)
					in.read(blockEmittedLight);
				if (skylight)
					sections[s] = new OverworldChunkSection(blockIds, blockEmittedLight, blockSkyLight, new SimpleBag<>());
				else
					sections[s] = new NoSkylightChunkSection(blockIds, blockEmittedLight, new SimpleBag<>());
			}
			byte[] biomeIds = new byte[256];
			in.read(biomeIds);
			if (skylight)
				chunks[i] = new OverworldChunk(w, x, z, sections, biomeIds);
			else
				chunks[i] = new NoSkylightChunk(w, x, z, sections, biomeIds);
		}
		return chunks;
	}
	
	public static void writeRegion(Chunk[] chunks, World w, int rx, int rz, boolean skylight) throws IOException {
		File file = getRegionFile(w, rx, rz);
		writeRegion(chunks, rx, rz, skylight, file);
	}
	
	public static File getRegionFile(World w, int rx, int rz) {
		return new File(w.getDirectory(), getRegionFileName(rx, rz));
	}
	
	public static String getRegionFileName(int rx, int rz) {
		return rx + "." + rz + ".region";
	}
	
	public static void writeRegion(Chunk[] chunks, int rx, int rz, boolean skylight, File file) throws IOException {
		try (EasyOutputWrapper out = new EasyOutputWrapper(new FileOutputStream(file))) {
			writeRegion(chunks, rx, rz, skylight, out);
		}
	}
	
	public static void writeRegion(Chunk[] chunks, int rx, int rz, boolean skylight, EasyOutputStream out) throws IOException {
		out.writeVarInt(CURRENT_REGION_VERSION);
		out.writeBoolean(skylight);
		out.writeInt(rx);
		out.writeInt(rz);
		for (Chunk chunk : chunks) {
			if (chunk == null) {
				out.writeBoolean(false);
			} else {
				chunk.writeTo(out, true);
			}
		}
	}
	
}
