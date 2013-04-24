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

import static org.rascalmpl.interpreter.env.IsomorphicTypes.isIsomorphic;
import static org.rascalmpl.interpreter.env.IsomorphicTypes.getReverseParameterizationOfTypes;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;


public class TraverseFunction extends AbstractFunction {
	
	private FunctionType ftype;
	
	private AbstractFunction phi;
	private Type isomorphicAdt;
	
	// Open recursive visit functions in a fvisit pack
	private Map<Type, AbstractFunction> functions;
	
	private static TypeFactory TF = TypeFactory.getInstance();
	private static RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	
	
	private TraverseFunction(FunctionType ftype, Type type, Result<IValue> phi, Map<Type, AbstractFunction> functions, IEvaluator<Result<IValue>> eval) {
		super(null, eval, ftype, false, null, eval.getCurrentEnvt());
		this.ftype = ftype;
		this.isomorphicAdt = type;
		this.functions = functions;
	}
	
	public static void generateTraverseFunctions(Set<Type> allTypes, Map<Type, AbstractFunction> phis, Map<Type, AbstractFunction> functions, IEvaluator<Result<IValue>> eval) {
		Set<Type> typeConstrs = new HashSet<Type>();
		Map<Type, Type> typeMapping = new HashMap<Type,Type>();
		for(Type type : allTypes) {
			if(phis.containsKey(type)) {
				AbstractFunction phi = phis.get(type);
				if(phi.isAna())
					typeMapping.put(type, phi.getTraverseFunctionType().getArgumentTypes().getFieldType(0));
				else if(phi.isCata())
					typeMapping.put(type, phi.getTraverseFunctionType().getReturnType());
				functions.put(type, new TraverseFunction(phi.getTraverseFunctionType(), type, phi, functions, eval));
			} else {
				if(!type.isParameterized())
					functions.put(type, new TraverseFunction((FunctionType) RTF.functionType(type, TF.tupleType(type)), type, null, functions, eval));
				else typeConstrs.add(type);
			}
		}
		// Deal with type constructors
		for(Type typeConstr : typeConstrs) {
			Type tparams = typeConstr.getTypeParameters();
			Type[] toTparams = new Type[tparams.getArity()]; 
			for(int i = 0; i < tparams.getArity(); i++)
				toTparams[i] = typeMapping.get(tparams.getFieldType(i));
			FunctionType visitType = (FunctionType) RTF.functionType(eval.getCurrentEnvt().abstractDataType(typeConstr.getName(), toTparams), typeConstr);
			functions.put(typeConstr, new TraverseFunction(visitType, typeConstr, null, functions, eval));
		}
	}
	
//	private static void checkPhis(Map<Type, AbstractFunction> phis, Map<Type, Type> typeMapping) {
//		for(Entry<Type, AbstractFunction> entry : phis.entrySet()) {
//			AbstractFunction phi = entry.getValue();
//			FunctionType tphi = (FunctionType) phi.getType();
//			if(phi.isAna()) {
//				;
//			} else if(phi.isCata()) {
//				;
//			}
//		}
//	}
		
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if (!(actualTypes.length == 1 
				&& actuals.length == 1)) throw new MatchFailed();	
		
		Type argType = actualTypes[0];
		IValue arg = actuals[0];
		
		if(!arg.getType().isSubtypeOf(argType)) throw new MatchFailed();
		
		if(self == null) self = this;
		
		Result<IValue> result = makeResult(argType, arg, ctx);
		
		// Anamorphic transformation is top-down
		if(this.phi != null && this.phi.isAna()) {
			if(phi.getFunctionType().getArgumentTypes().getArity() != 2) {
				result = this.phi.call(new Type[] { argType }, new IValue[] { arg }, keyArgValues);
			} else {
				Result<IValue> type = new TypeReifier(ctx.getValueFactory()).typeToValue(this.isomorphicAdt, ctx);
				result = this.phi.call(new Type[] { argType, type.getType() }, new IValue[] { arg, type.getValue() }, keyArgValues);
			}
			argType = result.getType();
			arg = result.getValue();
		}
		
		if(argType.isAbstractDataType())
			result = call(argType, (IConstructor) arg, keyArgValues, self, openFunctions);
		else if(argType.isNodeType())
			result = call(argType, (INode) arg, keyArgValues, self, openFunctions);
		else if(argType.isListType())
			result = call(argType, (IList) arg, keyArgValues, self, openFunctions);
		else if(argType.isSetType())
			result = call(argType, (ISet) arg, keyArgValues, self, openFunctions);
//		else if(type.isMapType())
//			result = call(argType, (IMap) arg, keyArgValues, self, openFunctions);
		else if(argType.isTupleType())
			result = call(argType, (ITuple) arg, keyArgValues, self, openFunctions);
//		else if(argType.isStringType())
//			result = call(argType, (IString) arg, keyArgValues, self, openFunctions);
		
