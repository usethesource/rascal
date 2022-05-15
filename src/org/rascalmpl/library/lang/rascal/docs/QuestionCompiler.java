/**
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.library.lang.rascal.docs;

import io.usethesource.vallang.*;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.RunTests;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import java.io.File;

public class QuestionCompiler {
    private final IValueFactory vf = IRascalValueFactory.getInstance();
    private final Evaluator eval;

    public QuestionCompiler(PathConfig pcfg) {
        final GlobalEnvironment heap = new GlobalEnvironment();
        final ModuleEnvironment top = new ModuleEnvironment("***question compiler***", heap);
        eval = new Evaluator(vf, System.in, System.err, System.out, top, heap);
        eval.addRascalSearchPath(URIUtil.rootLocation("std"));
        eval.addRascalSearchPath(URIUtil.rootLocation("test-modules"));
        eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));

    }

    /**
     * Compile a .questions file to .adoc
     */
    public IString compileQuestions(ISourceLocation qmodule, PathConfig pcfg) {
        if (!eval.getHeap().existsModule("lang::rascal::tutor::QuestionCompiler")) {
            eval.doImport(null, "lang::rascal::tutor::QuestionCompiler");
        }

        return (IString) eval.call("compileQuestions", qmodule, pcfg.asConstructor());
    }

    public IList checkQuestions(String questionModule) {
        return RunTests.runTests(questionModule, eval);
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
