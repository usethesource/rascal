package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= ci(bla)

NOTE: ci(*) means whatever * represents is Case Insensitive.
*/
public class CILiteral extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_bla = vf.constructor(Factory.Symbol_CiLit, vf.string("bla"));
	private final static IConstructor SYMBOL_char_b = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(98))));
	private final static IConstructor SYMBOL_char_l = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(108))));
	private final static IConstructor SYMBOL_char_a = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Single, vf.integer(97))));
	
	private final static IConstructor PROD_S_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_bla_bla = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_b, SYMBOL_char_l, SYMBOL_char_a), SYMBOL_bla, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode LITERAL_bla0 = new CaseInsensitiveLiteralStackNode(0, PROD_bla_bla, new char[]{'b','l','a'});
	
	public CILiteral(){
		super();
	}
	
	public void S(){
		expect(PROD_S_A, LITERAL_bla0);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "Bla".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"A\")],sort(\"S\"),\\no-attrs()),[appl(prod([\\char-class([single(98)]),\\char-class([single(108)]),\\char-class([single(97)])],cilit(\"bla\"),\\no-attrs()),[char(66),char(108),char(97)])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		CILiteral cil = new CILiteral();
		IConstructor result = cil.parse(NONTERMINAL_START_S, null, "Bla".toCharArray());
		System.out.println(result);
		
		System.out.println("S(Bla) <- good");
	}
}
