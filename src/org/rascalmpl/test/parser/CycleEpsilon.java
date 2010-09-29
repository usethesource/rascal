package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.EpsilonStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A
A ::= AA | epsilon | a
*/
public class CycleEpsilon extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_epsilon = vf.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_epsilon = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_epsilon), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode NONTERMINAL_A2 = new NonTerminalStackNode(2, "A");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode EPSILON_4 = new EpsilonStackNode(4);
	
	public CycleEpsilon(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
	}
	
	public void A(){
		expect(PROD_A_AA, NONTERMINAL_A1, NONTERMINAL_A2);
		
		expect(PROD_A_a, LITERAL_a3);
		
		expect(PROD_A_epsilon, EPSILON_4);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[amb({appl(prod([empty()],sort(\"A\"),\\no-attrs()),[]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[cycle(sort(\"A\"),1),cycle(sort(\"A\"),1)])}),cycle(sort(\"A\"),1)]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[cycle(sort(\"A\"),1),amb({appl(prod([empty()],sort(\"A\"),\\no-attrs()),[]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"A\"),\\no-attrs()),[cycle(sort(\"A\"),1),cycle(sort(\"A\"),1)])})])})]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		CycleEpsilon ce = new CycleEpsilon();
		IConstructor result = ce.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("S([A([A(cycle(A,1),cycle(A,1)),A()],cycle(A,1)),A(cycle(A,1),[A(cycle(A,1),cycle(A,1)),A()]),A(a)]) <- good");
	}
}
