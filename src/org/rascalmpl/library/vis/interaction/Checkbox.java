/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.interaction;


import java.awt.Color;
import java.awt.Cursor;
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
	
	java.awt.Checkbox checkbox;

	public Checkbox(FigurePApplet fpa, IPropertyManager properties, IString name, IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties);
		
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
	    	        public void itemStateChanged(ItemEvent e) {
	    	          try {
	    	        
	    	        	  boolean selected = e.getStateChange() == ItemEvent.SELECTED;
	    	        	  System.err.println("getStateChange = " + e.getStateChange());
	    	        	  System.err.println("getState = " + checkbox.getState());
	    	        	  //checkbox.setState(!checkbox.getState());
	    	        	  System.err.println("new getState = " + checkbox.getState());
	    	        	  doCallBack(checkbox.getState());
	    	          } catch (Exception ex) {
	    	        	  System.err.println("EXCEPTION");
	    	              ex.printStackTrace();
	    	          }
	    	        }
	    	      });
	    //checkbox.setBackground(new Color(255));
	    checkbox.isEnabled();
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
		fpa.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
		synchronized(fpa){
			callback.call(argTypes, argVals);
		}
		fpa.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
	
	@Override
	public void destroy(){
		fpa.remove(checkbox);
		checkbox = null;
		fpa.invalidate();
		fpa.setComputedValueChanged();
	}

}
