package org.rascalmpl.library.vis.swt;

import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.IRunTimePropertyChanges;


public interface IFigureConstructionEnv {
	
	Composite getSWTParent();
	ICallbackEnv getCallBackEnv();
	IEvaluatorContext getRascalContext();
	FigureExecutionEnvironment getFigureExecEnv();
	IRunTimePropertyChanges getRunTimePropertyChanges();

}
