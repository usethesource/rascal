package org.meta_environment.rascal.interpreter.env;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.exceptions.FailureException;
import org.meta_environment.rascal.interpreter.exceptions.RascalRunTimeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;
import org.meta_environment.rascal.interpreter.exceptions.ReturnException;

/**
 * TODO: find a more elegant solution for this, by implementing IValue we
 * get away with storing a Lambda as an IValue, but what happens when 
 * somebody actually starts interpreting this IValue as an IConstructor?
 */
public class Lambda extends Result implements IValue {
    protected static final ValueFactory VF = ValueFactory.getInstance();
	protected static final TypeEvaluator TE = TypeEvaluator.getInstance();
	protected static final TypeFactory TF = TypeFactory.getInstance();
    protected EnvironmentStack stackClone;
    protected final Evaluator eval;
	protected final Type formals;
	private boolean hasVarArgs;
	private boolean isVoidFunction;
    
    protected final static Type FunctionType = TF.abstractDataType("Rascal.Function");
    protected final static Type ClosureType = TF.constructor(FunctionType, "Rascal.Function.Closure");
	protected final Type returnType;
	private final List<Statement> body;
	private final String name;
	protected static int callNesting = 0;
	protected static boolean callTracing = false;
	
	
	public Lambda(Evaluator eval, Type returnType, String name, Type formals, java.util.List<Statement> body, 
				Environment env) {
			this.eval = eval;
		this.returnType = returnType;
		this.name = name;
		this.formals = formals;
		this.body = body;
		this.hasVarArgs = hasVarArgs(formals);
		this.isVoidFunction = returnType.isSubtypeOf(TF.voidType());
		this.value = this;
		this.type = ClosureType;
		this.stackClone = GlobalEnvironment.getInstance().copyStack();  
		this.stackClone.pushFrame(env);
	}
    
	private boolean hasVarArgs(Type formals) {
		if (formals.isTupleType() && formals.getArity() > 0) {
			return formals.getFieldType(formals.getArity() - 1).isListType();
		}
		return false;
	}

	public boolean match(Type actuals) {
//		if (!hasVarArgs) {
			return actuals.isSubtypeOf(formals);
//		}
//		return matchVarArgsFunction(actuals);
	}
	
	public boolean isAmbiguous(Lambda other) {
		return other.match(formals) || match(other.formals);
	}
	
