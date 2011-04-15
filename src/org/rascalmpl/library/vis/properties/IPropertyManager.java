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


import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;

public interface IPropertyManager {
	
	public boolean isBooleanPropertySet(BoolProp property);
	public boolean getBooleanProperty(BoolProp property) ;
	
	public boolean isIntegerPropertySet(IntProp property);
	public int getIntegerProperty(IntProp property) ;
	
	public boolean isRealPropertySet(RealProp property);
	public float getRealProperty(RealProp property);
	
	public boolean isStringPropertySet(StrProp property);
	public String getStringProperty(StrProp property);
	
	public boolean isColorPropertySet(ColorProp property);
	public int getColorProperty(ColorProp property) ;
	
	// public IFigureApplet getFPA();
	public boolean isDraggable();
	public Figure getMouseOver();
}
