/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

 package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.rascalmpl.parser.uptr.debug.DebugLogger;
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
 * A -> [a]
 * B -> [b] ws [b]
 * C -> [c]
 *
 * ws -> [\ ]
 */
public class RecoveryTests extends SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest{
	private static final IConstructor SYMBOL_START_S = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("S"));

	private static final IConstructor SYMBOL_ws = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("ws"));
	private static final IConstructor SYMBOL_char_space = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(32))));

	private static final IConstructor SYMBOL_A = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("A"));
	private static final IConstructor SYMBOL_a = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("a"));
	private static final IConstructor SYMBOL_char_a = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(97))));
	private static final IConstructor PROD_A_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_A, VF.list(SYMBOL_a), VF.set());

	private static final IConstructor SYMBOL_B = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("B"));
	private static final IConstructor SYMBOL_b = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("b"));
	private static final IConstructor SYMBOL_char_b = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(98))));
	private static final IConstructor PROD_B_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_B, VF.list(SYMBOL_b, SYMBOL_ws, SYMBOL_b), VF.set());

	private static final IConstructor SYMBOL_C = VF.constructor(RascalValueFactory.Symbol_Sort, VF.string("C"));
	private static final IConstructor SYMBOL_c = VF.constructor(RascalValueFactory.Symbol_Lit, VF.string("c"));
	private static final IConstructor SYMBOL_char_c = VF.constructor(RascalValueFactory.Symbol_CharClass, VF.list(VF.constructor(RascalValueFactory.CharRange_Single, VF.integer(99))));
	private static final IConstructor PROD_C_c = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_C, VF.list(SYMBOL_c), VF.set());

	private static final IConstructor PROD_S_A_B_C = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_START_S, VF.list(SYMBOL_A, SYMBOL_ws, SYMBOL_B, SYMBOL_ws, SYMBOL_C), VF.set());

	private static final IConstructor PROD_ws = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_ws, VF.list(SYMBOL_char_space), VF.set());
	private static final IConstructor PROD_a_a = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_a, VF.list(SYMBOL_char_a), VF.set());
	private static final IConstructor PROD_b_b = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_b, VF.list(SYMBOL_char_b), VF.set());
	private static final IConstructor PROD_c_c = VF.constructor(RascalValueFactory.Production_Default,  SYMBOL_c, VF.list(SYMBOL_char_c), VF.set());

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
		return IParserTest.createExpectArray(PROD_A_a,
			new LiteralStackNode<IConstructor>(6, 0, PROD_a_a, new int[]{'a'})
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
		return IParserTest.createExpectArray(PROD_C_c,
			new LiteralStackNode<IConstructor>(12, 0, PROD_c_c, new int[]{'c'})
    	);
	}

	private int nextFreeStackNodeId = 100;

	@Override
	protected int getFreeStackNodeId() {
		return nextFreeStackNodeId++;
	}

    private ITree parse(String s) {
		DebugLogger debugLogger = new DebugLogger(new PrintWriter(System.out));
		IRecoverer<IConstructor> recoverer = new ToNextWhitespaceRecoverer(() -> nextFreeStackNodeId++);
        return parse("S" /* NONTERMINAL_START_S */, null, s.toCharArray(),
			new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false), recoverer, debugLogger);
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
		return parse("a b b c");
	}

    @Override
	public IValue getExpectedResult() {
		return toTree("appl(prod(sort(\"S\"),[sort(\"A\"),lit(\"ws\"),sort(\"B\"),lit(\"ws\"),sort(\"C\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"B\"),[lit(\"b\"),lit(\"ws\"),lit(\"b\")],{}),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"C\"),[lit(\"c\")],{}),[appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)])])])");
	}

    @Test
    public void testOk() {
		Assert.assertEquals(getExpectedResult(), executeParser());
    }

	@Test
	public void testIncorrectCharacter() {
		String expected = "appl(prod(sort(\"S\"),[sort(\"A\"),lit(\"ws\"),sort(\"B\"),lit(\"ws\"),sort(\"C\")],{}),[appl(prod(sort(\"A\"),[lit(\"a\")],{}),[appl(prod(lit(\"a\"),[\\char-class([single(97)])],{}),[char(97)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(error(sort(\"B\"),prod(sort(\"B\"),[lit(\"b\"),lit(\"ws\"),lit(\"b\")],{}),2),[appl(prod(lit(\"b\"),[\\char-class([single(98)])],{}),[char(98)]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(skipped(\\iter-star(\\char-class([range(1,1114111)]))),[char(120)])]),appl(prod(lit(\"ws\"),[\\char-class([single(32)])],{}),[char(32)]),appl(prod(sort(\"C\"),[lit(\"c\")],{}),[appl(prod(lit(\"c\"),[\\char-class([single(99)])],{}),[char(99)])])])";
		Assert.assertEquals(toTree(expected), parse("a b x c"));
	}

}
