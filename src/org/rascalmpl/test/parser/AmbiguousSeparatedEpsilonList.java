package org.rascalmpl.test.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.parser.uptr.NodeToUPTR;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class AmbiguousSeparatedEpsilonList extends SGTDBF implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(Factory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(Factory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_SEP = VF.constructor(Factory.Symbol_Sort, VF.string("Sep"));
	private final static IConstructor SYMBOL_PLUS_LIST_SEP_A = VF.constructor(Factory.Symbol_IterSepX, SYMBOL_A, VF.list(SYMBOL_SEP));
	private final static IConstructor SYMBOL_a = VF.constructor(Factory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(Factory.Symbol_CharClass, VF.list(VF.constructor(Factory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_epsilon = VF.constructor(Factory.Symbol_Empty);
	
	private final static IConstructor PROD_S_PLUSLISTA = VF.constructor(Factory.Production_Default, SYMBOL_START_S, VF.list(SYMBOL_PLUS_LIST_SEP_A), VF.set());
	private final static IConstructor PROD_SEP_epsilon = VF.constructor(Factory.Production_Default, SYMBOL_SEP, VF.list(SYMBOL_epsilon), VF.set());
	private final static IConstructor PROD_PLUSLISTSEPA = VF.constructor(Factory.Production_Regular, SYMBOL_PLUS_LIST_SEP_A);
	private final static IConstructor PROD_A_a = VF.constructor(Factory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(Factory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private final static IConstructor PROD_A_epsilon = VF.constructor(Factory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_epsilon), VF.set());
	
	private final static AbstractStackNode NONTERMINAL_START_S = new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode NONTERMINAL_A0 = new NonTerminalStackNode(0, 0, "A");
	private final static AbstractStackNode NONTERMINAL_SEP1 = new NonTerminalStackNode(1, 1, "SEP");
	private final static AbstractStackNode LIST2 = new SeparatedListStackNode(2, 0, PROD_PLUSLISTSEPA, NONTERMINAL_A0, new AbstractStackNode[]{NONTERMINAL_SEP1}, true);
	private final static AbstractStackNode LITERAL_a3 = new LiteralStackNode(3, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode EPSILON4 = new EpsilonStackNode(4, 0);
	private final static AbstractStackNode EPSILON5 = new EpsilonStackNode(5, 0);
	
	private final static AbstractStackNode[] S_EXPECT_1 = new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = LIST2;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setParentProduction(PROD_S_PLUSLISTA);
	}
	
	private final static AbstractStackNode[] A_EXPECT_1 = new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = LITERAL_a3;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setParentProduction(PROD_A_a);
	}
	
	private final static AbstractStackNode[] A_EXPECT_2 = new AbstractStackNode[1];
	static{
		A_EXPECT_2[0] = EPSILON4;
		A_EXPECT_2[0].setProduction(A_EXPECT_2);
		A_EXPECT_2[0].setParentProduction(PROD_A_epsilon);
	}
	
	private final static AbstractStackNode[] SEP_EXPECT_1 = new AbstractStackNode[1];
	static{
		SEP_EXPECT_1[0] = EPSILON5;
		SEP_EXPECT_1[0].setProduction(SEP_EXPECT_1);
		SEP_EXPECT_1[0].setParentProduction(PROD_SEP_epsilon);
	}
	
	public AmbiguousSeparatedEpsilonList(){
		super();
	}
	
	public AbstractStackNode[] S(){
		return new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode[] A(){
		return new AbstractStackNode[]{A_EXPECT_1[0], A_EXPECT_2[0]};
	}
	
	public AbstractStackNode[] SEP(){
		return new AbstractStackNode[]{SEP_EXPECT_1[0]};
	}
	
	public IConstructor executeParser(){
		return (IConstructor) parse(NONTERMINAL_START_S, null, "a".toCharArray(), new NodeToUPTR());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[\\iter-seps(sort(\"A\"),[sort(\"Sep\")])],{}),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[]),amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)})])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), Factory.uptr, Factory.Tree, new ByteArrayInputStream(expectedInput.getBytes()));
	}
	
	public static void main(String[] args){
		AmbiguousSeparatedEpsilonList asel = new AmbiguousSeparatedEpsilonList();
		IConstructor result = asel.executeParser();
		System.out.println(result);
		
		System.out.println("S([(ASEP)+([(ASEP)+(A(a)),(ASEP)+(A(),repeat(SEP(),A()),SEP(),A(a))],SEP(),A(),repeat(SEP(),A())),(ASEP)+(A(),repeat(SEP(),A()),SEP(),A(a)),(ASEP)+(A(a))]) <- good, but not minimal");
	}
}
