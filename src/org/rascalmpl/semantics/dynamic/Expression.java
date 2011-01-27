package org.rascalmpl.semantics.dynamic;

public abstract class Expression extends org.rascalmpl.ast.Expression {


public Expression (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class ReifiedType extends org.rascalmpl.ast.Expression.ReifiedType {


public ReifiedType (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.BasicType __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.BasicType basic = this.getBasicType();
		java.util.List<org.rascalmpl.ast.Expression> args = this.getArguments();
		org.eclipse.imp.pdb.facts.type.Type[] fieldTypes = new org.eclipse.imp.pdb.facts.type.Type[args.size()];
		org.eclipse.imp.pdb.facts.IValue[] fieldValues = new org.eclipse.imp.pdb.facts.IValue[args.size()];

		int i = 0;
		boolean valued = false;
		for (org.rascalmpl.ast.Expression a : args) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> argResult = a.interpret(__eval);
			org.eclipse.imp.pdb.facts.type.Type argType = argResult.getType();

			if (argType instanceof org.rascalmpl.interpreter.types.ReifiedType) {
				fieldTypes[i] = argType.getTypeParameters().getFieldType(0);
				i++;
			}
			else {
				valued = true;
				fieldValues[i] = argResult.getValue();
				i++;
			}
		}

		org.eclipse.imp.pdb.facts.type.Type type = basic.__evaluate(new org.rascalmpl.interpreter.BasicTypeEvaluator(__eval.getCurrentEnvt(), valued ? null : org.rascalmpl.interpreter.Evaluator.__getTf().tupleType(fieldTypes), valued ? fieldValues : null));

		return type.accept(new org.rascalmpl.interpreter.TypeReifier(__eval, __eval.__getVf()));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.ast.BasicType basic = this.getBasicType();
		java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> args = __eval.visitElements(this.getArguments());

		return new org.rascalmpl.interpreter.matching.ReifiedTypePattern(__eval.__getCtx(), this, basic, args);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class StepRange extends org.rascalmpl.ast.Expression.StepRange {


public StepRange (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> from = this.getFirst().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> to = this.getLast().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> second = this.getSecond().interpret(__eval);
		return from.makeStepRange(to, second);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class And extends org.rascalmpl.ast.Expression.And {


public And (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.AndResult(__eval.__getCtx(), this.getLhs().__evaluate(__eval), this.getRhs()
				.__evaluate(__eval));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class TransitiveClosure extends org.rascalmpl.ast.Expression.TransitiveClosure {


public TransitiveClosure (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getArgument().interpret(__eval).transitiveClosure();
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class MultiVariable extends org.rascalmpl.ast.Expression.MultiVariable {


public MultiVariable (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.Name name = this.getName();
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> variable = __eval.getCurrentEnvt().getVariable(name);

		if (variable == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError(org.rascalmpl.interpreter.utils.Names.name(name), name);
		}

		if (variable.getValue() == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(org.rascalmpl.interpreter.utils.Names.name(name), name);
		}

		return variable;
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.MultiVariablePattern(__eval.__getCtx(), this, this.getQualifiedName());
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

}
static public class LessThan extends org.rascalmpl.ast.Expression.LessThan {


public LessThan (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.lessThan(right);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class NotIn extends org.rascalmpl.ast.Expression.NotIn {


public NotIn (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return right.notIn(left);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class CallOrTree extends org.rascalmpl.ast.Expression.CallOrTree {


public CallOrTree (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.ast.Expression nameExpr = this.getExpression();
		if(__eval.isConcreteSyntaxList(this)) {
			org.rascalmpl.ast.Expression.List args = (org.rascalmpl.ast.Expression.List)this.getArguments().get(1);
			// TODO what if somebody writes a variable in  the list production itself?
			return new org.rascalmpl.interpreter.matching.ConcreteListPattern(__eval.__getCtx(), this,
					__eval.visitElements(args.getElements()));
		}
		if(__eval.isConcreteSyntaxAppl(this)){
			if (org.rascalmpl.values.uptr.TreeAdapter.isLexical((org.eclipse.imp.pdb.facts.IConstructor) this.getTree())) {
				return new org.rascalmpl.interpreter.matching.ConcreteApplicationPattern(__eval.__getCtx(), this, __eval.visitConcreteLexicalArguments(this));
			}
			
			return new org.rascalmpl.interpreter.matching.ConcreteApplicationPattern(__eval.__getCtx(), this, __eval.visitConcreteArguments(this));
		}
		if (__eval.isConcreteSyntaxAmb(this)) {
			new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
			//			return new AbstractPatternConcreteAmb(vf, new EvaluatorContext(ctx.getEvaluator(), this), this, visitArguments(this));
		}

		if (nameExpr.isQualifiedName()) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> prefix = __eval.__getCtx().getCurrentEnvt().getVariable(nameExpr.getQualifiedName());

			// TODO: get rid of __eval if-then-else by introducing subclasses for NodePattern for each case.
			if (nameExpr.isQualifiedName() && prefix == null) {
				return new org.rascalmpl.interpreter.matching.NodePattern(__eval.__getCtx(), this, null, nameExpr.getQualifiedName(), __eval.visitArguments(this));
			}
			else if (nameExpr.isQualifiedName() && ((prefix instanceof org.rascalmpl.interpreter.result.AbstractFunction) || prefix instanceof org.rascalmpl.interpreter.result.OverloadedFunctionResult)) {
				return new org.rascalmpl.interpreter.matching.NodePattern(__eval.__getCtx(), this, null,nameExpr.getQualifiedName(), __eval.visitArguments(this));
			}
		}

		return new org.rascalmpl.interpreter.matching.NodePattern(__eval.__getCtx(), this, nameExpr.__evaluate(__eval), null, __eval.visitArguments(this));
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
		
		__eval.setCurrentAST(this);

		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> function = this.getExpression().interpret(__eval);
		
		java.util.List<org.rascalmpl.ast.Expression> args = this.getArguments();

		org.eclipse.imp.pdb.facts.IValue[] actuals = new org.eclipse.imp.pdb.facts.IValue[args.size()];
		org.eclipse.imp.pdb.facts.type.Type[] types = new org.eclipse.imp.pdb.facts.type.Type[args.size()];

		for(int i = 0; i < args.size(); i++){
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> resultElem = args.get(i).interpret(__eval);
			types[i] = resultElem.getType();
			actuals[i] = resultElem.getValue();
		}
		
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> res = function.call(types, actuals);
		
		// we need to update the strategy context when the function is of type Strategy
		org.rascalmpl.interpreter.strategy.IStrategyContext strategyContext = __eval.getStrategyContext();
		if(strategyContext != null){
			if(function.getValue() instanceof org.rascalmpl.interpreter.result.AbstractFunction){
				org.rascalmpl.interpreter.result.AbstractFunction f = (org.rascalmpl.interpreter.result.AbstractFunction) function.getValue();
				if(f.isTypePreserving()){
					strategyContext.update(actuals[0], res.getValue());
				}
			}else if(function.getValue() instanceof org.rascalmpl.interpreter.result.OverloadedFunctionResult){
				org.rascalmpl.interpreter.result.OverloadedFunctionResult fun = (org.rascalmpl.interpreter.result.OverloadedFunctionResult) function.getValue();
				
				for(org.rascalmpl.interpreter.result.AbstractFunction f: fun.iterable()){
					if(f.isTypePreserving()){
						strategyContext.update(actuals[0], res.getValue());
					}
				}
			}
		}
		return res;
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

}
static public class List extends org.rascalmpl.ast.Expression.List {


public List (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.ListPattern(__eval.__getCtx(), this, __eval.visitElements(this.getElements()));
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> elements = this
		.getElements();

		org.eclipse.imp.pdb.facts.type.Type elementType =  org.rascalmpl.interpreter.Evaluator.__getTf().voidType();
		java.util.List<org.eclipse.imp.pdb.facts.IValue> results = new java.util.ArrayList<org.eclipse.imp.pdb.facts.IValue>();

		// Splicing is true for the complete list; a terrible, terrible hack.
		boolean splicing = __eval.__getConcreteListsShouldBeSpliced();
		boolean first = true;
		int skip = 0;

		for (org.rascalmpl.ast.Expression expr : elements) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> resultElem = expr.interpret(__eval);
			
			if(resultElem.getType().isVoidType())
				throw new org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired(expr);

			if (skip > 0) {
				skip--;
				continue;
			}

			org.eclipse.imp.pdb.facts.type.Type resultType = resultElem.getType();
			if (splicing && resultType instanceof org.rascalmpl.interpreter.types.NonTerminalType) {
				org.eclipse.imp.pdb.facts.IConstructor sym = ((org.rascalmpl.interpreter.types.NonTerminalType)resultType).getSymbol();

				if (org.rascalmpl.values.uptr.SymbolAdapter.isAnyList(sym)) {
					org.eclipse.imp.pdb.facts.IConstructor appl = ((org.eclipse.imp.pdb.facts.IConstructor)resultElem.getValue());
					org.eclipse.imp.pdb.facts.IList listElems = org.rascalmpl.values.uptr.TreeAdapter.getArgs(appl);
					// Splice elements in list if element types permit __eval

					if (!listElems.isEmpty()) {
						for(org.eclipse.imp.pdb.facts.IValue val : listElems){
							elementType = elementType.lub(val.getType());
							results.add(val);
						}
					}
					else {
						// make sure to remove surrounding sep
						if (!first) {
							if (org.rascalmpl.values.uptr.SymbolAdapter.isIterStarSeps(sym)) {
								for (@SuppressWarnings("unused") org.eclipse.imp.pdb.facts.IValue sep : org.rascalmpl.values.uptr.SymbolAdapter.getSeparators(sym)) {
									results.remove(results.size() - 1);
								}
							}
						}
						else {
							if (org.rascalmpl.values.uptr.SymbolAdapter.isIterStarSeps(sym)) {
								skip = org.rascalmpl.values.uptr.SymbolAdapter.getSeparators(sym).length();
							}
						}
					}
				}
				else {
					// Just add it.
					elementType = elementType.lub(resultElem.getType());
					results.add(results.size(), resultElem.getValue());
				}
			}
			else {
				/* = no concrete syntax */ 
				if(resultElem.getType().isListType() &&
						!expr.isList() &&
						elementType.isSubtypeOf(resultElem.getType().getElementType())
				){
					/*
					 * Splice elements in list if element types permit __eval
					 */
					for(org.eclipse.imp.pdb.facts.IValue val : ((org.eclipse.imp.pdb.facts.IList) resultElem.getValue())){
						elementType = elementType.lub(val.getType());
						results.add(val);
					}
				} else {
					elementType = elementType.lub(resultElem.getType());

					results.add(results.size(), resultElem.getValue());
				}
			}


			first = false;
		}
		org.eclipse.imp.pdb.facts.type.Type resultType = org.rascalmpl.interpreter.Evaluator.__getTf().listType(elementType);
		org.eclipse.imp.pdb.facts.IListWriter w = resultType.writer(__eval.__getVf());
		w.appendAll(results);
		// Was: return makeResult(resultType, applyRules(w.done()));
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(resultType, w.done(), __eval);
	
}

}
static public class Map extends org.rascalmpl.ast.Expression.Map {


public Map (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Mapping_Expression> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	

		java.util.List<org.rascalmpl.ast.Mapping_Expression> mappings = this
		.getMappings();
		java.util.Map<org.eclipse.imp.pdb.facts.IValue,org.eclipse.imp.pdb.facts.IValue> result = new java.util.HashMap<org.eclipse.imp.pdb.facts.IValue,org.eclipse.imp.pdb.facts.IValue>();
		org.eclipse.imp.pdb.facts.type.Type keyType = org.rascalmpl.interpreter.Evaluator.__getTf().voidType();
		org.eclipse.imp.pdb.facts.type.Type valueType = org.rascalmpl.interpreter.Evaluator.__getTf().voidType();

		for (org.rascalmpl.ast.Mapping_Expression mapping : mappings) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> keyResult = mapping.getFrom().interpret(__eval);
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> valueResult = mapping.getTo().interpret(__eval);
			
			if(keyResult.getType().isVoidType())
				throw new org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired(mapping.getFrom());
			
			if(valueResult.getType().isVoidType())
				throw new org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired(mapping.getTo());

			keyType = keyType.lub(keyResult.getType());
			valueType = valueType.lub(valueResult.getType());
			
			org.eclipse.imp.pdb.facts.IValue keyValue = result.get(keyResult.getValue());
			if(keyValue != null){
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.MultipleKey(keyValue, mapping.getFrom(), __eval.getStackTrace());
			}

			result.put(keyResult.getValue(), valueResult.getValue());
		}

		org.eclipse.imp.pdb.facts.type.Type type = org.rascalmpl.interpreter.Evaluator.__getTf().mapType(keyType, valueType);
		org.eclipse.imp.pdb.facts.IMapWriter w = type.writer(__eval.__getVf());
		w.putAll(result);

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(type, w.done(), __eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Map in pattern not yet implemented");
	
}

}
static public class ReifyType extends org.rascalmpl.ast.Expression.ReifyType {


public ReifyType (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type t = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentEnvt(), __eval.__getHeap()).eval(this.getType());
		return t.accept(new org.rascalmpl.interpreter.TypeReifier(__eval, __eval.__getVf()));
	
}

}
static public class QualifiedName extends org.rascalmpl.ast.Expression.QualifiedName {


public QualifiedName (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.ast.QualifiedName name = this.getQualifiedName();
		org.eclipse.imp.pdb.facts.type.Type signature = org.rascalmpl.interpreter.PatternEvaluator.__getTf().tupleType(new org.eclipse.imp.pdb.facts.type.Type[0]);

		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = __eval.__getCtx().getEvaluator().getCurrentEnvt().getVariable(name);

		if (r != null) {
			if (r.getValue() != null) {
				// Previously declared and initialized variable
				return new org.rascalmpl.interpreter.matching.QualifiedNamePattern(__eval.__getCtx(), this, name);
			}

			org.eclipse.imp.pdb.facts.type.Type type = r.getType();
			if (type instanceof org.rascalmpl.interpreter.types.NonTerminalType) {
				org.rascalmpl.interpreter.types.NonTerminalType cType = (org.rascalmpl.interpreter.types.NonTerminalType) type;
				if (cType.isConcreteListType()) {
					return new org.rascalmpl.interpreter.matching.ConcreteListVariablePattern(__eval.__getCtx(), this, type, org.rascalmpl.interpreter.utils.Names.lastName(name));
				}
			}

			return new org.rascalmpl.interpreter.matching.QualifiedNamePattern(__eval.__getCtx(), this,name);
		}

		if (__eval.__getCtx().getCurrentEnvt().isTreeConstructorName(name, signature)) {
			return new org.rascalmpl.interpreter.matching.NodePattern(__eval.__getCtx(), this, null, name,
					new java.util.ArrayList<org.rascalmpl.interpreter.matching.IMatchingResult>());
		}

		// Completely fresh variable
		return new org.rascalmpl.interpreter.matching.QualifiedNamePattern(__eval.__getCtx(), this, name);
		//return new AbstractPatternTypedVariable(vf, env, ev.tf.valueType(), name);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.QualifiedName name = this.getQualifiedName();
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> variable = __eval.getCurrentEnvt().getVariable(name);

		if (variable == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError(org.rascalmpl.interpreter.utils.Names.fullName(name), name);
		}

		if (variable.getValue() == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(org.rascalmpl.interpreter.utils.Names.fullName(name), name);
		}

		return variable;
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

}
static public class VoidClosure extends org.rascalmpl.ast.Expression.VoidClosure {


public VoidClosure (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Parameters __param2,java.util.List<org.rascalmpl.ast.Statement> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type formals = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentEnvt(), __eval.__getHeap()).eval(this.getParameters());
		org.rascalmpl.interpreter.types.RascalTypeFactory RTF = org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance();
		return new org.rascalmpl.interpreter.result.RascalFunction(this, __eval, (org.rascalmpl.interpreter.types.FunctionType) RTF.functionType(org.rascalmpl.interpreter.Evaluator.__getTf().voidType(), formals), this.getParameters().isVarArgs(), this.getStatements(), __eval.getCurrentEnvt(),
					__eval.__getAccumulators());

	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class TypedVariable extends org.rascalmpl.ast.Expression.TypedVariable {


public TypedVariable (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type type = new org.rascalmpl.interpreter.TypeEvaluator(__eval.__getCtx().getCurrentEnvt(), __eval.__getCtx().getHeap()).eval(this.getType());

		if (type instanceof org.rascalmpl.interpreter.types.NonTerminalType) {
			org.rascalmpl.interpreter.types.NonTerminalType cType = (org.rascalmpl.interpreter.types.NonTerminalType) type;
			if (cType.isConcreteListType()) {
				return new org.rascalmpl.interpreter.matching.ConcreteListVariablePattern(__eval.__getCtx(), this, type, this.getName());
			}
		}
		return new org.rascalmpl.interpreter.matching.TypedVariablePattern(__eval.__getCtx(), this, type, this.getName());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(this.toString(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO: should allow qualified names in TypeVariables?!?
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = __eval.getCurrentEnvt().getVariable(org.rascalmpl.interpreter.utils.Names.name(this.getName()));

		if (result != null && result.getValue() != null) {
			return result;
		}

		throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(org.rascalmpl.interpreter.utils.Names.name(this.getName()), this);
	
}

}
static public class Match extends org.rascalmpl.ast.Expression.Match {


public Match (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.MatchResult(__eval.__getCtx(), this.getPattern(), true, this.getExpression());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Guarded extends org.rascalmpl.ast.Expression.Guarded {


public Guarded (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type type =  new org.rascalmpl.interpreter.TypeEvaluator(__eval.__getCtx().getCurrentEnvt(), __eval.__getCtx().getHeap()).eval(this.getType());
		org.rascalmpl.interpreter.matching.IMatchingResult absPat = this.getPattern().__evaluate(__eval);
		return new org.rascalmpl.interpreter.matching.GuardedPattern(__eval.__getCtx(), this, type, absPat);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = this.getPattern().interpret(__eval);
		org.eclipse.imp.pdb.facts.type.Type expected = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentEnvt(), __eval.__getHeap()).eval(this.getType());

		// TODO: clean up __eval hack
		if (expected instanceof org.rascalmpl.interpreter.types.NonTerminalType && result.getType().isSubtypeOf(org.rascalmpl.interpreter.Evaluator.__getTf().stringType())) {
			java.lang.String command = '(' + expected.toString() + ')' + '`' + ((org.eclipse.imp.pdb.facts.IString) result.getValue()).getValue() + '`';
			__eval.__setInterrupt(false);
			org.eclipse.imp.pdb.facts.IConstructor tree = __eval.__getParser().parseCommand(this.getLocation().getURI(), command, new org.rascalmpl.parser.RascalActionExecutor(__eval, __eval.__getParser().getInfo()));

			tree = (org.eclipse.imp.pdb.facts.IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(tree).get(1); // top command expression
			tree = (org.eclipse.imp.pdb.facts.IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(tree).get(0); // typed quoted embedded fragment
			tree = (org.eclipse.imp.pdb.facts.IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(tree).get(8); // wrapped string between `...`
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(expected, tree, __eval);
		}
		if (!result.getType().isSubtypeOf(expected)) {
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(expected, result.getType(), this.getPattern());
		}

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(expected, result.getValue(), __eval);
	
}

}
static public class NoMatch extends org.rascalmpl.ast.Expression.NoMatch {


public NoMatch (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.MatchResult(__eval.__getCtx(), this.getPattern(), false, this.getExpression());
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Comprehension extends org.rascalmpl.ast.Expression.Comprehension {


public Comprehension (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Comprehension __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getComprehension().interpret(__eval);	
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Enumerator extends org.rascalmpl.ast.Expression.Enumerator {


public Enumerator (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.EnumeratorResult(__eval.__getCtx(), this.getPattern().__evaluate(__eval.__getPe()), this.getExpression());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();
		try {
			org.rascalmpl.interpreter.matching.IBooleanResult gen = __eval.makeBooleanResult(this);
			gen.init();
			__eval.pushEnv();
			if(gen.hasNext() && gen.next()) {
				return org.rascalmpl.interpreter.result.ResultFactory.bool(true, __eval);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.bool(false, __eval);
		} finally {
			__eval.unwind(old);
		}
	
}

}
static public class Visit extends org.rascalmpl.ast.Expression.Visit {


public Visit (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Label __param2,org.rascalmpl.ast.Visit __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getVisit().interpret(__eval);
	
}

}
static public class Subscript extends org.rascalmpl.ast.Expression.Subscript {


public Subscript (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
			
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> expr = this.getExpression().interpret(__eval);
		int nSubs = this.getSubscripts().size();
		org.rascalmpl.interpreter.result.Result<?> subscripts[] = new org.rascalmpl.interpreter.result.Result<?>[nSubs];
		for (int i = 0; i < nSubs; i++) {
			org.rascalmpl.ast.Expression subsExpr = this.getSubscripts().get(i);
			subscripts[i] = __eval.isWildCard(subsExpr.toString()) ? null : subsExpr.interpret(__eval);
		}
		return expr.subscript(subscripts);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class GreaterThanOrEq extends org.rascalmpl.ast.Expression.GreaterThanOrEq {


public GreaterThanOrEq (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.greaterThanOrEqual(right);
	
}

}
static public class Intersection extends org.rascalmpl.ast.Expression.Intersection {


public Intersection (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.intersect(right);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Tuple extends org.rascalmpl.ast.Expression.Tuple {


public Tuple (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.TuplePattern(__eval.__getCtx(), this, __eval.visitElements(this.getElements()));
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> elements = this
		.getElements();

		org.eclipse.imp.pdb.facts.IValue[] values = new org.eclipse.imp.pdb.facts.IValue[elements.size()];
		org.eclipse.imp.pdb.facts.type.Type[] types = new org.eclipse.imp.pdb.facts.type.Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> resultElem = elements.get(i).interpret(__eval);
			types[i] = resultElem.getType();
			values[i] = resultElem.getValue();
		}

		//return makeResult(tf.tupleType(types), applyRules(vf.tuple(values)));
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().tupleType(types), __eval.__getVf().tuple(values), __eval);
	
}

}
static public class Reducer extends org.rascalmpl.ast.Expression.Reducer {


public Reducer (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3,java.util.List<org.rascalmpl.ast.Expression> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalReducer(this.getInit(), this.getResult(), this.getGenerators());
	
}

}
static public class IfThenElse extends org.rascalmpl.ast.Expression.IfThenElse {


public IfThenElse (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();
		__eval.pushEnv();

		try {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> cval = this.getCondition().interpret(__eval);
	
			if (!cval.getType().isBoolType()) {
				throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), cval.getType(), this);
			}
	
			if (cval.isTrue()) {
				return this.getThenExp().interpret(__eval);
			}
	
			return this.getElseExp().interpret(__eval);	
		} finally {
			__eval.unwind(old);
		}
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class SetAnnotation extends org.rascalmpl.ast.Expression.SetAnnotation {


public SetAnnotation (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Name __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> base = this.getExpression().interpret(__eval);
		java.lang.String annoName = org.rascalmpl.interpreter.utils.Names.name(this.getName());
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> anno = this.getValue().interpret(__eval);
		return base.setAnnotation(annoName, anno, __eval.getCurrentEnvt());
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class VariableBecomes extends org.rascalmpl.ast.Expression.VariableBecomes {


public VariableBecomes (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.interpreter.matching.IMatchingResult pat = this.getPattern().__evaluate(__eval);
		java.util.LinkedList<org.rascalmpl.ast.Name> names = new java.util.LinkedList<org.rascalmpl.ast.Name>();
		names.add(this.getName());
		org.rascalmpl.interpreter.matching.IMatchingResult var = new org.rascalmpl.interpreter.matching.QualifiedNamePattern(__eval.__getCtx(), this, org.rascalmpl.ast.ASTFactoryFactory.getASTFactory().makeQualifiedNameDefault(this.getTree(), names));
		return new org.rascalmpl.interpreter.matching.VariableBecomesPattern(__eval.__getCtx(), this, var, pat);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getPattern().interpret(__eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

}
static public class Modulo extends org.rascalmpl.ast.Expression.Modulo {


public Modulo (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.modulo(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Negative extends org.rascalmpl.ast.Expression.Negative {


public Negative (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> arg = this.getArgument().interpret(__eval);
		return arg.negative();
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Subtraction extends org.rascalmpl.ast.Expression.Subtraction {


public Subtraction (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.subtract(right);

	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class TransitiveReflexiveClosure extends org.rascalmpl.ast.Expression.TransitiveReflexiveClosure {


public TransitiveReflexiveClosure (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getArgument().interpret(__eval).transitiveReflexiveClosure();
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Closure extends org.rascalmpl.ast.Expression.Closure {


public Closure (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.Parameters __param3,java.util.List<org.rascalmpl.ast.Statement> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type formals = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentEnvt(), __eval.__getHeap()).eval(this.getParameters());
		org.eclipse.imp.pdb.facts.type.Type returnType = __eval.evalType(this.getType());
		org.rascalmpl.interpreter.types.RascalTypeFactory RTF = org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance();
		return new org.rascalmpl.interpreter.result.RascalFunction(this, __eval, (org.rascalmpl.interpreter.types.FunctionType) RTF.functionType(returnType, formals), this.getParameters().isVarArgs(), this.getStatements(), __eval.getCurrentEnvt(),
					__eval.__getAccumulators());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Implication extends org.rascalmpl.ast.Expression.Implication {


public Implication (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.OrResult(__eval.__getCtx(), new org.rascalmpl.interpreter.matching.NotResult(__eval.__getCtx(), this.getLhs().__evaluate(
				__eval)), this.getRhs().__evaluate(__eval));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class GreaterThan extends org.rascalmpl.ast.Expression.GreaterThan {


public GreaterThan (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.greaterThan(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class IsDefined extends org.rascalmpl.ast.Expression.IsDefined {


public IsDefined (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		try {
			this.getArgument().interpret(__eval); // wait for exception
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(true), __eval);

		} catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			// TODO For now we __evaluate any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(false), __eval);
		}
	
}

}
static public class FieldProject extends org.rascalmpl.ast.Expression.FieldProject {


public FieldProject (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,java.util.List<org.rascalmpl.ast.Field> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO: move to result classes
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>  base = this.getExpression().interpret(__eval);

		org.eclipse.imp.pdb.facts.type.Type baseType = base.getType();
		if (!baseType.isTupleType() && !baseType.isRelationType() && !baseType.isMapType()) {
			throw new org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError("projection", baseType, this);
		}

		java.util.List<org.rascalmpl.ast.Field> fields = this.getFields();
		int nFields = fields.size();
		int selectedFields[] = new int[nFields];

		for(int i = 0 ; i < nFields; i++){
			org.rascalmpl.ast.Field f = fields.get(i);
			if (f.isIndex()) {
				selectedFields[i] = ((org.eclipse.imp.pdb.facts.IInteger) f.getFieldIndex().interpret(__eval).getValue()).intValue();
			} 
			else {
				java.lang.String fieldName = org.rascalmpl.interpreter.utils.Names.name(f.getFieldName());
				try {
					selectedFields[i] = baseType.getFieldIndex(fieldName);
				} catch (org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException e){
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(fieldName, baseType, this);
				}
			}

			if (!baseType.isMapType() && !baseType.getElementType().isVoidType()) {
				if (selectedFields[i] < 0 || selectedFields[i] > baseType.getArity()) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds(__eval.__getVf().integer(i), __eval.getCurrentAST(), __eval.getStackTrace());
				}
			}
			else if (baseType.isMapType() && selectedFields[i] < 0 || selectedFields[i] > 1) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds(__eval.__getVf().integer(i), __eval.getCurrentAST(), __eval.getStackTrace());
			}
		}

		return base.fieldSelect(selectedFields);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Equals extends org.rascalmpl.ast.Expression.Equals {


public Equals (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.equals(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class FieldAccess extends org.rascalmpl.ast.Expression.FieldAccess {


public FieldAccess (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> expr = this.getExpression().interpret(__eval);
		java.lang.String field = org.rascalmpl.interpreter.utils.Names.name(this.getField());


		return expr.fieldAccess(field, __eval.getCurrentEnvt().getStore());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Range extends org.rascalmpl.ast.Expression.Range {


public Range (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		//IListWriter w = vf.listWriter(tf.integerType());
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> from = this.getFirst().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> to = this.getLast().interpret(__eval);
		return from.makeRange(to);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Descendant extends org.rascalmpl.ast.Expression.Descendant {


public Descendant (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO: what would be the value of a descendant pattern???
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();

	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.interpreter.matching.IMatchingResult absPat = this.getPattern().__evaluate(__eval);
		return new org.rascalmpl.interpreter.matching.DescendantPattern(__eval.__getCtx(), this, absPat);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

}
static public class Addition extends org.rascalmpl.ast.Expression.Addition {


public Addition (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.add(right);

	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class FieldUpdate extends org.rascalmpl.ast.Expression.FieldUpdate {


public FieldUpdate (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Name __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> expr = this.getExpression().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> repl = this.getReplacement().interpret(__eval);
		java.lang.String name = org.rascalmpl.interpreter.utils.Names.name(this.getKey());
		return expr.fieldUpdate(name, repl, __eval.getCurrentEnvt().getStore());
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Or extends org.rascalmpl.ast.Expression.Or {


public Or (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.OrResult(__eval.__getCtx(), this.getLhs().__evaluate(__eval), this.getRhs()
				.__evaluate(__eval));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Anti extends org.rascalmpl.ast.Expression.Anti {


public Anti (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.rascalmpl.interpreter.matching.IMatchingResult absPat = this.getPattern().__evaluate(__eval);
		return new org.rascalmpl.interpreter.matching.AntiPattern(__eval.__getCtx(), this, absPat);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO: what would be the value of an anti expression???
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

}
static public class Set extends org.rascalmpl.ast.Expression.Set {


public Set (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.SetPattern(__eval.__getCtx(), this, __eval.visitElements(this.getElements()));
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> elements = this
		.getElements();

		org.eclipse.imp.pdb.facts.type.Type elementType = org.rascalmpl.interpreter.Evaluator.__getTf().voidType();
		java.util.List<org.eclipse.imp.pdb.facts.IValue> results = new java.util.ArrayList<org.eclipse.imp.pdb.facts.IValue>();

		for (org.rascalmpl.ast.Expression expr : elements) {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> resultElem = expr.interpret(__eval);
			if(resultElem.getType().isVoidType())
				throw new org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired(expr);
		
			if(resultElem.getType().isSetType() && !expr.isSet() &&
					elementType.isSubtypeOf(resultElem.getType().getElementType())){
				/*
				 * Splice the elements in the set if element types permit __eval.
				 */
				for(org.eclipse.imp.pdb.facts.IValue val : ((org.eclipse.imp.pdb.facts.ISet) resultElem.getValue())){
					elementType = elementType.lub(val.getType());
					results.add(val);
				}
			} else {
				elementType = elementType.lub(resultElem.getType());
				results.add(results.size(), resultElem.getValue());
			}
		}
		org.eclipse.imp.pdb.facts.type.Type resultType = org.rascalmpl.interpreter.Evaluator.__getTf().setType(elementType);
		org.eclipse.imp.pdb.facts.ISetWriter w = resultType.writer(__eval.__getVf());
		w.insertAll(results);
		//Was: return makeResult(resultType, applyRules(w.done()));
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(resultType, w.done(), __eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

}
static public class All extends org.rascalmpl.ast.Expression.All {


public All (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> producers = this.getGenerators();
		int size = producers.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = __eval.makeBooleanResult(producers.get(0));
			gens[0].init();
			olds[0] = __eval.getCurrentEnvt();
			__eval.pushEnv();

			while (i >= 0 && i < size) {
				if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
				if (gens[i].hasNext()) {
					if (!gens[i].next()) {
						return new org.rascalmpl.interpreter.result.BoolResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(false), __eval);
					}
					
					if(i == size - 1){
						__eval.unwind(olds[i]);
						__eval.pushEnv();
					} 
					else {
						i++;
						gens[i] = __eval.makeBooleanResult(producers.get(i));
						gens[i].init();
						olds[i] = __eval.getCurrentEnvt();
						__eval.pushEnv();
					}
				} else {
					__eval.unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			__eval.unwind(old);
		}
		
		return new org.rascalmpl.interpreter.result.BoolResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(true), __eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class TypedVariableBecomes extends org.rascalmpl.ast.Expression.TypedVariableBecomes {


public TypedVariableBecomes (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.Name __param3,org.rascalmpl.ast.Expression __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getPattern().interpret(__eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError(this.toString(), this.getLocation());
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type type = new org.rascalmpl.interpreter.TypeEvaluator(__eval.__getCtx().getCurrentEnvt(), __eval.__getCtx().getHeap()).eval(this.getType());
		org.rascalmpl.interpreter.matching.IMatchingResult pat = this.getPattern().__evaluate(__eval);
		org.rascalmpl.interpreter.matching.IMatchingResult var = new org.rascalmpl.interpreter.matching.TypedVariablePattern(__eval.__getCtx(), this, type, this.getName());
		return new org.rascalmpl.interpreter.matching.VariableBecomesPattern(__eval.__getCtx(), this, var, pat);
	
}

}
static public class In extends org.rascalmpl.ast.Expression.In {


public In (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return right.in(left);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class GetAnnotation extends org.rascalmpl.ast.Expression.GetAnnotation {


public GetAnnotation (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> base = this.getExpression().interpret(__eval);
		java.lang.String annoName = org.rascalmpl.interpreter.utils.Names.name(this.getName());
		return base.getAnnotation(annoName, __eval.getCurrentEnvt());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Negation extends org.rascalmpl.ast.Expression.Negation {


public Negation (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.NotResult(__eval.__getCtx(), this.getArgument().__evaluate(__eval));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.NotPattern(__eval.__getCtx(), this, this.getArgument().__evaluate(__eval));
	
}

}
static public class It extends org.rascalmpl.ast.Expression.It {


public It (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> v = __eval.getCurrentEnvt().getVariable(org.rascalmpl.interpreter.Evaluator.IT);
		if (v == null) {
			throw new org.rascalmpl.interpreter.staticErrors.ItOutsideOfReducer(this);
		}
		return v;
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Composition extends org.rascalmpl.ast.Expression.Composition {


public Composition (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.compose(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Join extends org.rascalmpl.ast.Expression.Join {


public Join (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.join(right);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class LessThanOrEq extends org.rascalmpl.ast.Expression.LessThanOrEq {


public LessThanOrEq (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.lessThanOrEqual(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Bracket extends org.rascalmpl.ast.Expression.Bracket {


public Bracket (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return this.getExpression().__evaluate(__eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return this.getExpression().__evaluate(__eval);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getExpression().interpret(__eval);
	
}

}
static public class IfDefinedOtherwise extends org.rascalmpl.ast.Expression.IfDefinedOtherwise {


public IfDefinedOtherwise (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		try {
			return this.getLhs().interpret(__eval);
		}
		catch (org.rascalmpl.interpreter.staticErrors.UninitializedVariableError e){
			return this.getRhs().interpret(__eval);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			// TODO For now we __evaluate any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return this.getRhs().interpret(__eval);
		}
	
}

}
static public class Literal extends org.rascalmpl.ast.Expression.Literal {


public Literal (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Literal __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return this.getLiteral().__evaluate(__eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		if (this.getLiteral().isBoolean()) {
			return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
		}
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getLiteral().interpret(__eval);
	
}

}
static public class Product extends org.rascalmpl.ast.Expression.Product {


public Product (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.multiply(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Expression.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Ambiguity in expression: " + this);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
	
}

}
static public class Division extends org.rascalmpl.ast.Expression.Division {


public Division (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.divide(right);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(__eval.__getTf().boolType(), this.interpret(
				__eval.__getCtx().getEvaluator()).getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class NonEmptyBlock extends org.rascalmpl.ast.Expression.NonEmptyBlock {


public NonEmptyBlock (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Statement> __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.ASTFactory factory = org.rascalmpl.ast.ASTFactoryFactory.getASTFactory();
		return factory.makeStatementNonEmptyBlock(this.getTree(), factory.makeLabelEmpty(this.getTree()), this.getStatements()).interpret(__eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Any extends org.rascalmpl.ast.Expression.Any {


public Any (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> generators = this.getGenerators();
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];

		int i = 0;
		gens[0] = __eval.makeBooleanResult(generators.get(0));
		gens[0].init();
		while (i >= 0 && i < size) {
			if (__eval.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(__eval.getStackTrace());
			if (gens[i].hasNext() && gens[i].next()) {
				if (i == size - 1) {
					return new org.rascalmpl.interpreter.result.BoolResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(true), __eval);
				}

				i++;
				gens[i] = __eval.makeBooleanResult(generators.get(i));
				gens[i].init();
			} else {
				i--;
			}
		}
		return new org.rascalmpl.interpreter.result.BoolResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(false), __eval);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class Equivalence extends org.rascalmpl.ast.Expression.Equivalence {


public Equivalence (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalBooleanExpression(this);
	
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.EquivalenceResult(__eval.__getCtx(), this.getLhs().__evaluate(__eval), this.getRhs().__evaluate(__eval));
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
static public class NonEquals extends org.rascalmpl.ast.Expression.NonEquals {


public NonEquals (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IBooleanResult __evaluate(org.rascalmpl.interpreter.BooleanEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.BasicBooleanResult(__eval.__getCtx(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> left = this.getLhs().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> right = this.getRhs().interpret(__eval);
		return left.nonEquals(right);
	
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError(this.toString(), this);
	
}

}
}