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
* S ::= AB | AC
* A ::= a
* B ::= a
* C ::= a
* 
* NOTE: This test, tests prefix sharing.
*/
@SuppressWarnings({"unchecked", "cast"})
public class Ambiguous8 extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_AB = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_B), VF.set());
	private final static IConstructor PROD_S_AC = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_C), VF.set());
	private final static IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_B_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_C_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_C, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B1 = new NonTerminalStackNode<IConstructor>(1, 1, "B");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C2 = new NonTerminalStackNode<IConstructor>(2, 1, "C");
	private final static AbstractStackNode<IConstructor> LITERAL_a3 = new LiteralStackNode<IConstructor>(3, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_a4 = new LiteralStackNode<IConstructor>(4, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_a5 = new LiteralStackNode<IConstructor>(5, 0, PROD_a_a, new int[]{'a'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECTS;
	static{
		ExpectBuilder<IConstructor> sExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		sExpectBuilder.addAlternative(PROD_S_AB, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_A0, NONTERMINAL_B1});
		sExpectBuilder.addAlternative(PROD_S_AC, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_A0, NONTERMINAL_C2});
		S_EXPECTS = sExpectBuilder.buildExpectArray();
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECTS;
	static{
		ExpectBuilder<IConstructor> aExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		aExpectBuilder.addAlternative(PROD_A_a, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{LITERAL_a3});
		A_EXPECTS = aExpectBuilder.buildExpectArray();
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECTS;
	static{
		ExpectBuilder<IConstructor> bExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		bExpectBuilder.addAlternative(PROD_B_a, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{LITERAL_a4});
		B_EXPECTS = bExpectBuilder.buildExpectArray();
	}
	
	private final static AbstractStackNode<IConstructor>[] C_EXPECTS;
	static{
		ExpectBuilder<IConstructor> cExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		cExpectBuilder.addAlternative(PROD_C_a, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{LITERAL_a5});
		C_EXPECTS = cExpectBuilder.buildExpectArray();
	}
	
	public Ambiguous8(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return S_EXPECTS;
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return A_EXPECTS;
	}
	
	public AbstractStackNode<IConstructor>[] B(){
		return B_EXPECTS;
	}
	
	public AbstractStackNode<IConstructor>[] C(){
		return C_EXPECTS;
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "aa".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"C\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"C\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"S\"),[sort(\"A\"),sort(\"B\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"B\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		Ambiguous8 a8 = new Ambiguous8();
		IConstructor result = a8.executeParser();
		System.out.println(result);
		
		System.out.println("[S(A(a),B(a)),S(A(a),C(a))] <- good");
	}
}
