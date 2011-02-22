package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class ConcatStringResult extends StringResult {
	private StringResult left;
	private StringResult right;
	
	/*package*/ ConcatStringResult(Type type, StringResult left, StringResult right, IEvaluatorContext ctx) {
		super(type, null, ctx);
		this.left = left;
		this.right = right;
	}
	
	@Override
	protected int length() {
		return left.length() + right.length();
	}
	
	@Override
	protected void yield(StringBuilder b) {
		left.yield(b);
		right.yield(b);
	}

	@Override
	public IString getValue() {
		// we assume that it is cheaper to know the size!
		StringBuilder builder = new StringBuilder(length());
		yield(builder);
		return getValueFactory().string(builder.toString());
	}

	
	
}
