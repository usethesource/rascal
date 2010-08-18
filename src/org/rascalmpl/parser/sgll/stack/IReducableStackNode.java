package org.rascalmpl.parser.sgll.stack;

public interface IReducableStackNode{
	boolean reduce(char[] input);
	
	boolean reduceWithoutResult(char[] input, int location);
	
	int getLength();
}
