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
package org.rascalmpl.library.vis.figure.combine.containers;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.FigureMath;



/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {
	
	final static boolean debug = false;

	public Ellipse(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}
	
	@Override
	void drawContainer(GraphicsContext gc){
		if (debug) System.err.println("drawContainer:"+this.getClass()+" "+getLeft()+" "+getTop()+" "+size.getWidth()+" "+size.getHeight());
		double lw = getLineWidthProperty();
		gc.ellipse(getLeft() , getTop(), size.getWidth()-lw, size.getHeight()-lw);
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
	public void connectArrowFrom(double left, double top, double X, double Y, double fromX, double fromY,
			Figure toArrow,GraphicsContext gc){
		
		if(fromX == X)
			fromX += 0.00001;
        double theta = FigureMath.atan((fromY - Y) / (fromX - X));
        if(theta < 0){
        	if(fromX < X )
        		theta += FigureMath.PI;
        } else {
        	if(fromX < X )
        		theta += FigureMath.PI;
        }
        double sint = FigureMath.sin(theta);
        double cost = FigureMath.cos(theta);
        double r = minSize.getHeight() * minSize.getWidth() / (4 * FigureMath.sqrt((minSize.getHeight()*minSize.getHeight()*cost*cost + minSize.getWidth()*minSize.getWidth()*sint*sint)/4));
        double IX = X + r * cost;
        double IY = Y + r * sint;
        
   //     fpa.line(left + fromX, top + fromY, left + IX, top + IY);
        
        if(toArrow != null){
        	gc.pushMatrix();
        	gc.translate(left + IX, top + IY);
        	gc.rotate(FigureMath.radians(-90) + theta);
        	toArrow.draw(gc);
        	gc.popMatrix();
        }
	}
	
	@Override
	public boolean mouseInside(double mousex, double mousey){
		double w2 = size.getWidth()/2;
		double h2 = size.getHeight()/2;
		double X = getLeft() + w2;
		double Y = getTop() + h2;
		double ex =  (mousex - X) / w2;
		double ey = 	(mousey - Y) / h2;
		boolean b =  ex * ex + ey * ey <= 1;
		//System.err.println("ellipse.mouseInside: " + b);
		return b;
	}

}
