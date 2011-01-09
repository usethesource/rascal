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

public class ComputeTrigger extends Figure {
	
	private String tname;
	private String neededTriggers[];		// Triggers needed to compute figure
	int nTriggers;							// Number of triggers
	private String lastTriggerValues[];		// Their last used values
	
	Figure figure = null;					// Last computed figure
	
											// Function of type Figure (list[str]) to compute new figure
	private OverloadedFunctionResult callback;
	
	Type[] argTypes = new Type[1];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[1];		// Argument values of callback: argList
	private IList argList;					// Actual list of strings to be used as argument in call


	public ComputeTrigger(FigurePApplet fpa, IPropertyManager properties, IString tname, IString init, IValue fun, IList needed, IConstructor fcons, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		
		this.tname = tname.getValue();
		fpa.setStrTrigger(this.tname, init.getValue());
		
		nTriggers = needed.length();
		neededTriggers = new String[nTriggers];
		lastTriggerValues = new String[nTriggers];
		for(int i = 0; i < nTriggers; i++){
			neededTriggers[i] = ((IString)needed.get(i)).getValue();
		}

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
		
		figure = FigureFactory.make(fpa, fcons, properties, ctx);
	}

	@Override
	public void bbox() {
		boolean change = false;
		
		for(int i = 0; i < nTriggers; i++){
			String s = fpa.getStrTrigger(neededTriggers[i]);
			if(s != lastTriggerValues[i]){
				change = true;
				lastTriggerValues[i] = s;
			}	
		}
		
		if(change){
			doCallBack();
		}
		figure.bbox();
		width = figure.width;
		height = figure.height;
	}
	
	private void doCallBack(){
		for(int i = 0; i < nTriggers; i++){
			argList = argList.put(i, vf.string(lastTriggerValues[i]));
		}
		argVals[0] = argList;
		
		Result<IValue> triggerVal = callback.call(argTypes, argVals);
		//System.err.println("callback returns: " + triggerVal.getValue());
		String s = ((IString)triggerVal.getValue()).getValue();
		fpa.setStrTrigger(tname, s);
	}

	@Override
	public void draw(float left, float top) {
		this.left = left;
		this.top = top;
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
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		if(mouseInside(mousex, mousey)){
			doCallBack();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		if(figure != null)
			return figure.keyPressed(key, keyCode);
		return false;
	}

}
