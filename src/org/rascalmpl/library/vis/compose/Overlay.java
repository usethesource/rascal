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
package org.rascalmpl.library.vis.compose;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


/**
 * 
 * Overlay elements by stacking them:
 * - when alignAnchors==true aligned around their anchor point
 * - otherwise aligned according to current alignment settings.
 * 
 * @author paulk
 *
 */
public class Overlay extends Compose {
	
	private static boolean debug = false;
	double topAnchor = 0;
	double bottomAnchor = 0;
	double leftAnchor = 0;
	double rightAnchor = 0;

	public Overlay(IFigureApplet fpa, Figure[] figures, PropertyManager properties) {
		super(fpa, figures, properties);
	}
	
	@Override
	public void bbox(){
		
		topAnchor = bottomAnchor = leftAnchor = rightAnchor = 0;
		
		for(Figure ve : figures){
			ve.bbox();
			topAnchor = Math.max(topAnchor, ve.topAlign());
			bottomAnchor = Math.max(bottomAnchor, ve.bottomAlign());
			leftAnchor = Math.max(leftAnchor, ve.leftAlign());
			rightAnchor = Math.max(rightAnchor, ve.rightAlign());
		}
		minSize.setWidth(leftAnchor + rightAnchor);
		minSize.setHeight(topAnchor + bottomAnchor);
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", minSize.getWidth(), minSize.getHeight());
		determinePlacement();
		super.bbox();
	}

	private void determinePlacement() {
		for(int i = 0 ; i < figures.length ; i++){
			xPos[i] = leftAnchor - figures[i].leftAlign();
			yPos[i] = topAnchor - figures[i].topAlign();
		}
	}
}
