package org.mcphoton.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods that listen to events.
 * 
 * @author ElectronWill
 * 		
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listen {
	ListenOrder order() default ListenOrder.NORMAL;
	
	boolean ignoreCancelled() default true;
}
