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
S ::= AA
A ::= aa | a
*/
public class Ambiguous3 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_aa = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_AA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_A), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_aa), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.list(Factory.Attributes));
	private final static IConstructor PROD_aa_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a, SYMBOL_char_a), SYMBOL_aa, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode LITERAL_a2 = new LiteralStackNode(2, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_aa3 = new LiteralStackNode(3, PROD_aa_aa, new char[]{'a','a'});
	
	public Ambiguous3(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AA, NONTERMINAL_A0, NONTERMINAL_A1);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a2);
		
		expect(PROD_A_aa, LITERAL_aa3);
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
		Ambiguous3 a3 = new Ambiguous3();
		IValue result = a3.parse(NONTERMINAL_START_S, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(A(a),A(aa)),S(A(aa),A(a))] <- good");
	}
}
