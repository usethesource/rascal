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

import static org.rascalmpl.library.vis.properties.Properties.FILL_COLOR;
import static org.rascalmpl.library.vis.properties.Properties.FONT_COLOR;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Rectangle;


public abstract class SWTWidgetFigure<WidgetType extends Control> extends Figure implements IHasSWTElement{

	public WidgetType widget;
	
	SWTWidgetFigure(IFigureConstructionEnv env,PropertyManager properties){
		super(properties);
		children = childless;
	}
	
	@Override 
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		env.addSWTElement(widget);
		widget.setBackground(SWTFontsAndColors.getRgbColor(prop.getColor(FILL_COLOR)));
		widget.setForeground(SWTFontsAndColors.getRgbColor(prop.getColor(FONT_COLOR)));
	}
	
	@Override
	public void computeMinSize(){
		Point p = widget.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		minSize.setX(p.x);
		minSize.setY(p.y);
		
	}
	
	@Override
	public boolean containsSWTElement() {
		return true;
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		
	}

	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements) {
		widget.setFont(gc.getFont());
		visibleSWTElements.add(this);
		int rx = FigureMath.round(globalLocation.getX() + gc.getTranslateX());
		int ry = FigureMath.round(globalLocation.getY() + gc.getTranslateY());
		if(widget.getLocation().x != rx || widget.getLocation().y != ry){
			widget.setLocation(rx,ry);
		 }
			rx = FigureMath.ceil(size.getX());
			ry = FigureMath.ceil(size.getY());
			 if(widget.getSize().x != rx  || widget.getSize().y != ry){
				widget.setSize(rx,
						ry);
			}
				Color b = SWTFontsAndColors.getRgbColor(prop.getColor(FILL_COLOR));
				if(!widget.getBackground().equals(b)){
					widget.setBackground(b);
				}
				b = SWTFontsAndColors.getRgbColor(prop.getColor(FONT_COLOR));
				if(!widget.getForeground().equals(b)){
					widget.setForeground(b);
				}
					// TODO : set Font!
			if(!widget.getVisible()){
				widget.setVisible(true);
			}

	}
	
	@Override
	public void destroyElement(IFigureConstructionEnv env) { 
		if(widget!=null)widget.dispose();
	}
	

	@Override
	public Control getControl() {
		return widget;
	}

	public void hideElement(IFigureConstructionEnv env) {
		setVisible(false);
	}
	
	@Override
	public void setVisible(boolean visible){
		if(!widget.isDisposed() && !visible && widget.getVisible() ){
			widget.setLocation(-10 - widget.getSize().x, -10 - widget.getSize().y);
			//widget.setVisible(false);
		} else if(!widget.isDisposed() && visible &&  !widget.getVisible() ){
			widget.setVisible(visible);
		}
	}
	
	@Override
	public int getStableOrder(){
		return sequenceNr;
	}
}
