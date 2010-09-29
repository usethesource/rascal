package org.rascalmpl.test.parser;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.junit.Assert;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.values.ValueFactoryFactory;

public class ParserTest extends TestCase{
	private final static TypeFactory tf = TypeFactory.getInstance();
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	public ParserTest(){
		super();
	}
	
	public void executeParser(IParserTest parser){
		try{
			IValue expectedResult = parser.getExpectedResult();
			if(expectedResult.getType().equals(tf.stringType())){
				try{
					parser.executeParser();
					Assert.fail("Expected a parse error to occur:\n"+expectedResult);
				}catch(SyntaxError se){
					IString message = vf.string(se.getMessage());
					if(!message.isEqual(expectedResult)){
						Assert.fail("Expected a parse error to occur:\n"+expectedResult+"\nError was:\n"+message);
					}
				}
			}else{
				IConstructor result = parser.executeParser();
				if(!result.isEqual(expectedResult)){
					Assert.fail(parser.getClass().getName()+";\tGot: "+result+"\n\t expected: "+expectedResult);
				}
			}
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
		executeParser(new Ambiguous7());
		executeParser(new Ambiguous8());
		executeParser(new Ambiguous9());
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

		executeParser(new NullableSharing());
	}
	
	public void testAmbiguousRecursion(){
		executeParser(new AmbiguousRecursive());
	}
	
	public void testCycle(){
		executeParser(new NotAUselessSelfLoop());
		executeParser(new UselessSelfLoop());
		
		executeParser(new CycleEpsilon());
	}
	
	public void testListCycle(){
		executeParser(new EpsilonList());
		executeParser(new AmbiguousEpsilonList());
	}
	
	public void testFollowRestriction(){
		executeParser(new FollowRestriction());
	}
	
	public void testReject(){
		executeParser(new Reject1());
		executeParser(new Reject2());
		executeParser(new Reject3());
		executeParser(new Reject4());
	}
	
	public void testListSharing(){
		executeParser(new ListOverlap());
	}
}
