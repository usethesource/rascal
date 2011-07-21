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

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

public class Choice extends Figure {
	final private IValue callback;
	final org.eclipse.swt.widgets.List list;

	public Choice(IFigureApplet fpa, String[] choices, IValue fun, IEvaluatorContext ctx, PropertyManager properties) {
		super(fpa, properties);
		fpa.checkIfIsCallBack(fun, ctx);
		this.callback = fun;
		this.list = new org.eclipse.swt.widgets.List(fpa.getComp(), SWT.SINGLE|SWT.BORDER);
		for(String val : choices){
             list.add(val);
        }
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (list.getSelectionCount()!=1) return;
					doCallBack(list.getSelection()[0]);
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public void bbox() {
		Point p = list.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// width = list.getSize().x;
		// height = list.getSize().y;
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		super.bbox();
	}

	public void doCallBack(String s) {
		// System.err.println("Selected:"+s);
		fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
				.getInstance().stringType(), ValueFactoryFactory.getValueFactory().string(s));
		fpa.setComputedValueChanged();
		fpa.redraw();
	}

	
	@Override
	public void draw(double left, double top, GraphicsContext gc) {
		this.setLeft(left);
		this.setTop(top);
//		list.setSize(FigureApplet.round(getWidthProperty()),
//				FigureApplet.round(getHeightProperty()));
		list.setSize(FigureApplet.round(size.getWidth()),
				FigureApplet.round(size.getHeight()));
		list.setBackground(gc.getRgbColor(getFillColorProperty()));
		list.setLocation(FigureApplet.round(left), FigureApplet.round(top));
	}

	@Override
	public void destroy() {
		// fpa.setComputedValueChanged();
		list.dispose();
	}
	
	@Override
	public void layout() {
	}

}
