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
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.SequenceStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.ITree;
/*
S ::= (A B)
A ::= a
B ::= b
*/
@SuppressWarnings({"unchecked", "cast"})
public class Sequence2 extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_SEQ_AB = VF.constructor(RascalValueFactory.Symbol_Seq, VF.list(SYMBOL_A, SYMBOL_B));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	private final static IConstructor SYMBOL_b = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	
	private final static IConstructor PROD_S_SEQ_AB = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_SEQ_AB), VF.set());
	private final static IConstructor PROD_SEQUENCE_AB = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_SEQ_AB, VF.list(SYMBOL_A, SYMBOL_B), VF.set());
	private final static IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private final static IConstructor PROD_B_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_b), VF.set());
	private final static IConstructor PROD_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_b, VF.list(SYMBOL_char_b), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_A1 = new NonTerminalStackNode<IConstructor>(1, 0, "A");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_B2 = new NonTerminalStackNode<IConstructor>(2, 1, "B");
	private final static AbstractStackNode<IConstructor> SEQUENCE3 = new SequenceStackNode<IConstructor>(3, 0, PROD_SEQUENCE_AB, new AbstractStackNode[]{NONTERMINAL_A1, NONTERMINAL_B2});
	private final static AbstractStackNode<IConstructor> LITERAL_a4 = new LiteralStackNode<IConstructor>(4, 0, PROD_a_a, new int[]{'a'});
	private final static AbstractStackNode<IConstructor> LITERAL_b5 = new LiteralStackNode<IConstructor>(5, 0, PROD_b_b, new int[]{'b'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = SEQUENCE3;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_SEQ_AB);
	}
	
	private final static AbstractStackNode<IConstructor>[] A_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		A_EXPECT_1[0] = LITERAL_a4;
		A_EXPECT_1[0].setProduction(A_EXPECT_1);
		A_EXPECT_1[0].setAlternativeProduction(PROD_A_a);
	}
	
	private final static AbstractStackNode<IConstructor>[] B_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		B_EXPECT_1[0] = LITERAL_b5;
		B_EXPECT_1[0].setProduction(B_EXPECT_1);
		B_EXPECT_1[0].setAlternativeProduction(PROD_B_b);
	}
	
	public Sequence2(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] A(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{A_EXPECT_1[0]};
	}
	
	public AbstractStackNode<IConstructor>[] B(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{B_EXPECT_1[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "ab".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory());
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[seq([sort(\"A\"),sort(\"B\")])],{}),[appl(prod(seq([sort(\"A\"),sort(\"B\")]),[sort(\"A\"),sort(\"B\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"B\"),[lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		Sequence2 s2 = new Sequence2();
		IConstructor result = s2.executeParser();
		System.out.println(result);
		
		System.out.println("S((A(a),B(b))) <- good");
	}
}
