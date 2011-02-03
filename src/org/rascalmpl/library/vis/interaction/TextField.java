package org.rascalmpl.library.vis.interaction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

public class TextField extends Figure {
											// Function of type Figure (list[str]) to compute new figure
	private final IValue callback;  // Function of type void() to inform backend about entered text
	private final IValue validate;	// Function of type bool(str) to validate input sofar
	
	Type[] argTypes = new Type[1];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[1];		// Argument values of callback: argList
	private boolean validated = true;
	
	private final Color trueColor = new Color(0);
	private final Color falseColor = new Color(0XFF0000);
	
	final java.awt.TextField field = new java.awt.TextField();

	public TextField(FigurePApplet fpa, IPropertyManager properties, IString text, IValue cb, IValue validate,  IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		
		if(cb.getType().isExternalType() && ((cb instanceof RascalFunction) || (cb instanceof OverloadedFunctionResult))){
			this.callback = cb;
		} else {
			 RuntimeExceptionFactory.illegalArgument(cb, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		
		if(validate != null){
			if(validate.getType().isExternalType() && (validate instanceof RascalFunction) || (validate instanceof OverloadedFunctionResult)){
				this.validate = validate;
			} else {
				 RuntimeExceptionFactory.illegalArgument(validate, ctx.getCurrentAST(), ctx.getStackTrace());
				 this.validate = null;
			}
		} else 
			this.validate = null;
		
		TypeFactory tf = TypeFactory.getInstance();

		argTypes[0] = tf.stringType();
		argVals[0] = vf.string("");
		
		System.err.println("callback = " + callback);
		
	    field.addKeyListener(
	    		  new KeyListener(){
	    			  public void keyTyped(KeyEvent e){
	    				  boolean b = doValidate();
	    				  if(e.getKeyCode() == KeyEvent.VK_ENTER){
	    					  if(b)
	    						  doCallBack();
	    					  field.validate();
	    				  } 
	    			  }
					// @Override
					public void keyPressed(KeyEvent e) {
						 keyTyped(e);
					}
					// @Override
					public void keyReleased(KeyEvent e) {
						keyTyped(e);
					}
	    		  });
	    field.setText(text.getValue());
	    fpa.add(field);
	}

	@Override
	public void bbox() {
		width = getWidthProperty();
		height = getHeightProperty();
		field.setSize(PApplet.round(width), PApplet.round(height));
		field.setPreferredSize(new Dimension(PApplet.round(width), PApplet.round(height)));
		
	}
	
	public boolean doValidate(){
		if(validate != null){
			argVals[0] = vf.string(field.getText());
			Result<IValue> res;
			if(validate instanceof RascalFunction)
				res = ((RascalFunction) validate).call(argTypes, argVals);
			else
				res = ((OverloadedFunctionResult) validate).call(argTypes, argVals);
			validated = res.getValue().equals(vf.bool(true));
			field.setForeground(validated ? trueColor : falseColor);
			return validated;
		} else
			return true;
	}
	
	public void doCallBack(){
		argVals[0] = vf.string(field.getText());
		if(callback instanceof RascalFunction)
			((RascalFunction) callback).call(argTypes, argVals);
		else
			((OverloadedFunctionResult) callback).call(argTypes, argVals);
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		fpa.setBackground(new Color(getFillColorProperty()));
		//field.setBackground(new Color(getFillColorProperty()));
		field.setForeground(validated ? new Color(getFontColorProperty()) : falseColor);
		field.setLocation(PApplet.round(left), PApplet.round(top));
	}
}
