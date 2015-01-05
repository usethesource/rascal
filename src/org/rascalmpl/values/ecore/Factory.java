/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.ecore;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	public static final TypeStore ecore = new TypeStore();
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type ECore = tf.abstractDataType(ecore, "ECore");
	public static final Type Classifier = tf.abstractDataType(ecore, "Classifier");
	public static final Type Type = tf.abstractDataType(ecore, "Type");
	public static final Type Feature = tf.abstractDataType(ecore, "Feature");
	public static final Type Literal = tf.abstractDataType(ecore, "Literal");
	public static final Type Structural = tf.abstractDataType(ecore, "Structural");
	
	public static final Type Package = tf.aliasType(ecore, "Package", tf.listType(tf.stringType()));

	public static final Type ECore_ecore = tf.constructor(ecore, ECore, "ecore",
			tf.setType(Classifier), "classifiers",
			tf.relType(Classifier, "class", Feature, "feature", Type, "typ"), "features",
			tf.relType(Classifier, "owner",
					Feature, "feature",
					Feature, "opposite"), "opposites",
			tf.relType(Classifier, "sub", Classifier, "super"), "subtype");
	
	public static final Type Classifier_dataType = tf.constructor(ecore, Classifier, "dataType",
			Package, "package",
			tf.stringType(), "name",
			tf.boolType(), "serializable"); 
	
	public static final Type Classifier_enum = tf.constructor(ecore, Classifier, "enum",
			Package, "package",
			tf.stringType(), "name",
			tf.listType(Literal), "literals"); 
	
	public static final Type Classifier_class = tf.constructor(ecore, Classifier, "class",
			Package, "package",
			tf.stringType(), "name",
			tf.boolType(), "abstract",
			tf.boolType(), "interface"); 
	
	public static final Type Literal_literal = tf.constructor(ecore, Literal, "literal",
			tf.stringType(), "name",
			tf.integerType(), "val");

	public static final Type Type_classifier = tf.constructor(ecore, Type, "classifier", 
			Classifier, "classifier",
			tf.boolType(), "ordered",
			tf.boolType(), "unique",
			tf.integerType(), "lowerBound",
			tf.integerType(), "upperBound",
			tf.boolType(), "many",
			tf.boolType(), "required");
	
	public static final Type Type_signature = tf.constructor(ecore, Type, "signature", 
			Type, "result",
			tf.listType(Type), "parameters");


	public static final Type Type_none = tf.constructor(ecore, Type, "none"); 

	public static final Type Feature_structural = tf.constructor(ecore, Feature, "structural",
			Structural, "structural",
			tf.boolType(), "changeable",
			tf.boolType(), "transient",
			tf.boolType(), "unsettable",
			tf.boolType(), "derived");

	public static final Type Feature_operation = tf.constructor(ecore, Feature, "operation", 
			tf.stringType(), "name",
			tf.listType(tf.stringType()), "parameters");

	public static final Type Structural_attribute = tf.constructor(ecore, Structural, "attribute", 
			tf.stringType(), "name",
			tf.boolType(), "id");

	public static final Type Structural_reference = tf.constructor(ecore, Structural, "reference", 
			tf.stringType(), "name",
			tf.boolType(), "containment",
			tf.boolType(), "container",
			tf.boolType(), "resolveProxies");
	

	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {
	}
	
	public static TypeStore getStore() {
		return ecore;
	}
}
