package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.EpsilonStackNode;
import org.rascalmpl.parser.sgll.stack.IReducableStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.LiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= AC
A ::= B+ !+ a
B ::= a
C ::= a | epsilon

'X !+ Y' means, 'X' does not match when followed by 'Y'.
*/
public class FollowRestriction extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_B = vf.constructor(Factory.Symbol_Sort, vf.string("B"));
	private final static IConstructor SYMBOL_C = vf.constructor(Factory.Symbol_Sort, vf.string("C"));
	private final static IConstructor SYMBOL_PLUS_LIST_B = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_B);
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_epsilon = vf.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_AC = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_C), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_PLUSLISTB = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_B), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTB = vf.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_B);
	private final static IConstructor PROD_B_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_B, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_C_epsilon = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_epsilon), SYMBOL_C, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_B1 = new NonTerminalStackNode(1, "B");
	private final static AbstractStackNode NONTERMINAL_C2 = new NonTerminalStackNode(2, "C");
	private final static AbstractStackNode LIST4 = new ListStackNode(4, PROD_PLUSLISTB, NONTERMINAL_B1, true);
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode LITERAL_a6 = new LiteralStackNode(6, PROD_a_a, new char[]{'a'});
	private final static AbstractStackNode EPSILON_7 = new EpsilonStackNode(7);
	
	private final static IReducableStackNode LITERAL_a99 = new LiteralStackNode(99, PROD_a_a, new char[]{'a'});
	
	public FollowRestriction(){
		super();
	}
	
	public void S(){
		expect(PROD_S_AC, NONTERMINAL_A0, NONTERMINAL_C2);
	}
	
	public void A(){
		expect(PROD_A_PLUSLISTB, new IReducableStackNode[]{LITERAL_a99}, LIST4);
	}
	
	public void B(){
		expect(PROD_B_a, LITERAL_a5);
	}
	
	public void C(){
		expect(PROD_C_a, LITERAL_a6);
		
		expect(PROD_C_epsilon, EPSILON_7);
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
	
	public IValue executeParser(){
		return parse(NONTERMINAL_START_S, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"A\"),sort(\"C\")],sort(\"S\"),\\no-attrs()),[appl(prod([iter(sort(\"B\"))],sort(\"A\"),\\no-attrs()),[appl(regular(iter(sort(\"B\"))),[appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(regular(iter(sort(\"B\"))),[appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(regular(iter(sort(\"B\"))),[appl(prod([lit(\"a\")],sort(\"B\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])])])]),appl(prod([empty()],sort(\"C\"),\\no-attrs()),[])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		FollowRestriction fr = new FollowRestriction();
		IValue result = fr.parse(NONTERMINAL_START_S, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(B+(a,B+(a,B+(a)))),C()) <- good");
	}
}
