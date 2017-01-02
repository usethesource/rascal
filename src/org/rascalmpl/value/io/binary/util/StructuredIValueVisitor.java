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

public interface StructuredIValueVisitor<E extends Throwable> {
    
    void enterNamedValues(String[] names, int numberOfNestedValues) throws E;
    void leaveNamedValue() throws E;
    
    boolean enterConstructor(IConstructor cons, int children) throws E;
    void enterConstructorKeywordParameters() throws E;
    void enterConstructorAnnotations() throws E;
    void leaveConstructor(IValue cons) throws E;

    boolean enterNode(INode node, int children) throws E;
    void enterNodeKeywordParameters() throws E;
    void enterNodeAnnotations() throws E;
    void leaveNode(IValue node) throws E;

    boolean enterList(IList lst, int children) throws E;
    void leaveList(IValue lst) throws E;

    boolean enterSet(ISet set, int elements) throws E;
    void leaveSet(IValue set) throws E;
    
    boolean enterMap(IMap map, int elements) throws E;
    void leaveMap(IValue map) throws E;
    
    boolean enterTuple(ITuple tuple, int arity) throws E;
    void leaveTuple(IValue tuple) throws E;

    void visitString(IString val) throws E;
    void visitInteger(IInteger val) throws E;
    void visitReal(IReal val) throws E;
    void visitRational(IRational val) throws E;
    void visitSourceLocation(ISourceLocation val) throws E;
    void visitBoolean(IBool val) throws E;
    void visitDateTime(IDateTime val) throws E;
}
