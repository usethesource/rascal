package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.interpreter.AssignableEvaluator.AssignmentOperator;

public abstract class Assignable extends org.rascalmpl.ast.Assignable {


public Assignable (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class IfDefinedOrDefault extends org.rascalmpl.ast.Assignable.IfDefinedOrDefault {


public IfDefinedOrDefault (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("ifdefined assignable does not represent a value");
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		try {
			this.getReceiver().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval()); // notice we use 'eval' here not '__eval'
			// if it was not defined, __eval would have thrown an exception, so now we can just go on
			return this.getReceiver().__evaluate(__eval);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			__eval.__setValue(__eval.newResult(this.getDefaultExpression().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval()), __eval.__getValue()));
			__eval.__setOperator(AssignmentOperator.Default);
			return this.getReceiver().__evaluate(__eval);
		}
	
}

}
static public class Tuple extends org.rascalmpl.ast.Assignable.Tuple {


public Tuple (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Assignable> __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Assignable> arguments = this.getElements();
		
		if (!__eval.__getValue().getType().isTupleType()) {
			// TODO construct a better expected type
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.AssignableEvaluator.__getTf().tupleEmpty(), __eval.__getValue().getType(), this);
		}
		
		org.eclipse.imp.pdb.facts.type.Type tupleType = __eval.__getValue().getType();
		org.eclipse.imp.pdb.facts.ITuple tuple = (org.eclipse.imp.pdb.facts.ITuple) __eval.__getValue().getValue();
		org.eclipse.imp.pdb.facts.IValue[] results = new org.eclipse.imp.pdb.facts.IValue[arguments.size()];
		org.eclipse.imp.pdb.facts.type.Type [] resultTypes = new org.eclipse.imp.pdb.facts.type.Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			org.eclipse.imp.pdb.facts.type.Type argType = tupleType.getFieldType(i);
			org.eclipse.imp.pdb.facts.IValue arg = tuple.get(i);
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(argType, arg, __eval.__getEval());
			org.rascalmpl.interpreter.AssignableEvaluator ae = new org.rascalmpl.interpreter.AssignableEvaluator(__eval.__getEnv(),null, result, __eval.__getEval());
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> argResult = arguments.get(i).__evaluate(ae);
			results[i] = argResult.getValue();
			resultTypes[i] = argResult.getType();
		}
		
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.AssignableEvaluator.__getTf().tupleType(resultTypes), tupleType.make(__eval.__getEval().getValueFactory(), results), __eval.__getEval());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Tuple in assignable does not represent a value:" + this);
	
}

}
static public class Bracket extends org.rascalmpl.ast.Assignable.Bracket {


public Bracket (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Subscript extends org.rascalmpl.ast.Assignable.Subscript {


public Subscript (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> receiver = this.getReceiver().__evaluate(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subscript = this.getSubscript().__evaluate(__eval);

		if (receiver.getType().isListType()) {
			if (subscript.getType().isIntegerType()) {
				org.eclipse.imp.pdb.facts.IList list = (org.eclipse.imp.pdb.facts.IList) receiver.getValue();
				org.eclipse.imp.pdb.facts.IValue result = list.get(((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue()).intValue());
				org.eclipse.imp.pdb.facts.type.Type type = receiver.getType().getElementType();
				return __eval.normalizedResult(type, result);
			}

			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().integerType(), subscript.getType(), this);
		}
		else if (receiver.getType().isMapType()) {
			org.eclipse.imp.pdb.facts.type.Type keyType = receiver.getType().getKeyType();

			if (receiver.hasInferredType() || subscript.getType().isSubtypeOf(keyType)) {
				org.eclipse.imp.pdb.facts.IValue result = ((org.eclipse.imp.pdb.facts.IMap) receiver.getValue()).get(subscript.getValue());
				
				if (result == null) {
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.noSuchKey(subscript.getValue(), this, __eval.getStackTrace());
				}
				org.eclipse.imp.pdb.facts.type.Type type = receiver.getType().getValueType();
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(type, result, __eval);
			}

			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(keyType, subscript.getType(), this.getSubscript());
		}
		// TODO implement other subscripts
		throw new org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError("subscript", receiver.getType(), this);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> rec = this.getReceiver().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval());
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subscript = this.getSubscript().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval());
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result;
		
		if (rec == null || rec.getValue() == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(this.getReceiver().toString(), this.getReceiver());
		}
		
		if (rec.getType().isListType() && subscript.getType().isIntegerType()) {
			try {
				org.eclipse.imp.pdb.facts.IList list = (org.eclipse.imp.pdb.facts.IList) rec.getValue();
				int index = ((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue()).intValue();
				__eval.__setValue(__eval.newResult(list.get(index), __eval.__getValue()));
				list = list.put(index, __eval.__getValue().getValue());
				result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(rec.hasInferredType() ? rec.getType().lub(list.getType()) : rec.getType(), list, __eval.__getEval());
			}  
			catch (java.lang.IndexOutOfBoundsException e){
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue(), __eval.__getEval().getCurrentAST(), __eval.__getEval().getStackTrace());
			}
		}
		else if (rec.getType().isMapType()) {
			org.eclipse.imp.pdb.facts.type.Type keyType = rec.getType().getKeyType();
			
			if (rec.hasInferredType() || subscript.getType().isSubtypeOf(keyType)) {
				org.eclipse.imp.pdb.facts.IValue oldValue = ((org.eclipse.imp.pdb.facts.IMap) rec.getValue()).get(subscript.getValue());
				__eval.__setValue(__eval.newResult(oldValue, __eval.__getValue()));
				org.eclipse.imp.pdb.facts.IMap map = ((org.eclipse.imp.pdb.facts.IMap) rec.getValue()).put(subscript.getValue(), __eval.__getValue().getValue());
				result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(rec.hasInferredType() ? rec.getType().lub(map.getType()) : rec.getType(), map, __eval.__getEval());
			}
			else {
				throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(keyType, subscript.getType(), this.getSubscript());
			}
			
		} 
		else if (rec.getType().isNodeType() && subscript.getType().isIntegerType()) {
			int index = ((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue()).intValue();
			org.eclipse.imp.pdb.facts.INode node = (org.eclipse.imp.pdb.facts.INode) rec.getValue();
			
			if(index >= node.arity()){
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue(), __eval.__getEval().getCurrentAST(), __eval.__getEval().getStackTrace());
			}
			__eval.__setValue(__eval.newResult(node.get(index), __eval.__getValue()));
			node = node.set(index, __eval.__getValue().getValue());
			result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(rec.getType(), node, __eval.__getEval());
		}
		else if (rec.getType().isTupleType() && subscript.getType().isIntegerType()) {
			int index = ((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue()).intValue();
			org.eclipse.imp.pdb.facts.ITuple tuple = (org.eclipse.imp.pdb.facts.ITuple) rec.getValue();
			
			if(index >= tuple.arity()){
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds((org.eclipse.imp.pdb.facts.IInteger) subscript.getValue(), __eval.__getEval().getCurrentAST(), __eval.__getEval().getStackTrace());
			}
			
			__eval.__setValue(__eval.newResult(tuple.get(index), __eval.__getValue()));
			
			tuple = tuple.set(index, __eval.__getValue().getValue());
			result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(rec.getType(), tuple, __eval.__getEval());
		}
		else if (rec.getType().isRelationType() && subscript.getType().isSubtypeOf(rec.getType().getFieldType(0))) {
			org.eclipse.imp.pdb.facts.IRelation rel = (org.eclipse.imp.pdb.facts.IRelation) rec.getValue();
			org.eclipse.imp.pdb.facts.IValue sub = subscript.getValue();

			if (rec.getType().getArity() != 2) {
				throw new org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptError(rec.getType(), subscript.getType(), this);
			}
			
			if (!__eval.__getValue().getType().isSubtypeOf(rec.getType().getFieldType(1))) {
				throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(rec.getType().getFieldType(1), __eval.__getValue().getType(), __eval.__getEval().getCurrentAST());
			}
			
			rel = rel.insert(__eval.__getEval().getValueFactory().tuple(sub, __eval.__getValue().getValue()));
			result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(rec.getType(), rel, __eval.__getEval());
		}
		else {
			throw new org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptError(rec.getType(), subscript.getType(), this);
			// TODO implement other subscripts
		}
		
		return __eval.recur(this, result);
	
}

}
static public class Constructor extends org.rascalmpl.ast.Assignable.Constructor {


public Constructor (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,java.util.List<org.rascalmpl.ast.Assignable> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type valueType = __eval.__getValue().getType();
		
		if (!valueType.isNodeType() && !valueType.isAbstractDataType() && !valueType.isConstructorType()) {
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.AssignableEvaluator.__getTf().nodeType(), __eval.__getValue().getType(), this);
		}
		
		org.eclipse.imp.pdb.facts.INode node = (org.eclipse.imp.pdb.facts.INode) __eval.__getValue().getValue();
		org.eclipse.imp.pdb.facts.type.Type nodeType = node.getType();
		
		if (nodeType.isAbstractDataType()) {
			nodeType = ((org.eclipse.imp.pdb.facts.IConstructor) __eval.__getValue().getValue()).getConstructorType();
		}
		
		if (!node.getName().equals(org.rascalmpl.interpreter.utils.Names.name(this.getName()))) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.nameMismatch(node.getName(), org.rascalmpl.interpreter.utils.Names.name(this.getName()), this.getName(), __eval.__getEval().getStackTrace());
		}
		
		java.util.List<org.rascalmpl.ast.Assignable> arguments = this.getArguments();
		
		if (node.arity() != arguments.size()) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.arityMismatch(node.arity(), arguments.size(), this, __eval.__getEval().getStackTrace());
		}
		
