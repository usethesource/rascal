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
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.AbstractAST;
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
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredAnnotationError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
/*package*/ class AssignableEvaluator extends NullASTVisitor<Result<IValue>>{
	enum AssignmentOperator {Default, Addition, Subtraction, Product, Division, Intersection, IsDefined}
	private AssignmentOperator operator;
    private Result<IValue> value;
    private final Environment env;
    private final IEvaluator<Result<IValue>> eval;
	private static final TypeFactory tf = TypeFactory.getInstance();
    
	public AssignableEvaluator(Environment env, Assignment operator, Result<IValue> value, IEvaluator<Result<IValue>> eval) {
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
	private Result<IValue> newResult(Result<IValue> oldValue, Result<IValue> rhsValue) {
		Result<IValue> newValue;
		if(oldValue != null){
			switch(operator){
			case Default:
				newValue = rhsValue; break;
			case Addition:
				newValue = oldValue.add(rhsValue); break;
			case Subtraction:
				newValue = oldValue.subtract(rhsValue); break;
			case Product:
				newValue = oldValue.multiply(rhsValue); break;
			case Division:
				newValue = oldValue.divide(rhsValue); break;
			case Intersection:
				newValue = oldValue.intersect(rhsValue); break;
			case IsDefined:
				return oldValue;
			default:
				throw new ImplementationError("Unknown assignment operator");
			}
		
			if (newValue.getType().isSubtypeOf(oldValue.getType())) {
				newValue = makeResult(oldValue.getType(), newValue.getValue(),eval);
				return newValue;
			} else 	if (oldValue.hasInferredType()) {
				// Be liberal here: if the user has not declared a variable explicitly
				// we use the lub type for the new value.
				newValue = makeResult(oldValue.getType().lub(newValue.getType()), newValue.getValue(),eval);
				newValue.setInferredType(true);
				return newValue;
			}
			
			// we use rhs value here, because the addition (for example) implicitly casts up to value,
			// in which case the error is lost. Since we know that the left hand side of the addition
			// is always the variable we are updating, the cause of the error must always be in the value
			// on the right hand side
			throw new UnexpectedTypeError(oldValue.getType(), rhsValue.getType(), eval.getCurrentAST());
		}
		switch(operator){
			case Default:
			case IsDefined:
				return rhsValue;
			default:
				throw new UninitializedVariableError("assignment operator", eval.getCurrentAST());
		}
	}
	
	private Result<IValue> newResult(IValue oldValue, Result<IValue> rhsValue){
		if (oldValue != null){
			Result<IValue> res = makeResult(oldValue.getType().lub(rhsValue.getType()), oldValue, eval);
			return newResult(res, rhsValue);
		}
		switch(operator){
		case Default:
		case IsDefined:
				return rhsValue;
		default:
			throw new UninitializedVariableError("assignment operator", eval.getCurrentAST());
		}
	}
	
	@Override
	public Result<IValue> visitAssignableVariable(Variable x) {
		QualifiedName qname = x.getQualifiedName();
		Result<IValue> previous = env.getVariable(qname);
		
		//System.out.println("I am assigning: " + x + "(oldvalue = " + previous + ")");
		
		if(previous != null){
			value = newResult(previous, value);
			env.storeVariable(qname, value);
			return value;
		}
		
		switch(operator){
		case Default:
		case IsDefined:
				//System.out.println("And it's local: " + x);
//				env.storeLocalVariable(qname, value); ?? OOPS 
//				env.declareVariable(value.getType(), qname.toString());
				env.storeVariable(qname, value);
				return value;
		default:
			throw new UninitializedVariableError(x.toString(), x);
		}
		
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
			value = newResult(result.getAnnotation(label, env), value);
		} catch (Throw e){
			// NoSuchAnnotation
		}
		return recur(x, result.setAnnotation(label, value, env));
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
				result = makeResult(rec.hasInferredType() ? rec.getType().lub(list.getType()) : rec.getType(), list, eval);
			}  
			catch (java.lang.IndexOutOfBoundsException e){
				throw RuntimeExceptionFactory.indexOutOfBounds((IInteger) subscript.getValue(), eval.getCurrentAST(), eval.getStackTrace());
			}
		}
		else if (rec.getType().isMapType()) {
			Type keyType = rec.getType().getKeyType();
			
			if (rec.hasInferredType() || subscript.getType().isSubtypeOf(keyType)) {
				IValue oldValue = ((IMap) rec.getValue()).get(subscript.getValue());
				value = newResult(oldValue, value);
				IMap map = ((IMap) rec.getValue()).put(subscript.getValue(), value.getValue());
				result = makeResult(rec.hasInferredType() ? rec.getType().lub(map.getType()) : rec.getType(), map, eval);
			}
			else {
				throw new UnexpectedTypeError(keyType, subscript.getType(), x.getSubscript());
			}
			
		} else if (rec.getType().isNodeType() && subscript.getType().isIntegerType()) {
			int index = ((IInteger) subscript.getValue()).intValue();
			INode node = (INode) rec.getValue();
			
			if(index >= node.arity()){
				throw RuntimeExceptionFactory.indexOutOfBounds((IInteger) subscript.getValue(), eval.getCurrentAST(), eval.getStackTrace());
			}
			value = newResult(node.get(index), value);
			node = node.set(index, value.getValue());
			result = makeResult(rec.getType(), node, eval);
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
		try {
			x.getReceiver().accept(eval); // notice we use 'eval' here not 'this'
			// if it was not defined, this would have thrown an exception, so now we can just go on
			return x.getReceiver().accept(this);
		}
		catch (Throw e) {
			value = newResult(x.getDefaultExpression().accept(eval), value);
			operator = AssignmentOperator.Default;
			return x.getReceiver().accept(this);
		}
	}
	
	@Override
	public Result<IValue> visitAssignableFieldAccess(FieldAccess x) {
		Result<IValue> receiver = x.getReceiver().accept(eval);
		String label = x.getField().toString();
		
		if(receiver == null || receiver.getValue() == null) {
			throw new UninitializedVariableError(x.getReceiver().toString(), x.getReceiver());
		}
		
		if (receiver.getType().isTupleType()) {
			
			int idx = receiver.getType().getFieldIndex(label);
			if (idx < 0) {
				throw new UndeclaredFieldError(label, receiver.getType(), x);
			}
			
			value = newResult(((ITuple) receiver.getValue()).get(idx), value);
			IValue result = ((ITuple) receiver.getValue()).set(idx, value.getValue());
			return recur(x, makeResult(receiver.getType(), result, eval));
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
			return recur(x, makeResult(receiver.getType(), result, eval));
		}
		else if (receiver.getType().isSourceLocationType()){
//			ISourceLocation loc = (ISourceLocation) receiver.getValue();
			
			value = newResult(receiver.fieldAccess(label, env.getStore()), value);
			return recur(x, receiver.fieldUpdate(label, value, env.getStore()));
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
			throw new UnexpectedTypeError(tf.tupleEmpty(), value.getType(), x);
		}
		
		Type tupleType = value.getType();
		ITuple tuple = (ITuple) value.getValue();
		IValue[] results = new IValue[arguments.size()];
		Type [] resultTypes = new Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			Type argType = tupleType.getFieldType(i);
			IValue arg = tuple.get(i);
			Result<IValue> result = makeResult(argType, arg, eval);
			AssignableEvaluator ae = new AssignableEvaluator(env,null, result, eval);
			Result<IValue> argResult = arguments.get(i).accept(ae);
			results[i] = argResult.getValue();
			resultTypes[i] = argResult.getType();
		}
		
		return makeResult(tf.tupleType(resultTypes), tupleType.make(eval.getValueFactory(), results), eval);
	}
	
	
	@Override
	public Result<IValue> visitAssignableConstructor(Constructor x) {
		throw new ImplementationError("Constructor assignables not yet implemented");
	}
	
	public AbstractAST getCurrentAST() {
		return eval.getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return eval.getCurrentEnvt();
	}

	public Evaluator getEvaluator() {
		return eval.getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return eval.getHeap();
	}

	public java.lang.String getStackTrace() {
		return eval.getStackTrace();
	}

	public void pushEnv() {
		eval.pushEnv();		
	}

	public void runTests() {
		eval.runTests();
	}

	public void setCurrentEnvt(Environment environment) {
		eval.setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		eval.unwind(old);
	}

}
