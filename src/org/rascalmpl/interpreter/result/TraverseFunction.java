/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;


public class TraverseFunction extends AbstractFunction {
	
	private FunctionType ftype;
	private Type type;
	private Result<IValue> algebra;
	private boolean isCatamorphism;
	private Map<Type, AbstractFunction> functions;
	
	
	private static TypeFactory TF = TypeFactory.getInstance();
	private static RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	
	
	private TraverseFunction(FunctionType ftype, Type type, Result<IValue> algebra, boolean isCatamorphism, Map<Type, AbstractFunction> functions, IEvaluator<Result<IValue>> eval) {
		super(null, eval, ftype, false, null, eval.getCurrentEnvt());
		this.ftype = ftype;
		this.type = type;
		this.algebra = algebra;
		this.isCatamorphism = isCatamorphism;
		this.functions = functions;
	}
	
	public static void generateTraverseFunctions(Set<Type> allTypes, Map<Type, Result<IValue>> algebra, boolean isCatamorphism, Map<Type, AbstractFunction> functions, IEvaluator<Result<IValue>> eval) {
		boolean isTP = algebra.isEmpty();
		for(Type type : allTypes)
			if(!isTP && isCatamorphism) {
				if(algebra.containsKey(type)) {
					Type returnType = null;
					if(algebra.get(type).getType() instanceof OverloadedFunctionType)
						returnType = ((OverloadedFunctionType) algebra.get(type).getType()).getReturnType();
					else returnType = ((FunctionType) algebra.get(type).getType()).getReturnType();
					functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(returnType, TF.tupleType(type)), 
											type, algebra.get(type), isCatamorphism, functions, eval));
				} else {
					functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(type, TF.tupleType(type)), 
											type, null, isCatamorphism, functions, eval));
				}
			} else if(!isTP && !isCatamorphism) {
				if(algebra.containsKey(type)) {
					FunctionType ftype = (FunctionType) algebra.get(type).getType();
					Type argType = ftype.getArgumentTypes().getFieldType(1);
					functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(type, TF.tupleType(argType)),
											type, algebra.get(type), isCatamorphism, functions, eval));
				} else {
					functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(type, TF.tupleType(type)),
											type, null, isCatamorphism, functions, eval));
				}
			} else {
				functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(type, TF.tupleType(type)),
										type, null, isCatamorphism, functions, eval));
			}
	}
		
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if (!(actualTypes.length == 1 && actuals.length == 1)) throw new MatchFailed();
		
		Type type = actualTypes[0];
		IValue arg = actuals[0];
		
		if(!arg.getType().isSubtypeOf(type)) throw new MatchFailed();
		
		if(self == null) self = this;
		
		Result<IValue> result = makeResult(actualTypes[0], arg, ctx);
		
		if(type.isAbstractDataType())
			result = call(type, (IConstructor) arg, keyArgValues, self, openFunctions);
		else if(type.isNodeType())
			result = call(type, (INode) arg, keyArgValues, self, openFunctions);
		else if(type.isListType())
			result = call(type, (IList) arg, keyArgValues, self, openFunctions);
		else if(type.isSetType())
			result = call(type, (ISet) arg, keyArgValues, self, openFunctions);
//		else if(type.isMapType())
//			result = call(type, (IMap) arg, keyArgValues, self, openFunctions);
		else if(type.isTupleType())
			result = call(type, (ITuple) arg, keyArgValues, self, openFunctions);
