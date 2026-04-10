/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.type.Type;

public class UnexpectedType extends StaticError {
	private static final long serialVersionUID = -9009407553448884728L;
	
	public UnexpectedType(Type expected, Type got, AbstractAST ast) {
		super(computeMessage(expected, got), ast);
	}
	
	public UnexpectedType(Type expected, Type got, ISourceLocation loc) {
		super(computeMessage(expected, got), loc);
	}

	 private static String computeMessage(Type expected, Type got) {
		if (expected.isAbstractData() && (got.isExternalType() && ((RascalType) got).isNonterminal())) {
			return "Expected data[" + expected + "], but got " + (SymbolAdapter.isSort(((NonTerminalType) got).getSymbol()) ? "syntax" : "lexical") + "[" + got + "]";
		}
		else if (got.isAbstractData() && (expected.isExternalType() && ((RascalType) expected).isNonterminal())) {
			return "Expected " + (SymbolAdapter.isSort(((NonTerminalType) expected).getSymbol()) ? "syntax" : "lexical") + "[" + expected + "], but got data[" + got + "]";
		}
		else {
			return "Expected " + expected + ", but got " + got;
		}
	}
}
