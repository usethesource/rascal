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
package org.rascalmpl.library.lang.yaml;

import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class YAMLTypeFactory {
	public static final TypeStore yaml = new TypeStore();
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Node = tf.abstractDataType(yaml, "Node");

	public static final Type Node_sequence = tf.constructor(yaml, Node, "sequence",
			tf.listType(Node), "list");

	public static final Type Node_scalar = tf.constructor(yaml, Node, "scalar", 
			tf.valueType(), "value");
	
	public static final Type Node_reference = tf.constructor(yaml, Node, "reference");
	
	public static final Type Node_mapping = tf.constructor(yaml, Node, "mapping",
			tf.mapType(Node, Node), "map");
	
	
	public static YAMLTypeFactory getInstance() {
		return InstanceHolder.factory;
	}
	
	private YAMLTypeFactory() {
	}
	
	public static TypeStore getStore() {
		return yaml;
	}
	
	private static final class InstanceHolder {
		public final static YAMLTypeFactory factory = new YAMLTypeFactory();
	}
	
}
