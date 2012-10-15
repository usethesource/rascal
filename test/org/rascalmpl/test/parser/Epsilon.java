/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= epsilon
*/
@SuppressWarnings("unchecked")
public class Epsilon extends SGTDBF<IConstructor, IConstructor, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_epsilon = VF.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_epsilon = VF.constructor(Factory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_epsilon), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> EPSILON_1 = new EpsilonStackNode<IConstructor>(1, 0);
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = EPSILON_1;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_epsilon);
	}
	
	public Epsilon(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, new char[]{}, new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[empty()],{}),[])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		Epsilon e = new Epsilon();
		IConstructor result = e.executeParser();
		System.out.println(result);
		
		System.out.println("S() <- good");
	}
}
