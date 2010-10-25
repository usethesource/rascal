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
S ::= A
A ::= BB
B ::= aa | a
*/
public class Ambiguous5 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_aa = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_BB = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B, SYMBOL_B), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_B_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_aa), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_aa_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a, SYMBOL_char_a), SYMBOL_aa, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, 0, "B");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, 1, "B");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_aa4 = new LiteralStackNode(4, 0, PROD_aa_aa, new char[]{'a','a'});
	
	public Ambiguous5(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);
	}
	
	public void A(){
		expect(PROD_A_BB, NONTERMINAL_B1, NONTERMINAL_B2);
	}
	
	public void B(){
		expect(PROD_B_a, LITERAL_a3);
		
		expect(PROD_B_aa, LITERAL_aa4);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[amb({appl(prod([sort(\"B\"),sort(\"B\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"aa\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)]),\\char-class([single(97)])],lit(\"aa\"),\\no-attrs()),[char(97),char(97)])])]),appl(prod([sort(\"B\"),sort(\"B\")],sort(\"A\"),\\no-attrs()),[appl(prod([lit(\"aa\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)]),\\char-class([single(97)])],lit(\"aa\"),\\no-attrs()),[char(97),char(97)])]),appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])})]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		Ambiguous5 a5 = new Ambiguous5();
		IConstructor result = a5.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("S([A(B(a),B(aa)),A(B(aa),B(a))]) <- good");
	}
}
