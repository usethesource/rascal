/*******************************************************************************
* Copyright (c) 2008 CWI.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    jurgen@vinju.org

*******************************************************************************/
package org.rascalmpl.value.visitors;

/**
 * This class can be used to wrap exceptions that occur during visiting 
 * such that clients of a visitor can deal with them appropriately.
 */
public class VisitorException extends Exception {
	private static final long serialVersionUID = 4217829420715152023L;

	public VisitorException(String msg) {
		super(msg);
	}
	
	public VisitorException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public VisitorException(Throwable cause) {
		super(cause);
	}
}
