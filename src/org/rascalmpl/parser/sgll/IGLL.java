package org.rascalmpl.parser.sgll;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.INode;

public interface IGLL{
	public final static int START_SYMBOL_ID = -1;
	
	public final static int LIST_LIST_FLAG = 0x80000000;
	
	INode parse(IConstructor start, String input);
	INode parse(IConstructor start, char[] input);
}
