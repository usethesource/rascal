package org.rascalmpl.values.origins;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	public static TypeStore origins = new TypeStore();
	private static TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Origin = tf.abstractDataType(origins, "Origin");
	public static final Type Origin_literal = tf.constructor(origins, Origin, "literal", tf.sourceLocationType(), "location");
	public static final Type Origin_expression = tf.constructor(origins, Origin, "expression", tf.sourceLocationType(), "location");
	public static final Type Origin_none = tf.constructor(origins, Origin, "none");

}
