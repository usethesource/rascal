package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.Declaration.Alias;

public class CyclicAliasError extends StaticError {
	private static final long serialVersionUID = 4212805540522552180L;
	private final Alias first;
	private final Alias last;

	public CyclicAliasError(String name, Alias first, Alias last) {
		super("Cyclic alias declaration for " + name, last);
		this.first = first;
		this.last = last;
	}
	
	public Alias getFirstDeclaration() {
		return first;
	}
	
	public Alias getLastDeclaration() {
		return last;
	}

}
