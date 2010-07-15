package org.rascalmpl.test.parser;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Assert;

public class ParserTest extends TestCase{
	
	public ParserTest(){
		super();
	}
	
	public void executeParser(IParserTest parser){
		try{
			Assert.assertTrue(parser.executeTest());
		}catch(IOException ioex){
			// Ignore, never happens.
		}
	}
	
	public void testBasic(){
		executeParser(new Simple1());
		executeParser(new Simple2());
		
		executeParser(new CharRange());
		executeParser(new CILiteral());
		
		executeParser(new Epsilon());
	}
	
	public void testAmbiguitiesBasic(){
		executeParser(new Ambiguous1());
		executeParser(new Ambiguous2());
		executeParser(new Ambiguous3());
		executeParser(new Ambiguous4());
		executeParser(new Ambiguous5());
		executeParser(new Ambiguous6());
	}
	
	public void testSplitAndMerge(){
		executeParser(new SplitAndMerge1());
		executeParser(new SplitAndMerge2());
		executeParser(new SplitAndMerge3());
	}
	
	public void testList(){
		executeParser(new CharPlusList());
		executeParser(new CharStarList());
		
		executeParser(new NonTerminalPlusList());
		executeParser(new NonTerminalStarList());
	}
	
	public void testSeparatedList(){
		executeParser(new SeparatedPlusList());
		executeParser(new SeparatedStarList());
	}
	
	public void testOptional(){
		executeParser(new Optional1());
		executeParser(new Optional2());
		executeParser(new Optional3());
	}
	
	public void testAmbiguousList(){
		executeParser(new AmbiguousNonTerminalPlusList1());
		executeParser(new AmbiguousNonTerminalPlusList2());
		executeParser(new AmbiguousNestedPlusList());
	}
	
	public void testRecursion(){
		executeParser(new RightRecursion());
		executeParser(new LeftRecursion());
		
		executeParser(new EmptyRightRecursion());
	}
	
	public void testAmbiguousRecursion(){
		executeParser(new AmbiguousRecursive());
	}
	
	public void testCycle(){
		executeParser(new NotAUselessSelfLoop());
		executeParser(new UselessSelfLoop());
		
		executeParser(new CycleEpsilon());
	}
	
	public void testFollowRestriction(){
		executeParser(new FollowRestriction());
	}
	
	public void testReject(){
		executeParser(new Reject());
	}
}
