package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;

public interface INode{
	void addAlternative(IConstructor production, INode[] children);
	
	boolean isEpsilon();
}
