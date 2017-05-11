/** 
 * Copyright (c) 2017, paulklint, Centrum Wiskunde & Informatica (CWI) 
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

import java.util.Set;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;

public class TypeStoreWrapperCompiled implements LimitedTypeStore {
    final RVMCore rvm;
    
    TypeStoreWrapperCompiled(RascalExecutionContext rex){
        this.rvm = rex.getRVM();
    }
    @Override
    public Type lookupAbstractDataType(String name) {
        
        try {
            return rvm.getAbstractDataType(name);
        }
        catch (NoSuchRascalFunction e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; //<=== do something reasonable here
    }

    @Override
    public Type lookupConstructor(Type adt, String constructorName, Type args) {
        try {
            return rvm.getConstructor(constructorName, adt, args);
        }
        catch (NoSuchRascalFunction e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; //<=== do something reasonable here
    }

    @Override
    public Set<Type> lookupConstructor(Type adt, String constructorName) throws FactTypeUseException {
        return rvm.getConstructor(constructorName, adt);
    }

    @Override
    public boolean hasKeywordField(Type constructor, String name) {
        // TODO Auto-generated method stub
        return true;
    }

}
