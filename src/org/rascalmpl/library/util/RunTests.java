/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.library.util;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.exception.ParseError;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RunTests {
    private final static TypeFactory TF = TypeFactory.getInstance();
    private final static TypeStore store = new TypeStore();

    //  data TestResult = \testResult(str name, bool success, loc src, str message, list[value] exceptions = []);
    private final static Type TestResult = TF.abstractDataType(store, "TestResult");
    private final static Type Exception = TF.abstractDataType(store, "Exception");
    private final static Type testResult = TF.constructor(store, TestResult, "testResult", TF.stringType(), "name", TF.boolType(), "success", TF.sourceLocationType(), "src");

    public RunTests(IValueFactory vf) { }

    public IList runTests(IString moduleName, IEvaluatorContext ctx) {
        return runTests(moduleName.getValue(), (Evaluator) ctx.getEvaluator());
    }
    
    // TODO: this has to be rewritten after we finish the compiler, using some Java reflection.
    public static IList runTests(String module, Evaluator eval) {
        IValueFactory vf = eval.getValueFactory();
        ModuleEnvironment root = new ModuleEnvironment("***testroot***", eval.getHeap());
        Environment old = eval.getCurrentEnvt();
        IListWriter results = vf.listWriter();

        try {
            eval.setCurrentEnvt(root);
            
            // make sure to have an absolutely up-to-date version of the module
            ModuleEnvironment testModuleEnv = eval.getHeap().getModule(module);
            if (testModuleEnv != null) {
                eval.getHeap().removeModule(testModuleEnv);
            }
            eval.doImport(eval.getMonitor(), module);
            eval.setTestResultListener(new ITestResultListener() {

                @Override
                public void start(String context, int count) { }

                @Override
                public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception) {
                    IConstructor tr = vf.constructor(testResult, vf.string(test), vf.bool(successful), loc);

                    if (!successful) {
                        tr = tr.asWithKeywordParameters().setParameter("message", vf.string(message));

                        if (exception != null) {
                            if (exception instanceof Throw) {
                                tr = tr.asWithKeywordParameters().setParameter("exceptions", vf.list(((Throw) exception).getException()));
                            }
                            else {
                                Type ext = TF.constructor(store, Exception, exception.getClass().getSimpleName());
                                IConstructor ex = vf.constructor(ext);
                                if (exception.getMessage() != null && !exception.getMessage().isEmpty()) {
                                    ex = ex.asWithKeywordParameters().setParameter("message", vf.string(exception.getMessage()));
                                }
                                tr = tr.asWithKeywordParameters().setParameter("exceptions", vf.list(ex));
                            }
                        }
                    }
                    // TODO: create messages from exceptions and also capture the IO somehow.
                    results.append(tr);
                }

                @Override
                public void ignored(String test, ISourceLocation loc) { }

                @Override
                public void done() { }
            });

            eval.runTests(eval.getMonitor());

            return results.done();
        }
        catch (ParseError e) {
            throw e;
        }
        catch (StaticError e) {
            throw e;
        }
        catch (Throwable e) {
            throw RuntimeExceptionFactory.illegalArgument(vf.string(module), e.getMessage());
        }
        finally {
            eval.setCurrentEnvt(old);
            eval.setTestResultListener(null);
        }
    }
}
