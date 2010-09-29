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
S ::= Ab
A ::= aa
*/
public class Simple1 extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_aa = vf.constructor(Factory.Symbol_Lit, vf.string("aa"));
	private final static IConstructor SYMBOL_b = vf.constructor(Factory.Symbol_Lit, vf.string("b"));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	private final static IConstructor SYMBOL_char_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	
	private final static IConstructor PROD_S_Ab = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A, SYMBOL_b), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_aa = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_aa), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_aa_a = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a, SYMBOL_char_a), SYMBOL_aa, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_b_b = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b), SYMBOL_b, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
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
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "aab".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"A\"),lit(\"b\")],sort(\"S\"),\\no-attrs()),[appl(prod([lit(\"aa\")],sort(\"A\"),\\no-attrs()),[appl(prod([\\char-class([single(97)]),\\char-class([single(97)])],lit(\"aa\"),\\no-attrs()),[char(97),char(97)])]),appl(prod([\\char-class([single(98)])],lit(\"b\"),\\no-attrs()),[char(98)])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		Simple1 s1 = new Simple1();
		IConstructor result = s1.parse(NONTERMINAL_START_S, null, "aab".toCharArray());
		System.out.println(result);
		
		System.out.println("S(A(aa),b) <- good");
	}
}
