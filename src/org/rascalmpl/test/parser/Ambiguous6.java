package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A | E
A ::= B
B ::= C
C ::= D
D ::= E | a
E ::= F
F ::= G
G ::= a
*/
public class Ambiguous6 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_D = vf.constructor(Factory.Symbol_Sort, vf.string("D"));
	private final static IConstructor SYMBOL_E = vf.constructor(Factory.Symbol_Sort, vf.string("E"));
	private final static IConstructor SYMBOL_F = vf.constructor(Factory.Symbol_Sort, vf.string("F"));
	private final static IConstructor SYMBOL_G = vf.constructor(Factory.Symbol_Sort, vf.string("G"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_E = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_E), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_C = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_D = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_D), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_D_E = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_E), SYMBOL_D, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_D_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_D, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_E_F = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_F), SYMBOL_E, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_F_G = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_G), SYMBOL_F, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_G_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_G, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_C2 = new NonTerminalStackNode(2, 0, "C");
	private final static AbstractStackNode NONTERMINAL_D3 = new NonTerminalStackNode(3, 0, "D");
	private final static AbstractStackNode NONTERMINAL_E4 = new NonTerminalStackNode(4, 0, "E");
	private final static AbstractStackNode NONTERMINAL_E5 = new NonTerminalStackNode(5, 0, "E");
	private final static AbstractStackNode NONTERMINAL_F6 = new NonTerminalStackNode(6, 0, "F");
	private final static AbstractStackNode NONTERMINAL_G7 = new NonTerminalStackNode(7, 0, "G");
	private final static AbstractStackNode LITERAL_a8 = new LiteralStackNode(8, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a9 = new LiteralStackNode(9, 0, PROD_a_a, new char[]{'a'});
	
	public Ambiguous6(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
		
		expect(PROD_S_E, NONTERMINAL_E4);
	}
	
	public void A(){
		expect(PROD_A_B, NONTERMINAL_B1);
	}
	
	public void B(){
		expect(PROD_B_C, NONTERMINAL_C2);
	}
	
	public void C(){
		expect(PROD_C_D, NONTERMINAL_D3);
	}
	
	public void D(){
		expect(PROD_D_E, NONTERMINAL_E5);
		
		expect(PROD_D_a, LITERAL_a8);
	}
	
	public void E(){
		expect(PROD_E_F, NONTERMINAL_F6);
	}
	
	public void F(){
		expect(PROD_F_G, NONTERMINAL_G7);
	}
	
	public void G(){
		expect(PROD_G_a, LITERAL_a9);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"B\")],sort(\"A\"),\\no-attrs()),[appl(prod([sort(\"C\")],sort(\"B\"),\\no-attrs()),[appl(prod([sort(\"D\")],sort(\"C\"),\\no-attrs()),[amb({appl(prod([lit(\"a\")],sort(\"D\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"E\")],sort(\"D\"),\\no-attrs()),[appl(prod([sort(\"F\")],sort(\"E\"),\\no-attrs()),[appl(prod([sort(\"G\")],sort(\"F\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"G\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])})])])])]),appl(prod([sort(\"E\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"F\")],sort(\"E\"),\\no-attrs()),[appl(prod([sort(\"G\")],sort(\"F\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"G\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		Ambiguous6 a6 = new Ambiguous6();
		IConstructor result = a6.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(A(B(C([D(E(F(G(a)))),D(a)])))),S(E(F(G(a))))] <- good");
	}
}
