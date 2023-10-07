package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IValue;

public class TraversalState {
	
	/*
	 * TraversalState contains the complete context for a traversal and provides the following:
	 * (1) flags that represent the state of the traversal:
	 * 		- matched: 		did a case match?
	 * 		- changed: 		has a replacement been made?
	 * 		- leaveVisit:	leave the visit due to a return in the excuted case?
	 * 	    - begin:		begin of a string visit
	 * 	    - end:			end of a string visit
	 * 
	 * (2) The specific traverseOnce function to be executed: 'traverse'
	 * (3) An interface with the compiled code that executes the cases of the visit:
	 * 		- phi: the compiled function
	 */

	private final IDescendantDescriptor descriptor; 	
											// Describes in wich subtree to descend or not
	private final boolean isAllwaysTrue;

	private boolean matched;				// Some rule matched;
	private boolean changed;				// Original subject has been changed
	private boolean leavingVisit;			// Return executed in visit case code
	
	private int begin;						// Begin of string visit
	private int end;						// End of string visit

	private final IVisitFunction phi;
	
	ITraverse traverse;						// The specific traverseOnce function to be used

	public TraversalState(IVisitFunction phi,  IDescendantDescriptor descriptor) {
		this.descriptor = descriptor;
		this.isAllwaysTrue = descriptor.isAllwaysTrue();
		matched = false;
		changed = false;
		leavingVisit = false;
		begin = 0;
		end = 0;
		this.phi = phi;
	}
	
	public boolean hasMatched() {
		return matched;
	}
	
	public void setMatched(boolean matched){
		this.matched = matched;
	}

	public boolean hasChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	public void setMatchedAndChanged(boolean matched, boolean changed){
		this.matched = matched;
		this.changed = changed;
	}
	
	public void setLeavingVisit(boolean leavingVisit) {
		this.leavingVisit = leavingVisit;
	}
	
	public boolean isLeavingVisit(){
		return leavingVisit;
	}
	
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public boolean shouldDescentInAbstractValue(IValue subject){
		return isAllwaysTrue || descriptor.shouldDescentInAbstractValue(subject).getValue();
	}
	
	public boolean shouldDescentInConcreteValue(ITree subject){
		return descriptor.shouldDescentInConcreteValue(subject).getValue();
	}
	
	public boolean isConcreteMatch(){
		return descriptor.isConcreteMatch();
	}
	
	/**
	 * @param subject argument for phi function
	 * @return result of phi(subject)
	 */
	public IValue execute(IValue subject) {
		return phi.execute(subject, this);
	}
}