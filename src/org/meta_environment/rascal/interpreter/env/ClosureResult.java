package org.meta_environment.rascal.interpreter.env;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;

/**
 * TODO: find a more elegant solution for this, by implementing IValue we
 * get away with storing a Closure as an IValue, but what happens when 
 * somebody actually starts interpreting this IValue as an INode?
 */
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
		Map<Type,Type> bindings = global.getTypeBindings();
		
		int count = 0;
		try {
			for (Environment e : stack) {
				global.pushFrame(e);
				count++;
			}
			
			// here we promote type bindings from the calling context
			// to the context of the closure
			global.pushFrame();
		    global.storeTypeBindings(bindings);
			
			return eval.call(func, actuals, actualTypes);
		}
		finally {
			while (--count >= 0) {
				global.popFrame();
			}
			global.popFrame();
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
