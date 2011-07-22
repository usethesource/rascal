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

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.IFigureExecutionEnvironment;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;



/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {
	
	final static boolean debug = false;

	public Ellipse(IFigureExecutionEnvironment fpa, Figure inner, PropertyManager properties) {
		super(fpa, inner, properties);
	}
	
	@Override
	void drawContainer(GraphicsContext gc){
		if (debug) System.err.println("drawContainer:"+this.getClass()+" "+getLeft()+" "+getTop()+" "+size.getWidth()+" "+size.getHeight());
		gc.ellipse(getLeft(), getTop(), size.getWidth(), size.getHeight());
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
        double theta = FigureApplet.atan((fromY - Y) / (fromX - X));
        if(theta < 0){
        	if(fromX < X )
        		theta += FigureApplet.PI;
        } else {
        	if(fromX < X )
        		theta += FigureApplet.PI;
        }
        double sint = FigureApplet.sin(theta);
        double cost = FigureApplet.cos(theta);
        double r = minSize.getHeight() * minSize.getWidth() / (4 * FigureApplet.sqrt((minSize.getHeight()*minSize.getHeight()*cost*cost + minSize.getWidth()*minSize.getWidth()*sint*sint)/4));
        double IX = X + r * cost;
        double IY = Y + r * sint;
        
   //     fpa.line(left + fromX, top + fromY, left + IX, top + IY);
        
        if(toArrow != null){
        	toArrow.bbox();
        	gc.pushMatrix();
        	gc.translate(left + IX, top + IY);
        	gc.rotate(FigureApplet.radians(-90) + theta);
        	toArrow.draw(-toArrow.minSize.getWidth()/2, 0, gc);
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
