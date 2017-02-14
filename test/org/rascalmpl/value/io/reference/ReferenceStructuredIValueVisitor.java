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
package org.rascalmpl.value.io.reference;

import org.rascalmpl.value.*;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.io.binary.util.StructuredIValueVisitor;
import org.rascalmpl.value.visitors.IValueVisitor;

import java.util.*;
import java.util.Map.Entry;

public class ReferenceStructuredIValueVisitor {
    public static <E extends Throwable> void accept(IValue root, StructuredIValueVisitor<E> visit) throws E {
        root.accept(new IValueVisitor<Void, E>() {
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
            public Void visitList(IList o) throws E {
                if (visit.enterList(o, o.length())) {
                    for (IValue v: o) {
                        v.accept(this);
                    }
                    visit.leaveList(o);
                }
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

            @Override
            public Void visitSet(ISet o) throws E {
                if (visit.enterSet(o, o.size())) {
                    List<IValue> reversedSet = new ArrayList<>();
                    for (IValue v:  o) {
                        reversedSet.add(v);
                    }
                    ListIterator<IValue> li = reversedSet.listIterator(reversedSet.size());
                    while (li.hasPrevious()) {
                        li.previous().accept(this);
                    }
                    visit.leaveSet(o);
                }
                return null;
            }

            @Override
            public Void visitMap(IMap o) throws E {
                if (visit.enterMap(o, o.size())) {
                    List<IValue> reversedMap = new ArrayList<>();
                    for (IValue v:  o) {
                        reversedMap.add(v);
                    }
                    ListIterator<IValue> li = reversedMap.listIterator(reversedMap.size());
                    while (li.hasPrevious()) {
                        IValue k = li.previous();
                        k.accept(this);
                        o.get(k).accept(this);
                    }
                    visit.leaveMap(o);
                }
                return null;
            }

            @Override
            public Void visitSourceLocation(ISourceLocation o) throws E {
                visit.visitSourceLocation(o);
                return null;
            }

            @Override
            public Void visitTuple(ITuple o) throws E {
                if (visit.enterTuple(o, o.arity())) {
                    for (IValue v: o) {
                        v.accept(this);
                    }
                    visit.leaveTuple(o);
                }
                return null;
            }

            @Override
            public Void visitNode(INode o) throws E {
                if (visit.enterNode(o, o.arity())) {
                    for (IValue v: o) {
                        v.accept(this);
                    }
                    if(o.mayHaveKeywordParameters()){
                        IWithKeywordParameters<? extends INode> okw = o.asWithKeywordParameters();
                        if(okw.hasParameters()){
                            assert okw instanceof AbstractDefaultWithKeywordParameters;
                            AbstractDefaultWithKeywordParameters<INode> nodeKw = (AbstractDefaultWithKeywordParameters<INode>)(okw);
                            io.usethesource.capsule.api.Map.Immutable<String, IValue> params = nodeKw.internalGetParameters();
                            visit.enterNodeKeywordParameters();
                            visitNamedValues(params);
                        }
                    } else {
                        IAnnotatable<? extends INode> oan = o.asAnnotatable();
                        if(oan.hasAnnotations()){
                            assert oan instanceof AbstractDefaultAnnotatable;
                            AbstractDefaultAnnotatable<INode> nodeAnno = (AbstractDefaultAnnotatable<INode>)(oan);
                            io.usethesource.capsule.api.Map.Immutable<String, IValue> annos = nodeAnno.internalGetAnnotations();
                            visit.enterNodeAnnotations();
                            visitNamedValues(annos);
                        }
                    }
                    visit.leaveNode(o);
                }
                return null;
            }

            private void visitNamedValues(io.usethesource.capsule.api.Map.Immutable<String, IValue> namedValues) throws E {
                // since the PrePostValueIterator uses a stack, we see the annotations an keyword params in reverse (but in pairs)
                List<Entry<String, IValue>> reverseEntries = new ArrayList<>();
                Iterator<Entry<String, IValue>> iterator = namedValues.entryIterator();
                while (iterator.hasNext()) {
                    Entry<String, IValue> param = iterator.next();
                    reverseEntries.add(0, new AbstractMap.SimpleImmutableEntry<String, IValue>(param.getKey(), param.getValue()));
                }
                
                visit.enterNamedValues(reverseEntries.stream().map(e -> e.getKey()).toArray(i -> new String[i]), namedValues.size());
                for (Entry<String, IValue> ent: reverseEntries) {
                    ent.getValue().accept(this);
                }
                visit.leaveNamedValue();
            }

            @Override
            public Void visitConstructor(IConstructor o) throws E {
                // clone of visitNode! only different method calls
                if (visit.enterConstructor(o, o.arity())) {
                    for (IValue v: o) {
                        v.accept(this);
                    }
                    if(o.mayHaveKeywordParameters()){
                        IWithKeywordParameters<? extends IConstructor> okw = o.asWithKeywordParameters();
                        if(okw.hasParameters()){
                            assert okw instanceof AbstractDefaultWithKeywordParameters;
                            AbstractDefaultWithKeywordParameters<IConstructor> nodeKw = (AbstractDefaultWithKeywordParameters<IConstructor>)(okw);
                            io.usethesource.capsule.api.Map.Immutable<String, IValue> params = nodeKw.internalGetParameters();
                            visit.enterConstructorKeywordParameters();
                            visitNamedValues(params);
                        }
                    } else {
                        IAnnotatable<? extends IConstructor> oan = o.asAnnotatable();
                        if(oan.hasAnnotations()){
                            assert oan instanceof AbstractDefaultAnnotatable;
                            AbstractDefaultAnnotatable<IConstructor> nodeAnno = (AbstractDefaultAnnotatable<IConstructor>)(oan);
                            io.usethesource.capsule.api.Map.Immutable<String, IValue> annos = nodeAnno.internalGetAnnotations();
                            visit.enterConstructorAnnotations();
                            visitNamedValues(annos);
                        }
                    }
                    visit.leaveConstructor(o);
                }
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
            public Void visitExternal(IExternalValue externalValue) throws E {
                return null;
            }

            @Override
            public Void visitDateTime(IDateTime o) throws E {
                visit.visitDateTime(o);
                return null;
            }
            
        });
        
    }

}
