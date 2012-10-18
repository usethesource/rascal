/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.values.ValueFactoryFactory;

public class ParserGenerator {
	private final Evaluator evaluator;
	private final JavaBridge bridge;
	private final IValueFactory vf;
	private static final String packageName = "org.rascalmpl.java.parser.object";
	private static final boolean debug = true;

	public ParserGenerator(IRascalMonitor monitor, PrintWriter out, List<ClassLoader> loaders, IValueFactory factory) {
		this.bridge = new JavaBridge(loaders, factory);
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment scope = new ModuleEnvironment("___parsergenerator___", heap);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), out, out, scope,heap);
		this.vf = factory;
		
		monitor.startJob("Loading parser generator", 100, 139);
		try {
			evaluator.doImport(monitor, "lang::rascal::grammar::ParserGenerator");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Modules");
			evaluator.doImport(monitor, "lang::rascal::grammar::Assimilator");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Priorities");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Regular");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Keywords");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Literals");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Parameters");
			evaluator.doImport(monitor, "Ambiguity");
		}
		catch (Throwable e) {
			throw new ImplementationError("Exception while loading parser generator: " + e.getMessage(), e);
		}
		finally {
			monitor.endJob(true);
		}
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
	public Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getParser(IRascalMonitor monitor, URI loc, String name, IMap definition) {
		monitor.startJob("Generating parser:" + name, 100, 90);
		
		try {
			monitor.event("Importing and normalizing grammar:" + name, 30);
			IConstructor grammar = getGrammar(monitor, name, definition);
			debugOutput(grammar.toString(), "/tmp/grammar.trm");
			String normName = name.replaceAll("::", "_");
			monitor.event("Generating java source code for parser: " + name,30);
			IString classString = (IString) evaluator.call(monitor, "generateObjectParser", vf.string(packageName), vf.string(normName), grammar);
			debugOutput(classString.getValue(), "/tmp/parser.java");
			monitor.event("Compiling generated java code: " + name, 30);
			return bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
		}  catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("parser generator: " + e.getMessage() + e.getTrace());
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
	public Class<IGTD<IConstructor, IConstructor, ISourceLocation>> getRascalParser(IRascalMonitor monitor, URI loc, String name, IMap definition, IGTD<IConstructor, IConstructor, ISourceLocation> objectParser) {
		try {
			monitor.event("Importing and normalizing grammar: " + name, 10);
			IConstructor grammar = getGrammar(monitor, name, definition);
			String normName = name.replaceAll("::", "_");
			monitor.event("Generating java source code for Rascal parser:" + name, 10);
			IString classString = (IString) evaluator.call(monitor, "generateMetaParser", vf.string(packageName), vf.string("$Rascal_" + normName), vf.string(packageName + "." + normName), grammar);
			debugOutput(classString.getValue(), "/tmp/metaParser.java");
			monitor.event("compiling generated java code: " + name, 10);
			return bridge.compileJava(loc, packageName + ".$Rascal_" + normName, objectParser.getClass(), classString.getValue());
		}  catch (ClassCastException e) {
			throw new ImplementationError("meta parser generator:" + e.getMessage(), e);
		} catch (Throw e) {
			throw new ImplementationError("meta parser generator: " + e.getMessage() + e.getTrace());
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

	public IRelation getNestingRestrictions(IRascalMonitor monitor,
			IConstructor g) {
		return (IRelation) evaluator.call(monitor, "doNotNest", g);
	}
}
