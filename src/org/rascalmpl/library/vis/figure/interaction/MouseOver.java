package org.rascalmpl.library.vis.figure.interaction;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;

public class MouseOver extends Overlap{

	static Figure empty = new Space(null, new PropertyManager());
	
	Figure mouseOverFig;
	boolean mouseOver;
	
	public MouseOver(Figure under, Figure mouseOverFig, PropertyManager properties) {
		super(under, mouseOverFig, properties);
		this.mouseOverFig = mouseOverFig;
	}
	
	@Override
	public void bbox(){
		super.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			if(nonLocalFigure.isHShrinkPropertySet(flip)){
				minSize.setWidth(flip, Math.max(innerFig.minSize.getWidth(flip), nonLocalFigure.minSize.getWidth(flip)/ nonLocalFigure.getHShrinkProperty(flip)));
			} else {
				minSize.setWidth(flip, innerFig.minSize.getWidth(flip));
			}
		}
	}
	
	public void setMouseOver(){
		nonLocalFigure = mouseOverFig;
		mouseOver = true;
	}
	
	public void unsetMouseOver(){
		nonLocalFigure = empty;
		mouseOver =false;
	}
	

	public void setSWTZOrder(ISWTZOrdering zorder){
		zorder.pushOverlap();
		zorder.register(innerFig);
		innerFig.setSWTZOrder(zorder);
		if(mouseOver){
			zorder.pushOverlap();
			zorder.registerOverlap(this);
			nonLocalFigure.setSWTZOrder(zorder);
			zorder.popOverlap();
			
		} 
		zorder.popOverlap();
		zorder.registerMouseOver(this);
	}
	
}
