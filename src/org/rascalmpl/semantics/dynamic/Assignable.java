/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.OptionalExpression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.AssignableEvaluator.AssignmentOperator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotation;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSlice;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscript;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;

public abstract class Assignable extends org.rascalmpl.ast.Assignable {

	static public class Annotation extends
			org.rascalmpl.ast.Assignable.Annotation {

		public Annotation(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Assignable __param2, Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			String label = org.rascalmpl.interpreter.utils.Names.name(this
					.getAnnotation());
			Result<IValue> result = this.getReceiver().interpret(
					(Evaluator) __eval.__getEval());

			if (result == null || result.getValue() == null) {
				throw new UninitializedVariable(label, this.getReceiver());
			}

			if (!__eval.__getEnv().declaresAnnotation(result.getType(), label)) {
				throw new UndeclaredAnnotation(label, result.getType(),
						this);
			}

			try {
				__eval.__setValue(__eval.newResult(result.getAnnotation(label,
						__eval.__getEnv()), __eval.__getValue()));
			} catch (Throw e) {
				// NoSuchAnnotation
			}
			return __eval.recur(this, result.setAnnotation(label, __eval
					.__getValue(), __eval.__getEnv()));
			// result.setValue(((IConstructor)
			// result.getValue()).setAnnotation(label, value.getValue()));
			// return recur(this, result);

		}

		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			return makeResult(TF.boolType(), getReceiver().interpret(__eval.getEvaluator()).has(getAnnotation()).getValue(), __eval.getEvaluator());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> receiver = this.getReceiver().interpret(__eval);
			String label = Names.name(this.getAnnotation());

			if (!__eval.getCurrentEnvt().declaresAnnotation(receiver.getType(),
					label)) {
				throw new UndeclaredAnnotation(label, receiver.getType(),
						this);
			}

			Type type = __eval.getCurrentEnvt().getAnnotationType(
					receiver.getType(), label);
			IValue value = ((IConstructor) receiver.getValue())
					.asAnnotatable().getAnnotation(label);

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					type, value, __eval);

		}

	}

	static public class Bracket extends org.rascalmpl.ast.Assignable.Bracket {

		public Bracket(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Assignable __param2) {
			super(__param1, tree, __param2);
		}

	}

	static public class Constructor extends
			org.rascalmpl.ast.Assignable.Constructor {

		public Constructor(ISourceLocation __param1, IConstructor tree, Name __param2,
				List<org.rascalmpl.ast.Assignable> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			Type valueType = __eval.__getValue().getType();

			if (!valueType.isNode() && !valueType.isAbstractData()
					&& !valueType.isConstructor()) {
				throw new UnexpectedType(
						org.rascalmpl.interpreter.AssignableEvaluator.__getTf()
								.nodeType(), __eval.__getValue().getType(),
						this);
			}

			IConstructor node = (IConstructor) __eval.__getValue().getValue();
			Type nodeType = node.getType();

			if (nodeType.isAbstractData()) {
				nodeType = ((IConstructor) __eval.__getValue().getValue())
						.getConstructorType();
			}

			if (!node.getName().equals(
					org.rascalmpl.interpreter.utils.Names.name(this.getName()))) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.nameMismatch(node.getName(),
								org.rascalmpl.interpreter.utils.Names.name(this
										.getName()), this.getName(), __eval
										.__getEval().getStackTrace());
			}

			List<org.rascalmpl.ast.Assignable> arguments = this.getArguments();

			if (node.arity() != arguments.size()) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.arityMismatch(node.arity(), arguments.size(), this,
								__eval.__getEval().getStackTrace());
			}

			IValue[] results = new IValue[arguments.size()];
			Type[] resultTypes = new Type[arguments.size()];

			for (int i = 0; i < arguments.size(); i++) {
				Type argType = !nodeType.isConstructor() ? org.rascalmpl.interpreter.AssignableEvaluator
						.__getTf().valueType()
						: nodeType.getFieldType(i);
				IValue arg = node.get(i);
				Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(argType, arg, __eval.__getEval());
				AssignableEvaluator ae = new AssignableEvaluator(__eval
						.__getEnv(), null, result, __eval.__getEval());
				Result<IValue> argResult = arguments.get(i).assignment(ae);
				results[i] = argResult.getValue();
				resultTypes[i] = argType;
			}
			
			if (!nodeType.isAbstractData() && !nodeType.isConstructor()) {
				// TODO: can this be the case?
				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(nodeType, __eval.__getEval()
								.getValueFactory().node(node.getName(), results),
								__eval.__getEval());
			}

			Type constructor = __eval.__getEval().getCurrentEnvt()
					.getConstructor(
							node.getName(),
							org.rascalmpl.interpreter.AssignableEvaluator
									.__getTf().tupleType(resultTypes));

			if (constructor == null) {
				throw new ImplementationError("could not find constructor for "
						+ node.getName()
						+ " : "
						+ org.rascalmpl.interpreter.AssignableEvaluator
								.__getTf().tupleType(resultTypes));
			}

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					constructor.getAbstractDataType(), __eval.__getEval().getValueFactory().constructor(constructor, results), __eval
							.__getEval());

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			throw new ImplementationError(
					"Constructor assignable does not represent a value");

		}

	}

	static public class FieldAccess extends
			org.rascalmpl.ast.Assignable.FieldAccess {

		public FieldAccess(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Assignable __param2, Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			Result<IValue> receiver = this.getReceiver().interpret(
					(Evaluator) __eval.__getEval());
			String label = org.rascalmpl.interpreter.utils.Names.name(this.getField());

			if (receiver == null || receiver.getValue() == null) {
				throw new UninitializedVariable(label, this.getReceiver());
			}

			if (receiver.getType().isTuple()) {

				int idx = receiver.getType().getFieldIndex(label);
				if (idx < 0) {
					throw new UndeclaredField(label, receiver.getType(), this);
				}

				__eval.__setValue(__eval.newResult(((ITuple) receiver
						.getValue()).get(idx), __eval.__getValue()));
				IValue result = ((ITuple) receiver.getValue()).set(idx, __eval
						.__getValue().getValue());
				return __eval.recur(this,
						org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(receiver.getType(), result, __eval
								.__getEval()));
			} 
			else if (receiver.getType() instanceof NonTerminalType) {
				Result<IValue> result = receiver.fieldUpdate(label, __eval.__getValue(), __eval.getCurrentEnvt().getStore());

				__eval.__setValue(__eval.newResult(receiver.fieldAccess(label, __eval.getCurrentEnvt().getStore()), __eval
						.__getValue()));

				if (!result.getType().isSubtypeOf(receiver.getType())) {
					throw new UnexpectedType(receiver.getType(), result.getType(), __eval.getCurrentAST());
				}
				return __eval.recur(this, result);
			}
			else if (receiver.getType().isConstructor()
					|| receiver.getType().isAbstractData()) {
				IConstructor cons = (IConstructor) receiver.getValue();
				Type node = cons.getConstructorType();
				Type kwType = __eval.getCurrentEnvt().getConstructorFunction(node).getKeywordArgumentTypes(__eval.getCurrentEnvt());

				if (node.hasField(label)) {
					int index = node.getFieldIndex(label);

					if (!__eval.__getValue().getType().isSubtypeOf(
							node.getFieldType(index))) {
						throw new UnexpectedType(node.getFieldType(index),
								__eval.__getValue().getType(), this);
					}
					__eval.__setValue(__eval.newResult(cons.get(index), __eval
							.__getValue()));

					IValue result = cons.set(index, __eval.__getValue().getValue());
					return __eval.recur(this,
							org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(receiver.getType(), result, __eval
									.__getEval()));
				}
				else if (kwType.hasField(label)) {
					if (!__eval.__getValue().getType().isSubtypeOf(
							kwType.getFieldType(label))) {
						throw new UnexpectedType(kwType.getFieldType(label),
								__eval.__getValue().getType(), this);
					}

					IValue paramValue = cons.asWithKeywordParameters().getParameter(label);
					if (paramValue == null) {
					    __eval.__getOperator();
                        if (__eval.__getOperator().name().equals(AssignmentOperator.IsDefined.name()) && __eval.__getValue() != null) {
					        paramValue = __eval.__getValue().getValue();
					    }
					    else { // we use the default
					        paramValue = receiver.fieldAccess(label, __eval.getCurrentEnvt().getStore()).getValue();
					    }
					}
					__eval.__setValue(__eval.newResult(paramValue, __eval.__getValue()));

					IValue result = cons.asWithKeywordParameters().setParameter(label,  __eval.__getValue().getValue());
					return __eval.recur(this,
							org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(receiver.getType(), result, __eval
									.__getEval()));
				}
				else {
					throw new UndeclaredField(label, receiver.getValue().getType(), this);
				}

			} else if (receiver.getType().isSourceLocation()) {
				// ISourceLocation loc = (ISourceLocation) receiver.getValue();

				__eval.__setValue(__eval.newResult(receiver.fieldAccess(label,
						__eval.__getEnv().getStore()), __eval.__getValue()));
				return __eval.recur(this, receiver.fieldUpdate(label, __eval
						.__getValue(), __eval.__getEnv().getStore()));
				// return recur(this, eval.sourceLocationFieldUpdate(loc, label,
				// value.getValue(), value.getType(), this));
			} else if (receiver.getType().isDateTime()) {
				__eval.__setValue(__eval.newResult(receiver.fieldAccess(label,
						__eval.__getEnv().getStore()), __eval.__getValue()));
				return __eval.recur(this, receiver.fieldUpdate(label, __eval
						.__getValue(), __eval.__getEnv().getStore()));
			} else {
				throw new UndeclaredField(label, receiver.getType(), this);
			}

		}
		
		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			return makeResult(TF.boolType(), getReceiver().interpret(__eval.getEvaluator()).isDefined(getField()).getValue(), __eval.getEvaluator());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> receiver = this.getReceiver().interpret(__eval);
			String label = org.rascalmpl.interpreter.utils.Names.name(this
					.getField());

			if (receiver == null || receiver.getValue() == null) {
				// TODO: can this ever happen?
				throw new UndeclaredVariable(label, this.getReceiver());
			}

			Type receiverType = receiver.getType();
			if (receiverType.isTuple()) {
				// the run-time tuple may not have labels, the static type can
				// have labels,
				// so we use the static type here.
				int index = receiverType.getFieldIndex(label);
				IValue result = ((ITuple) receiver.getValue()).get(index);
				Type type = receiverType.getFieldType(index);
				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(type, result, __eval);
			}
			else if (receiverType.isExternalType() && receiverType instanceof NonTerminalType) {
				return receiver.fieldAccess(label, __eval.getCurrentEnvt().getStore());
			}
			else if (receiverType.isConstructor() || receiverType.isAbstractData()) {
				IConstructor cons = (IConstructor) receiver.getValue();
				Type node = cons.getConstructorType();
				Type kwType = __eval.getCurrentEnvt().getConstructorFunction(node).getKeywordArgumentTypes(__eval.getCurrentEnvt());

				if (!kwType.hasField(label) && !node.hasField(label)) {
					throw new UndeclaredField(label, receiverType, this);
				}

				if (kwType.hasField(label)) {
				  return ResultFactory
						  .makeResult(kwType.getFieldType(label), cons.asWithKeywordParameters().getParameter(label)
								  ,__eval);
				}
				else {
				  int index = node.getFieldIndex(label);
				  return ResultFactory
				      .makeResult(node.getFieldType(index), cons.get(index),
				          __eval);
				}
			} else if (receiverType.isSourceLocation()) {
				return receiver.fieldAccess(label, new TypeStore());
			} else {
				throw new UndeclaredField(label, receiverType, this);
			}

		}

	}

	static public class IfDefinedOrDefault extends
			org.rascalmpl.ast.Assignable.IfDefinedOrDefault {

		public IfDefinedOrDefault(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Assignable __param2, Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {
			if (getReceiver().isDefined(__eval.getEvaluator()).getValue().getValue()) {
				return getReceiver().assignment(__eval);
			}
			else {
				__eval.__setValue(__eval.newResult(this.getDefaultExpression()
						.interpret((Evaluator) __eval.__getEval()), __eval
						.__getValue()));
				__eval.__setOperator(AssignmentOperator.Default);
				return this.getReceiver().assignment(__eval);
			}
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			throw new ImplementationError(
					"ifdefined assignable does not represent a value");

		}

	}

	static public class Subscript extends
			org.rascalmpl.ast.Assignable.Subscript {

		public Subscript(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Assignable __param2,
				Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {
			__eval.__getEval().setCurrentAST(this);
			Result<IValue> rec = this.getReceiver().interpret(
					(Evaluator) __eval.__getEval());
			Result<IValue> subscript = this.getSubscript().interpret(
					(Evaluator) __eval.__getEval());
			Result<IValue> result;

			if (rec == null || rec.getValue() == null) {
				throw new UninitializedVariable(this.getReceiver());
			}

			if (rec.getType().isList()
					&& subscript.getType().isInteger()) {
				try {
					IList list = (IList) rec.getValue();
					int index = ((IInteger) subscript.getValue()).intValue();
					__eval.__setValue(__eval.newResult(list.get(index), __eval
							.__getValue()));
					list = list.put(index, __eval.__getValue().getValue());
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(list.getType()) : rec.getType(), list,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
							.indexOutOfBounds((IInteger) subscript.getValue(),
									__eval.__getEval().getCurrentAST(), __eval
											.__getEval().getStackTrace());
				}
			} else if (rec.getType().isMap()) {
				Type keyType = rec.getType().getKeyType();

				if (rec.hasInferredType() || subscript.getType().isSubtypeOf(keyType)) {
					IValue oldValue = ((IMap) rec.getValue()).get(subscript.getValue());
					Result<IValue> oldResult = null;
					
					if (oldValue != null) {
						Type oldType = rec.getType().getValueType();
					    oldResult = makeResult(oldType, oldValue, __eval.getEvaluator());
					    oldResult.setInferredType(rec.hasInferredType());
					    __eval.__setValue(__eval.newResult(oldResult, __eval.__getValue()));
					}
					
					IMap map = ((IMap) rec.getValue()).put(subscript.getValue(), __eval.__getValue().getValue());
					result = makeResult(rec.hasInferredType() ? rec.getType().lub(map.getType()) : rec.getType(), map,
									__eval.__getEval());
				} else {
					throw new UnexpectedType(keyType, subscript.getType(),
							this.getSubscript());
				}

			} else if (rec.getType().isNode()
					&& subscript.getType().isInteger()) {
				int index = ((IInteger) subscript.getValue()).intValue();
				IConstructor node = (IConstructor) rec.getValue();

				if (index >= node.arity()) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
							.indexOutOfBounds((IInteger) subscript.getValue(),
									__eval.__getEval().getCurrentAST(), __eval
											.__getEval().getStackTrace());
				}
				__eval.__setValue(__eval.newResult(node.get(index), __eval
						.__getValue()));
				node = node.set(index, __eval.__getValue().getValue());
				result = org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(rec.getType(), node, __eval.__getEval());
			} else if (rec.getType().isTuple()
					&& subscript.getType().isInteger()) {
				int index = ((IInteger) subscript.getValue()).intValue();
				ITuple tuple = (ITuple) rec.getValue();

				if (index >= tuple.arity()) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
							.indexOutOfBounds((IInteger) subscript.getValue(),
									__eval.__getEval().getCurrentAST(), __eval
											.__getEval().getStackTrace());
				}

				__eval.__setValue(__eval.newResult(tuple.get(index), __eval
						.__getValue()));

				tuple = tuple.set(index, __eval.__getValue().getValue());
				result = org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(rec.getType(), tuple, __eval.__getEval());
			} else if (rec.getType().isRelation()
					&& subscript.getType().isSubtypeOf(
							rec.getType().getFieldType(0))) {
				ISet rel = (ISet) rec.getValue();
				IValue sub = subscript.getValue();

				if (rec.getType().getArity() != 2) {
					throw new UnsupportedSubscript(rec.getType(),
							subscript.getType(), this);
				}

				if (!__eval.__getValue().getType().isSubtypeOf(
						rec.getType().getFieldType(1))) {
					throw new UnexpectedType(
							rec.getType().getFieldType(1), __eval.__getValue()
									.getType(), __eval.__getEval()
									.getCurrentAST());
				}

				rel = rel.insert(__eval.__getEval().getValueFactory().tuple(
						sub, __eval.__getValue().getValue()));
				result = org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(rec.getType(), rel, __eval.__getEval());
			} else {
				throw new UnsupportedSubscript(rec.getType(), subscript
						.getType(), this);
				// TODO implement other subscripts
			}

			return __eval.recur(this, result);

		}
		
		/**
		 * Return an evaluation result that is already in normal form, i.e., all
		 * potential rules have already been applied to it.
		 */
		private Result<IValue> normalizedResult(IEvaluator<Result<IValue>> __eval, Type t, IValue v) {
			Map<Type, Type> bindings = __eval.getCurrentEnvt().getTypeBindings();
			Type instance;

			if (bindings.size() > 0) {
				instance = t.instantiate(bindings);
			} else {
				instance = t;
			}

			if (v != null) {
				checkType(v.getType(), instance);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(instance, v, __eval);
		}
		
		private void checkType(Type given, Type expected) {
			if (expected instanceof FunctionType) {
				return;
			}
			if (!given.isSubtypeOf(expected)) {
				throw new UnexpectedType(expected, given, this);
			}
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> receiver = this.getReceiver().interpret(__eval);
			Result<IValue> subscript = this.getSubscript().interpret(__eval);

			assert receiver != null && receiver.getValue() != null;
			
			if (receiver.getType().isList()) {
				if (subscript.getType().isInteger()) {
					IList list = (IList) receiver.getValue();
					IValue result = list.get(((IInteger) subscript.getValue())
							.intValue());
					Type type = receiver.getType().getElementType();
					return normalizedResult(__eval, type, result);
				}

				throw new UnexpectedType(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.integerType(), subscript.getType(), this);
			} else if (receiver.getType().isMap()) {
				Type keyType = receiver.getType().getKeyType();

				if (receiver.hasInferredType()|| subscript.getType().isSubtypeOf(keyType)) {
					IValue result = ((IMap) receiver.getValue()).get(subscript.getValue());

					if (result == null) {
						throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
								.noSuchKey(subscript.getValue(), this, __eval
										.getStackTrace());
					}
					Type type = receiver.getType().getValueType();
					return org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(type, result, __eval);
				}

				throw new UnexpectedType(keyType, subscript.getType(),
						this.getSubscript());
			}
			// TODO implement other subscripts
			throw new UnsupportedOperation("subscript",
					receiver.getType(), this);

		}
		
		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> subscript = this.getSubscript().interpret(__eval);
			return getReceiver().interpret(__eval.getEvaluator()).isKeyDefined(new Result<?>[] { subscript });
		}

	}
	
	static public class Slice extends
	org.rascalmpl.ast.Assignable.Slice {

		public Slice(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Assignable __param2,
				OptionalExpression __param3, OptionalExpression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			Result<IValue> rec = this.getReceiver().interpret(
					(Evaluator) __eval.__getEval());
			Result<IValue> first = null;
			if(this.getOptFirst().hasExpression()){
				first = this.getOptFirst().getExpression().interpret(
					(Evaluator) __eval.__getEval());
			}
			Result<IValue> end = null;
			if(this.getOptLast().hasExpression()){
				end = this.getOptLast().getExpression().interpret(
					(Evaluator) __eval.__getEval());
			}
			Result<IValue> result;

      assert rec != null && rec.getValue() != null;
			
			if( !(first == null || first.getType().isInteger()) ){
				throw new UnsupportedSubscript(rec.getType(), first.getType(), this);
			}
					
			if( !(end == null || end.getType().isInteger()) ){
				throw new UnsupportedSubscript(rec.getType(), end.getType(), this);
			}
			int len =  rec.getType().isList() ? ((IList) rec.getValue()).length()
					 : rec.getType().isString() ? ((IString) rec.getValue()).length()
					 : rec.getType().isNode() ?((INode) rec.getValue()).arity() 
					 : 0 ;

			int firstIndex = 0;
			int secondIndex = 1;
			int endIndex = len;

			if(first != null){
				firstIndex = ((IInteger) first.getValue()).intValue();
				if(firstIndex < 0)
					firstIndex += len;
			}

			if(end != null){
				endIndex =  ((IInteger) end.getValue()).intValue();
				if(endIndex < 0){
					endIndex += len;
				}
			}

			secondIndex = firstIndex <= endIndex ? firstIndex + 1 : firstIndex - 1;

			if (rec.getType().isList()) {
				try {
					IList list = (IList) rec.getValue();
					
					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isList()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}
					
					__eval.__setValue(__eval.newResult(list, __eval.__getValue()));
					list = list.replace(firstIndex, secondIndex, endIndex, (IList) repl);
					
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(list.getType()) : rec.getType(), list,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
			} else if (rec.getType().isString()) {
				try {
					IString str = (IString) rec.getValue();
					
					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isString()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}
					
					__eval.__setValue(__eval.newResult(str, __eval.__getValue()));
					str = str.replace(firstIndex, secondIndex, endIndex, (IString) repl);
					
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(str.getType()) : rec.getType(), str,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
			} else if (rec.getType().isNode()) {

				try {
					INode node = (INode) rec.getValue();

					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isList()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}

					__eval.__setValue(__eval.newResult(node, __eval.__getValue()));
					node = node.replace(firstIndex, secondIndex, endIndex, (IList) repl);

					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(node.getType()) : rec.getType(), node,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
			} else {
				throw new UnsupportedSlice(rec.getType(), this);
				// TODO implement other slices
			}

			return __eval.recur(this, result);
		}
	}
	
	static public class SliceStep extends
	org.rascalmpl.ast.Assignable.SliceStep {

		public SliceStep(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Assignable __param2,
				OptionalExpression __param3, Expression __param4, OptionalExpression __param5) {
			super(__param1, tree, __param2, __param3, __param4, __param5);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			Result<IValue> rec = this.getReceiver().interpret(
					(Evaluator) __eval.__getEval());
			Result<IValue> first = null;
			if(this.getOptFirst().hasExpression()){
				first = this.getOptFirst().getExpression().interpret(
					(Evaluator) __eval.__getEval());
			}
			Result<IValue> second = this.getSecond().interpret((Evaluator) __eval.__getEval());
			Result<IValue> end = null;
			if(this.getOptLast().hasExpression()){
				end = this.getOptLast().getExpression().interpret(
					(Evaluator) __eval.__getEval());
			}
			Result<IValue> result;

			assert rec != null && rec.getValue() != null;
			
			if( !(first == null || first.getType().isInteger()) ){
				throw new UnsupportedSubscript(rec.getType(), first.getType(), this);
			}
			
			if(!second.getType().isInteger()){
				throw new UnsupportedSubscript(rec.getType(), second.getType(), this);
			}
					
			if( !(end == null || end.getType().isInteger()) ){
				throw new UnsupportedSubscript(rec.getType(), end.getType(), this);
			}
			
			int len = rec.getType().isList() ? ((IList) rec.getValue()).length()
					 : rec.getType().isString() ? ((IString) rec.getValue()).length()
					 :  rec.getType().isNode() ?((INode) rec.getValue()).arity() 
					 : 0 ;

			int firstIndex = 0;
			int secondIndex = 1;
			int endIndex = len;

			if(first != null){
				firstIndex = ((IInteger) first.getValue()).intValue();
				if(firstIndex < 0)
					firstIndex += len;
			}

			if(end != null){
				endIndex =  ((IInteger) end.getValue()).intValue();
				if(endIndex < 0){
					endIndex += len;
				}
			}

			secondIndex = ((IInteger)second.getValue()).intValue();
			if(secondIndex < 0)
				secondIndex += len;

			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}

			if (rec.getType().isList()) {
				try {
					IList list = (IList) rec.getValue();					
					
					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isList()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}
					
					__eval.__setValue(__eval.newResult(list, __eval.__getValue()));					
					list = list.replace(firstIndex, secondIndex, endIndex, (IList) repl);
					
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(list.getType()) : rec.getType(), list,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
			} else if (rec.getType().isString()) {
				try {
					IString str = (IString) rec.getValue();

					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isString()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}
					
					__eval.__setValue(__eval.newResult(str, __eval.__getValue()));
					str = str.replace(firstIndex, secondIndex, endIndex, (IString) repl);
					
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(str.getType()) : rec.getType(), str,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
				
			} else if (rec.getType().isNode()) {

				try {
					INode node = (INode) rec.getValue();

					IValue repl = __eval.__getValue().getValue();
					if(!repl.getType().isList()){
						throw new UnexpectedType(rec.getType(), repl.getType(), __eval.__getEval().getCurrentAST());
					}

					__eval.__setValue(__eval.newResult(node, __eval.__getValue()));
					node = node.replace(firstIndex, secondIndex, endIndex, (IList) repl);

					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(rec.hasInferredType() ? rec.getType()
									.lub(node.getType()) : rec.getType(), node,
									__eval.__getEval());
				} catch (IndexOutOfBoundsException e) { // include last in message
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
					.indexOutOfBounds((IInteger) first.getValue(),
							__eval.__getEval().getCurrentAST(), __eval
							.__getEval().getStackTrace());
				}
			} else {
				throw new UnsupportedSlice(rec.getType(), this);
				// TODO implement other slices
			}

			return __eval.recur(this, result);
		}

	}

	static public class Tuple extends org.rascalmpl.ast.Assignable.Tuple {

		public Tuple(ISourceLocation __param1, IConstructor tree, List<org.rascalmpl.ast.Assignable> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			List<org.rascalmpl.ast.Assignable> arguments = this.getElements();

			if (!__eval.__getValue().getType().isTuple()) {
				// TODO construct a better expected type
				throw new UnexpectedType(
						org.rascalmpl.interpreter.AssignableEvaluator.__getTf()
								.tupleEmpty(), __eval.__getValue().getType(),
						this);
			}

			Type tupleType = __eval.__getValue().getType();
			ITuple tuple = (ITuple) __eval.__getValue().getValue();
			IValue[] results = new IValue[arguments.size()];
			Type[] resultTypes = new Type[arguments.size()];

			for (int i = 0; i < arguments.size(); i++) {
				Type argType = tupleType.getFieldType(i);
				IValue arg = tuple.get(i);
				Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(argType, arg, __eval.__getEval());
				AssignableEvaluator ae = new AssignableEvaluator(__eval
						.__getEnv(), null, result, __eval.__getEval());
				Result<IValue> argResult = arguments.get(i).assignment(ae);
				results[i] = argResult.getValue();
				resultTypes[i] = argResult.getType();
			}

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.AssignableEvaluator.__getTf()
							.tupleType(resultTypes), __eval
							.__getEval().getValueFactory().tuple(results), __eval
							.__getEval());

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			throw new ImplementationError(
					"Tuple in assignable does not represent a value:" + this);

		}

	}

	static public class Variable extends org.rascalmpl.ast.Assignable.Variable {

		public Variable(ISourceLocation __param1, IConstructor tree, QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> assignment(AssignableEvaluator __eval) {

			QualifiedName qname = this.getQualifiedName();
			Result<IValue> previous = __eval.__getEnv().getVariable(qname);

			if (previous != null && previous.getValue() != null) {
				__eval.__setValue(__eval.newResult(previous, __eval
						.__getValue()));
				__eval.__getEnv().storeVariable(qname, __eval.__getValue());
				return __eval.__getValue();
			}

			switch (__eval.__getOperator()) {
			case Default:
			case IsDefined:
				__eval.__getEnv().storeVariable(qname, __eval.__getValue());
				return __eval.__getValue();
			default:
				throw new UninitializedVariable(Names.fullName(qname), this);
			}

			// TODO implement semantics of global keyword, when not given the
			// variable should be inserted in the local scope.

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			return __eval.getCurrentEnvt().getSimpleVariable(this.getQualifiedName());

		}

	}

	public Assignable(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