		// Catamorphic transformation is bottom-up
		if(this.phi != null && this.phi.isCata()) {
			if(phi.getFunctionType().getArgumentTypes().getArity() != 2) {
				result = this.phi.call(new Type[] { result.getType() }, new IValue[] { result.getValue() }, keyArgValues);
			} else {
				Result<IValue> type = new TypeReifier(ctx.getValueFactory()).typeToValue(this.isomorphicAdt, ctx);
				result = this.phi.call(new Type[] { result.getType(), type.getType() }, new IValue[] { result.getValue(), type.getValue() }, keyArgValues);
			}
			
		}
		
		return result;
	}
	
	private Result<IValue> call(Type type, IConstructor arg, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		if(arg.arity() != 0) {	
			for(int i = 1; i < arg.arity(); i++) { 
				 
			}
			// Finds the types of the visit functions to be applied to the children
			AbstractFunction[] visitFunctions = this.getVisitFunctions(type, arg);
			// Finds the declared types of the children
			Type childrenTypes = arg.getConstructorType().getFieldTypes();
			
			Type targs[] = new Type[arg.arity()]; 
			IValue args[] = new IValue[arg.arity()];
			for(int i = 0; i < arg.arity(); i++) {
				IValue child = arg.get(i);
				Result<IValue> result = visitFunctions[i].call(new Type[] { childrenTypes.getFieldType(i) }, 
															   new IValue[] { child }, keyArgValues);
				targs[i] = result.getType();
				args[i] = result.getValue();
			}
			
			Type adt = arg.getConstructorType().getAbstractDataType();
			if(this.phi != null && this.phi.isCata()) 
				adt = ctx.getCurrentEnvt().lookupAbstractDataType(((FunctionType) this.phi.getType()).getArgumentTypes().getFieldType(0).getName());
			if(this.phi != null && this.phi.isAna()) 
				adt = ctx.getCurrentEnvt().lookupAbstractDataType(this.isomorphicAdt.getName());
			
			Type resultConstrType = ctx.getCurrentEnvt().lookupConstructor(adt, arg.getConstructorType().getName(), TF.tupleType(targs));
			arg = ctx.getValueFactory().constructor(resultConstrType, args);
			return makeResult(arg.getType(), arg, ctx);
		}
		return makeResult(type, arg, ctx);
	}
	
	// TODO: Have to think of the node functor
	private Result<IValue> call(Type type, INode arg, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
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
	
	private Result<IValue> call(Type type, IList arg, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {

		AbstractFunction[] visitFunctions = new AbstractFunction[2];
		visitFunctions[0] = this.functions.get(this.isomorphicAdt.getElementType());
		visitFunctions[1] = this;
		
		IListWriter w = ctx.getValueFactory().listWriter();
		
		if(arg.length() != 0) {
			
			Type[] childrenTypes = new Type[2];
			
			if(this.phi != null && this.phi.isCata()) {
				// in case of catamorphism, e.g., list[Expr] -> list[tuple[str,int]], Expr -> str, list[Expr] -> int
				childrenTypes[0] = type.getElementType();
				childrenTypes[1] = type;
				
				Result<IValue> head = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { arg.get(0) }, keyArgValues);
				Result<IValue> tail = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { arg.delete(0) }, keyArgValues);
				
				w.append(ctx.getValueFactory().tuple(head.getValue(), tail.getValue()));
				
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			if(this.phi != null && this.phi.isAna()) {
				// in case of anamorphism, e.g., list[tuple[str, int]] -> list[Expr], str -> Expr, int -> list[Expr]
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
		if(this.phi != null && this.phi.isCata()) 
			type = TF.listType(TF.tupleType(visitFunctions[0].getReturnType(), visitFunctions[1].getReturnType()));
		if(this.phi != null && this.phi.isAna()) 
			type = visitFunctions[1].getReturnType();
		return makeResult(type, arg, ctx);
	}
	
	private Result<IValue> call(Type type, ISet arg, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {

		AbstractFunction[] visitFunctions = new AbstractFunction[2];
		visitFunctions[0] = this.functions.get(this.isomorphicAdt.getElementType());
		visitFunctions[1] = this;
		
		ISetWriter w = ctx.getValueFactory().setWriter();
		
		if(arg.size() != 0) {
			
			Type[] childrenTypes = new Type[2];
			
			if(this.phi != null && this.phi.isCata()) {
				// in case of catamorphism, e.g., set[Expr] -> set[tuple[str,int]], Expr -> str, set[Expr] -> int
				childrenTypes[0] = type.getElementType();
				childrenTypes[1] = type;
				
				Iterator<IValue> iter = arg.iterator();
				
				IValue elem0 = iter.next();
				ISet rest = arg.delete(elem0);
				
				Result<IValue> head = visitFunctions[0].call(new Type[] { childrenTypes[0] }, new IValue[] { elem0 }, keyArgValues);
				Result<IValue> tail = visitFunctions[1].call(new Type[] { childrenTypes[1] }, new IValue[] { rest }, keyArgValues);
				
				w.insert(ctx.getValueFactory().tuple(head.getValue(), tail.getValue()));
				
				arg = w.done();
				return makeResult(arg.getType(), arg, ctx);
			}
			if(this.phi != null && this.phi.isAna()) {
				// in case of anamorphism, e.g., set[tuple[str, int]] -> set[Expr], str -> Expr, int -> set[Expr]
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
		if(this.phi != null && this.phi.isCata()) 
			type = TF.setType(TF.tupleType(visitFunctions[0].getReturnType(), visitFunctions[1].getReturnType()));
		if(this.phi != null && this.phi.isAna()) 
			type = visitFunctions[1].getReturnType();
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
	private Result<IValue> call(Type type, ITuple arg, Map<String, IValue> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
		AbstractFunction[] visitFunctions = new AbstractFunction[this.isomorphicAdt.getArity()];
		Type[] childrenTypes = new Type[type.getArity()];
		
		for(int i = 0; i < this.isomorphicAdt.getArity(); i++)
			visitFunctions[i] = this.functions.get(this.isomorphicAdt.getFieldType(i));
		
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
		
	private AbstractFunction[] getVisitFunctions(Type type, IConstructor arg) {
		Type constructorType = arg.getConstructorType();
		// 'type' can be an adt ('type == this.type') or its functor  
		if(this.isomorphicAdt.equals(type)) {
			// then catamorphic or type-preserving
			int arity = constructorType.getFieldTypes().getArity();
			AbstractFunction[] functions = new AbstractFunction[arity];
			for(int i = 0; i < arity; i++)
				functions[i] = this.functions.get(constructorType.getFieldTypes().getFieldType(i));
			return functions;
		}	
		Type adt = ctx.getCurrentEnvt().lookupAbstractDataType(this.isomorphicAdt.getName()); // adt declaration
		Type functor = ctx.getCurrentEnvt().lookupAbstractDataType(type.getName()); // functor declaration		
		if(isIsomorphic(adt, functor)) {
			// then anamorphic transformation	
			// Type arguments instantiating the functor to 'this.type'
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			for(int i = 0; i < adt.getTypeParameters().getArity(); i++)
				bindings.put(adt.getTypeParameters().getFieldType(i), this.isomorphicAdt.getTypeParameters().getFieldType(i));
			bindings.putAll(getReverseParameterizationOfTypes(functor));
			
			// Type arguments instantiating the functor to 'type'
			Map<Type, Type> bindings0 = new HashMap<Type, Type>();
			for(int i = 0; i < functor.getTypeParameters().getArity(); i++)
				bindings0.put(functor.getTypeParameters().getFieldType(i), type.getTypeParameters().getFieldType(i));
			
			Set<Type> constructors = ctx.getCurrentEnvt().lookupConstructor(functor, constructorType.getName());
			Type theConstructor = null;
			for(Type constructor : constructors) {
				if(constructorType.getAbstractDataType().isSubtypeOf(constructor.instantiate(bindings0).getAbstractDataType()))
						theConstructor = constructor;
			}
			assert(theConstructor != null);
			AbstractFunction[] functions = new AbstractFunction[theConstructor.getArity()];
			for(int i = 0; i < theConstructor.getArity(); i++) {
				if(theConstructor.getFieldTypes().getFieldType(i).isParameterType())
					functions[i] = this.functions.get(bindings.get(theConstructor.getFieldTypes().getFieldType(i)));
				else functions[i] = this.functions.get(theConstructor.getFieldTypes().getFieldType(i));
			}
			return functions;
		}
		return null;
	}
	
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
		return this.ftype;
	}
		
}
