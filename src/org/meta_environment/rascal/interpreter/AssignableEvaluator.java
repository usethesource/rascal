package org.meta_environment.rascal.interpreter;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Assignable;
import org.meta_environment.rascal.ast.Assignment;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Assignable.Annotation;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault;
import org.meta_environment.rascal.ast.Assignable.Subscript;
import org.meta_environment.rascal.ast.Assignable.Tuple;
import org.meta_environment.rascal.ast.Assignable.Variable;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredAnnotationError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptError;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
/*package*/ class AssignableEvaluator extends NullASTVisitor<Result<IValue>> {
	enum AssignmentOperator {Default, Addition, Subtraction, Product, Division, Intersection, IsDefined};
	private AssignmentOperator operator;
    private Result<IValue> value;
    private final Environment env;
    private final Evaluator eval;
 
    
	public AssignableEvaluator(Environment env, Assignment operator, Result<IValue> value, Evaluator eval) {
		if(operator == null || operator.isDefault())
			this.operator = AssignmentOperator.Default;
		else if(operator.isAddition())
			this.operator = AssignmentOperator.Addition;
		else if(operator.isSubtraction())
			this.operator = AssignmentOperator.Subtraction;
		else if(operator.isProduct())
			this.operator = AssignmentOperator.Product;
		else if(operator.isDivision())
			this.operator = AssignmentOperator.Division;
		else if(operator.isIntersection())
			this.operator = AssignmentOperator.Intersection;
		else if(operator.isIfDefined())
			this.operator = AssignmentOperator.IsDefined;
		else
				throw new ImplementationError("Unknown assignment operator");
		this.value = value;
		this.env = env;
		this.eval = eval;
	} 
	
	/*
	 * Given an old result and a right-hand side Result, compute a new result.
	 */
	private Result<IValue> newResult(Result<IValue> oldValue, Result<IValue> rhsValue){
		Result<IValue> newValue;
		if(oldValue != null){
			switch(operator){
			case Default:
				newValue = rhsValue; break;
			case Addition:
				newValue = oldValue.add(rhsValue, eval.getCurrentAST()); break;
			case Subtraction:
				newValue = oldValue.subtract(rhsValue, eval.getCurrentAST()); break;
			case Product:
				newValue = oldValue.multiply(rhsValue, eval.getCurrentAST()); break;
			case Division:
				newValue = oldValue.divide(rhsValue, eval.getCurrentAST()); break;
			case Intersection:
				newValue = oldValue.intersect(rhsValue, eval.getCurrentAST()); break;
			case IsDefined:
				return oldValue;
			default:
				throw new ImplementationError("Unknown assignment operator");
			}
		
			if (newValue.getType().isSubtypeOf(oldValue.getType())) {
				newValue.setType(oldValue.getType());
				return newValue;
			} else {
				// TODO: I don't think this check uses static types only.
				throw new UnexpectedTypeError(oldValue.getType(), newValue.getType(),eval.getCurrentAST());
			}
		}
		switch(operator){
		case Default:
		case IsDefined:
				return rhsValue;
		}
		throw new UninitializedVariableError("assignment operator", eval.getCurrentAST());
	}
	
	private Result<IValue> newResult(IValue oldValue, Result<IValue> rhsValue){
		if(oldValue != null){
			Result<IValue> res = makeResult(oldValue.getType(), oldValue);
			return newResult(res, rhsValue);
		}
		switch(operator){
		case Default:
		case IsDefined:
				return rhsValue;
		}
		throw new UninitializedVariableError("assignment operator", eval.getCurrentAST());
	}
	
	@Override
	public Result<IValue> visitAssignableVariable(Variable x) {
		QualifiedName qname = x.getQualifiedName();
		Result<IValue> previous = env.getLocalVariable(qname);
		
		if(previous != null){
			value = newResult(previous, value);
			env.storeLocalVariable(qname, value);
			return value;
		}
		previous = env.getVariable(qname);
		if(previous != null){
			value = newResult(previous, value);
			env.storeVariable(qname, value);
			return value;
		}
		
		switch(operator){
		case Default:
		case IsDefined:
				env.storeLocalVariable(qname, value);
				return value;
		}
		throw new UninitializedVariableError(x.toString(), x);
		
		// TODO implement semantics of global keyword, when not given the
		// variable should be inserted in the local scope.
	}
	
	@Override
	public Result<IValue> visitAssignableAnnotation(Annotation x) {
		String label = x.getAnnotation().toString();
		Result<IValue> result = x.getReceiver().accept(eval);
				
		if(result == null || result.getValue() == null)
			throw new UninitializedVariableError(x.getReceiver().toString(), x.getReceiver());
		
		if (!env.declaresAnnotation(result.getType(), label)) {
			throw new UndeclaredAnnotationError(label, result.getType(), x);
		}
		
		try {
			value = newResult(result.getAnnotation(label, env, x), value);
		} catch (Throw e){
			// NoSuchAnnotation
		}
		return recur(x, result.setAnnotation(label, value, env, x));
//		result.setValue(((IConstructor) result.getValue()).setAnnotation(label, value.getValue()));
		//return recur(x, result);
	}
	
	@Override
	public Result<IValue> visitAssignableSubscript(Subscript x) {
		Result<IValue> rec = x.getReceiver().accept(eval);
		Result<IValue> subscript = x.getSubscript().accept(eval);
		Result<IValue> result;
		
		if (rec == null || rec.getValue() == null) {
			throw new UninitializedVariableError(x.getReceiver().toString(), x.getReceiver());
		}
		
		if (rec.getType().isListType() && subscript.getType().isIntegerType()) {
			try {
				IList list = (IList) rec.getValue();
				int index = ((IInteger) subscript.getValue()).intValue();
				value = newResult(list.get(index), value);
				list = list.put(index, value.getValue());
				result = makeResult(rec.getType(), list);
			}  
			catch (java.lang.IndexOutOfBoundsException e){
				throw RuntimeExceptionFactory.indexOutOfBounds((IInteger) subscript.getValue(), eval.getCurrentAST());
			}
		}
		else if (rec.getType().isMapType()) {
			Type keyType = rec.getType().getKeyType();
			
			if (subscript.getType().isSubtypeOf(keyType)) {
				value = newResult(((IMap) rec.getValue()).get(subscript.getValue()), value);
				IMap map = ((IMap) rec.getValue()).put(subscript.getValue(), value.getValue());
				result = makeResult(rec.getType(), map);
			}
			else {
				throw new UnexpectedTypeError(keyType, subscript.getType(), x);
			}
			
		} else if (rec.getType().isNodeType() && subscript.getType().isIntegerType()) {
			int index = ((IInteger) subscript.getValue()).intValue();
			INode node = (INode) rec.getValue();
			
			if(index >= node.arity()){
				throw RuntimeExceptionFactory.indexOutOfBounds((IInteger) subscript.getValue(), eval.getCurrentAST());
			}
			value = newResult(node.get(index), value);
			node = node.set(index, value.getValue());
			result = makeResult(rec.getType(), node);
		} else {
			throw new UnsupportedSubscriptError(rec.getType(), subscript.getType(), x);
			// TODO implement other subscripts
		}
		
		return recur(x, result);
	}

	private Result<IValue> recur(Assignable x, Result<IValue> result) {
		return x.getReceiver().accept(new AssignableEvaluator(env, null, result, eval));
	}
	
	@Override
	public Result<IValue> visitAssignableIfDefinedOrDefault(IfDefinedOrDefault x) {
		Result<IValue> cond = x.getDefaultExpression().accept(eval);
		
		if (((IBool) cond.getValue()).getValue()) {
			return x.getReceiver().accept(this);
		}
		else {
			return x.getReceiver().accept(eval);
		}
	}
	
	@Override
	public Result<IValue> visitAssignableFieldAccess(FieldAccess x) {
		Result<IValue> receiver = x.getReceiver().accept(eval);
		String label = x.getField().toString();
		
		if(receiver == null || receiver.getValue() == null)
			throw new UninitializedVariableError(x.getReceiver().toString(), x.getReceiver());
		if (receiver.getType().isTupleType()) {
			if (!receiver.getType().hasField(label)) {
				throw new UndeclaredFieldError(label, receiver.getType(), x);
			}
			value = newResult(((ITuple) receiver.getValue()).get(label), value);
			IValue result = ((ITuple) receiver.getValue()).set(label, value.getValue());
			return recur(x, makeResult(receiver.getType(), result));
		}
		else if (receiver.getType().isConstructorType() || receiver.getType().isAbstractDataType()) {
			IConstructor cons = (IConstructor) receiver.getValue();
			Type node = cons.getConstructorType();
			
			/* TODO: remove?
			if (!receiver.getType().hasField(label)) {
				throw new NoSuchFieldError(receiver.getType() + " does not have a field named `" + label + "`", x);
			}
			*/

			if (!node.hasField(label)) {
				throw new UndeclaredFieldError(label, receiver.getValue().getType() , x);
			}
			
			int index = node.getFieldIndex(label);
			
			if (!value.getType().isSubtypeOf(node.getFieldType(index))) {
				throw new UnexpectedTypeError(node.getFieldType(index), value.getType(), x);
			}
			value = newResult(cons.get(index), value);
			
			IValue result = cons.set(index, value.getValue());
			return recur(x, makeResult(receiver.getType(), result));
		}
		else if (receiver.getType().isSourceLocationType()){
//			ISourceLocation loc = (ISourceLocation) receiver.getValue();
			
			value = newResult(receiver.fieldAccess(label, env.getStore(), x), value);
			return recur(x, receiver.fieldUpdate(label, value, env.getStore(), x));
			//return recur(x, eval.sourceLocationFieldUpdate(loc, label, value.getValue(), value.getType(), x));
		}
		else {
			throw new UndeclaredFieldError(label, receiver.getType(), x);
		}

	}
	
	@Override
	public Result<IValue> visitAssignableTuple(Tuple x) {
		java.util.List<Assignable> arguments = x.getElements();
		
		if (!value.getType().isTupleType()) {
			// TODO construct a better expected type
			throw new UnexpectedTypeError(eval.tf.tupleEmpty(), value.getType(), x);
		}
		
		Type tupleType = value.getType();
		ITuple tuple = (ITuple) value.getValue();
		IValue[] results = new IValue[arguments.size()];
		Type [] resultTypes = new Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			Type argType = tupleType.getFieldType(i);
			IValue arg = tuple.get(i);
			Result<IValue> result = makeResult(argType, arg);
			AssignableEvaluator ae = new AssignableEvaluator(env,null, result, eval);
			Result<IValue> argResult = arguments.get(i).accept(ae);
			results[i] = argResult.getValue();
			resultTypes[i] = argResult.getType();
		}
		
		return makeResult(eval.tf.tupleType(resultTypes), tupleType.make(eval.vf, results));
	}
	
	
	@Override
	public Result<IValue> visitAssignableConstructor(Constructor x) {
		throw new ImplementationError("Constructor assignables not yet implemented");
	}
}
