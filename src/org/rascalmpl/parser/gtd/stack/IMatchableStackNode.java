package org.rascalmpl.parser.gtd.stack;


public interface IMatchableStackNode{
	boolean match(char[] input);
	
	boolean matchWithoutResult(char[] input, int location);
	
	int getLength();
}
