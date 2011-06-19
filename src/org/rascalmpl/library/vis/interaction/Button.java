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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Button extends Figure {
	final private IValue callback;
	final org.eclipse.swt.widgets.Button button;
	boolean first;

	public Button(IFigureApplet fpa, String caption, IValue fun, IEvaluatorContext ctx, PropertyManager properties) {
		super(fpa, properties);
		first = true;
		fpa.checkIfIsCallBack(fun, ctx);
		this.callback = fun;
		this.button = new org.eclipse.swt.widgets.Button(fpa.getComp(),
				SWT.PUSH);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					doCallBack();
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}
		});
		
		button.setText(caption);
		//System.out.printf("created button\n");
		 
		
	}

	@Override
	public void bbox() {
		//System.out.printf("starting button button\n");
		Point p = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		setResizable();
		super.bbox();
	}

	public void doCallBack() {
		fpa.executeRascalCallBackWithoutArguments(callback);
		fpa.setComputedValueChanged();
		fpa.redraw();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		//button.setSize(FigureApplet.round(getWidthProperty()),
		//		FigureApplet.round(getHeightProperty()));
		button.setSize(FigureApplet.round(size.getWidth()),
				FigureApplet.round(size.getHeight()));
		button.setBackground(fpa.getRgbColor(getFillColorProperty()));
		button.setLocation(FigureApplet.round(left),
		         FigureApplet.round(top));
		
		print(button, left, top);
	}

	@Override
	public void destroy() {
		// fpa.setComputedValueChanged();
		System.out.printf("destroying button\n");
		button.dispose();
	}

	@Override
	public void layout() {
	}

}
