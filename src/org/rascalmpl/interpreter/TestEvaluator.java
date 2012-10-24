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
 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.library.cobra.Cobra;
import org.rascalmpl.library.cobra.QuickCheck;

public class TestEvaluator {

	private final Evaluator eval;
	private final ITestResultListener testResultListener;

	public TestEvaluator(Evaluator eval, ITestResultListener testResultListener){
		super();

		this.eval = eval;
		this.testResultListener = testResultListener;
	}

	public void test(String moduleName) {
		ModuleEnvironment topModule = eval.getHeap().getModule(moduleName);

		if (topModule != null) {
			runTests(topModule, topModule.getTests());
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
					QuickCheck qc = QuickCheck.getInstance();
					StringWriter sw = new StringWriter();
					PrintWriter out = new PrintWriter(sw);
					int maxDepth = Cobra.readIntTag(test, Cobra.MAXDEPTH, 5);
					int tries = Cobra.readIntTag(test, Cobra.TRIES, 100);

					boolean result = qc.quickcheck(test, maxDepth, tries, false, out);
					if (!result) {
						out.flush();
						testResultListener.report(false, test.getName(), test.getAst().getLocation(), sw.getBuffer()
								.toString());
					} else {
						testResultListener.report(true, test.getName(), test.getAst().getLocation(), sw.getBuffer()
								.toString());
					}
				}
				catch(StaticError e) {
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage());
				}
				catch(Throw e){
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage());
				}
				catch(Throwable e){
					testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage());
				}
			}
		}
		finally {
			testResultListener.done();
		}
	}




}
