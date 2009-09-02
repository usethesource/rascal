package org.meta_environment.rascal.interpreter;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

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
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.rascal.interpreter.types.OverloadedFunctionType;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;
import org.meta_environment.uptr.Factory;

/**
 * TypeReifier is a visitor that maps types to values that represent these types. These values have
 * very specific types, namely 'type[&T]' where &T is bound to the type the value represents. 
 * <br>
 * The 'type[&T]' is rank-2 polymorphic because &T can be bound differently on different nesting levels
 * of type representations. Therefore it can not be declared or represented in Rascal itself.
 * <br>
 * However, see the Type module in the standard library for a sketch of the kind of values it generates. 
 * 
 */
public class TypeReifier implements ITypeVisitor<Result<IValue>> {
	private final IEvaluatorContext ctx;
	private final Type adt;
	private final TypeFactory tf;
	private final Environment env;
	private final IValueFactory vf;
	private final Type param;
	private final TypeStore store;
	private final Type cons;
	private final Type valueAdt;
	private final Type listAdt;
	private final Type listCons;
	
	private Set<IValue> visiting = new HashSet<IValue>();
	
	public TypeReifier(IEvaluatorContext ctx) {
		this.ctx = ctx;
		this.env = ctx.getCurrentEnvt();
		this.store = env.getStore();
		this.tf = TypeFactory.getInstance();
		this.vf = ValueFactoryFactory.getValueFactory();
		this.param = tf.parameterType("T");
		this.adt = RascalTypeFactory.getInstance().reifiedType(param);
		this.cons = env.abstractDataType("constructor");

		store.declareAbstractDataType(adt);
		
	    Map<Type,Type> bindings = bind(tf.valueType());
	    this.valueAdt = adt.instantiate(store, bindings);
	    this.listAdt = tf.listType(valueAdt);
	    this.listCons = tf.listType(cons);
	    
	}

	/**
	 * Collects all constructor of the ADT, the builts the rather complex reified representation.
	 */
	public Result<IValue> visitAbstractData(Type type) {
		Map<Type,Type> bindings = bind(type);
		String name = type.getName();
		
		Type params = type.getTypeParameters();
		Type staticType;
		IValue result;
		IValue stub;
		
		if (params.isVoidType()) {
			staticType = tf.constructor(store, adt.instantiate(store, bindings), "adt", tf.stringType(), "name");
			stub = staticType.make(vf, vf.string(name));
		}
		else {
			staticType = tf.constructor(store, adt.instantiate(store, bindings), "adt", tf.stringType(), "name", listAdt, "parameters");
			stub = staticType.make(vf, vf.string(name), getTypeParameterList(params));
		}
		
		if (visiting.contains(stub)) {
			// we break an infinite recursion here
			return makeResult(staticType, stub, ctx);
		}
		
		visiting.add(stub);
		
		IListWriter constructorListW = vf.listWriter(cons);
		for (Type alt : store.lookupAlternatives(type)) {
			constructorListW.append(alt.accept(this).getValue());
		}
		IList constructorList = constructorListW.done();
		
		visiting.remove(stub);
		
		if (params.isVoidType()) {
			staticType = tf.constructor(store, adt.instantiate(store, bindings), "adt", tf.stringType(), "name", listCons, "constructors");
			result = staticType.make(vf, vf.string(name), constructorList);
		}
		else {
			staticType = tf.constructor(store, adt.instantiate(store, bindings), "adt", tf.stringType(), "name", listAdt, "parameters", listCons, "constructors");
			result = staticType.make(vf, name, getTypeParameterList(params), constructorList);
		}
		
		return makeResult(staticType.getAbstractDataType(), result, ctx);
	}

