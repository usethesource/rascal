/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;

public class TestExecutor {

	private final Evaluator eval;

	public TestExecutor(Evaluator eval, ITestResultListener testResultListener){
		super();

		this.eval = eval;
		Execute.setTestResultListener(testResultListener);
	}

	public void test(String moduleName) {
		
		IValueFactory vf = eval.getValueFactory();
		ISourceLocation src;
		try {
			src = vf.sourceLocation("rascal", "", moduleName.replaceAll("::",  "/") + ".rsc");
			eval.call("executeTests", src);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void test() {
//		ModuleEnvironment topModule = (ModuleEnvironment) eval.getCurrentEnvt().getRoot();
//
//		runTests(topModule, topModule.getTests());
//
//		for (String i : topModule.getImports()) {
//			ModuleEnvironment mod = topModule.getImport(i);
//			
//			if (mod != null) {
//			  runTests(mod, mod.getTests());
//			}
//		}
//	}
//
//	private void runTests(ModuleEnvironment env, List<AbstractFunction> tests) {
//		testResultListener.start(tests.size());
//
////		try {
//		for (int i = tests.size() - 1; i >= 0; i--) {
//		  AbstractFunction test = tests.get(i);
//		  if (test.hasTag("ignore") || test.hasTag("Ignore") || test.hasTag("ignoreInterpreter") || test.hasTag("IgnoreInterpreter")) {
//			  continue;
//		  }
//
//		  try{
//		    QuickCheck qc = QuickCheck.getInstance();
//		    StringWriter sw = new StringWriter();
//		    PrintWriter out = new PrintWriter(sw);
//		    int maxDepth = Cobra.readIntTag(test, Cobra.MAXDEPTH, 5);
//		    int tries = Cobra.readIntTag(test, Cobra.TRIES, 500);
//
//		    boolean result = qc.quickcheck(test, maxDepth, tries, false, out);
//		    if (!result) {
//		      out.flush();
//		      testResultListener.report(false, test.getName(), test.getAst().getLocation(), sw.getBuffer()
//		          .toString(), null);
//		    } else {
//		      testResultListener.report(true, test.getName(), test.getAst().getLocation(), sw.getBuffer()
//		          .toString(), null);
//		    }
//		  }
//		  catch(StaticError e) {
//		    testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage(), e);
//		  }
//		  catch(Throw e){
//		    testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage(), e);
//		  }
//		  catch(Throwable e){
//		    testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage(), e);
//		  }
//		}
//		//		}
//		//		finally {
//		testResultListener.done();
//		//		}
//	}
//



}
