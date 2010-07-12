package org.rascalmpl.test.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= Aab | bab
A ::= B
B ::= b
*/
public class Ambiguous2 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_ab = vf.constructor(Factory.Symbol_Lit, vf.string("ab"));
	private final static IConstructor SYMBOL_bab = vf.constructor(Factory.Symbol_Lit, vf.string("bab"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_char_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	
	private final static IConstructor PROD_S_Aab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_ab), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_S_bab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_bab), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b), SYMBOL_B, vf.list(Factory.Attributes));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b), SYMBOL_b, vf.list(Factory.Attributes));
	private final static IConstructor PROD_ab_ab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b, SYMBOL_char_a, SYMBOL_char_b), SYMBOL_ab, vf.list(Factory.Attributes));
	private final static IConstructor PROD_bab_bab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b, SYMBOL_char_a, SYMBOL_char_b), SYMBOL_bab, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, "B");
	private final static AbstractStackNode LITERAL_b2 = new LiteralStackNode(2, PROD_b_b, new char[]{'b'});
	private final static AbstractStackNode LITERALL_ab3 = new LiteralStackNode(3, PROD_ab_ab, new char[]{'a','b'});
	private final static AbstractStackNode LITERAL_bab4 = new LiteralStackNode(4, PROD_bab_bab, new char[]{'b','a','b'});
	
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
	
	public IValue parse(IConstructor start, char[] input){
		throw new UnsupportedOperationException();
	}
	
	public IValue parse(IConstructor start, File inputFile) throws IOException{
		throw new UnsupportedOperationException();
	}
	
	public IValue parse(IConstructor start, InputStream in) throws IOException{
		throw new UnsupportedOperationException();
	}
	
	public IValue parse(IConstructor start, Reader in) throws IOException{
		throw new UnsupportedOperationException();
	}
	
	public IValue parse(IConstructor start, String input){
		throw new UnsupportedOperationException();
	}
	
	public static void main(String[] args){
		Ambiguous2 a2 = new Ambiguous2();
		IValue result = a2.parse(NONTERMINAL_START_S, "bab".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(bab),S(A(B(b)),ab)] <- good");
	}
}
