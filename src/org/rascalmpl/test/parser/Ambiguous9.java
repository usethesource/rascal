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
* S ::= E
* E ::= E + E | E * E | 1
* 
* NOTE: This test, tests prefix sharing.
*/
public class Ambiguous9 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_E = VF.constructor(Factory.Symbol_Sort, VF.string("E"));
	private final static IConstructor SYMBOL_plus = VF.constructor(Factory.Symbol_Lit, VF.string("+"));
	private final static IConstructor SYMBOL_star = VF.constructor(Factory.Symbol_Lit, VF.string("*"));
	private final static IConstructor SYMBOL_1 = VF.constructor(Factory.Symbol_Lit, VF.string("1"));
	private final static IConstructor SYMBOL_char_plus = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(43))));
	private final static IConstructor SYMBOL_char_star = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(42))));
	private final static IConstructor SYMBOL_char_1 = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(49))));
	
	private final static IConstructor PROD_S_E = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_E), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_E_EplusE = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_E, SYMBOL_plus, SYMBOL_E), SYMBOL_E, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_E_EstarE = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_E, SYMBOL_star, SYMBOL_E), SYMBOL_E, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_E_1 = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_1), SYMBOL_E, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_plus_plus = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_plus), SYMBOL_plus, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_star_star = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_star), SYMBOL_star, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_1_1 = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_1), SYMBOL_1, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_E0 = new NonTerminalStackNode(0, 0, "E");
	private final static AbstractStackNode NONTERMINAL_E1 = new NonTerminalStackNode(1, 0, "E");
	private final static AbstractStackNode NONTERMINAL_E2 = new NonTerminalStackNode(2, 2, "E");
	private final static AbstractStackNode NONTERMINAL_E3 = new NonTerminalStackNode(3, 2, "E");
	private final static AbstractStackNode LITERAL_4 = new LiteralStackNode(4, 1, PROD_plus_plus, "+".toCharArray());
	private final static AbstractStackNode LITERAL_5 = new LiteralStackNode(5, 1, PROD_star_star, "*".toCharArray());
	private final static AbstractStackNode LITERAL_6 = new LiteralStackNode(6, 0, PROD_1_1, "1".toCharArray());
	
	
	public Ambiguous9(){
		super();
	}
	
	public void S(){
		expect(PROD_S_E, NONTERMINAL_E0);
	}
	
	public void E(){
		expect(PROD_E_EplusE, NONTERMINAL_E1, LITERAL_4, NONTERMINAL_E2);
		expect(PROD_E_EstarE, NONTERMINAL_E1, LITERAL_5, NONTERMINAL_E3);
		
		expect(PROD_E_1, LITERAL_6);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "1+1+1".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([sort(\"E\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([sort(\"E\"),lit(\"+\"),sort(\"E\")],sort(\"E\"),\\no-attrs()),[appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])]),appl(prod([\\char-class([single(43)])],lit(\"+\"),\\no-attrs()),[char(43)]),appl(prod([sort(\"E\"),lit(\"+\"),sort(\"E\")],sort(\"E\"),\\no-attrs()),[appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])]),appl(prod([\\char-class([single(43)])],lit(\"+\"),\\no-attrs()),[char(43)]),appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])])])]),appl(prod([sort(\"E\"),lit(\"+\"),sort(\"E\")],sort(\"E\"),\\no-attrs()),[appl(prod([sort(\"E\"),lit(\"+\"),sort(\"E\")],sort(\"E\"),\\no-attrs()),[appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])]),appl(prod([\\char-class([single(43)])],lit(\"+\"),\\no-attrs()),[char(43)]),appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])])]),appl(prod([\\char-class([single(43)])],lit(\"+\"),\\no-attrs()),[char(43)]),appl(prod([lit(\"1\")],sort(\"E\"),\\no-attrs()),[appl(prod([\\char-class([single(49)])],lit(\"1\"),\\no-attrs()),[char(49)])])])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Ambiguous9 a9 = new Ambiguous9();
		IConstructor result = a9.parse(NONTERMINAL_START_S, null, "1+1+1".toCharArray());
		System.out.println(result);
		
		System.out.println("S([E(E(1),+,E(E(1),+,E(1))),E(E(E(1),+,E(1)),+,E(1))]) <- good");
	}
}