		org.eclipse.imp.pdb.facts.IValue[] results = new org.eclipse.imp.pdb.facts.IValue[arguments.size()];
		org.eclipse.imp.pdb.facts.type.Type [] resultTypes = new org.eclipse.imp.pdb.facts.type.Type[arguments.size()];
		
		for (int i = 0; i < arguments.size(); i++) {
			org.eclipse.imp.pdb.facts.type.Type argType = !nodeType.isConstructorType() ? org.rascalmpl.interpreter.AssignableEvaluator.__getTf().valueType() : nodeType.getFieldType(i);
			org.eclipse.imp.pdb.facts.IValue arg = node.get(i);
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(argType, arg, __eval.__getEval());
			org.rascalmpl.interpreter.AssignableEvaluator ae = new org.rascalmpl.interpreter.AssignableEvaluator(__eval.__getEnv(),null, result, __eval.__getEval());
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> argResult = arguments.get(i).__evaluate(ae);
			results[i] = argResult.getValue();
			resultTypes[i] = argType;
		}
		
		if (!nodeType.isAbstractDataType() && !nodeType.isConstructorType()) {
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(nodeType, nodeType.make(__eval.__getEval().getValueFactory(), node.getName(), results), __eval.__getEval());
		}
		
		org.eclipse.imp.pdb.facts.type.Type constructor = __eval.__getEval().getCurrentEnvt().getConstructor(node.getName(), org.rascalmpl.interpreter.AssignableEvaluator.__getTf().tupleType(resultTypes));
		
