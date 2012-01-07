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
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.NodeToUPTR;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= aAa
A ::= Ba | aB
B ::= a
*/
public class SplitAndMerge1 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_aAa = VF.constructor(Factory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_a, SYMBOL_A, SYMBOL_a), VF.set());
	private final static IConstructor PROD_A_Ba = VF.constructor(Factory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_B, SYMBOL_a), VF.set());
	private final static IConstructor PROD_A_aB = VF.constructor(Factory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a, SYMBOL_B), VF.set());
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 1, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, 1, "B");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, 2, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 1, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, 0, PROD_a_a, new int[]{'a'});
	
	private final static AbstractStackNode[] S_EXPECT_1 = new AbstractStackNode[3];
	static{
		S_EXPECT_1[0] = LITERAL_a3;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = NONTERMINAL_A0;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[2] = LITERAL_a4;
		S_EXPECT_1[2].setProduction(S_EXPECT_1);
		S_EXPECT_1[2].setParentProduction(PROD_S_aAa);
	}
	
	private final static AbstractStackNode[] A_EXPECT_1 = new AbstractStackNode[2];
	static{
		A_EXPECT_1[0] = NONTERMINAL_B1;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[1] = LITERAL_a5;
		A_EXPECT_1[1].setProduction(A_EXPECT_1);
		A_EXPECT_1[1].setParentProduction(PROD_A_Ba);
	}
	
	private final static AbstractStackNode[] A_EXPECT_2 = new AbstractStackNode[2];
	static{
		A_EXPECT_2[0] = LITERAL_a6;
		A_EXPECT_2[0].setProduction(A_EXPECT_2);
		A_EXPECT_2[1] = NONTERMINAL_B2;
		A_EXPECT_2[1].setProduction(A_EXPECT_2);
		A_EXPECT_2[1].setParentProduction(PROD_A_aB);
	}
	
	private final static AbstractStackNode[] B_EXPECT_1 = new AbstractStackNode[1];
	static{
		B_EXPECT_1[0] = LITERAL_a7;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[0].setParentProduction(PROD_A_a);
	}
	
	public SplitAndMerge1(){
		super();
	}
	
	public AbstractStackNode[] S(){
		return new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode[] A(){
		return new AbstractStackNode[]{A_EXPECT_1[0], A_EXPECT_2[0]};
	}
	
	public AbstractStackNode[] B(){
		return new AbstractStackNode[]{B_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return (IConstructor) parse(NONTERMINAL_START_S, null, "aaaa".toCharArray(), new NodeToUPTR());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[lit(\"a\"),sort(\"A\"),lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),amb({appl(prod(sort(\"A\"),[lit(\"a\"),sort(\"B\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(prod(sort(\"B\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),lit(\"a\")],{}),[appl(prod(sort(\"B\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])}),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		SplitAndMerge1 ms1 = new SplitAndMerge1();
		IConstructor result = ms1.executeParser();
		System.out.println(result);
		
		System.out.println("S(a,[A(a,B(a)),A(B(a),a)],a) <- good");
	}
}
