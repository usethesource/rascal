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
package org.rascalmpl.value.io.binary.message;

import java.util.Iterator;

import java.util.Map.Entry;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.visitors.NullVisitor;

/* package */ final class IValueSizeEstimator extends NullVisitor<Void, RuntimeException> {

    final class StopCountingException extends RuntimeException {
        private static final long serialVersionUID = 0;

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
    public static int estimateIValueSize(IValue root, int stopCountingAfter) {
        IValueSizeEstimator counter = new IValueSizeEstimator(stopCountingAfter);
        try {
            root.accept(counter);
            return counter.values;
        } 
        catch (StopCountingException e) {
            return stopCountingAfter;
        }
    }
    private final int stopAfter;
    public int values;

    private IValueSizeEstimator(int stopAfter) {
        this.stopAfter = stopAfter;
        values = 0;
    }

    private void checkEarlyExit() {
        if (values > stopAfter) {
            throw new StopCountingException();
        }
    }

    private Void accept(Iterable<IValue> o) {
        for (IValue v: o) {
            v.accept(this);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private Void acceptKWAnno(IValue o) {
        if (o.mayHaveKeywordParameters()) {
            for (IValue v: o.asWithKeywordParameters().getParameters().values()) {
                v.accept(this);
            }
        }
        else if (o.isAnnotatable()) {
            for (IValue v: o.asAnnotatable().getAnnotations().values()) {
                v.accept(this);
            }
        }
        return null;
    }
    @Override
    public Void visitNode(INode o) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        accept(o.getChildren());
        return acceptKWAnno(o);
    }
    
    @Override
    public Void visitConstructor(IConstructor o) throws StopCountingException {
        return visitNode(o);
    }

    
    @Override
    public Void visitList(IList o) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        if (o.length() > 10) {
            values += o.length();
        }
        return accept(o);
    }


    @Override
    public Void visitSet(ISet o) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        if (o.size() > 10) {
            values += o.size();
        }
        return accept(o);
    }
    
    @Override
    public Void visitListRelation(IList o) throws StopCountingException {
        return visitList(o);
    }
    @Override
    public Void visitRelation(ISet o) throws StopCountingException {
        return visitSet(o);
    }
    @Override
    public Void visitTuple(ITuple o) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        return accept(o);
    }
    @Override
    public Void visitMap(IMap o) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        if (o.size() > 10) {
            values += o.size();
        }
        Iterator<Entry<IValue, IValue>> entries = o.entryIterator();
        while (entries.hasNext()) {
            Entry<IValue, IValue> e = entries.next();
            e.getKey().accept(this);
            e.getValue().accept(this);
        }
        return null;
    }

    @Override
    public Void visitExternal(IExternalValue externalValue) throws StopCountingException {
        checkEarlyExit();
        values += 1;
        return null;
    }
    
}