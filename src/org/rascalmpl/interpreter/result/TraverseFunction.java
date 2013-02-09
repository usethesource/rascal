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
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;


public class TraverseFunction extends AbstractFunction {
	
	private Type type;
	private Result<IValue> algebra;
	
	private Map<Type, AbstractFunction> recursiveFunctions = new HashMap<Type, AbstractFunction>();
	
	public TraverseFunction(Type type, Result<IValue> algebra, IEvaluator<Result<IValue>> eval) {
		super(null, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(type, TypeFactory.getInstance().tupleType(type)),
				false, null, eval.getCurrentEnvt());
		this.type = type;
		this.algebra = algebra;
	}
	
	public static void generateTraverseFunctions(Set<Type> allTypes, Map<Type, Result<IValue>> algebra, boolean isCatamorphism, Map<Type, AbstractFunction> functions, IEvaluator<Result<IValue>> eval) {
		boolean isTP = algebra.isEmpty();
		for(Type type : allTypes)
			if(!isTP && isCatamorphism) {
				if(algebra.containsKey(type)) {
					
				}
			} else if(!isTP && !isCatamorphism) {
				
			} else {
				
			}
			
	}
		
//	@Override
//	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		if (actualTypes.length != 1) throw new MatchFailed();
//		if(self == null) self = this;
//			
//		IValue arg = actuals[0];
//		Type type = arg.getType();
//		Result<IValue> result = makeResult(actualTypes[0], arg, ctx);
//		if(type.isAbstractDataType())
//			result = call((IConstructor) arg, keyArgValues, self, openFunctions);
//		else if(type.isNodeType())
//			result = call((INode) arg, keyArgValues, self, openFunctions);
//		else if(type.isListType())
//			result = call((IList) arg, keyArgValues, self, openFunctions);
//		else if(type.isSetType())
//			result = call((ISet) arg, keyArgValues, self, openFunctions);
//		else if(type.isMapType())
//			result = call((IMap) arg, keyArgValues, self, openFunctions);
//		else if(type.isTupleType())
//			result = call((ITuple) arg, keyArgValues, self, openFunctions);
//		else if(type.isStringType())
//			result = call((IString) arg, keyArgValues, self, openFunctions);
//		return result;
//	}
//	
//	private Result<IValue> call(IConstructor arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		Type constrParamTypes = arg.getConstructorType().getFieldTypes();
//		if(arg.arity() != 0) {
//			IValue args[] = new IValue[arg.arity()];
//			Type targs[] = new Type[arg.arity()]; 
//			for(int i = 0; i < arg.arity(); i++) {
//				IValue child = arg.get(i);
//				Result<IValue> result = null;
//				if(getFunctionName(this.type).equals(getFunctionName(constrParamTypes.getFieldType(i))))
//					result = self.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, null, null);
//				else
//					result = functions.get(getFunctionName(constrParamTypes.getFieldType(i)))
//								.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, null, null);
//				args[i] = result.getValue();
//				targs[i] = result.getType();
//			}
//			Type t = arg.getConstructorType();
//			if(algebra != null) {
//				IValue constr = ((IConstructor) algebra.getValue()).get(t.getName() + arg.getType().getName());
//				return makeResult(constr.getType(), constr, ctx).call(targs, args, null, null, null);
//			}
//			arg = ctx.getValueFactory().constructor(t, args);
//			return makeResult(arg.getType(), arg, ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
//	
//	private Result<IValue> call(INode arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		Type nodeParamTypes = arg.getType().getFieldTypes();
//		if(arg.arity() != 0) {
//			IValue args[] = new IValue[arg.arity()];
//			Type targs[] = new Type[arg.arity()];
//			for(int i = 0; i < arg.arity(); i++) {
//				IValue child = arg.get(i);
//				Result<IValue> result = null;
//				if(getFunctionName(this.type).equals(getFunctionName(nodeParamTypes.getFieldType(i))))
//					result = self.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, null, null);
//				else
//					result = functions.get(getFunctionName(nodeParamTypes.getFieldType(i)))
//								.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, null, null);
//				args[i] = result.getValue();
//				targs[i] = result.getType();
//			}
//			if(algebra != null) {
//				IValue constr = ((IConstructor) algebra.getValue()).get("node");
//				return makeResult(constr.getType(), constr, ctx).call(targs, args, null, null, null);
//			}
//			return makeResult(arg.getType(), ctx.getValueFactory().node(arg.getName(), args), ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
//	
//	private Result<IValue> call(IList arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		AbstractFunction func = functions.get(getFunctionName(type.getElementType()));
//		if(arg.length() != 0) {
//			IListWriter w = arg.getType().writer(ctx.getValueFactory());
//			for(int i = 0; i < arg.length(); i++) {
//				Result<IValue> result = func.call(new Type[] { arg.get(i).getType() }, new IValue[] { arg.get(i) }, keyArgValues, null, null);
//				w.append(result.getValue());
//			}
//			arg = w.done();
//			return makeResult(arg.getType(), arg, ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
//	
//	private Result<IValue> call(ISet arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		AbstractFunction func = functions.get(getFunctionName(type.getElementType()));
//		if(arg.size() != 0) {
//			ISetWriter w = arg.getType().writer(ctx.getValueFactory());
//			for(IValue elem : arg) {
//				Result<IValue> result = func.call(new Type[] { elem.getType() }, new IValue[] { elem }, keyArgValues, null, null);
//				w.insert(result.getValue());
//			}
//			arg = w.done();
//			return makeResult(arg.getType(), arg, ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
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
//	private Result<IValue> call(ITuple arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, Map<String, Result<IValue>> openFunctions) {
//		Type tupleTypes = arg.getType().getFieldTypes();
//		if(arg.arity() != 0) {
//			IValue args[] = new IValue[arg.arity()];
//			for(int i = 0; i < arg.arity(); i++) {
//				Result<IValue> result = functions.get(getFunctionName(tupleTypes.getFieldType(i)))
//						.call(new Type[] { arg.get(i).getType() }, new IValue[] { arg.get(i) }, keyArgValues, null, null);
//				args[i] = result.getValue();
//			}
//			arg =  ctx.getValueFactory().tuple(args);
//			return makeResult(arg.getType(), arg, ctx);
//		}
//		return makeResult(arg.getType(), arg, ctx);
//	}
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
	
//	@Override
//	public String getName() {
//		return getFunctionName(this.type);
//	}
//	
//	public static String getFunctionName(Type type) {
//		return new String("visit" + type.toString());
//	}
	
}
