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
S ::= A | E
A ::= B
B ::= C
C ::= D
D ::= E | a
E ::= F
F ::= G
G ::= a
*/
public class Ambiguous6 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(Factory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_D = VF.constructor(Factory.Symbol_Sort, VF.string("D"));
	private final static IConstructor SYMBOL_E = VF.constructor(Factory.Symbol_Sort, VF.string("E"));
	private final static IConstructor SYMBOL_F = VF.constructor(Factory.Symbol_Sort, VF.string("F"));
	private final static IConstructor SYMBOL_G = VF.constructor(Factory.Symbol_Sort, VF.string("G"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_A = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_E = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_E), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_B = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_C = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_C), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_D = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_D), SYMBOL_C, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_D_E = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_E), SYMBOL_D, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_D_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_D, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_E_F = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_F), SYMBOL_E, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_F_G = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_G), SYMBOL_F, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_G_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_G, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_C2 = new NonTerminalStackNode(2, 0, "C");
	private final static AbstractStackNode NONTERMINAL_D3 = new NonTerminalStackNode(3, 0, "D");
	private final static AbstractStackNode NONTERMINAL_E4 = new NonTerminalStackNode(4, 0, "E");
	private final static AbstractStackNode NONTERMINAL_E5 = new NonTerminalStackNode(5, 0, "E");
	private final static AbstractStackNode NONTERMINAL_F6 = new NonTerminalStackNode(6, 0, "F");
	private final static AbstractStackNode NONTERMINAL_G7 = new NonTerminalStackNode(7, 0, "G");
	private final static AbstractStackNode LITERAL_a8 = new LiteralStackNode(8, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a9 = new LiteralStackNode(9, 0, PROD_a_a, new char[]{'a'});
	
	public Ambiguous6(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
		
		expect(PROD_S_E, NONTERMINAL_E4);
	}
	
	public void A(){
		expect(PROD_A_B, NONTERMINAL_B1);
	}
	
	public void B(){
		expect(PROD_B_C, NONTERMINAL_C2);
	}
	
	public void C(){
		expect(PROD_C_D, NONTERMINAL_D3);
	}
	
	public void D(){
		expect(PROD_D_E, NONTERMINAL_E5);
		
		expect(PROD_D_a, LITERAL_a8);
	}
	
	public void E(){
		expect(PROD_E_F, NONTERMINAL_F6);
	}
	
	public void F(){
		expect(PROD_F_G, NONTERMINAL_G7);
	}
	
	public void G(){
		expect(PROD_G_a, LITERAL_a9);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"B\")],sort(\"A\"),\\no-attrs()),[appl(prod([sort(\"C\")],sort(\"B\"),\\no-attrs()),[appl(prod([sort(\"D\")],sort(\"C\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"D\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"E\")],sort(\"D\"),\\no-attrs()),[appl(prod([sort(\"F\")],sort(\"E\"),\\no-attrs()),[appl(prod([sort(\"G\")],sort(\"F\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"G\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])})])])])]),appl(prod([sort(\"E\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"F\")],sort(\"E\"),\\no-attrs()),[appl(prod([sort(\"G\")],sort(\"F\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"G\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		Ambiguous6 a6 = new Ambiguous6();
		IConstructor result = a6.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(A(B(C([D(E(F(G(a)))),D(a)])))),S(E(F(G(a))))] <- good");
	}
}
