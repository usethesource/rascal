/** 
 * Copyright (c) 2016, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.rascalmpl.types.TypeReifier;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

import junit.framework.TestCase;

public class TypeReificationTest extends TestCase {

    public void testJustRandomTypesWithoutExceptions() {
        TypeFactory tf = TypeFactory.getInstance();
        TypeStore store = new TypeStore();
        Random rnd = new Random();
        
        List<Type> collector = new LinkedList<>();
        int tries = 50000;
        
        for (int i = 0; i < tries; i++) {
            collector.add(tf.randomType(store, rnd, 5));
        }
        
        // no exceptions thrown
        assertTrue(collector.size() == tries);
    }
    
    public void testEmptyTupleBidirectionality() {
        TypeFactory tf = TypeFactory.getInstance();
        testOne(tf.tupleEmpty(), new TypeStore());
    }
    
    public void testEmptyTupleReturnFunBidirectionality() {
        TypeFactory tf = TypeFactory.getInstance();
        testOne(tf.functionType(tf.tupleEmpty(), tf.voidType(), tf.voidType()), new TypeStore());
        testOne(tf.functionType(tf.tupleEmpty(), tf.voidType(), null), new TypeStore());
        testOne(tf.functionType(tf.tupleEmpty(), tf.voidType(), tf.tupleEmpty()), new TypeStore());
        testOne(tf.functionType(tf.tupleEmpty(), tf.tupleEmpty(), tf.voidType()), new TypeStore());
        testOne(tf.functionType(tf.tupleEmpty(), tf.tupleEmpty(), null), new TypeStore());
        testOne(tf.functionType(tf.tupleEmpty(), tf.tupleEmpty(), tf.tupleEmpty()), new TypeStore());
    }

    private void testOne(Type type, TypeStore store) {
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        TypeReifier tr = new TypeReifier(vf);
        IMap syntax = vf.mapWriter().done();
        IConstructor reified = tr.typeToValue(type, store, syntax);
        try {
            Type recovered = tr.valueToType(reified);
            if(recovered != type) {
                System.err.println("NOT OK: " + type + " != " + recovered);
                System.err.println("toString equal? " + recovered.toString().equals(type.toString()));
                System.err.println("reified was      : " + reified);
                System.err.println("reified recovered: " + tr.typeToValue(recovered, store, syntax));
            }

            assertTrue(recovered == type);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can not come back from " + reified);
            fail(e.getMessage());
        }
    }
    
    public void testFuncTypeKeywordParameter() {
        TypeFactory tf = TypeFactory.getInstance();
        testOne(tf.functionType(tf.voidType(), tf.tupleType(new Type[] {tf.integerType(), tf.realType()}, new String[] {"a", "b"}), tf.tupleType(new Type[] {tf.integerType()},  new String[] {"a"})), new TypeStore());
    }
    
    public void testFuncTypeParametersOrder() {
        TypeFactory tf = TypeFactory.getInstance();
        testOne(tf.functionType(tf.voidType(), tf.tupleType(new Type[] {tf.integerType(), tf.realType()}, new String[] {"a", "b"}), null), new TypeStore());
        testOne(tf.functionType(tf.voidType(), tf.tupleType(tf.integerType(), tf.realType()), null), new TypeStore());
    }

    public void testFuncTypeReificationBidirectionality() {
        TypeFactory tf = TypeFactory.getInstance();
        TypeStore store = new TypeStore();
        
        for (int i = 0; i < 50; i++) {
            Type type = tf.randomType(store);
            while (!type.isFunction()) {
                type = tf.randomType(store);
            }
            
            testOne(type, store);
        }
    }
    
    public void testTypeReificationBidirectionality() {
        TypeFactory tf = TypeFactory.getInstance();
        TypeStore store = new TypeStore();
        
        for (int i = 0; i < 10_000; i++) {
            testOne(tf.randomType(store), store);
        }
    }
}
