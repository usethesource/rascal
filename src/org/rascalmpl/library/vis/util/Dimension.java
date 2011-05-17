/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
*******************************************************************************/

package org.rascalmpl.library.vis.util;

public enum Dimension { 
	X, Y; 
	
	public static Dimension flip(Dimension d){
		switch(d){
		case X: return Y;
		case Y: return X;
		default: return X;
		}
	}
	
	
	public static Dimension flip(boolean f,Dimension d){
		if(f){
			return flip(d);
		} else {
			return d;
		}
	}
}