		if (constructor == null) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("could not find constructor for " + node.getName() + " : " + org.rascalmpl.interpreter.AssignableEvaluator.__getTf().tupleType(resultTypes));
		}
		
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(constructor.getAbstractDataType(), constructor.make(__eval.__getEval().getValueFactory(), results), __eval.__getEval());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Constructor assignable does not represent a value");
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Assignable.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Assignable> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
	
}

}
static public class Variable extends org.rascalmpl.ast.Assignable.Variable {


public Variable (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		org.rascalmpl.ast.QualifiedName qname = this.getQualifiedName();
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> previous = __eval.__getEnv().getVariable(qname);

		if (__eval.__getEnv().isNameFinal(qname)) {
			throw new org.rascalmpl.interpreter.staticErrors.AssignmentToFinalError(this.getQualifiedName());
		}
		
		//System.out.println("I am assigning: " + this + "(oldvalue = " + previous + ")");
		
		if(previous != null && previous.getValue() != null){
			__eval.__setValue(__eval.newResult(previous, __eval.__getValue()));
			__eval.__getEnv().storeVariable(qname, __eval.__getValue());
			return __eval.__getValue();
		}
		
		switch(__eval.__getOperator()){
		case Default:
		case IsDefined:
				//System.out.println("And it's local: " + this);
//				env.storeLocalVariable(qname, value); ?? OOPS 
//				env.declareVariable(value.getType(), qname.toString());
				__eval.__getEnv().storeVariable(qname, __eval.__getValue());
				return __eval.__getValue();
		default:
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(this.toString(), this);
		}
		
		// TODO implement semantics of global keyword, when not given the
		// variable should be inserted in the local scope.
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.getCurrentEnvt().getVariable(this.getQualifiedName());
	
}

}
static public class Annotation extends org.rascalmpl.ast.Assignable.Annotation {


public Annotation (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> receiver = this.getReceiver().__evaluate(__eval);
		java.lang.String label = this.getAnnotation().toString();

		if (!__eval.getCurrentEnvt().declaresAnnotation(receiver.getType(), label)) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError(label, receiver.getType(), this);
		}

