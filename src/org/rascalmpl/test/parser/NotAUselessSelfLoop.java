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
S ::= AA | B
A ::= CC | a
B ::= AA | CC
C ::= AA | a
*/
public class NotAUselessSelfLoop extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_S_B = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_CC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C, SYMBOL_C), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_CC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_C, SYMBOL_C), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_C_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_C, vf.list(Factory.Attributes));
	private final static IConstructor PROD_C_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_C, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, "B");
	private final static AbstractStackNode NONTERMINAL_C3 = new NonTerminalStackNode(3, "C");
	private final static AbstractStackNode NONTERMINAL_C4 = new NonTerminalStackNode(4, "C");
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, PROD_a_a, new char[]{'a'});
	
	public NotAUselessSelfLoop(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AA, NONTERMINAL_A0, NONTERMINAL_A1);
		
		expect(PROD_S_B, NONTERMINAL_B2);
	}
	
	public void A(){
		expect(PROD_A_CC, NONTERMINAL_C3, NONTERMINAL_C4);
		
		expect(PROD_A_a, LITERAL_a5);
	}
	
	public void B(){
		expect(PROD_B_AA, NONTERMINAL_A0, NONTERMINAL_A1);

		expect(PROD_B_CC, NONTERMINAL_C3, NONTERMINAL_C4);
	}
	
	public void C(){
		expect(PROD_C_AA, NONTERMINAL_A0, NONTERMINAL_A1);
		
		expect(PROD_C_a, LITERAL_a5);
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
		NotAUselessSelfLoop nausl = new NotAUselessSelfLoop();
		IValue result = nausl.parse(NONTERMINAL_START_S, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S([B(C(A(a),A(a)),C(a)),B(C(a),C(A(a),A(a))),B(A(a),A(C(a),C(a))),B(A(C(a),C(a)),A(a))]),S(A(a),A(C(a),C(a))),S(A(C(a),C(a)),A(a))] <- good");
	}
}
