package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Reference {
	
	final Object[] stack;
	final int pos;
	
	public Reference(Object[] stack, int pos) {
		this.stack = stack;
		this.pos = pos;
	}

}
