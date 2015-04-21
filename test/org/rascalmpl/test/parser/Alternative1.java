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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.AlternativeStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

@SuppressWarnings({"unchecked", "cast"})
public class Alternative1 extends SGTDBF<IConstructor, IConstructor, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_b = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	private final static IConstructor SYMBOL_c = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("c"));
	private final static IConstructor SYMBOL_char_c = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(99))));
	private final static IConstructor SYMBOL_ALT_bc = VF.constructor(RascalValueFactory.Symbol_Alt, VF.set(SYMBOL_b, SYMBOL_c));
	private final static IConstructor SYMBOL_d = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("d"));
	private final static IConstructor SYMBOL_char_d = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(100))));
	
	private final static IConstructor PROD_S_a_ALTbc_d = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_a, SYMBOL_ALT_bc, SYMBOL_d), VF.set());
	private final static IConstructor PROD_ALTbc = VF.constructor(RascalValueFactory.Production_Regular, SYMBOL_ALT_bc);
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private final static IConstructor PROD_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_b, VF.list(SYMBOL_char_b), VF.set());
	private final static IConstructor PROD_c_c = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_c, VF.list(SYMBOL_char_c), VF.set());
	private final static IConstructor PROD_d_d = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_d, VF.list(SYMBOL_char_d), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
	static{
		S_EXPECT_1[0] = new LiteralStackNode<IConstructor>(1, 0, PROD_a_a, new int[]{'a'});
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[1] = new AlternativeStackNode<IConstructor>(4, 1, PROD_ALTbc, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(2, 0, PROD_b_b, new int[]{'b'}), new LiteralStackNode<IConstructor>(3, 1, PROD_c_c, new int[]{'c'})});
		S_EXPECT_1[1].setProduction(S_EXPECT_1);
		S_EXPECT_1[2] = new LiteralStackNode<IConstructor>(5, 2, PROD_d_d, new int[]{'d'});
		S_EXPECT_1[2].setProduction(S_EXPECT_1);
		S_EXPECT_1[2].setAlternativeProduction(PROD_S_a_ALTbc_d);
	}
	
	public Alternative1(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "abd".toCharArray(), new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[lit(\"a\"),alt({lit(\"b\"),lit(\"c\")}),lit(\"d\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(regular(alt({lit(\"b\"),lit(\"c\")})),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(lit(\"d\"),[\\char-class([single(100)])],{}),[char(100)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		Alternative1 a1 = new Alternative1();
		IConstructor result = a1.executeParser();
		System.out.println(result);
	}
}
