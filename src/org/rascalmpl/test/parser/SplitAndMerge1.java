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
S ::= aAa
A ::= Ba | aB
B ::= a
*/
public class SplitAndMerge1 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_aAa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_A, SYMBOL_a), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_Ba = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_B, SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_aB = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_B), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_B, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, "B");
	private final static AbstractStackNode NONTERMINAL_B2 = new NonTerminalStackNode(2, "B");
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a7 = new LiteralStackNode(7, PROD_a_a, new char[]{'a'});
	
	public SplitAndMerge1(){
		super();
	}
	
	public void S(){
		expect(PROD_S_aAa, LITERAL_a3, NONTERMINAL_A0, LITERAL_a4);
	}
	
	public void A(){
		expect(PROD_A_Ba, NONTERMINAL_B1, LITERAL_a5);
		
		expect(PROD_A_aB, LITERAL_a6, NONTERMINAL_B2);
	}
	
	public void B(){
		expect(PROD_A_a, LITERAL_a7);
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
	
	public boolean executeTest(){
		SplitAndMerge1 ms1 = new SplitAndMerge1();
		IValue result = ms1.parse(NONTERMINAL_START_S, "aaaa".toCharArray());
		return result.equals("TODO");
	}

	public static void main(String[] args){
		SplitAndMerge1 ms1 = new SplitAndMerge1();
		IValue result = ms1.parse(NONTERMINAL_START_S, "aaaa".toCharArray());
		System.out.println(result);
		
		System.out.println("S(a,[A(a,B(a)),A(B(a),a)],a) <- good");
	}
}
