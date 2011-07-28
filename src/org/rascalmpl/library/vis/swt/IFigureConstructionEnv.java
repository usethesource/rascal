package org.rascalmpl.library.vis.swt;

import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;


public interface IFigureConstructionEnv {
	
	Composite getSWTParent();
	ICallbackEnv getCallBackEnv();
	IEvaluatorContext getRascalContext();
	FigureExecutionEnvironment getFigureExecEnv();

}
