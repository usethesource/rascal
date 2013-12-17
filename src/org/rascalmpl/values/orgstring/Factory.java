package org.rascalmpl.values.orgstring;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {

	public static final TypeStore orgstrings = new TypeStore();
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type OrgString = tf.abstractDataType(orgstrings, "OrgString");
	
	public static final Type OrgString_chunk = 
			tf.constructor(orgstrings, OrgString, "chunk", 
					tf.stringType(), "val", tf.sourceLocationType(), "origin");
	
	public static final Type OrgString_orphan = 
			tf.constructor(orgstrings, OrgString, "orphan", 
					tf.stringType(), "val");
	
	public static final Type OrgString_insincere = 
			tf.constructor(orgstrings, OrgString, "insincere", 
					tf.stringType(), "val", tf.listType(tf.sourceLocationType()), "origins");

	public static final Type OrgString_concat= 
			tf.constructor(orgstrings, OrgString, "concat", 
					OrgString, "lhs", OrgString, "rhs");

	
}
