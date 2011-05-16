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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


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

	public Container(IFigureApplet fpa, PropertyManager properties, IConstructor innerCons, IList childProps, IEvaluatorContext ctx) {
		super(fpa,properties,innerCons,childProps,ctx);
	}

	@Override
	public 
	void bbox(double desiredWidth, double desiredHeight){
		double lw = getLineWidthProperty();
		if(desiredWidth != Figure.AUTO_SIZE){ 
			if(desiredWidth < 1.0f){
				desiredWidth = width = 1.0f;
			} else {
				width = desiredWidth;
			}
		}
		if(desiredHeight != Figure.AUTO_SIZE){ 
			if(desiredHeight < 1.0f){
				desiredHeight = height = 1.0f;
			} else {
				height = desiredHeight;
			}
		}
		if(isWidthPropertySet()){
			desiredWidth = width = getWidthProperty();
		} 
		if(isHeightPropertySet()){
			desiredHeight = height = getHeightProperty();
		} 
		
		if(innerFig != null){
			double innerDesiredWidth,
			      innerDesiredHeight;
			double spacingX, spacingY;
			spacingX = spacingY = 0;
			if(desiredWidth != AUTO_SIZE){
				if(isHGapFactorPropertySet() || !isHGapPropertySet()){
					spacingX = (getHGapFactorProperty()) * desiredWidth;
				} else { // HGapProperty set
					spacingX = 2 * getHGapProperty();
				}
				innerDesiredWidth = desiredWidth - spacingX - 2*lw;
			} else {
				innerDesiredWidth = Figure.AUTO_SIZE;
			}
			if(desiredHeight != AUTO_SIZE){
				if(isVGapFactorPropertySet() || !isVGapPropertySet()){
					spacingY = (getVGapFactorProperty()) * desiredHeight;
				} else { // HGapProperty set
					spacingY =  2 * getVGapProperty();
				}
				innerDesiredHeight = desiredHeight - spacingY - 2*lw;
			} else {
				innerDesiredHeight = Figure.AUTO_SIZE;
			}
			innerFig.bbox(innerDesiredWidth,innerDesiredHeight);
			if(desiredWidth == AUTO_SIZE || innerFig.width > innerDesiredWidth){
				if(isHGapFactorPropertySet() || !isHGapPropertySet()){
					// the next formula can be obtained by rewriting hGapFactor = gapsSize / (innerFigureSize + gapsSize)
					spacingX = (innerFig.width / (1/getHGapFactorProperty() - 1));
				} else { // HGapProperty set
					spacingX = 2 * getHGapProperty();
				}
				width = innerFig.width + spacingX + 2*lw;
			}
			if(desiredHeight == AUTO_SIZE || innerFig.height > innerDesiredHeight){
				if(isVGapFactorPropertySet() || !isVGapPropertySet()){
					// the next formula can be obtained by rewriting hGapFactor = gapsSize / (innerFigureSize + gapsSize)
					spacingY = (innerFig.height / (1/getVGapFactorProperty() - 1));
				} else { // HGapProperty set
					spacingY = 2 * getVGapProperty();
				}
				height = innerFig.height + spacingY + 2*lw;
			}
			if(desiredWidth != AUTO_SIZE && innerFig.width != innerDesiredWidth){
				spacingX = desiredWidth - 2 * lw - innerFig.width;
			}
			if(desiredHeight != AUTO_SIZE && innerFig.height != innerDesiredHeight){
				spacingY = desiredHeight - 2 * lw - innerFig.height;
			}
			innerFigX = lw + innerFig.getHAlignProperty()*spacingX;
			innerFigY = lw + innerFig.getVAlignProperty()*spacingY;
		} else {
			if(desiredWidth == AUTO_SIZE){
				width = getWidthProperty();
			} 
			if(desiredHeight == AUTO_SIZE){
				height = getHeightProperty();
			}
		}
		
		if(debug)System.err.printf("container.bbox: width=%f, height=%f, hanchor=%f, vanchor=%f\n", width, height, getHAlignProperty(), getVAlignProperty());
	}

	@Override
	public
	void draw(double left, double top) {
		if(!isVisible())
			return;
		this.setLeft(left);
		this.setTop(top);
	
		applyProperties();
		if(debug)System.err.printf("%s.draw: left=%f, top=%f, width=%f, height=%f, hanchor=%f, vanchor=%f\n", containerName(), left, top, width, height, getHAlignProperty(), getVAlignProperty());

		if(height > 0 && width > 0){
			drawContainer();
			if(innerFig != null && isNextVisible()){
				if(debug)System.err.printf("%s.draw2:  inside.width=%f\n",  containerName(), innerFig.width);
				if(innerFits()) {
					fpa.incDepth();
					innerDraw();
					fpa.decDepth();
				}
			}
		}
	}
	
	/**
	 * @return true if the inner element fits in the current container.
	 */
	boolean innerFits(){
		return innerFig.width + 2*getHGapProperty() <= width && innerFig.height + 2*getVGapProperty() <= height;
	}
	
	/**
	 * If the inside  element fits, draw it.
	 */
	void innerDraw(){
		innerFig.draw(max(0, getLeft() + innerFigX),
			    	  max(0, getTop()  + innerFigY));
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
		append(width).append(",").
		append(height).append(")").toString();
	}

}
