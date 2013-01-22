/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt.applet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public class SWTZOrderManager implements IFigureChangedListener{
	
	List<Control> swtZOrder;
	List<CoverSWTCanvas> covers;
	FigureSWTApplet parent;
	boolean firstDrawSinceFigureChanged;
	List<Overlap> overlapFigures;
	public SWTZOrderManager(FigureSWTApplet parent,List<Overlap> overlapFigures) {
		swtZOrder = new LinkedList<Control>();
		covers = new ArrayList<CoverSWTCanvas>();
		this.parent = parent;
		firstDrawSinceFigureChanged = true;
		this.overlapFigures = overlapFigures;
	}
	
	public void dispose(){
		for(CoverSWTCanvas cover : covers){
			cover.dispose();
		}
	}
	
	public void draw(Rectangle part){
		for(CoverSWTCanvas cover : covers){
			if(cover.overlapsWith(part)){
				cover.relocate();
			} else {
				cover.setLocation(-10 - cover.getSize().x, -10 - cover.getSize().y);
			}
		}
		if(firstDrawSinceFigureChanged){
			setOrder();
			firstDrawSinceFigureChanged = false;
		}
	}
	
	public void clearSWTOrder(){
		swtZOrder.clear();
		dispose();
		covers.clear();
	}
	
	public void addSWTElement(Control c){
		swtZOrder.add(c);
	}
	
	public void addAboveSWTElement(Figure fig){
		CoverSWTCanvas c = new CoverSWTCanvas(parent, fig);
		covers.add(c);
		swtZOrder.add(c);
	}
	
	void setOrder(){
		Control prev = null;
		for(Control c : swtZOrder){
			if(prev == null){
				c.moveBelow(null);
			} else {
				c.moveAbove(prev);
			}
			prev = c;
		}
	}

	@Override
	public void notifyFigureChanged() {
		firstDrawSinceFigureChanged = true;
		if(!swtZOrder.isEmpty()){
			for(Overlap f : overlapFigures){
				if(f.over != Space.empty){
					addAboveSWTElement(f.over);
				}
			}
		}
	}
}
