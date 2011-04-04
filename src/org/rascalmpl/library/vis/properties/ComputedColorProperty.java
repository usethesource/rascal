/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.FigurePApplet;

public class ComputedColorProperty  implements IColorPropertyValue  {
	
	Property property;
	private FigurePApplet fpa;
	
	IValue fun;
	Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	int value;

	public ComputedColorProperty(Property prop,  IValue fun, FigurePApplet fpa){
		this.property = prop;
		this.fun = fun;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public synchronized int getValue() {
		
		Result<IValue> res;
		int old = value;
		synchronized(fpa){
			if(fun instanceof RascalFunction)
				res = ((RascalFunction) fun).call(argTypes, argVals);
			else
				res = ((OverloadedFunctionResult) fun).call(argTypes, argVals);
		}
		
		value = ((IInteger) res.getValue()).intValue();
		if(value != old)
			fpa.setComputedValueChanged();
		return value;
	}
	
	public boolean isCallBack() {
		return true;
	}

}
