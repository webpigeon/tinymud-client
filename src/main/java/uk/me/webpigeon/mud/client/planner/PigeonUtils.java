package uk.me.webpigeon.mud.client.planner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public class PigeonUtils {

	public static Set<FolFormula> ground(Set<FolFormula> formulas, Map<Variable,Constant> bindings) {
		
		Set<FolFormula> result = new HashSet<FolFormula>();
		for (FolFormula ungrounded : formulas) {
			FolFormula grounded = (FolFormula)ungrounded.substitute(bindings);
			result.add(grounded);
		}
		
		return result;
	}
	
	public static FolBeliefSet updateState(FolBeliefSet state, ActionInstance action){
		FolBeliefSet clone = new FolBeliefSet();
		clone.addAll(state);
		
		for (FolFormula formula : action.getAddList()) {
			clone.add(formula);
		}
		
		for (FolFormula formula : action.getDelList()) {
			clone.remove(formula);
		}
		
		return clone;
	}
	
	public static FolBeliefSet clone(FolBeliefSet state){
		FolBeliefSet clone = new FolBeliefSet();
		clone.addAll(state);
		
		return state;
	}
	
}
