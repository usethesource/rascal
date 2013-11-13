package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

/**
 * A Reference identifies a variable in a specific stack frame and can be used
 * to retrieve that variable's value or destructively update its value.
 *
 */
public class Reference {
	
	final Object[] stack;
	final int pos;
	
	public Reference(Object[] stack, int pos) {
		this.stack = stack;
		this.pos = pos;
	}
	
	public Object getValue(){
		return stack[pos];
	}
	
	/**
	 * @return true when this reference refers to a variable with non-null value.
	 */
	public boolean isDefined(){
		return stack[pos] != null;
	}
	
	/**
	 * Set the variable identified by this reference to undefined (null).
	 */
	public void undefine(){
		stack[pos] = null;
	}
}
