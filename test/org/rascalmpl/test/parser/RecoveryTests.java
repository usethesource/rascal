package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.recovery.ToNextWhitespaceRecoverer;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;

/**
 * S -> A ws B ws C
 * A -> [a] ws [a]
 * B -> [b] ws [b]
 * C -> [c] ws [c]
 * 
 * ws -> [\ ]
 */
public class RecoveryTests extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private final static IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));
	
	private final static IConstructor SYMBOL_ws = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("ws"));
	private final static IConstructor SYMBOL_char_space = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(32))));

	private final static IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private final static IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private final static IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	private final static IConstructor PROD_A_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a, SYMBOL_ws, SYMBOL_a), VF.set());

	private final static IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private final static IConstructor SYMBOL_b = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("b"));
	private final static IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	private final static IConstructor PROD_B_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_b, SYMBOL_ws, SYMBOL_b), VF.set());

	private final static IConstructor SYMBOL_C = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("C"));
	private final static IConstructor SYMBOL_c = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("c"));
	private final static IConstructor SYMBOL_char_c = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(99))));
	private final static IConstructor PROD_C_c_c = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_C, VF.list(SYMBOL_c, SYMBOL_ws, SYMBOL_c), VF.set());
	
	private final static IConstructor PROD_S_A_B_C = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_ws, SYMBOL_B, SYMBOL_ws, SYMBOL_C), VF.set());

	private final static IConstructor PROD_ws = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_ws, VF.list(SYMBOL_char_space), VF.set());
	private final static IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private final static IConstructor PROD_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_b, VF.list(SYMBOL_char_b), VF.set());
	private final static IConstructor PROD_c_c = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_c, VF.list(SYMBOL_char_c), VF.set());
		
	public AbstractStackNode<IConstructor>[] S(){
		return IParserTest.createExpectArray(PROD_S_A_B_C,
			new NonTerminalStackNode<IConstructor>(1, 0, "A"),
        	new LiteralStackNode<IConstructor>(2, 1, PROD_ws, new int[]{' '}),
			new NonTerminalStackNode<IConstructor>(3, 2, "B"),
        	new LiteralStackNode<IConstructor>(4, 3, PROD_ws, new int[]{' '}),
			new NonTerminalStackNode<IConstructor>(5, 4, "C")
    	);
	}

	public AbstractStackNode<IConstructor>[] A(){
		return IParserTest.createExpectArray(PROD_A_a_a,
			new LiteralStackNode<IConstructor>(6, 0, PROD_a_a, new int[]{'a'}),
        	new LiteralStackNode<IConstructor>(7, 1, PROD_ws, new int[]{' '}),
			new LiteralStackNode<IConstructor>(8, 2, PROD_a_a, new int[]{'a'})
    	);
	}

	public AbstractStackNode<IConstructor>[] B(){
		return IParserTest.createExpectArray(PROD_B_b_b,
			new LiteralStackNode<IConstructor>(9, 0, PROD_b_b, new int[]{'b'}),
        	new LiteralStackNode<IConstructor>(10, 1, PROD_ws, new int[]{' '}),
			new LiteralStackNode<IConstructor>(11, 2, PROD_b_b, new int[]{'b'})
    	);
	}

	public AbstractStackNode<IConstructor>[] C(){
		return IParserTest.createExpectArray(PROD_C_c_c,
			new LiteralStackNode<IConstructor>(12, 0, PROD_c_c, new int[]{'c'}),
        	new LiteralStackNode<IConstructor>(13, 1, PROD_ws, new int[]{' '}),
			new LiteralStackNode<IConstructor>(14, 2, PROD_c_c, new int[]{'c'})
    	);
	}

	private int nextFreeStackNodeId = 100;

	@Override
	protected int getFreeStackNodeId() {
		return nextFreeStackNodeId++;
	}

    private ITree parse(String s) {
		IRecoverer<IConstructor> recoverer = new ToNextWhitespaceRecoverer(() -> nextFreeStackNodeId++);
        return parse("S" /* NONTERMINAL_START_S */, null, s.toCharArray(), 
			new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false), recoverer, null);
    }

    private ITree toTree(String s) {
        try {
		    return (ITree) new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
	public ITree executeParser() {
		return parse("a a b b c c");
	}

    @Override
	public IValue getExpectedResult() {
		return toTree("appl(prod(sort(\"S\"),[sort(\"A\"),lit(\"ws\"),sort(\"B\"),lit(\"ws\"),sort(\"C\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\"),lit(\"ws\"),lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"B\"),[lit(\"b\"),lit(\"ws\"),lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"C\"),[lit(\"c\"),lit(\"ws\"),lit(\"c\")],{}),[appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)])])])");
	}

    @Test
    public void testOk() {
		Assert.assertEquals(getExpectedResult(), executeParser());
    }

	@Test
	public void testMissingCharRecovery() {
		String expected = "appl(prod(sort(\"S\"),[sort(\"A\"),lit(\"ws\"),sort(\"B\"),lit(\"ws\"),sort(\"C\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\"),lit(\"ws\"),lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"B\"),[lit(\"b\"),lit(\"ws\"),lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"C\"),[lit(\"c\"),lit(\"ws\"),lit(\"c\")],{}),[appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)])])])";
		Assert.assertEquals(toTree(expected), parse("a a b c c"));
	}
    
}
