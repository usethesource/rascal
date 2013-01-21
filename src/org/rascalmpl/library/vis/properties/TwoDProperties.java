/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.library.vis.properties;

import static org.rascalmpl.library.vis.properties.Properties.HALIGN;
import static org.rascalmpl.library.vis.properties.Properties.HCONNECT;
import static org.rascalmpl.library.vis.properties.Properties.HEND_GAP;
import static org.rascalmpl.library.vis.properties.Properties.HGAP;
import static org.rascalmpl.library.vis.properties.Properties.HGROW;
import static org.rascalmpl.library.vis.properties.Properties.HMIRROR;
import static org.rascalmpl.library.vis.properties.Properties.HPOS;
import static org.rascalmpl.library.vis.properties.Properties.HRESIZABLE;
import static org.rascalmpl.library.vis.properties.Properties.HSHADOWPOS;
import static org.rascalmpl.library.vis.properties.Properties.HSHRINK;
import static org.rascalmpl.library.vis.properties.Properties.HSIZE;
import static org.rascalmpl.library.vis.properties.Properties.HSTART_GAP;
import static org.rascalmpl.library.vis.properties.Properties.HZOOMABLE;
import static org.rascalmpl.library.vis.properties.Properties.VALIGN;
import static org.rascalmpl.library.vis.properties.Properties.VCONNECT;
import static org.rascalmpl.library.vis.properties.Properties.VEND_GAP;
import static org.rascalmpl.library.vis.properties.Properties.VGAP;
import static org.rascalmpl.library.vis.properties.Properties.VGROW;
import static org.rascalmpl.library.vis.properties.Properties.VMIRROR;
import static org.rascalmpl.library.vis.properties.Properties.VPOS;
import static org.rascalmpl.library.vis.properties.Properties.VRESIZABLE;
import static org.rascalmpl.library.vis.properties.Properties.VSHADOWPOS;
import static org.rascalmpl.library.vis.properties.Properties.VSHRINK;
import static org.rascalmpl.library.vis.properties.Properties.VSIZE;
import static org.rascalmpl.library.vis.properties.Properties.VSTART_GAP;
import static org.rascalmpl.library.vis.properties.Properties.VZOOMABLE;


public enum TwoDProperties {
	
	RESIZABLE	("resizable",	HRESIZABLE,	VRESIZABLE	),
	ZOOMABLE	("zoomable",	HZOOMABLE,	VZOOMABLE	),
	START_GAP	("startGap",	HSTART_GAP,	VSTART_GAP	),
	END_GAP		("endGap",		HEND_GAP,	VEND_GAP	),	
	MIRROR		("mirror",		HMIRROR,	VMIRROR		),
	
	POS			("pos",			HPOS,		VPOS		),
	SIZE		("size",		HSIZE,		VSIZE		),
	GAP			("gap",			HGAP,		VGAP		),
	SHADOWPOS	("shadowPos",	HSHADOWPOS,	VSHADOWPOS	),
	SHRINK		("shrink",		HSHRINK,	VSHRINK		),
	ALIGN		("align",		HALIGN,		VALIGN		),
	GROW		("grow",		HGROW,		VGROW		),
	CONNECT		("connect",		HCONNECT,	VCONNECT	);
	
	String commonName;
	Properties hor;
	Properties ver;
	
	TwoDProperties(String commonName,Properties hor, Properties ver){
		this.commonName = commonName;
		this.hor = hor;
		this.ver = ver;
	}

}
