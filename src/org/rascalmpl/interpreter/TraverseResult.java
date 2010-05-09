package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.IValue;

public class TraverseResult {
	/*
	 * TraverseResult contains the value returned by a traversal
	 * and a changed flag that indicates whether the value itself or
	 * any of its children has been changed during the traversal.
	 */

	// TODO: can this be put in the result hierarchy?
		public boolean matched;   // Some rule matched;
		public IValue value; 		// Result<IValue> of the 
		public boolean changed;   // Original subject has been changed

		public TraverseResult(boolean someMatch, IValue value){
			this.matched = someMatch;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(IValue value){
			this.matched = false;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(IValue value, boolean changed){
			this.matched = true;
			this.value   = value;
			this.changed = changed;
		}
		public TraverseResult(boolean someMatch, IValue value, boolean changed){
			this.matched = someMatch;
			this.value   = value;
			this.changed = changed;
		}

}
