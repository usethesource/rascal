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
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
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
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, out, out, scope,heap);
		this.evaluator.getConfiguration().setRascalJavaClassPathProperty(config.getRascalJavaClassPathProperty());
		this.evaluator.getConfiguration().setGeneratorProfiling(config.getGeneratorProfilingProperty());
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		
		this.evaluator.setBootstrapperProperty(true);
		this.bridge = new JavaBridge(loaders, factory, config);
		this.vf = factory;
		
		monitor.jobStart("Loading parser generator", 100, 139);
		try {
			evaluator.doImport(monitor, "lang::rascal::grammar::ParserGenerator");
			evaluator.doImport(monitor, "lang::rascal::grammar::ConcreteSyntax");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Modules");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Priorities");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Regular");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Keywords");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Literals");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Parameters");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Symbols");
			evaluator.doImport(monitor, "analysis::grammars::Ambiguity");
		}
		finally {
			monitor.jobEnd(true);
		}
	}
	
	public void setGeneratorProfiling(boolean f) {
		evaluator.getConfiguration().setGeneratorProfiling(f);
	}
	
	public IValue diagnoseAmbiguity(IConstructor parseForest) {
		return evaluator.call("diagnose", parseForest);
	}
	
	/**
	 * Generate a parser from a Rascal syntax definition (a set of production rules).
	 * 
	 * @param monitor a progress monitor; this method will contribute 100 work units
	 * @param loc     a location for error reporting
	 * @param name    the name of the parser for use in code generation and for later reference
	 * @param imports a set of syntax definitions (which are imports in the Rascal grammar)
	 * @return
	 */
	public Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition) {
		monitor.jobStart("Generating parser:" + name, 100, 90);
		
		try {
			monitor.jobStep("Importing and normalizing grammar:" + name, 30);
			IConstructor grammar = getGrammarFromModules(monitor, name, definition);
			debugOutput(grammar, System.getProperty("java.io.tmpdir") + "/grammar.trm");
			String normName = name.replaceAll("::", "_");
			monitor.jobStep("Generating java source code for parser: " + name,30);
			IString classString = (IString) evaluator.call(monitor, "generateObjectParser", vf.string(packageName), vf.string(normName), grammar);
			debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
			monitor.jobStep("Compiling generated java code: " + name, 30);
			return bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
		}  catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
		} finally {
			monitor.jobEnd(true);
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
		return (IConstructor) evaluator.call(monitor, "modules2grammar", vf.string(main), modules);
	}
	
	public IConstructor getExpandedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		IConstructor g = getGrammarFromModules(monitor, main, definition);
		
		monitor.jobStep("Expanding keywords", 10);
		g = (IConstructor) evaluator.call(monitor, "expandKeywords", g);
		monitor.jobStep("Adding regular productions",10);
		g = (IConstructor) evaluator.call(monitor, "makeRegularStubs", g);
		monitor.jobStep("Expanding regulars", 10);
		g = (IConstructor) evaluator.call(monitor, "expandRegularSymbols", g);
		monitor.jobStep("Expanding parametrized symbols");
		g = (IConstructor) evaluator.call(monitor, "expandParameterizedSymbols", g);
		monitor.jobStep("Defining literals");
		g = (IConstructor) evaluator.call(monitor, "literals", g);
		return g;
	}

	public ISet getNestingRestrictions(IRascalMonitor monitor,
			IConstructor g) {
		return (ISet) evaluator.call(monitor, "doNotNest", g);
	}

	/** 
	 * Produces the name generated by the parser generator for a parse method for the given symbol
	 */
	public String getParserMethodName(IConstructor symbol) {
	  return ((IString) evaluator.call((IRascalMonitor) null, "getParserMethodName", symbol)).getValue();
	}
	
	/**
	 * Converts the parse tree of a symbol to a UPTR symbol
	 */
	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
	  return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
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
		monitor.jobStart("Generating parser:" + name, 100, 130);
		Profiler profiler = evaluator.getConfiguration().getGeneratorProfilingProperty() ? new Profiler(evaluator) : null;

		try {
			monitor.jobStep("Importing and normalizing grammar:" + name, 30);
			if (profiler != null) {
				profiler.start();
			}
			IConstructor grammar = IRascalValueFactory.getInstance().grammar(definition);
			debugOutput(grammar, System.getProperty("java.io.tmpdir") + "/grammar.trm");
			return getNewParser(monitor, loc, name, grammar);
		} catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
		} finally {
			monitor.jobEnd(true);
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
		monitor.jobStart("Generating parser:" + name, 100, 60);

		try {
			String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
			monitor.jobStep("Generating java source code for parser: " + name,30);
			IString classString = (IString) evaluator.call(monitor, "newGenerate", vf.string(packageName), vf.string(normName), grammar);
			debugOutput(classString, System.getProperty("java.io.tmpdir") + "/parser.java");
			monitor.jobStep("Compiling generated java code: " + name, 30);
			return bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
		} catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
		} finally {
			monitor.jobEnd(true);
		}
	}

	public String createHole(IConstructor part, int size) {
	    return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
	}
}
