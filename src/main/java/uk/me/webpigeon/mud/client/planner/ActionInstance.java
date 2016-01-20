package uk.me.webpigeon.mud.client.planner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.action.ActionName;
import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

public class ActionInstance {
	private Operator action;
	private Map<Variable, Constant> bindings;

	public ActionInstance(Operator action) {
		this.action = action;
		this.bindings = new HashMap<Variable, Constant>();
	}
	
	public ActionInstance(Operator action, Map<Variable, Constant> groundSet) {
		this.action = action;
		this.bindings = new HashMap<Variable, Constant>(groundSet);
	}
	
	public void apply(FolBeliefSet bs) {
		assert isGround();
		
		Set<FolFormula> preList = getPre();
		for (FolFormula pre : preList) {
			if (!bs.contains(pre)) {
				throw new IllegalArgumentException("precondition not fofilled "+pre);
			}
		}
		
		Set<FolFormula> addList = getAddList();
		for (FolFormula addItem : addList) {
			bs.add(addItem);
		}
		
		Set<FolFormula> rmList = getDelList();
		for (FolFormula rmItem : rmList) {
			bs.remove(rmItem);
		}
	}
	
	public boolean isGround() {
		for (FolFormula fol : getPre()) {
			if(!fol.isGround()){
				return false;
			}
		}
		
		for (FolFormula fol : getAddList()) {
			if(!fol.isGround()){
				return false;
			}
		}
		
		for (FolFormula fol : getDelList()) {
			if(!fol.isGround()){
				return false;
			}
		}
		
		return true;
	}
	
	public Set<FolFormula> getPre() {	
		return PigeonUtils.ground(action.getPrecondtions(), bindings);
	}
	
	public Set<FolFormula> getAddList() {
		return PigeonUtils.ground(action.getAddList(), bindings);
	}
	
	public Set<FolFormula> getDelList() {
		return PigeonUtils.ground(action.getDelList(), bindings);
	}

	public void ground(Variable variable, Constant constant) {
		bindings.put(variable, constant);
	}
	
	public String toString() {
		return action.toString()+" "+bindings;
	}
	
}
