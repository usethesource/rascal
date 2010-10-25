package org.rascalmpl.test.parser;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;

public interface IParserTest{
	IConstructor executeParser();
	
	IValue getExpectedResult() throws IOException;
}
