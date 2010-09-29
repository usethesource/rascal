package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.EpsilonStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
* S ::= N N
* N ::= A
* A ::= epsilon
*/
public class NullableSharing extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_N = vf.constructor(Factory.Symbol_Sort, vf.string("N"));
	private final static IConstructor SYMBOL_empty = vf.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_NN = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_N, SYMBOL_N), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_N_A = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_A), SYMBOL_N, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_empty = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_empty), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode NONTERMINAL_N1 = new NonTerminalStackNode(1, "N");
	private final static AbstractStackNode NONTERMINAL_N2 = new NonTerminalStackNode(2, "N");
	private final static AbstractStackNode EPSILON3 = new EpsilonStackNode(3);
	
	public NullableSharing(){
		super();
	}
	
	public void S(){
		expect(PROD_S_NN, NONTERMINAL_N1, NONTERMINAL_N2);
	}
	
	public void A(){
		expect(PROD_A_empty, EPSILON3);
	}
	
	public void N(){
		expect(PROD_N_A, NONTERMINAL_A0);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([sort(\"N\"),sort(\"N\")],sort(\"S\"),\\no-attrs()),[appl(prod([sort(\"A\")],sort(\"N\"),\\no-attrs()),[appl(prod([empty()],sort(\"A\"),\\no-attrs()),[])]),appl(prod([sort(\"A\")],sort(\"N\"),\\no-attrs()),[appl(prod([empty()],sort(\"A\"),\\no-attrs()),[])])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		NullableSharing ns = new NullableSharing();
		IConstructor result = ns.parse(NONTERMINAL_START_S, null, "".toCharArray());
		System.out.println(result);
		
		System.out.println("S(N(A()),N(A())) <- good");
	}
}
