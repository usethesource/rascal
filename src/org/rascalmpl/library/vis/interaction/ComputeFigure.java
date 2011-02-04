package org.rascalmpl.library.vis.interaction;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

public class ComputeFigure extends Figure {
	
	Figure figure = null;					// Last computed figure
	
	final private IValue callback;
	
	Type[] argTypes = new Type[0];			// Argument types of callback
	IValue[] argVals = new IValue[0];		// Argument values of callback
	
	private IEvaluatorContext ctx;


	public ComputeFigure(FigurePApplet fpa, IPropertyManager properties,  IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
	
		this.ctx = ctx;

		if(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunctionResult))){
			this.callback = fun;
		} else
			this.callback = null;
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
	}

	@Override
	public void bbox() {
		
		Result<IValue> figureVal;
		
		if(figure != null){
			figure.destroy();
		}
		synchronized(fpa){
			if(callback instanceof RascalFunction)
				figureVal = ((RascalFunction) callback).call(argTypes, argVals);
			else
				figureVal = ((OverloadedFunctionResult) callback).call(argTypes, argVals);
		}
			
		//System.err.println("callback returns: " + figureVal.getValue());
		IConstructor figureCons = (IConstructor) figureVal.getValue();
		figure = FigureFactory.make(fpa, figureCons, properties, ctx);
		fpa.setComputedValueChanged();
		figure.bbox();
		width = figure.width;
		height = figure.height;
		fpa.validate();
	}

	@Override
	public void draw(float left, float top) {
		figure.draw(left,top);
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY){
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY);
		return false;
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY, float centerX, float centerY){
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY, centerX, centerY);
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		if(figure != null)
			return figure.mouseOver(mouseX, mouseY, mouseInParent);
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, float centerX, float centerY, boolean mouseInParent){
		if(figure != null)
			return figure.mouseOver(mouseX, mouseY, centerX, centerY, mouseInParent);
		return false;
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		if(figure != null)
			return figure.mousePressed(mouseX, mouseY, e);
		return super.mousePressed(mouseX, mouseY, e);
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		if(figure != null)
			return figure.keyPressed(key, keyCode);
		return false;
	}

}
