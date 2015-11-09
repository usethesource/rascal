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
import java.net.URISyntaxException;
import java.util.HashMap;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;							// TODO: remove import: YES
import org.rascalmpl.interpreter.control_exceptions.Throw;			// TODO: remove import: LATER
import org.rascalmpl.interpreter.env.GlobalEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.env.ModuleEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.load.StandardLibraryContributor;	// remove import: NO
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.JavaBridge;					// remove import: NO
import org.rascalmpl.library.Prelude;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;

public class ParserGenerator {
	private Evaluator evaluator;
	private final JavaBridge bridge;
	private final IValueFactory vf;
	private final TypeFactory tf;
	private ISourceLocation parserGeneratorBinaryLocation;
	private RVM rvmParserGenerator;
	private Function getParserMethodNameFunction;
	private Function newGenerateFunction;
	private Function createHoleFunction;
	private static final String packageName = "org.rascalmpl.java.parser.object";
	private static final boolean debug = false;
	private static final boolean useCompiledParserGenerator = true;

	public ParserGenerator(RascalExecutionContext rex) {
		this.vf = rex.getValueFactory();
		this.tf = TypeFactory.getInstance();
		this.bridge = new JavaBridge(rex.getClassLoaders(), rex.getValueFactory(), rex.getConfiguration());

		if(useCompiledParserGenerator){
			try {
				parserGeneratorBinaryLocation = vf.sourceLocation("compressed+boot", "", "ParserGenerator.rvm.ser.gz");
				//parserGeneratorBinaryLocation = vf.sourceLocation("compressed+home", "", "/bin/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rvm.ser.gz");

			} catch (URISyntaxException e) {
				throw new RuntimeException("Cannot initialize: " + e.getMessage());
			}
			RascalExecutionContext rex2 = new RascalExecutionContext("$parsergenerator$", vf, rex.getStdOut(), rex.getStdErr());
		
			rvmParserGenerator = RVM.readFromFileAndInitialize(parserGeneratorBinaryLocation, rex2);
			
			Type symSort = RascalTypeFactory.getInstance().nonTerminalType(vf.constructor(RascalValueFactory.Symbol_Sort, vf.string("Sym")));
			getParserMethodNameFunction = rvmParserGenerator.getFunction("getParserMethodName", tf.stringType(), tf.tupleType(symSort));
			if(getParserMethodNameFunction == null){
				throw new CompilerError("Function getParserMethodName not found");
			}

			newGenerateFunction = rex2.getRVM().getFunction("newGenerate", tf.stringType(), 
					tf.tupleType(tf.stringType(), tf.stringType(), tf.abstractDataType(rex.getTypeStore(), "Grammar")));
			if(newGenerateFunction == null){
				throw new CompilerError("Function newGenerate not found");
			}

			Type cpLex = RascalTypeFactory.getInstance().nonTerminalType(vf.constructor(RascalValueFactory.Symbol_Lex, vf.string("ConcretePart")));
			createHoleFunction = rvmParserGenerator.getFunction("createHole", tf.stringType(), 
																 			  tf.tupleType(cpLex, tf.integerType()));
			if(createHoleFunction == null){
				throw new CompilerError("Function createHole not found");
			}
		} else {
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment scope = new ModuleEnvironment("$parsergenerator$", heap);
			this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), rex.getStdOut(), rex.getStdErr(), scope, heap);
			evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		this.evaluator.setBootstrapperProperty(true);

			IRascalMonitor monitor = rex.getMonitor();

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
	}
	
//	public IValue diagnoseAmbiguity(IConstructor parseForest) {
//		return evaluator.call("diagnose", parseForest);
//	}
	
