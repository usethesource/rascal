package org.rascalmpl.parser.sgll.result;

public interface INode{
	void addAlternative(INode[] children);
	
	boolean isEpsilon();
}
