/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public interface ICallbackEnv {
	public IEvaluatorContext getRascalContext();
	public void checkIfIsCallBack(IValue fun);
	public void fakeRascalCallBack();
	public int getComputeClock();
	public void signalRecompute();
	public long getAndResetRascalTime(); // profiling
	public Result<IValue> executeRascalCallBack(IValue callback, Type[] argTypes, IValue[] argVals) ;
	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) ;
	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback, Type type, IValue arg) ;
	public IConstructor executeRascalFigureCallBack(IValue callback, Type[] argTypes, IValue[] argVals);
	public void registerAnimation(Animation a);
	public void unregisterAnimation(Animation a);
}
