package org.mcphoton.network;

import java.nio.ByteBuffer;

public final class DataSending {
	
	private final ByteBuffer buffer;
	private final Runnable onCompletedRunnable;
	
	public DataSending(ByteBuffer buffer) {
		this.buffer = buffer;
		this.onCompletedRunnable = null;
	}
	
	public DataSending(ByteBuffer buffer, Runnable onCompletedRunnable) {
		this.buffer = buffer;
		this.onCompletedRunnable = onCompletedRunnable;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public Runnable getOnCompletedRunnable() {
		return onCompletedRunnable;
	}
	
	public void onCompleted() {
		if (onCompletedRunnable != null)
			onCompletedRunnable.run();
	}
	
}
