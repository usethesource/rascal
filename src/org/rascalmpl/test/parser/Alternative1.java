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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.AlternativeStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class Alternative1 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_b = VF.constructor(Factory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(98))));
	private final static IConstructor SYMBOL_c = VF.constructor(Factory.Symbol_Lit, VF.string("c"));
	private final static IConstructor SYMBOL_char_c = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(99))));
	private final static IConstructor SYMBOL_ALT_bc = VF.constructor(Factory.Symbol_Alt, VF.set(SYMBOL_b, SYMBOL_c));
	private final static IConstructor SYMBOL_d = VF.constructor(Factory.Symbol_Lit, VF.string("d"));
	private final static IConstructor SYMBOL_char_d = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(100))));
	
	private final static IConstructor PROD_S_a_ALTbc_d = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a, SYMBOL_ALT_bc, SYMBOL_d), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_ALTbc = VF.constructor(Factory.Production_Regular, SYMBOL_ALT_bc, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_b_b = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_b), SYMBOL_b, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_c_c = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_c), SYMBOL_c, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_d_d = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_d), SYMBOL_d, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode LITERAL_a1 = new LiteralStackNode(1, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_b2 = new LiteralStackNode(2, 0, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LITERAL_c3 = new LiteralStackNode(3, 1, PROD_c_c, new char[]{'c'});
	private final static AbstractStackNode ALT4 = new AlternativeStackNode(4, 1, PROD_ALTbc, new AbstractStackNode[]{LITERAL_b2, LITERAL_c3});
	private final static AbstractStackNode LITERAL_d5 = new LiteralStackNode(5, 2, PROD_d_d, new char[]{'d'});
	
	public Alternative1(){
		super();
	}
	
	public void S(){
		expect(PROD_S_a_ALTbc_d, LITERAL_a1, ALT4, LITERAL_d5);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "abd".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([lit(\"a\"),alt({lit(\"b\"),lit(\"c\")}),lit(\"d\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)]),appl(regular(alt({lit(\"b\"),lit(\"c\")}),\\no-attrs()),[appl(prod([\\char-class([single(98)])],lit(\"b\"),\\no-attrs()),[char(98)])]),appl(prod([\\char-class([single(100)])],lit(\"d\"),\\no-attrs()),[char(100)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Alternative1 a1 = new Alternative1();
		IConstructor result = a1.parse(NONTERMINAL_START_S, null, "abd".toCharArray());
		System.out.println(result);
	}
}