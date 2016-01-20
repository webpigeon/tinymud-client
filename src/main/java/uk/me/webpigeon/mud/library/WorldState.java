package uk.me.webpigeon.mud.library;

import java.util.HashMap;
import java.util.Map;

public class WorldState {
	public Map<String, Room> rooms;
	public Map<String, Account> players;
	
	public WorldState() {
		this.rooms = new HashMap<String, Room>();
		this.players = new HashMap<String, Account>();
	}
	
}
