/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
/**
 * 
 */
package org.rascalmpl.library.vis.properties;

public enum Property {
	ALIGN,			// Internally represented by HALIGN and VALIGN
	ALIGN_ANCHORS,	// boolean
	ANCHOR,         // Internally represented by HANCHOR and VANCHOR
	DOI,            // int, degree of interest
	FILL_COLOR,      // color
	FONT,           // str
	FONT_COLOR,      // color
	FONT_SIZE,       // int
	FROM_ANGLE,      // int
	GAP,            // Internally represented by HGAP and VGAP
	HANCHOR,        // float
	HALIGN,			// float
	HEIGHT, 		// float
	HGAP, 			// float
	HINT,			// str
	ID, 			// str
	INNERRADIUS, 	// float
	LINE_COLOR, 		// color
	LINE_WIDTH, 		// float
	ONCLICK,		// function/closure
	MOUSEOVER, 		// figure
	SHAPE_CLOSED, 	// boolean
	SHAPE_CONNECTED,	// boolean
	SHAPE_CURVED,	// boolean
	SIZE, 			// Internally represented by WIDTH and HEIGHT
	TEXT_ANGLE, 		// float
	TO_ANGLE,		// float
	VALIGN,			// float
	VANCHOR,		// float
	VGAP, 			// float
	WIDTH,			// float
	TEXT			// str, used to represent text arguments
}