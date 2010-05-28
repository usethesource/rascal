package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.util.IndexedStack;

public interface INode{
	void addAlternative(IConstructor production, INode[] children);
	
	boolean isEpsilon();
	
	IValue toTerm(IndexedStack<INode> stack, int depth);
}
