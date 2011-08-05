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
package org.rascalmpl.library.vis.figure.graph.layered;

public enum Direction {
	TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
	
	static Direction[] dirs = {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT,BOTTOM_RIGHT};
	
	public static int ord(Direction dir){
		switch(dir){
		case TOP_LEFT: return 0;
		case TOP_RIGHT: return 1;
		case BOTTOM_LEFT: return 2;
		case BOTTOM_RIGHT: return 3;
		default:
			return -1;
		} 
	}
	
	public static boolean isTopDirection(Direction dir){
		return dir == TOP_LEFT || dir == TOP_RIGHT;
	}
		
	public static boolean isLeftDirection(Direction dir){
		return dir == TOP_LEFT || dir == BOTTOM_LEFT;	
	}
}
