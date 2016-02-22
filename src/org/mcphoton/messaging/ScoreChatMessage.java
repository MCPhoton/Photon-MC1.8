package org.mcphoton.messaging;

import java.util.HashMap;
import java.util.Map;

public class ScoreChatMessage extends ChatMessage {
	
	public ScoreChatMessage(Map<String, Object> map) {
		super(map);
	}
	
	public ScoreChatMessage(String playerName, String objectiveName) {
		HashMap<String, String> map = new HashMap<>();
		map.put("name", playerName);
		map.put("objective", objectiveName);
		this.map.put("score", map);
	}
	
	public String getObjectiveName() {
		Map<String, String> m = (Map) map.get("score");
		return m.get("objective");
	}
	
	public String getPlayerName() {
		Map<String, String> m = (Map) map.get("score");
		return m.get("name");
	}
	
	public void setObjectiveName(String name) {
		Map<String, String> m = (Map) map.get("score");
		m.put("objective", name);
	}
	
	public void setPlayerName(String name) {
		Map<String, String> m = (Map) map.get("score");
		m.put("name", name);
	}
	
}
