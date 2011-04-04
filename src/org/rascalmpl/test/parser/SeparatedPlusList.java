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
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= sep(A, b)+
A ::= a

sep(X, Y) means, a list of X separated by Y's.
*/
public class SeparatedPlusList extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_b = VF.constructor(Factory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_SEP_PLUS_LIST_A = VF.constructor(Factory.Symbol_IterSepX, SYMBOL_A, Factory.Symbols.make(VF,SYMBOL_b));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_char_b = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(98))));
	
	private final static IConstructor PROD_S_SEPPLUSLIST_A_b = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_SEP_PLUS_LIST_A), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_SEPPLUSLIST_A_b = VF.constructor(Factory.Production_Regular, SYMBOL_SEP_PLUS_LIST_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_b_b = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_b), SYMBOL_b, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode LITERAL_b1 = new LiteralStackNode(1, 1, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LIST2 = new SeparatedListStackNode(2, 0, PROD_SEPPLUSLIST_A_b, NONTERMINAL_A0, new AbstractStackNode[]{LITERAL_b1}, true);
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	
	public SeparatedPlusList(){
		super();
	}
	
	public void S(){
		expect(PROD_S_SEPPLUSLIST_A_b, LIST2);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a3);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "ababa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([\\iter-seps(sort(\"A\"),[lit(\"b\")])],sort(\"S\"),\\no-attrs()),[appl(regular(\\iter-seps(sort(\"A\"),[lit(\"b\")]),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(98)])],lit(\"b\"),\\no-attrs()),[char(98)]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([\\char-class([single(98)])],lit(\"b\"),\\no-attrs()),[char(98)]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		SeparatedPlusList nrpl = new SeparatedPlusList();
		IConstructor result = nrpl.parse(NONTERMINAL_START_S, null, "ababa".toCharArray());
		System.out.println(result);
		
		System.out.println("S((Ab)+(A(a),b,A(a),b,A(a))) <- good");
	}
}
