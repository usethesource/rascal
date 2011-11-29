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
package org.rascalmpl.library;

import java.io.IOException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Reflective {

	public Reflective(IValueFactory values){
		super();
	}

	public IValue getModuleParseTree(IString modulePath, IEvaluatorContext ctx) {
		try {
			IConstructor tree = null;
			URI uri = ctx.getEvaluator().getRascalResolver().resolve(URI.create("rascal:///" + modulePath.getValue()));
			tree = ctx.getEvaluator().parseModule(ctx.getEvaluator(), uri, new ModuleEnvironment("___getModuleParseTree___", ctx.getHeap()));
			return TreeAdapter.getArgs(tree).get(1);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.moduleNotFound(modulePath, null, null);
		}
	}
	
	public IConstructor getModuleGrammar(ISourceLocation loc, IEvaluatorContext ctx) {
		URI uri = loc.getURI();
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.getGrammar(evaluator.getMonitor(), uri);
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.parseCommand(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}

	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.parseCommands(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}
	
	public IValue parseModule(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.parseModuleWithoutIncludingExtends(evaluator.getMonitor(), str.getValue().toCharArray(), loc.getURI(), new ModuleEnvironment("___parseModule___", ctx.getHeap()));
	}
	
	public IConstructor typeOf(IValue v, IEvaluatorContext ctx) {
		return (IConstructor)v.getType().accept(new TypeReifier(ctx, ctx.getValueFactory())).getValue();
	}
}
