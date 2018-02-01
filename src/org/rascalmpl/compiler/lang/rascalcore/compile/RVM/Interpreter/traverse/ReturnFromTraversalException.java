package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

import io.usethesource.vallang.IValue;

class ReturnFromTraversalException extends RuntimeException {
	private final static long serialVersionUID = -5118318371303187359L;
	
	private final IValue returnValue;
	
	public ReturnFromTraversalException(IValue v) {
		super();
		returnValue = v;
	}
	
	@Override
	public Throwable fillInStackTrace(){
		return null;
	}
	
	public IValue getValue(){
		return returnValue;
	}
}