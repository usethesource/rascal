package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

public interface IMatchableStackNode{
	boolean match(URI inputURI, char[] input);
	
	boolean matchWithoutResult(char[] input, int location);
	
	int getLength();
}
