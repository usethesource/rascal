package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.graphics.FontStyle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
public interface IFigureExecutionEnvironment {

	// Rascal stuff
	public IEvaluatorContext getRascalContext();
	
	// SWT stuff
	public Composite getComp();
	public Color getColor(final int which);
	public Color getRgbColor(final int c) ;

	// font layout stuff
	public double textAscent(String fontName, double fontSize, FontStyle... styles);
	public double textDescent(String fontName, double fontSize, FontStyle... styles);
	public double textWidth(String s,String fontName, double fontSize, FontStyle... styles);
	
	// callback stuff
	public void setComputedValueChanged();
	public void redraw();
	public void checkIfIsCallBack(IValue fun) ;
	public Result<IValue> executeRascalCallBack(IValue callback,Type[] argTypes, IValue[] argVals);
	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) ;
	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback,Type type, IValue arg) ;
}
