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

public class Button extends SWTWidgetFigureWithSingleCallBack<org.eclipse.swt.widgets.Button> {

	public Button(IFigureConstructionEnv env, String caption, IValue fun,  PropertyManager properties) {
		super(env, fun, properties);
		widget = makeWidget(env.getSWTParent(), env,caption);
		widget.setVisible(false);
	}
	
	int buttonType(){
		return SWT.PUSH;
	}

	org.eclipse.swt.widgets.Button makeWidget(Composite comp, IFigureConstructionEnv env,String caption) {
		org.eclipse.swt.widgets.Button result = new org.eclipse.swt.widgets.Button(comp,buttonType());
		result.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doCallback();
			}
		});
		result.setText(caption);
		return result;
	}

	@Override
	void executeCallback() {
		cbenv.executeRascalCallBackWithoutArguments(callback);

	}


}
