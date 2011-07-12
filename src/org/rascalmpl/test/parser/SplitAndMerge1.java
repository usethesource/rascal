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
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, 2, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, 0, PROD_a_a, new char[]{'a'});
	
	public SplitAndMerge1(){
		super();
	}
	
	public void S(){
		expect(PROD_S_aAa, LITERAL_a3, NONTERMINAL_A0, LITERAL_a4);
	}
	
	public void A(){
		expect(PROD_A_Ba, NONTERMINAL_B1, LITERAL_a5);
		
		expect(PROD_A_aB, LITERAL_a6, NONTERMINAL_B2);
	}
	
	public void B(){
		expect(PROD_A_a, LITERAL_a7);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaaa".toCharArray(), new NodeToUPTR());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[lit(\"a\"),sort(\"A\"),lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),amb({appl(prod(sort(\"A\"),[lit(\"a\"),sort(\"B\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(prod(sort(\"B\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"A\"),[sort(\"B\"),lit(\"a\")],{}),[appl(prod(sort(\"B\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])}),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		SplitAndMerge1 ms1 = new SplitAndMerge1();
		IConstructor result = ms1.parse(NONTERMINAL_START_S, null, "aaaa".toCharArray(), new NodeToUPTR());
		System.out.println(result);
		
		System.out.println("S(a,[A(a,B(a)),A(B(a),a)],a) <- good");
	}
}
