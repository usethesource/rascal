package org.meta_environment.rascal.interpreter.control_exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ControlException extends RuntimeException{
	private final static long serialVersionUID = -5118318371303187359L;
	
	private volatile boolean initialized = false;
	
	public ControlException(){
		super();
	}
	
	public ControlException(String message){
		super(message);
	}
	
	public ControlException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ControlException(Throwable cause){
		super(cause);
	}
	
	@Override
	public Throwable fillInStackTrace(){
		return null;
	}
	
	@Override
	public void printStackTrace(){
		if(!initialized){
			super.fillInStackTrace();
			initialized = true;
		}
		
		super.printStackTrace();
	}
	
	@Override
	public void printStackTrace(PrintStream s){
		if(!initialized){
			super.fillInStackTrace();
			initialized = true;
		}
		
		super.printStackTrace(s);
	}
	
	@Override
	public void printStackTrace(PrintWriter s){
		if(!initialized){
			super.fillInStackTrace();
			initialized = true;
		}
		
		super.printStackTrace(s);
	}
	
	@Override
	public StackTraceElement[] getStackTrace(){
		if(!initialized){
			super.fillInStackTrace();
			initialized = true;
		}
		
		return super.getStackTrace();
	}
}
