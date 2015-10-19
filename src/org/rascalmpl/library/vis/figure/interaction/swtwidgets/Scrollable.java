/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import static org.rascalmpl.library.vis.util.vector.Dimension.X;
import static org.rascalmpl.library.vis.util.vector.Dimension.Y;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.value.IConstructor;

public class Scrollable extends SWTWidgetFigure<FigureSWTApplet> {

	public Figure innerFig;
	boolean hscroll, vscroll;
	
	public Scrollable(boolean hscroll,boolean vscroll,IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(env,  properties);
		this.hscroll = hscroll;
		this.vscroll = vscroll;
		widget = makeWidget(env.getSWTParent(), env,inner);
		env.getSWTParent().registerChild(widget);
		innerFig = widget.getFigure();
		widget.setVisible(false);
	}
	
	@Override
	public void computeMinSize(){
		super.computeMinSize();
		BoundingBox innerFigBB = widget.getFigure().minSize;
		if(!hscroll || !vscroll){
			Rectangle r = widget.computeTrim(0, 0, FigureMath.ceil(innerFigBB.getX()), FigureMath.ceil(innerFigBB.getY()));
			if(!hscroll){
				minSize.setMax(X, r.width);
			}
			if(!vscroll){
				minSize.setMax(Y,r.height);
			}
		}
		
	}
	
	FigureSWTApplet makeWidget(Composite comp, IFigureConstructionEnv env,IConstructor inner) {
		return new FigureSWTApplet(comp, inner,env.getFigureExecEnv(),hscroll,vscroll);
	}
	

}
