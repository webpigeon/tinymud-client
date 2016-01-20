package uk.me.webpigeon.mud.client.planner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;

public class LinearSolver implements Planner {
	private static final Boolean DEBUG = true;
	private static final Integer MAX_DEPTH = 15;
	
	@Override
	public List<ActionInstance> plan(FolBeliefSet initial, List<FolFormula> goals, Set<ActionInstance> actionSet) {
		return plan(initial, goals, actionSet, new ArrayList<ActionInstance>(), 0);
	}

	public List<ActionInstance> plan(FolBeliefSet initial, List<FolFormula> goals, Set<ActionInstance> actionSet, List<ActionInstance> currAction, int depth) {
		
		String padding = "[*] ";
		for (int i=0; i<depth;i++) {
			padding += "++";
		}
		
		List<ActionInstance> plan = new ArrayList<ActionInstance>();
		
		FolBeliefSet state = new FolBeliefSet();
		state.addAll(initial);
		
		if (goals.isEmpty()) {
			return plan;
		}
		
		if (depth > MAX_DEPTH) {
			return null;
		}
		
		for (int i=0; i<goals.size(); ) {
			FolFormula goal = goals.get(i);
			
			if (DEBUG) {
				System.out.println(padding+"Current Plan: "+currAction);
				System.out.println(padding+"subgoal: "+goal);
				System.out.println(padding+"other goals: "+goals.subList(i+1, goals.size()));
			}
			
			if (isSatisfied(state, goal)) {
				i++;
				continue;
			}
			
			Set<ActionInstance> possibleActions = findAction(actionSet, goal, state);
			
			if (DEBUG) {
				System.out.println(padding+"possible actions: "+possibleActions);
			}
			
			boolean found = false;
			
			for (ActionInstance possibleAction : possibleActions) {	
				//TODO check if preconditions are reachable
				//TODO check if this action voilates another goal
				
				if (DEBUG) {
					System.out.println(padding+"next possible action "+possibleAction+" for goal "+goal);
				}
				
				FolBeliefSet tempState = PigeonUtils.clone(state);
				List<FolFormula> subgoals = new ArrayList<>(possibleAction.getPre());
				currAction.add(possibleAction);
				
				List<ActionInstance> solution = plan(tempState, subgoals, actionSet, currAction, depth+=1);
				if (solution == null) {
					currAction.remove(currAction.size()-1);
					continue;
				}
				
				//accept temporary state as valid
				tempState = PigeonUtils.updateState(tempState, possibleAction);
				
				//TODO handle clobbering
				Set<FolFormula> clobbered = getClobberedGoals(goals.subList(0, i), tempState);
				//System.out.println("clobber detection "+i+" on "+tempState+" "+possibleAction + goals.subList(0, i) + " "+clobbered);
				if(!clobbered.isEmpty()) {
					if (DEBUG) {
						System.out.println(padding+"whoops, clobbered "+clobbered+" for goal "+goal);
					}
					
					goals.removeAll(clobbered);
					goals.addAll(clobbered);
					i -= clobbered.size();
				}
				
				plan.addAll(solution);
				state = tempState;
				plan.add(possibleAction);
				
				i++;
				found = true;
				break;
				
			}
			
			if (!found) {
				return null;
			}
			
		}
		
		return plan;
	}
	
	public Set<FolFormula> getClobberedGoals(List<FolFormula> goals, FolBeliefSet tmp) {
		System.out.println(tmp+" "+goals);
		
		Set<FolFormula> clobbered = new HashSet<FolFormula>();
		
		for (FolFormula goal : goals) {
			if (!isSatisfied(tmp, goal)) {
				clobbered.add(goal);
			}
		}
		
		return clobbered;
	}
	
	public Set<ActionInstance> findAction(Set<ActionInstance> actions, FolFormula goal, FolBeliefSet state) {
		SortedSet<ActionInstance> instances = new TreeSet<ActionInstance>(new InitialStateDistance(state));
		
		for (ActionInstance action : actions) {
			Set<FolFormula> addList = action.getAddList();
			if (addList.contains(goal)) {
				instances.add(action);
			}
		}
		
		return instances;
	}

	private boolean isSatisfied(FolBeliefSet state, FolFormula goal) {
		return state.contains(goal);
	}
	
}
