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


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class ComputeFigure extends Figure {
	
	Figure figure = null;					// Last computed figure
	
	final private IValue callback;
	
	
	final private IEvaluatorContext ctx;
	private IList childProps;

	public ComputeFigure(IFigureApplet fpa, PropertyManager properties,  IValue fun, IList childProps,IEvaluatorContext ctx) {
		super(fpa, properties);
	
		this.ctx = ctx;
		this.childProps = childProps;
		fpa.checkIfIsCallBack(fun, ctx);
		this.callback = fun;
	}

	@Override
	public void bbox(double desiredWidth, double desiredHeight) {	
		
		if(figure != null){
			figure.destroy();
		}
		IConstructor figureCons = (IConstructor) fpa.executeRascalCallBackWithoutArguments(callback).getValue();
		figure = FigureFactory.make(fpa, figureCons, properties, childProps, ctx);
		fpa.setComputedValueChanged();
		figure.bbox(AUTO_SIZE, AUTO_SIZE);
		width = figure.width;
		height = figure.height;
		fpa.validate();
	}

	@Override
	public void draw(double left, double top) {
		// System.err.println("ComputeFigure.draw: " + left + ", " + top + ", " + width + ", " + height);
		this.setLeft(left);
		this.setTop(top);
		figure.draw(left,top);
	}
	
	@Override 
	public double topAlign(){
		return figure != null ? figure.topAlign() : 0;
	}
	
	@Override 
	public double bottomAlign(){
		return figure != null ? figure.bottomAlign() : 0;
	}
	
	@Override 
	public double leftAlign(){
		return figure != null ? figure.leftAlign() : 0;
	}
	
	@Override 
	public double rightAlign(){
		return figure != null ? figure.rightAlign() : 0;
	}
	
	@Override
	public boolean mouseInside(double mouseX, double mouseY){
		//System.err.println("ComputeFigure.mouseInside: [" + mouseX + ", " + mouseY + "] " +
		//		getLeft() + ", " + getTop() + ", " + (getLeft() +width) + ", " + (getTop() + height));
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY);
		return false;
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
