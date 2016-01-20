package uk.me.webpigeon.mud.client.planner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public class DumbSolver implements Planner {


	public List<ActionInstance> plan(FolBeliefSet initialState, List<FolFormula> goals, Set<ActionInstance> actions) {
		
		List<ActionInstance> plan = new ArrayList<ActionInstance>();
		int iStillFeelLikeIt = 100;
		
		while(iStillFeelLikeIt > 0) {
			if (isSolved(goals, initialState)) {
				return plan;
			}
			
			
			
			iStillFeelLikeIt--;
		}
		
		return plan;
	}
	
	public boolean isSolved(List<FolFormula> goals, FolBeliefSet state){
		for (FolFormula goal : goals) {
			if(!state.contains(goal)){
				return false;
			}
		}
		return true;
	}

}
