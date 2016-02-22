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
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import java.util.Optional;
import org.mcphoton.network.SendablePacket;
import com.electronwill.collections.OpenList;

/**
 * Updates a rectangular area on a map item.
 *
 * @author ElectronWill
 */
public final class MapsPacket extends SendablePacket {

	/**
	 * Number of columns updated.
	 */
	public byte colCount;
	/**
	 * Non-empty only if there are updated columns.
	 */
	public Optional<byte[]> data;
	public OpenList<MapIcon> mapIcons;
	/**
	 * The item damage of the map. That's its Map ID too.
	 */
	public int mapId;
	public byte mapScale;

	public Optional<Byte> rows;
	public Optional<Byte> xOffset, zOffset;

	public MapsPacket(int mapId, byte mapScale, byte colCount, OpenList<MapIcon> mapIcons) {
		this.mapId = mapId;
		this.mapScale = mapScale;
		this.colCount = colCount;
		this.mapIcons = mapIcons;
		this.xOffset = Optional.empty();
		this.zOffset = Optional.empty();
		this.rows = Optional.empty();
		this.data = Optional.empty();
	}

	public MapsPacket(int mapId, byte mapScale, OpenList<MapIcon> mapIcons, byte xOffset, byte zOffset, byte[] data) {
		this.mapId = mapId;
		this.mapScale = mapScale;
		this.mapIcons = mapIcons;
		this.xOffset = Optional.of(xOffset);
		this.zOffset = Optional.of(zOffset);
		this.data = Optional.of(data);
	}

	public MapsPacket(int mapId, byte mapScale, OpenList<MapIcon> mapIcons, int xOffset, int zOffset, byte[] data) {
		this(mapId, mapScale, mapIcons, (byte) xOffset, (byte) zOffset, data);
	}

	public MapsPacket(int mapId, byte mapScale, OpenList<MapIcon> mapIcons) {
		this.mapId = mapId;
		this.mapScale = mapScale;
		this.mapIcons = mapIcons;
		this.xOffset = Optional.empty();
		this.zOffset = Optional.empty();
		this.data = Optional.empty();
	}

	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeVarInt(mapId);
		dest.write(mapScale);
		dest.writeVarInt(mapIcons.size());
		for (MapIcon icon : mapIcons) {
			dest.write(icon.compactDirectionAndType());
			dest.write(icon.x);
			dest.write(icon.z);
		}
		dest.write(colCount);
		if (!data.isPresent()) {
			if (colCount > 0) {
				throw new IllegalArgumentException("Invalid MapsPacket fields: if colCount > 0 then there MUST be some data!");
			}
			return;
		}
		if (colCount == 0) {
			throw new IllegalArgumentException("Invalid MapsPacket fields: if there is any data then colCount MUST BE greater than 0!");
		}
		dest.write(rows.get());
		dest.write(xOffset.get());
		dest.write(zOffset.get());
		dest.writeVarInt(data.get().length);
		dest.write(data.get());
	}

	@Override
	public int id() {
		return 0x34;
	}

	@Override
	public int maxDataSize() {
		return data.isPresent() ? 20 + mapIcons.size() * 3 + data.get().length : 12 + mapIcons.size() * 3;
	}

	public static final class MapIcon {

		public byte direction;
		public byte type;
		public byte x;
		public byte z;

		public byte compactDirectionAndType() {
			return (byte) ((direction << 4) | type);
		}
	}

}