	private boolean matchVarArgsFunction(Type actuals) {
		int arity = formals.getArity();
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(formals.getFieldType(i))) {
				return false;
			}
		}
		
		if (i > actuals.getArity()) {
			return false;
		}

		Type elementType = formals.getFieldType(i).getElementType();

		for (; i < actuals.getArity(); i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(elementType)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Type getClosureType() {
		return ClosureType;
	}
	
	public Result call(IValue[] actuals, Type actualTypes) {
		GlobalEnvironment global = GlobalEnvironment.getInstance();
		
		int count = restoreStack(global);
		try {
			Map<Type,Type> bindings = global.getTypeBindings();
			Type instantiatedFormals = formals.instantiate(bindings);
			
			if (hasVarArgs) {
				actualTypes = computeVarArgsActualTypes(actualTypes, instantiatedFormals);
			}
			
			try {
				global.pushFrame();
				
				bindTypeParameters(actualTypes, instantiatedFormals);
				
				if (hasVarArgs) {
					actuals = computeVarArgsActuals(actuals, formals);
				}

				assignFormals(actuals, global);
				
				for (Statement stat: body) {
					stat.accept(eval);
				}
				
				if(!isVoidFunction){
					throw new RascalTypeError("Function definition:" + this + "\n does not have a return statement.");
				}
				
				return new Result(TF.voidType(), null);
			}
			catch (ReturnException e) {
				Result result = e.getValue();
				
				Type instantiatedReturnType = returnType.instantiate(global.getTypeBindings());
				
				if(!result.type.isSubtypeOf(instantiatedReturnType)){
					throw new RascalTypeError("Actual return type " + result.type + " is not compatible with declared return type " + returnType);
				}
				
				return new Result(instantiatedReturnType, result.value);
			} 
			catch (FailureException e){
				throw new RascalRunTimeError("Fail statement occurred outside switch or visit statement in " + this);
			}
			finally {
				global.popFrame();
			}
		}
		finally {
			discardStack(global, count);
		}
	}

	private void assignFormals(IValue[] actuals, GlobalEnvironment global) {
		for (int i = 0; i < formals.getArity(); i++) {
			Type formal = formals.getFieldType(i).instantiate(global.getTypeBindings());
			Result result = new Result(formal, actuals[i]);
			global.top().storeVariable(formals.getFieldName(i), result);
		}
	}


	private void discardStack(GlobalEnvironment global, int count) {
		while (--count >= 0) {
			global.popFrame();
		}
	}

	private int restoreStack(GlobalEnvironment global) {
		int count = 0;
		
		for (Environment e : stackClone) {
			global.pushFrame(e);
			count++;
		}
		return count;
	}
	
	protected void bindTypeParameters(Type actualTypes, Type formals) {
		try {
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			formals.match(actualTypes, bindings);
			GlobalEnvironment.getInstance().storeTypeBindings(bindings);
		}
		catch (FactTypeError e) {
			throw new RascalTypeError("Could not bind type parameters in " + formals + " to " + actualTypes, e);
		}
	}	
	
	private IValue[] computeVarArgsActuals(IValue[] actuals, Type formals) {
		int arity = formals.getArity();
		IValue[] newActuals = new IValue[arity];
		int i;

		if (arity == actuals.length) {
			// the last argument is already a list
			return actuals;
		}
		
		for (i = 0; i < arity - 1; i++) {
			newActuals[i] = actuals[i];
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actuals.length; j++) {
			lub = lub.lub(actuals[j].getType());
		}
		
		IListWriter list = VF.listWriter(lub);
		list.insertAt(0, actuals, i, actuals.length - arity + 1);
		newActuals[i] = list.done();
		return newActuals;
	}

	private Type computeVarArgsActualTypes(Type actualTypes, Type formals) {
		if (actualTypes.isSubtypeOf(formals)) {
			// the argument is already provided as a list
			return actualTypes;
		}
		
		int arity = formals.getArity();
		Type[] types = new Type[arity];
		java.lang.String[] labels = new java.lang.String[arity];
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			types[i] = formals.getFieldType(i);
			labels[i] = formals.getFieldName(i);
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actualTypes.getArity(); j++) {
			lub = lub.lub(actualTypes.getFieldType(j));
		}
		
		types[i] = TF.listType(lub);
		labels[i] = formals.getFieldName(i);
		
		return TF.tupleType(types, labels);
	}
	
	private StringBuffer showCall(FunctionDeclaration func, IValue[] actuals, String arrow){
		StringBuffer trace = new StringBuffer();
		for(int i = 0; i < callNesting; i++){
			trace.append("-");
		}
		trace.append(arrow).append(" ").append(func.getSignature().getName()).append("(");
		String sep = "";
		for(int i = 0; i < actuals.length; i++){
			trace.append(sep).append(actuals[i]);
			sep = ", ";
		}
		trace.append(")");
		return trace;
	}

	
	public boolean isNormal() {
		return false;
	}
	
	public boolean isClosure() {
		return true;
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		throw new UnsupportedOperationException();
	}

	public IValue getAnnotation(String label) {
		throw new UnsupportedOperationException();
	}

	public Type getType() {
		return ClosureType;
	}

	public boolean hasAnnotation(String label) {
		return false;
	}

	public boolean isEqual(IValue other) throws FactTypeError {
		return other == this;
	}

	public boolean isIdentical(IValue other) throws FactTypeError {
		return other == this;
	}

	public IValue setAnnotation(String label, IValue value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(returnType + " " + name + " " + "(" + formals + ")" + " {");
		for (Statement s : body) {
			b.append("  " + s.toString() + "\n");
		}
		b.append("}\n");
		return b.toString();
	}
}
