package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
// NOTE: This test only succeeds when the expect builder is used and it shares
// all productions correctly. Otherwise graph node sharing breaks, since the
// id's of the stack nodes do not end up being unique.
// This test could be easily fixed to resolve this issue, but the nice thing
// now is that it breaks when production prefixes are not shared. In any other
// case, the test would likely succeed regardless whether or not this feature
// works. I.e. this kind of stuff should not really be tested in an
// 'acceptance test'. 
/*
S ::= SSS | SS | a
*/
@SuppressWarnings({"unchecked", "cast"})
public class AmbiguousRecursivePrefixShared extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	
	private final static IConstructor PROD_S_SSS = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_S, SYMBOL_S, SYMBOL_S), VF.set());
	private final static IConstructor PROD_S_SS = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_S, SYMBOL_S), VF.set());
	private final static IConstructor PROD_S_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_a), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	
	private final static AbstractStackNode<IConstructor> NONTERMINAL_START_S = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_S0 = new NonTerminalStackNode<IConstructor>(0, 0, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_S1 = new NonTerminalStackNode<IConstructor>(1, 1, "S");
	private final static AbstractStackNode<IConstructor> NONTERMINAL_S2 = new NonTerminalStackNode<IConstructor>(2, 2, "S");
	private final static AbstractStackNode<IConstructor> LITERAL_a5 = new LiteralStackNode<IConstructor>(5, 0, PROD_a_a, new int[]{'a'});
	
	private final static AbstractStackNode<IConstructor>[] S_EXPECTS;
	static{
		ExpectBuilder<IConstructor> sExpectBuilder = new ExpectBuilder<IConstructor>(new IntegerKeyedHashMap<>(), new IntegerMap());
		sExpectBuilder.addAlternative(PROD_S_SSS, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_S0, NONTERMINAL_S1, NONTERMINAL_S2});
		sExpectBuilder.addAlternative(PROD_S_SS, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{NONTERMINAL_S0, NONTERMINAL_S1});
		sExpectBuilder.addAlternative(PROD_S_a, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{LITERAL_a5});
		S_EXPECTS = sExpectBuilder.buildExpectArray();
	}
	
	public AmbiguousRecursivePrefixShared(){
		super();
	}
	
	public AbstractStackNode<IConstructor>[] S(){
		return S_EXPECTS;
	}
	
	public ITree executeParser(){
		return parse(NONTERMINAL_START_S, null, "aaa".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
	}
	
	public IValue getExpectedResult() throws IOException{
		String expectedInput = "amb({appl(prod(sort(\"S\"),[sort(\"S\"),sort(\"S\"),sort(\"S\")],{}),[appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"S\"),[sort(\"S\"),sort(\"S\")],{}),[appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"S\"),[sort(\"S\"),sort(\"S\")],{}),[appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])]),appl(prod(sort(\"S\"),[sort(\"S\"),sort(\"S\")],{}),[appl(prod(sort(\"S\"),[sort(\"S\"),sort(\"S\")],{}),[appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])]),appl(prod(sort(\"S\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])])])})";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
	}

	public static void main(String[] args){
		AmbiguousRecursive ar = new AmbiguousRecursive();
		IConstructor result = ar.executeParser();
		System.out.println(result);
		
		System.out.println("[S(S(a),S(a),S(a)),S(S(a),S(S(a),S(a))),S(S(S(a),S(a)),S(a))] <- good");
	}
}
