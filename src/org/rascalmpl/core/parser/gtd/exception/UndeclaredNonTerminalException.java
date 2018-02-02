/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.core.parser.gtd.exception;

public class UndeclaredNonTerminalException extends RuntimeException{
	private static final long serialVersionUID = 1584464650068099643L;
	
	private final String name;
	private final Class<?> clazz;
	
	// TODO find the location in the Rascal source of the offending non-terminal
	
	public UndeclaredNonTerminalException(String name, Class<?> clazz){
		super();
		
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getName(){
		return name;
	}
	
	public String getClassName(){
		return clazz.getSimpleName();
	}
	
	public String getMessage(){
		return "Undeclared non-terminal: " + name + ", in class: "+clazz;
	}
}
