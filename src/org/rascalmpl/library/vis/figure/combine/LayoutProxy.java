package org.rascalmpl.library.vis.figure.combine;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public abstract class LayoutProxy extends WithInnerFig {

	// Figure which is merely a wrapper for the inner figure from a layout perspective
	
	public LayoutProxy(Figure inner, PropertyManager properties) {
		super(inner, properties);
		if(inner!=null){
			properties.stealExternalPropertiesFrom(inner.prop);
		}
	}

	@Override
	public void computeMinSize() {
		minSize.set(innerFig.minSize);
		resizable.set(innerFig.resizable);
	}

	@Override
	public void resizeElement(Rectangle view) {
		innerFig.size.set(size);
		innerFig.location.set(0,0);
	}
	

	protected void setInnerFig(Figure inner){
		if(inner!=null){
			children = new Figure[1];
			children[0] = inner;
		} else {
			children = EMPTY_ARRAY;
		}
		innerFig = inner;
	}
	
	@Override
	public void connectArrowFrom(double left, double top, double X, double Y,
			double fromX, double fromY, Figure toArrow, GraphicsContext gc, List<IHasSWTElement> visibleSWTElements ) {
		if(children.length > 0){
			children[0].connectArrowFrom(left, top, X, Y, fromX, fromY, toArrow, gc, visibleSWTElements);
		}
	}
	
}
