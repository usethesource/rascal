package org.rascalmpl.library.vis.figure.combine;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public abstract class LayoutProxy extends WithInnerFig {

	// Figure which is merely a wrapper for the inner figure from a layout perspective
	
	public LayoutProxy(Figure inner, PropertyManager properties) {
		super(inner, properties);
		if(inner!=null){
			properties.stealLayoutPropertiesFrom(inner.prop);
			
			properties.stealProperty(Properties.ID, inner.prop);
		}
	}

	@Override
	public void computeMinSize() {
		
		minSize.set(innerFig.minSize);

	}

	@Override
	public void resizeElement(Rectangle view) {
		innerFig.size.set(size);
		innerFig.location.set(0,0);
	}
	
	@Override
	protected void setInnerFig(Figure inner){
		super.setInnerFig(inner);
		if(inner!=null){
			prop.stealLayoutPropertiesFrom(inner.prop);
		}
	}
	
}
