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

import org.rascalmpl.value.impl.fast.ValueFactory;
import org.rascalmpl.value.io.binary.message.IValueReader;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.stream.IValueInputStream;
import org.rascalmpl.value.io.binary.stream.IValueOutputStream;
import org.rascalmpl.value.io.binary.stream.IValueOutputStream.CompressionRate;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireOutputStream;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.RandomValues;

import junit.framework.TestCase;

/**
 * @author Arnold Lankamp
 */
public class TestBinaryIO extends TestCase {
	private static IValueFactory vf = ValueFactory.getInstance();
	public void testBinaryIO() {
	    TypeStore ts = new TypeStore();
	    RandomValues.addNameType(ts);
	    for (IValue value: RandomValues.getTestValues(vf)) {
	        ioRoundTrip(ts, value);
	    }
	}
	
	public void testRandomBinaryIO() {
	    TypeStore ts = new TypeStore();
	    Type name = RandomValues.addNameType(ts);
	    Random r = new Random(42);
	    for (int i = 0; i < 20; i++) {
	        IValue value = RandomValues.generate(name, ts, vf, r, 10);
	        ioRoundTrip(ts, value);
	    }
	}
	
	public void testConstructorTypeWithLabel() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A");
	    Type cons = tf.constructor(ts, adt, "b", tf.integerType(), "i");
	    iopRoundTrip(ts, cons);
	}
	public void testConstructorTypeWithParams() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");
	    iopRoundTrip(ts, cons);
	}
	public void testConstructorTypeWithInstanciatedParams() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
	    Type adt = tf.abstractDataType(ts, "A", tf.parameterType("T"));
	    Type cons = tf.constructor(ts, adt, "b", tf.parameterType("T"), "tje");
	    
	    HashMap<Type, Type> binding = new HashMap<>();
	    binding.put(tf.parameterType("T"), tf.integerType());
	    iopRoundTrip(ts, cons.instantiate(binding));
	}
	
	public void testRandomTypesIO() {
        TypeFactory tf = TypeFactory.getInstance();
	    TypeStore ts = new TypeStore();
        for (int i =0; i < 100; i++) {
            Type tp = tf.randomType(ts);
            iopRoundTrip(ts, tp);
        }
	}

    private void iopRoundTrip(TypeStore ts, Type tp) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (IWireOutputStream w = new BinaryWireOutputStream(buffer, 1000)) {
                IValueWriter.write(w, ts, WindowSizes.SMALL_WINDOW, tp);
            }
            try (IWireInputStream read = new BinaryWireInputStream(new ByteArrayInputStream(buffer.toByteArray()))) {
                Type result = IValueReader.readType(read, vf, new TypeStore());
                if(!tp.equals(result)){
                    String message = "Not equal: \n\t"+result+" expected: "+tp;
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

    private void ioRoundTrip(TypeStore ts, IValue value) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (IValueOutputStream w = new IValueOutputStream(buffer, ts, CompressionRate.Normal)) {
                w.write(value);
            }
            try (IValueInputStream read = new IValueInputStream(new ByteArrayInputStream(buffer.toByteArray()), vf, ts)) {
                IValue result = read.read();
                if(!value.isEqual(result)){
                    String message = "Not equal: \n\t"+value+" : "+value.getType()+"\n\t"+result+" : "+result.getType();
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
