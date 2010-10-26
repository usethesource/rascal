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
S ::= aa | Aa
A ::= a | reject(a)
*/
public class Reject4 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_aa = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_aa), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_Aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_a), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_aa_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a, SYMBOL_char_a), SYMBOL_aa, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode LITERAL_aa2 = new LiteralStackNode(2, 0, PROD_aa_a, new char[]{'a', 'a'});
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, 0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 0, PROD_a_a, new char[]{'a'});
	
	public Reject4(){
		super();
	}
	
	public void S(){
		expect(PROD_S_aa, LITERAL_aa2);
		
		expect(PROD_S_Aa, NONTERMINAL_A0, LITERAL_a3);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a4);
		
		expectReject(PROD_A_a, LITERAL_a5);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod([lit(\"aa\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)]),\\char-class([single(97)])],lit(\"aa\"),\\no-attrs()),[char(97),char(97)])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Reject4 r4 = new Reject4();
		IConstructor result = r4.parse(NONTERMINAL_START_S, null, "aa".toCharArray());
		System.out.println(result);
		
		System.out.println("S(aa) <- good");
	}
}
