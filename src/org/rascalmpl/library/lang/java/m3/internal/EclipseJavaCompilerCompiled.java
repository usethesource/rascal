/** 
 * Copyright (c) 2017, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.lang.java.m3.internal;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class EclipseJavaCompilerCompiled extends EclipseJavaCompiler {

    public EclipseJavaCompilerCompiled(IValueFactory vf) {
        super(vf);
    }
    
    private void checkInterrupted(RascalExecutionContext rex) {
        throw new RuntimeException("Not implemented");
    }

    private LimitedTypeStore getM3Store(RascalExecutionContext rex) {
        throw new RuntimeException("Not implemented");
    }

    public IValue createM3FromJarClass(ISourceLocation jarLoc, RascalExecutionContext rex) {
        return createM3FromJarClass(jarLoc, getM3Store(rex));
    }

    public IValue createM3sFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, RascalExecutionContext rex) {
        return createM3sFromFiles(files, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(rex), () -> checkInterrupted(rex));
    }
    
    public IValue createM3sAndAstsFromFiles(ISet files, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, RascalExecutionContext rex) {
        return createM3sAndAstsFromFiles(files, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(rex), () -> checkInterrupted(rex));
    }

    public IValue createM3FromString(ISourceLocation loc, IString contents, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, RascalExecutionContext rex) {
        return createM3FromString(loc, contents, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(rex)); 
    }
    
    public IValue createAstsFromFiles(ISet files, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, RascalExecutionContext rex) {
        return createAstsFromFiles(files, collectBindings, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(rex), () -> checkInterrupted(rex));
    }

    public IValue createAstFromString(ISourceLocation loc, IString contents, IBool collectBindings, IBool errorRecovery, IList sourcePath, IList classPath, IString javaVersion, RascalExecutionContext rex) {
        return createAstFromString(loc, contents, collectBindings, errorRecovery, sourcePath, classPath, javaVersion, getM3Store(rex));
    }
}
