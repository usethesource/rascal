package org.rascalmpl.library.lang.json;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

public class Factory {
	private static final TypeFactory tf = TypeFactory.getInstance();
	private static final TypeStore json = new TypeStore();
	
	public static final Type JSON = tf.abstractDataType(json, "JSON");
	
	public static final Type JSON_null = tf.constructor(json, JSON, "null");
	
	public static final Type JSON_object = tf.constructor(json, JSON, "object",
			tf.mapType(tf.stringType(), JSON), "properties");
	
	public static final Type JSON_array = tf.constructor(json, JSON, "array",
			tf.listType(JSON), "values");
	
	public static final Type JSON_number = tf.constructor(json, JSON, "number", tf.realType(), "n");
	public static final Type JSON_string = tf.constructor(json, JSON, "string", tf.stringType(), "s");
	public static final Type JSON_boolean = tf.constructor(json, JSON, "boolean", tf.boolType(), "b");
	
	public static final Type JSON_ivalue = tf.constructor(json, JSON, "ivalue", tf.valueType(), "v");
	
	

}
