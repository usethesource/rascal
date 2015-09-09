/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Configuration;						// remove import: NO
import org.rascalmpl.interpreter.Evaluator;							// TODO: remove import: YES
import org.rascalmpl.interpreter.control_exceptions.Throw;			// TODO: remove import: LATER
import org.rascalmpl.interpreter.env.GlobalEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.env.ModuleEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.load.StandardLibraryContributor;	// remove import: NO
import org.rascalmpl.interpreter.utils.JavaBridge;					// remove import: NO
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;

public class ParserGenerator {
	private final Evaluator evaluator;
	private final JavaBridge bridge;
	private final IValueFactory vf;
	private static final String packageName = "org.rascalmpl.java.parser.object";
	private static final boolean debug = false;

	public ParserGenerator(IRascalMonitor monitor, PrintWriter out, List<ClassLoader> loaders, IValueFactory factory, Configuration config) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment scope = new ModuleEnvironment("$parsergenerator$", heap);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), out, out, scope,heap);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		this.evaluator.setBootstrapperProperty(true);
		this.bridge = new JavaBridge(loaders, factory, config);
		this.vf = factory;
		
		monitor.startJob("Compiled -- Loading parser generator, 2", 100, 139);
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
			evaluator.doImport(monitor, "Ambiguity");
		}
		finally {
			monitor.endJob(true);
		}
	}
	
