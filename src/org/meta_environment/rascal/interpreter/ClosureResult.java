package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.EnvironmentStack;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;

public class ClosureResult extends EvalResult implements IValue {
    private final EnvironmentStack stack;
    private final Evaluator eval;
    private final FunctionDeclaration func;
    private final static TypeFactory tf = TypeFactory.getInstance();
    
    protected final static Type FunctionType = tf.namedTreeType("Rascal.Function");
    protected final static Type ClosureType = tf.treeNodeType(FunctionType, "Rascal.Function.Closure");
    
	public ClosureResult(Evaluator eval, FunctionDeclaration func, Environment env) {
		this.eval = eval;
		this.func = func;
		this.value = this;
		this.type = ClosureType;
		
		GlobalEnvironment global = GlobalEnvironment.getInstance();
		this.stack = global.copyStack();  
		this.stack.pushFrame(env);
	}
	
	public static Type getClosureType() {
		return ClosureType;
	}
	
	public EvalResult call(IValue[] actuals, Type actualTypes) {
		GlobalEnvironment global = GlobalEnvironment.getInstance();
		
		int count = 0;
		try {
			for (Environment e : stack) {
				global.pushFrame(e);
				count++;
			}
			
			return eval.call(func, actuals, actualTypes);
		}
		finally {
			while (--count >= 0) {
				global.popFrame();
			}
		}
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
		return func.toString();
	}
}
