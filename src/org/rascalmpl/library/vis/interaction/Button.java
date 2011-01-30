package org.rascalmpl.library.vis.interaction;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

public class Button extends Figure {
											// Function of type Figure (list[str]) to compute new figure
	private RascalFunction callback;
	
	Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	
	final java.awt.Button button = new java.awt.Button();

	public Button(FigurePApplet fpa, IPropertyManager properties, IString tname, IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		
		System.err.println("fun = " + fun + fun.getType().isExternalType() + (fun instanceof RascalFunction));
		System.err.println(fun.getClass());
		if(fun.getType().isExternalType() && (fun instanceof RascalFunction)){
			this.callback = (RascalFunction) fun;
			System.err.println("Assign to callback");
		} else {
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		
		System.err.println("callback = " + callback);
		
	    button.addMouseListener(
	    	      new MouseAdapter() {
	    	        public void mouseClicked(MouseEvent e) {
	    	          try {
	    	        	  doCallBack();
	    	          } catch (Exception ex) {
	    	        	  System.err.println("EXCEPTION");
	    	            ex.printStackTrace();
	    	          }
	    	        }
	    	      });
	    button.setLabel(tname.getValue());
	    fpa.add(button);
	}

	@Override
	public void bbox() {
		width = getWidthProperty();
		height = getHeightProperty();
		button.setSize(PApplet.round(width), PApplet.round(height));
	}
	
	public void doCallBack(){
		callback.call(argTypes, argVals);
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		button.setLocation(PApplet.round(width), PApplet.round(height));
	}

}
