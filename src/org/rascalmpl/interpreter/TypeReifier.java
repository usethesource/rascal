/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * TypeReifier is a visitor that maps types to values that represent these types. These values have
 * very specific types, namely 'type[&T]' where &T is bound to the type the value represents. 
 * <br>
 * The 'type[&T]' is rank-2 polymorphic because &T can be bound differently on different nesting levels
 * of type representations. Therefore it can not be declared or represented in Rascal itself.
 * <br>
 *
 * This is the shape of the values that type reification produces:
<pre>
data type[&T] =
  \value() |
  \int() |
  \real() |
  \bool() |
  \map(Type \key, Type \value) |
  \list(Type element) |
  \set(Type element) |
  \rel(list[tuple[Type \type, str label]] fields) | // with labels
  \rel(list[Type] arguments) |  // without labels
  \tuple(list[tuple[Type \type, str label]] fields) | // with labels
  \tuple(list[Type] arguments) | // without labels
  \void() |
  \fun(Type \return, list[tuple[Type \type, str label]]) |  
  \node() |
  \non-terminal(Symbol symbol) |
  \adt(str name,  list[constructor] constructors, list[tuple[Type,Type]] bindings) |
  \parameter(str name, Type bound) |
  \loc() |
  \alias(str name, Type aliased, list[tuple[Type,Type]] bindings) |
  \reified(Type reified)

data constructor[&T] = 
  \constructor(str name, list[tuple[Type \type, str label]] fields)
</pre>
 */
public class TypeReifier implements ITypeVisitor<Result<IValue>> {
	private final IEvaluatorContext ctx;
	private final Type typeOfTypes;
	private final TypeFactory tf;
	private final Environment env;
	private final IValueFactory vf;
	private final Type param;
	private final TypeStore store;
	private final Type cons;
	private final Type valueAdt;
	private final Type listAdt;
	private final Type listCons;
	private final Type fieldType;
	private final Type bindingType;
	private final Type tupleType;
	
	private Set<IValue> visiting = new HashSet<IValue>();
	
	public TypeReifier(IEvaluatorContext ctx, IValueFactory valueFactory) {
		this.ctx = ctx;
		this.env = ctx.getCurrentEnvt();
		this.store = constructCompleteTypeStore(this.env);
		this.tf = TypeFactory.getInstance();
		this.vf = valueFactory;
		this.param = tf.parameterType("T");
		this.typeOfTypes = RascalTypeFactory.getInstance().reifiedType(param);
		this.cons = env.abstractDataType("constructor");

		store.declareAbstractDataType(typeOfTypes);
		
	    Map<Type,Type> bindings = bind(tf.valueType());
	    this.valueAdt = typeOfTypes.instantiate(bindings);
	    
	    // TODO note: maybe we should instantiate these instances of typeOfType with value to be on the safe side...
	    this.listAdt = tf.listType(valueAdt);
	    this.tupleType = tf.tupleType(typeOfTypes, typeOfTypes);
		this.bindingType = tf.listType(tupleType);
	    this.fieldType = tf.tupleType(typeOfTypes, "type", tf.stringType(), "label");
	    this.listCons = tf.listType(cons);
	}
	
	private static TypeStore constructCompleteTypeStore(Environment env) {
	  	TypeStore complete = new TypeStore();
	  	ModuleEnvironment mod = (ModuleEnvironment) env.getRoot();
		constructCompleteTypeStoreRec(complete, mod, new HashSet<java.lang.String>());
		return complete;
	}
	
	private static void constructCompleteTypeStoreRec(TypeStore complete, ModuleEnvironment env, java.util.Set<java.lang.String> done) {
		if (done.contains(env.getName())) {
			return;
		}
		done.add(env.getName());
		
		complete.importStore(env.getStore());
		
		for (java.lang.String i : env.getImports()) {
			constructCompleteTypeStoreRec(complete, env.getImport(i), done);
		}
	}
	

