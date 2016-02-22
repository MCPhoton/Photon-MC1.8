package org.mcphoton.security;

import java.util.concurrent.atomic.AtomicInteger;
import org.mcphoton.world.World;

/**
 * A permit that grants some rights to its owner.
 * 
 * @author ElectronWill
 * 		
 */
public abstract class AccessPermit {
	
	private static final AtomicInteger remainingRootPermits = new AtomicInteger(4);
	
	public AccessPermit() throws AccessDeniedException {
		if (remainingRootPermits.getAndDecrement() == 0)
			throw new AccessDeniedException("All root permits have already been given.");
	}
	
	public AccessPermit(final AccessPermit creatorPermit) throws AccessDeniedException {
		if (!mayCreatePermit())
			throw new AccessDeniedException("Creator is not allowed to create an AccessPermit.");
	}
	
	public abstract boolean mayCreatePermit();
	
	public abstract boolean mayGetDirectChunkAccess();
	
	public abstract boolean maySetBlockId(World w, int x, int y, int z, int id);
	
	public abstract boolean maySetBlockEmittedLight(World w, int x, int y, int z, int level);
	
	public abstract boolean maySetBlockSkylight(World w, int x, int y, int z, int level);
	
	public abstract boolean maySetPermission(String permission, boolean value, Object target);
	
	public abstract boolean maySetPermissionDefault(String permission, boolean defaultValue);
	
	public abstract boolean maySetPermissionManager(PermissionsManager manager);
	
	public abstract boolean mayUnsetPermission(String permission, Object target);
	
}
