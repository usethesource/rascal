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
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.ITree;
/*
S ::= AA | B
A ::= CC | a
B ::= AA | CC
C ::= AA | a
*/
@SuppressWarnings({"unchecked", "cast"})
public class NotAUselessSelfLoop extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_AA = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_A), VF.set());
	private final static IConstructor PROD_S_B = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_B), VF.set());
	private final static IConstructor PROD_A_CC = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_C, SYMBOL_C), VF.set());
	private final static IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_B_AA = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_A, SYMBOL_A), VF.set());
	private final static IConstructor PROD_B_CC = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_C, SYMBOL_C), VF.set());
	private final static IConstructor PROD_C_AA = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_C, VF.list(SYMBOL_A, SYMBOL_A), VF.set());
	private final static IConstructor PROD_C_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_C, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A1 = new NonTerminalStackNode<IConstructor>(1, 1, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A2 = new NonTerminalStackNode<IConstructor>(2, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A3 = new NonTerminalStackNode<IConstructor>(3, 1, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A4 = new NonTerminalStackNode<IConstructor>(4, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A5 = new NonTerminalStackNode<IConstructor>(5, 1, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B6 = new NonTerminalStackNode<IConstructor>(6, 0, "B");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C7 = new NonTerminalStackNode<IConstructor>(7, 0, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C8 = new NonTerminalStackNode<IConstructor>(8, 1, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C9 = new NonTerminalStackNode<IConstructor>(9, 0, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C10 = new NonTerminalStackNode<IConstructor>(10, 1, "C");
	private final static AbstractStackNode<IConstructor> LITERAL_a11 = new LiteralStackNode<IConstructor>(11, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_a12 = new LiteralStackNode<IConstructor>(12, 0, PROD_a_a, new int[]{'a'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		S_EXPECT_1[0] = NONTERMINAL_A0;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = NONTERMINAL_A1;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[1].setAlternativeProduction(PROD_S_AA);
	}
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_2[0] = NONTERMINAL_B6;
		S_EXPECT_2[0].setProduction(S_EXPECT_2);
		S_EXPECT_2[0].setAlternativeProduction(PROD_S_B);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		A_EXPECT_1[0] = NONTERMINAL_C7;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[1] = NONTERMINAL_C8;
		A_EXPECT_1[1].setProduction(A_EXPECT_1);
		A_EXPECT_1[1].setAlternativeProduction(PROD_A_CC);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_2[0] = LITERAL_a11;
		A_EXPECT_2[0].setProduction(A_EXPECT_2);
		A_EXPECT_2[0].setAlternativeProduction(PROD_A_a);
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		B_EXPECT_1[0] = NONTERMINAL_A2;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[1] = NONTERMINAL_A3;
		B_EXPECT_1[1].setProduction(B_EXPECT_1);
		B_EXPECT_1[1].setAlternativeProduction(PROD_B_AA);
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		B_EXPECT_2[0] = NONTERMINAL_C9;
		B_EXPECT_2[0].setProduction(B_EXPECT_2);
		B_EXPECT_2[1] = NONTERMINAL_C10;
		B_EXPECT_2[1].setProduction(B_EXPECT_2);
		B_EXPECT_2[1].setAlternativeProduction(PROD_B_CC);
	}
	
	private final static AbstractStackNode<IConstructor>[] C_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		C_EXPECT_1[0] = NONTERMINAL_A4;
		C_EXPECT_1[0].setProduction(C_EXPECT_1);
		C_EXPECT_1[1] = NONTERMINAL_A5;
		C_EXPECT_1[1].setProduction(C_EXPECT_1);
		C_EXPECT_1[1].setAlternativeProduction(PROD_C_AA);
	}
	
	private final static AbstractStackNode<IConstructor>[] C_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		C_EXPECT_2[0] = LITERAL_a12;
		C_EXPECT_2[0].setProduction(C_EXPECT_2);
		C_EXPECT_2[0].setAlternativeProduction(PROD_C_a);
	}
	public NotAUselessSelfLoop(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0], S_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0], A_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] B(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{B_EXPECT_1[0], B_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] C(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{C_EXPECT_1[0], C_EXPECT_2[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])]),appl(prod(sort(\"S\"),[sort(\"B\")],{}),[amb({appl(prod(sort(\"B\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])]),appl(prod(sort(\"B\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])]),appl(prod(sort(\"B\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"B\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])})]),appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[sort(\"C\"),sort(\"C\")],{}),[appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		NotAUselessSelfLoop nausl = new NotAUselessSelfLoop();
		IConstructor result = nausl.executeParser();
		System.out.println(result);
		
		System.out.println("[S(A(a),A(C(a),C(a))),S([B(A(C(a),C(a)),A(a)),B(A(a),A(C(a),C(a))),B(C(a),C(A(a),A(a))),B(C(A(a),A(a)),C(a))]),S(A(C(a),C(a)),A(a))] <- good");
	}
}
