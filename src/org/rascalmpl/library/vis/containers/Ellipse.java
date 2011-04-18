/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

// import processing.core.FigureApplet;
// import processing.core.FigureApplet;



/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {

	public Ellipse(IFigureApplet fpa, PropertyManager properties, IConstructor inside,  IList childProps,IEvaluatorContext ctx) {
		super(fpa, properties, inside, childProps, ctx);
	}
	
	@Override
	void drawContainer(){
		fpa.ellipseMode(FigureApplet.CORNERS);
		fpa.ellipse(getLeft(), getTop(), getLeft() + width, getTop() + height);
	}
	
	@Override
	String containerName(){
		return "ellipse";
	}
	
	/**
	 * Draw a connection from an external position (fromX, fromY) to the center (X,Y) of the current figure.
	 * At the intersection with the border of the current figure, place an arrow that is appropriately rotated.
	 * @param left		X of left corner
	 * @param top		Y of left corner
	 * @param X			X of center of current figure
	 * @param Y			Y of center of current figure
	 * @param fromX		X of center of figure from which connection is to be drawn
	 * @param fromY		Y of center of figure from which connection is to be drawn
	 * @param toArrow	the figure to be used as arrow
	 */
	@Override
	public void connectArrowFrom(float left, float top, float X, float Y, float fromX, float fromY,
			Figure toArrow){
		
		if(fromX == X)
			fromX += 0.00001;
        float theta = FigureApplet.atan((fromY - Y) / (fromX - X));
        if(theta < 0){
        	if(fromX < X )
        		theta += FigureApplet.PI;
        } else {
        	if(fromX < X )
        		theta += FigureApplet.PI;
        }
        float sint = FigureApplet.sin(theta);
        float cost = FigureApplet.cos(theta);
        float r = height * width / (4 * FigureApplet.sqrt((height*height*cost*cost + width*width*sint*sint)/4));
        float IX = X + r * cost;
        float IY = Y + r * sint;
        
   //     fpa.line(left + fromX, top + fromY, left + IX, top + IY);
        
        if(toArrow != null){
        	toArrow.bbox(AUTO_SIZE, AUTO_SIZE);
        	fpa.pushMatrix();
        	fpa.translate(left + IX, top + IY);
        	fpa.rotate(FigureApplet.radians(-90) + theta);
        	toArrow.draw(-toArrow.width/2, 0);
        	fpa.popMatrix();
        }
	}
	
	/**
	 * Draw focus around this figure
	 */
	@Override
	public void drawFocus(){
		if(isVisible()){
			fpa.stroke(255, 0,0);
			fpa.noFill();
			fpa.ellipseMode(FigureApplet.CORNERS);
			fpa.ellipse(getLeft(), getTop(), getLeft() + width, getTop() + height);
		}
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		float w2 = width/2;
		float h2 = height/2;
		float X = getLeft() + w2;
		float Y = getTop() + h2;
		float ex =  (mousex - X) / w2;
		float ey = 	(mousey - Y) / h2;
		boolean b =  ex * ex + ey * ey <= 1;
		//System.err.println("ellipse.mouseInside: " + b);
		return b;
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey, float centerX, float centerY){
		float w2 = width/2;
		float h2 = height/2;
		float ex =  (mousex - centerX) / w2;
		float ey = 	(mousey - centerY) / h2;
		boolean b =  ex * ex + ey * ey <= 1;
		//System.err.println("ellipse.mouseInside: " + b);
		return b;
	}

}
