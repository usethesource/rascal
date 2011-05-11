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

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Button extends Figure {
	final private IValue callback;
	final org.eclipse.swt.widgets.Button button;

	public Button(IFigureApplet fpa, PropertyManager properties, IString tname,
			IValue fun, IEvaluatorContext ctx) {
		super(fpa, properties);
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
		button.setText(tname.getValue());
		// button.setBackground(new Color(255));
		// fpa.setBackground(new Color(0XFFFFFFFF));
		// fpa.add(button);
	}

	@Override
	public void bbox(double desiredWidth, double desiredHeight) {
		width = button.getSize().x;
		height = button.getSize().y;
	}

	public void doCallBack() {
		fpa.executeRascalCallBackWithoutArguments(callback);
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		button.setSize(FigureApplet.round(getWidthProperty()),
				FigureApplet.round(getHeightProperty()));
		button.setBackground(fpa.getRgbColor(getFillColorProperty()));
		button.setLocation(FigureApplet.round(left),
		         FigureApplet.round(top));
	}

	@Override
	public void destroy() {
		// fpa.setComputedValueChanged();
		button.dispose();
	}

}
