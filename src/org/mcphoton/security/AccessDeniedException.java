package org.mcphoton.security;

/**
 * Thrown when something is denied by an AccessPermit.
 * 
 * @author ElectronWill
 * 		
 */
public final class AccessDeniedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public AccessDeniedException() {}
	
	public AccessDeniedException(String message) {
		super(message);
		
	}
	
	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause);
		
	}
	
	public AccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}
	
	public AccessDeniedException(Throwable cause) {
		super(cause);
		
	}
	
}
