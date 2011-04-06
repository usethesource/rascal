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
import org.rascalmpl.parser.gtd.stack.CharStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= [a-z]*
*/
public class CharStarList extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_char_a_z = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Range, VF.integer(97), VF.integer(122))));
	private final static IConstructor SYMBOL_STAR_LIST_a_z = VF.constructor(Factory.Symbol_IterStar, SYMBOL_char_a_z);
	
	private final static IConstructor PROD_S_STARLISTa_z = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_STAR_LIST_a_z), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_STARLISTa_z = VF.constructor(Factory.Production_Regular, SYMBOL_STAR_LIST_a_z, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode CHAR0 = new CharStackNode(0, 0, new char[][]{{'a', 'z'}});
	private final static AbstractStackNode LIST1 = new ListStackNode(1, 0, PROD_STARLISTa_z, CHAR0, false);
	
	public CharStarList(){
		super();
	}
	
	public void S(){
		expect(PROD_S_STARLISTa_z, LIST1);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "abc".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([\\iter-star(\\char-class([range(97,122)]))],sort(\"S\"),\\no-attrs()),[appl(regular(\\iter-star(\\char-class([range(97,122)])),\\no-attrs()),[char(97),char(98),char(99)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		CharStarList csl = new CharStarList();
		IConstructor result = csl.parse(NONTERMINAL_START_S, null, "abc".toCharArray());
		System.out.println(result);
		
		System.out.println("S([a-z]*([a-z](a),[a-z](b),[a-z](c))) <- good");
	}
}