//		else if(type.isStringType())
//			result = call(type, (IString) arg, keyArgValues, self, openFunctions);
		return result;
	}
	
	private Result<IValue> call(Type type, IConstructor arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if(arg.arity() != 0) {
			
			// Finds the types of the visit functions to be applied to the children
			AbstractFunction[] visitFunctions = this.getVisitFunctions(type, arg);
			
			// Finds the declared types of the children
			Type childrenTypes = arg.getConstructorType().getFieldTypes();
			
			IValue args[] = new IValue[arg.arity()];
			Type targs[] = new Type[arg.arity()]; 
			
			for(int i = 0; i < arg.arity(); i++) {
				IValue child = arg.get(i);
				Result<IValue> result = visitFunctions[i].call(new Type[] { childrenTypes.getFieldType(i) }, 
															   new IValue[] { child }, keyArgValues);
				targs[i] = result.getType();
				args[i] = result.getValue();
			}
			Type resultConstrType = ctx.getCurrentEnvt().lookupFirstConstructor(arg.getConstructorType().getName(), TF.tupleType(targs));
			arg = ctx.getValueFactory().constructor(resultConstrType, args);
			return makeResult(arg.getType(), arg, ctx);
		}
		return makeResult(type, arg, ctx);
	}
	
	// TODO: Have to think of the node functor
	private Result<IValue> call(Type type, INode arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if(arg.arity() != 0) {
			IValue args[] = new IValue[arg.arity()];
			Type targs[] = new Type[arg.arity()];
			
			for(int i = 0; i < arg.arity(); i++) {
				IValue child = arg.get(i);
				Result<IValue> result = this.functions.get(TF.valueType())
						.call(new Type[] { TF.valueType() }, new IValue[] { child }, keyArgValues);
				
				args[i] = result.getValue();
				targs[i] = result.getType();
			}
			return makeResult(TF.nodeType(), ctx.getValueFactory().node(arg.getName(), args), ctx);
		}
		return makeResult(type, arg, ctx);
	}
	
	private Result<IValue> call(Type type, IList arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		boolean isCatamorphism = (this.algebra != null && this.isCatamorphism);
		boolean isAnamorphism = (this.algebra != null && !this.isCatamorphism);
		
		AbstractFunction[] visitFunctions = new AbstractFunction[2];
		visitFunctions[0] = this.functions.get(this.type.getElementType());
		visitFunctions[1] = this;
		
		IListWriter w = ctx.getValueFactory().listWriter();
		
		if(arg.length() != 0) {
			
			Type[] childrenTypes = new Type[2];
			
			// Have to distinguish between cata- and ana- morphisms
			if(isCatamorphism) {
				childrenTypes[0] = type.getElementType();
				childrenTypes[1] = type;
				
				Result<IValue> head = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { arg.get(0) }, keyArgValues);
				Result<IValue> tail = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { arg.delete(0) }, keyArgValues);
				w.append(ctx.getValueFactory().tuple(head.getValue(), tail.getValue()));
				
				// in case of catamorphism the result type is list[tuple[a,b]]
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			if(isAnamorphism) {
				// in case of anamorphism, type is a list[tuple[a, b]]
				childrenTypes[0] = type.getElementType().getFieldType(0);
				childrenTypes[1] = type.getElementType().getFieldType(1);
				
				ITuple tuple = (ITuple) arg.get(0);
				Result<IValue> elem0 = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { tuple.get(0) }, keyArgValues);
				Result<IValue> elem1 = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { tuple.get(1) }, keyArgValues);
				w.append(elem0.getValue());
				w.appendAll((IList) elem1.getValue());
				
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			for(int i = 0; i < arg.length(); i++) {
				Result<IValue> result = visitFunctions[0].call(new Type[] { type.getElementType() }, new IValue[] { arg.get(i) }, keyArgValues);
				w.append(result.getValue());
			}
			arg = w.done();
			return makeResult(arg.getType(), arg, ctx);
		}
		arg = w.done();
		if(isCatamorphism) 
			type = TF.listType(TF.tupleType(visitFunctions[0].getReturnType(), visitFunctions[1].getReturnType()));
		if(isAnamorphism) 
			type = TF.listType(visitFunctions[1].getReturnType());
		return makeResult(type, arg, ctx);
	}
	
	private Result<IValue> call(Type type, ISet arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		boolean isCatamorphism = (this.algebra != null && this.isCatamorphism);
		boolean isAnamorphism = (this.algebra != null && !this.isCatamorphism);
		
		AbstractFunction[] visitFunctions = new AbstractFunction[2];
		visitFunctions[0] = this.functions.get(this.type.getElementType());
		visitFunctions[1] = this;
		
		ISetWriter w = ctx.getValueFactory().setWriter();
		
		if(arg.size() != 0) {
			
			Type[] childrenTypes = new Type[2];
			
			// Have to distinguish between cata- and ana- morphisms
			if(isCatamorphism) {
				childrenTypes[0] = type.getElementType();
				childrenTypes[1] = type;
				
				Iterator<IValue> iter = arg.iterator();
				IValue elem0 = iter.next();
				ISet rest = arg.delete(elem0);
				Result<IValue> head = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { elem0 }, keyArgValues);
				Result<IValue> tail = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { rest }, keyArgValues);
				w.insert(ctx.getValueFactory().tuple(head.getValue(), tail.getValue()));
				
				// in case of catamorphism the result type is set[tuple[a,b]]
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			if(isAnamorphism) {
				// in case of anamorphism, type is a set[tuple[a, b]]
				childrenTypes[0] = type.getElementType().getFieldType(0);
				childrenTypes[1] = type.getElementType().getFieldType(1);
				
				ITuple tuple = (ITuple) arg.iterator().next();
				Result<IValue> elem0 = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { tuple.get(0) }, keyArgValues);
				Result<IValue> elem1 = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { tuple.get(1) }, keyArgValues);
				w.insert(elem0.getValue());
				w.insertAll((ISet) elem1.getValue());
				
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			Iterator<IValue> iter = arg.iterator();
			for(int i = 0; i < arg.size(); i++) {
				Result<IValue> result = visitFunctions[0].call(new Type[] { type.getElementType() }, new IValue[] { iter.next() }, keyArgValues);
				w.insert(result.getValue());
			}
			arg = w.done();
			return makeResult(arg.getType(), arg, ctx);
		}
		arg = w.done();
		if(isCatamorphism) 
			type = TF.listType(TF.tupleType(visitFunctions[0].getReturnType(), visitFunctions[1].getReturnType()));
		if(isAnamorphism) 
			type = TF.listType(visitFunctions[1].getReturnType());
		return makeResult(type, arg, ctx);

		
	}
