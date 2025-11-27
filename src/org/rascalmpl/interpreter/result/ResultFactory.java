/*******************************************************************************
 * Copyright (c) 2009-2013 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 * * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl *
 * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen) * Paul Klint - Paul.Klint@cwi.nl - CWI * Mark
 * Hills - Mark.Hills@cwi.nl (CWI) * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.interpreter.result;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.ITypeVisitor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class ResultFactory {
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return (Result<T>) declaredType.accept(new Visitor(declaredType, value, ctx));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> nothing() {
		Type type = TypeFactory.getInstance().voidType();
		return (Result<T>) type.accept(new Visitor(type, null, null));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IValue> Result<T> nothing(Type type) {
		return (Result<T>) type.accept(new Visitor(type, null, null));
	}
	

	public static Result<IBool> bool(boolean b, IEvaluatorContext ctx) {
		IValueFactory vf = ctx.getValueFactory();
		IBool result = vf.bool(b);
		return new BoolResult(result.getType(), result, ctx);
	}
	
	private static class Visitor implements ITypeVisitor<Result<? extends IValue>, RuntimeException> {
		private IValue value;
		private Type declaredType;
		private IEvaluatorContext ctx;

		public Visitor(Type type, IValue value, IEvaluatorContext ctx) {
			this.declaredType = type;
			this.value = value;
			this.ctx = ctx;
		}
		
		@Override
		public ElementResult<? extends IValue> visitAbstractData(Type type) {
			// TODO: rename constructor result to AbstractData
			if (type.isSubtypeOf(RascalValueFactory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}
			return new ConstructorResult(declaredType, (IConstructor)value, ctx);
		}
		
		@Override
		public Result<? extends IValue> visitAlias(Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public BoolResult visitBool(Type boolType) {
			return new BoolResult(declaredType, (IBool)value, ctx);
		}

		@Override
		public Result<? extends IValue> visitConstructor(Type type) {
			if (RascalType.isNonterminal(RascalValueFactory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}
			else if (RascalType.isFunction(type)) {
				return (AbstractFunction)value;
			}
			
			return new ConstructorResult(declaredType.getAbstractDataType(), (IConstructor)value, ctx);
		}
		
		@Override
		public ElementResult<IReal> visitReal(Type type) {
			return new RealResult(declaredType, (IReal)value, ctx);
		}
		
		@Override
		public IntegerResult visitInteger(Type type) {
			return new IntegerResult(declaredType, (IInteger)value, ctx);
		}
		
		@Override
		public RationalResult visitRational(Type type) {
			return new RationalResult(declaredType, (IRational)value, ctx);
		}
		
		@Override
		public NumberResult visitNumber(Type type) {
			return new NumberResult(declaredType, (INumber) value, ctx);
		}
		
		@Override
		public ListOrRelationResult<IList> visitList(Type type) {
			if (type.isListRelation()) {
			    // TODOmmm. what if it was first a list relation (an empty one), but not anymore?
				if (value != null && !(value.getType().isListRelation())) {
					throw new ImplementationError("somehow a list relation value turned into a list, but its type did not change with it", ctx.getCurrentAST().getLocation());
				}
				
				return new ListRelationResult(declaredType, (IList)value, ctx);
			}

			return new ListResult(declaredType, (IList)value, ctx);
		}

		@Override
		public MapResult visitMap(Type type) {
			return new MapResult(declaredType, (IMap)value, ctx);
		}

		@Override
		public ElementResult<? extends IValue> visitNode(Type type) {
			if (type.isSubtypeOf(RascalValueFactory.Tree)) {
				return new ConcreteSyntaxResult(declaredType, (IConstructor)value, ctx);
			}

			return new NodeResult(declaredType, (INode)value, ctx);
		}

		@Override
		public Result<? extends IValue> visitParameter(Type parameterType) {
			if (ctx != null) {
				Type instantiated = parameterType.instantiate(ctx.getEvaluator().getCurrentEnvt().getStaticTypeBindings());

				if (parameterType != instantiated) {
					return instantiated.accept(this);
				}
			}

			return parameterType.getBound().accept(this);
		}
		
		@Override
		public SetOrRelationResult<ISet> visitSet(Type type) {
			if (type.isRelation()) {
				if (value != null && !(value.getType().isRelation()))
					throw new ImplementationError("somehow a relation value turned into a set, but its type did not change with it", ctx.getCurrentAST().getLocation());
				return new RelationResult(declaredType, (ISet)value, ctx);
			}
			
			return new SetResult(declaredType, (ISet)value, ctx);
		}

		@Override
		public SourceLocationResult visitSourceLocation(Type type) {
			return new SourceLocationResult(declaredType, (ISourceLocation)value, ctx);		
		}

		@Override
		public StringResult visitString(Type type) {
			return new StringResult(declaredType, (IString)value, ctx);
		}

		@Override
		public TupleResult visitTuple(Type type) {
			return new TupleResult(declaredType, (ITuple)value, ctx);
		}

		@Override
		public Result<?> visitFunction(Type type) {
			if (value instanceof AbstractFunction) {
				// the weird thing is, that value is also a result in that case.

				if (value.getType() != type) {
					return new FunctionResultFacade(type, (AbstractFunction) value, ctx);
				}
				else {
					return (AbstractFunction) value;
				}
			}
			else if (value instanceof OverloadedFunction) {
				if (value.getType() != type) {
					return new FunctionResultFacade(type, (OverloadedFunction) value, ctx);
				}
				else {
					return (OverloadedFunction) value;
				}
			}
			else if (value instanceof ComposedFunctionResult) {
				return (Result<?>) value;
			}
			else {
				// otherwise this is an abstract ICalleableValue
				// for which no further operations are defined?
				return new ValueResult(declaredType, value, ctx);
			}
		}

		@Override
		public ValueResult visitValue(Type type) {
			return new ValueResult(declaredType, value, ctx);
		}

		@Override
		public VoidResult visitVoid(Type type) {
			return new VoidResult(declaredType, ctx);
		}

		@Override
		public Result<? extends IValue> visitExternal(Type externalType) {
			if (RascalType.isFunction(externalType)) {
			    
			}
			else if (RascalType.isNonterminal(externalType)) {
				return new ConcreteSyntaxResult(externalType, (IConstructor) value, ctx);
			} 
			else if (RascalType.isReified(externalType)) {
				return new ConstructorResult(externalType, (IConstructor) value, ctx);
			}

			return new ValueResult(declaredType, value, ctx);
		}

		@Override
		public Result<? extends IValue> visitDateTime(Type type) {
			return new DateTimeResult(declaredType, (IDateTime)value, ctx);		
		}
	}
}
