/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import org.mcphoton.core.Photon;
import com.electronwill.collections.Bag;
import com.electronwill.collections.SimpleBag;

/**
 * Utility class for managing events and their listeners.
 *
 * @author ElectronWill
 */
public final class Events {
	
	/**
	 * Contains listeners associated to events classes, by ListenOrder.
	 */
	private static final HashMap<Class<? extends PhotonEvent>, EnumMap<ListenOrder, Bag<EventHandler>>> REGISTRATIONS = new HashMap<>();
	
	public static void registerAll(Object listener) {
		Method[] publicMethods = listener.getClass().getMethods();
		for (Method method : publicMethods) {
			Listen listenAnnotation = method.getAnnotation(Listen.class);
			
			if (listenAnnotation == null)
				continue;
				
			int pCount = method.getParameterCount();
			if (pCount != 1) {
				throw new IllegalArgumentException(
						"Method " + method.toGenericString() + " must have 1 parameter, but it has " + pCount + " parameters");
			}
			
			Class<?> pClass = method.getParameterTypes()[0];
			if (!PhotonEvent.class.isAssignableFrom(pClass)) {
				throw new IllegalArgumentException("Method " + method.toGenericString() + " must take a PhotonEvent as parameter");
			}
			
			ListenOrder order = listenAnnotation.order();
			boolean ignoreCancelled = listenAnnotation.ignoreCancelled();
			if (ignoreCancelled && CancellableEvent.class.isAssignableFrom(pClass)) {
				Class<? extends CancellableEvent> eventClass = (Class<? extends CancellableEvent>) pClass;
				EventHandler<CancellableEvent> handler = (CancellableEvent e) -> {
					try {
						if (!e.isCancelled()) {
							method.invoke(listener, e);
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						Photon.log.errorFrom(ex, "EventHandler");
					}
				};
				register(eventClass, handler, order);
			} else {
				Class<? extends PhotonEvent> eventClass = (Class<? extends PhotonEvent>) pClass;
				EventHandler<PhotonEvent> handler = (PhotonEvent e) -> {
					try {
						method.invoke(listener, e);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						Photon.log.errorFrom(ex, "EventHandler");
					}
				};
				register(eventClass, handler, order);
			}
		}
	}
	
	public static synchronized <E extends PhotonEvent> void register(Class<E> eventClass, EventHandler<? super E> eventHandler,
			ListenOrder listenOrder) {
		EnumMap<ListenOrder, Bag<EventHandler>> handlersMap = REGISTRATIONS.get(eventClass);
		if (handlersMap == null) {
			handlersMap = new EnumMap(ListenOrder.class);
			REGISTRATIONS.put(eventClass, handlersMap);
		}
		Bag<EventHandler> handlersBag = handlersMap.get(listenOrder);
		if (handlersBag == null) {
			handlersBag = new SimpleBag<>();
			handlersMap.put(listenOrder, handlersBag);
		}
		handlersBag.add(eventHandler);
	}
	
	public static synchronized <E extends PhotonEvent> void unregister(Class<E> eventClass, EventHandler<? super E> eventHandler,
			ListenOrder listenOrder) {
		EnumMap<ListenOrder, Bag<EventHandler>> handlersMap = REGISTRATIONS.get(eventClass);
		if (handlersMap == null)
			return;
		Bag<EventHandler> handlersBag = handlersMap.get(listenOrder);
		if (handlersBag == null)
			return;
		handlersBag.remove(eventHandler);
	}
	
	public static synchronized <E extends PhotonEvent> void unregister(Class<E> eventClass, EventHandler<? super E> eventHandler) {
		EnumMap<ListenOrder, Bag<EventHandler>> handlersMap = REGISTRATIONS.get(eventClass);
		if (handlersMap == null)
			return;
		for (ListenOrder order : ListenOrder.values()) {
			Bag<EventHandler> handlersBag = handlersMap.get(order);
			if (handlersBag == null)
				return;
			handlersBag.remove(eventHandler);
		}
	}
	
	public static synchronized void notifyListeners(PhotonEvent event) {
		Class<? extends PhotonEvent> eventClass = event.getClass();
		EnumMap<ListenOrder, Bag<EventHandler>> handlersMap = REGISTRATIONS.get(eventClass);
		if (handlersMap == null)
			return;
		for (ListenOrder order : ListenOrder.values()) {
			Bag<EventHandler> handlersBag = handlersMap.get(order);
			if (handlersBag == null)
				continue;
			for (EventHandler handler : handlersBag) {
				try {
					handler.handle(event);
				} catch (Throwable t) {
					Photon.log.errorFrom(t, "EventHandler", "An error occured while handling event on this handler");
				}
			}
		}
	}
	
	private Events() {}
	
}
