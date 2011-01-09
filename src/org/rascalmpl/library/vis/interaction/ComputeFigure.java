package org.rascalmpl.library.vis.interaction;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

public class ComputeFigure extends Figure {
	
	private String neededTriggers[];		// Triggers needed to compute figure
	int nTriggers;							// Number of triggers
	private String lastTriggerValues[];		// Their last used values
	
	Figure figure = null;					// Last computed figure
	
											// Function of type Figure (list[str]) to compute new figure
	private OverloadedFunctionResult callback;
	
	Type[] argTypes = new Type[1];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[1];		// Argument values of callback: argList
	private IList argList;					// Actual list of strings to be used as argument in call
	
	private IEvaluatorContext ctx;


	public ComputeFigure(FigurePApplet fpa, IPropertyManager properties,  IValue fun, IList needed, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		nTriggers = needed.length();
		neededTriggers = new String[nTriggers];
		lastTriggerValues = new String[nTriggers];
		for(int i = 0; i < nTriggers; i++){
			neededTriggers[i] = ((IString)needed.get(i)).getValue();
		}
	
		this.ctx = ctx;

		if(fun.getType().isExternalType() && (fun instanceof OverloadedFunctionResult)){
			this.callback = (OverloadedFunctionResult) fun;
		} else
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
	
		TypeFactory tf = TypeFactory.getInstance();

		argTypes[0] = tf.listType(tf.stringType());
		argList = vf.list(tf.stringType());
		
		IString empty = vf.string("");
		for(int i = 0; i < nTriggers; i++){
			argList = argList.insert(empty);
		}
		
		argVals[0] = argList;
	}

	@Override
	public void bbox() {
		boolean change = (figure == null);
		
		for(int i = 0; i < nTriggers; i++){
			String s = fpa.getStrTrigger(neededTriggers[i]);
			if(s != lastTriggerValues[i]){
				change = true;
				lastTriggerValues[i] = s;
			}	
		}
		
		if(change){
			for(int i = 0; i < nTriggers; i++){
				argList = argList.put(i, vf.string(lastTriggerValues[i]));
			}
			argVals[0] = argList;
			
			Result<IValue> figureVal = callback.call(argTypes, argVals);
			//System.err.println("callback returns: " + figureVal.getValue());
			IConstructor figureCons = (IConstructor) figureVal.getValue();
			figure = FigureFactory.make(fpa, figureCons, properties, ctx);
		}
		figure.bbox();
		width = figure.width;
		height = figure.height;
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
		return false;
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		if(figure != null)
			return figure.keyPressed(key, keyCode);
		return false;
	}

}
