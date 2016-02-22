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
package org.mcphoton.network.clientbound.status;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.mcphoton.core.Photon;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.network.SendablePacket;
import com.electronwill.json.Json;
import com.electronwill.json.JsonWriter;

/**
 *
 * @author ElectronWill
 */
public final class ResponsePacket extends SendablePacket {
	
	public String jsonResponse;
	
	public ResponsePacket(Map<String, Object> jsonObject) throws IOException {
		JsonWriter jw = new JsonWriter(false);
		jw.write(jsonObject);
		this.jsonResponse = jw.toString();
	}
	
	public ResponsePacket(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	
	public ResponsePacket() {
		Map<String, Object> jsonObject = new HashMap<>();
		
		// == Version ==
		Map<String, Object> versionObject = new HashMap<>();
		versionObject.put("name", Photon.gameVersion());
		versionObject.put("protocol", Photon.protocolVersion());
		jsonObject.put("version", versionObject);
		
		// == Players ==
		// TODO OPTIMIZE: avoid creating maps and directly write json
		Map<String, Object> playersObject = new HashMap<>();
		playersObject.put("max", Photon.getMaxPlayers());
		playersObject.put("online", Photon.getPlayerCount());
		/*
		Map[] infosArray = new Map[onlinePlayers.size()];
		for (int i = 0; i < onlinePlayers.size(); i++) {// OPTIONAL
			OnlinePlayer p = onlinePlayers.get(i);
			Map<String, Object> playerInfos = new HashMap<>();
			playerInfos.put("name", p.name());
			playerInfos.put("id", p.uniqueId().toString());
			infosArray[i] = playerInfos;
		}
		playersObject.put("sample", infosArray);// TODO optional, make it a configurable option
		*/
		jsonObject.put("players", playersObject);
		
		// == Description ==
		ChatMessage motd = Photon.getDescription();
		jsonObject.put("description", motd.getMap());
		
		// == Custom logo ==
		String base64Logo = Photon.getLogoBase64();
		String logoString = "data:image/png;base64," + base64Logo;
		jsonObject.put("favicon", logoString);
		
		// == Finalization ==
		this.jsonResponse = Json.dump(jsonObject, false);
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		dest.writeString(jsonResponse);
	}
	
	@Override
	public int id() {
		return 0;
	}
	
	@Override
	public int maxDataSize() {
		return jsonResponse.length() * 4 + 5;// UTF-8 => max 4 bytes per char, VarInt => max 5 bytes
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + jsonResponse;
	}
}
