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
S ::= A
A ::= aA | a
*/
public class RightRecursion extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_A = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_aA = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a, SYMBOL_A), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, 1, "A");
	private final static AbstractStackNode LITERAL_a2 = new LiteralStackNode(2, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	
	public RightRecursion(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
	}
	
	public void A(){
		expect(PROD_A_aA, LITERAL_a2, NONTERMINAL_A1);
		
		expect(PROD_A_a, LITERAL_a3);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)]),appl(prod([lit(\"a\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		RightRecursion rr = new RightRecursion();
		IConstructor result = rr.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(a,A(a,A(a)))) <- good");
	}
}
