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
package org.rascalmpl.value.util;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.DefaultTypeVisitor;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

public class RandomValues {
	private static TypeStore ts = new TypeStore();
	private static TypeFactory tf = TypeFactory.getInstance();
	private static Type Boolean = tf.abstractDataType(ts,"Boolean");

	private static Type Name = tf.abstractDataType(ts,"Name");
	@SuppressWarnings("deprecation")
    private static Type True = tf.constructor(ts,Boolean, "true");
	@SuppressWarnings("deprecation")
    private static Type False= tf.constructor(ts,Boolean, "false");
	private static Type And= tf.constructor(ts,Boolean, "and", Boolean, Boolean);
	private static Type Or= tf.constructor(ts,Boolean, "or", tf.listType(Boolean));
	private static Type Not= tf.constructor(ts,Boolean, "not", Boolean);
	private static Type TwoTups = tf.constructor(ts,Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = tf.constructor(ts,Name, "name", tf.stringType());
	private static Type Friends = tf.constructor(ts,Boolean, "friends", tf.listType(Name));
	private static Type Couples = tf.constructor(ts,Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	static {
	    ts.declareKeywordParameter(Name, "moreName", Name);
	    ts.declareKeywordParameter(Name, "listName", tf.listType(Name));
	    ts.declareKeywordParameter(Name, "anyValue", tf.valueType());
	    ts.declareAnnotation(Boolean, "boolAnno", tf.boolType());
	}

	private static IValue name(IValueFactory vf, String n){
		return vf.constructor(NameNode, vf.string(n));
	}
	
	public static IValue[] getTestValues(IValueFactory vf) {
	    return new IValue[] {
			vf.constructor(True),
			vf.constructor(And, vf.constructor(True), vf.constructor(False)),
			vf.constructor(Not, vf.constructor(And, vf.constructor(True), vf.constructor(False))),
			vf.constructor(TwoTups, vf.tuple(vf.constructor(True), vf.constructor(False)),vf.tuple(vf.constructor(True), vf.constructor(False))),
			vf.constructor(Or, vf.list(vf.constructor(True), vf.constructor(False), vf.constructor(True))),
			vf.constructor(Friends, vf.list(name(vf, "Hans").asWithKeywordParameters().setParameter("listName", vf.list(name(vf,"Hansie"))), name(vf, "Bob"))),
			vf.constructor(Or, vf.list(Boolean)).asAnnotatable().setAnnotation("boolAnno", vf.bool(true)),
			vf.constructor(Couples, vf.list(vf.tuple(name(vf, "A"), name(vf, "B")), vf.tuple(name(vf, "C"), name(vf, "D")))),
			vf.integer(0),
			vf.integer(1),
			vf.integer(-1),
			vf.string("🍝"),
			vf.integer(Integer.MAX_VALUE),
			vf.integer(Integer.MIN_VALUE),
			vf.integer(new byte[]{(byte)0xfe, (byte)0xdc, (byte)0xba, (byte)0x98, (byte)0x76, (byte)0x54}),
			vf.constructor(True).asAnnotatable().setAnnotation("test", vf.integer(1))
	    };
	};
	
	public static Type addNameType(TypeStore t) {
	    t.extendStore(ts);
	    return Name;
	}

    public static IValue generate(Type tp, TypeStore ts, IValueFactory vf, Random rand, int maxDepth) {
        Type[] randomTypes = new Type[] { 
                tf.boolType(), tf.stringType(), tf.listType(tf.valueType()), tf.listType(tf.numberType()),
                tf.sourceLocationType(), tf.integerType(), tf.realType()
        };
        return tp.accept(new ITypeVisitor<IValue, RuntimeException>() {
            private int currentDepth = 0;
            
            private Type pickRandom(Collection<Type> types) {
                int nth = rand.nextInt(types.size());
                int index = 0;
                for (Type t: types) {
                    if (index == nth) {
                       return t; 
                    }
                    index++;
                }
                throw new AssertionError("Dead code");
            }

            private Type randomType() {
                if (currentDepth < maxDepth && rand.nextDouble() > 0.8 && !ts.getAbstractDataTypes().isEmpty()) {
                    return pickRandom(ts.getAbstractDataTypes());
                }
                if (currentDepth < maxDepth && rand.nextDouble() > 0.8) {
                    return tf.nodeType();
                }
                return randomTypes[rand.nextInt(randomTypes.length)];
            }
            @Override
            public IValue visitAbstractData(Type type) throws RuntimeException {
                Set<Type> candidates = ts.lookupAlternatives(type);
                if (candidates.isEmpty()) {
                    throw new UnsupportedOperationException("The "+type+" ADT has no constructors in the type store");
                }
                Type constructor = pickRandom(candidates);
                if (currentDepth >= maxDepth) {
                    // find the constructor that does not add depth
                    Iterator<Type> it = candidates.iterator();
                    while (alwaysIncreasesDepth(constructor) && it.hasNext()) {
                       constructor = it.next(); 
                    }
                }
                return constructor.accept(this);
            }

            private boolean alwaysIncreasesDepth(Type constructor) {
                return constructor.accept(new DefaultTypeVisitor<Boolean, RuntimeException>(false) {
                    @Override
                    public Boolean visitAbstractData(Type type) throws RuntimeException {
                        return true;
                    }
                });
            }

            @Override
            public IValue visitNode(Type type) throws RuntimeException {
                currentDepth++;
                IValue[] children = new IValue[rand.nextInt(20)];
                for (int i = 0; i < children.length; i++) {
                    children[i] = randomType().accept(this);
                }
                INode result = vf.node("nodename" + rand.nextInt(2000), children);
                if (currentDepth < maxDepth && rand.nextDouble() > 0.6) {
                    int kwArgs = 1 + rand.nextInt(8);
                    for (int i = 0; i < kwArgs; i++) {
                        result = result.asWithKeywordParameters().setParameter("kwArg" + i, randomType().accept(this));
                    }
                }
                else if (currentDepth < maxDepth && rand.nextDouble() > 0.9) {
                    int annos = 1 + rand.nextInt(6);
                    for (int i = 0; i < annos; i++) {
                        result = result.asAnnotatable().setAnnotation("anno" + i, randomType().accept(this));
                    }
                }
                currentDepth--;
                return result;
            }


            @Override
            public IValue visitConstructor(Type type) throws RuntimeException {
                currentDepth++;
                IValue[] children = new IValue[type.getArity()];
                for (int i = 0; i < children.length; i++) {
                    children[i] = type.getFieldType(i).accept(this);
                }
                IConstructor result = vf.constructor(type, children);
                Map<String, Type> annos = ts.getAnnotations(type);
                if (annos != null && !annos.isEmpty()) {
                    if (rand.nextBoolean()) {
                        for (Map.Entry<String, Type> an: annos.entrySet()) {
                            if (rand.nextDouble() > 0.4) {
                                result = result.asAnnotatable().setAnnotation(an.getKey(), an.getValue().accept(this));
                            }
                        }
                    }
                }
                Map<String, Type> kws = ts.getKeywordParameters(type);
                if (kws != null && !kws.isEmpty()) {
                    if (rand.nextBoolean()) {
                        for (Map.Entry<String, Type> kw: kws.entrySet()) {
                            if (rand.nextDouble() > 0.4) {
                                result = result.asWithKeywordParameters().setParameter(kw.getKey(), kw.getValue().accept(this));
                            }
                        }
                    }
                }
                currentDepth--;
                return result;
            }

            @Override
            public IValue visitReal(Type type) throws RuntimeException {
                return vf.real(rand.nextDouble());
            }

            @Override
            public IValue visitInteger(Type type) throws RuntimeException {
                return vf.integer(rand.nextInt());
            }

            @Override
            public IValue visitRational(Type type) throws RuntimeException {
                return vf.rational(rand.nextInt(), rand.nextInt());
            }

            @Override
            public IValue visitList(Type type) throws RuntimeException {
                currentDepth++;
                int arity = currentDepth >= maxDepth ? 0 : rand.nextInt(20);
                IListWriter result = vf.listWriter();
                for (int i = 0; i < arity; i++) {
                    result.append(type.getElementType().accept(this));
                }
                currentDepth--;
                return result.done();
            }

            @Override
            public IValue visitMap(Type type) throws RuntimeException {
                currentDepth++;
                int arity = currentDepth >= maxDepth ? 0 : rand.nextInt(20);
                IMapWriter result = vf.mapWriter(); 
                for (int i = 0; i < arity; i++) {
                    IValue key = type.getKeyType().accept(this);
                    IValue value = type.getValueType().accept(this);
                    result.put(key, value);
                }
                currentDepth--;
                return result.done();
            }

            @Override
            public IValue visitNumber(Type type) throws RuntimeException {
                return visitInteger(type);
            }

            @Override
            public IValue visitAlias(Type type) throws RuntimeException {
                return type.getAliased().accept(this);
            }

            @Override
            public IValue visitSet(Type type) throws RuntimeException {
                currentDepth++;
                int arity = currentDepth >= maxDepth ? 0 : rand.nextInt(20);
                ISetWriter result = vf.setWriter();
                for (int i = 0; i < arity; i++) {
                    result.insert(type.getElementType().accept(this));
                }
                currentDepth--;
                return result.done();
            }

            @Override
            public IValue visitSourceLocation(Type type) throws RuntimeException {
                try {
                    return vf.sourceLocation("correct", "auth" + rand.nextDouble(), "/" + rand.nextInt());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e); 
                }
            }

            @Override
            public IValue visitString(Type type) throws RuntimeException {
                return vf.string("random-" + rand.nextInt() +"-string");
            }


            @Override
            public IValue visitTuple(Type type) throws RuntimeException {
                currentDepth++;
                IValue[] result = new IValue[type.getArity()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = type.getFieldType(i).accept(this);
                }
                currentDepth--;
                return vf.tuple(result);
            }

            @Override
            public IValue visitDateTime(Type type) throws RuntimeException {
                return vf.datetime(rand.nextLong());
            }
            @Override
            public IValue visitValue(Type type) throws RuntimeException {
                return randomType().accept(this);
            }

            @Override
            public IValue visitVoid(Type type) throws RuntimeException {
                return null;
            }

            @Override
            public IValue visitBool(Type type) throws RuntimeException {
                return vf.bool(rand.nextBoolean());
            }

            @Override
            public IValue visitParameter(Type type) throws RuntimeException {
                throw new UnsupportedOperationException("Shouldn't visit into parameters");
            }
            @Override
            public IValue visitExternal(Type type) throws RuntimeException {
                throw new UnsupportedOperationException("External types not supported");
            }
        });
    }


}
