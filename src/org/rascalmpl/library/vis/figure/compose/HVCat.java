/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.compose;

import static org.rascalmpl.library.vis.properties.Properties.JUSTIFY;
import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.TwoDProperties;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;

/**
 * HVCat elements on consecutive rows. Width is determined by the width property, height is
 * determined by the number and size of the elements. This is similar to aligning words in
 * a text but is opposed to composition in a grid, where the elements are placed on fixed
 * grid positions.
 * 
 * @author paulk
 *
 */
// A HVCat is always wrapped in an WidthDependsOnHeightWrapper figure
public class HVCat extends WidthDependsOnHeight{
	static boolean debug = false;
	
	public HVCat(Dimension major,Figure[] figures, PropertyManager properties) {
		super(major,figures, properties);
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		if(size.get(major) < minSize.get(major)) {
			System.err.printf("Cannot ever fit! %s %s\n",size,minSize);
			return;
		}
		double majorSize = size.get(major);
		double y = 0;
		for(int i = 0 ; i < children.length ; ){
			double majorSizeLeft = majorSize;
			double majorSizeLeftExcludingGaps = majorSize;
			double maxMinor = 0;

			int startOfRow = i;
			while(i < children.length && majorSizeLeft >= children[i].minSize.get(major)){
				majorSizeLeftExcludingGaps-= children[i].minSize.get(major);
				majorSizeLeft-= children[i].minSize.get(major) + prop.get2DReal(major, TwoDProperties.GAP);
				maxMinor = Math.max(maxMinor,children[i].minSize.get(minor));
				i++;
			}
			int endOfRow = i ;
			double gap = prop.get2DReal(major, TwoDProperties.GAP);
			int nrOfGaps = endOfRow - startOfRow -1; 
			double x = 0;
			if(prop.getBool(JUSTIFY)){
				gap = majorSizeLeftExcludingGaps / (double)nrOfGaps;
			} else {
				x = (majorSizeLeftExcludingGaps - (nrOfGaps * gap)) * prop.getReal(Properties.INNER_ALIGN);
			}
			for(int j = startOfRow ; j < endOfRow ; j++){
				Figure child = children[j];
				child.size.set(child.minSize);
				child.location.set(major, x);
				child.location.set(minor, y + ( (maxMinor - child.minSize.get(minor)) * prop.get2DReal(minor,ALIGN)));
				x+= child.minSize.get(major) + gap;
			}
			y+=maxMinor +  prop.get2DReal(minor, TwoDProperties.GAP);
		}
		size.setMax(minor,y);
	}


}
