/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Reflective {

	public Reflective(IValueFactory values){
		super();
	}

	public IConstructor getModuleGrammar(ISourceLocation loc, IEvaluatorContext ctx) {
		URI uri = loc.getURI();
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.getGrammar(evaluator.getMonitor(), uri);
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommand(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}

	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommands(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}
	
	public IValue parseModule(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseModuleWithoutIncludingExtends(evaluator.getMonitor(), str.getValue().toCharArray(), loc.getURI(), new ModuleEnvironment("___parseModule___", ctx.getHeap()));
	}

	public IValue getModuleLocation(IString modulePath, IEvaluatorContext ctx) {
		URI uri = ctx.getEvaluator().getRascalResolver().resolve(URI.create("rascal://" + modulePath.getValue()));
		if (uri == null) throw RuntimeExceptionFactory.moduleNotFound(modulePath, ctx.getCurrentAST(), null);
		return ctx.getValueFactory().sourceLocation(uri);
	}
}
