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

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
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
S ::= A!c | C
A ::= c: C | CB
B ::= b
C ::= c
*/
@SuppressWarnings({"unchecked"})
public class PrefixSharedExcept extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IntegerKeyedHashMap<IntegerList> dontNest = new IntegerKeyedHashMap<IntegerList>();
	private final static IntegerMap resultStoreIdMappings = new IntegerMap();

	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	private final static IConstructor SYMBOL_char_c = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(99))));
	
	private final static IConstructor PROD_S_A = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_START_S, VF.list(SYMBOL_A), VF.set());
	private final static IConstructor PROD_S_C = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_START_S, VF.list(SYMBOL_C), VF.set());
	private final static IConstructor PROD_A_CB = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_A, VF.list(SYMBOL_C, SYMBOL_B), VF.set());
	private final static IConstructor PROD_A_C = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_C, VF.list(SYMBOL_C), VF.set());
	private final static IConstructor PROD_B_b = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_B, VF.list(SYMBOL_char_b), VF.set());
	private final static IConstructor PROD_C_c = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_C, VF.list(SYMBOL_char_c), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C1 = new NonTerminalStackNode<IConstructor>(1, 0, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C2 = new NonTerminalStackNode<IConstructor>(2, 0, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_C3 = new NonTerminalStackNode<IConstructor>(3, 0, "C");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B4 = new NonTerminalStackNode<IConstructor>(4, 1, "B");

	private final static AbstractStackNode<IConstructor> LITERAL_b = new LiteralStackNode<IConstructor>(5, 0, PROD_B_b, new int[]{'b'});
	private final static AbstractStackNode<IConstructor> LITERAL_c = new LiteralStackNode<IConstructor>(6, 0, PROD_C_c, new int[]{'c'});

	static {
		IntegerList parents = new IntegerList();
		parents.add(NONTERMINAL_A0.getId());
		dontNest.put(NONTERMINAL_C2.getId(), parents);

		resultStoreIdMappings.put(NONTERMINAL_A0.getId(), 0);
	}
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = NONTERMINAL_A0;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_A);
	}
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_2[0] = NONTERMINAL_C1;
		S_EXPECT_2[0].setProduction(S_EXPECT_2);
		S_EXPECT_2[0].setAlternativeProduction(PROD_S_C);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECTS;
	static{
		ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(dontNest, resultStoreIdMappings);

		AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
		A_EXPECT_1[0] = NONTERMINAL_C2;
		builder.addAlternative(PROD_A_C, A_EXPECT_1);

		AbstractStackNode<IConstructor>[] A_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
		A_EXPECT_2[0] = NONTERMINAL_C3;
		A_EXPECT_2[1] = NONTERMINAL_B4;
		builder.addAlternative(PROD_A_CB, A_EXPECT_2);

		A_EXPECTS = builder.buildExpectArray();
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		B_EXPECT_1[0] = LITERAL_b;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[0].setAlternativeProduction(PROD_B_b);
	}
	
	private final static AbstractStackNode<IConstructor>[] C_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		C_EXPECT_1[0] = LITERAL_c;
		C_EXPECT_1[0].setProduction(C_EXPECT_1);
		C_EXPECT_1[0].setAlternativeProduction(PROD_C_c);
	}
	
	public PrefixSharedExcept(){
		super();
	}

	protected int getResultStoreId(int parentId) {
		return resultStoreIdMappings.get(parentId);
	}

	protected boolean hasNestingRestrictions(String name){
		return true;
	}

	protected IntegerList getFilteredParents(int childId) {
		return dontNest.get(childId);
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0], S_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return A_EXPECTS;
	}
	
	public AbstractStackNode<IConstructor>[] B(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{B_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] C(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{C_EXPECT_1[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "c".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[sort(\"C\")],{}),[appl(prod(sort(\"C\"),[\\char-class([single(99)])],{}),[appl(prod(sort(\"C\"),[\\char-class([single(99)])],{}),[char(99)])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		PrefixSharedExcept pse = new PrefixSharedExcept();
		IConstructor result = pse.executeParser();
		System.out.println(result);
		
		System.out.println("appl(prod(sort(\"S\"),[sort(\"C\")],{}),[appl(prod(sort(\"C\"),[\\char-class([single(99)])],{}),[appl(prod(sort(\"C\"),[\\char-class([single(99)])],{}),[char(99)])])]) <- good");
	}
}
