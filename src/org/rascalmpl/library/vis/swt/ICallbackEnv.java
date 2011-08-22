package org.rascalmpl.library.vis.swt;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;

public interface ICallbackEnv {
	public IEvaluatorContext getRascalContext();
	public void checkIfIsCallBack(IValue fun);
	public void fakeRascalCallBack();
	public int getComputeClock();
	public void signalRecompute();
	public Result<IValue> executeRascalCallBack(IValue callback, Type[] argTypes, IValue[] argVals) ;
	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) ;
	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback, Type type, IValue arg) ;
	public IConstructor executeRascalFigureCallBack(IValue callback, Type[] argTypes, IValue[] argVals);
}
