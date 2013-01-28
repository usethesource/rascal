/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.combine;


import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GAP;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GROW;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;

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
	

	@Override
	public void computeMinSize() {
		if(innerFig!=null){ 
			for(Dimension d : HOR_VER){
				minSize.set(d, innerFig.minSize.get(d) * getGrowFactor(d));
				minSize.setMax(d, innerFig.minSize.get(d) + 2 * prop.get2DReal(d, GAP));
//				if(!innerFig.resizable.get(d) && prop.is2DPropertySet(d, GROW)){
//					resizable.set(d,false);
//				}
			}
		}
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		if(innerFig == null) return;
		for(Dimension d : HOR_VER){
				double innerDesiredWidth =  size.get(d) / getGrowFactor(d);
				innerDesiredWidth = Math.min(size.get(d) - 2 * prop.get2DReal(d, GAP), innerDesiredWidth);
				innerFig.size.set(d, innerDesiredWidth);
				innerFig.localLocation.set(d, (size.get(d) - innerFig.size.get(d)) * innerFig.prop.get2DReal(d, ALIGN));
		}
	}
	
}
