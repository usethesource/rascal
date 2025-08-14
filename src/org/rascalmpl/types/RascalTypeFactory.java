/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.types;

import java.util.Arrays;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.SymbolFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RascalTypeFactory {
	private TypeFactory tf = TypeFactory.getInstance();
	private IRascalValueFactory vf = IRascalValueFactory.getInstance();
	
	private static class InstanceHolder {
		public static final RascalTypeFactory sInstance = new RascalTypeFactory();
	}
	
	public static RascalTypeFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	 
	public Type nonTerminalType(IConstructor cons) {
		assert !SymbolAdapter.isADT(cons) && !SymbolAdapter.isParameter(cons);

		return tf.externalType(new NonTerminalType(cons));
	}
	
	public Type nonTerminalType(org.rascalmpl.ast.Type symbol, boolean lex, String layout) {
		return tf.externalType(new NonTerminalType(symbol, lex, layout));
	}
	
	public Type reifiedType(Type arg) {
		return tf.externalType(new ReifiedType(arg));
	}

	public Type syntaxType(String name, Type[] parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeSort(name, 
			Arrays.stream(parameters)
				.map(p -> ((NonTerminalType) p).getSymbol())
				.collect(vf.listWriter()))
		));
	}

	public Type syntaxType(String name, IList parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeSort(name, parameters)));
	}

	public Type syntaxType(String name) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeSort(name)));
	}

	public Type lexicalType(String name, Type[] parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLex(name, 
			Arrays.stream(parameters)
				.map(p -> ((NonTerminalType) p).getSymbol())
				.collect(vf.listWriter()))
		));
	}

	public Type lexicalType(String name) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLex(name)));
	}

	public Type lexicalType(String name, IList parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLex(name, parameters)));
	}

	public Type keywordType(String name) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeKeyword(name, vf.list())));
	}

	public Type layoutType(String name, Type[] parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLayout(name, vf.list())));
	}

	public Type layoutType(String name, IList parameters) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLayout(name, parameters)));
	}

	public Type layoutType(String name) {
		return tf.externalType(new NonTerminalType(SymbolFactory.makeLayout(name)));
	}

	/**
	 * Changes a data-type, a lexical, a syntax or a layout type to a keyword type.
	 * Because keyword types do not support parameters, this method throws exceptions
	 * for unsupported cases. It is the task of the type-checker to make sure these
	 * will be caught before running. 
	 * 
	 * @param arg a data-type, lexical or syntax or layout sort to be modified
	 * @return a keyword type with the same name as the input type, but mofied to a keyword non-terminal.
	 */
    public Type modifyToKeyword(Type arg) {
		return tf.externalType(new ModifySyntaxRole.Keyword(arg).apply());
    }

   /**
	 * Changes a keyword, a lexical, a data type or a layout type to a syntax type.
	 * When the modified type is not a named entity type like a syntax or lexical sort,
	 * and adt or a keyword non-terminal, then this method throws an exception. 
	 * 
	 * @param arg a data-type, lexical or syntax or layout sort to be modified
	 * @return a syntax type with the same name as the input type, but modified to a syntax non-terminal,
	 * or a still open modifier type for lazy instantation.
	 */
    public Type modifyToSyntax(Type arg) {
		return tf.externalType(new ModifySyntaxRole.Syntax(arg).apply());
	}

	 /**
	 * Changes a keyword, a syntax, a data type or a layout type to a lexical type.
	 * When the modified type is not a named entity type like a syntax or lexical sort,
	 * and adt or a keyword non-terminal, then this method throws an exception. 
	 * 
	 * @param arg a data-type, lexical or syntax or layout sort to be modified
	 * @return a syntax type with the same name as the input type, but modified to a syntax non-terminal,
	 * or a still open modifier type for lazy instantation.
	 */
    public Type modifyToLexical(Type arg) {
		return tf.externalType(new ModifySyntaxRole.Lexical(arg).apply());
	}

	
	/**
	 * Changes a keyword, a lexical, a syntax or a layout type to a data type.
	 * When the modified type is not a named entity type like a syntax or lexical sort,
	 * and adt or a keyword non-terminal, then this method throws an exception. 
	 * 
	 * @param arg a data-type, lexical or syntax or layout sort to be modified
	 * @return a data type with the same name as the input type, but modified to a data non-terminal,
	 * or a still open modifier type for lazy instantation.
	 */
    public Type modifyToData(Type arg) {
		return tf.externalType(new ModifySyntaxRole.Data(arg).apply());
	}

	/**
	 * Changes a syntax, a lexical, a syntax or a data type to a layout type.
	 * When the modified type is not a named entity type like a syntax or lexical sort,
	 * and adt or a keyword non-terminal, then this method throws an exception. 
	 * 
	 * @param arg a data-type, lexical or syntax or layout sort to be modified
	 * @return a data type with the same name as the input type, but modified to a data non-terminal,
	 * or a still open modifier type for lazy instantation.
	 */
    public Type modifyToLayout(Type arg) {
		return tf.externalType(new ModifySyntaxRole.Layout(arg).apply());
	}

}
