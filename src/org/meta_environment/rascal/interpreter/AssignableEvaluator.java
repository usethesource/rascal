package org.meta_environment.rascal.interpreter;

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
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Assignable.Annotation;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.Assignable.IfDefined;
import org.meta_environment.rascal.ast.Assignable.Subscript;
import org.meta_environment.rascal.ast.Assignable.Tuple;
import org.meta_environment.rascal.ast.Assignable.Variable;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.Result;
import org.meta_environment.rascal.interpreter.errors.AssignmentError;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.IndexOutOfBoundsError;
import org.meta_environment.rascal.interpreter.errors.NoSuchAnnotationError;
import org.meta_environment.rascal.interpreter.errors.NoSuchFieldError;
import org.meta_environment.rascal.interpreter.errors.TypeError;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
/*package*/ class AssignableEvaluator extends NullASTVisitor<Result> {
    private Result value;
    private final Environment env;
    private final Evaluator eval;
    
	public AssignableEvaluator(Environment env, Result value, Evaluator eval) {
		this.value = value;
		this.env = env;
		this.eval = eval;
	}  
	
	@Override
	public Result visitAssignableVariable(Variable x) {
		String name = x.getQualifiedName().toString();
		Result previous = env.getVariable(name);
		
		if (previous != null) {
			if (value.type.isSubtypeOf(previous.type)) {
				value.type = previous.type;
			} else {
				throw new AssignmentError("Variable `" + name
						+ "` has type " + previous.type
						+ "; cannot assign value of type " + value.type, x);
			}
			
			env.storeVariable(name, value);
		}
		else {
			env.storeVariable(name, value);
		}
		
		// TODO implement semantics of global keyword, when not given the
		// variable should be inserted in the local scope.
		return value;
	}
	
	@Override
	public Result visitAssignableAnnotation(Annotation x) {
		String label = x.getAnnotation().toString();
		Result result = x.getReceiver().accept(eval);
		
		if (!result.type.declaresAnnotation(label)) {
			throw new NoSuchAnnotationError("No annotation `" + label + "` declared for " + result.type, x);
		}
		
		result.value = ((IConstructor) result.value).setAnnotation(label, value.value);
		
		return recur(x, result);
	}
	
	@Override
	public Result visitAssignableSubscript(Subscript x) {
		Result rec = x.getReceiver().accept(eval);
		Result subscript = x.getSubscript().accept(eval);
		Result result;
		
		if (rec.type.isListType() && subscript.type.isIntegerType()) {
			try {
			IList list = (IList) rec.value;
			int index = ((IInteger) subscript.value).getValue();
			list = list.put(index, value.value);
			result = eval.result(rec.type, list);
			} catch (Exception e){
				throw new IndexOutOfBoundsError("Index " + ((IInteger) subscript.value).getValue() + " out of bounds", x);
			}
		}
		else if (rec.type.isMapType()) {
			Type keyType = rec.type.getKeyType();
			
			if (subscript.type.isSubtypeOf(keyType)) {
				IMap map = ((IMap) rec.value).put(subscript.value, value.value);
				result = eval.result(rec.type, map);
			}
			else {
				throw new TypeError("Key type " + keyType + " of map is not compatible with " + subscript.type, x);
			}
			
		} else if (rec.type.isNodeType() && subscript.type.isIntegerType()) {
			int index = ((IInteger) subscript.value).getValue();
			INode node = (INode) rec.value;
			
			if(index >= node.arity()){
				throw new IndexOutOfBoundsError("Subscript out of bounds", x);
			}
			node = node.set(index, value.value);
			result = eval.result(rec.type, node);
		} else {
			throw new TypeError("Receiver " + rec.type + " is incompatible with subscript " + subscript.type, x);
			// TODO implement other subscripts
		}
		
		return recur(x, result);
	}

	private Result recur(Assignable x, Result result) {
		return x.getReceiver().accept(new AssignableEvaluator(env, result, eval));
	}
	
	@Override
	public Result visitAssignableIfDefined(IfDefined x) {
		Result cond = x.getCondition().accept(eval);
		
		if (((IBool) cond.value).getValue()) {
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
		
		if (receiver.type.isTupleType()) {
			if (!receiver.type.hasField(label)) {
				throw new NoSuchFieldError(receiver.type + " does not have a field named `" + label + "`", x);
			}
			IValue result = ((ITuple) receiver.value).set(label, value.value);
			return recur(x, eval.result(receiver.type, result));
		}
		else if (receiver.type.isConstructorType() || receiver.type.isAbstractDataType()) {
			IConstructor cons = (IConstructor) receiver.value;
			Type node = cons.getConstructorType();
			
			if (!receiver.type.hasField(label)) {
				throw new NoSuchFieldError(receiver.type + " does not have a field named `" + label + "`", x);
			}
			
			if (!node.hasField(label)) {
				throw new NoSuchFieldError("Field `" + label + "` accessed on constructor that does not have it." + receiver.value.getType(), x);
			}
			
			int index = node.getFieldIndex(label);
			
			IValue result = cons.set(index, value.value);
			return recur(x, eval.result(receiver.type, result));
		}
		else {
			throw new NoSuchFieldError(x.getReceiver() + " has no field named `" + label + "`", x);
		}
		
	}
	
	@Override
	public Result visitAssignableTuple(Tuple x) {
		java.util.List<Assignable> arguments = x.getElements();
		
		if (!value.type.isTupleType()) {
			throw new AssignmentError("Receiver is a tuple, but the assigned value is not: " + value.type, x); 
		}
		
		Type tupleType = value.type;
		ITuple tuple = (ITuple) value.value;
		IValue[] results = new IValue[arguments.size()];
		Type [] resultTypes = new Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			Type argType = tupleType.getFieldType(i);
			IValue arg = tuple.get(i);
			Result result = eval.result(argType, arg);
			AssignableEvaluator ae = new AssignableEvaluator(env,result, eval);
			Result argResult = arguments.get(i).accept(ae);
			results[i] = argResult.value;
			resultTypes[i] = argResult.type;
		}
		
		return eval.result(eval.tf.tupleType(resultTypes), tupleType.make(eval.vf, results));
	}
	
	@Override
	public Result visitAssignableConstructor(Constructor x) {
		throw new ImplementationError("Constructor assignables not yet implemented", x);
	}
}
