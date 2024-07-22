/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
/*
* S ::= E
* E ::= E + E | E * E | 1
* 
* NOTE: This test, tests prefix sharing.
*/
@SuppressWarnings({"unchecked", "cast"})
public class Ambiguous9 extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_E = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("E"));
	private final static IConstructor SYMBOL_plus = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("+"));
	private final static IConstructor SYMBOL_star = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("*"));
	private final static IConstructor SYMBOL_1 = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("1"));
	private final static IConstructor SYMBOL_char_plus = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(43))));
	private final static IConstructor SYMBOL_char_star = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(42))));
	private final static IConstructor SYMBOL_char_1 = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(49))));
	
	private final static IConstructor PROD_S_E = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_E), VF.set());
	private final static IConstructor PROD_E_EplusE = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_E, VF.list(SYMBOL_E, SYMBOL_plus, SYMBOL_E), VF.set());
	private final static IConstructor PROD_E_EstarE = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_E, VF.list(SYMBOL_E, SYMBOL_star, SYMBOL_E), VF.set());
	private final static IConstructor PROD_E_1 = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_E, VF.list(SYMBOL_1), VF.set());
	private final static IConstructor PROD_plus_plus = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_plus, VF.list(SYMBOL_char_plus), VF.set());
	private final static IConstructor PROD_star_star = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_star, VF.list(SYMBOL_char_star), VF.set());
	private final static IConstructor PROD_1_1 = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_1, VF.list(SYMBOL_char_1), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_E0 = new NonTerminalStackNode<IConstructor>(0, 0, "E");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_E1 = new NonTerminalStackNode<IConstructor>(1, 0, "E");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_E2 = new NonTerminalStackNode<IConstructor>(2, 2, "E");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_E3 = new NonTerminalStackNode<IConstructor>(3, 2, "E");
	private final static AbstractStackNode<IConstructor> LITERAL_4 = new LiteralStackNode<IConstructor>(4, 1, PROD_plus_plus, new int[] {'+'});
	private final static AbstractStackNode<IConstructor> LITERAL_5 = new LiteralStackNode<IConstructor>(5, 1, PROD_star_star, new int[] {'*'});
	private final static AbstractStackNode<IConstructor> LITERAL_6 = new LiteralStackNode<IConstructor>(6, 0, PROD_1_1, new int[] {'1'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECTS;
	static{
		ExpectBuilder<IConstructor> sExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		sExpectBuilder.addAlternative(PROD_S_E, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_E0});
		S_EXPECTS = sExpectBuilder.buildExpectArray();
	}
	
	private final static AbstractStackNode<IConstructor>[] E_EXPECTS;
	static{
		ExpectBuilder<IConstructor> eExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		eExpectBuilder.addAlternative(PROD_E_EplusE, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_E1, LITERAL_4, NONTERMINAL_E2});
		eExpectBuilder.addAlternative(PROD_E_EstarE, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_E1, LITERAL_5, NONTERMINAL_E3});
		eExpectBuilder.addAlternative(PROD_E_1, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{LITERAL_6});
		E_EXPECTS = eExpectBuilder.buildExpectArray();
	}
	
	public Ambiguous9(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return S_EXPECTS;
	}
	
	public AbstractStackNode<IConstructor>[] E(){
		return E_EXPECTS;
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "1+1+1".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[sort(\"E\")],{}),[amb({appl(prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"E\")],{}),[appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])]),appl(prod(lit(\"+\"),[\\char-class([single(43)])],{}),[char(43)]),appl(prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"E\")],{}),[appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])]),appl(prod(lit(\"+\"),[\\char-class([single(43)])],{}),[char(43)]),appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])])])]),appl(prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"E\")],{}),[appl(prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"E\")],{}),[appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])]),appl(prod(lit(\"+\"),[\\char-class([single(43)])],{}),[char(43)]),appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])])]),appl(prod(lit(\"+\"),[\\char-class([single(43)])],{}),[char(43)]),appl(prod(sort(\"E\"),[lit(\"1\")],{}),[appl(prod(lit(\"1\"),[\\char-class([single(49)])],{}),[char(49)])])])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		Ambiguous9 a9 = new Ambiguous9();
		IConstructor result = a9.executeParser();
		System.out.println(result);
		
		System.out.println("S([E(E(1),+,E(E(1),+,E(1))),E(E(E(1),+,E(1)),+,E(1))]) <- good");
	}
}
