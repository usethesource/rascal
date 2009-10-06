package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.BoolResult;
import org.meta_environment.rascal.interpreter.result.ConstructorResult;
import org.meta_environment.rascal.interpreter.result.IntegerResult;
import org.meta_environment.rascal.interpreter.result.ListResult;
import org.meta_environment.rascal.interpreter.result.MapResult;
import org.meta_environment.rascal.interpreter.result.NodeResult;
import org.meta_environment.rascal.interpreter.result.RealResult;
import org.meta_environment.rascal.interpreter.result.RelationResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.SetResult;
import org.meta_environment.rascal.interpreter.result.SourceLocationResult;
import org.meta_environment.rascal.interpreter.result.StringResult;
import org.meta_environment.rascal.interpreter.result.TupleResult;

public class Strategy extends AbstractFunction {

	protected final AbstractFunction function;

	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}

	public AbstractFunction getFunction() {
		return function;
	}
	
	// Generics stink (just so you know ;-)).
	public static Result<IValue> makeResult(IValue result, IEvaluatorContext ctx){
		Type resultType = result.getType();
		if(resultType.isConstructorType()){
			return (Result<IValue>) (Result<?>) new ConstructorResult(resultType, (IConstructor) result, ctx);
		}else if(resultType.isNodeType()){
			return (Result<IValue>) (Result<?>) new NodeResult(resultType, (INode) result, ctx);
		}else if(resultType.isTupleType()){
			return (Result<IValue>) (Result<?>) new TupleResult(resultType, (ITuple) result, ctx);
		}else if(resultType.isListType()){
			return (Result<IValue>) (Result<?>) new ListResult(resultType, (IList) result, ctx);
		}else if(resultType.isRelationType()){
			return (Result<IValue>) (Result<?>) new RelationResult(resultType, (IRelation) result, ctx);
		}else if(resultType.isSetType()){
			return (Result<IValue>) (Result<?>) new SetResult(resultType, (ISet) result, ctx);
		}else if(resultType.isMapType()){
			return (Result<IValue>) (Result<?>) new MapResult(resultType, (IMap) result, ctx);
		}else if(resultType.isBoolType()){
			return (Result<IValue>) (Result<?>) new BoolResult(resultType, (IBool) result, ctx);
		}else if(resultType.isIntegerType()){
			return (Result<IValue>) (Result<?>) new IntegerResult(resultType, (IInteger) result, ctx);
		}else if(resultType.isRealType()){
			return (Result<IValue>) (Result<?>) new RealResult(resultType, (IReal) result, ctx);
		}else if(resultType.isStringType()){
			return (Result<IValue>) (Result<?>) new StringResult(resultType, (IString) result, ctx);
		}else if(resultType.isSourceLocationType()){
			return (Result<IValue>) (Result<?>) new SourceLocationResult(resultType, (ISourceLocation) result, ctx);
		}else{
			throw new RuntimeException("Unknown result type: "+resultType);
		}
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		return function.call(argTypes, argValues);
	}

	public static IValue getCurrentStratCtx(IEvaluatorContext ctx) {
		if (ctx.getStrategyContext() != null) {
			return ctx.getStrategyContext().getValue();
		}
		//TODO: need to be fix
		return ValueFactoryFactory.getValueFactory().string("strategycontext_null");
	}

}
