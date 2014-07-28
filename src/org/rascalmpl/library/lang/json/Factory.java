package org.rascalmpl.library.lang.json;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.lang.json.io.JSONReadingTypeVisitor;

public class Factory {
	private static final TypeFactory tf = TypeFactory.getInstance();
	private static final TypeStore json = new TypeStore();
	
	public static final Type JSON = tf.abstractDataType(json, "JSON");
	
	public static final Type JSON_null = tf.constructor(json, JSON, "null");
	
	public static final Type JSON_object = tf.constructor(json, JSON, "object",
			tf.mapType(tf.stringType(), JSON), "properties");
	
	public static final Type JSON_array = tf.constructor(json, JSON, "array",
			tf.listType(JSON), "values");
	
	public static final Type JSON_integer = tf.constructor(json, JSON, "integer", tf.integerType(), "n");
	public static final Type JSON_float = tf.constructor(json, JSON, "float", tf.realType(), "r");
	public static final Type JSON_string = tf.constructor(json, JSON, "string", tf.stringType(), "s");
	public static final Type JSON_boolean = tf.constructor(json, JSON, "boolean", tf.boolType(), "b");
	
	public static final Type JSON_ivalue = tf.constructor(json, JSON, "ivalue", tf.valueType(), "v");
	
	public static abstract class JSON {
		public abstract IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr);
	}
	
	public static class Null extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class Object extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class Array extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class Integer extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class Float extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class String extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class Boolean extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class Ivalue extends JSON {

		@Override
		public IValue toValue(IValueFactory vf, JSONReadingTypeVisitor tr) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}


}
