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

import org.rascalmpl.value.*;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.visitors.IValueVisitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map.Entry;

public class StacklessStructuredVisitor {

    public static <E extends Throwable> void accept(IValue root, StructuredIValueVisitor<E> visit) throws E {
        Deque<NextStep<E>> workList = new ArrayDeque<>();
        workList.push(new NextStep<>(root, StacklessStructuredVisitor::visitValue));
        while (!workList.isEmpty()) {
            NextStep<E> current =  workList.pop();
            current.next.accept(current.val, workList, visit);
        }
    }

    @FunctionalInterface
    private static interface NextStepConsumer<E extends Throwable> {
        void accept(IValue current, Deque<NextStep<E>> worklist, StructuredIValueVisitor<E> visit) throws E;
    }
    
    private final static class NextStep<E extends Throwable> {
        final IValue val;
        final NextStepConsumer<E> next;
        public NextStep(IValue val, NextStepConsumer<E> next) {
            this.val = val;
            this.next = next;
        }
    }
    
    private static <E extends Throwable> void visitValue(IValue current, Deque<NextStep<E>> workList, StructuredIValueVisitor<E> visit) throws E {
        current.accept(new IValueVisitor<Void, E>() {

            @Override
            public Void visitList(IList lst) throws E {
                if (visit.enterList(lst, lst.length())) {
                    workList.push(new NextStep<>(lst, (l, w, v) -> {
                        v.leaveList(l);
                    }));
                    for (int i = lst.length() - 1; i >= 0; i--) {
                        workList.push(new NextStep<>(lst.get(i), StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }


            @Override
            public Void visitSet(ISet set) throws E {
                if (visit.enterSet(set, set.size())) {
                    workList.push(new NextStep<>(set, (l, w, v) -> {
                        v.leaveSet(l);
                    }));
                    for (IValue v: set) {
                        workList.push(new NextStep<>(v, StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }

            @Override
            public Void visitMap(IMap map) throws E {
                if (visit.enterMap(map, map.size())) {
                    workList.push(new NextStep<>(map, (l, w, v) -> {
                        v.leaveMap(l);
                    }));
                    for (IValue k: map) {
                        workList.push(new NextStep<>(map.get(k), StacklessStructuredVisitor::visitValue));
                        workList.push(new NextStep<>(k, StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }

            @Override
            public Void visitTuple(ITuple tuple) throws E {
                if (visit.enterTuple(tuple, tuple.arity())) {
                    workList.push(new NextStep<>(tuple, (l, w, v) -> {
                        v.leaveTuple(l);
                    }));
                    for (int i = tuple.arity() - 1; i >= 0; i--) {
                        workList.push(new NextStep<>(tuple.get(i), StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }

            @Override
            public Void visitNode(INode node) throws E {
                // WARNING, cloned to visitConstructor, fix bugs there as well!
                if (visit.enterNode(node, node.arity())) {
                    workList.push(new NextStep<>(node, (l, w, v) -> {
                        v.leaveNode(l);
                    }));
                    if(node.mayHaveKeywordParameters()){
                        IWithKeywordParameters<? extends INode> withKW = node.asWithKeywordParameters();
                        if(withKW.hasParameters()){
                            assert withKW instanceof AbstractDefaultWithKeywordParameters;
                            @SuppressWarnings("unchecked")
                            AbstractDefaultWithKeywordParameters<INode> nodeKw = (AbstractDefaultWithKeywordParameters<INode>)(withKW);
                            pushKWPairs(nodeKw.internalGetParameters());
                            workList.push(new NextStep<>(node, (l, w, v) -> {
                                v.enterNodeKeywordParameters();
                            }));
                        }

                    } else {
                        IAnnotatable<? extends INode> withAnno = node.asAnnotatable();
                        if(withAnno.hasAnnotations()){
                            assert withAnno instanceof AbstractDefaultAnnotatable;
                            @SuppressWarnings("unchecked")
                            AbstractDefaultAnnotatable<INode> nodeAnno = (AbstractDefaultAnnotatable<INode>)withAnno;
                            pushKWPairs(nodeAnno.internalGetAnnotations());
                            workList.push(new NextStep<>(node, (l, w, v) -> {
                                v.enterNodeAnnotations();
                            }));
                        }
                    }
                    for(int i = node.arity() - 1; i >= 0; i--){
                        workList.push(new NextStep<>(node.get(i), StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }


            private void pushKWPairs(io.usethesource.capsule.api.Map.Immutable<String, IValue> namedValues) {
                workList.push(new NextStep<>(null, (l,w,v) -> {
                    v.leaveNamedValue();
                }));

                String[] names = new String[namedValues.size()];
                int i = names.length;
                Iterator<Entry<String, IValue>> entryIterator = namedValues.entryIterator();
                while (entryIterator.hasNext()) {
                    Entry<String, IValue> param = entryIterator.next();
                    workList.push(new NextStep<>(param.getValue(), StacklessStructuredVisitor::visitValue));
                    i--;
                    names[i] = param.getKey();
                }
                assert i == 0;
                workList.push(new NextStep<>(null, (l,w,v) -> {
                    v.enterNamedValues(names, names.length);
                }));
            }

            @Override
            public Void visitConstructor(IConstructor constr) throws E {
                // WARNING, cloned from visitNode, fix bugs there as well!
                if (visit.enterConstructor(constr, constr.arity())) {
                    workList.push(new NextStep<>(constr, (l, w, v) -> {
                        v.leaveConstructor(l);
                    }));
                    if(constr.mayHaveKeywordParameters()){
                        IWithKeywordParameters<? extends IConstructor> withKW = constr.asWithKeywordParameters();
                        if(withKW.hasParameters()){
                            assert withKW instanceof AbstractDefaultWithKeywordParameters;
                            @SuppressWarnings("unchecked")
                            AbstractDefaultWithKeywordParameters<IConstructor> constrKw = (AbstractDefaultWithKeywordParameters<IConstructor>)(withKW);
                            pushKWPairs(constrKw.internalGetParameters());
                            workList.push(new NextStep<>(constr, (l, w, v) -> {
                                v.enterConstructorKeywordParameters();
                            }));
                        }

                    } else {
                        IAnnotatable<? extends IConstructor> withAnno = constr.asAnnotatable();
                        if(withAnno.hasAnnotations()){
                            assert withAnno instanceof AbstractDefaultAnnotatable;
                            @SuppressWarnings("unchecked")
                            AbstractDefaultAnnotatable<IConstructor> constrAnno = (AbstractDefaultAnnotatable<IConstructor>)withAnno;
                            pushKWPairs(constrAnno.internalGetAnnotations());
                            workList.push(new NextStep<>(constr, (l, w, v) -> {
                                v.enterConstructorAnnotations();
                            }));
                        }
                    }
                    for(int i = constr.arity() - 1; i >= 0; i--){
                        workList.push(new NextStep<>(constr.get(i), StacklessStructuredVisitor::visitValue));
                    }
                }
                return null;
            }



            @Override
            public Void visitExternal(IExternalValue externalValue) throws E {
                throw new RuntimeException("External values not supported yet");
            }

            @Override
            public Void visitSourceLocation(ISourceLocation o) throws E {
                visit.visitSourceLocation(o);
                return null;
            }
            @Override
            public Void visitInteger(IInteger o) throws E {
                visit.visitInteger(o);
                return null;
            }
            @Override
            public Void visitBoolean(IBool boolValue) throws E {
                visit.visitBoolean(boolValue);
                return null;
            }
            @Override
            public Void visitDateTime(IDateTime o) throws E {
                visit.visitDateTime(o);
                return null;
            }

            @Override
            public Void visitString(IString o) throws E {
                visit.visitString(o);
                return null;
            }

            @Override
            public Void visitReal(IReal o) throws E {
                visit.visitReal(o);
                return null;
            }

            @Override
            public Void visitRational(IRational o) throws E {
                visit.visitRational(o);
                return null;
            }

            @Override
            public Void visitRelation(ISet o) throws E {
                return visitSet(o);
            }

            @Override
            public Void visitListRelation(IList o) throws E {
                return visitList(o);
            }
        });
        
    }
    
    
}
