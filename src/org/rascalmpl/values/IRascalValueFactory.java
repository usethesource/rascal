/*******************************************************************************
 * Copyright (c) 2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl
*******************************************************************************/
package org.rascalmpl.values;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiFunction;

import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

/**
 * See {@link RascalValueFactory} for documentation.
 */
public interface IRascalValueFactory extends IValueFactory {
    static IRascalValueFactory getInstance() {
        return RascalValueFactory.getInstance();
    }
    
	IConstructor reifiedType(IConstructor symbol, IMap definitions);
	
	ITree appl(Map<String,IValue> kwParams, IConstructor prod, IList args);
	ITree appl(IConstructor prod, IList args);
	ITree appl(IConstructor prod, IValue... args);
	@Deprecated IConstructor appl(IConstructor prod, ArrayList<ITree> args);
	
	ITree cycle(IConstructor symbol, int cycleLength);

	ITree amb(ISet alternatives);
	
	ITree character(int ch);
	
	ITree character(byte ch);
	
	IConstructor grammar(IMap rules);
	
	/**
	 * Construct a function-as-value (i.e. IValue) with interface IFunction.
	 * 
	 * @param functionType the type of the function 
	 * @param func         a JVM closure or any other form which implements the BiFunction interface
	 * @return a function as value
	 */
	default IFunction function(Type functionType, BiFunction<IValue[], Map<String, IValue>, IValue> func) {
	    throw new UnsupportedOperationException("This Rascal value factory does not support function values:" + getClass());
	}
	
	/**
	 * Constructs a parse function from a grammar, where the parse function has the following (overloaded) signature:
	 *
	 * &T parse(str input, loc origin);
	 * &T parse(loc input, loc origin);
	 * 
	 * The parse function
	 *   * its return type is bound by the start-nonterminal of the grammar which was provided.
	 *   * is overloaded on the first argument; it reads input either from a str or the contents of the resource that a loc points to.
	 *   * behaves differently depending on the keyword parameters.
	 *   * uses `origin` for the source location references in the resulting parse tree, which defaults to the loc parameter in case of a loc input;
	 *     when a str input is given it defaults to |unknown:///|.
	 *     
	 *  Parameters:
	 *     
	 *  `allowAmbiguity`: if true then no exception is thrown in case of ambiguity and a parse forest is returned. if false,
	 *                    the parser throws an exception during tree building and produces only the first ambiguous subtree in its message.
	 *                    if set to `false`, the parse constructs trees in linear time. if set to `true` the parser constructs trees in polynomial time.
	 * 
	 *  `hasSideEffects`: if false then the parser is a lot faster when constructing trees, since it does not execute the parse _actions_ in an
	 *                    interpreted environment to make side effects (like a symbol table) and it can share more intermediate results as a result.
	 *  
	 *  `firstAmbiguity`: if true, then the parser returns the subforest for the first (left-most innermost) ambiguity instead of a parse tree for
	 *                    the entire input string. This is for grammar debugging purposes a much faster solution then waiting for an entire 
	 *                    parse forest to be constructed in polynomial time.
	 * 
	 *  `filter` : is a function that eiter returns a modified tree of the same type, or throws `FilterException` which leads
	 *             to the removal of the current tree under an ambiguity cluster that is higher up the tree. The filter function
	 *             is called on every parse tree node while it is being constructed after a succesful parse forest is produced.
	 */
	default IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
	    throw new UnsupportedOperationException("This Rascal value factory does not support a parser generator:" + getClass());
	}
	
	/**
	 * Same as `parser` but produces parsers which are parametrized by the start-nonterminal:
	 * 
	 *  * &U parse(type[&U <: Tree], str input, loc origin);
	 *  * &U parse(type[&U <: Tree], loc input, loc origin);
	 */
	default IFunction parsers(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
        throw new UnsupportedOperationException("This Rascal value factory does not support a parser generator:" + getClass());
    }

	/**
	 * Same as `parsers` but saves the result to disk rather than wrapping it as an IFunction.
	 * 
	 * @param start
	 * @param saveLocation
	 * @throws IOException
	 */
    default void storeParsers(IValue start, ISourceLocation saveLocation) throws IOException {
		throw new UnsupportedOperationException("This Rascal value factory does not support a parser generator that can store parsers on disk." + getClass());
	}

	/**
	 * Reverse of storeParsers and with the same effect as the {@see parsers} method.
	 */
	default IFunction loadParsers(ISourceLocation saveLocation, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
		throw new UnsupportedOperationException("This Rascal value factory does not support a parser generator that can restore parsers from disk." + getClass());
	}
}
