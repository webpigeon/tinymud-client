package uk.me.webpigeon.mud.client.planner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.action.ActionName;
import net.sf.tweety.action.grounding.GroundingTools;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public class Operator {
	private String name;
	
	private Set<Variable> variables;
	private	Set<FolFormula> pre;
	private Set<FolFormula> add;
	private Set<FolFormula> del;
	
	
	public Operator(String name, Variable ... variables) {
		this.name = name;
		
		this.variables = new HashSet<Variable>();
		for (Variable variable : variables) {
			this.variables.add(variable);
		}
		
		this.pre = new HashSet<FolFormula>();
		this.add = new HashSet<FolFormula>();
		this.del = new HashSet<FolFormula>();
	}

	@Override
	public String toString() {
		return name+"("+variables+")";
	}

	public void addPrecondition(FolFormula formula) {
		pre.add(formula);
	}

	public void addAdd(FolFormula formula) {
		add.add(formula);
	}
	
	public void addDel(FolFormula formula) {
		del.add(formula);
	}
	
	public Set<ActionInstance> getGroundings(Set<Constant> constants) {
		Set<ActionInstance> groundActions = new HashSet<ActionInstance>();
		
		Set<Map<Variable, Constant>> groundings = GroundingTools.getAllSubstitutions(variables, constants);
		for (Map<Variable,Constant> groundSet : groundings){
			ActionInstance action = new ActionInstance(this, groundSet);
			groundActions.add(action);
		}
		
		return groundActions;
	}
	
	public ActionInstance bind(Variable v, Constant c) {
		ActionInstance action = new ActionInstance(this);
		action.ground(v,c);
		return action;
	}
	
	public void print(){
		System.out.println(name);
		System.out.println(pre);
		System.out.println(add);
		System.out.println(del);
	}

	public Set<FolFormula> getPrecondtions() {
		return Collections.unmodifiableSet(pre);
	}
	
	public Set<FolFormula> getAddList() {
		return Collections.unmodifiableSet(add);
	}
	
	public Set<FolFormula> getDelList() {
		return Collections.unmodifiableSet(del);
	}

	public String getName() {
		return name;
	}
	
}
