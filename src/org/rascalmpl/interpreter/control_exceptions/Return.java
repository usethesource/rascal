/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.result.Result;

public class Return extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    private final Result<IValue> value;
	private ISourceLocation loc;
	
    public Return(ISourceLocation loc) {
    	  super();
    	
    	  this.loc = loc;
    	  this.value = null;
    }
    
    public Return(Result<IValue> value, ISourceLocation loc) {
    	  super();

    	  this.loc = loc;
    	  this.value = value;
    }
	
	public Result<IValue> getValue() {
	  return value;
	}
	
	/**
	 * Points to the location of the expression of the return, for use in error messages
	 * @return
	 */
	public ISourceLocation getLocation() {
		return loc;
	}
}
