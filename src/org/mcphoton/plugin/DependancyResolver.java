/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package org.mcphoton.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.electronwill.collections.OpenList;

/**
 *
 * @author ElectronWill
 */
@SuppressWarnings("unchecked")
public final class DependancyResolver<T> {
	
	private final Map<T, List<T>> map = new HashMap<>();
	
	public void put(T thing, T... dependancies) {
		put(thing, new OpenList<>(dependancies));
	}
	
	public void put(T thing, List<T> dependancies) {
		map.put(thing, dependancies);
	}
	
	public void remove(T thing) {
		map.remove(thing);
	}
	
	public boolean contains(T thing) {
		return map.containsKey(thing);
	}
	
	public Set<Entry<T, List<T>>> entries() {
		return map.entrySet();
	}
	
	public int entriesCount() {
		return map.size();
	}
	
	public List<T> resolveOrder() {
		final List<T> resolved = new OpenList<>(map.size());
		int lastSize = -1;// necessary to detect dependancies errors (like circular dependancies)
		while (map.size() > 0) {
			Iterator<Entry<T, List<T>>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<T, List<T>> entry = it.next();
				final T thing = entry.getKey();
				final List<T> deps = entry.getValue();
				if (deps.isEmpty()) {// no dependancy
					resolved.add(thing);
					it.remove();
				} else {// maybe unresolved dependancies
					boolean allDepsResolved = true;
					for (T dep : deps) {// for each dependancy
						if (!resolved.contains(dep)) {
							allDepsResolved = false;
							break;
						}
					}
					if (allDepsResolved) {// all dependancies are resolved
						resolved.add(thing);
						it.remove();
					} // else: do nothing
				}
			}
			if (map.size() == lastSize) {// no change made since the last iteration
				// Unresolvable dependancies!
				return resolved;
			}
			lastSize = map.size();
		}
		return resolved;
	}
	
}
