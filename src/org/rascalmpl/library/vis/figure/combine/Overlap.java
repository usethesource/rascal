package org.rascalmpl.library.vis.figure.combine;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;

public class Overlap extends FigureWithNonLocalFigure{
	
	public Overlap(Figure under, Figure over, PropertyManager properties){
		super(under,over,properties);
	}
	

	@Override
	public void bbox(){
		super.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			minSize.setWidth(flip, Math.max(innerFig.minSize.getWidth(flip), nonLocalFigure.minSize.getWidth(flip)/ nonLocalFigure.getHShrinkProperty(flip)));
		}
	}
	
	
	public void layout(){
		super.layout();
		for(boolean flip : BOTH_DIMENSIONS){
				nonLocalFigure.takeDesiredWidth(flip,innerFig.size.getWidth(flip) * nonLocalFigure.getHShrinkProperty(flip));
		}
		nonLocalFigure.layout();
		for(boolean flip : BOTH_DIMENSIONS){
			nonLocalFigureLoc.setX(flip,
				(innerFig.size.getWidth(flip) - nonLocalFigure.size.getWidth(flip)) * nonLocalFigure.getHAlignProperty(flip));
		}
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		zorder.pushOverlap();
		zorder.register(innerFig);
		innerFig.setSWTZOrder(zorder);
		zorder.pushOverlap();
		zorder.registerOverlap(this);
		nonLocalFigure.setSWTZOrder(zorder);
		zorder.popOverlap();
		zorder.popOverlap();
	}
}
