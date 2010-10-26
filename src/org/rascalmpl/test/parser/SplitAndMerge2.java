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
S ::= D | Da
D ::= C
C ::= Baa | Ba
B ::= A
A ::= a
*/
public class SplitAndMerge2 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_D = vf.constructor(Factory.Symbol_Sort, vf.string("D"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_aa = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_D = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_D), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_Da = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_D, SYMBOL_a), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_D_C = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C), SYMBOL_D, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_Ba = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B, SYMBOL_aa), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_Baa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B, SYMBOL_a), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_aa_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a, SYMBOL_char_a), SYMBOL_aa, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, 0, "B");
	private final static AbstractStackNode NONTERMINAL_C3 = new NonTerminalStackNode(3, 0, "C");
	private final static AbstractStackNode NONTERMINAL_D4 = new NonTerminalStackNode(4, 0, "D");
	private final static AbstractStackNode NONTERMINAL_D5 = new NonTerminalStackNode(5, 0, "D");
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a8 = new LiteralStackNode(8, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_aa9 = new LiteralStackNode(9, 1, PROD_aa_aa, new char[]{'a','a'});
	
	public SplitAndMerge2(){
		super();
	}
	
	public void S(){
		expect(PROD_S_D, NONTERMINAL_D4);
		
		expect(PROD_S_Da, NONTERMINAL_D5, LITERAL_a6);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a7);
	}
	
	public void B(){
		expect(PROD_B_A, NONTERMINAL_A0);
	}
	
	public void C(){
		expect(PROD_C_Ba, NONTERMINAL_B1, LITERAL_a8);
		
		expect(PROD_C_Baa, NONTERMINAL_B2, LITERAL_aa9);
	}
	
	public void D(){
		expect(PROD_D_C, NONTERMINAL_C3);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"D\"),lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"C\")],sort(\"D\"),\\no-attrs()),[appl(prod([sort(\"B\"),lit(\"aa\")],sort(\"C\"),\\no-attrs()),[appl(prod([sort(\"A\")],sort(\"B\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"D\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"C\")],sort(\"D\"),\\no-attrs()),[appl(prod([sort(\"B\"),lit(\"a\")],sort(\"C\"),\\no-attrs()),[appl(prod([sort(\"A\")],sort(\"B\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([\\char-class([single(97)]),\\char-class([single(97)])],lit(\"aa\"),\\no-attrs()),[char(97),char(97)])])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		SplitAndMerge2 ms2 = new SplitAndMerge2();
		IConstructor result = ms2.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(D(C(B(A(a)),aa))),S(D(C(B(A(a)),a)),a)] <- good");
	}
}
