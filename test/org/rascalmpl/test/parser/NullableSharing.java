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
import org.rascalmpl.values.uptr.RascalValueFactory;

/*
* S ::= N N
* N ::= A
* A ::= epsilon
*/
@SuppressWarnings({"unchecked", "cast"})
public class NullableSharing extends SGTDBF<IConstructor, IConstructor, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_N = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("N"));
	private final static IConstructor SYMBOL_empty = VF.constructor(RascalValueFactory.Symbol_Empty);
	
	private final static IConstructor PROD_S_NN = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_N, SYMBOL_N), VF.set());
	private final static IConstructor PROD_N_A = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_N, VF.list(SYMBOL_A), VF.set());
	private final static IConstructor PROD_A_empty = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_empty), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_N1 = new NonTerminalStackNode<IConstructor>(1, 0, "N");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_N2 = new NonTerminalStackNode<IConstructor>(2, 1, "N");
	private final static AbstractStackNode<IConstructor> EPSILON3 = new EpsilonStackNode<IConstructor>(3, 0);
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		S_EXPECT_1[0] = NONTERMINAL_N1;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = NONTERMINAL_N2;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[1].setAlternativeProduction(PROD_S_NN);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = EPSILON3;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setAlternativeProduction(PROD_A_empty);
	}
	
	private final static AbstractStackNode<IConstructor>[] N_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		N_EXPECT_1[0] = NONTERMINAL_A0;
		N_EXPECT_1[0].setProduction(N_EXPECT_1);
		N_EXPECT_1[0].setAlternativeProduction(PROD_N_A);
	}
	
	public NullableSharing(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] N(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{N_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "".toCharArray(), new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[sort(\"N\"),sort(\"N\")],{}),[appl(prod(sort(\"N\"),[sort(\"A\")],{}),[appl(prod(sort(\"A\"),[empty()],{}),[])]),appl(prod(sort(\"N\"),[sort(\"A\")],{}),[appl(prod(sort(\"A\"),[empty()],{}),[])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		NullableSharing ns = new NullableSharing();
		IConstructor result = ns.executeParser();
		System.out.println(result);
		
		System.out.println("S(N(A()),N(A())) <- good");
	}
}
