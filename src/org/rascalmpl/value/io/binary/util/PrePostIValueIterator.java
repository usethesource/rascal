/** 
 * Copyright (c) 2016, Davy Landman, Paul Klint, Centrum Wiskunde & Informatica (CWI) 
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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.visitors.NullVisitor;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * A stackless iterator of IValues, it combines a pre visit with a post visit. The post visit is limited to the compound IValues.
 * 
 * To make a difference between the pre and post visit, we call the pre visit the beginning of an value, and the post visit the end. 
 * @author Davy Landman
 *
 */
public class PrePostIValueIterator  {
    
    private final PositionStack stack;
    private IValue value;
    private boolean beginning;
    private boolean hasEnd;
    private IValueFactory vf = ValueFactoryFactory.getValueFactory();

    public PrePostIValueIterator(IValue root) {
        this(root, 1024);
    }

    public PrePostIValueIterator(IValue root, int stackSize) {
        stack = new PositionStack(stackSize);
        stack.push(root, true);
        beginning = false; // start out at fake end
        hasEnd = false;
    }


    public boolean hasNext() {
        return !stack.isEmpty() || (beginning && hasEnd);
    }

    
    /**
     * Do not go into the children of the current value, and skip the post visit.
     * 
     * It can only be called on the pre visit phase of the visitor (<code>atBeginning() == true</code>).
     * @return
     */
    public IValue skipValue() {
        assert beginning;
        beginning = false;
        return value;
    }

    /**
     * Are we at the beginning of an IValue (the pre visit step)
     * @return true if we are in the pre visit, false if we are in the post visit
     */
    public boolean atBeginning() {
        return beginning;
    }


    public IValue getValue() {
        return value;
    }


    /**
     * Advanced the iterator to the next value, should be preceded with a call to {@link hasNext()}
     * @return the next IValue (also available via {@link getValue()})
     */
    public IValue next() {
        if (beginning) {
            if (hasEnd) {
                stack.push(value, false);
                value.accept(new NullVisitor<Void, RuntimeException>() {
                    @Override
                    public Void visitConstructor(IConstructor cons) throws RuntimeException {
                        return visitNode(cons);
                    }
                    @Override
                    public Void visitList(IList lst) throws RuntimeException {
                        for (int i = lst.length() - 1; i >= 0; i--) {
                            IValue elem = lst.get(i);
                            stack.push(elem, true);
                        }
                        return null;
                    }
                    @Override
                    public Void visitMap(IMap map) throws RuntimeException {
                        for(IValue key : map){
                            IValue val = map.get(key);
                            stack.push(val, true);
                            stack.push(key, true);
                        }
                        return null;
                    }
                    @SuppressWarnings("unchecked")
                    @Override
                    public Void visitNode(INode node) throws RuntimeException {
                        if(node.mayHaveKeywordParameters()){
                            if(node.asWithKeywordParameters().hasParameters()){
                                assert node.asWithKeywordParameters() instanceof AbstractDefaultWithKeywordParameters;
                                AbstractDefaultWithKeywordParameters<IValue> nodeKw = (AbstractDefaultWithKeywordParameters<IValue>)(node.asWithKeywordParameters());
                                pushKWPairs(nodeKw.internalGetParameters().entryIterator());
                            }
                        } else {
                            if(node.asAnnotatable().hasAnnotations()){
                                assert node.asAnnotatable() instanceof AbstractDefaultAnnotatable;
                                AbstractDefaultAnnotatable<IValue> nodeAnno = (AbstractDefaultAnnotatable<IValue>)(node.asAnnotatable());
                                pushKWPairs(nodeAnno.internalGetAnnotations().entryIterator());
                            }
                        }

                        for(int i = node.arity() - 1; i >= 0; i--){
                            IValue elem = node.get(i);
                            stack.push(elem, true);
                        }
                        return null;
                    }
                    private void pushKWPairs(Iterator<Entry<String, IValue>> entryIterator) {
                        while (entryIterator.hasNext()) {
                            Entry<String, IValue> param = entryIterator.next();
                            stack.push(param.getValue(), true);
                            stack.push(vf.string(param.getKey()), true);
                        }
                    }
                    @Override
                    public Void visitRational(IRational rat) throws RuntimeException {
                        stack.push(rat.denominator(), true);
                        stack.push(rat.numerator(), true);
                        return null;
                    }
                    @Override
                    public Void visitSet(ISet set) throws RuntimeException {
                        for(IValue elem : set){
                            stack.push(elem, true);
                        }
                        return null;
                    }
                    @Override
                    public Void visitTuple(ITuple tuple) throws RuntimeException {
                        for(int i = tuple.arity() - 1; i >= 0; i--){
                            IValue elem = tuple.get(i);
                            stack.push(elem, true);
                        }
                        return null;
                    }
                    @Override
                    public Void visitListRelation(IList o) throws RuntimeException {
                        return visitList(o);
                    }
                    @Override
                    public Void visitRelation(ISet o) throws RuntimeException {
                        return visitSet(o);
                    }
                });
            }
        }
        value = stack.currentIValue();
        beginning = stack.currentBeginning();
        hasEnd = beginning && hasEnd(value);
        stack.pop();
        return value;
    }


    private static boolean hasEnd(IValue val) {
        return val instanceof IConstructor
           || val instanceof IList
           || val instanceof ISet
           || val instanceof IMap
           || val instanceof INode
           || val instanceof IRational
           || val instanceof ITuple
           ;
    }

}