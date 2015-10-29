/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/

package org.rascalmpl.test.functionality;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

import junit.framework.TestCase;

public class IOTests extends TestCase {
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
			vf.constructor(True).asAnnotatable().setAnnotation("anno", vf.constructor(False)),
			vf.constructor(True).asAnnotatable().setAnnotation("anno", vf.constructor(False)).asAnnotatable().setAnnotation("banno", vf.constructor(False)),
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
	
	
}
