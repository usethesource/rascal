/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.IFigureApplet;

public class ComputedBooleanProperty implements IBooleanPropertyValue {
	Property property;
	String tname;
	IValue fun;
	Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	boolean value;
	private Object fpa;

	public ComputedBooleanProperty(Property prop, IValue fun, IFigureApplet fpa){
		this.property = prop;
		this.fun = fun;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public synchronized boolean getValue() {
		Result<IValue> res;
		synchronized(fpa){
			if(fun instanceof RascalFunction)
				res = ((RascalFunction) fun).call(argTypes, argVals);
			else
				res = ((OverloadedFunctionResult) fun).call(argTypes, argVals);
		}
		
		value = ((IBool) res.getValue()).getValue();
	
		return value;
	}

	public boolean isCallBack() {
		return true;
	}

}
