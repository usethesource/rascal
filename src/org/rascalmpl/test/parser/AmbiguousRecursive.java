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
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= SSS | SS | a
*/
public class AmbiguousRecursive extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_a = vf.constructor(Factory.Symbol_Lit, vf.string("a"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_SSS = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_S, SYMBOL_S, SYMBOL_S), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_SS = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_S, SYMBOL_S), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_S_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_a), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_a_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a), SYMBOL_a, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_S0 = new NonTerminalStackNode(0, 0, "S");
	private final static AbstractStackNode NONTERMINAL_S1 = new NonTerminalStackNode(1, 1, "S");
	private final static AbstractStackNode NONTERMINAL_S2 = new NonTerminalStackNode(2, 2, "S");
	private final static AbstractStackNode NONTERMINAL_S3 = new NonTerminalStackNode(3, 0, "S");
	private final static AbstractStackNode NONTERMINAL_S4 = new NonTerminalStackNode(4, 1, "S");
	private final static AbstractStackNode LITERAL_a5 = new LiteralStackNode(5, 0, PROD_a_a, new char[]{'a'});
	
	public AmbiguousRecursive(){
		super();
	}
	
	public void S(){
		expect(PROD_S_SSS, NONTERMINAL_S0, NONTERMINAL_S1, NONTERMINAL_S2);
		
		expect(PROD_S_SS, NONTERMINAL_S3, NONTERMINAL_S4);
		
		expect(PROD_S_a, LITERAL_a5);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod([sort(\"S\"),sort(\"S\"),sort(\"S\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([sort(\"S\"),sort(\"S\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([sort(\"S\"),sort(\"S\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])]),appl(prod([sort(\"S\"),sort(\"S\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"S\"),sort(\"S\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])]),appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])]),appl(prod([lit(\"a\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(97)])],lit(\"a\"),\\no-attrs()),[char(97)])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		AmbiguousRecursive ar = new AmbiguousRecursive();
		IConstructor result = ar.parse(NONTERMINAL_START_S, null, "aaa".toCharArray());
		System.out.println(result);
		
		System.out.println("[S(S(a),S(a),S(a)),S(S(a),S(S(a),S(a))),S(S(S(a),S(a)),S(a))] <- good");
	}
}
