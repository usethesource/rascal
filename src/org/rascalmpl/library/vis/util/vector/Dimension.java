/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util.vector;

public enum Dimension {
	
	X, Y;

	public static final Dimension[] HOR_VER = {X,Y};
	
	public Dimension other(){
		if(this == X){
			return Y;
		} else {
			return X;
		}
	}

}
