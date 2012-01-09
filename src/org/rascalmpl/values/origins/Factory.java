/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.origins;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	public static final TypeStore origins = new TypeStore();
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Origin = tf.abstractDataType(origins, "Origin");
	public static final Type Origin_literal = tf.constructor(origins, Origin, "literal", tf.sourceLocationType(), "location");
	public static final Type Origin_expression = tf.constructor(origins, Origin, "expression", tf.sourceLocationType(), "location");
	public static final Type Origin_none = tf.constructor(origins, Origin, "none");

}
