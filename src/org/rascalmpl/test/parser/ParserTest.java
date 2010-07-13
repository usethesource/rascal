package org.rascalmpl.test.parser;

import org.junit.Assert;
import org.rascalmpl.test.TestFramework;

public class ParserTest extends TestFramework{
	
	public ParserTest(){
		super();
	}
	
	public void executeParser(IParserTest parser){
		Assert.assertTrue(parser.executeTest());
	}
	
	public void testBasic(){
		// Simple1+2
		// Char range
		// Literal stuff
		// Epsilon
	}
	
	public void testAmbiguitiesBasic(){
		// Ambiguous1-6
	}
	
	public void testSplitAndMerge(){
		// SplitAndMerge1-3
	}
	
	public void testList(){
		// list *
	}
	
	public void testSeparatedList(){
		// sep list *
	}
	
	public void testOptional(){
		// Optional1-3
	}
	
	public void testRecursion(){
		// left / right / amb recursion
	}
	
	public void testCycle(){
		// UselessSelfLoop
		// Not a useless self loop
		// Cycle epsilon
	}
}
