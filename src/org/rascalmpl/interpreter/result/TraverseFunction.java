/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class TraverseFunction extends AbstractFunction {
	
	public TraverseFunction(IEvaluator<Result<IValue>> eval) {
		super(null, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(TypeFactory.getInstance().parameterType("T"), 
																					  TypeFactory.getInstance().tupleType(TypeFactory.getInstance().parameterType("T"))),
				false, null, eval.getCurrentEnvt());
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(actuals.length != 0) {
			if(self == null) 
				self = this;
			IValue arg = actuals[0];
			Type type = arg.getType();
			if(type.isAbstractDataType())
				arg = call((IConstructor) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isNodeType())
				arg = call((INode) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isListType())
				arg = call((IList) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isSetType())
				arg = call((ISet) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isMapType())
				arg = call((IMap) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isTupleType())
				arg = call((ITuple) arg, keyArgValues, self, selfParams, selfParamBounds);
			else if(type.isStringType())
				arg = call((IString) arg, keyArgValues, self, selfParams, selfParamBounds);
			return makeResult(actualTypes[0], arg, ctx);
		}
		return null;
	}
	
	private IConstructor call(IConstructor arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(arg.arity() != 0) {
			IValue args[] = new IValue[arg.arity()];
			for(int i = 0; i < arg.arity(); i++) {
				IValue child = arg.get(i);
				Result<IValue> result = self.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, self, selfParams, selfParamBounds);
				args[i] = result.getValue();
			}
			Type t = arg.getConstructorType();
			return ctx.getValueFactory().constructor(t, args);
		}
		return arg;
	}
	
	private INode call(INode arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(arg.arity() != 0) {
			IValue args[] = new IValue[arg.arity()];
			for(int i = 0; i < arg.arity(); i++) {
				IValue child = arg.get(i);
				Result<IValue> result = self.call(new Type[] { child.getType() }, new IValue[] { child }, keyArgValues, self, selfParams, selfParamBounds);
				args[i] = result.getValue();
			}
			return ctx.getValueFactory().node(arg.getName(), args);
		}
		return arg;
	}
	
	private IList call(IList arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(arg.length() != 0) {
			IListWriter w = arg.getType().writer(ctx.getValueFactory());
			for(int i = 0; i < arg.length(); i++) {
				Result<IValue> result = self.call(new Type[] { arg.get(i).getType() }, new IValue[] { arg.get(i) }, keyArgValues, self, selfParams, selfParamBounds);
				w.append(result.getValue());
			}
			return w.done();
		}
		return arg;
	}
	
	private ISet call(ISet arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(arg.size() != 0) {
			ISetWriter w = arg.getType().writer(ctx.getValueFactory());
			for(IValue elem : arg) {
				Result<IValue> result = self.call(new Type[] { elem.getType() }, new IValue[] { elem }, keyArgValues, self, selfParams, selfParamBounds);
				w.insert(result.getValue());
			}
			return w.done();
		}
		return arg;
	}
	
	private IMap call(IMap arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(!arg.isEmpty()) {
			IMapWriter w = arg.getType().writer(ctx.getValueFactory());
			Iterator<Entry<IValue, IValue>> iter = arg.entryIterator();
			while(iter.hasNext()) {
				Entry<IValue, IValue> elem = iter.next();
				Result<IValue> keyResult = self.call(new Type[] { elem.getKey().getType() }, new IValue[] { elem.getKey() }, keyArgValues, self, selfParams, selfParamBounds);
				Result<IValue> valueResult = self.call(new Type[] { elem.getValue().getType() }, new IValue[] { elem.getValue() }, keyArgValues, self, selfParams, selfParamBounds);
				w.put(keyResult.getValue(), valueResult.getValue());
			}
			return w.done();
		}
		return arg;
	}

	private ITuple call(ITuple arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		if(arg.arity() != 0) {
			IValue args[] = new IValue[arg.arity()];
			for(int i = 0; i < arg.arity(); i++) {
				Result<IValue> result = self.call(new Type[] { arg.get(i).getType() }, new IValue[] { arg.get(i) }, keyArgValues, self, selfParams, selfParamBounds);
				args[i] = result.getValue();
			}
			return ctx.getValueFactory().tuple(args);
		}
		return arg;
	}
	
	private IString call(IString arg, Map<String, Result<IValue>> keyArgValues, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		return arg;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public boolean isDefault() {
		return false;
	}
	
}
