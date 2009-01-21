/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class IOTests extends TestCase {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactory.getInstance();
	private static Type Boolean = tf.abstractDataType("Boolean");
	
	private static Type Name = tf.abstractDataType("Name");
	private static Type True = tf.constructor(Boolean, "true");
	private static Type False= tf.constructor(Boolean, "false");
	private static Type And= tf.constructor(Boolean, "and", Boolean, Boolean);
	private static Type Or= tf.constructor(Boolean, "or", tf.listType(Boolean));
	private static Type Not= tf.constructor(Boolean, "not", Boolean);
	private static Type TwoTups = tf.constructor(Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = tf.constructor(Name, "name", tf.stringType());
	private static Type Friends = tf.constructor(Boolean, "friends", tf.listType(Name));
	private static Type Couples = tf.constructor(Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	
	private IValue[] testValues = {
			vf.constructor(True),
			vf.constructor(True),
			vf.constructor(True),
			vf.constructor(True),
			vf.constructor(And, vf.constructor(True), vf.constructor(False)),
			vf.constructor(Not, vf.constructor(And, vf.constructor(True), vf.constructor(False))),
			vf.constructor(And, vf.constructor(And, vf.constructor(True), vf.constructor(True)), vf.constructor(And, vf.constructor(True), vf.constructor(True))),
			vf.constructor(TwoTups, vf.tuple(vf.constructor(True), vf.constructor(False)),vf.tuple(vf.constructor(True), vf.constructor(False))),
			vf.constructor(Or, vf.list(vf.constructor(True), vf.constructor(False), vf.constructor(True))),
			vf.constructor(Friends, vf.list(name("Hans"), name("Bob"))),
			vf.constructor(Or, vf.list(Boolean)),
			vf.constructor(Couples, vf.list(vf.tuple(name("A"), name("B")), vf.tuple(name("C"), name("D"))))
	};
	
	private String[] testATerm = {
			"true{[\"anno\",false]}",
			"true{[\"banno\",false],[\"anno\",false]}",
			"true{}",
			"true",
			"and(true,false)",
		    "not(and(true,false))",
		    "!and(and(true,#A),#B)",
		    "twotups((true,false),(true,false))",
		    "or([true,false,true])",
		    "friends([name(\"Hans\"),name(\"Bob\")])",
		    "or([])",
		    "couples([(name(\"A\"),name(\"B\")),(name(\"C\"),name(\"D\"))])"
	    };

	private static IValue name(String n) {
		return vf.constructor(NameNode, vf.string(n));
	}
	
	public void testATermReader() {
		ATermReader testReader = new ATermReader();
		
		tf.declareAnnotation(Boolean, "anno", Boolean);
		tf.declareAnnotation(tf.valueType(), "banno", Boolean);
		
		try {
			for (int i = 0; i < testATerm.length; i++) {
				IValue result = testReader.read(vf, Boolean, new ByteArrayInputStream(testATerm[i].getBytes()));
				System.err.println(testATerm[i] + " -> " + result);
				
				if (!result.equals(testValues[i])) {
					fail(testATerm[i] + " did not parse correctly: " + result + " != " + testValues[i]);
				}
			}
		} catch (FactTypeError e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

	
}
