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
package org.rascalmpl.library.lang.xml;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	public static final TypeStore xml = new TypeStore();
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Node = tf.abstractDataType(xml, "Node");
	public static final Type Namespace = tf.abstractDataType(xml, "Namespace");

	public static final Type Namespace_namespace = tf.constructor(xml, Namespace, "namespace", 
			tf.stringType(), "prefix",
			tf.stringType(), "uri");
	public static final Type Namespace_none = tf.constructor(xml, Namespace, "none"); 
	
	
	public static final Type Node_document = tf.constructor(xml, Node, "document", 
			Node, "root");

	public static final Type Node_attribute = tf.constructor(xml, Node, "attribute", 
			Namespace, "namespace",
			tf.stringType(), "name",
			tf.stringType(), "text");

	public static final Type Node_element = tf.constructor(xml, Node, "element", 
			Namespace, "namespace",
			tf.stringType(), "name",
			tf.listType(Node), "children");

	
	public static final Type Node_charData = tf.constructor(xml, Node, "charData",
			tf.stringType(), "text");
	public static final Type Node_cdata = tf.constructor(xml, Node, "cdata",
			tf.stringType(), "text");
	public static final Type Node_comment = tf.constructor(xml, Node, "comment",
			tf.stringType(), "text");
	public static final Type Node_pi = tf.constructor(xml, Node, "pi",
			tf.stringType(), "target",
			tf.stringType(), "text");
	
	public static final Type Node_entityRef = tf.constructor(xml, Node, "entityRef",
			tf.stringType(), "name");

	public static final Type Node_charRef = tf.constructor(xml, Node, "charRef",
			tf.integerType(), "code");

	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {
	}
	
	public static TypeStore getStore() {
		return xml;
	}
}
