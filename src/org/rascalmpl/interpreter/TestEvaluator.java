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
package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.test.infrastructure.QuickCheck;
import org.rascalmpl.test.infrastructure.QuickCheck.TestResult;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TestEvaluator {

    private final Evaluator eval;
    private final ITestResultListener testResultListener;

    public TestEvaluator(Evaluator eval, ITestResultListener testResultListener){
        super();

        this.eval = eval;
        this.testResultListener = testResultListener;
    }

    public boolean test(String moduleName) {
        ModuleEnvironment topModule = eval.getHeap().getModule(moduleName);

        if (topModule != null) {
            runTests(topModule, topModule.getTests());
            return true;
        }
        
        return false;
    }

    public void test() {
        ModuleEnvironment topModule = (ModuleEnvironment) eval.getCurrentEnvt().getRoot();

        runTests(topModule, topModule.getTests());
        List<String> imports = new ArrayList<>(topModule.getImports());
        Collections.shuffle(imports);

        for (String i : imports) {
            ModuleEnvironment mod = topModule.getImport(i);

            if (mod != null) {
                runTests(mod, mod.getTests());
            }
        }
    }

    public static int readIntTag(AbstractFunction test, String key, int defaultVal) {
        if (test.hasTag(key)) {
            int result = Integer.parseInt(((IString) test.getTag(key)).getValue());
            if (result < 1) {
                throw new IllegalArgumentException(key + " smaller than 1");
            }
            return result;
        } else {
            return defaultVal;
        }
    }

    

    private void runTests(ModuleEnvironment env, List<AbstractFunction> tests) {
        testResultListener.start(env.getName(), tests.size());
        // first, let's shuffle the tests
        tests = new ArrayList<>(tests); // just to be sure, clone the list
        Collections.shuffle(tests);

        QuickCheck qc = new QuickCheck(new Random(), eval.__getVf());  
        for (AbstractFunction test: tests) {
            if (test.hasTag("ignore") || test.hasTag("Ignore") || test.hasTag("ignoreInterpreter") || test.hasTag("IgnoreInterpreter")) {
                testResultListener.ignored(test.getName(), test.getAst().getLocation());
                continue;
            }

            try{
                int maxDepth = readIntTag(test, QuickCheck.MAXDEPTH, 5);
                int maxWidth = readIntTag(test, QuickCheck.MAXWIDTH, 5);
                int tries = readIntTag(test, QuickCheck.TRIES, 500);
                String expected = null;
                if(test.hasTag(QuickCheck.EXPECT_TAG)){
                    expected = ((IString) test.getTag(QuickCheck.EXPECT_TAG)).getValue();
                } 
                TestResult result = qc.test(test.getEnv().getName() + "::" + test.getName(), test.getFormals(), expected, (Type[] actuals, IValue[] args) -> {
                    try {
                        IValue testResult = test.call(actuals, args, null).getValue();
                        if ((testResult instanceof IBool) && ((IBool)testResult).getValue()) {
                            return QuickCheck.SUCCESS;
                        }
                        else {
                            return new TestResult(false, null);
                        }
                    }
                    catch (Throwable e) {
                        return new TestResult(false, e);
                    }
                }, env.getRoot().getStore(), tries, maxDepth, maxWidth);
                
                eval.getOutPrinter().flush();
                eval.getErrorPrinter().flush();
                
                if (!result.succeeded()) {
                    StringWriter sw = new StringWriter();
                    PrintWriter out = new PrintWriter(sw);
                    result.writeMessage(out);
                    out.flush();
                    testResultListener.report(false, test.getName(), test.getAst().getLocation(), sw.getBuffer().toString(), result.thrownException());
                } else {
                    testResultListener.report(true, test.getName(), test.getAst().getLocation(), "test succeeded", null);
                }
            }
            catch(Throwable e){
                testResultListener.report(false, test.getName(), test.getAst().getLocation(), e.getMessage(), e);
            }
            eval.getOutPrinter().flush();
            eval.getErrorPrinter().flush();
        }
        testResultListener.done();
    }
}