//	/**
//	 * Generate a parser from a Rascal syntax definition (a set of production rules).
//	 * 
//	 * @param monitor a progress monitor; this method will contribute 100 work units
//	 * @param loc     a location for error reporting
//	 * @param name    the name of the parser for use in code generation and for later reference
//	 * @param imports a set of syntax definitions (which are imports in the Rascal grammar)
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public Class<IGTD<IConstructor, ITree, ISourceLocation>> getParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition) {
//		monitor.startJob("Compiled -- Generating parser: " + name, 100, 90);
//
//		try {
//			monitor.event("Importing and normalizing grammar:" + name, 30);
//			IConstructor grammar = convertMapToGrammar(definition);
//			debugOutput(grammar.toString(), System.getProperty("java.io.tmpdir") + "/grammar.trm");
//			String normName = name.replaceAll("::", "_");
//			monitor.event("Generating java source code for parser: " + name,30);
//			IString classString = vf.string("");
//			if(fullyCompiled){
//				
//			} else {
//				classString = (IString) evaluator.call(monitor, "generateObjectParser", vf.string(packageName), vf.string(normName), grammar);
//			}
//			debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
//			monitor.event("Compiling generated java code: " + name, 30);
//			Class<IGTD<IConstructor, ITree, ISourceLocation>> p = bridge.compileJava(loc, packageName + "." + normName, getClass(), classString.getValue());
//
//			String className = normName;
//			Class<?> clazz;
//			for (ClassLoader cl: evaluator.getClassLoaders()) {
//				try {
//					clazz = cl.loadClass(className);
//					return (Class<IGTD<IConstructor, ITree, ISourceLocation>>) clazz.newInstance();
//
//				} catch (ClassNotFoundException e) {
//					continue;
//				} catch (InstantiationException e) {
//					throw new CompilerError("could not instantiate " + className + " to valid IGTD parser: " + e.getMessage());
//				} catch (IllegalAccessException e) {
//					throw new CompilerError("not allowed to instantiate " + className + " to valid IGTD parser: " + e.getMessage());
//				}
//			}
//			throw new CompilerError("class for cached parser " + className + " could not be found");
//
//		}  catch (ClassCastException e) {
//			throw new CompilerError("parser generator:" + e.getMessage() + ", " + e);
//		} catch (Throw e) {
//			throw new CompilerError("parser generator: " + e.getMessage() + e.getTrace());
//		} finally {
//			monitor.endJob(true);
//		}
//	}

//	/**
//	 * Uses the user defined syntax definitions to generate a parser for Rascal that can deal
//	 * with embedded concrete syntax fragments
//	 * 
//	 * Note that this method works under the assumption that a normal parser was generated before!
//	 * The class that this parser generates will inherit from that previously generated parser.
//	 * @param rex TODO
//	 */
//	public Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getRascalParser(ISourceLocation loc, String name, IMap definition, IGTD<IConstructor, IConstructor, ISourceLocation> objectParser, RascalExecutionContext rex) {
//		try {
//			rex.event("Importing and normalizing grammar: " + name, 10);
//			IConstructor grammar = convertMapToGrammar(definition);
//			String normName = name.replaceAll("::", "_");
//			rex.event("Generating java source code for Rascal parser:" + name, 10);
//			IString classString = (IString) evaluator.call(rex.getMonitor(), "generateMetaParser", vf.string(packageName), vf.string("$Rascal_" + normName), vf.string(packageName + "." + normName), grammar);
//			debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/metaParser.java");
//			rex.event("compiling generated java code: " + name, 10);
//			return bridge.compileJava(loc, packageName + ".$Rascal_" + normName, objectParser.getClass(), classString.getValue());
//		}  catch (ClassCastException e) {
//			throw new CompilerError("meta parser generator:" + e.getMessage() + e);
//		} catch (Throw e) {
//			throw new CompilerError("meta parser generator: " + e.getMessage() + e.getTrace());
//		}
//	}

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

	/** 
	 * Produces the name generated by the parser generator for a parse method for the given symbol
	 * @param rex TODO
	 */
	public String getParserMethodName(IConstructor symbol, RascalExecutionContext rex) {
	  if(useCompiledParserGenerator){
		  return ((IString) rvmParserGenerator.executeFunction(getParserMethodNameFunction, new IValue[]{ symbol }, new HashMap<String, IValue>())).getValue();
	  } else {
		  return ((IString) evaluator.call((IRascalMonitor) null, "getParserMethodName", symbol)).getValue();
	  }
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
 * @param rex TODO
   * @return A parser class, ready for instantiation
   */
  public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition, RascalExecutionContext rex) {
  		return getNewParser(loc, name, convertMapToGrammar(definition),  rex);
  }

  /**
   * Generate a parser from a Rascal grammar.
 * @param loc     a location for error reporting
 * @param name    the name of the parser for use in code generation and for later reference
 * @param grammar a grammar
 * @param rex TODO
   * 
   * @return A parser class, ready for instantiation
   */
  public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(ISourceLocation loc, String name, IConstructor grammar, RascalExecutionContext rex) {
	  rex.startJob("Compiled -- Generating new parser:" + name, 100, 60);
	  try {

		  String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
		  rex.event("Generating java source code for parser: " + name,30);
		  IString classString;
		  if(useCompiledParserGenerator){
			  classString = (IString) rvmParserGenerator.executeFunction(newGenerateFunction, new IValue[]{ vf.string(packageName), vf.string(normName), grammar }, new HashMap<String, IValue>());
		  } else {
			  classString = (IString) evaluator.call(rex.getMonitor(), "newGenerate", vf.string(packageName), vf.string(normName), grammar);
		  }
		  debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
		  rex.event("Compiling generated java code: " + name, 30);
		  return bridge.compileJava(loc, packageName + "." + normName, this.getClass(), classString.getValue());
  	}  catch (ClassCastException e) {
  		throw new CompilerError("parser generator:" + e.getMessage() + e);
  	} catch (Throw e) {
  		throw new CompilerError("parser generator: " + e.getMessage() + e.getTrace());
  	} finally {
  		rex.endJob(true);
  	}
  }

  public String createHole(IConstructor part, int size, RascalExecutionContext rex) {
	  if(useCompiledParserGenerator){
		  return ((IString) rvmParserGenerator.executeFunction(createHoleFunction, new IValue[]{ part, vf.integer(size) }, new HashMap<String, IValue>())).getValue();
	  } else {
		  return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
	  }
  }
}
