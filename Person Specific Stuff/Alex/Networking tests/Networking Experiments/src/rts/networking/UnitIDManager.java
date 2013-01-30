package rts.networking;

import java.util.HashMap;
import java.util.Random;

import rts.elements.ControllableObject;

/**
 * Manages IDs of players and units
 * Created as an effort to integrate
 * with Bobby and Ian's client...
 * 
 * @author Alex
 */
public class UnitIDManager {
	private static final int UNIT_ID_UPPER_LIMIT = 100000000;
	private static final int UNIT_ID_LOWER_LIMIT = 1;
	private static final int PLAYER_ID_UPPER_LIMIT = 9;
	private static final int PLAYER_ID_LOWER_LIMIT = 1;
	
//	private HashMap<Integer, ControllableObject> UIDs = new HashMap<Integer, ControllableObject>();
//	private HashMap<String, Integer> playerIDs = new HashMap<String, Integer>();
	private HashMap<Integer, ControllableObject> usedUnitIDs;
	private HashMap<String, Integer> usedPlayerIDs;
	
	Random random = new Random();
	
	public UnitIDManager() {}
	
	public void addPlayer(String playerName) {
		int id = random.nextInt(PLAYER_ID_UPPER_LIMIT) + PLAYER_ID_LOWER_LIMIT;
		
		if (!usedPlayerIDs.containsKey(playerName)) {
			do {
				id = random.nextInt(PLAYER_ID_UPPER_LIMIT) + PLAYER_ID_LOWER_LIMIT;
			} while (usedPlayerIDs.containsValue(id));
		}
		
		addPlayer(playerName, id);
	}
	
	public void addPlayer(String playerName, int id) {
		usedPlayerIDs.put(playerName, id);
	}
	
	public void addUnit(ControllableObject object, int playerID) {
		int id = random.nextInt();
		
		while (usedUnitIDs.containsKey(id)) {
			id = random.nextInt(UNIT_ID_UPPER_LIMIT) + UNIT_ID_LOWER_LIMIT;
		}
		
		if (usedPlayerIDs.containsValue(playerID)) {
			id = playerID * UNIT_ID_UPPER_LIMIT + id;
			addUnit(object, playerID, id);
		}
	}
	
	public void addUnit(ControllableObject object, int playerID, int id) {
		usedUnitIDs.put(id, object);
	}
	
	public void remove(int unitID) {
		usedUnitIDs.remove(unitID);
	}
}
