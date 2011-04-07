/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.interaction;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

public class ComputeFigure extends Figure {
	
	Figure figure = null;					// Last computed figure
	
	final private IValue callback;
	
	final Type[] argTypes = new Type[0];			// Argument types of callback
	final IValue[] argVals = new IValue[0];		// Argument values of callback
	
	final private IEvaluatorContext ctx;


	public ComputeFigure(IFigureApplet fpa, IPropertyManager properties,  IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties);
	
		this.ctx = ctx;

		if(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunctionResult))){
			this.callback = fun;
		} else
			this.callback = null;
			 RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
	}

	@Override
	public void bbox() {
		
		Result<IValue> figureVal;
		
		if(figure != null){
			figure.destroy();
		}
		fpa.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
		synchronized(fpa){
			if(callback instanceof RascalFunction)
				figureVal = ((RascalFunction) callback).call(argTypes, argVals);
			else
				figureVal = ((OverloadedFunctionResult) callback).call(argTypes, argVals);
		}
		fpa.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			
		System.err.println("callback returns: " + figureVal.getValue());
		IConstructor figureCons = (IConstructor) figureVal.getValue();
		figure = FigureFactory.make(fpa, figureCons, properties, ctx);
		fpa.setComputedValueChanged();
		figure.bbox();
		width = figure.width;
		height = figure.height;
		fpa.validate();
	}

	@Override
	public void draw(float left, float top) {
		System.err.println("ComputeFigure.draw: " + left + ", " + top + ", " + width + ", " + height);
		this.setLeft(left);
		this.setTop(top);
		figure.draw(left,top);
	}
	
	@Override 
	public float topAnchor(){
		return figure != null ? figure.topAnchor() : 0;
	}
	
	@Override 
	public float bottomAnchor(){
		return figure != null ? figure.bottomAnchor() : 0;
	}
	
	@Override 
	public float leftAnchor(){
		return figure != null ? figure.leftAnchor() : 0;
	}
	
	@Override 
	public float rightAnchor(){
		return figure != null ? figure.rightAnchor() : 0;
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY){
		System.err.println("ComputeFigure.mouseInside: [" + mouseX + ", " + mouseY + "] " +
				getLeft() + ", " + getTop() + ", " + (getLeft() +width) + ", " + (getTop() + height));
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY);
		return false;
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY, float centerX, float centerY){
		System.err.println("ComputeFigure.mouseInside: [" + mouseX + ", " + mouseY + "] " +
				getLeft() + ", " + getTop() + ", " + (getLeft() +width) + ", " + (getTop() + height));
		if(figure != null){
			boolean b = figure.mouseInside(mouseX, mouseY, centerX, centerY);
			System.err.println("ComputeFigure.mouseInside => " + b);
			return b;
		}
		System.err.println("ComputeFigure.mouseInside => " + false);
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		System.err.println("ComputeFigure.mouseOver1: " + figure);
		if(figure != null){
			return figure.mouseOver(mouseX, mouseY, mouseInParent);
		}
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, float centerX, float centerY, boolean mouseInParent){
		// System.err.println("ComputeFigure.mouseOver2: " + figure);
		if(figure != null){
			return figure.mouseOver(mouseX, mouseY, centerX, centerY, mouseInParent);
		}
		return false;
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		System.err.println("ComputeFigure.mousePressed: " + mouseX + ", " + mouseY);
		if(figure != null)
			return figure.mousePressed(mouseX, mouseY, e);
		return super.mousePressed(mouseX, mouseY, e);
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		if(figure != null)
			return figure.keyPressed(key, keyCode);
		return false;
	}
	
	@Override
	public void destroy(){
		if(figure != null)
			figure.destroy();
	}

}
