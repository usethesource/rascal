/*******************************************************************************
 * Copyright (c) 2009-2011 CWI

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

public class Checkbox extends Figure {
	final private IValue callback;
	final org.eclipse.swt.widgets.Button button;

	public Checkbox(IFigureApplet fpa, String caption, boolean checked, IValue fun,
			IEvaluatorContext ctx, PropertyManager properties) {
		super(fpa, properties);
		fpa.checkIfIsCallBack(fun, ctx);
		this.callback = fun;
		this.button = new org.eclipse.swt.widgets.Button(fpa.getComp(),
				SWT.CHECK);
		button.setSelection(checked);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					doCallBack(button.getSelection());
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}
		});
		button.setText(caption);
	}

	@Override
	public void bbox() {
		Point p = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// width = list.getSize().x;
		// height = list.getSize().y;
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		setNonResizable();
		super.bbox();
	}

	public void doCallBack(boolean selected) {
		// System.err.println("Calling callback: " + callback +
		// " with selected = " + selected);
		fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
				.getInstance().boolType(), ValueFactoryFactory
				.getValueFactory().bool(selected));
		fpa.setComputedValueChanged();
		fpa.redraw();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		// button.setSize(FigureApplet.round(getWidthProperty()),
		// FigureApplet.round(getHeightProperty()));
		button.setSize(FigureApplet.round(minSize.getWidth()), FigureApplet.round(minSize.getHeight()));
		button.setBackground(fpa.getRgbColor(getFillColorProperty()));
		button.setLocation(FigureApplet.round(left), FigureApplet.round(top));
		this.print(button, left, top);
	}

	@Override
	public void destroy() {
		// fpa.setComputedValueChanged();
		button.dispose();
	}

	@Override
	public void layout() {
		size.set(minSize);	
	}
}
