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

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;
/*
S ::= aA+ | A+a
A ::= a
*/
@SuppressWarnings({"unchecked", "cast"})
public class AmbiguousNonTerminalPlusList1 extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_PLUS_LIST_A = VF.constructor(RascalValueFactory.Symbol_IterPlus, SYMBOL_A);
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_aPLUSLISTA = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_a, SYMBOL_PLUS_LIST_A), VF.set());
	private final static IConstructor PROD_S_PLUSLISTAa = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_PLUS_LIST_A, SYMBOL_a), VF.set());
	private final static IConstructor PROD_PLUSLISTA = VF.constructor(RascalValueFactory.Production_Regular, SYMBOL_PLUS_LIST_A);
	private final static IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> LIST1 = new ListStackNode<IConstructor>(1, 1, PROD_PLUSLISTA, NONTERMINAL_A0, true);
	private final static AbstractStackNode<IConstructor> LIST2 = new ListStackNode<IConstructor>(2, 0, PROD_PLUSLISTA, NONTERMINAL_A0, true);
	private final static AbstractStackNode<IConstructor> LITERAL_a3 = new LiteralStackNode<IConstructor>(3, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_a4 = new LiteralStackNode<IConstructor>(4, 1, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_a5 = new LiteralStackNode<IConstructor>(5, 0, PROD_a_a, new int[]{'a'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		S_EXPECT_1[0] = LITERAL_a3;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = LIST1;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[1].setAlternativeProduction(PROD_S_aPLUSLISTA);
	}
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		S_EXPECT_2[0] = LIST2;
		S_EXPECT_2[0].setProduction(S_EXPECT_2);
		S_EXPECT_2[1] = LITERAL_a4;
		S_EXPECT_2[1].setProduction(S_EXPECT_2);
		S_EXPECT_2[1].setAlternativeProduction(PROD_S_PLUSLISTAa);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = LITERAL_a5;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setAlternativeProduction(PROD_A_a);
	}
	
	public AmbiguousNonTerminalPlusList1(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0], S_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod(sort(\"S\"),[lit(\"a\"),iter(sort(\"A\"))],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(regular(iter(sort(\"A\"))),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])]),appl(prod(sort(\"S\"),[iter(sort(\"A\")),lit(\"a\")],{}),[appl(regular(iter(sort(\"A\"))),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		AmbiguousNonTerminalPlusList1 nrpl1 = new AmbiguousNonTerminalPlusList1();
		IConstructor result = nrpl1.executeParser();
		System.out.println(result);
		
		System.out.println("[S(a,A+(A(a),A(a))),S(A+(A(a),A(a)),a)] <- good");
	}
}
