package org.rascalmpl.test.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.CharStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A+
A ::= [a]+
*/
public class AmbiguousNestedPlusList extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_PLUS_LIST_A = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_A);
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_PLUS_LIST_a = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_char_a);
	
	private final static IConstructor PROD_S_PLUSLISTA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTA = vf.constructor(Factory.Production_List, vf.list(SYMBOL_A), vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_PLUSLISTa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTa_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_PLUS_LIST_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode LIST1 = new ListStackNode(1, PROD_PLUSLISTA, NONTERMINAL_A0, true);
	private final static AbstractStackNode CHAR2 = new CharStackNode(2, new char[][]{{'a', 'a'}});
	private final static AbstractStackNode CHAR_LIST3 = new ListStackNode(3, PROD_PLUSLISTa_a, CHAR2, true);
	
	public AmbiguousNestedPlusList(){
		super();
	}
	
	public void S(){
		expect(PROD_S_PLUSLISTA, LIST1);
	}
	
	public void A(){
		expect(PROD_A_PLUSLISTa, CHAR_LIST3);
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
		AmbiguousNestedPlusList anpl = new AmbiguousNestedPlusList();
		IValue result = anpl.parse(NONTERMINAL_START_S, "aa".toCharArray());
		return result.equals("TODO");
	}

	public static void main(String[] args){
		AmbiguousNestedPlusList anpl = new AmbiguousNestedPlusList();
		IValue result = anpl.parse(NONTERMINAL_START_S, "aa".toCharArray());
		System.out.println(result);
		
		System.out.println("S([A+(A([a]+([a](a))),A+(A([a]+([a](a))))),A+(A([a]+([a](a),[a]+([a](a)))))]) <- good");
	}
}
