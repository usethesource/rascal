package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.Declaration.Alias;

public class CyclicAliasDeclaration extends TypeErrorException {
	private static final long serialVersionUID = 4212805540522552180L;
	private final Alias first;
	private final Alias last;

	public CyclicAliasDeclaration(String name, Alias first, Alias last) {
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
