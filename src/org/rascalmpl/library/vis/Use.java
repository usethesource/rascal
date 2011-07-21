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


import org.rascalmpl.library.vis.containers.WithInnerFig;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
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

	public Use(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa, inner, properties);
		if(debug)System.err.println("use.init: width=" + minSize.getWidth() + ", height=" + minSize.getHeight());
	}

	@Override
	public 
	void bbox(){
		innerFig.bbox();
		minSize.setWidth(innerFig.minSize.getWidth());
		minSize.setHeight(innerFig.minSize.getHeight());
		if(debug)System.err.println("use.bbox: width=" + minSize.getWidth() + ", height=" + minSize.getHeight());
		setNonResizable();
		super.bbox();
	}

	@Override
	public
	void draw(double left, double top, GraphicsContext gc) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties(gc);
		
		innerFig.draw(left + getHAlignProperty()*(minSize.getWidth() - innerFig.minSize.getWidth()),
					top  + getVAlignProperty()*(minSize.getHeight() - innerFig.minSize.getHeight()), gc);
	}
}
