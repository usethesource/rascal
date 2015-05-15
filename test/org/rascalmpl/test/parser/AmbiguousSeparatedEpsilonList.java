package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.ITree;
@SuppressWarnings({"unchecked", "cast"})
public class AmbiguousSeparatedEpsilonList extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_SEP = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("Sep"));
	private final static IConstructor SYMBOL_PLUS_LIST_SEP_A = VF.constructor(RascalValueFactory.Symbol_IterSepX, SYMBOL_A, VF.list(SYMBOL_SEP));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_epsilon = VF.constructor(RascalValueFactory.Symbol_Empty);
	
	private final static IConstructor PROD_S_PLUSLISTA = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_START_S, VF.list(SYMBOL_PLUS_LIST_SEP_A), VF.set());
	private final static IConstructor PROD_SEP_epsilon = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_SEP, VF.list(SYMBOL_epsilon), VF.set());
	private final static IConstructor PROD_PLUSLISTSEPA = VF.constructor(RascalValueFactory.Production_Regular, SYMBOL_PLUS_LIST_SEP_A);
	private final static IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private final static IConstructor PROD_A_epsilon = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_epsilon), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A0 = new NonTerminalStackNode<IConstructor>(0, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_SEP1 = new NonTerminalStackNode<IConstructor>(1, 1, "SEP");
	private final static AbstractStackNode<IConstructor> LIST2 = new SeparatedListStackNode<IConstructor>(2, 0, PROD_PLUSLISTSEPA, NONTERMINAL_A0, new AbstractStackNode[]{NONTERMINAL_SEP1}, true);
	private final static AbstractStackNode<IConstructor> LITERAL_a3 = new LiteralStackNode<IConstructor>(3, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> EPSILON4 = new EpsilonStackNode<IConstructor>(4, 0);
	private final static AbstractStackNode<IConstructor> EPSILON5 = new EpsilonStackNode<IConstructor>(5, 0);
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = LIST2;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_PLUSLISTA);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = LITERAL_a3;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setAlternativeProduction(PROD_A_a);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_2[0] = EPSILON4;
		A_EXPECT_2[0].setProduction(A_EXPECT_2);
		A_EXPECT_2[0].setAlternativeProduction(PROD_A_epsilon);
	}
	
	private final static AbstractStackNode<IConstructor>[] SEP_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		SEP_EXPECT_1[0] = EPSILON5;
		SEP_EXPECT_1[0].setProduction(SEP_EXPECT_1);
		SEP_EXPECT_1[0].setAlternativeProduction(PROD_SEP_epsilon);
	}
	
	public AmbiguousSeparatedEpsilonList(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0], A_EXPECT_2[0]};
	}
	
	public AbstractStackNode<IConstructor>[] SEP(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{SEP_EXPECT_1[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "a".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[\\iter-seps(sort(\"A\"),[sort(\"Sep\")])],{}),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])}),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[]),amb({appl(regular(\\iter-seps(sort(\"A\"),[sort(\"Sep\")])),[appl(prod(sort(\"A\"),[empty()],{}),[]),appl(prod(sort(\"Sep\"),[empty()],{}),[]),appl(prod(sort(\"A\"),[empty()],{}),[])]),cycle(\\iter-seps(sort(\"A\"),[sort(\"Sep\")]),1)})])})])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
	
	public static void main(String[] args){
		AmbiguousSeparatedEpsilonList asel = new AmbiguousSeparatedEpsilonList();
		IConstructor result = asel.executeParser();
		System.out.println(result);
		
		System.out.println("S([(ASEP)+([(ASEP)+(A(a)),(ASEP)+(A(),repeat(SEP(),A()),SEP(),A(a))],SEP(),A(),repeat(SEP(),A())),(ASEP)+(A(),repeat(SEP(),A()),SEP(),A(a)),(ASEP)+(A(a))]) <- good, but not minimal");
	}
}
