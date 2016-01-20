package uk.me.webpigeon.mud.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.tweety.action.grounding.GroundingTools;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Sort;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;

import uk.me.webpigeon.mud.client.planner.Operator;
import uk.me.webpigeon.mud.client.planner.Planner;
import uk.me.webpigeon.mud.client.planner.ActionInstance;
import uk.me.webpigeon.mud.client.planner.DumbSolver;
import uk.me.webpigeon.mud.client.planner.LinearSolver;

public class App {

	public static void main(String[] args) {
		Predicate at = new Predicate("at", 1);
		Predicate boxAt = new Predicate("boxAt", 1);
		Predicate bananasAt = new Predicate("bananasAt", 1);
		
		Constant a = new Constant("a");
		Constant b = new Constant("b");
		Constant c = new Constant("c");
		
		Set<Constant> constants = new HashSet<Constant>();
		constants.add(a);
		constants.add(b);
		constants.add(c);
		
		FolBeliefSet initialState = new FolBeliefSet();
		initialState.add(new FOLAtom(at, a));
		initialState.add(new FOLAtom(new Predicate("level", 1), new Constant("low")));
		initialState.add(new FOLAtom(boxAt, c));
		initialState.add(new FOLAtom(bananasAt, b));
		
		Operator move = new Operator("move", new Variable("X"), new Variable("Y"));
		move.addPrecondition(new FOLAtom(new Predicate("at", 1), new Variable("X")));
		move.addPrecondition(new FOLAtom(new Predicate("level", 1), new Constant("low")));
		//move.addPostCondition( (new FOLAtom(new Predicate("at", 1), new Variable("X"))).complement() );
		move.addAdd(new FOLAtom(new Predicate("at", 1), new Variable("Y")));
		move.addDel(new FOLAtom(new Predicate("at", 1), new Variable("X")));
		
		Operator pushBox = new Operator("pushBox", new Variable("X"), new Variable("Y"));
		pushBox.addPrecondition(new FOLAtom(at, new Variable("X")));
		pushBox.addPrecondition(new FOLAtom(boxAt, new Variable("X")));
		pushBox.addPrecondition(new FOLAtom(new Predicate("level", 1), new Constant("low")));
		pushBox.addAdd(new FOLAtom(at, new Variable("Y")));
		pushBox.addAdd(new FOLAtom(boxAt, new Variable("Y")));
		pushBox.addDel(new FOLAtom(at, new Variable("X")));
		pushBox.addDel(new FOLAtom(boxAt, new Variable("X")));
		
		Operator toast = new Operator("toast", new Variable("X"));
		toast.addPrecondition(new FOLAtom(new Predicate("at", 1), new Variable("X")));
		toast.addPrecondition(new FOLAtom(new Predicate("level", 1), new Constant("low")));
		toast.addAdd(new FOLAtom(new Predicate("toast", 0)));
		
		//this should not be possible, it's here to upset planners
		Operator stupidMove = new Operator("warp", new Variable("X"), new Variable("Y"));
		stupidMove.addPrecondition(new FOLAtom(new Predicate("at", 1), new Variable("X")));
		stupidMove.addPrecondition(new FOLAtom(new Predicate("warp-core", 1), new Variable("X")));
		stupidMove.addPrecondition(new FOLAtom(new Predicate("level", 1), new Constant("low")));
		stupidMove.addAdd(new FOLAtom(new Predicate("at", 1), new Variable("Y")));
		stupidMove.addDel(new FOLAtom(new Predicate("at", 1), new Variable("X")));
		
		
		//moveAB.apply(initialState);
		//System.out.println(initialState);
		
		List<FolFormula> goals = new ArrayList<FolFormula>();
		goals.add(new FOLAtom(at, b));
		goals.add(new FOLAtom(boxAt, a));
		
		Set<ActionInstance> actionSet = new HashSet<ActionInstance>();
		//actionSet.addAll(toast.getGroundings(constants));
		//actionSet.addAll(stupidMove.getGroundings(constants));
		actionSet.addAll(move.getGroundings(constants));
		actionSet.addAll(pushBox.getGroundings(constants));
		
		
		Planner planner = new LinearSolver();
		System.out.println("Initial state: "+initialState);
		System.out.println("Goals: "+goals);
		
		List<ActionInstance> actions = planner.plan(initialState, goals, actionSet);
		System.out.println("plan: "+actions);
		
		//System.out.println("unbound: "+f.getUnboundVariables());
		//System.out.println("quantifier: "+f.getQuantifierVariables());
		
		
		JFrame frame = new JFrame("WP-mud");
		frame.setPreferredSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new JTextArea(), BorderLayout.CENTER);
		frame.add(new JTextField(), BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}

}
