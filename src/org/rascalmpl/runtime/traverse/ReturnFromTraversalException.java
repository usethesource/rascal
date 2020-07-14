package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import io.usethesource.vallang.IValue;

public class ReturnFromTraversalException extends RuntimeException {
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