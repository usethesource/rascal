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
package org.rascalmpl.library.vis.properties;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;

public class LikeRealProperty implements IRealPropertyValue {
	final private Property property;
	final private Figure fig;

	public LikeRealProperty(Property prop, String id, IFigureApplet fpa, IEvaluatorContext ctx){
		this.property = prop;
		this.fig = fpa.getRegisteredId(id);
		if(this.fig == null)
			throw RuntimeExceptionFactory.figureException("Cannot be the same as not (yet) existing figure", ctx.getValueFactory().string(id), ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	public Property getProperty(){
		return property;
	}
	
	public float getValue() {
		return fig.properties.getRealProperty(property);
	}

	public boolean isCallBack() {
		return false;
	}

}
