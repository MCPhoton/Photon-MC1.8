package org.mcphoton.event;

@FunctionalInterface
public interface EventHandler<E extends PhotonEvent> {
	
	void handle(E event);
	
}
