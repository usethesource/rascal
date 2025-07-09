/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.parser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.rascalmpl.parser.gtd.exception.ParseError;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class ParserTest {
	private static final TypeFactory tf = TypeFactory.getInstance();
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	
	public void executeParser(IParserTest parser){
		try {
			IValue expectedResult = parser.getExpectedResult();
			if (expectedResult.getType().equals(tf.stringType())){
				try {
					parser.executeParser();
					Assert.fail("Expected a parse error to occur:\n"+expectedResult);
				} 
				catch (ParseError pe) {
					IString message = vf.string(pe.getMessage());
					if(!message.equals(expectedResult)){
						Assert.fail("Expected a parse error to occur:\n"+expectedResult+"\nError was:\n"+message);
					}
				}
			} else {
				IConstructor result = parser.executeParser();
				if(!result.equals(expectedResult)){
					Assert.fail(parser.getClass().getName()+";\tGot: "+result+"\n\t expected: "+expectedResult);
				}
			}
		} catch(IOException ioex) {
			// Ignore, never happens.
		}
	}
	
	@Test
	public void testBasic(){
		executeParser(new Simple1());
		executeParser(new Simple2());
		
		executeParser(new CharRange());
		executeParser(new CILiteral());
		
		executeParser(new Epsilon());
	}
	
	@Test
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
	
	@Test
	public void testSplitAndMerge(){
		executeParser(new SplitAndMerge1());
		executeParser(new SplitAndMerge2());
		executeParser(new SplitAndMerge3());
	}
	
	@Test
	public void testList(){
		executeParser(new CharPlusList());
		executeParser(new CharStarList());
		
		executeParser(new NonTerminalPlusList());
		executeParser(new NonTerminalStarList());
	}
	
	@Test
	public void testSeparatedList(){
		executeParser(new SeparatedPlusList());
		executeParser(new SeparatedStarList());
	}
	
	@Test
	public void testOptional(){
		executeParser(new Optional1());
		executeParser(new Optional2());
		executeParser(new Optional3());
	}
	
	@Test
	public void testSequence(){
		executeParser(new Sequence1());
		executeParser(new Sequence2());
		executeParser(new Sequence4());
	}
	
	@Test
	public void testAlternative(){
		executeParser(new Alternative1());
		executeParser(new Alternative2());
	}
	
	@Test
	public void testAmbiguousList(){
		executeParser(new AmbiguousNonTerminalPlusList1());
		executeParser(new AmbiguousNonTerminalPlusList2());
		executeParser(new AmbiguousNestedPlusList());
	}
	
	@Test
	public void testRecursion(){
		executeParser(new RightRecursion());
		executeParser(new LeftRecursion());
		
		executeParser(new EmptyRightRecursion());

		executeParser(new NullableSharing());
	}
	
	@Test
	public void testAmbiguousRecursion(){
		executeParser(new AmbiguousRecursive());
		executeParser(new AmbiguousRecursivePrefixShared());
	}
	
	@Test
	public void testCycle(){
		executeParser(new NotAUselessSelfLoop());
		executeParser(new UselessSelfLoop());
		
		executeParser(new CycleEpsilon());
	}
	
	@Test
	public void testListCycle(){
		executeParser(new EpsilonList());
		executeParser(new AmbiguousEpsilonList());
		executeParser(new AmbiguousSeparatedEpsilonList());
	}
	
	@Test
	public void testListSharing(){
		executeParser(new ListOverlap());
	}

	@Test
	public void testDoubleLeftNullable() {
		executeParser(new DoubleLeftNullable());
	}

	@Test
	public void testDoubleRightNullable() {
		executeParser(new DoubleRightNullableWithPrefixSharing());
	}

	@Test
	public void testPrefixSharedExcept() {
		executeParser(new PrefixSharedExcept());
	}
}
