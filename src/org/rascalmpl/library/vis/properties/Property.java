/**
 * 
 */
package org.rascalmpl.library.vis.properties;

public enum Property {
	ANCHOR,         // Internally represented by HANCHOR and VANCHOR
	DOI,            // int, degree of interest
	FILLCOLOR,      // color
	FONT,           // str
	FONTCOLOR,      // color
	FONTSIZE,       // int
	FROMANGLE,      // int
	GAP,            // Internally represented by HGAP and VGAP
	HANCHOR,        // float
	HEIGHT, 		// float
	HGAP, 			// float
	HINT,			// str
	ID, 			// str
	INNERRADIUS, 	// float
	LINECOLOR, 		// color
	LINEWIDTH, 		// float
	ONCLICK,		// function/closure
	MOUSEOVER, 		// figure
	SHAPECLOSED, 	// boolean
	SHAPECONNECTED,	// boolean
	SHAPECURVED,	// boolean
	SIZE, 			// Internally represented by WIDTH and HEIGHT
	TEXTANGLE, 		// float
	TOANGLE,		// float
	VANCHOR,		// float
	VGAP, 			// float
	WIDTH,			// float
	
	TEXT			// str, used to represent text arguments
}