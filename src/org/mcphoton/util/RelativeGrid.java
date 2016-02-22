package org.mcphoton.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * A 2d grid with its center at coordinates (0,0).
 * 
 * @author ElectronWill
 * 		
 * @param <E>
 */
public class RelativeGrid<E> {
	
	private final AtomicReferenceArray<E>[] grid;
	private final int center;
	
	public RelativeGrid(int d) {
		int length = 2 * d + 1;
		grid = new AtomicReferenceArray[length];
		for (int i = 0; i < 2 * d + 1; i++) {
			grid[i] = new AtomicReferenceArray(length);
		}
		center = d;
	}
	
	public boolean contains(int rx, int rz) {
		return get(rx, rz) != null;
	}
	
	public boolean contains(Object value) {
		for (int x = 0; x < grid.length; x++) {
			AtomicReferenceArray<E> xArray = grid[x];
			for (int z = 0; z < grid.length; z++) {
				E e = xArray.get(z);
				if (e.equals(value))
					return true;
			}
		}
		return false;
	}
	
	public E get(int rx, int rz) {
		return grid[center + rx].get(center + rz);
	}
	
	public void remove(int rx, int rz) {
		grid[center + rx].set(center + rz, null);
	}
	
	public void set(int rx, int rz, E value) {
		grid[center + rx].set(center + rz, value);
	}
	
	public int getCenterIndex() {
		return center;
	}
	
	public int sideLength() {
		return grid.length;
	}
	
	public int size() {
		return grid.length * grid.length;
	}
	
	public RelativeGrid<E> shift(int xShift, int zShift) {
		int d = (sideLength() - 1) / 2;
		RelativeGrid<E> newGrid = new RelativeGrid<E>(sideLength());
		for (int x = -d; x <= d; x++) {
			for (int z = -d; z <= d; z++) {
				newGrid.set(x + xShift, z + zShift, this.get(x, z));
			}
		}
		return newGrid;
	}
	
	/*
	 * public List<Truc> trucsAFairePourToutChargerDansLOrdre()
	 * public void put(Chunk c)
	 * public void remove(chunk c)
	 * public Chunk getRelative(x,z)
	 * -----
	 * public void handlePlayerMove():
	 * 		
	 */
	
}