	private IList getTypeParameterList(Type params) {
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
		
		if (params.isVoidType()) {
			Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "alias", tf.stringType(), "name", aliased.getType(), "aliased");
			return makeResult(staticType.getAbstractDataType(), staticType.make(vf, vf.string(name), aliased.getValue()), ctx);
		}
		else {
			Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "alias", tf.stringType(), "name", tf.listType(valueAdt), "parameters", aliased.getType(), "aliased");
			return makeResult(staticType.getAbstractDataType(), staticType.make(vf, vf.string(name), getTypeParameterList(params), aliased.getValue()), ctx);
		}
	}

	public Result<IValue> visitBool(Type boolType) {
		Map<Type,Type> bindings = bind(boolType);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "bool", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitConstructor(Type type) {
		Type argumentTypes = type.getFieldTypes();
		Object[] fields = new Object[2 + argumentTypes.getArity() * 2];
		
		fields[0] = tf.stringType();
		fields[1] = "name";
		for (int i = 0, j = 2; j < fields.length; j+=2, i++) {
			fields[j] = argumentTypes.getFieldType(i);
			String fieldName = argumentTypes.getFieldName(i);
			
			if (fieldName != null) {
				fields[j+1] = fieldName;
			}
			else {
				fields[j+1] = "field" + i;
			}
		}
		
		IValue[] values = new IValue[argumentTypes.getArity() + 1];
		values[0] = vf.string(type.getName());
		for (int j = 0, i = 1; i < values.length; i++, j++) {
			values[i] = argumentTypes.getFieldType(j).accept(this).getValue();
		}
		
		Type staticType = tf.constructor(store, cons, "constructor", fields);
		
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, values), ctx);
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
		Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "non-terminal", Factory.Symbol, "symbol");
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, nt.getSymbol()), ctx);
	}

	private Result<IValue> visitFunction(Type externalType) {
		FunctionType func = (FunctionType) externalType;
		Type argumentTypes = func.getArgumentTypes();
		Result<IValue> returnTypeValue = func.getReturnType().accept(this);
		Map<Type,Type> bindings = bind(func);
		
		Object[] fields = new Object[2 + argumentTypes.getArity() * 2];
		
		fields[0] = returnTypeValue.getType();
		fields[1] = "return";
		for (int i = 0, j = 2; j < fields.length; j+=2, i++) {
			fields[j] = argumentTypes.getFieldType(i);
			fields[j+1] = argumentTypes.getFieldName(i);
		}
		
		IValue[] values = new IValue[argumentTypes.getArity()];
		for (int i = 0; i < values.length; i++) {
			values[i] = argumentTypes.getFieldType(i).accept(this).getValue();
		}
		
		Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "func", fields);
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, values), ctx);
	}

	public Result<IValue> visitInteger(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "int", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitList(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> elem = type.getElementType().accept(this);
		TypeStore store = new TypeStore();
		store.declareAbstractDataType(adt);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "list", elem.getType(), "element");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, elem.getValue()), ctx);
	}

	public Result<IValue> visitMap(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> key = type.getKeyType().accept(this);
		Result<IValue> value = type.getValueType().accept(this);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "map", key.getType(), "key", value.getType(), "value");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, key.getValue(), value.getValue()), ctx);
	}

	public Result<IValue> visitNode(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "node", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitParameter(Type parameterType) {
		throw new ImplementationError("all parameter types should have been instantiated by now");
	}

	public Result<IValue> visitReal(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "real", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitRelationType(Type type) {
		Type argumentTypes = type.getFieldTypes();
		Map<Type,Type> bindings = bind(type);
		
		Object[] fields = new Object[argumentTypes.getArity() * 2];
		
		for (int i = 0, j = 0; j < fields.length; j+=2, i++) {
			fields[j] = argumentTypes.getFieldType(i);
			String fieldName = argumentTypes.getFieldName(i);
			
			if (fieldName != null) {
				fields[j+1] = fieldName;
			}
			else {
				fields[j+1] = "field" + i;
			}
		}
		
		IValue[] values = new IValue[argumentTypes.getArity()];
		for (int i = 0; i < values.length; i++) {
			values[i] = argumentTypes.getFieldType(i).accept(this).getValue();
		}
		
		Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "rel", fields);
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, values), ctx);
	}

	public Result<IValue> visitSet(Type type) {
		Map<Type,Type> bindings = bind(type);
		Result<IValue> elem = type.getElementType().accept(this);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "set", elem.getType(), "element");
		return makeResult(cons.getAbstractDataType(), cons.make(vf, elem.getValue()), ctx);
	}

	public Result<IValue> visitSourceLocation(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "loc", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitString(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "str", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitTuple(Type type) {
		Map<Type,Type> bindings = bind(type);
		
		Object[] fields = new Object[type.getArity() * 2];
		
		for (int i = 0, j = 0; j < fields.length; j+=2, i++) {
			fields[j] = type.getFieldType(i);
			fields[j+1] = type.getFieldName(i);
		}
		
		IValue[] values = new IValue[type.getArity()];
		for (int i = 0; i < values.length; i++) {
			values[i] = type.getFieldType(i).accept(this).getValue();
		}
		
		Type staticType = tf.constructor(store, adt.instantiate(store, bindings), "tuple", fields);
		return makeResult(staticType.getAbstractDataType(), staticType.make(vf, values), ctx);
	}

	public Result<IValue> visitValue(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "value", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

	public Result<IValue> visitVoid(Type type) {
		Map<Type,Type> bindings = bind(type);
		Type cons = tf.constructor(store, adt.instantiate(store, bindings), "void", tf.tupleEmpty());
		return makeResult(cons.getAbstractDataType(), cons.make(vf), ctx);
	}

}
