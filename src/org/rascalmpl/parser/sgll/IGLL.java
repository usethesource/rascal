package org.rascalmpl.parser.sgll;

import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.stack.StackNode;

public interface IGLL{
	public final static int START_SYMBOL_ID = -1;
	
	public final static int LIST_LIST_FLAG = 0x80000000;
	
	INode parse(StackNode startNode);
}