	/**
	 * Collects all constructor of the ADT, then builds the rather complex reified representation.
	 */
	public Result<IValue> visitAbstractData(Type type) {
		String name = type.getName();
		Type adtDefinition = store.lookupAbstractDataType(name);
		Map<Type,Type> bindings = bind(type);
		Type staticType;
		IValue result;
		IValue stub;
		
		staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "adt", tf.stringType(), "name", listCons, "constructors", bindingType, "bindings");
		stub = staticType.make(vf, vf.string(name), bindingType.make(vf), vf.list());
		
		if (visiting.contains(stub)) {
			// we break an infinite recursion here
			return makeResult(staticType, stub, ctx);
		}
		
		visiting.add(stub);
		
		IListWriter constructorListW = vf.listWriter(cons);
		for (Type alt : store.lookupAlternatives(adtDefinition)) {
			constructorListW.append(alt.accept(this).getValue());
		}
		IList constructorList = constructorListW.done();
		
		visiting.remove(stub);
		
		Type formals = adtDefinition.getTypeParameters();
		Type actuals = type.getTypeParameters();
		
		IList bindingList = computeBindingList(formals, actuals);
		
		result = staticType.make(vf, vf.string(name),  constructorList, bindingList);
		
		return makeResult(staticType.getAbstractDataType(), result, ctx);
	}

	private IList computeBindingList(Type formals, Type actuals) {
		IListWriter bindingRepresentation = vf.listWriter(tupleType);
		int i = 0;
		if (! formals.isVoidType()) {
			for (Type key : formals) {
				Result<IValue> keyRep = key.accept(this);
				Result<IValue> value = actuals.getFieldType(i++).accept(this);
				bindingRepresentation.append(vf.tuple(keyRep.getValue(), value.getValue()));
			}
		}
		IList bindingList = bindingRepresentation.done();
		return bindingList;
	}

	private IList getTypeParameterList(Type params) {
		if (params.isVoidType()) {
			return vf.list();
		}
		
		Type paramListType = tf.listType(valueAdt);
		IListWriter reifiedW = paramListType.writer(vf);
		
		for (Type p : params) {
			reifiedW.append(p.accept(this).getValue());
		}
		
		IList reifiedParams = reifiedW.done();
		return reifiedParams;
	}

	private Map<Type, Type> bind(Type arg) {
		Map<Type, Type> bindings = new HashMap<Type,Type>();
		bindings.put(param, arg);
		return bindings;
	}

	public Result<IValue> visitAlias(Type type) {
		String name = type.getName();
		Type params = type.getTypeParameters();
		Map<Type,Type> bindings = bind(type);
		Result<IValue> aliased = type.getAliased().accept(this);
		Type formals = store.getAlias(name).getTypeParameters();
		
		IList bindingList = computeBindingList(formals, params);
		
		Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "alias", tf.stringType(), "name", aliased.getType(), "aliased", bindingType, "bindings");
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, vf.string(name), aliased.getValue(), bindingList), ctx);
	}

	public Result<IValue> visitBool(Type boolType) {
		Map<Type,Type> bindings = bind(boolType);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "bool", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitConstructor(Type type) {
		Type argumentTypes = type.getFieldTypes();
		IListWriter fields = vf.listWriter(fieldType);
		
		for (int i = 0; i < type.getArity(); i++) {
			IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
			IValue argLabel = vf.string(argumentTypes.getFieldName(i));
			fields.append(vf.tuple(argType, argLabel));
		}
		
		Type staticType = tf.constructor(store, cons, "constructor", tf.stringType(), "name", tf.listType(fieldType), "fields");

		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, vf.string(type.getName()), fields.done()), ctx);
	}

	public Result<IValue> visitExternal(Type externalType) {
		if (externalType instanceof FunctionType) {
			return visitFunction(externalType);
		}
		if (externalType instanceof NonTerminalType) {
			return visitNonTerminal(externalType);
		}
		if (externalType instanceof OverloadedFunctionType) {
			throw new NotYetImplemented("reification of overloaded function types");
		}
		
		throw new ImplementationError("unexpected type to reify: " + externalType);
	}

	private Result<IValue> visitNonTerminal(Type externalType) {
		NonTerminalType nt = (NonTerminalType) externalType;
		Map<Type,Type> bindings = bind(nt);
		Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "non-terminal", Factory.Symbol, "symbol");
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, nt.getSymbol()), ctx);
	}

	private Result<IValue> visitFunction(Type type) {
		Type argumentTypes = ((FunctionType) type).getArgumentTypes();
		IListWriter fields = vf.listWriter(fieldType);
		
		for (int i = 0; i < argumentTypes.getArity(); i++) {
			IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
			IValue argLabel = vf.string(argumentTypes.getFieldName(i));
			fields.append(vf.tuple(argType, argLabel));
		}
		
		IValue[] values = new IValue[argumentTypes.getArity() + 1];
		values[0] = ((FunctionType) type).getReturnType().accept(this).getValue();
		for (int j = 0, i = 1; i < values.length; i++, j++) {
			values[i] = argumentTypes.getFieldType(j).accept(this).getValue();
		}
		
		Type staticType = tf.constructor(store, cons, "fun", typeOfTypes, "return", tf.listType(fieldType), "fields");
		
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, values), ctx);
	}

	public Result<IValue> visitInteger(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "int", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitRational(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "rat", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitNumber(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "num", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}
	
	public Result<IValue> visitList(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> elem = type.getElementType().accept(this);
		TypeStore store = new TypeStore();
		store.declareAbstractDataType(typeOfTypes);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "list", elem.getType(), "element");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, elem.getValue()), ctx);
	}

	public Result<IValue> visitMap(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> key = type.getKeyType().accept(this);
		Result<IValue> value = type.getValueType().accept(this);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "map", key.getType(), "key", value.getType(), "value");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, key.getValue(), value.getValue()), ctx);
	}

	public Result<IValue> visitNode(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "node", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitParameter(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "parameter", tf.stringType(), "name", typeOfTypes, "bound");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, store, vf.string(type.getName()), type.getBound().accept(this).getValue()), ctx);
	}

	public Result<IValue> visitReal(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "real", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitRelationType(Type type) {
		Type argumentTypes = type.getFieldTypes();
		Map<Type,Type> bindings = bind(type);
		
		if (argumentTypes.hasFieldNames()) {
			IListWriter fields = vf.listWriter(fieldType);
			for (int i = 0; i < argumentTypes.getArity(); i++) {
				IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
				IValue argLabel = vf.string(argumentTypes.getFieldName(i));
				fields.append(vf.tuple(argType, argLabel));
			}
			
			Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "rel", tf.listType(fieldType), "fields");
			return makeResult(staticType.getAbstractDataType(), staticType.make(vf, fields.done()), ctx);
		}
		
		IListWriter fields = vf.listWriter(typeOfTypes);
		for (int i = 0; i < argumentTypes.getArity(); i++) {
			IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
			fields.append(argType);
		}
		
		Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "rel", tf.listType(typeOfTypes), "arguments");
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, fields.done()), ctx);
	}

	public Result<IValue> visitSet(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> elem = type.getElementType().accept(this);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "set", elem.getType(), "element");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, elem.getValue()), ctx);
	}

	public Result<IValue> visitSourceLocation(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "loc", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitString(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "str", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitTuple(Type type) {
		Type argumentTypes = type;
		Map<Type,Type> bindings = bind(type);
		
		if (argumentTypes.hasFieldNames()) {
			IListWriter fields = vf.listWriter(fieldType);
			for (int i = 0; i < argumentTypes.getArity(); i++) {
				IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
				IValue argLabel = vf.string(argumentTypes.getFieldName(i));
				fields.append(vf.tuple(argType, argLabel));
			}
			
			Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "tuple", tf.listType(fieldType), "fields");
			return makeResult(staticType.getAbstractDataType(), staticType.make(vf, fields.done()), ctx);
		}
		
		IListWriter fields = vf.listWriter(typeOfTypes);
		for (int i = 0; i < argumentTypes.getArity(); i++) {
			IValue argType = argumentTypes.getFieldType(i).accept(this).getValue();
			fields.append(argType);
		}
		
		Type staticType = tf.constructor(store, typeOfTypes.instantiate(bindings), "tuple", tf.listType(typeOfTypes), "arguments");
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, fields.done()), ctx);
	}

	public Result<IValue> visitValue(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "value", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitVoid(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "void", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitDateTime(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, typeOfTypes.instantiate(bindings), "datetime", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}
	
}
