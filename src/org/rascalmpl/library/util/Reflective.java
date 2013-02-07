/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.io.IOException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;

public class Reflective {
	private final IValueFactory values;
	private Evaluator cachedEvaluator;
	private int robin = 0;
	private static final int maxCacheRounds = 5;

	public Reflective(IValueFactory values){
		super();
		this.values = values;
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
	
	public IValue parseModule(ISourceLocation loc, IEvaluatorContext ctx) {
		try {
			Evaluator ownEvaluator = getPrivateEvaluator(ctx);
			return ownEvaluator.parseModule(ownEvaluator.getMonitor(), loc.getURI(), null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}

	private Evaluator getPrivateEvaluator(IEvaluatorContext ctx) {
		if (cachedEvaluator == null || robin++ > maxCacheRounds) {
			robin = 0;
			IEvaluator<?> callingEval = ctx.getEvaluator();
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___full_module_parser___", heap));
			cachedEvaluator = new Evaluator(callingEval.getValueFactory(), callingEval.getStdErr(), callingEval.getStdOut(), root, heap);
		}
		
		return cachedEvaluator;
	}
	
	public IValue parseModule(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator ownEvaluator = getPrivateEvaluator(ctx);
		return ownEvaluator.parseModule(ownEvaluator.getMonitor(), str.getValue().toCharArray(), loc.getURI(), null);
	}
	
	public IValue getModuleLocation(IString modulePath, IEvaluatorContext ctx) {
		URI uri = ctx.getEvaluator().getRascalResolver().resolve(URIUtil.createRascalModule(modulePath.getValue()));
		if (uri == null) throw RuntimeExceptionFactory.moduleNotFound(modulePath, ctx.getCurrentAST(), null);
		return ctx.getValueFactory().sourceLocation(uri);
	}
}
