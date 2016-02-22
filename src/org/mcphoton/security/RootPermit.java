package org.mcphoton.security;

import org.mcphoton.world.World;

public final class RootPermit extends AccessPermit {
	
	public RootPermit() throws AccessDeniedException {
		super();
	}
	
	@Override
	public boolean mayCreatePermit() {
		return true;
	}
	
	@Override
	public boolean mayGetDirectChunkAccess() {
		return true;
	}
	
	@Override
	public boolean maySetBlockId(World w, int x, int y, int z, int id) {
		return true;
	}
	
	@Override
	public boolean maySetPermission(String permission, boolean value, Object target) {
		return true;
	}
	
	@Override
	public boolean maySetPermissionDefault(String permission, boolean defaultValue) {
		return true;
	}
	
	@Override
	public boolean maySetPermissionManager(PermissionsManager manager) {
		return true;
	}
	
	@Override
	public boolean mayUnsetPermission(String permission, Object target) {
		return true;
	}
	
	@Override
	public boolean maySetBlockEmittedLight(World w, int x, int y, int z, int level) {
		return true;
	}
	
	@Override
	public boolean maySetBlockSkylight(World w, int x, int y, int z, int level) {
		return true;
	}
	
}
