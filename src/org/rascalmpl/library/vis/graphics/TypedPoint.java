/*****************************************************************************
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
package org.rascalmpl.library.vis.graphics;

class TypedPoint {
	
	enum kind {
		CURVED, NORMAL, BEZIER;
	}
	final double x, y;
	final kind curved;

	TypedPoint(double x, double y, kind curved) {
		this.x = x;
		this.y = y;
		this.curved = curved;
	}
}
