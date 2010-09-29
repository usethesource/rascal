package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.SGLL;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.EpsilonStackNode;
import org.rascalmpl.parser.sgll.stack.ListStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/*
S ::= A+
A ::= epsilon
*/
public class EpsilonList extends SGLL implements IParserTest{
	private final static IConstructor SYMBOL_START_S = vf.constructor(Factory.Symbol_Sort, vf.string("S"));
	private final static IConstructor SYMBOL_A = vf.constructor(Factory.Symbol_Sort, vf.string("A"));
	private final static IConstructor SYMBOL_PLUS_LIST_A = vf.constructor(Factory.Symbol_IterPlus, SYMBOL_A);
	private final static IConstructor SYMBOL_epsilon = vf.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_PLUSLISTA = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_PLUS_LIST_A), SYMBOL_START_S, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_PLUSLISTA = vf.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_A, vf.constructor(Factory.Attributes_NoAttrs));
	private final static IConstructor PROD_A_epsilon = vf.constructor(Factory.Production_Default, vf.list(SYMBOL_epsilon), SYMBOL_A, vf.constructor(Factory.Attributes_NoAttrs));
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, "A");
	private final static AbstractStackNode LIST1 = new ListStackNode(1, PROD_PLUSLISTA, NONTERMINAL_A0, true);
	private final static AbstractStackNode EPSILON2 = new EpsilonStackNode(3);
	
	public EpsilonList(){
		super();
	}
	
	public void S(){
		expect(PROD_S_PLUSLISTA, LIST1);
	}
	
	public void A(){
		expect(PROD_A_epsilon, EPSILON2);
	}
	
	public IConstructor executeParser(){
		return parse(NONTERMINAL_START_S, null, "".toCharArray());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "parsetree(appl(prod([iter(sort(\"A\"))],sort(\"S\"),\\no-attrs()),[appl(regular(iter(sort(\"A\")),\\no-attrs()),[amb({appl(regular(iter(sort(\"A\")),\\no-attrs()),[appl(prod([empty()],sort(\"A\"),\\no-attrs()),[])]),cycle(iter(sort(\"A\")),1)})])]),-1)";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.ParseTree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		EpsilonList el = new EpsilonList();
		IConstructor result = el.parse(NONTERMINAL_START_S, null, "".toCharArray());
		System.out.println(result);
		
		System.out.println("S([A+(A()),A+(repeat(A())))]) <- good");
	}
}
