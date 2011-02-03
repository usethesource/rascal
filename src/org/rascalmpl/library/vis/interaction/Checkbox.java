package org.rascalmpl.library.vis.interaction;


import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

public class Checkbox extends Figure {
											
	private final RascalFunction callback;		// Function of type void() to communicate checkbox state change
	
	final Type[] argTypes = new Type[1];		// Argument types of callback: [bool]
	final IValue[] argVals = new IValue[1];		// Argument values of callback: bool
	
	final java.awt.Checkbox checkbox;

	public Checkbox(FigurePApplet fpa, IPropertyManager properties, IString name, IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		
		if(fun.getType().isExternalType() && (fun instanceof RascalFunction)){
			this.callback = (RascalFunction) fun;

		} else {
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		TypeFactory tf = TypeFactory.getInstance();

		argTypes[0] = tf.boolType();
		argVals[0] = vf.bool(false);
		
		checkbox = new java.awt.Checkbox(name.getValue(), false);
	    checkbox.addItemListener(
	    	      new ItemListener() {
	    	    	  @Override
	    	        public void itemStateChanged(ItemEvent e) {
	    	          try {
	    	        	  checkbox.getParent().invalidate();
	    	        	  System.err.println("itemStateChanged: getState() == " + checkbox.getState());
	    	        	  //checkbox.setState(!checkbox.getState());
	    	        	  doCallBack(checkbox.getState());
	    	          } catch (Exception ex) {
	    	        	  System.err.println("EXCEPTION");
	    	              ex.printStackTrace();
	    	          }
	    	        }
	    	      });
	    //checkbox.setBackground(new Color(255));
	    fpa.add(checkbox);
	    System.err.println("Created checkbox");
	}

	@Override
	public void bbox() {
		width = checkbox.getWidth();
		height = checkbox.getHeight();
	}
	
	public void doCallBack(boolean selected){
		System.err.println("Calling callback: " + callback + " with selected = " + selected);
		argVals[0] = vf.bool(selected);
		callback.call(argTypes, argVals);
		checkbox.getParent().validate();
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		checkbox.setBackground(new Color(getFillColorProperty()));
		checkbox.setLocation(PApplet.round(left), PApplet.round(top));
	}

}
