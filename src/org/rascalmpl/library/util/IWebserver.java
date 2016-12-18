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
package org.rascalmpl.library.util;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalKeywordParameters;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalModule;
import org.rascalmpl.library.lang.rascal.boot.IJava2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel.KWcompile;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;

@RascalModule("util::Webserver")
public interface IWebserver extends IJava2Rascal {

    //  Status
    
    IConstructor ok();
    IConstructor created();
    IConstructor accepted(); 
    IConstructor noContent(); 
    IConstructor partialContent(); 
    IConstructor redirect(); 
    IConstructor notModified(); 
    IConstructor badRequest(); 
    IConstructor unauthorized(); 
    IConstructor forbidden(); 
    IConstructor notFound(); 
    IConstructor rangeNotSatisfiable(); 
    IConstructor internalError();
    
    // Request
    
    IConstructor get(IString path, KWRequest kwArgs);
    IConstructor put(IString path, IValue Body, KWRequest kwArgs);
    IConstructor post(IString path, IValue Body, KWRequest kwArgs);
    IConstructor delete(IString path);
    IConstructor head(IString path);
    
    @RascalKeywordParameters
    interface KWRequest {
        KWRequest headers(IMap headers);
        KWRequest parameters(IMap headers);
        KWRequest uploads(IMap headers);
    }
    KWRequest kw_Request();
    
}
