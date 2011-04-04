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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A | C
A ::= Ba | a
B ::= Aa | a
C ::= B
*/
public class SplitAndMerge3 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(Factory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_A = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_C = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_C), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_Ba = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B, SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_Aa = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A, SYMBOL_a), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_B = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B), SYMBOL_C, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, 0, "B");
	private final static AbstractStackNode NONTERMINAL_B3 = new NonTerminalStackNode(3, 0, "B");
	private final static AbstractStackNode NONTERMINAL_C4 = new NonTerminalStackNode(4, 0, "C");
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a8 = new LiteralStackNode(8, 0, PROD_a_a, new char[]{'a'});
	
	public SplitAndMerge3(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);

		expect(PROD_S_C, NONTERMINAL_C4);
	}
	
	public void A(){
		expect(PROD_A_Ba, NONTERMINAL_B2, LITERAL_a5);
		
		expect(PROD_A_a, LITERAL_a6);
	}
	
	public void B(){
		expect(PROD_B_Aa, NONTERMINAL_A1, LITERAL_a7);
		
		expect(PROD_B_a, LITERAL_a8);
	}
	
	public void C(){
		expect(PROD_C_B, NONTERMINAL_B3);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"B\"),lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([sort(\"A\"),lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([sort(\"C\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"B\")],sort(\"C\"),\\no-attrs()),[appl(prod([sort(\"A\"),lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([sort(\"B\"),lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		SplitAndMerge3 ms3 = new SplitAndMerge3();
		IConstructor result = ms3.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(C(B(A(B(a),a),a))),S(A(B(A(a),a),a))] <- good");
	}
}
