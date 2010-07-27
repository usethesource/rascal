package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;

public abstract class AbstractNode{
	
	public AbstractNode(){
		super();
	}
	
	public boolean isContainer(){
		return (this instanceof ContainerNode);
	}
	
	public abstract boolean isEpsilon();
	
	public abstract boolean isRejected();
	
	public abstract void addAlternative(IConstructor production, Link children);
	
	public abstract IValue toTerm(IndexedStack<AbstractNode> stack, int depth);
}
