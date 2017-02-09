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
package org.rascalmpl.library.experiments.tutor3;

import java.io.IOException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.util.PathConfig;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class QuestionCompiler {
    IValueFactory vf;
    private OverloadedFunction compileQuestions;
    private  IList srcs;
    private  IList courses;
    private  IList libs;
    private  ISourceLocation bin;
    private  ISourceLocation boot;
    
    private RVMCore rvm;
    
    public QuestionCompiler(IValueFactory vf, PathConfig pcfg) throws IOException{
//        this.vf = vf;
//        srcs = pcfg.getSrcs();
//        libs = pcfg.getLibs();
//        bin = pcfg.getBin();
//        boot = pcfg.getBoot();
//        courses = pcfg.getcourses();
//        if(rvm == null){
//            RascalExecutionContext rex = 
//                    RascalExecutionContextBuilder.normalContext(vf, pcfg.getBoot() /* TODO needs a kernel location */, System.out, System.err)
//                        .setJVM(true)                   // options for complete repl
//                        .setTrace(false)
//                        .build();
//            rvm = ExecutionTools.initializedRVM(rex.getQuestionCompiler(), rex);
//        }
//        try {
//            compileQuestions = rvm.getOverloadedFunction("str compileQuestions(str qmodule, list[loc] srcs, list[loc] libs, list[loc] courses, loc bin, loc boot)");
//        } catch (NoSuchRascalFunction e) {
//            System.err.println("Function compileQuestions not found");
//            e.printStackTrace();
//        }
    }
    
    /**
     * Compile a .questions file to .adoc
     * @param questionsLoc Location of the questions source file
     * @param kwArgs    Keyword arguments
     * @return Void. As a side-effect a .adoc file will be generated.
     */
    public String compileQuestions(String qmodule, Map<String,IValue> kwArgs){
        try {
            IString res = (IString) rvm.executeRVMFunction(compileQuestions, new IValue[] { vf.string(qmodule), srcs, libs, courses, bin, boot}, kwArgs);
            return res.getValue();
        } catch (Exception e){
            e.printStackTrace(System.err);
        }
        return "";
    }
}
