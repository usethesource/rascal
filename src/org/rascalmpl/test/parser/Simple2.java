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
S ::= AB
A ::= a
B ::= b
*/
public class Simple2 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A0 = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B1 = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_a2 = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_b3 = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	
	private final static IConstructor PROD_S_AB = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A0, SYMBOL_B1), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a2), SYMBOL_A0, vf.list(Factory.Attributes));
	private final static IConstructor PROD_B_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b3), SYMBOL_B1, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_a2, vf.list(Factory.Attributes));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b), SYMBOL_b3, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, "B");
	private final static AbstractStackNode LITERAL_a2 = new LiteralStackNode(2, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_b3 = new LiteralStackNode(3, PROD_b_b, new char[]{'b'});
	
	public Simple2(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AB, NONTERMINAL_A0, NONTERMINAL_B1);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a2);
	}
	
	public void B(){
		expect(PROD_B_b, LITERAL_b3);
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
		Simple2 s2 = new Simple2();
		IValue result = s2.parse(NONTERMINAL_START_S, "ab".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(a),B(b)) <- good");
	}
}