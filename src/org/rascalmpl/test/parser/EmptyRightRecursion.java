package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= AB
A ::= a
B ::= B | epsilon
*/
public class EmptyRightRecursion extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_epsilon = VF.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_AB = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A, SYMBOL_B), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_B = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_epsilon = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_epsilon), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 1, "B");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, 0, "B");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode EPSILON_4 = new EpsilonStackNode(4, 0);
	
	public EmptyRightRecursion(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AB, NONTERMINAL_A0, NONTERMINAL_B1);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a3);
	}
	
	public void B(){
		expect(PROD_B_B, NONTERMINAL_B2);
		
		expect(PROD_B_epsilon, EPSILON_4);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([sort(\"A\"),sort(\"B\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),amb({appl(prod([sort(\"B\")],sort(\"B\"),\\no-attrs()),[cycle(sort(\"B\"),1)]),appl(prod([empty()],sort(\"B\"),\\no-attrs()),[])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		EmptyRightRecursion erre = new EmptyRightRecursion();
		IConstructor result = erre.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(a),[B(cycle(B,1)),B()])");
	}
}
