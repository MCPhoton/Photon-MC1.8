package org.mcphoton.event.impl;

import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.CancellableEvent;

public class PlayerMoveEvent extends CancellableEvent {
	
	private final OnlinePlayer player;
	private double newX, newY, newZ;
	private float newYaw, newPitch;
	
	public PlayerMoveEvent(OnlinePlayer player, double newX, double newY, double newZ, float newYaw, float newPitch) {
		this.player = player;
		this.newX = newX;
		this.newY = newY;
		this.newZ = newZ;
		this.newYaw = newYaw;
		this.newPitch = newPitch;
	}
	
	public PlayerMoveEvent(OnlinePlayer player, double newX, double newY, double newZ) {
		this(player, newX, newY, newZ, player.getYaw(), player.getPitch());
	}
	
	public PlayerMoveEvent(OnlinePlayer player, float newYaw, float newPitch) {
		this(player, player.getX(), player.getY(), player.getZ(), newYaw, newPitch);
	}
	
	public float getCurrentPitch() {
		return player.getPitch();
	}
	
	public float getCurrentYaw() {
		return player.getYaw();
	}
	
	public double getCurrentX() {
		return player.getX();
	}
	
	public double getCurrentY() {
		return player.getY();
	}
	
	public double getCurrentZ() {
		return player.getZ();
	}
	
	public float getNewPitch() {
		return newPitch;
	}
	
	public double getNewX() {
		return newX;
	}
	
	public double getNewY() {
		return newY;
	}
	
	public OnlinePlayer getPlayer() {
		return player;
	}
	
	public double getNewZ() {
		return newZ;
	}
	
	public void setNewZ(double newZ) {
		this.newZ = newZ;
	}
	
	public float getNewYaw() {
		return newYaw;
	}
	
	public void setNewYaw(float newYaw) {
		this.newYaw = newYaw;
	}
	
	public void setNewX(double newX) {
		this.newX = newX;
	}
	
	public void setNewY(double newY) {
		this.newY = newY;
	}
	
	public void setNewPitch(float newPitch) {
		this.newPitch = newPitch;
	}
	
}
