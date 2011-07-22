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
package org.rascalmpl.library.vis.interaction;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.IFigureExecutionEnvironment;
import org.rascalmpl.library.vis.containers.WithInnerFig;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class ComputeFigure extends WithInnerFig {
	
	final private IValue callback;
	
	
	private IList childProps;

	public ComputeFigure(IFigureExecutionEnvironment fpa, PropertyManager properties,  IValue fun, IList childProps) {
		super(fpa, null,properties);
	
		this.childProps = childProps;
		fpa.checkIfIsCallBack(fun);
		this.callback = fun;
	}
	
	public void computeFiguresAndProperties(){
		super.computeFiguresAndProperties();
		if(innerFig != null){
			innerFig.destroy();
		}
		IConstructor figureCons = (IConstructor) fpa.executeRascalCallBackWithoutArguments(callback).getValue();
		innerFig = FigureFactory.make(fpa, figureCons, properties, childProps, fpa.getRascalContext());
		innerFig.init();
		innerFig.computeFiguresAndProperties();
		properties = innerFig.properties;
		//fpa.setComputedValueChanged();
	}

	@Override
	public void bbox() {	
		innerFig.bbox();
		minSize.setWidth(innerFig.minSize.getWidth());
		minSize.setHeight(innerFig.minSize.getHeight());
		resizableX = innerFig.resizableX;
		resizableY = innerFig.resizableY;
	}

	public void layout(){
		innerFig.size.set(size);
		innerFig.globalLocation.set(globalLocation);
		innerFig.layout();
		
	}
	
	@Override
	public void draw(double left, double top, GraphicsContext gc) {
		//System.err.println("ComputeFigure.draw: " + left + ", " + top + "\n");
		this.setLeft(left);
		this.setTop(top);
		innerFig.draw(left,top, gc);
	}
	

}
