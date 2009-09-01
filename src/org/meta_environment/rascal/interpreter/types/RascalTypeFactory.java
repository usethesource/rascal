package org.meta_environment.rascal.interpreter.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.env.Environment;

public class RascalTypeFactory {
	private TypeFactory tf = TypeFactory.getInstance();

	private static class InstanceHolder {
		public static final RascalTypeFactory sInstance = new RascalTypeFactory();
	}
	
	public static RascalTypeFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	
	public Type functionType(Type returnType, Type argumentTypes) {
		return tf.externalType(new FunctionType(returnType, argumentTypes));
	}
	
	public Type nonTerminalType(IConstructor cons) {
		return tf.externalType(new NonTerminalType(cons));
	}
	
	public Type nonTerminalType(org.meta_environment.rascal.ast.Type symbol) {
		return tf.externalType(new NonTerminalType(symbol));
	}
	
	public Type overloadedFunctionType(Set<FunctionType> newAlternatives) {
		return tf.externalType(new OverloadedFunctionType(newAlternatives));
	}

	public Type reifiedType(Type arg) {
		return tf.externalType(new ReifiedType(arg));
	}
	
	/**
	 * Declare the ADT for representing reified types, which follows the Type module from the standard library exactly.
	 * 
	 * The type[&T] is declared using parameterized types such that instantiation should lead automatically to well represented
	 * types. The exception is abstract data-types themselves, since the name of a type actually defines it. So, later when
	 * we actually construct reified types they are a special case that need to be declared on-the-fly.
	 */
	public void declareReifiedTypes(Environment env) {
		TypeStore store = env.getStore();
		TypeFactory tf = TypeFactory.getInstance();
		Type T = tf.parameterType("T");
		Type T1 = tf.parameterType("T1");
		Type T2 = tf.parameterType("T2");
		Type T3 = tf.parameterType("T3");
		Type T4 = tf.parameterType("T4");
		Type T5 = tf.parameterType("T5");
		Type T6 = tf.parameterType("T6");
		Type T7 = tf.parameterType("T7");
		Type U = tf.parameterType("U");
		Type adt = reifiedType(T);
		
		// Basic types
		declareReifiedType(store, adt, T, tf.integerType());
		declareReifiedType(store, adt, T, tf.realType());
		declareReifiedType(store, adt, T, tf.boolType());
		declareReifiedType(store, adt, T, tf.valueType());
		declareReifiedType(store, adt, T, tf.voidType());
		declareReifiedType(store, adt, T, tf.sourceLocationType());
		declareReifiedType(store, adt, T, tf.nodeType());
		
		Map<Type, Type> bindings = new HashMap<Type,Type>();
		Type adtT;
		
		// Simply structured types, list , map and set
		bindings.clear();
		bindings.put(T, tf.listType(T));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "list", T, "element");
		
		bindings.clear();
		bindings.put(T, tf.setType(T));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "set", T, "element");
		
		bindings.clear();
		bindings.put(T, tf.mapType(T, U));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "map", T, "key", U, "value");
		
		// Tuple types
		bindings.clear();
		bindings.put(T, tf.tupleEmpty());
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple");
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2, T3));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2, T3);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2, T3, T4));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2, T3, T4);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2, T3, T4, T5));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2, T3, T4, T5);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2, T3, T4, T5, T6));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2, T3, T4, T5, T6);
		
		bindings.clear();
		bindings.put(T, tf.tupleType(T1, T2, T3, T4, T5, T6, T7));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "tuple", T1, T2, T3, T4, T5, T6, T7);
	
		// Relation types
		
		bindings.clear();
		bindings.put(T, tf.relType());
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel");
		
		bindings.clear();
		bindings.put(T, tf.relType(T));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2, T3));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2, T3);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2, T3, T4));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2, T3, T4);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2, T3, T4, T5));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2, T3, T4, T5);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2, T3, T4, T5, T6));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2, T3, T4, T5, T6);
		
		bindings.clear();
		bindings.put(T, tf.relType(T1, T2, T3, T4, T5, T6, T7));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "rel", T1, T2, T3, T4, T5, T6, T7);
		
		// Function Types
		RascalTypeFactory rtf = RascalTypeFactory.getInstance();
		
		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.voidType()));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func");
		
		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1);
		
		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2);

		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2, T3)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2, T3);

		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2, T3, T4)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2, T3, T4);

		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2, T3, T4, T5)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2, T3, T4, T5);

		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2, T3, T4, T5, T6)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2, T3, T4, T5);

		bindings.clear();
		bindings.put(T, rtf.functionType(T, tf.tupleType(T1, T2, T3, T4, T5, T6, T7)));
		adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, "func", T1, T2, T3, T4, T5, T6, T7);
		
		// abstract data types and constructors
		Type constructor = tf.abstractDataType(store, "Constructor");
		bindings.clear();
		
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2", T3, "t3");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2", T3, "t3", T4, "t4");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2", T3, "t3", T4, "t4", T5, "t5");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2", T3, "t3", T4, "t4", T5, "t5", T6, "t6");
		tf.constructor(store, constructor, "constructor", tf.stringType(), "name", T1, "t1", T2, "t2", T3, "t3", T4, "t4", T5, "t5", T6, "t6", T7, "t7");

		tf.constructor(store, adt, "adt", tf.stringType(), "name", tf.listType(constructor), "constructors");
		tf.constructor(store, adt, "adt", tf.stringType(), "name", tf.listType(adt), "parameters", tf.listType(constructor), "constructors");
	}

	private void declareReifiedType(TypeStore store, Type adt, Type T, Type type) {
		Map<Type, Type> bindings = new HashMap<Type,Type>();
		bindings.put(T, tf.integerType());
		Type adtT = adt.instantiate(store, bindings);
		tf.constructor(store, adtT, type.toString());
	}
}
