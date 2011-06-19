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
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;
// TODO: something is weird here, when resizing!!
public class TextField extends Figure {
	// Function of type Figure (list[str]) to compute new figure
	private final IValue callback; // Function of type void() to inform backend
									// about entered text
	private final IValue validate; // Function of type bool(str) to validate
									// input sofar

	private boolean validated = true;

	private final Color trueColor;
	private final Color falseColor;
	
	private int tLimit;
	
	final private IValueFactory vf = ValueFactoryFactory.getValueFactory();

	final Text textfield;

	public TextField(IFigureApplet fpa, String text, IValue cb, IValue validate, IEvaluatorContext ctx, PropertyManager properties) {
		super(fpa, properties);
		trueColor = fpa.getRgbColor(getFontColorProperty());
		falseColor = fpa.getColor(SWT.COLOR_RED);
		textfield = new Text(fpa.getComp(), SWT.SINGLE | SWT.BORDER);
		fpa.checkIfIsCallBack(cb, ctx);
		this.callback = cb;
		if (validate != null) {
			fpa.checkIfIsCallBack(validate, ctx);
		}
		this.validate = validate;

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
		textfield.setText(text);
		minSize.setWidth(getWidthProperty());
		tLimit = FigureApplet.round(minSize.getWidth() / fpa.textWidth("b"));
		if (text.length()>tLimit) {
			  tLimit = text.length();	
			  minSize.setWidth(fpa.textWidth(text));
		}
	}

	@Override
	public void bbox() {
		Point p = textfield.computeSize(FigureApplet.round(minSize.getWidth()), SWT.DEFAULT, true);
		minSize.setWidth(p.x);
		minSize.setHeight(p.y);
		textfield.setTextLimit(tLimit);
		setNonResizable();
		super.bbox();
	}

	public boolean doValidate() {
		if (validate != null) {
			Result<IValue> res = fpa.executeRascalCallBackSingleArgument(
					validate, TypeFactory.getInstance().stringType(),
					vf.string(textfield.getText()));
			validated = res.getValue().isEqual(vf.bool(true));
			textfield.setForeground(validated ? trueColor : falseColor);
			textfield.redraw();
			return validated;
		}
		return true;
	}

	public void doCallBack() {
		if (validated) {
			fpa.executeRascalCallBackSingleArgument(callback, TypeFactory
					.getInstance().stringType(), vf.string(textfield.getText()));
			fpa.setComputedValueChanged();
		}
		textfield.redraw();
	}

	@Override
	public void draw(double left, double top) {
		this.setLeft(left);
		this.setTop(top);
		textfield.setForeground(validated ? trueColor : falseColor);
		textfield
				.setSize(FigureApplet.round(minSize.getWidth()), FigureApplet.round(minSize.getHeight()));
		textfield.setBackground(fpa.getRgbColor(getFillColorProperty()));
		textfield
				.setLocation(FigureApplet.round(left), FigureApplet.round(top));
		print(textfield, left, top);
	}

	@Override
	public void destroy() {
		textfield.dispose();
	}
	
	@Override
	public void layout() {
		size.set(minSize);	
	}
}
