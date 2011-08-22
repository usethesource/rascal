package org.rascalmpl.library.vis.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.properties.IRunTimePropertyChanges;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;


public interface IFigureConstructionEnv {
	
	FigureSWTApplet getSWTParent();
	void addSWTElement(Control c);
	void addAboveSWTElement(Figure fig);
	void registerOverlap(Overlap o);
	void unregisterOverlap(Overlap o);
	ICallbackEnv getCallBackEnv();
	IEvaluatorContext getRascalContext();
	FigureExecutionEnvironment getFigureExecEnv();
	IRunTimePropertyChanges getRunTimePropertyChanges();

}
