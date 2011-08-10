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
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;
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
		ModuleEnvironment topModule = (ModuleEnvironment) eval.getHeap().getModule(moduleName);
		
		if (topModule != null) {
			runTests(topModule, topModule.getTests());

			for (String i : topModule.getImports()) {
				ModuleEnvironment mod = topModule.getImport(i);
				runTests(mod, mod.getTests());
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
	
	private void runTests(ModuleEnvironment env, List<AbstractFunction> tests) {
		testResultListener.start(tests.size());

		try {
			for(int i = tests.size() - 1; i >= 0; i--) {
				AbstractFunction test = tests.get(i);

				try{
					Result<IValue> result = test.call(new Type[0], new IValue[0]);

					if (result.getType().isBoolType()) {
						boolean success = ((IBool) result.getValue()).getValue();
						testResultListener.report(success, test.getName(), test.getAst().getLocation());
					}
				}
				catch(StaticError e) {
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e);
				}
				catch(Throw e){
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e);
				}
				catch(Throwable e){
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e);
				}
			}
		}
		finally {
			testResultListener.done();
		}
	}
}
