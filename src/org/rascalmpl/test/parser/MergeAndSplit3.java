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
S ::= A | C
A ::= Ba | a
B ::= Aa | a
C ::= B
*/
public class MergeAndSplit3 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_S_C = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_Ba = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B, SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_Aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_a), SYMBOL_B, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_B, vf.list(Factory.Attributes));
	private final static IConstructor PROD_C_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_C, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, "B");
	private final static AbstractStackNode NONTERMINAL_B3 = new NonTerminalStackNode(3, "B");
	private final static AbstractStackNode NONTERMINAL_C4 = new NonTerminalStackNode(4, "C");
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, PROD_a_a, new char[]{'a'});
	
	public MergeAndSplit3(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, NONTERMINAL_A0);

		expect(PROD_S_C, NONTERMINAL_C4);
	}
	
	public void A(){
		expect(PROD_A_Ba, NONTERMINAL_B2, LITERAL_a6);
		
		expect(PROD_A_a, LITERAL_a5);
	}
	
	public void B(){
		expect(PROD_B_Aa, NONTERMINAL_A1, LITERAL_a7);
		
		expect(PROD_B_a, LITERAL_a5);
	}
	
	public void C(){
		expect(PROD_C_B, NONTERMINAL_B3);
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
		MergeAndSplit3 ms3 = new MergeAndSplit3();
		IValue result = ms3.parse(NONTERMINAL_START_S, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(C(B(A(B(a),a),a))),S(A(B(A(a),a),a))] <- good");
	}
}
