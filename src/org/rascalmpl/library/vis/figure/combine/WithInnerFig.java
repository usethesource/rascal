package org.rascalmpl.library.vis.figure.combine;


import static org.rascalmpl.library.vis.properties.TwoDProperties.GROW;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.vector.Dimension;

public abstract class WithInnerFig extends Figure {
	

	final static boolean debug = false;
	public static final Figure[] EMPTY_ARRAY = new Figure[0];
	public Figure innerFig;
	

	public WithInnerFig(Figure inner, PropertyManager properties) {
		super(properties);
		this.innerFig = inner;
		setInnerFig(inner);
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

	public double getGrowFactor(Dimension d){
		return Math.max(prop.get2DReal(d, GROW), 1.0 / innerFig.prop.get2DReal(d,SHRINK));
	}
	
	
	
}
