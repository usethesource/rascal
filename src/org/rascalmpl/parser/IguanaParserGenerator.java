/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Ali Afroozeh - Ali.Afroozeh@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.parser;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.Grammar;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class IguanaParserGenerator {
	private final Evaluator evaluator;
	private final IValueFactory vf;

	public IguanaParserGenerator(IRascalMonitor monitor, PrintWriter out, List<ClassLoader> loaders, IValueFactory factory, Configuration config) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment scope = new ModuleEnvironment("___parsergenerator___", heap);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), out, out, scope,heap);
		this.evaluator.getConfiguration().setRascalJavaClassPathProperty(config.getRascalJavaClassPathProperty());
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		
		this.evaluator.setBootstrapperProperty(true);
		this.vf = factory;
		
		monitor.startJob("Loading parser generator", 100, 139);
		try {
			evaluator.doImport(monitor, "lang::rascal::grammar::ConcreteSyntax");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Modules");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Priorities");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Regular");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Keywords");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Literals");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Parameters");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Symbols");
			evaluator.doImport(monitor, "lang::rascal::grammar::IguanaParserGenerator");
			evaluator.doImport(monitor, "Ambiguity");
		}
		finally {
			monitor.endJob(true);
		}
	}
	
	public String getNonterminalName(IConstructor symbolTree) {
		return SymbolAdapter.toString((IConstructor) evaluator.call(new NullRascalMonitor(), "sym2symbol", symbolTree));
	}
	
	private IConstructor getPreprocessedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		try {
			return (IConstructor) evaluator.call(monitor, "preprocess", definition);
		}
		catch (Throwable e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw e;
		}
	}
	
	public Grammar generateGrammar(IRascalMonitor monitor, String main, IMap definitions) {
		IConstructor gr = getPreprocessedGrammar(monitor, main, definitions);
	    return new RascalToIguanaGrammarConverter(vf).convert(main, gr);
	}
	
	public IValue diagnoseAmbiguity(IConstructor parseForest) {
		return evaluator.call("diagnose", parseForest);
	}
	
	public IConstructor getGrammar(IRascalMonitor monitor, String main, IMap definition) {
		return (IConstructor) evaluator.call(monitor, "modules2grammar", vf.string(main), definition);
	}
	
	public IConstructor getExpandedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		IConstructor g = getGrammar(monitor, main, definition);
		
		monitor.event("Expanding keywords", 10);
		g = (IConstructor) evaluator.call(monitor, "expandKeywords", g);
		monitor.event("Adding regular productions",10);
		g = (IConstructor) evaluator.call(monitor, "makeRegularStubs", g);
		monitor.event("Expanding regulars", 10);
		g = (IConstructor) evaluator.call(monitor, "expandRegularSymbols", g);
		monitor.event("Expanding parametrized symbols");
		g = (IConstructor) evaluator.call(monitor, "expandParameterizedSymbols", g);
		monitor.event("Defining literals");
		g = (IConstructor) evaluator.call(monitor, "literals", g);
		return g;
	}

	public ISet getNestingRestrictions(IRascalMonitor monitor,
			IConstructor g) {
		return (ISet) evaluator.call(monitor, "doNotNest", g);
	}
	
	/**
	 * Converts the parse tree of a symbol to a UPTR symbol
	 */
	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
	  return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
	}
	

	public String createHole(IConstructor part, int size) {
		return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
	}
}
