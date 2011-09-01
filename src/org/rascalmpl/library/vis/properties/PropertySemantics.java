package org.rascalmpl.library.vis.properties;


enum PropertySemantics{
	INTERNAL, // this property is exclusively used by the figure itself
	EXTERNAL, // this property is exclusively used by the surrounding figure 
	BOTH; // this property is used both by the figure itself and by the surrounding figure
}