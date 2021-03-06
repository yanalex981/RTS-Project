package rts.networking.collections;

import java.util.HashMap;

import rts.elements.ControllableObject;

/**
 * Stores units of a player
 * Units are retrieved by ID
 * 
 * @author Alex
 */
public class UnitList {
	int currentID = 0;
	SortedList usedIDs = new SortedList();
	
	HashMap<Integer, ControllableObject> units = new HashMap<Integer, ControllableObject>(16);
	
	public UnitList() {}
	
	public void add(ControllableObject o) {
		while (usedIDs.exists(currentID)) {
			++currentID; // Integer overflow will not occur in Java. It loops back to -2 billion
		}
		
		o.setID(currentID);
		usedIDs.add(currentID);
		units.put(currentID, o);
	}
	
	public boolean exists(int id) {
		return usedIDs.exists(id);
	}
	
	public void remove(int id) {
		units.remove(id);
		usedIDs.remove(id);
	}
	
	public ControllableObject get(int unitID) {
		return units.get(unitID);
	}
	
	public int size() {
		return units.size();
	}
}
