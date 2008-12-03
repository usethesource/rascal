package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.MapType;
import org.eclipse.imp.pdb.facts.type.TupleType;
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

/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
/*package*/ class AssignableEvaluator extends NullASTVisitor<EvalResult> {
    private EvalResult value;
    private final EnvironmentStack env;
    private final Evaluator eval;
    
	public AssignableEvaluator(EnvironmentStack env, EvalResult value, Evaluator eval) {
		this.value = value;
		this.env = env;
		this.eval = eval;
	}  
	
	@Override
	public EvalResult visitAssignableVariable(Variable x) {
		env.storeVariable(x.getQualifiedName().toString(), value);
		return value;
	}
	
	@Override
	public EvalResult visitAssignableAnnotation(Annotation x) {
		String label = x.getAnnotation().toString();
		EvalResult result = x.getReceiver().accept(eval);
		
		result.value = result.value.setAnnotation(label, value.value);
		
		return recur(x, result);
	}
	
	@Override
	public EvalResult visitAssignableSubscript(Subscript x) {
		EvalResult rec = x.getReceiver().accept(eval);
		EvalResult subscript = x.getSubscript().accept(eval);
		EvalResult result;
		
		if (rec.type.getBaseType().isListType() && subscript.type.getBaseType().isIntegerType()) {
			IList list = (IList) rec.value;
			int index = ((IInteger) subscript.value).getValue();
			list = list.put(index, value.value);
			result = eval.result(rec.type, list);
		}
		else if (rec.type.getBaseType().isMapType()) {
			Type keyType = ((MapType) rec.type).getKeyType();
			
			if (subscript.type.isSubtypeOf(keyType)) {
				IMap map = ((IMap) rec.value).put(subscript.value, value.value);
				result = eval.result(rec.type, map);
			}
			else {
				throw new RascalTypeError("Key type " + keyType + " of map is not compatible with " + subscript.type);
			}
		} else {
			throw new RascalTypeError("Receiver " + rec.type + " is incompatible with subscript " + subscript.type);
			// TODO implement other subscripts
		}
		
		return recur(x, result);
	}

	private EvalResult recur(Assignable x, EvalResult result) {
		return x.getReceiver().accept(new AssignableEvaluator(env, result, eval));
	}
	
	@Override
	public EvalResult visitAssignableIfDefined(IfDefined x) {
		EvalResult cond = x.getCondition().accept(eval);
		
		if (((IBool) cond.value).getValue()) {
			return x.getReceiver().accept(this);
		}
		else {
			return x.getReceiver().accept(eval);
		}
	}
	
	@Override
	public EvalResult visitAssignableFieldAccess(FieldAccess x) {
		EvalResult receiver = x.getReceiver().accept(eval);
		String label = x.getField().toString();
		
		if (receiver.type.isTupleType()) {
			IValue result = ((ITuple) receiver.value).set(label, value.value);
			return recur(x, eval.result(receiver.type, result));
		}
		else if (receiver.type.isTreeNodeType() || receiver.type.isNamedTreeType()) {
			IValue result = ((INode) receiver.value).set(label, value.value);
			return recur(x, eval.result(receiver.type, result));
		}
		else {
			throw new RascalTypeError(x.getReceiver() + " has no field named " + label);
		}
		
	}
	
	@Override
	public EvalResult visitAssignableTuple(Tuple x) {
		java.util.List<Assignable> arguments = x.getElements();
		
		if (!value.type.getBaseType().isTupleType()) {
			throw new RascalTypeError("Receiver is a tuple, but the assigned value is not: " + value.type); 
		}
		
		TupleType tupleType = (TupleType) value.type;
		ITuple tuple = (ITuple) value.value;
		IValue[] results = new IValue[arguments.size()];
		Type [] resultTypes = new Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			Type argType = tupleType.getFieldType(i);
			IValue arg = tuple.get(i);
			EvalResult result = eval.result(argType, arg);
			AssignableEvaluator ae = new AssignableEvaluator(env,result, eval);
			EvalResult argResult = arguments.get(i).accept(ae);
			results[i] = argResult.value;
			resultTypes[i] = argResult.type;
		}
		
		return eval.result(eval.tf.tupleType(resultTypes), tupleType.make(eval.vf, results));
	}
	
	@Override
	public EvalResult visitAssignableConstructor(Constructor x) {
		throw new RascalBug("Constructor assignables not yet implemented");
	}
}
