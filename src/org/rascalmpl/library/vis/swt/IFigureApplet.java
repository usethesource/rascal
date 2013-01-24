/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.library.vis.swt;

import java.io.OutputStream;

public interface IFigureApplet {
	
	public int getFigureWidth();
	public int getFigureHeight();
	public String getName();
	public void dispose();

	public void write(OutputStream out, int fileFormat /*SWT IMAGE_BMP, IMAGE_JPEG, IMAGE_ICO*/);
	

	
	
}
