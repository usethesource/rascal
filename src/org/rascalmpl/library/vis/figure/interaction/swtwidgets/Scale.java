/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction.swtwidgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Scale extends SWTWidgetFigureWithSingleCallBack<org.eclipse.swt.widgets.Scale>{

	int selection;
	PropertyValue<Integer> low, high,selected;
	public Scale(IFigureConstructionEnv env, Dimension major, PropertyValue<Integer> low, PropertyValue<Integer> high,  PropertyValue<Integer> selected,IValue callback,
			PropertyManager properties) {
		super(env, callback, properties);
		this.selected = selected;
		this.low = low;
		this.high = high;
		widget = makeWidget(env.getSWTParent(), major, env);
		widget.setVisible(false);

	}
	

	org.eclipse.swt.widgets.Scale makeWidget(Composite comp, Dimension major, IFigureConstructionEnv env) {
		int swtConstant = 0;
		switch(major){
		case X: swtConstant = SWT.HORIZONTAL; break;
		case Y: swtConstant = SWT.VERTICAL; break;
		}
		org.eclipse.swt.widgets.Scale result = new org.eclipse.swt.widgets.Scale(comp,swtConstant);
		result.setSelection(selected.getValue());
		result.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doCallback();
			}
		});
		return result;
	}
	

	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		widget.setMinimum(low.getValue());
		//widget.setSelection(selected.getValue());
		widget.setMaximum(high.getValue());
		super.initElem(env, mparent, swtSeen, visible, resolver);
	}
	

	@Override
	void executeCallback() {
		cbenv.executeRascalCallBackSingleArgument(callback, TypeFactory
				.getInstance().integerType(), ValueFactoryFactory.getValueFactory().integer(widget.getSelection()));
	}


}
