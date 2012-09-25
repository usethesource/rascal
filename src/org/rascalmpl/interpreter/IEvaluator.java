/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.PreModule;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.callbacks.IConstructorDeclared;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.JavaBridge;

/**
 * TODO: This interface was used by the
 * {@link org.rascalmpl.interpreter.debug.DebuggingDecorator}, which is now
 * removed. This interface should be reiterated together with
 * {@link org.rascalmpl.interpreter.IEvaluatorContext}.
 * 
 * TODO: For refactoring means, this interface currently contains all public
 * methods of {@link Evaluator} that are used throughout the interpreter.
 * Interface needs to be properly split up in different compoments.
 */
public interface IEvaluator<T> extends IEvaluatorContext {

	/**
	 * Notify subscribers about a suspension caused while interpreting the program.
	 * @param currentAST the AST that is causes the suspension.
	 */
	public void notifyAboutSuspension(AbstractAST currentAST);

	/*
	 * Evaluation.
	 */
	public Result<IValue> eval(Statement stat);
	public Result<IValue> eval(IRascalMonitor monitor, Command command);	
	public Module evalRascalModule(AbstractAST x, String name);	
	
	/*
	 * Indentations. Methods solely used in {@link StringTemplateConverter}.
	 */
	public void indent(String n);	
	public void unindent();
	public String getCurrentIndent();
	
	/*
	 * Methods solely used in {@link Cases}.
	 */
	public boolean matchAndEval(Result<IValue> subject, Expression pat, Statement stat);
	public boolean matchEvalAndReplace(Result<IValue> subject, Expression pat, List<Expression> conditions, Expression replacementExpr);

	/*
	 * Module stuff.
	 */
	public void addImportToCurrentModule(AbstractAST x, String name);
	public void extendCurrentModule(ISourceLocation src, String name);
	public ModuleEnvironment getCurrentModuleEnvironment();

	public String getModuleName(Module module);
	public String getModuleName(PreModule module);	
	public String getCachedParser(Module preModule);
	public String getCachedParser(PreModule preModule);	
	public boolean needBootstrapParser(Module preModule);
	public boolean needBootstrapParser(PreModule preModule);
		
	/*
	 * Misc.
	 */
	public Stack<Accumulator> __getAccumulators();
	public ModuleEnvironment __getRootScope();
	public boolean __getConcreteListsShouldBeSpliced();
	public GlobalEnvironment __getHeap();
	public boolean __getInterrupt();
	public void __setInterrupt(boolean interrupt);	
	public JavaBridge __getJavaBridge();
	public TypeDeclarationEvaluator __getTypeDeclarator();
	public IValueFactory __getVf();

	public void updateProperties();

	public void printHelpMessage(PrintWriter out);

	public IRascalMonitor getMonitor();
	public IRascalMonitor setMonitor(IRascalMonitor monitor);	
	
	public void notifyConstructorDeclaredListeners();
	
	public IConstructor parseObject(IConstructor startSort, IMap robust, URI location, char[] input);
	
	public Module preParseModule(URI location, ISourceLocation cause);
		
	public void pushEnv();
	public Environment pushEnv(Statement s);

	public List<ClassLoader> getClassLoaders();
	
	public IConstructor getGrammar(Environment env);
	
	public RascalURIResolver getRascalResolver();

	public IConstructor parseCommand(IRascalMonitor monitor, String command,
			URI location);

	public IConstructor parseModule(IRascalMonitor monitor, URI location,
			ModuleEnvironment env) throws IOException;

	public void registerConstructorDeclaredListener(IConstructorDeclared iml);

	public Result<IValue> eval(IRascalMonitor monitor, String command, URI location);

	public IValue call(IRascalMonitor monitor, String module, String name, IValue... args);

	public IValue call(IRascalMonitor monitor, String name, IValue... args);

	public IConstructor parseCommands(IRascalMonitor monitor, String commands,
			URI location);

	public Result<IValue> evalMore(IRascalMonitor monitor, String commands,
			URI location);

	public IConstructor parseModuleWithoutIncludingExtends(IRascalMonitor monitor,
			char[] data, URI location, ModuleEnvironment env);

	public IConstructor getGrammar(IRascalMonitor monitor, URI uri);

	public IValue call(String name, IValue... args);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			IMap robust, String input, ISourceLocation loc);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			IMap robust, String input);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			IMap robust, URI location);

}
