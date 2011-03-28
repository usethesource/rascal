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
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
* S ::= AB | AC
* A ::= a
* B ::= a
* C ::= a
* 
* NOTE: This test, tests prefix sharing.
*/
public class Ambiguous8 extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_C = VF.constructor(Factory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_AB = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A, SYMBOL_B), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_AC = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A, SYMBOL_C), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_C, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 1, "B");
	private final static AbstractStackNode NONTERMINAL_C2 = new NonTerminalStackNode(2, 1, "C");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 0, PROD_a_a, new char[]{'a'});
	
	public Ambiguous8(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AB, NONTERMINAL_A0, NONTERMINAL_B1);
		
		expect(PROD_S_AC, NONTERMINAL_A0, NONTERMINAL_C2);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a3);
	}
	
	public void B(){
		expect(PROD_B_a, LITERAL_a4);
		
	}
	
	public void C(){
		expect(PROD_C_a, LITERAL_a5);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"A\"),sort(\"C\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([sort(\"A\"),sort(\"B\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Ambiguous8 a8 = new Ambiguous8();
		IConstructor result = a8.parse(NONTERMINAL_START_S, null, "aa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(A(a),B(a)),S(A(a),C(a))] <- good");
	}
}
