/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.containers.WithInnerFig;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Use another element. Mostly used to override properties.
 * 
 * TODO: This does not currently work as expected!
 * 
 * @author paulk
 *
 */
public class Use extends WithInnerFig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean debug = false;

	public Use(IFigureApplet fpa, PropertyManager properties, IConstructor inside, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties,inside,childProps, ctx);
		if(debug)System.err.println("use.init: width=" + width + ", height=" + height);
	}

	@Override
	public 
	void bbox(double desiredWidth, double desiredHeight){
		innerFig.bbox(AUTO_SIZE, AUTO_SIZE);
		width = innerFig.width;
		height = innerFig.height;
		if(debug)System.err.println("use.bbox: width=" + width + ", height=" + height);
	}

	@Override
	public
	void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		
		innerFig.draw(left + getHAlignProperty()*(width - innerFig.width),
					top  + getVAlignProperty()*(height - innerFig.height));
	}
}
