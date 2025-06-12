/** 
 * Copyright (c) 2022, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWO-I - CWI) 
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
package org.rascalmpl.tutor.lang.rascal.tutor.repl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * This class marshalls between a virtual Rascal REPL and Rascal client code.
 */
public class TutorCommandExecutorCreator {
    private final IRascalValueFactory vf;
    private final Type resetType;
    private final Type evalType;
    private final Type promptType;
    private final Type execConstructor;

    public TutorCommandExecutorCreator(IRascalValueFactory vf, TypeFactory tf, TypeStore ts) {
        this.vf = vf;
        promptType = tf.functionType(tf.stringType(), tf.tupleEmpty(), tf.tupleEmpty());
        resetType = tf.functionType(tf.voidType(), tf.tupleEmpty(), tf.tupleEmpty());
        evalType = tf.functionType(tf.mapType(tf.stringType(), tf.stringType()), tf.tupleType(tf.stringType()), tf.tupleEmpty());
        execConstructor = ts.lookupConstructor(ts.lookupAbstractDataType("CommandExecutor"), "executor").iterator().next();
    }
    
    public IConstructor createExecutor(IConstructor pathConfigCons) {
        try {
            PathConfig pcfg = new PathConfig(pathConfigCons);
            TutorCommandExecutor repl = new TutorCommandExecutor(pcfg);
            return vf.constructor(execConstructor,
                pathConfigCons,
                prompt(repl),
                reset(repl),
                eval(repl)
            );
        }
        catch (IOException | URISyntaxException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
        }
    }

    IFunction reset(TutorCommandExecutor exec) {
        return vf.function(resetType, (args, kwargs) -> {
            exec.reset();
            return null;
        });
    }

    IFunction prompt(TutorCommandExecutor exec) {
        return vf.function(promptType, (args, kwargs) -> {
            return vf.string(exec.prompt());
        });
    }

    IFunction eval(TutorCommandExecutor exec) {
        return vf.function(evalType, (args, kwargs) -> {    
            try {
                IString command = (IString) args[0];
                Map<String, String> output = exec.eval(command.getValue());
                IMapWriter mw = vf.mapWriter();

                for (String mimeType : output.keySet()) {
                    mw.put(vf.string(mimeType), vf.string(output.get(mimeType)));                
                }
                
                return mw.done();
            }
            catch (InterruptedException | IOException e) {
                throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
            }
        });
    }
}
