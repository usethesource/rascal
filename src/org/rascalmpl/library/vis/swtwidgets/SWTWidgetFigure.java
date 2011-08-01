/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Atze van der Ploeg - ploeg@cwi.nl (CWI)
 *******************************************************************************/

package org.rascalmpl.library.vis.swtwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.ISWTZOrdering;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;


public abstract class SWTWidgetFigure<WidgetType extends Control> extends Figure{

	public WidgetType widget;
	
	SWTWidgetFigure(IFigureConstructionEnv env,PropertyManager properties){
		super(properties);
		
	}

	@Override
	public void bbox(){
		Point p = widget.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		setResizable();
		super.bbox();
	}
	
	@Override
	public void layout() {
		widget.setLocation(FigureApplet.round(getLeft()),
		         FigureApplet.round(getTop()));
		widget.setSize(FigureApplet.round(size.getWidth()),
				FigureApplet.round(size.getHeight()));
	}

	@Override
	public void draw(GraphicsContext gc) {
		// SWT draws this itself! this is only layout
		widget.setBackground(SWTFontsAndColors.getRgbColor(getFillColorProperty()));
		widget.setForeground(SWTFontsAndColors.getRgbColor(getFontColorProperty()));
	}
	
	@Override
	public void destroy() {
		if(widget!=null)widget.dispose();
	}
	

	public void setSWTZOrder(ISWTZOrdering zorder){
		zorder.registerControl(widget);
	}
	
	public void suspend(){
		widget.setVisible(false);
	}
	
	public void activate(){
		widget.setVisible(true);
	}
}
