package org.rascalmpl.parser.sgll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;

public interface IGLL{
	public final static int START_SYMBOL_ID = -1;
	
	public final static int LIST_LIST_FLAG = 0x80000000;
	
	IValue parse(IConstructor start, char[] input);
	IValue parse(IConstructor start, String input);
	IValue parse(IConstructor start, InputStream in) throws IOException;
	IValue parse(IConstructor start, Reader in) throws IOException;
	IValue parse(IConstructor start, File inputFile) throws IOException;
}
