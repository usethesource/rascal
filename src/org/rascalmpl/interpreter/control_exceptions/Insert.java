/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;

public class Insert extends ControlException {
	private static final long serialVersionUID = -6601026099925601817L;
    
	private final Result<IValue> value;
	private IBooleanResult mp;
	
	public Insert(){
    	super();
    	
    	this.value = null;
    	this.mp = null;
    }
    
    public Insert(Result<IValue> value){
    	super();
    	
    	this.value = value;
    	this.mp = null;
    }
    
    public Insert(Result<IValue> value, IBooleanResult mp){
    	super();
    	
    	this.value = value;
    	this.mp = mp;
    }
	
	public Result<IValue> getValue() {
		return value;
	}
	
	public IBooleanResult getMatchPattern(){
		return mp;
	}
	
	public void setMatchPattern(IBooleanResult mp){
		this.mp = mp;
	}
}
