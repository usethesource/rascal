/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.type.TypeFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * This is the way of executing actions for Rascal syntax definitions. Each function
 * that returns a non-terminal type and is named one of the constructor names of one
 * of the alternatives and has the same argument types as the syntax production will
 * be called when a certain production is constructed, e.g:
 * 
 * Stat if(Exp e, Stat thenPart, Stat elsePart);
 * 
 * Also, on ambiguity clusters functions named 'amb' are called with a set[&T] argument
 * for the alternatives, e.g. Stat amb(set[Stat] alternatives);
 * 
 * Also, on entering a production the 'enter' function is called with a reifed type argument 
 * for the production type that is entered: void enter(type[Stat.If] prod);
 * 
 * Also on exiting a production the 'exit' function is called, similarly:
 * void exit(type[Stat.If] prod);
 * 
 * Note that RascalFunctionActionExecutors use functions visible from the call site of the parse
 * function.
 */
public class RascalFunctionActionExecutor implements IActionExecutor<ITree> {
	private final static TypeFactory TF = TypeFactory.getInstance();
	private final RascalExecutionContext rex;

	public RascalFunctionActionExecutor(RascalExecutionContext rex) {
		this.rex = rex;
	}
	
	public void completed(Object environment, boolean filtered) {

	}

	public Object createRootEnvironment() {
		//rex.getStdErr().println("Cannot execute createRootEnvironment");
		return null;
		
		//return rex.getEvaluatorContext().getCurrentEnvt();		// TODO: change
	}

	public Object enteringListNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringListProduction(Object production, Object env) {
		return env;
	}

	public Object enteringNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringProduction(Object production, Object env) {
		return env;
	}

	public void exitedListProduction(Object production, boolean filtered, Object environment) {

	}

	public void exitedProduction(Object production, boolean filtered, Object environment) {
	}

	public ITree filterAmbiguity(ITree ambCluster, Object environment) {
		ISet alts = (ISet) ambCluster.get("alternatives");
		
		if (alts.size() == 0) {
			return null;
		}
		
		//TODO: implement this for compiled code:
		
//		Environment env = (Environment) environment;
//		
//		Result<IValue> var = null; //env.getVariable("amb");
//		
//		if (var != null && var instanceof ICallableValue) {
//			Type type = RascalTypeFactory.getInstance().nonTerminalType(ambCluster);
//			ICallableValue func = (ICallableValue) var;
//			try {
//				Result<IValue> result = func.call(
//						new Type[] {TF.setType(type)}, new IValue[] {alts}, null
//				);
//				
//				if (result.getType().isBottom()) {
//					return ambCluster;
//				}
//				IConstructor r = (IConstructor) result.getValue();
//				if (TreeAdapter.isAmb(r)) {
//					ISet returnedAlts = TreeAdapter.getAlternatives(r);
//					if (returnedAlts.size() == 1) {
//						return (IConstructor) returnedAlts.iterator().next();
//					}
//					else if (returnedAlts.size() == 0) {
//						return null;
//					}
//					else {
//						return r;
//					}
//				}
//					
//				return (IConstructor) result.getValue();
//			}
//			catch (ArgumentMismatch e) {
//				return ambCluster;
//			}
//		}
		
		return ambCluster;
	}

	@Override
	public ITree filterCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListAmbiguity(ITree ambCluster, Object environment) {
		return filterAmbiguity(ambCluster, environment);
	}

	@Override
	public ITree filterListCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListProduction(ITree tree, Object environment) {
		return tree;
	}

	@Override
	public ITree filterProduction(ITree tree,
			Object environment) {
		String cons = TreeAdapter.getConstructorName(tree);
		
		//TODO: implement this for compiled code:
		
//		if (cons != null) {
//			Environment env = (Environment) environment;
//			Result<IValue> var = env.getVariable(cons);
//			
//			if (var != null && var instanceof ICallableValue) {
//				ICallableValue function = (ICallableValue) var;
//				
//				
//				try{
//					Result<IValue> result = null;
//					if(TreeAdapter.isContextFree(tree)){
//						// For context free trees, try it without layout and literal arguments first.
//						result = call(function, TreeAdapter.getASTArgs(tree));
//					}
//					
//					if (result == null){
//						result = call(function, TreeAdapter.getArgs(tree));
//					}
//					
//					if (result == null) {
//						return tree;
//					}
//					
//					if (result.getType().isBottom()) {
//						return tree;
//					}
//					
//					if (!(result.getType() instanceof NonTerminalType 
//							&& SymbolAdapter.isEqual(((NonTerminalType) result.getType()).getSymbol(), TreeAdapter.getType(tree)))) {
//						// do not call the function if it does not return the right type
//						return tree;
//					}
//					
//					return (IConstructor) result.getValue();
//				} catch(Filtered f){
//					return null;
//				}
//			}
//		}
		
		return tree;
	}

//TODO: implement this for compiled code:
	
//	private static Result<IValue> call(ICallableValue function, IList args) {
//		try{
//			int nrOfArgs = args.length();
//			Type[] types = new Type[nrOfArgs];
//			IValue[] actuals = new IValue[nrOfArgs];
//			
//			for(int i = nrOfArgs - 1; i >= 0; --i){
//				IValue arg = args.get(i);
//				types[i] = RascalTypeFactory.getInstance().nonTerminalType((IConstructor) arg);
//				actuals[i] = arg;
//			}
//			
//			return function.call(types, actuals, null);
//		}catch(MatchFailed e){
//			return null;
//		}catch(Failure f){
//			return null;
//		}
//	}

	public boolean isImpure(Object rhs) {
		return true;
	}

}
