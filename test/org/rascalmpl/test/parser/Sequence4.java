package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.SequenceStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
/*
S ::= (A () B)
A ::= a
B ::= b
*/
@SuppressWarnings({"unchecked", "cast"})
public class Sequence4 extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_SEQ = VF.constructor(RascalValueFactory.Symbol_Seq, VF.list());
	
	private final static IConstructor PROD_S_SEQ_AB = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_SEQ), VF.set());
	private final static IConstructor PROD_SEQUENCE = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_SEQ, VF.list(), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> SEQUENCE4 = new SequenceStackNode<IConstructor>(3, 0, PROD_SEQUENCE, new AbstractStackNode[]{});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECT_1 = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
	static{
		S_EXPECT_1[0] = SEQUENCE4;
		S_EXPECT_1[0].setProduction(S_EXPECT_1);
		S_EXPECT_1[0].setAlternativeProduction(PROD_S_SEQ_AB);
	}
	
	public Sequence4(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{S_EXPECT_1[0]};
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "appl(prod(sort(\"S\"),[seq([])],{}),[appl(prod(seq([]),[],{}),[])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		Sequence4 s4 = new Sequence4();
		IConstructor result = s4.executeParser();
		System.out.println(result);
	}
}
