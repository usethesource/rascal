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
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= [a-z]
*/
public class CharRange extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_char_a_z = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Range, VF.integer(97), VF.integer(122))));
	
	private final static IConstructor PROD_PLUSLISTa_z_a_z = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a_z), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode CHAR_a0 = new CharStackNode(0, 0, new char[][]{{'a','z'}});
	
	public CharRange(){
		super();
	}
	
	public void S(){
		expect(PROD_PLUSLISTa_z_a_z, CHAR_a0);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([\\char-class([range(97,122)])],sort(\"S\"),\\no-attrs()),[char(97)])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		CharRange cr = new CharRange();
		IConstructor result = cr.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("S([a-z](a)) <- good");
	}
}
