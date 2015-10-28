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

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.fast.ValueFactory;
import org.rascalmpl.value.io.binary.BinaryReader;
import org.rascalmpl.value.io.binary.BinaryWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

/**
 * @author Arnold Lankamp
 */
public class TestBinaryIO extends TestCase {
	private static TypeStore ts = new TypeStore();
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactory.getInstance();
	private static Type Boolean = tf.abstractDataType(ts,"Boolean");
	
	private static Type Name = tf.abstractDataType(ts,"Name");
	private static Type True = tf.constructor(ts,Boolean, "true");
	private static Type False= tf.constructor(ts,Boolean, "false");
	private static Type And= tf.constructor(ts,Boolean, "and", Boolean, Boolean);
	private static Type Or= tf.constructor(ts,Boolean, "or", tf.listType(Boolean));
	private static Type Not= tf.constructor(ts,Boolean, "not", Boolean);
	private static Type TwoTups = tf.constructor(ts,Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = tf.constructor(ts,Name, "name", tf.stringType());
	private static Type Friends = tf.constructor(ts,Boolean, "friends", tf.listType(Name));
	private static Type Couples = tf.constructor(ts,Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	
	private IValue[] testValues = {
			vf.constructor(True),
			vf.constructor(And, vf.constructor(True), vf.constructor(False)),
			vf.constructor(Not, vf.constructor(And, vf.constructor(True), vf.constructor(False))),
			vf.constructor(TwoTups, vf.tuple(vf.constructor(True), vf.constructor(False)),vf.tuple(vf.constructor(True), vf.constructor(False))),
			vf.constructor(Or, vf.list(vf.constructor(True), vf.constructor(False), vf.constructor(True))),
			vf.constructor(Friends, vf.list(name("Hans"), name("Bob"))),
			vf.constructor(Or, vf.list(Boolean)),
			vf.constructor(Couples, vf.list(vf.tuple(name("A"), name("B")), vf.tuple(name("C"), name("D")))),
			vf.integer(0),
			vf.integer(1),
			vf.integer(-1),
			vf.string("🍝"),
			vf.integer(Integer.MAX_VALUE),
			vf.integer(Integer.MIN_VALUE),
			vf.integer(new byte[]{(byte)0xfe, (byte)0xdc, (byte)0xba, (byte)0x98, (byte)0x76, (byte)0x54}),
			vf.constructor(True).asAnnotatable().setAnnotation("test", vf.integer(1))
	};

	private static IValue name(String n){
		return vf.constructor(NameNode, vf.string(n));
	}

	public void testBinaryIO(){
		try{
			for(int i = 0; i < testValues.length; i++){
				IValue value = testValues[i];
				
				System.out.println(value); // Temp

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BinaryWriter binaryWriter = new BinaryWriter(value, baos, ts);
				binaryWriter.serialize();
				
				//PBFWriter.writeValueToFile(value, new File("/tmp/testIO"+i+".pbf")); // Temp

				byte[] data = baos.toByteArray();
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				BinaryReader binaryReader = new BinaryReader(vf, ts, bais);
				printBytes(data); // Temp
				IValue result = binaryReader.deserialize();

				System.out.println(result); // Temp
				System.out.println(); // Temp
				
				if(!value.isEqual(result)){
					String message = "Not equal: \n\t"+value+" : "+value.getType()+"\n\t"+result+" : "+result.getType();
					System.err.println(message);
					fail(message);
				}
			}
		}catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
	}
	
	private final static String[] HEX = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
	
	// May be handy when debugging.
	private static void printBytes(byte[] bytes){
		for(int i = 0; i < bytes.length; i++){
			byte b = bytes[i];
			int higher = (b & 0xf0) >> 4;
			int lower = b & 0xf;
			System.out.print("0x");
			System.out.print(HEX[higher]);
			System.out.print(HEX[lower]);
			System.out.print(" ");
		}
		System.out.println();
	}
}
