package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Assignable;
import org.meta_environment.rascal.ast.Assignment;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Assignable.Annotation;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.Assignable.IfDefined;
import org.meta_environment.rascal.ast.Assignable.Subscript;
import org.meta_environment.rascal.ast.Assignable.Tuple;
import org.meta_environment.rascal.ast.Assignable.Variable;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.exceptions.AssignmentException;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;
import org.meta_environment.rascal.interpreter.exceptions.IndexOutOfBoundsException;
import org.meta_environment.rascal.interpreter.exceptions.NoSuchAnnotationException;
import org.meta_environment.rascal.interpreter.exceptions.NoSuchFieldException;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.SubscriptException;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;
import org.meta_environment.rascal.interpreter.exceptions.UndefinedValueException;
import org.meta_environment.rascal.interpreter.exceptions.UninitializedVariableException;
import org.meta_environment.rascal.interpreter.result.Result;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
/*package*/ class AssignableEvaluator extends NullASTVisitor<Result> {
	enum AssignmentOperator {Default, Addition, Subtraction, Product, Division, Intersection};
	private AssignmentOperator operator;
    private Result value;
    private final Environment env;
    private final Evaluator eval;
 
    
	public AssignableEvaluator(Environment env, Assignment operator, Result value, Evaluator eval) {
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
		else
				throw new ImplementationException("Unknown assignment operator");
		this.value = value;
		this.env = env;
		this.eval = eval;
	}  
	
	@Override
	public Result visitAssignableVariable(Variable x) {
		String name = x.getQualifiedName().toString();
		Result previous = env.getVariable(x.getQualifiedName(), name);
		
		if (previous != null) {
			if (value.getType().isSubtypeOf(previous.getType())) {
				value.setType(previous.getType());
			} else {
				throw new AssignmentException("Variable `" + name
						+ "` has type " + previous.getType()
						+ "; cannot assign value of type " + value.getType(), x);
			}
			if(operator == AssignmentOperator.Default){
				env.storeVariable(name, value);
			} else {
				//TODO add the various operators here.
			}
		}
		else {
			if(operator == AssignmentOperator.Default)
				env.storeVariable(name, value);
			else {
				throw new UndefinedValueException("Variable needs previous value for assignment operator " + operator, x);
			}
		}
		
		// TODO implement semantics of global keyword, when not given the
		// variable should be inserted in the local scope.
		return value;
	}
	
	@Override
	public Result visitAssignableAnnotation(Annotation x) {
		String label = x.getAnnotation().toString();
		Result result = x.getReceiver().accept(eval);
		
		if(result == null || result.getValue() == null)
			throw new UninitializedVariableException(x.getReceiver().toString(), x.getReceiver());
		
		if (!env.declaresAnnotation(result.getType(), label)) {
			throw new NoSuchAnnotationException("No annotation `" + label + "` declared for " + result.getType(), x);
		}
		
		result.setValue(((IConstructor) result.getValue()).setAnnotation(label, value.getValue()));
		
		return recur(x, result);
	}
	
	@Override
	public Result visitAssignableSubscript(Subscript x) {
		Result rec = x.getReceiver().accept(eval);
		Result subscript = x.getSubscript().accept(eval);
		Result result;
		
		if(rec == null || rec.getValue() == null)
				throw new UninitializedVariableException(x.getReceiver().toString(), x.getReceiver());
		if (rec.getType().isListType() && subscript.getType().isIntegerType()) {
			try {
			IList list = (IList) rec.getValue();
			int index = ((IInteger) subscript.getValue()).getValue();
			list = list.put(index, value.getValue());
			result = eval.result(rec.getType(), list);
			}  catch (RascalException e){
				throw e;
			}
			catch (Exception e){
				throw new IndexOutOfBoundsException("Index " + ((IInteger) subscript.getValue()).getValue() + " out of bounds", x);
			}
		}
		else if (rec.getType().isMapType()) {
			Type keyType = rec.getType().getKeyType();
			
			if (subscript.getType().isSubtypeOf(keyType)) {
				IMap map = ((IMap) rec.getValue()).put(subscript.getValue(), value.getValue());
				result = eval.result(rec.getType(), map);
			}
			else {
				throw new TypeErrorException("Key type " + keyType + " of map is not compatible with " + subscript.getType(), x);
			}
			
		} else if (rec.getType().isNodeType() && subscript.getType().isIntegerType()) {
			int index = ((IInteger) subscript.getValue()).getValue();
			INode node = (INode) rec.getValue();
			
			if(index >= node.arity()){
				throw new IndexOutOfBoundsException("Subscript out of bounds", x);
			}
			node = node.set(index, value.getValue());
			result = eval.result(rec.getType(), node);
		} else {
			throw new TypeErrorException("Receiver " + rec.getType() + " is incompatible with subscript " + subscript.getType(), x);
			// TODO implement other subscripts
		}
		
		return recur(x, result);
	}

	private Result recur(Assignable x, Result result) {
		return x.getReceiver().accept(new AssignableEvaluator(env, null, result, eval));
	}
	
	@Override
	public Result visitAssignableIfDefined(IfDefined x) {
		Result cond = x.getCondition().accept(eval);
		
		if (((IBool) cond.getValue()).getValue()) {
			return x.getReceiver().accept(this);
		}
		else {
			return x.getReceiver().accept(eval);
		}
	}
	
	@Override
	public Result visitAssignableFieldAccess(FieldAccess x) {
		Result receiver = x.getReceiver().accept(eval);
		String label = x.getField().toString();
		
		if(receiver == null || receiver.getValue() == null)
			throw new UninitializedVariableException(x.getReceiver().toString(), x.getReceiver());
		if (receiver.getType().isTupleType()) {
			if (!receiver.getType().hasField(label)) {
				throw new NoSuchFieldException(receiver.getType() + " does not have a field named `" + label + "`", x);
			}
			IValue result = ((ITuple) receiver.getValue()).set(label, value.getValue());
			return recur(x, eval.result(receiver.getType(), result));
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
				throw new NoSuchFieldException("Field `" + label + "` accessed on constructor that does not have it." + receiver.getValue().getType(), x);
			}
			
			int index = node.getFieldIndex(label);
			try {
				IValue result = cons.set(index, value.getValue());
				return recur(x, eval.result(receiver.getType(), result));
			} catch(Exception e){
				throw new AssignmentException(label, x);
			}
		}
		else if(receiver.getType().isSourceLocationType()){
			ISourceLocation loc = (ISourceLocation) receiver.getValue();
			
			return recur(x, eval.sourceLocationFieldUpdate(loc, label, value.getValue(), x));
		}
		else {
			throw new NoSuchFieldException(x.getReceiver() + " has no field named `" + label + "`", x);
		}

	}
	
	@Override
	public Result visitAssignableTuple(Tuple x) {
		java.util.List<Assignable> arguments = x.getElements();
		
		if (!value.getType().isTupleType()) {
			throw new AssignmentException("Receiver is a tuple, but the assigned value is not: " + value.getType(), x); 
		}
		
		Type tupleType = value.getType();
		ITuple tuple = (ITuple) value.getValue();
		IValue[] results = new IValue[arguments.size()];
		Type [] resultTypes = new Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			Type argType = tupleType.getFieldType(i);
			IValue arg = tuple.get(i);
			Result result = eval.result(argType, arg);
			AssignableEvaluator ae = new AssignableEvaluator(env,null, result, eval);
			Result argResult = arguments.get(i).accept(ae);
			results[i] = argResult.getValue();
			resultTypes[i] = argResult.getType();
		}
		
		return eval.result(eval.tf.tupleType(resultTypes), tupleType.make(eval.vf, results));
	}
	
	
	@Override
	public Result visitAssignableConstructor(Constructor x) {
		throw new ImplementationException("Constructor assignables not yet implemented", x);
	}
}
