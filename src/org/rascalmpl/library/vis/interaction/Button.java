package org.rascalmpl.library.vis.interaction;

import java.awt.Color;
import java.awt.Cursor;
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
	final private RascalFunction callback;
	
	final Type[] argTypes = new Type[0];		// Argument types of callback: []
	final IValue[] argVals = new IValue[0];		// Argument values of callback: []
	
	final java.awt.Button button = new java.awt.Button();

	public Button(FigurePApplet fpa, IPropertyManager properties, IString tname, IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		if(fun.getType().isExternalType() && (fun instanceof RascalFunction)){
			this.callback = (RascalFunction) fun;
		} else {
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		
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
	    button.setBackground(new Color(0));
	    fpa.add(button);
	}

	@Override
	public void bbox() {
		width = button.getWidth();
		height = button.getHeight();
	}
	
	public void doCallBack(){
		button.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
		callback.call(argTypes, argVals);
		button.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		button.setBackground(new Color(getFillColorProperty()));
		button.setLocation(PApplet.round(left), PApplet.round(top));
	}

}
