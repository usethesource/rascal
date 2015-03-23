package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

public class RascalExceptionCarrier extends Exception {

	public Object rascalException ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RascalExceptionCarrier(Object rasEx) {
		// TODO Auto-generated constructor stub
		super("RascalExceptionCarrier") ;
		rascalException = rasEx ;
	}

}
