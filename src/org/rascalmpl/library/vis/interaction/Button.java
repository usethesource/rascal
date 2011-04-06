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
*******************************************************************************/
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
		super(fpa, properties);
		if(fun.getType().isExternalType() && (fun instanceof RascalFunction)){
			this.callback = (RascalFunction) fun;
		} else {
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
			 this.callback = null;
		}
		
	    button.addMouseListener(
	    	      new MouseAdapter() {
	    	        @Override
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
	    //button.setBackground(new Color(255));
	    fpa.setBackground(new Color(0XFFFFFFFF));
	    fpa.add(button);
	}

	@Override
	public void bbox() {
		width = button.getWidth();
		height = button.getHeight();
	}
	
	public void doCallBack(){
		fpa.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
		//System.err.println("Button, call callback");
		synchronized(fpa){
			callback.call(argTypes, argVals);
		}
		fpa.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		button.setBackground(new Color(getFillColorProperty()));
		button.setLocation(PApplet.round(left), PApplet.round(top));
	}
	
	@Override
	public void destroy(){
		fpa.remove(button);
		fpa.invalidate();
		fpa.setComputedValueChanged();
	}

}
