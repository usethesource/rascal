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

/*
S ::= AA
A ::= BB
B ::= bb | b
*/
@SuppressWarnings({"unchecked", "cast"})
public class Ambiguous4 extends SGTDBF<IConstructor, IConstructor, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_b = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_bb = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("bb"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	
	private final static IConstructor PROD_S_AA = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_A), VF.set());
	private final static IConstructor PROD_A_BB = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_B, SYMBOL_B), VF.set());
	private final static IConstructor PROD_B_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_b), VF.set());
	private final static IConstructor PROD_B_bb = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_bb), VF.set());
	private final static IConstructor PROD_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_b, VF.list(SYMBOL_char_b), VF.set());
	private final static IConstructor PROD_bb_bb = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_bb, VF.list(SYMBOL_char_b, SYMBOL_char_b), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A1 = new NonTerminalStackNode<IConstructor>(1, 1, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B2 = new NonTerminalStackNode<IConstructor>(2, 0, "B");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B3 = new NonTerminalStackNode<IConstructor>(3, 1, "B");
	private final static AbstractStackNode<IConstructor> LITERAL_b4 = new LiteralStackNode<IConstructor>(4, 0, PROD_b_b, new int[]{'b'});
	private final static AbstractStackNode<IConstructor> LITERAL_bb5 = new LiteralStackNode<IConstructor>(5, 0, PROD_bb_bb, new int[]{'b','b'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		S_EXPECT_1[0] = NONTERMINAL_A0;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = NONTERMINAL_A1;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[1].setAlternativeProduction(PROD_S_AA);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		A_EXPECT_1[0] = NONTERMINAL_B2;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[1] = NONTERMINAL_B3;
		A_EXPECT_1[1].setProduction(A_EXPECT_1);
		A_EXPECT_1[1].setAlternativeProduction(PROD_A_BB);
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		B_EXPECT_1[0] = LITERAL_b4;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[0].setAlternativeProduction(PROD_B_b);
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		B_EXPECT_2[0] = LITERAL_bb5;
		B_EXPECT_2[0].setProduction(B_EXPECT_2);
		B_EXPECT_2[0].setAlternativeProduction(PROD_B_bb);
	}
	
	public Ambiguous4(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] B(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{B_EXPECT_1[0], B_EXPECT_2[0]};
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "bbbbbb".toCharArray(), new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])]),appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])])])]),appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"A\")],{}),[amb({appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])]),appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])])])}),amb({appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])]),appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])])])})]),appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"A\")],{}),[appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),sort(\"B\")],{}),[appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])]),appl(prod(sort(\"B\"),[lit(\"bb\")],{}),[appl(prod(lit(\"bb\"),[\\char-class([single(98)]),\\char-class([single(98)])],{}),[char(98),char(98)])])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		Ambiguous4 a4 = new Ambiguous4();
		IConstructor result = a4.executeParser();
		System.out.println(result);
		
		System.out.println("[S(A(B(bb),B(bb)),A(B(b),B(b))),S(A(B(b),B(b)),A(B(bb),B(bb))),S([A(B(b),B(bb)),A(B(bb),B(b))],[A(B(b),B(bb)),A(B(bb),B(b))])] <- good");
	}
}
