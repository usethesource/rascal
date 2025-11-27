/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Davy Landman - davy.landman@swat.engineering - Swat.engineering
*******************************************************************************/

package org.rascalmpl.test.functionality;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.ATermReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;


public class ATermTests {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static Type Boolean = tf.abstractDataType(ts, "Boolean");
	
	private static Type Name = tf.abstractDataType(ts, "Name");
	private static Type True = tf.constructor(ts, Boolean, "true");
	private static Type False= tf.constructor(ts, Boolean, "false");
	private static Type And= tf.constructor(ts, Boolean, "and", Boolean, Boolean);
	private static Type Or= tf.constructor(ts, Boolean, "or", tf.listType(Boolean));
	private static Type Not= tf.constructor(ts, Boolean, "not", Boolean);
	private static Type TwoTups = tf.constructor(ts, Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = tf.constructor(ts, Name, "name", tf.stringType());
	private static Type Friends = tf.constructor(ts, Boolean, "friends", tf.listType(Name));
	private static Type Couples = tf.constructor(ts, Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	
	private IValue[] testValues = {
			vf.constructor(True).asWithKeywordParameters().setParameter("anno", vf.constructor(False)),
			vf.constructor(True).asWithKeywordParameters().setParameter("anno", vf.constructor(False)).asWithKeywordParameters().setParameter("banno", vf.constructor(False)),
			vf.constructor(True),
			vf.constructor(True),
			vf.constructor(And, vf.constructor(True), vf.constructor(False)),
			vf.constructor(Not, vf.constructor(And, vf.constructor(True), vf.constructor(False))),
			vf.constructor(And, vf.constructor(And, vf.constructor(True), vf.constructor(True)), vf.constructor(And, vf.constructor(True), vf.constructor(True))),
			vf.constructor(TwoTups, vf.tuple(vf.constructor(True), vf.constructor(False)),vf.tuple(vf.constructor(True), vf.constructor(False))),
			vf.constructor(Or, vf.list(vf.constructor(True), vf.constructor(False), vf.constructor(True))),
			vf.constructor(Friends, vf.list(name("Hans"), name("Bob"))),
			vf.constructor(Or, vf.list()),
			vf.constructor(Couples, vf.list(vf.tuple(name("A"), name("B")), vf.tuple(name("C"), name("D"))))
	};
	
	static {
		ts.declareKeywordParameter(Boolean, "anno", Boolean);
		ts.declareKeywordParameter(Boolean, "banno", Boolean);
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
	
	@Test
	public void testATermReader() {
		ATermReader testReader = new ATermReader();
		
		try {
			for (int i = 0; i < testATerm.length; i++) {
				IValue result = testReader.read(vf, ts, Boolean, new ByteArrayInputStream(testATerm[i].getBytes()));
				System.err.println(testATerm[i] + " -> " + result);
				
				if (!result.equals(testValues[i])) {
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