		org.eclipse.imp.pdb.facts.type.Type type = __eval.getCurrentEnvt().getAnnotationType(receiver.getType(), label);
		org.eclipse.imp.pdb.facts.IValue value = ((org.eclipse.imp.pdb.facts.IConstructor) receiver.getValue()).getAnnotation(label);

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(type, value, __eval);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		java.lang.String label = org.rascalmpl.interpreter.utils.Names.name(this.getAnnotation());
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = this.getReceiver().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval());
				
		if(result == null || result.getValue() == null)
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(this.getReceiver().toString(), this.getReceiver());
		
		if (!__eval.__getEnv().declaresAnnotation(result.getType(), label)) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError(label, result.getType(), this);
		}
		
		try {
			__eval.__setValue(__eval.newResult(result.getAnnotation(label, __eval.__getEnv()), __eval.__getValue()));
		} catch (org.rascalmpl.interpreter.control_exceptions.Throw e){
			// NoSuchAnnotation
		}
		return __eval.recur(this, result.setAnnotation(label, __eval.__getValue(), __eval.__getEnv()));
//		result.setValue(((IConstructor) result.getValue()).setAnnotation(label, value.getValue()));
		//return recur(this, result);
	
}

}
static public class FieldAccess extends org.rascalmpl.ast.Assignable.FieldAccess {


public FieldAccess (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assignable __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> receiver = this.getReceiver().__evaluate(__eval);
		java.lang.String label = org.rascalmpl.interpreter.utils.Names.name(this.getField());

		if (receiver == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError(this.getReceiver().toString(), this.getReceiver());
		}
		
		org.eclipse.imp.pdb.facts.type.Type receiverType = receiver.getType();
		if (receiverType.isTupleType()) {
			// the run-time tuple may not have labels, the static type can have labels,
			// so we use the static type here. 
			int index = receiverType.getFieldIndex(label);
			org.eclipse.imp.pdb.facts.IValue result = ((org.eclipse.imp.pdb.facts.ITuple) receiver.getValue()).get(index);
			org.eclipse.imp.pdb.facts.type.Type type = receiverType.getFieldType(index);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(type, result, __eval);
		}
		else if (receiverType.isConstructorType() || receiverType.isAbstractDataType()) {
			org.eclipse.imp.pdb.facts.IConstructor cons = (org.eclipse.imp.pdb.facts.IConstructor) receiver.getValue();
			org.eclipse.imp.pdb.facts.type.Type node = cons.getConstructorType();

			if (!receiverType.hasField(label, __eval.getCurrentEnvt().getStore())) {
				throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(label, receiverType, this);
			}

			if (!node.hasField(label)) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.noSuchField(label, this,__eval.getStackTrace());
			}

			int index = node.getFieldIndex(label);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(node.getFieldType(index), cons.get(index), __eval);
		}
		else if (receiverType.isSourceLocationType()) {
			return receiver.fieldAccess(label, new org.eclipse.imp.pdb.facts.type.TypeStore());
		}
		else {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(label, receiverType, this);
		}
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.AssignableEvaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> receiver = this.getReceiver().__evaluate((org.rascalmpl.interpreter.Evaluator)__eval.__getEval());
		java.lang.String label = org.rascalmpl.interpreter.utils.Names.name(this.getField());
		
