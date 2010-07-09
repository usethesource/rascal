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

public class Simple1 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A0 = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_aa1 = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_b2 = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	
	private final static IConstructor PROD_S_Ab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A0, SYMBOL_b2), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_aa1), SYMBOL_A0, vf.list(Factory.Attributes));
	private final static IConstructor PROD_aa_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_aa1, vf.list(Factory.Attributes));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_b), SYMBOL_b2, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(-1, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode LITERAL_aa1 = new LiteralStackNode(1, PROD_aa_a, new char[]{'a','a'});
	private final static AbstractStackNode LITERAL_b2 = new LiteralStackNode(2, PROD_b_b, new char[]{'b'});
	
	public Simple1(){
		super();
	}
	
	public void S(){
		expect(PROD_S_Ab, NONTERMINAL_A0, LITERAL_b2);
	}
	
	public void A(){
		expect(PROD_A_aa, LITERAL_aa1);
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
		Simple1 s1 = new Simple1();
		IValue result = s1.parse(NONTERMINAL_START_S, "aab".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(aa),b) <- good");
	}
}
