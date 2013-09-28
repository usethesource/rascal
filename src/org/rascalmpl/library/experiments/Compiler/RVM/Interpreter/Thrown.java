package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IValue;

public class Thrown extends RuntimeException {
	
	private static final long serialVersionUID = 5789848344801944419L;
	
	private static Thrown instance = new Thrown(null);
	IValue value;
	
	private Thrown(IValue value) {
		this.value = value;
	}
	
	public static Thrown getInstance(IValue value) {
		instance.value = value;
		return instance;
	}

}
