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
import org.rascalmpl.parser.gtd.stack.OptionalStackNode;
import org.rascalmpl.parser.uptr.NodeToUPTR;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= aO?
O ::= a
*/
public class Optional1 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_O = VF.constructor(Factory.Symbol_Sort, VF.string("O"));
	private final static IConstructor SYMBOL_OPTIONAL_O = VF.constructor(Factory.Symbol_Opt, SYMBOL_O);
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_aOPTIONAL_O = VF.constructor(Factory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_a, SYMBOL_OPTIONAL_O), VF.set());
	private final static IConstructor PROD_OPTIONAL_O_O = VF.constructor(Factory.Production_Default,  SYMBOL_OPTIONAL_O, VF.list(SYMBOL_O), VF.set());
	private final static IConstructor PROD_O_a = VF.constructor(Factory.Production_Default,  SYMBOL_O, VF.list(SYMBOL_a),VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode LITERAL_a0 = new LiteralStackNode(0, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode LITERAL_a1 = new LiteralStackNode(1, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode NON_TERMINAL_O2 = new NonTerminalStackNode(2, 0, "O");
	private final static AbstractStackNode OPTIONAL_3 = new OptionalStackNode(3, 1, PROD_OPTIONAL_O_O, NON_TERMINAL_O2);
	
	private final static AbstractStackNode[] S_EXPECT_1 = new AbstractStackNode[2];
	static{
		S_EXPECT_1[0] = LITERAL_a0;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = OPTIONAL_3;
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[1].setParentProduction(PROD_S_aOPTIONAL_O);
	}
	
	private final static AbstractStackNode[] O_EXPECT_1 = new AbstractStackNode[1];
	static{
		O_EXPECT_1[0] = LITERAL_a1;
		O_EXPECT_1[0].setProduction(O_EXPECT_1);
		O_EXPECT_1[0].setParentProduction(PROD_O_a);
	}
	
	public Optional1(){
		super();
	}
	
	public AbstractStackNode[] S(){
		return new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode[] O(){
		return new AbstractStackNode[]{O_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return (IConstructor) parse(NONTERMINAL_START_S, null, "aa".toCharArray(), new NodeToUPTR());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[lit(\"a\"),opt(sort(\"O\"))],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(prod(opt(sort(\"O\")),[sort(\"O\")],{}),[appl(prod(sort(\"O\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		Optional1 o1 = new Optional1();
		IConstructor result = o1.executeParser();
		System.out.println(result);
		
		System.out.println("S(a,O?(O(a))) <- good");
	}
}
