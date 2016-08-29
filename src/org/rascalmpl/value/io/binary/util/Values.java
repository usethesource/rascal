/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary.util;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.visitors.IValueVisitor;

public class Values {
    private final static IValueVisitor<Boolean, RuntimeException> valueCompoundTest= new IValueVisitor<Boolean, RuntimeException>() {
        @Override
        public Boolean visitString(IString o) throws RuntimeException {
            return false;
        }
        @Override
        public Boolean visitReal(IReal o) throws RuntimeException {
            return false;
        }
        @Override
        public Boolean visitRational(IRational o) throws RuntimeException {
            // has two number parts
            return true;
        }
        @Override
        public Boolean visitList(IList o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitRelation(ISet o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitListRelation(IList o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitSet(ISet o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitSourceLocation(ISourceLocation o) throws RuntimeException {
            return false;
        }
        @Override
        public Boolean visitTuple(ITuple o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitNode(INode o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitConstructor(IConstructor o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitInteger(IInteger o) throws RuntimeException {
            return false;
        }
        @Override
        public Boolean visitMap(IMap o) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitBoolean(IBool boolValue) throws RuntimeException {
            return false;
        }
        @Override
        public Boolean visitExternal(IExternalValue externalValue) throws RuntimeException {
            return true;
        }
        @Override
        public Boolean visitDateTime(IDateTime o) throws RuntimeException {
            return false;
        }
    };

    public static boolean isCompound(IValue val) {
        return val.accept(valueCompoundTest);
    }

}
