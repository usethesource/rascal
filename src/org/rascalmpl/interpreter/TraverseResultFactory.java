package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public final class TraverseResultFactory {
	private static final TraverseResult tr = new TraverseResult(null);
	private static boolean free = true;
	
	public static final TraverseResult makeTraverseResult(boolean someMatch, IValue value){
		if(free){
			free = false;
			tr.matched = someMatch;
			tr.value = value;
			tr.changed = false;
			return tr;
		} else			
			throw new ImplementationError("TraverseResultFactory");
	}
	
	public static final TraverseResult makeTraverseResult(IValue value){
		if(free){
			free = false;
			tr.matched = false;
			tr.value = value;
			tr.changed = false;
			return tr;
		} else 
			throw new ImplementationError("TraverseResultFactory");
	}

	public static final TraverseResult makeTraverseResult(IValue value, boolean changed){
		if(free){
			free = false;
			tr.matched = true;
			tr.value   = value;
			tr.changed = changed;
			return tr;
		} else 
			throw new ImplementationError("TraverseResultFactory");
	}
	
	public static final TraverseResult makeTraverseResult(boolean someMatch, IValue value, boolean changed){
		if(free){
			free = false;
			tr.matched = someMatch;
			tr.value   = value;
			tr.changed = changed;
			return tr;
		} else 
			throw new ImplementationError("TraverseResultFactory");
	}
	
	public static final void freeTraverseResult(TraverseResult tr){
		if(free)
			throw new ImplementationError("TraverseResultFactory");
		free = true;
	}

}
