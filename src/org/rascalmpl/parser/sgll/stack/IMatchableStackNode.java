package org.rascalmpl.parser.sgll.stack;

public interface IMatchableStackNode{
	boolean match(char[] input);
	
	boolean matchWithoutResult(char[] input, int location);
	
	int getLength();
}