//	
//	private Result<IValue> call(IMap arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		AbstractFunction keyFunc = functions.get(getFunctionName(type.getKeyType()));
//		AbstractFunction valueFunc = functions.get(getFunctionName(type.getValueType()));
//		if(!arg.isEmpty()) {
//			IMapWriter w = arg.getType().writer(ctx.getValueFactory());
//			Iterator<Entry<IValue, IValue>> iter = arg.entryIterator();
//			while(iter.hasNext()) {
//				Entry<IValue, IValue> elem = iter.next();
//				Result<IValue> keyResult = keyFunc.call(new Type[] { elem.getKey().getType() }, new IValue[] { elem.getKey() }, keyArgValues, null, null);
//				Result<IValue> valueResult = valueFunc.call(new Type[] { elem.getValue().getType() }, new IValue[] { elem.getValue() }, keyArgValues, null, null);
//				w.put(keyResult.getValue(), valueResult.getValue());
//			}
//			arg = w.done();
//			return makeResult(arg.getType(), arg, ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
//
	private Result<IValue> call(Type type, ITuple arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		AbstractFunction[] visitFunctions = new AbstractFunction[this.type.getArity()];
		Type[] childrenTypes = new Type[type.getArity()];
		
		for(int i = 0; i < this.type.getArity(); i++)
			visitFunctions[i] = this.functions.get(this.type.getFieldType(i));
		
		for(int i = 0; i < type.getArity(); i++)
			childrenTypes[i] = type.getFieldType(i);
		
		if(arg.arity() != 0) {
			Type targs[] = new Type[type.getArity()];
			IValue args[] = new IValue[arg.arity()];
			for(int i = 0; i < arg.arity(); i++) {
				Result<IValue> result = visitFunctions[i].call(new Type[] { childrenTypes[0] }, new IValue[] { arg.get(i) }, keyArgValues, null, null);
				targs[i] = result.getType();
				args[i] = result.getValue();
			}
			arg =  ctx.getValueFactory().tuple(args);
			return makeResult(arg.getType(), arg, ctx);
		}
		return null;
	}
//	
//	private Result<IValue> call(IString arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		return makeResult(arg.getType(), arg, ctx);
//	}
//
	
	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public boolean isDefault() {
		return false;
	}
	
	@Override 
	public Type getType() {
		return super.getType();
	}
	
	private AbstractFunction[] getVisitFunctions(Type type, IConstructor arg) {
		Type constructorType = arg.getConstructorType();
		
		if(this.type.equals(type)) {
			// then type-preserving or catamorphic transformation
			int arity = constructorType.getFieldTypes().getArity();
			AbstractFunction[] functions = new AbstractFunction[arity];
			for(int i = 0; i < arity; i++)
				functions[i] = this.functions.get(constructorType.getFieldTypes().getFieldType(i));
			return functions;
		}
		
		Type adt = ctx.getCurrentEnvt().lookupAbstractDataType(this.type.getName()); // adt declaration
		Type functor = ctx.getCurrentEnvt().lookupAbstractDataType(type.getName()); // functor declaration
		
		if(org.rascalmpl.interpreter.env.IsomorphicTypes.isIsomorphic(adt, functor)) {
			// then anamorphic transformation
			
			// Type arguments instantiating the adt to the this.type
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			for(int i = 0; i < adt.getTypeParameters().getArity(); i++)
				bindings.put(adt.getTypeParameters().getFieldType(i), this.type.getTypeParameters().getFieldType(i));
			bindings.putAll(org.rascalmpl.interpreter.env.IsomorphicTypes.getReverseParameterization(functor));
			
			// Type arguments instantiating the functor to the type
			Map<Type, Type> bindings0 = new HashMap<Type, Type>();
			for(int i = 0; i < functor.getTypeParameters().getArity(); i++)
				bindings0.put(functor.getTypeParameters().getFieldType(i), type.getTypeParameters().getFieldType(i));
			
			Set<Type> constructors = ctx.getCurrentEnvt().lookupConstructor(functor, constructorType.getName());
			Type theConstructor = null;
			for(Type constructor : constructors)
				if(constructorType.equals(constructor.instantiate(bindings0)))
						theConstructor = constructor;
			assert(theConstructor != null);
			AbstractFunction[] functions = new AbstractFunction[theConstructor.getArity()];
			for(int i = 0; i < theConstructor.getArity(); i++) {
				if(theConstructor.getFieldTypes().getFieldTypes().isParameterType())
					functions[i] = this.functions.get(bindings.get(theConstructor.getFieldTypes().getFieldType(i)));
				else functions[i] = this.functions.get(theConstructor.getFieldTypes().getFieldType(i));
			}
			return functions;
		}
		
		return null;
	}
	
//	@Override
//	public String getName() {
//		return getFunctionName(this.type);
//	}
//	
//	public static String getFunctionName(Type type) {
//		return new String("visit" + type.toString());
//	}
	
}
