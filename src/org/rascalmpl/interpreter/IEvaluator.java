/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Anya Helene Bagge - (UiB)
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.debug.IRascalSuspendTriggerListener;
import org.rascalmpl.interpreter.callbacks.IConstructorDeclared;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

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
	
	/*
	 * Indentations. Methods solely used in {@link StringTemplateConverter}.
	 */
	public void indent(IString n);	
	public void unindent();
	public IString getCurrentIndent();
	
	/*
	 * Module stuff.
	 */
	public ModuleEnvironment getCurrentModuleEnvironment();

	/*
	 * Misc.
	 */
	public Stack<Accumulator> __getAccumulators();
	public ModuleEnvironment __getRootScope();
	public GlobalEnvironment __getHeap();
	public void __setInterrupt(boolean interrupt);	
	public JavaBridge __getJavaBridge();
	public void resetJavaBridge();
	public TypeDeclarationEvaluator __getTypeDeclarator();
	public IValueFactory __getVf();
	public TraversalEvaluator __getCurrentTraversalEvaluator();
	public void __pushTraversalEvaluator(TraversalEvaluator te);
	public TraversalEvaluator __popTraversalEvaluator();

	public void updateProperties();

	public void printHelpMessage(PrintWriter out);

	public IRascalMonitor getMonitor();
	public IRascalMonitor setMonitor(IRascalMonitor monitor);	
	
	public void notifyConstructorDeclaredListeners();
	
	
	
	public Environment pushEnv(Statement s);

	public List<ClassLoader> getClassLoaders();
	
	public IConstructor getGrammar(Environment env);
	
	public RascalSearchPath getRascalResolver();

	public ITree parseCommand(IRascalMonitor monitor, String command,
			ISourceLocation location);

	public ITree parseModuleAndFragments(IRascalMonitor monitor, ISourceLocation location) throws IOException;

	public void registerConstructorDeclaredListener(IConstructorDeclared iml);

	public Result<IValue> eval(IRascalMonitor monitor, String command, ISourceLocation location);

	public IValue call(IRascalMonitor monitor, String module, String name, IValue... args);

	public IValue call(IRascalMonitor monitor, String name, IValue... args);

	public IValue call(QualifiedName name, Map<String, IValue> kwArgs, IValue... args);
	
	public IValue call(String name, String module, Map<String, IValue> kwArgs, IValue... args);
	 
	public IConstructor parseCommands(IRascalMonitor monitor, String commands,
			ISourceLocation location);

	public Result<IValue> evalMore(IRascalMonitor monitor, String commands,
			ISourceLocation location);

	public IValue call(String name, IValue... args);
	
	/**
	 * Calls a constructor function, or an overloaded function with the same
	 * signature which overrrides it.
	 */
	public IValue call(String adt, String name, IValue... args);

	public IConstructor parseObject(IConstructor startSort, ISet filters, ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean hasSideEffects, boolean robust);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			ISet filters, String input, ISourceLocation loc,  boolean allowAmbiguity, boolean hasSideEffects, boolean robust);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			ISet filters, String input, boolean allowAmbiguity, boolean hasSideEffects, boolean robust);

	public IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort,
			ISet filters, ISourceLocation location, boolean allowAmbiguity, boolean hasSideEffects, boolean robust);

	/**
	 *  Freeze the global state of this evaluator so that it can no longer be updated.
	 * 
	 *  Any attempt to modify global variables or load/remove/change modules will
	 *  result in an UnsupportedOperationException.
	 *  
	 *  This method is not guaranteed to be thread safe itself, but after it returns
	 *  other methods that do not touch the execution stack should be thread safe.
	 */
	public void freeze();
	
	/**
	 * Fork the evaluator, creating an exact copy with its own stack.
	 * 
	 * Modules and global data will be shared between the old and new environment.
	 * 
	 * The current environment must be frozen (@see{#freeze}) before calling this method.
	 * 
	 * @return A new evaluator, identical to the current one except for the stack
	 */
	public IEvaluator<T> fork();

  public ParserGenerator getParserGenerator();

  public List<IRascalSuspendTriggerListener> getSuspendTriggerListeners();

 
}