//	public IValue diagnoseAmbiguity(IConstructor parseForest) {
//		return evaluator.call("diagnose", parseForest);
//	}
	
	/**
	 * Generate a parser from a Rascal syntax definition (a set of production rules).
	 * 
	 * @param monitor a progress monitor; this method will contribute 100 work units
	 * @param loc     a location for error reporting
	 * @param name    the name of the parser for use in code generation and for later reference
	 * @param imports a set of syntax definitions (which are imports in the Rascal grammar)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<IGTD<IConstructor, ITree, ISourceLocation>> getParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition) {
		monitor.startJob("Compiled -- Generating parser: " + name, 100, 90);

		try {
			monitor.event("Importing and normalizing grammar:" + name, 30);
			IConstructor grammar = convertMapToGrammar(definition);
			debugOutput(grammar.toString(), System.getProperty("java.io.tmpdir") + "/grammar.trm");
			String normName = name.replaceAll("::", "_");
			monitor.event("Generating java source code for parser: " + name,30);
			IString classString = (IString) evaluator.call(monitor, "generateObjectParser", vf.string(packageName), vf.string(normName), grammar);
			debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
			monitor.event("Compiling generated java code: " + name, 30);
			Class<IGTD<IConstructor, ITree, ISourceLocation>> p = bridge.compileJava(loc, packageName + "." + normName, getClass(), classString.getValue());

			String className = normName;
			Class<?> clazz;
			for (ClassLoader cl: evaluator.getClassLoaders()) {
				try {
					clazz = cl.loadClass(className);
					return (Class<IGTD<IConstructor, ITree, ISourceLocation>>) clazz.newInstance();

				} catch (ClassNotFoundException e) {
					continue;
				} catch (InstantiationException e) {
					throw new CompilerError("could not instantiate " + className + " to valid IGTD parser: " + e.getMessage());
				} catch (IllegalAccessException e) {
					throw new CompilerError("not allowed to instantiate " + className + " to valid IGTD parser: " + e.getMessage());
				}
			}
			throw new CompilerError("class for cached parser " + className + " could not be found");

		}  catch (ClassCastException e) {
			throw new CompilerError("parser generator:" + e.getMessage() + ", " + e);
		} catch (Throw e) {
			throw new CompilerError("parser generator: " + e.getMessage() + e.getTrace());
		} finally {
			monitor.endJob(true);
		}
	}

	/**
	 * Uses the user defined syntax definitions to generate a parser for Rascal that can deal
	 * with embedded concrete syntax fragments
	 * 
	 * Note that this method works under the assumption that a normal parser was generated before!
	 * The class that this parser generates will inherit from that previously generated parser.
	 */
	public Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getRascalParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition, IGTD<IConstructor, IConstructor, ISourceLocation> objectParser) {
		try {
			monitor.event("Importing and normalizing grammar: " + name, 10);
			IConstructor grammar = convertMapToGrammar(definition);
			String normName = name.replaceAll("::", "_");
			monitor.event("Generating java source code for Rascal parser:" + name, 10);
			IString classString = (IString) evaluator.call(monitor, "generateMetaParser", vf.string(packageName), vf.string("$Rascal_" + normName), vf.string(packageName + "." + normName), grammar);
			debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/metaParser.java");
			monitor.event("compiling generated java code: " + name, 10);
			return bridge.compileJava(loc, packageName + ".$Rascal_" + normName, objectParser.getClass(), classString.getValue());
		}  catch (ClassCastException e) {
			throw new CompilerError("meta parser generator:" + e.getMessage() + e);
		} catch (Throw e) {
			throw new CompilerError("meta parser generator: " + e.getMessage() + e.getTrace());
		}
	}

	private void debugOutput(String classString, String file) {
		if (debug) {
			FileOutputStream s = null;
			try {
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
	
	public IConstructor convertMapToGrammar(IMap definition) {
		TypeFactory TF = TypeFactory.getInstance();
		TypeStore TS = new TypeStore();
		Type Grammar = TF.abstractDataType(TS, "Grammar");
		Type Symbol = TF.abstractDataType(TS, "Symbol");
		Type Production = TF.abstractDataType(TS, "Production");
		Type grammar = TF.constructor(TS, Grammar, "grammar", TF.setType(Symbol), "starts", TF.mapType(Symbol, Production), "rules");

		return vf.constructor(grammar, vf.set(), definition);
	}
	
//	public IConstructor getExpandedGrammar(IRascalMonitor monitor, String main, IMap definition) {
//		IConstructor g = convertMapToGrammar(definition);
//		
//		monitor.event("Expanding keywords", 10);
//		g = (IConstructor) evaluator.call(monitor, "expandKeywords", g);
//		monitor.event("Adding regular productions",10);
//		g = (IConstructor) evaluator.call(monitor, "makeRegularStubs", g);
//		monitor.event("Expanding regulars", 10);
//		g = (IConstructor) evaluator.call(monitor, "expandRegularSymbols", g);
//		monitor.event("Expanding parametrized symbols");
//		g = (IConstructor) evaluator.call(monitor, "expandParameterizedSymbols", g);
//		monitor.event("Defining literals");
//		g = (IConstructor) evaluator.call(monitor, "literals", g);
//		return g;
//	}

//	public ISet getNestingRestrictions(IRascalMonitor monitor,
//			IConstructor g) {
//		return (ISet) evaluator.call(monitor, "doNotNest", g);
//	}

	/** 
	 * Produces the name generated by the parser generator for a parse method for the given symbol
	 */
	public String getParserMethodName(IConstructor symbol) {
	  return ((IString) evaluator.call((IRascalMonitor) null, "getParserMethodName", symbol)).getValue();
	}
	
//	/**
//	 * Converts the parse tree of a symbol to a UPTR symbol
//	 */
//	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
//	  return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
//	}
	
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
  		return getNewParser(monitor, loc, name,  convertMapToGrammar(definition));
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
  	monitor.startJob("Compiled -- Generating new parser:" + name, 100, 60);
  	try {

  		String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
  		monitor.event("Generating java source code for parser: " + name,30);
  		IString classString = (IString) evaluator.call(monitor, "newGenerate", vf.string(packageName), vf.string(normName), grammar);
  		debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
  		monitor.event("Compiling generated java code: " + name, 30);
  		return bridge.compileJava(loc, packageName + "." + normName, this.getClass(), classString.getValue());
  	}  catch (ClassCastException e) {
  		throw new CompilerError("parser generator:" + e.getMessage() + e);
  	} catch (Throw e) {
  		throw new CompilerError("parser generator: " + e.getMessage() + e.getTrace());
  	} finally {
  		monitor.endJob(true);
  	}
  }

//  /**
//   * Save a generated parser class to a jar file
//   * 
//   * @param parserClass The parser class
//   * @param outStream An output stream
//   * @throws IOException on IO error
//   */
//  public void saveToJar(Class<IGTD<IConstructor, IConstructor, ISourceLocation>> parserClass, OutputStream outStream) throws IOException {
//	  bridge.saveToJar("", parserClass, StandAloneParser.class, outStream, false);
//  }

  public String createHole(IConstructor part, int size) {
    return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
  }
}
