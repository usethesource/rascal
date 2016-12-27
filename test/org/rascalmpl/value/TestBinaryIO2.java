/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*******************************************************************************/
package org.rascalmpl.value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.rascalmpl.value.io.binary.message.IValueReader2;
import org.rascalmpl.value.io.binary.message.IValueWriter2;
import org.rascalmpl.value.io.binary.stream.IValueInputStream2;
import org.rascalmpl.value.io.binary.stream.IValueOutputStream2;
import org.rascalmpl.value.io.binary.stream.IValueOutputStream2.CompressionRate;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireOutputStream;
import org.rascalmpl.value.io.old.BinaryWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.RandomValues;
import org.rascalmpl.values.ValueFactoryFactory;

import junit.framework.TestCase;

/**
 * @author Arnold Lankamp
 */
public class TestBinaryIO2 extends TestCase {
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public void testBinaryIO() {
	    TypeStore ts = new TypeStore();
	    RandomValues.addNameType(ts);
	    for (IValue value: RandomValues.getTestValues(vf)) {
	        ioRoundTrip(value, 0);
	    }
	}
	
	public void testRandomBinaryIO() {
	    TypeStore ts = new TypeStore();
	    Type name = RandomValues.addNameType(ts);
	    Random r = new Random(42);
	    for (int i = 0; i < 20; i++) {
	        IValue value = RandomValues.generate(name, ts, vf, r, 10);
	        ioRoundTrip(value, 42);
	    }
	}
	
	public void testConstructorTypeWithLabel() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A");
	    Type cons = tf.constructor(ts, adt, "b", tf.integerType(), "i");
	    iopRoundTrip(cons, 0);
	}
	public void testConstructorTypeWithParams() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");
	    iopRoundTrip(cons, 0);
	}
	public void testConstructorTypeWithInstanciatedParams() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");

	    HashMap<Type, Type> binding = new HashMap<>();
	    binding.put(tf.parameterType("T"), tf.integerType());
	    iopRoundTrip(cons.instantiate(binding), 0);
	}
	
	public void testRandomTypesIO() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Random r = new Random();
	    int seed = r.nextInt();
	    r.setSeed(seed);
        for (int i =0; i < 1000; i++) {
            Type tp = tf.randomType(ts, r, 5);
            iopRoundTrip(tp, seed);
        }
	}
	public void testSmallRandomTypesIO() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Random r = new Random();
	    int seed = r.nextInt();
	    r.setSeed(seed);
        for (int i =0; i < 1000; i++) {
            Type tp = tf.randomType(ts, r, 3);
            iopRoundTrip(tp, seed);
        }
	}
	
	public void testOldFilesStillSupported() {
	    TypeStore ts = new TypeStore();
	    Type name = RandomValues.addNameType(ts);
	    Random r = new Random(42);
	    for (int i = 0; i < 20; i++) {
	        IValue value = RandomValues.generate(name, ts, vf, r, 10);
	        ioRoundTripOld(value, 42);
	    }
	}
	
	public void testConstructorWithParameterized1() {
	    TypeStore ts = new TypeStore();
        TypeFactory tf = TypeFactory.getInstance();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");

	    HashMap<Type, Type> binding = new HashMap<>();
	    binding.put(tf.parameterType("T"), tf.integerType());
	    ioRoundTrip(vf.constructor(cons.instantiate(binding), vf.integer(42)), 0);
	}

	public void testConstructorWithParameterized2() {
	    TypeStore ts = new TypeStore();
        TypeFactory tf = TypeFactory.getInstance();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");

	    ioRoundTrip(vf.constructor(cons, vf.integer(42)), 0);
	}
	

    private void iopRoundTrip(Type tp, int seed) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (IWireOutputStream w = new BinaryWireOutputStream(buffer, 1000)) {
                IValueWriter2.write(w, WindowSizes.SMALL_WINDOW, tp);
            }
            try (IWireInputStream read = new BinaryWireInputStream(new ByteArrayInputStream(buffer.toByteArray()))) {
                Type result = IValueReader2.readType(read, vf);
                if(!tp.equals(result)){
                    String message = "Not equal: (seed: " + seed +") \n\t"+result+" expected: "+seed;
                    System.err.println(message);
                    fail(message);
                }
            }
		}
	    catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
    }

    private void ioRoundTrip(IValue value, int seed) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (IValueOutputStream2 w = new IValueOutputStream2(buffer, CompressionRate.Normal)) {
                w.write(value);
            }
            try (IValueInputStream2 read = new IValueInputStream2(new ByteArrayInputStream(buffer.toByteArray()), vf)) {
                IValue result = read.read();
                if(!value.isEqual(result)){
                    String message = "Not equal: (seed: " + seed +") \n\t"+value+" : "+value.getType()+"\n\t"+result+" : "+result.getType();
                    System.err.println(message);
                    fail(message);
                }
            }
		}
	    catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
    }

    private void ioRoundTripOld(IValue value, int seed) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            BinaryWriter w = new BinaryWriter(value, buffer, new TypeStore());
            w.serialize();
            buffer.flush();
            try (IValueInputStream2 read = new IValueInputStream2(new ByteArrayInputStream(buffer.toByteArray()), vf)) {
                IValue result = read.read();
                if(!value.isEqual(result)){
                    String message = "Not equal: (seed: " + seed + ") \n\t"+value+" : "+value.getType()+"\n\t"+result+" : "+result.getType();
                    System.err.println(message);
                    fail(message);
                }
            }
		}
	    catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
    }
}
