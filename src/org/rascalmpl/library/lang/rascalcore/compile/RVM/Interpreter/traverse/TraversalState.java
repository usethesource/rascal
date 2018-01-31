package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.FunctionInstance;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Reference;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import org.rascalmpl.values.uptr.ITree;

public class TraversalState {
	
	/*
	 * TraversalState contains the complete context for a traversal and provides the following:
	 * (1) flags that represent the state of the traversal:
	 * 		- matched: 		did a case match?
	 * 		- changed: 		has a replacement been made?
	 * 		- leaveVisit:	leave the visit due to a return in the excuted case?
	 * 
	 * (2) The specific traverseOnce function to be executed: 'traverse'
	 * (3) An interface with the compiled code that executes the cases of the visit:
	 * 		- phi: the compiled function
	 * 		- refMatched, refChanged, refLeaveVisit refBegin, refEnd: reference variables of the phi function 
	 * 		  to communicate the above fields (matched, changed, leaveVisit) to/from the phi function.
	 * 		- access functions (isChanged, setChanged, etc) give a cached view on these fields.
	 * 		- In order to reduce garbage, we allocate the call frame of the phi function once and reuse and update
	 * 		  it on each call.
	 */

	private final RVMCore rvm;					// The RVM we are using
	private final DescendantDescriptor descriptor; 	
											// Describes in wich subtree to descend or not
	private final boolean isAllwaysTrue;

	private boolean matched;				// Some rule matched;
	private boolean changed;				// Original subject has been changed
	private boolean leavingVisit;			// Return executed in visit case code
	
	private final Reference refMatched; 	// Reference variable to cummunicate 'matched' with phi
	private final Reference refChanged;		// Reference variable to cummunicate 'changed' with phi
	private final Reference refLeaveVisit;	// Reference variable to cummunicate 'leavingVisit' with phi
	private final Reference refBegin;		// Reference variable to cummunicate 'begin' with phi (not cached in field)
	private final Reference refEnd;			// Reference variable to cummunicate 'end' with phi (not cached in field)
	
	private final Frame frame;				// Reused call frame of phi function
	private final int stackStartOfOtherLocals;
	private final int stackEnd;
	
	ITraverse traverse;						// The specific traverseOnce function to be used

	public TraversalState(RVMCore rvm, FunctionInstance phi, Reference refMatched, Reference refChanged, Reference refLeaveVisit, Reference refBegin, Reference refEnd, DescendantDescriptor descriptor) {
		this.rvm = rvm;
		this.refMatched = refMatched;
		this.refChanged = refChanged;
		this.refLeaveVisit = refLeaveVisit;
		this.refBegin = refBegin;
		this.refEnd = refEnd;
		this.descriptor = descriptor;
		this.isAllwaysTrue = descriptor.isAllwaysTrue();
		matched = false;
		changed = false;
		leavingVisit = false;
		setRefLeavingVisit(false);
		frame = rvm.makeFrameForVisit(phi);
		frame.stack[1] = refMatched;
		frame.stack[2] = refChanged;
		frame.stack[3] = refLeaveVisit;
		frame.stack[4] = refBegin;
		frame.stack[5] = refEnd;
		stackStartOfOtherLocals = 6;
		stackEnd = frame.stack.length;
	}
	
	public boolean hasMatched() {
		return matched;
	}
	
	private boolean getRefMatched() {
		return ((IBool)refMatched.getValue()).getValue();
	}

	private void setRefMatched(boolean matched) {
		refMatched.setValue(RascalPrimitive.vf.bool(matched));
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
	
	private boolean getRefChanged(){
		return ((IBool)refChanged.getValue()).getValue();
	}
	
	private void setRefChanged(boolean changed) {
		refChanged.setValue(RascalPrimitive.vf.bool(changed));
	}
	
	public void setMatchedAndChanged(boolean matched, boolean changed){
		this.matched = matched;
		this.changed = changed;
	}
	
	public boolean isLeavingVisit(){
		return leavingVisit;
	}
	
	private boolean getRefLeavingVisit(){
		return ((IBool)refLeaveVisit.getValue()).getValue();
	}
	
	private void setRefLeavingVisit(boolean leaving) {
		refLeaveVisit.setValue(RascalPrimitive.vf.bool(leaving));
	}

	public int getBegin() {
		return ((Integer)refBegin.getValue()).intValue();
	}
	public void setBegin(int begin) {
		refBegin.setValue((Integer)begin);
	}
	
	public int getEnd() {
		return ((Integer)refEnd.getValue()).intValue();
	}
	public void setEnd(int end) {
		refEnd.setValue((Integer)end);
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
	public IValue execute(IValue subject){
		setRefMatched(matched);		// Copy current values to reference variables
		setRefChanged(changed);
		frame.stack[0] = subject;	// Set subject argument in frame
									// stack[1] ... stack[5] are the reference parameters
									// Clear rest of stack to avoid unwanted initializations of other locals or temporaries
		for(int i = stackStartOfOtherLocals; i < stackEnd; i++){
			frame.stack[i] = null;
		}
		IValue res = rvm.executeRVMFunctionInVisit(frame);
		
		matched = getRefMatched();	// Copy values of reference variables back to fields
		changed = getRefChanged();
		leavingVisit = getRefLeavingVisit();
		return res;
	}
}