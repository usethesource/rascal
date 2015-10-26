/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.swt.widgets.Control;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IValue;

public abstract class SWTWidgetFigureWithValidationAndCallBack<WidgetType extends Control> extends SWTWidgetFigureWithSingleCallBack<WidgetType> {

	IValue validate;
	boolean validated;
	
	SWTWidgetFigureWithValidationAndCallBack(IFigureConstructionEnv env, IValue callback, IValue validate, PropertyManager properties) {
		super(env, callback, properties);
		if (validate != null) {
			cbenv.checkIfIsCallBack(validate);
		}
		this.validate = validate;
		validated = true;
	}

	
	
	public void doValidate(){
		if (validate != null) {
			validated = ((IBool)(executeValidate()).getValue()).getValue();
		}
	}
	
	public void doCallback(){
		if(validated || validate == null){
			executeCallback();
			cbenv.signalRecompute();
		}
	}
	
	abstract Result<IValue>  executeValidate();

}
