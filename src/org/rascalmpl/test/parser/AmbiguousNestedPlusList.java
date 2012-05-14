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
import org.rascalmpl.parser.gtd.stack.CharStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.NodeToUPTR;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A+
A ::= [a]+
*/
public class AmbiguousNestedPlusList extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_PLUS_LIST_A = VF.constructor(Factory.Symbol_IterPlus, SYMBOL_A);
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_PLUS_LIST_a = VF.constructor(Factory.Symbol_IterPlus, SYMBOL_char_a);
	
	private final static IConstructor PROD_S_PLUSLISTA = VF.constructor(Factory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_PLUS_LIST_A), VF.set());
	private final static IConstructor PROD_PLUSLISTA = VF.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_A);
	private final static IConstructor PROD_A_PLUSLISTa = VF.constructor(Factory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_PLUS_LIST_a), VF.set());
	private final static IConstructor PROD_PLUSLISTa = VF.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_a);
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode LIST1 = new ListStackNode(1, 0, PROD_PLUSLISTA, NONTERMINAL_A0, true);
	private final static AbstractStackNode CHAR2 = new CharStackNode(2, 0, new int[][]{{'a', 'a'}});
	private final static AbstractStackNode CHAR_LIST3 = new ListStackNode(3, 0, PROD_PLUSLISTa, CHAR2, true);
	
	private final static AbstractStackNode[] S_EXPECT_1 = new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = LIST1;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_PLUSLISTA);
	}
	
	private final static AbstractStackNode[] A_EXPECT_1 = new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = CHAR_LIST3;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setAlternativeProduction(PROD_A_PLUSLISTa);
	}
	
	public AmbiguousNestedPlusList(){
		super();
	}
	
	public AbstractStackNode[] S(){
		return new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode[] A(){
		return new AbstractStackNode[]{A_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return (IConstructor) parse(NONTERMINAL_START_S, null, "aa".toCharArray(), new NodeToUPTR());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[iter(sort(\"A\"))],{}),[amb({appl(regular(iter(sort(\"A\"))),[appl(prod(sort(\"A\"),[iter(\\char-class([single(97)]))],{}),[appl(regular(iter(\\char-class([single(97)]))),[char(97)])]),appl(prod(sort(\"A\"),[iter(\\char-class([single(97)]))],{}),[appl(regular(iter(\\char-class([single(97)]))),[char(97)])])]),appl(regular(iter(sort(\"A\"))),[appl(prod(sort(\"A\"),[iter(\\char-class([single(97)]))],{}),[appl(regular(iter(\\char-class([single(97)]))),[char(97),char(97)])])])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		AmbiguousNestedPlusList anpl = new AmbiguousNestedPlusList();
		IConstructor result = anpl.executeParser();
		System.out.println(result);
		
		System.out.println("S([A+(A([a]+([a](a))),A([a]+([a](a)))),A+(A([a]+([a](a),[a](a))))]) <- good");
	}
}
