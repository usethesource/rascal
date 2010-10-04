package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.CharStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= [a-z]
*/
public class CharRange extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_char_a_z = vf.constructor(Factory.Symbol_CharClass, vf.list(vf.constructor(Factory.CharRange_Range, vf.integer(97), vf.integer(122))));
	
	private final static IConstructor PROD_PLUSLISTa_z_a_z = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_char_a_z), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode CHAR_a0 = new CharStackNode(0, 0, new char[][]{{'a','z'}});
	
	public CharRange(){
		super();
	}
	
	public void S(){
		expect(PROD_PLUSLISTa_z_a_z, CHAR_a0);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([\\char-class([range(97,122)])],sort(\"S\"),\\no-attrs()),[char(97)]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}

	public static void main(String[] args){
		CharRange cr = new CharRange();
		IConstructor result = cr.parse(NONTERMINAL_START_S, null, "a".toCharArray());
		System.out.println(result);
		
		System.out.println("S([a-z](a)) <- good");
	}
}
