package org.mcphoton.messaging;

import java.util.Map;

public class SelectorChatMessage extends ChatMessage {
	
	public SelectorChatMessage(Map<String, Object> map) {
		super(map);
	}
	
	public SelectorChatMessage(String selector) {
		map.put("selector", selector);
	}
	
	public String getSelector() {
		return (String) map.get("selector");
	}
	
	public void setSelector(String selector) {
		map.put("selector", selector);
	}
	
}
