/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.properties;


enum PropertySemantics{
	INTERNAL, // this property is exclusively used by the figure itself
	EXTERNAL, // this property is exclusively used by the surrounding figure 
	BOTH; // this property is used both by the figure itself and by the surrounding figure
}