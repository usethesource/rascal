package org.rascalmpl.parser.sgll;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;

public interface IGLL{
	public final static int START_SYMBOL_ID = -1;
	
	public final static int LIST_LIST_FLAG = 0x80000000;
	
	IValue parse(IConstructor start, String input);
	IValue parse(IConstructor start, char[] input);
}
