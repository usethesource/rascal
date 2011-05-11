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
	
	@Override
	public void drawWithMouseOver(double left, double top){
		draw(left, top);
		if(innerFig != null && innerFig.isVisibleInMouseOver())
			innerDrawWithMouseOver(left, top);
		Figure mo = getMouseOver();
		if(mo != null && mo.isVisibleInMouseOver()){
			//System.err.printf("Mouse over width %f height %f", mo.width,mo.height);
			mo.drawWithMouseOver(max(0, left + mo.getHAlignProperty()*(width  - mo.width)),
			    				 max(0, top  + mo.getVAlignProperty()*(height - mo.height)));
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
	
	void innerDrawWithMouseOver(double left, double top){
		if(innerFig != null){
			double hgap = getHGapProperty();
			double vgap = getVGapProperty();
			innerFig.drawWithMouseOver(max(0, left + hgap + innerFig.getHAlignProperty()*(width  - innerFig.width  - 2 * hgap)),
			    	                   max(0, top  + vgap + innerFig.getVAlignProperty()*(height - innerFig.height - 2 * vgap)));
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
	public boolean mouseOver(int mouseX, int mouseY, double centerX, double centerY, boolean mouseInParent){
		if(debug){System.err.println("mouseOver: " + this);
				System.err.printf("mouse: %d, %d; center: %f, %f\n", mouseX, mouseY, centerX, centerY);
		}
		boolean mouseInMe =  mouseInside(mouseX, mouseY);
		boolean mouseOverActive = false;
		if(debug)System.err.println("mi = " + mouseInMe);
		
		if(innerFig != null){
			if(innerFits()){
				mouseOverActive = innerFig.mouseOver(mouseX, mouseY, centerX, centerY, mouseInMe);
			} else if(innerFig.isVisibleInMouseOver()){
				if(innerFig.mouseOver(mouseX, mouseY, mouseInMe)){
					mouseOverActive = true;
				}
			} else if (mouseInMe){
				innerFig.bbox(Figure.AUTO_SIZE,Figure.AUTO_SIZE);
				innerFig.mouseOver(mouseX, mouseY, centerX, centerY, mouseInMe);
				mouseOverActive = true;
			}
		}
		
		Figure mo = getMouseOver();
		if(mo != null){
			if(mo.isVisibleInMouseOver()){
				if(mo.mouseOver(mouseX, mouseY, mouseInMe)){
					mouseOverActive = true;
				}
			} else if(mouseInMe){
				mo.bbox(Figure.AUTO_SIZE,Figure.AUTO_SIZE);
				mo.mouseOver(mouseX, mouseY, centerX, centerY, mouseInMe);
				mouseOverActive = true;
			}
		}
		
		boolean status =  mouseInMe  || mouseOverActive || mouseInParent;
			
		if(!status)
			clearVisibleInMouseOver();
		else {
			setVisibleInMouseOver(true);
			fpa.registerMouseOver(this);
		}
		
		if(debug)System.err.println("mouseOver returns " + status + ", mi = " + mouseInMe + ", " + ", moa = " + mouseOverActive + ", " + this);
		return status;
	}
	
	@Override
	public void clearVisibleInMouseOver(){
		if(innerFig != null)
			innerFig.clearVisibleInMouseOver();
		Figure mo = getMouseOver();
		if(mo != null)
			mo.clearVisibleInMouseOver();
		setVisibleInMouseOver(false);
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, Object e){
		if(!isVisible())
			return false;
		System.err.println(containerName() + ".mousePressed: " + mouseX + ", " + mouseY);
	
		if(innerFig != null && isNextVisible() && innerFig.mousePressed(mouseX, mouseY, null))
				return true;
		
		Figure mo = getMouseOver();
		
		if(mo != null && mo.isVisibleInMouseOver() && mo.mousePressed(mouseX, mouseY, null))
			return true;
		
		if(mouseInside(mouseX, mouseY)){
			fpa.registerFocus(this);
			return super.mousePressed(mouseX, mouseY, e);
		}
		return false;
	}
	
	@Override
	public void drag(double mousex, double mousey){
		if (debug) System.err.println("Drag to " + mousex + ", " + mousey + ": " + this);
		if(!isDraggable())
			System.err.println("==== ERROR: DRAG NOT ALLOWED ON " + this + " ===");
		setLeftDragged(getLeftDragged() + (mousex - getLeft()));
		setTopDragged(getTopDragged() + (mousey - getTop()));
	}
	
	@Override
	public boolean mouseDragged(int mousex, int mousey){
		if(innerFits() && innerFig.isDraggable() && innerFig.mouseDragged(mousex, mousey))
			return true;
		
		if(isDraggable() && mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			drag(mousex, mousey);
			if (debug) System.err.printf("Figure.mouseDragged: %f,%f\n", getLeftDragged(), getTopDragged());
			return true;
		}
		return false;
	}
	
	

//	
//	@Override
//	public void drawMouseOverFigure(int mouseX, int mouseY){
//		LinkedList<Figure>  below = getParents();
//		
//	//	for(Figure f : below)
//	//		System.err.println("\t" + f);
//		
//		Figure root = below.pop();
//		double cX = root.getCenterX();
//		double cY = root.getCenterY();
//		
//	//	System.err.println("cX = " + cX + ", cY = " + cY);
//		
//		for(Figure f : below){
//	//		System.err.println("draw: " + f);
//			f.draw(max(0, cX - f.width/2), max(0, cY - f.height/2));
//		}
//		//draw(max(0, cX - width/2), max(0, cY - height/2));
//			
//		
////		if(isVisible()){
////			if(hasMouseOverFigure()){
////				Figure mo = getMouseOverFigure();
////				if(mo.mouseInside(mouseX, mouseY))
////					mo.draw(max(0, left + (width - mo.width)/2f), max(0, top + (height - mo.height)/2));
////				mo.drawMouseOverFigure(mouseX, mouseY);
////			} else if(innerFig != null){
////				if(!fpa.isRegistered(innerFig))
////					return;
////				innerDraw();
////			}
////		}
////	}
	

	
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
