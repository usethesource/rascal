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
S ::= Aab | bab
A ::= B
B ::= b
*/
public class Ambiguous2 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_ab = vf.constructor(Factory.Symbol_Lit, vf.string("ab"));
	private final static IConstructor SYMBOL_bab = vf.constructor(Factory.Symbol_Lit, vf.string("bab"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_char_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	
	private final static IConstructor PROD_S_Aab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_ab), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_bab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_bab), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b), SYMBOL_b, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_ab_ab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b, SYMBOL_char_a, SYMBOL_char_b), SYMBOL_ab, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_bab_bab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b, SYMBOL_char_a, SYMBOL_char_b), SYMBOL_bab, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode LITERAL_b2 = new LiteralStackNode(2, 0, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LITERALL_ab3 = new LiteralStackNode(3, 1, PROD_ab_ab, new char[]{'a','b'});
	private final static AbstractStackNode LITERAL_bab4 = new LiteralStackNode(4, 0, PROD_bab_bab, new char[]{'b','a','b'});
	
	public Ambiguous2(){
		super();
	}
	
	public void S(){
		expect(PROD_S_Aab, NONTERMINAL_A0, LITERALL_ab3);
		
		expect(PROD_S_bab, LITERAL_bab4);
	}
	
	public void A(){
		expect(PROD_A_B, NONTERMINAL_B1);
	}
	
	public void B(){
		expect(PROD_B_b, LITERAL_b2);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "bab".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"A\"),lit(\"ab\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"B\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"b\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(98)])],lit(\"b\"),\\no-attrs()),[char(98)])])]),appl(prod([\\char-class([single(98)]),\\char-class([single(97)]),\\char-class([single(98)])],lit(\"ab\"),\\no-attrs()),[char(97),char(98)])]),appl(prod([lit(\"bab\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(98)]),\\char-class([single(97)]),\\char-class([single(98)])],lit(\"bab\"),\\no-attrs()),[char(98),char(97),char(98)])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		Ambiguous2 a2 = new Ambiguous2();
		IConstructor result = a2.parse(NONTERMINAL_START_S, null, "bab".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(bab),S(A(B(b)),ab)] <- good");
	}
}
