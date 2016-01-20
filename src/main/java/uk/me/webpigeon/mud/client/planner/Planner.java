package uk.me.webpigeon.mud.client.planner;

import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public interface Planner {
	
	public List<ActionInstance> plan(FolBeliefSet initialState, List<FolFormula> goals, Set<ActionInstance> actionSet);

}
