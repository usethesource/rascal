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
package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;


/**
 * A container represents a visual element that can contain another (nested) visual element called the "inside" element.
 * Typical examples are Boxes and Ellipses that may contain another element.
 * 
 * A container has the following behaviour:
 * - It has a bounding box of its own unless interaction due to a moueOver overrules it.
 * - It draws itself (using drawContainer).
 * - It draws the inside element provided that it fits in the container.
 * - It always draws the inside element on mouseOver.
 * 
 * @author paulk
 * 
 */

public abstract class Container extends WithInnerFig {

	final private static boolean debug = false;

	public Container(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa,inner,properties);
		
	}
	
	public void bbox(){
		if(innerFig!=null)innerFig.bbox();
		minSize.clear();
		for(boolean flip : BOTH_DIMENSIONS){
			computeMinWidth(flip);
		}
		setResizable();
		super.bbox();
		
	}
	
	public void computeMinWidth(boolean flip){
		double lw = getLineWidthProperty();
		
		minSize.setWidth(flip,lw);
		if(innerFig!=null){ 
			minSize.addWidth(flip, innerFig.minSize.getWidth(flip) * getGrowFactor(flip));
		}
	}
	
	double getGrowFactor(boolean flip){
		return Math.max(getHGrowProperty(flip), 1.0 / innerFig.getHShrinkProperty(flip));
	}
	
	public void layout(){
		double lw = getLineWidthProperty();
		innerFigLocation.clear();
		for(boolean flip : BOTH_DIMENSIONS){
			if(innerFig != null) {
				double sizeWithouthBorders = size.getWidth(flip) - 2*lw ;
				double innerDesiredWidth =  sizeWithouthBorders / getGrowFactor(flip);
				innerFig.takeDesiredWidth(flip, innerDesiredWidth);
				innerFigLocation.addX(flip, (size.getWidth(flip) - innerFig.size.getWidth(flip)) * innerFig.getHAlignProperty(flip));
				innerFig.globalLocation.setX(flip,globalLocation.getX(flip) + innerFigLocation.getX(flip));
			}
		}
		if(innerFig!=null)innerFig.layout();
		
	}

	@Override
	public
	void draw(double left, double top) {
		//System.out.printf("drawing %f %f %f %f\n", left, top, size.getWidth(), size.getHeight());

		setLeft(left);
		setTop(top);
		applyProperties();
		drawContainer();
		if(innerFig!=null) {
			//System.out.printf("translate %f %f", innerFigLocation.getX(), innerFigLocation.getY());
			innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
		}	
	}

	

	/**
	 * drawContainer: draws the graphics associated with the container (if any). 
	 * It is overridden by subclasses.
	 */
	
	abstract void drawContainer();
	
	/**
	 * @return the actual container name, e.g. box, ellipse, ...
	 */
	
	abstract String containerName();
	
	@Override 
	public boolean keyPressed(int key, int keyCode){
		if(innerFig != null)
			return innerFig.keyPressed(key, keyCode);
		return false;
	}
	
	@Override
	public String  toString(){
		return new StringBuffer(containerName()).append("(").
		append(getLeft()).append(",").
		append(getTop()).append(",").
		append(minSize.getWidth()).append(",").
		append(minSize.getHeight()).append(")").toString();
	}

}
