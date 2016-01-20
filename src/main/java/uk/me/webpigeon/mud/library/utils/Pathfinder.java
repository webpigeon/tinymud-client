package uk.me.webpigeon.mud.library.utils;

import java.util.List;

import uk.me.webpigeon.mud.library.Room;

public interface Pathfinder {
	
	public List<String> findPath(Room from, Room to);

}
