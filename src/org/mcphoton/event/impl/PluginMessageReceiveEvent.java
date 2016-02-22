package org.mcphoton.event.impl;

import org.mcphoton.event.PhotonEvent;
import com.electronwill.streams.EasyInputStream;

public class PluginMessageReceiveEvent extends PhotonEvent {
	
	private String channel;
	private EasyInputStream message;
	
	public PluginMessageReceiveEvent(String channel, EasyInputStream message) {
		this.channel = channel;
		this.message = message;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public EasyInputStream getMessage() {
		return message;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public void setMessage(EasyInputStream message) {
		this.message = message;
	}
	
}
