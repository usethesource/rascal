/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Choice extends SWTWidgetFigureWithSingleCallBack<org.eclipse.swt.widgets.List> {
	

	public Choice(IFigureConstructionEnv env, String[] choices, IValue fun, PropertyManager properties) {
		super(env, fun, properties);
		widget = makeWidget(env.getSWTParent(), env,choices);
		widget.setVisible(false);
	}
	

	org.eclipse.swt.widgets.List makeWidget(Composite comp, IFigureConstructionEnv env,String[] choices) {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(comp, SWT.SINGLE|SWT.BORDER);
		for(String val : choices){
             list.add(val);
        }
		list.select(0);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (widget.getSelectionCount()!=1) return;
				doCallback();
			}
		});
		return list;
	}


	public void executeCallback() {
		cbenv.executeRascalCallBackSingleArgument(callback, TypeFactory
				.getInstance().stringType(), ValueFactoryFactory.getValueFactory().string(widget.getItem(widget.getSelectionIndex())));
	}

	


}
