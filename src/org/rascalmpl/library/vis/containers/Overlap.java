package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ISWTZOrdering;

public class Overlap extends FigureWithNonLocalFigure{
	
	public Overlap(Figure under, Figure over, PropertyManager properties){
		super(under,over,properties);
	}
	
	
	public void layout(){
		super.layout();
		nonLocalFigure.globalLocation.set(innerFig.globalLocation);
		for(boolean flip : BOTH_DIMENSIONS){
				nonLocalFigure.takeDesiredWidth(flip,innerFig.size.getWidth(flip) * nonLocalFigure.getHShrinkProperty(flip));
				nonLocalFigure.globalLocation.addX(flip,
						(innerFig.size.getWidth(flip) - nonLocalFigure.size.getWidth(flip)) * nonLocalFigure.getHAlignProperty(flip));
		}
		nonLocalFigure.layout();
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		zorder.register(innerFig);
		innerFig.setSWTZOrder(zorder);
		zorder.pushOverlap();
		zorder.registerOverlap(this);
		nonLocalFigure.setSWTZOrder(zorder);
		zorder.popOverlap();
	}
}
