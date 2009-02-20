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
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ValueFactoryFactory;

public class IOTests extends TestCase {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static Type Boolean = ts.abstractDataType("Boolean");
	
	private static Type Name = ts.abstractDataType("Name");
	private static Type True = ts.constructor(Boolean, "true");
	private static Type False= ts.constructor(Boolean, "false");
	private static Type And= ts.constructor(Boolean, "and", Boolean, Boolean);
	private static Type Or= ts.constructor(Boolean, "or", tf.listType(Boolean));
	private static Type Not= ts.constructor(Boolean, "not", Boolean);
	private static Type TwoTups = ts.constructor(Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = ts.constructor(Name, "name", tf.stringType());
	private static Type Friends = ts.constructor(Boolean, "friends", tf.listType(Name));
	private static Type Couples = ts.constructor(Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	
	private IValue[] testValues = {
			vf.constructor(True).setAnnotation("anno", vf.constructor(False)),
			vf.constructor(True).setAnnotation("anno", vf.constructor(False)).setAnnotation("banno", vf.constructor(False)),
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
	
	static {
		ts.declareAnnotation(Boolean, "anno", Boolean);
		ts.declareAnnotation(Boolean, "banno", Boolean);
	}
	
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
		
		
		
		try {
			for (int i = 0; i < testATerm.length; i++) {
				IValue result = testReader.read(vf, ts, Boolean, new ByteArrayInputStream(testATerm[i].getBytes()));
				System.err.println(testATerm[i] + " -> " + result);
				
				if (!result.isEqual(testValues[i])) {
					fail(testATerm[i] + " did not parse correctly: " + result + " != " + testValues[i]);
				}
			}
		} catch (FactTypeUseException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

	
}
