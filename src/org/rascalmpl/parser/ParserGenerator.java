/*******************************************************************************
 * Copyright (c) 2009-2019 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ParserGenerator {
	private final Evaluator evaluator;
	private final JavaBridge bridge;
	private final IValueFactory vf;
	private static final String packageName = "org.rascalmpl.java.parser.object";
	private static final boolean debug = false;

	public ParserGenerator(IRascalMonitor monitor, OutputStream out, List<ClassLoader> loaders, IValueFactory factory, Configuration config) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment scope = new ModuleEnvironment("$parsergenerator$", heap);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, out, out, scope, heap, monitor);
		this.evaluator.getConfiguration().setRascalJavaClassPathProperty(config.getRascalJavaClassPathProperty());
		this.evaluator.getConfiguration().setGeneratorProfiling(config.getGeneratorProfilingProperty());
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		
		this.evaluator.setBootstrapperProperty(true);
		this.bridge = new JavaBridge(loaders, factory, config);
		this.vf = factory;
		
		evaluator.doImport(monitor, 
	"lang::rascal::grammar::ParserGenerator",
			"lang::rascal::grammar::ConcreteSyntax",
			"lang::rascal::grammar::definition::Modules",
			"lang::rascal::grammar::definition::Priorities", 
			"lang::rascal::grammar::definition::Regular", 
			"lang::rascal::grammar::definition::Keywords",
			"lang::rascal::grammar::definition::Literals",
			"lang::rascal::grammar::definition::Parameters",
			"lang::rascal::grammar::definition::Symbols",
			"analysis::grammars::Ambiguity"
		);
	}
	
	public void setGeneratorProfiling(boolean f) {
		evaluator.getConfiguration().setGeneratorProfiling(f);
	}
	
	public IValue diagnoseAmbiguity(IConstructor parseForest) {
		synchronized(evaluator) {
			return evaluator.call("diagnose", parseForest);
		}
	}
	
	private void debugOutput(Object thing, String file) {
		if (debug) {
			String classString = thing.toString();
			FileOutputStream s = null;
			try {
			    System.err.println("Writing parser to " + file);
				s = new FileOutputStream(file);
				s.write(classString.getBytes());
				s.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public IConstructor getGrammarFromModules(IRascalMonitor monitor, String main, IMap modules) {
		synchronized(evaluator) {
			return (IConstructor) evaluator.call(monitor, "modules2grammar", vf.string(main), modules);
		}
	}
	
	public IConstructor getExpandedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		synchronized(evaluator) {
			IConstructor g = getGrammarFromModules(monitor, main, definition);
			g = (IConstructor) evaluator.call(monitor, "expandKeywords", g);
			g = (IConstructor) evaluator.call(monitor, "makeRegularStubs", g);
			g = (IConstructor) evaluator.call(monitor, "expandRegularSymbols", g);
			g = (IConstructor) evaluator.call(monitor, "expandParameterizedSymbols", g);
			g = (IConstructor) evaluator.call(monitor, "literals", g);
			return g;
		}
	}

	public ISet getNestingRestrictions(IRascalMonitor monitor,
			IConstructor g) {
		synchronized (evaluator) {
			return (ISet) evaluator.call(monitor, "doNotNest", g);
		}
	}

	/** 
	 * Produces the name generated by the parser generator for a parse method for the given symbol
	 */
	public String getParserMethodName(IConstructor symbol) {
		// we use a fast non-synchronized path for simple cases; 
		// this is to prevent locking the evaluator in IDE contexts
		// where many calls into the evaluator/parser are fired in rapid
		// succession.

		switch (symbol.getName()) {
			case "start":
				return "start__" + getParserMethodName(SymbolAdapter.getStart(symbol));
			case "layouts":
				return "layouts_" + SymbolAdapter.getName(symbol);
			case "sort":
			case "lex":
			case "keywords":
				return SymbolAdapter.getName(symbol);
		}

		synchronized (evaluator) {
			return ((IString) evaluator.call((IRascalMonitor) null, "getParserMethodName", symbol)).getValue();
		}
	}
	
	/**
	 * Converts the parse tree of a symbol to a UPTR symbol
	 */
	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
		synchronized (evaluator) {
	  		return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
		}
	}
	
  /**
   * Generate a parser from a Rascal syntax definition (a set of production rules).
   * 
   * @param monitor a progress monitor; this method will contribute 100 work units
   * @param loc     a location for error reporting
   * @param name    the name of the parser for use in code generation and for later reference
   * @param definition a map of syntax definitions (which are imports in the Rascal grammar)
   * @return A parser class, ready for instantiation
   */
	public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition) {
		Profiler profiler = evaluator.getConfiguration().getGeneratorProfilingProperty() ? new Profiler(evaluator) : null;

		try {
			if (profiler != null) {
				profiler.start();
			}
			IConstructor grammar = IRascalValueFactory.getInstance().grammar(definition);
			debugOutput(grammar, System.getProperty("java.io.tmpdir") + "/grammar.trm");
			return getNewParser(monitor, loc, name, grammar);
		} 
		catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} 
		catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
		} 
		finally {
			if (profiler != null) {
				profiler.pleaseStop();
				evaluator.getOutPrinter().println("PROFILE:");
				profiler.report();
				profiler = null;
			}
		}
	}

  /**
   * Generate a parser from a Rascal grammar.
   * 
   * @param monitor a progress monitor; this method will contribute 100 work units
   * @param loc     a location for error reporting
   * @param name    the name of the parser for use in code generation and for later reference
   * @param grammar a grammar
   * @return A parser class, ready for instantiation
   */
	public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(IRascalMonitor monitor, ISourceLocation loc, String name, IConstructor grammar) {
		try {
			String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
			
			IString classString;
			synchronized (evaluator) {
				classString = (IString) evaluator.call(monitor, "newGenerate", vf.string(packageName), vf.string(normName), grammar);
			}
			debugOutput(classString, System.getProperty("java.io.tmpdir") + "/parser.java");
			
			return bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
		} catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
		}
	}

	/**
   * Generate a parser from a Rascal grammar and write it to disk
   * 
   * @param monitor a progress monitor; this method will contribute 100 work units
   * @param loc     a location for error reporting
   * @param name    the name of the parser for use in code generation and for later reference
   * @param grammar a grammar
   * @return A parser class, ready for instantiation
	 * @throws IOException
   */
  public void writeNewParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition, ISourceLocation target) throws IOException {
	try (OutputStream out = URIResolverRegistry.getInstance().getOutputStream(target, false)) {
		String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
		IString classString;
		IConstructor grammar = IRascalValueFactory.getInstance().grammar(definition);

		synchronized (evaluator) {
			classString = (IString) evaluator.call(monitor, "newGenerate", vf.string(packageName), vf.string(normName), grammar);
		}
		debugOutput(classString, System.getProperty("java.io.tmpdir") + "/parser.java");
		
		bridge.compileJava(loc, packageName + "." + normName, classString.getValue(), out);
	} catch (ClassCastException e) {
		throw new ImplementationError("parser generator:" + e.getMessage(), e);
	} catch (Throw e) {
		throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
	}
}

	public IString createHole(IConstructor part, IInteger size) {
		synchronized (evaluator) {
			return (IString) evaluator.call("createHole", part, size);
		}
	}
}
