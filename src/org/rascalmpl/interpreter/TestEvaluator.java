/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Test;
import org.rascalmpl.ast.Test.Labeled;
import org.rascalmpl.ast.Test.Unlabeled;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.StaticError;

public class TestEvaluator {
	private final Evaluator eval;
	private final ITestResultListener testResultListener;
	
	public TestEvaluator(Evaluator eval, ITestResultListener testResultListener){
		super();
		
		this.eval = eval;
		this.testResultListener = testResultListener;
	}

	public void test(String moduleName) {
		Environment old = eval.getCurrentEnvt();
		
		ModuleEnvironment module = eval.getHeap().getModule(moduleName);
		if (module == null) {
			throw new IllegalArgumentException();
		}
		
		try {
			eval.setCurrentEnvt(module);
			eval.pushEnv();
			test();
		}
		finally {
			eval.unwind(module);
			if (old != null) {
				eval.setCurrentEnvt(old);
			}
		}
	}
	
	public void test() {
		ModuleEnvironment topModule = (ModuleEnvironment) eval.getCurrentEnvt().getRoot();
		
		runTests(topModule, topModule.getTests());
		
		for (String i : topModule.getImports()) {
			ModuleEnvironment mod = topModule.getImport(i);
			runTests(mod, mod.getTests());
		}
	}
	
	private void runTests(ModuleEnvironment env, List<Test> tests){
		Environment old = eval.getCurrentEnvt();
		try {
			eval.setCurrentEnvt(env);
			
			Visitor visitor = new Visitor();
			testResultListener.start(tests.size());
			for(int i = tests.size() - 1; i >= 0; i--){
				try {
					eval.pushEnv();
					tests.get(i).accept(visitor);
				}
				finally {
					eval.unwind(env);
				}
			}
		}
		finally {
			if (old != null) {
				eval.setCurrentEnvt(old);
			}
		}
		
		testResultListener.done();
	}
	
	private class Visitor extends NullASTVisitor<Result<IBool>>{
		
		public Visitor(){
			super();
		}
		
		public Result<IBool> visitTestLabeled(Labeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
//			System.err.println("visitTestLabeled: " + x);
			
			try{
				result = x.getExpression().interpret(eval);
			}catch(Throw e){
				testResultListener.report(false, x.toString(), x.getLocation(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), x.getLocation(), e);
			}
			
			testResultListener.report(result.isTrue(), x.toString(), x.getLocation());
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
		
		public Result<IBool> visitTestUnlabeled(Unlabeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
//			System.err.println("visitTestUnlabeled: " + x);
			
			try{
				result = x.getExpression().interpret(eval);
				testResultListener.report(result.isTrue(), x.toString(), x.getLocation());
			}
			catch(StaticError e) {
				testResultListener.report(false, x.toString(), x.getLocation(), e);
			}
			catch(Throw e){
				testResultListener.report(false, x.toString(), x.getLocation(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), x.getLocation(), e);
			}
			
			
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
	}
}
