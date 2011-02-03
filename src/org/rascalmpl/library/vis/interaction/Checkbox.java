package org.rascalmpl.library.vis.interaction;


import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

public class Checkbox extends Figure {
											
	private final RascalFunction callback;		// Function of type void() to communicate checkbox state change
	
	final Type[] argTypes = new Type[0];		// Argument types of callback: []
	final IValue[] argVals = new IValue[0];		// Argument values of callback: none
	
	final java.awt.Checkbox checkbox;

	public Checkbox(FigurePApplet fpa, IPropertyManager properties, IString name, IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		
		if(fun.getType().isExternalType() && (fun instanceof RascalFunction)){
			this.callback = (RascalFunction) fun;

		} else {
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		
		checkbox = new java.awt.Checkbox(name.getValue(), false);
	    checkbox.addItemListener(
	    	      new ItemListener() {
	    	    	  @Override
	    	        public void itemStateChanged(ItemEvent e) {
	    	          try {
	    	        	  doCallBack();
	    	          } catch (Exception ex) {
	    	        	  System.err.println("EXCEPTION");
	    	            ex.printStackTrace();
	    	          }
	    	        }
	    	      });
	    //checkbox.setBackground(new Color(0));
	    fpa.add(checkbox);
	}

	@Override
	public void bbox() {
		width = checkbox.getWidth();
		height = checkbox.getHeight();
	}
	
	public void doCallBack(){
		System.err.println("Calling callback: " + callback);
		callback.call(argTypes, argVals);
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		//fpa.setBackground(new Color(getFillColorProperty()));
		checkbox.setBackground(new Color(getFillColorProperty()));
		checkbox.setLocation(PApplet.round(left), PApplet.round(top));
	}

}
