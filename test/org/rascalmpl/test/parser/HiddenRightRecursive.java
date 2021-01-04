package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

/*
A ::= B B*
B ::= y | x A
*/
@SuppressWarnings({"unchecked", "cast"})
public class HiddenRightRecursive extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_STAR_LIST_B = VF.constructor(RascalValueFactory.Symbol_IterStar, SYMBOL_B);
	private final static IConstructor SYMBOL_x = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("x"));
	private final static IConstructor SYMBOL_y = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("y"));
	private final static IConstructor SYMBOL_char_x = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(120))));
	private final static IConstructor SYMBOL_char_y = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(121))));

	private final static IConstructor PROD_B_STARLIST_B = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_A, VF.list(SYMBOL_B, SYMBOL_STAR_LIST_B), VF.set());
	private final static IConstructor PROD_STARLIST_B = VF.constructor(RascalValueFactory.Production_Regular, SYMBOL_STAR_LIST_B);
	private final static IConstructor PROD_x_A = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_B, VF.list(SYMBOL_x, SYMBOL_A), VF.set());
	private final static IConstructor PROD_y = VF.constructor(RascalValueFactory.Production_Default, SYMBOL_B, VF.list(SYMBOL_y), VF.set());
	private final static IConstructor PROD_x_x = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_x, VF.list(SYMBOL_char_x), VF.set());
	private final static IConstructor PROD_y_y = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_y, VF.list(SYMBOL_char_y), VF.set());

	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_A = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "A");

	private final static AbstractStackNode<IConstructor> NON_TERMINAL_B1 = new NonTerminalStackNode<IConstructor>(1, 0, "B");
	private final static AbstractStackNode<IConstructor> NON_TERMINAL_B2 = new NonTerminalStackNode<IConstructor>(2, 0, "B");
	private final static AbstractStackNode<IConstructor> LIST3 = new ListStackNode<IConstructor>(3, 1, PROD_STARLIST_B, NON_TERMINAL_B2, false);

	private final static AbstractStackNode<IConstructor> LITERAL_y4 = new LiteralStackNode<IConstructor>(4, 0, PROD_y_y, new int[]{'y'});
	private final static AbstractStackNode<IConstructor> LITERAL_x5 = new LiteralStackNode<IConstructor>(5, 0, PROD_x_x, new int[]{'x'});
	private final static AbstractStackNode<IConstructor> NON_TERMINAL_A6 = new NonTerminalStackNode<IConstructor>(6, 1, "A");

	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		A_EXPECT_1[0] = NON_TERMINAL_B1;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[1] = LIST3;
		A_EXPECT_1[1].setProduction(A_EXPECT_1);
		A_EXPECT_1[1].setAlternativeProduction(PROD_B_STARLIST_B);
	}

	private final static AbstractStackNode<IConstructor>[] B_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		B_EXPECT_1[0] = LITERAL_y4;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[0].setAlternativeProduction(PROD_y);
	}

	private final static AbstractStackNode<IConstructor>[] B_EXPECT_2 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
	static{
		B_EXPECT_2[0] = LITERAL_x5;
		B_EXPECT_2[0].setProduction(B_EXPECT_2);
		B_EXPECT_2[1] = NON_TERMINAL_A6;
		B_EXPECT_2[1].setProduction(B_EXPECT_2);
		B_EXPECT_2[1].setAlternativeProduction(PROD_x_A);
	}

	public HiddenRightRecursive(){
		super();
	}

	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0]};
	}

	public AbstractStackNode<IConstructor>[] B(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{B_EXPECT_1[0], B_EXPECT_2[0]};
	}

	public ITree executeParser() {
		return parse(NONTERMINAL_START_A, null, "xy".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}

	public IValue getExpectedResult() throws IOException {
		String expectedInput = "appl(prod(sort(\"A\"),[sort(\"B\"),\\iter-star(sort(\"B\"),[])],{}),[appl(prod(sort(\"B\"),[lit(\"x\"),sort(\"A\")],{}),[appl(prod(lit(\"x\"),[\\char-class([from(120)])],{}),[char(120)]),appl(prod(sort(\"A\"),[sort(\"B\"),\\iter-star(sort(\"B\"),[])],{}),[appl(prod(sort(\"B\"),[lit(\"y\")],{}),[appl(prod(lit(\"y\"),[\\char-class([from(121)])],{}),[char(121)])]),appl(regular(\\iter-star(sort(\"B\"),[])),[])])]),appl(regular(\\iter-star(sort(\"B\"),[])),[])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}
}
