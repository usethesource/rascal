/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.test.infrastructure.TestFramework;

import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RascalTypes extends TestFramework {

    @Test
    public void testFunctionLub() {
        TypeFactory tf = TypeFactory.getInstance();
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();

        Type t1 = rtf.functionType(tf.integerType(), tf.tupleType(tf.integerType(), tf.integerType()), tf.voidType());
        Type t2 = rtf.functionType(tf.integerType(), tf.tupleType(tf.rationalType(), tf.rationalType()), tf.voidType());

        // if the arity is the same, the lub is still a function type (for computing the types of overloaded functions)
        assertTrue(!t1.lub(t2).isTop());
        assertTrue(t1.getArity() == t1.lub(t2).getArity());

        // but if its not the same, then we default to value, because we don't know how to call a function with different amounts of parameters
        Type t3 = rtf.functionType(tf.integerType(), tf.tupleType(tf.stringType()), tf.voidType());
        assertTrue(t1.lub(t3).isTop());
    }

    @Test
    public void testLubAndGlb() {
        TypeFactory tf = TypeFactory.getInstance();
        TypeStore store = new TypeStore();
        Random rnd = new Random();
        Set<Type> types = new HashSet<Type>();

        for (int i = 0; i <= 15000; i++) {
            // due to dependency injection tf.randomType will also generate
            // the external types offered by the rascal project, such
            // as functions, reifiedtypes and non-terminaltypes
            types.add(tf.randomType(store, rnd, 5));
        }

        for (Type t : types) {
            if (t.lub(t) != t) {
                fail("lub should be idempotent: " + t + " != " + t.lub(t));
            }
        }

        for(Type t1 : types) {
            for(Type t2 : types) {
                Type lub1 = t1.lub(t2);
                Type lub2 = t2.lub(t1);

                if(lub1 != lub2) {
                    System.err.println("Failure:");
                    System.err.println(t1 + ".lub(" + t2 + ") = " + lub1);
                    System.err.println(t2 + ".lub(" + t1 + ") = " + lub2);
                    fail("lub should be commutative");
                }

                if(t1.comparable(t2)) {
                    if (t1.isSubtypeOf(t2)) {
                        if(!t2.isSubtypeOf(lub1)) {
                            System.err.println("Failure. A type should be a sub-type of a lub it contributes to.");
                            System.err.println(t1 + ".lub(" + t2 + ") = " + lub1);
                            System.err.println("but: " + t2 + ".isSubTypeOf(" + lub1 + ") = false?");
                            fail("types should be sub-types of the lub they contribute to");
                        }
                    }
                    
                    if(t2.isSubtypeOf(t1)) {
                        if(!t1.isSubtypeOf(t1.lub(t2))) {
                            System.err.println("Failure. A type should be a sub-type of a lub it contributes to.");
                            System.err.println(t1 + ".lub(" + t2 + ") = " + lub1);
                            System.err.println("but: " + t1 + ".isSubTypeOf(" + lub1 + ") = false?");
                            fail("types should be sub-types of the lub they contribute to");
                        }
                    }
                }
            }
        }

        for(Type t : types) {
            if (t.glb(t) != t) {
                fail("glb should be idempotent: " + t + " != " + t.glb(t));
            }
        }

        for(Type t1 : types) {
            for(Type t2 : types) {
                Type glb1 = t1.glb(t2);
                Type glb2 = t2.glb(t1);

                if(glb1 != glb2) {
                    System.err.println("Failure:");
                    System.err.println(t1 + ".glb(" + t2 + ") = " + glb1);
                    System.err.println(t2 + ".glb(" + t1 + ") = " + glb2);
                    fail("glb should be commutative");
                }

                if(t1.comparable(t2)) {
                    if(t1.isSubtypeOf(t2)) {
                        assertTrue(t1.glb(t2).isSubtypeOf(t1));
                    }
                    if(t2.isSubtypeOf(t1)) {
                        assertTrue(t1.glb(t2).isSubtypeOf(t2));
                    }
                }
            }
        }
    }
}
