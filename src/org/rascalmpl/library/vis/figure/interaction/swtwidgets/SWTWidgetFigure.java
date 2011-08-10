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

package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;
import org.rascalmpl.library.vis.swt.zorder.IHasZOrder;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;
import org.rascalmpl.library.vis.util.FigureMath;


public abstract class SWTWidgetFigure<WidgetType extends Control> extends Figure implements IHasZOrder{

	public WidgetType widget;
	int zorder;
	
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
		widget.setSize(FigureMath.round(size.getWidth()),
				FigureMath.round(size.getHeight()));
	}

	@Override
	public void draw(GraphicsContext gc) {
		widget.setLocation(FigureMath.round(getLeft() + gc.getTranslateX()),
		         FigureMath.round(getTop() + gc.getTranslateY()));
		// SWT draws this itself! this is only layout
		widget.setBackground(SWTFontsAndColors.getRgbColor(getFillColorProperty()));
		widget.setForeground(SWTFontsAndColors.getRgbColor(getFontColorProperty()));
		widget.setVisible(true);
		gc.registerSWTElement(this);
	}
	
	@Override
	public void destroy() {
		if(widget!=null)widget.dispose();
	}
	

	public void setSWTZOrder(ISWTZOrdering zorder){
		zorder.registerControl(this);
	}
	
	
	public void setZOrder(int depth){
		zorder = depth;
	}
	
	public int getZOrder(){
		return zorder;
	}
	
	public Control getElement(){
		return widget;
	}
	
	public int getStableOrder(){
		return sequenceNr;
	}
	
	public void setVisible(boolean visible){
		if(!visible){
			widget.setLocation(-10 - widget.getSize().x, -10 - widget.getSize().y);
		} else {
			System.out.printf("Making %d visible\n",sequenceNr);
		}
		widget.setVisible(visible);
	}
}
