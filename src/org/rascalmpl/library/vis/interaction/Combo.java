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

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

public class Combo extends Figure {
	// Function of type Figure (list[str]) to compute new figure
	private final IValue callback; // Function of type void() to inform backend
									// about entered text
	private final IValue validate; // Function of type bool(str) to validate
									// input sofar

	private boolean validated = true;

	private final Color trueColor;
	private final Color falseColor;

	final org.eclipse.swt.widgets.Combo combo;

	private int tLimit;

	public Combo(IFigureApplet fpa, String text, String[] choices, IValue cb, IValue validate, IEvaluatorContext ctx, PropertyManager properties) {
		super(fpa, properties);
		// trueColor = fpa.getColor(SWT.COLOR_GREEN);
		trueColor = fpa.getRgbColor(getFontColorProperty());
		falseColor = fpa.getColor(SWT.COLOR_RED);
		combo = new org.eclipse.swt.widgets.Combo(fpa.getComp(), SWT.DROP_DOWN
				| SWT.BORDER);
		fpa.checkIfIsCallBack(cb, ctx);
		this.callback = cb;
		if (validate != null) {
			fpa.checkIfIsCallBack(validate, ctx);
		}
		this.validate = validate;
		// System.err.println("callback = " + callback);

		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				doValidate();
			}
		});
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				try {
					doCallBack(true);
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}

			public void widgetSelected(SelectionEvent e) {
				try {
					doCallBack(false);
				} catch (Exception ex) {
					System.err.println("EXCEPTION");
					ex.printStackTrace();
				}
			}
		});
		combo.setText(text);
		double m = getWidthProperty();
		tLimit = FigureApplet.round(m / fpa.textWidth("m"));
		for (String s : choices) {
			combo.add(s);
			double d = fpa.textWidth(s);
			if (d > m)
				m = d;
			if (s.length()>tLimit) tLimit = s.length();
		}
		minSize.setWidth(m + 40);

	}

	@Override
	public void bbox() {
		// Point p = combo.computeSize(FigureApplet.round(getWidthProperty()),
		// SWT.DEFAULT, true);
		Point p = combo.computeSize(FigureApplet.round(minSize.getWidth()), SWT.DEFAULT,
				true);
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		combo.setTextLimit(tLimit);
		
		super.bbox();
	}

	public boolean doValidate() {
		if (validate != null) {
			Result<IValue> res = fpa.executeRascalCallBackSingleArgument(
					validate, TypeFactory.getInstance().stringType(),
					ValueFactoryFactory.getValueFactory().string(combo.getText()));
			System.err.println("doValidate:"+combo.getText()+" "+res);
			validated = res.getValue().isEqual(ValueFactoryFactory.getValueFactory().bool(true));
			System.err.println("validate:"+combo.getText()+" "+validated);
			return validated;
		}
		return true;
	}

	public void doCallBack(boolean isTextfield) {
		if (!validated) {
			combo.setForeground(falseColor);
			combo.redraw();
			return;
		}
		combo.setForeground(trueColor);
		if (isTextfield)
			fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
					.getInstance().stringType(),  ValueFactoryFactory.getValueFactory().string(combo.getText()));
		else {
			int s = combo.getSelectionIndex();
			if (s < 0)
				return;
			fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
					.getInstance().stringType(), ValueFactoryFactory.getValueFactory().string(combo.getItem(s)));
		}
		fpa.setComputedValueChanged();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		combo.setForeground(validated ? trueColor : falseColor);
		// combo.setSize(FigureApplet.round(width), FigureApplet.round(height));
		combo.setBackground(fpa.getRgbColor(getFillColorProperty()));
		combo.setLocation(FigureApplet.round(left), FigureApplet.round(top));
		combo.setSize(FigureApplet.round(size.getWidth()), FigureApplet.round(size.getHeight()));
		print(combo, left, top);
	}

	@Override
	public void destroy() {
		combo.dispose();
	}
	
	@Override
	public void layout() {
	}
}
