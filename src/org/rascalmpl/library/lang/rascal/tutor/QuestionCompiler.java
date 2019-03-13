/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.lang.rascal.tutor;

import java.io.File;
import java.io.PrintWriter;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class QuestionCompiler {
    private final IValueFactory vf = IRascalValueFactory.getInstance();
    private final Evaluator eval;

    public QuestionCompiler(PathConfig pcfg) {
        final GlobalEnvironment heap = new GlobalEnvironment();
        final ModuleEnvironment top = new ModuleEnvironment("***question compiler***", heap);
        eval = new Evaluator(vf, new PrintWriter(System.err), new PrintWriter(System.out), top, heap);
        eval.addRascalSearchPath(URIUtil.rootLocation("std"));
        eval.addRascalSearchPath(URIUtil.rootLocation("test-modules"));
        eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
        eval.doImport(null, "lang::rascal::tutor::QuestionCompiler");
    }
    
    /**
     * Compile a .questions file to .adoc
     */
    public IString compileQuestions(IString qmodule, PathConfig pcfg) {
        return (IString) eval.call("compileQuestions", qmodule, pcfg.asConstructor());
    }
    
    public boolean checkQuestions(String questionModule) {
        eval.doImport(eval.getMonitor(), questionModule);
        ModuleEnvironment mod = eval.getHeap().getModule(questionModule);
        Environment old = eval.getCurrentEnvt();
        
        try {
            eval.setCurrentEnvt(mod);
            return eval.runTests(eval.getMonitor());
        }
        finally {
            eval.setCurrentEnvt(old);
            eval.getHeap().removeModule(mod);
        }
    }
    
    private String javaCompilerPathAsString(IList javaCompilerPath) {
        StringBuilder b = new StringBuilder();

        for (IValue elem : javaCompilerPath) {
            ISourceLocation loc = (ISourceLocation) elem;

            if (b.length() != 0) {
                b.append(File.pathSeparatorChar);
            }

            assert loc.getScheme().equals("file");
            String path = loc.getPath();
            if (path.startsWith("/") && path.contains(":\\")) {
                // a windows path should drop the leading /
                path = path.substring(1);
            }
            b.append(path);
        }

        return b.toString();
    }
}
