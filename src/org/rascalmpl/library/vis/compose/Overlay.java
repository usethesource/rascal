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

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
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

	public Overlay(IFigureApplet fpa, PropertyManager properties, IList elems,  IList childProps,  IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
	}
	
	@Override
	public void bbox(double desiredWidth, double desiredHeight){
		
		topAnchor = bottomAnchor = leftAnchor = rightAnchor = 0;
		
		for(Figure ve : figures){
			ve.bbox(desiredWidth, desiredHeight);
			topAnchor = max(topAnchor, ve.topAlign());
			bottomAnchor = max(bottomAnchor, ve.bottomAlign());
			leftAnchor = max(leftAnchor, ve.leftAlign());
			rightAnchor = max(rightAnchor, ve.rightAlign());
		}
		width = leftAnchor + rightAnchor;
		height = topAnchor + bottomAnchor;
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
		determinePlacement();
	}

	private void determinePlacement() {
		for(int i = 0 ; i < figures.length ; i++){
			xPos[i] = leftAnchor - figures[i].leftAlign();
			yPos[i] = topAnchor - figures[i].topAlign();
		}
	}
}
