/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class TextField extends Figure {
	// Function of type Figure (list[str]) to compute new figure
	private final IValue callback; // Function of type void() to inform backend
									// about entered text
	private final IValue validate; // Function of type bool(str) to validate
									// input sofar

	private boolean validated = true;

	private final Color trueColor;
	private final Color falseColor;

	final Text textfield;

	public TextField(IFigureApplet fpa, PropertyManager properties,
			final IString text, IValue cb, IValue validate,
			IEvaluatorContext ctx) {
		super(fpa, properties);
		trueColor = fpa.getColor(SWT.COLOR_GREEN);
		falseColor = fpa.getColor(SWT.COLOR_RED);
		textfield = new Text(fpa.getComp(), SWT.SINGLE | SWT.BORDER);
		fpa.checkIfIsCallBack(cb, ctx);
		this.callback = cb;
		if (validate != null) {
			fpa.checkIfIsCallBack(validate, ctx);
		}
		this.validate = validate;
		System.err.println("callback = " + callback);

		textfield.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				doValidate();
			}
		});
		textfield.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				try {
					doCallBack();
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}
		});
		textfield.setText(text.getValue());
	}

	@Override
	public void bbox(double desiredWidth, double desiredHeight) {
		Point p = textfield.computeSize(FigureApplet.round(getWidthProperty()),
				SWT.DEFAULT, true);
		width = p.x;
		height = p.y;
		textfield.setTextLimit(2 * (int) width / textfield.getLineHeight());
	}

	public boolean doValidate() {
		if (validate != null) {
			Result<IValue> res = fpa.executeRascalCallBackSingleArgument(
					validate, TypeFactory.getInstance().stringType(),
					vf.string(textfield.getText()));
			validated = res.getValue().equals(vf.bool(true));
			textfield.setForeground(validated ? trueColor : falseColor);
			textfield.redraw();
			return validated;
		}
		return true;
	}

	public void doCallBack() {
		fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
				.getInstance().stringType(), vf.string(textfield.getText()));
		fpa.setComputedValueChanged();
		fpa.redraw();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		textfield.setForeground(validated ? fpa
				.getRgbColor(getFontColorProperty()) : falseColor);
		textfield
				.setSize(FigureApplet.round(width), FigureApplet.round(height));
		textfield.setBackground(fpa.getRgbColor(getFillColorProperty()));
		textfield
				.setLocation(FigureApplet.round(left), FigureApplet.round(top));
	}

	@Override
	public void destroy() {
		textfield.dispose();
	}
}
