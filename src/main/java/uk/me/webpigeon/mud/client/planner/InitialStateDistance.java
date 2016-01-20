package uk.me.webpigeon.mud.client.planner;

import java.util.Comparator;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public class InitialStateDistance implements Comparator<ActionInstance> {
	private FolBeliefSet state;
	
	public InitialStateDistance(FolBeliefSet state) {
		this.state = state;
	}
	

	@Override
	public int compare(ActionInstance o1, ActionInstance o2) {
		
		int dist1 = getDistance(o1);
		int dist2 = getDistance(o2);
		
		return Integer.compare(dist1, dist2);
	}
	
	public int getDistance(ActionInstance o1) {
		int counts = 0;
		
		for (FolFormula precond : o1.getPre()) {
			if (!state.contains(precond)) {
				counts++;
			}
		}
		
		return counts;
	}

}
