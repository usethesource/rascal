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
S ::= AA | B
A ::= CC | a
B ::= AA | CC
C ::= AA | a
*/
public class NotAUselessSelfLoop extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_CC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C, SYMBOL_C), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_CC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C, SYMBOL_C), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode NONTERMINAL_A2 = new NonTerminalStackNode(2, "A");
	private final static AbstractStackNode NONTERMINAL_A3 = new NonTerminalStackNode(3, "A");
	private final static AbstractStackNode NONTERMINAL_A4 = new NonTerminalStackNode(4, "A");
	private final static AbstractStackNode NONTERMINAL_A5 = new NonTerminalStackNode(5, "A");
	private final static AbstractStackNode NONTERMINAL_B6 = new NonTerminalStackNode(6, "B");
	private final static AbstractStackNode NONTERMINAL_C7 = new NonTerminalStackNode(7, "C");
	private final static AbstractStackNode NONTERMINAL_C8 = new NonTerminalStackNode(8, "C");
	private final static AbstractStackNode NONTERMINAL_C9 = new NonTerminalStackNode(9, "C");
	private final static AbstractStackNode NONTERMINAL_C10 = new NonTerminalStackNode(10, "C");
	private final static AbstractStackNode LITERAL_a11 = new LiteralStackNode(11, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a12 = new LiteralStackNode(12, PROD_a_a, new char[]{'a'});
	
	public NotAUselessSelfLoop(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AA, NONTERMINAL_A0, NONTERMINAL_A1);
		
		expect(PROD_S_B, NONTERMINAL_B6);
	}
	
	public void A(){
		expect(PROD_A_CC, NONTERMINAL_C7, NONTERMINAL_C8);
		
		expect(PROD_A_a, LITERAL_a11);
	}
	
	public void B(){
		expect(PROD_B_AA, NONTERMINAL_A2, NONTERMINAL_A3);

		expect(PROD_B_CC, NONTERMINAL_C9, NONTERMINAL_C10);
	}
	
	public void C(){
		expect(PROD_C_AA, NONTERMINAL_A4, NONTERMINAL_A5);
		
		expect(PROD_C_a, LITERAL_a12);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(amb({appl(prod([sort(\"A\"),sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"C\"),sort(\"C\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])]),appl(prod([sort(\"B\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([sort(\"A\"),sort(\"A\")],sort(\"B\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"C\"),sort(\"C\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])]),appl(prod([sort(\"C\"),sort(\"C\")],sort(\"B\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"C\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])]),appl(prod([sort(\"C\"),sort(\"C\")],sort(\"B\"),\\no-attrs()),[appl(prod([sort(\"A\"),sort(\"A\")],sort(\"C\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"B\"),\\no-attrs()),[appl(prod([sort(\"C\"),sort(\"C\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])})]),appl(prod([sort(\"A\"),sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"C\"),sort(\"C\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])}),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		NotAUselessSelfLoop nausl = new NotAUselessSelfLoop();
		IConstructor result = nausl.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(A(a),A(C(a),C(a))),S([B(A(C(a),C(a)),A(a)),B(A(a),A(C(a),C(a))),B(C(a),C(A(a),A(a))),B(C(A(a),A(a)),C(a))]),S(A(C(a),C(a)),A(a))] <- good");
	}
}
