/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.interaction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class TextField extends Figure {
											// Function of type Figure (list[str]) to compute new figure
	private final IValue callback;  // Function of type void() to inform backend about entered text
	private final IValue validate;	// Function of type bool(str) to validate input sofar
	
	private boolean validated = true;
	
	private final Color trueColor = new Color(0);
	private final Color falseColor = new Color(0XFF0000);
	
	final java.awt.TextField field = new java.awt.TextField();

	public TextField(IFigureApplet fpa, PropertyManager properties, IString text, IValue cb, IValue validate,  IEvaluatorContext ctx) {
		super(fpa, properties);
		
		fpa.checkIfIsCallBack(cb, ctx);
		this.callback = cb;
		if(validate!=null){
			fpa.checkIfIsCallBack(validate, ctx);
		}
		this.validate = validate;
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
	public void bbox(double desiredWidth, double desiredHeight) {
		width = getWidthProperty();
		height = getHeightProperty();
		
		field.setSize(FigureApplet.round(width), FigureApplet.round(height));
		field.setPreferredSize(new Dimension(FigureApplet.round(width), FigureApplet.round(height)));
		width = field.getWidth();
		height = field.getHeight();
	}
	
	public boolean doValidate(){
		
		if(validate != null){
			Result<IValue> res = 
				fpa.executeRascalCallBackSingleArgument(validate, TypeFactory.getInstance().stringType(), vf.string(field.getText()));
			validated = res.getValue().equals(vf.bool(true));
			field.setForeground(validated ? trueColor : falseColor);
			return validated;
		}
		return true;
	}
	
	public void doCallBack(){
		fpa.executeRascalCallBackSingleArgument(callback, TypeFactory.getInstance().stringType(), vf.string(field.getText()));
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		// fpa.setBackground(new Color(getFillColorProperty()));
		//field.setBackground(new Color(getFillColorProperty()));
		field.setForeground(validated ? new Color(getFontColorProperty()) : falseColor);
		field.setLocation(FigureApplet.round(left), FigureApplet.round(top));
	}
	
	@Override
	public void destroy(){
		fpa.remove(field);
		fpa.invalidate();
		fpa.setComputedValueChanged();
	}
}
