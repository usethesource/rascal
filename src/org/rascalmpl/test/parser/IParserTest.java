package org.rascalmpl.test.parser;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IValue;

public interface IParserTest{
	IValue executeParser();
	
	IValue getExpectedResult() throws IOException;
}
