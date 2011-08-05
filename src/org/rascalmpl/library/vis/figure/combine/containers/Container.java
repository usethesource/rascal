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
package org.rascalmpl.library.vis.figure.combine.containers;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.WithInnerFig;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Rectangle;


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

	public Container(Figure inner, PropertyManager properties) {
		super(inner,properties);
		
	}
	
	public void bbox(){
		if(innerFig!=null){
			innerFig.bbox();
		}
		minSize.clear();
		for(boolean flip : BOTH_DIMENSIONS){
			computeMinWidth(flip);
		}
		setResizable();
		super.bbox();
		
	}
	
	public void computeMinWidth(boolean flip){
		
		if(innerFig!=null){ 
			minSize.setWidth(flip, innerFig.minSize.getWidth(flip) * getGrowFactor(flip) + getLineWidthProperty());
		}
	}
	
	double getGrowFactor(boolean flip){
		return Math.max(getHGrowProperty(flip), 1.0 / innerFig.getHShrinkProperty(flip));
	}
	
	
	public void layout(){
		if(innerFig == null) return;
		double lw = getLineWidthProperty();
		innerFigLocation.clear();
		for(boolean flip : BOTH_DIMENSIONS){
				double sizeWithouthBorders = size.getWidth(flip) - lw ;
				double innerDesiredWidth =  sizeWithouthBorders / getGrowFactor(flip);
				innerFig.takeDesiredWidth(flip, innerDesiredWidth);
		}
		innerFig.layout();
		for(boolean flip : BOTH_DIMENSIONS){
			innerFigLocation.setX(flip, (size.getWidth(flip) - innerFig.size.getWidth(flip)) * innerFig.getHAlignProperty(flip));
		}
	}

	@Override
	public
	void draw(GraphicsContext gc) {
		applyProperties(gc);
		drawContainer(gc);
		super.draw(gc);
	}
	
	@Override
	public void drawPart(Rectangle r,GraphicsContext gc){
		
		applyProperties(gc);
		drawContainer(gc);
		super.drawPart(r,gc);
	}
	
	
	/**
	 * drawContainer: draws the graphics associated with the container (if any). 
	 * It is overridden by subclasses.
	 */
	
	abstract void drawContainer(GraphicsContext gc);
	
	/**
	 * @return the actual container name, e.g. box, ellipse, ...
	 */
	
	abstract String containerName();
	
	
	@Override
	public String  toString(){
		return new StringBuffer(containerName()).append("(").
		append(getLeft()).append(",").
		append(getTop()).append(",").
		append(minSize.getWidth()).append(",").
		append(minSize.getHeight()).append(",").append(size.getWidth()).append(",").
		append(size.getHeight()).append(")").toString();
	}
	

}
