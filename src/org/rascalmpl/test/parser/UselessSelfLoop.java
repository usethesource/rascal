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
S ::= A | B
A ::= B | a
B ::= A | a
*/
public class UselessSelfLoop extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(Factory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_A = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_B = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B), SYMBOL_START_S, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_B = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_B), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_A, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_A = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_A), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_a), SYMBOL_B, VF.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default, VF.list(SYMBOL_char_a), SYMBOL_a, VF.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_A2 = new NonTerminalStackNode(2, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B3 = new NonTerminalStackNode(3, 0, "B");
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 0, PROD_a_a, new char[]{'a'});
	
	public UselessSelfLoop(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
		
		expect(PROD_S_B, NONTERMINAL_B1);
	}
	
	public void A(){
		expect(PROD_A_B, NONTERMINAL_B3);
		
		expect(PROD_A_a, LITERAL_a4);
	}
	
	public void B(){
		expect(PROD_B_A, NONTERMINAL_A2);
		
		expect(PROD_B_a, LITERAL_a5);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"B\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"A\")],sort(\"B\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"B\")],sort(\"A\"),\\no-attrs()),[cycle(sort(\"B\"),2)])})])})]),appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"B\")],sort(\"A\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"A\")],sort(\"B\"),\\no-attrs()),[cycle(sort(\"A\"),2)])})])})])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		UselessSelfLoop usl = new UselessSelfLoop();
		IConstructor result = usl.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("[S([A([B(cycle(A,2)),B(a)]),A(a)]),S([B([A(cycle(B,2)),A(a)]),B(a)])] <- good");
	}
}
