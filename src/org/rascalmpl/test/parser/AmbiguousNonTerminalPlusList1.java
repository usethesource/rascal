package org.rascalmpl.test.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= aA+ | A+a
A ::= a
*/
public class AmbiguousNonTerminalPlusList1 extends SGLL{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_PLUS_LIST_A = vf.constructor(Factory.Symbol_IterPlus, vf.string("A"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_aPLUSLISTA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_PLUS_LIST_A), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_S_PLUSLISTAa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_A, SYMBOL_a), SYMBOL_START_S, vf.list(Factory.Attributes));
	private final static IConstructor PROD_PLUSLISTA_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_PLUS_LIST_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.list(Factory.Attributes));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.list(Factory.Attributes));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_A1 = new NonTerminalStackNode(1, "A");
	private final static AbstractStackNode LIST2 = new ListStackNode(2, PROD_PLUSLISTA_A, NONTERMINAL_A0, true);
	private final static AbstractStackNode LIST3 = new ListStackNode(3, PROD_PLUSLISTA_A, NONTERMINAL_A1, true);
	private final static AbstractStackNode LITERAL_a4 = new LiteralStackNode(4, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, PROD_a_a, new char[]{'a'});
	
	public AmbiguousNonTerminalPlusList1(){
		super();
	}
	
	public void S(){
		expect(PROD_S_aPLUSLISTA, LITERAL_a4, LIST2);
		
		expect(PROD_S_PLUSLISTAa, LIST3, LITERAL_a5);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a6);
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
		AmbiguousNonTerminalPlusList1 nrpl1 = new AmbiguousNonTerminalPlusList1();
		IValue result = nrpl1.parse(NONTERMINAL_START_S, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(a,A+(A(a),A+(A(a)))),S(A+(A(a),A+(A(a))),a)] <- good");
	}
}
