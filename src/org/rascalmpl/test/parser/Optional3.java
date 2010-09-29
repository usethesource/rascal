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
import org.rascalmpl.parser.sgll.stack.OptionalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= aO? | aA
O ::= A
A ::= a
*/
public class Optional3 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_O = vf.constructor(Factory.Symbol_Sort, vf.string("O"));
	private final static IConstructor SYMBOL_OPTIONAL_O = vf.constructor(Factory.Symbol_Opt, SYMBOL_O);
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_aOPTIONAL_O = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_OPTIONAL_O), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_aA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a, SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_OPTIONAL_O_O = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_O), SYMBOL_OPTIONAL_O, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_O_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_O, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode LITERAL_a0 = new LiteralStackNode(0, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a1 = new LiteralStackNode(1, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a2 = new LiteralStackNode(2, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode NONTERMINAL_A3 = new NonTerminalStackNode(3, "A");
	private final static AbstractStackNode NONTERMINAL_A4 = new NonTerminalStackNode(4, "A");
	private final static AbstractStackNode NON_TERMINAL_O5 = new NonTerminalStackNode(5, "O");
	private final static AbstractStackNode OPTIONAL_6 = new OptionalStackNode(6, PROD_OPTIONAL_O_O, NON_TERMINAL_O5);
	
	public Optional3(){
		super();
	}
	
	public void S(){
		expect(PROD_S_aOPTIONAL_O, LITERAL_a0, OPTIONAL_6);
		
		expect(PROD_S_aA, LITERAL_a1, NONTERMINAL_A3);
	}
	
	public void A(){
		expect(PROD_A_a, LITERAL_a2);
	}
	
	public void O(){
		expect(PROD_O_A, NONTERMINAL_A4);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(amb({appl(prod([lit(\"a\"),sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)]),appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([lit(\"a\"),opt(sort(\"O\"))],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)]),appl(prod([sort(\"O\")],opt(sort(\"O\")),\\no-attrs()),[appl(prod([sort(\"A\")],sort(\"O\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])}),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Optional3 o3 = new Optional3();
		IConstructor result = o3.parse(NONTERMINAL_START_S, null, "aa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(a,O?(O(A(a)))),S(a,A(a))] <- good");
	}
}