		if(receiver == null || receiver.getValue() == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(this.getReceiver().toString(), this.getReceiver());
		}
		
		if (receiver.getType().isTupleType()) {
			
			int idx = receiver.getType().getFieldIndex(label);
			if (idx < 0) {
				throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(label, receiver.getType(), this);
			}
			
			__eval.__setValue(__eval.newResult(((org.eclipse.imp.pdb.facts.ITuple) receiver.getValue()).get(idx), __eval.__getValue()));
			org.eclipse.imp.pdb.facts.IValue result = ((org.eclipse.imp.pdb.facts.ITuple) receiver.getValue()).set(idx, __eval.__getValue().getValue());
			return __eval.recur(this, org.rascalmpl.interpreter.result.ResultFactory.makeResult(receiver.getType(), result, __eval.__getEval()));
		}
		else if (receiver.getType().isConstructorType() || receiver.getType().isAbstractDataType()) {
			org.eclipse.imp.pdb.facts.IConstructor cons = (org.eclipse.imp.pdb.facts.IConstructor) receiver.getValue();
			org.eclipse.imp.pdb.facts.type.Type node = cons.getConstructorType();
			
			/* TODO: remove?
			if (!receiver.getType().hasField(label)) {
				throw new NoSuchFieldError(receiver.getType() + " does not have a field named `" + label + "`", this);
			}
			*/

			if (!node.hasField(label)) {
				throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(label, receiver.getValue().getType() , this);
			}
			
			int index = node.getFieldIndex(label);
			
			if (!__eval.__getValue().getType().isSubtypeOf(node.getFieldType(index))) {
				throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(node.getFieldType(index), __eval.__getValue().getType(), this);
			}
			__eval.__setValue(__eval.newResult(cons.get(index), __eval.__getValue()));
			
			org.eclipse.imp.pdb.facts.IValue result = cons.set(index, __eval.__getValue().getValue());
			return __eval.recur(this, org.rascalmpl.interpreter.result.ResultFactory.makeResult(receiver.getType(), result, __eval.__getEval()));
		}
		else if (receiver.getType().isSourceLocationType()){
//			ISourceLocation loc = (ISourceLocation) receiver.getValue();
			
			__eval.__setValue(__eval.newResult(receiver.fieldAccess(label, __eval.__getEnv().getStore()), __eval.__getValue()));
			return __eval.recur(this, receiver.fieldUpdate(label, __eval.__getValue(), __eval.__getEnv().getStore()));
			//return recur(this, eval.sourceLocationFieldUpdate(loc, label, value.getValue(), value.getType(), this));
		}
		else {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError(label, receiver.getType(), this);
		}

	
}

}
}