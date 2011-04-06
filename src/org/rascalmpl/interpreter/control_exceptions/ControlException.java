/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.control_exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ControlException extends RuntimeException {
	private final static long serialVersionUID = -5118318371303187359L;
	
	public ControlException() {
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
		return;
	}
	
	@Override
	public void printStackTrace(PrintStream s){
		return;
	}
	
	@Override
	public void printStackTrace(PrintWriter s){
		return;
	}
	
	@Override
	public StackTraceElement[] getStackTrace(){
		return null;
	}
}
