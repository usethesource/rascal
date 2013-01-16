/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util.vector;

public class ConnectionPoint {
	
	Coordinate location;
	double angle;
	
	ConnectionPoint(double x, double y , double angle){
		location = new Coordinate(x,y);
		this.angle = angle;
	}
	
	public Coordinate getLocation(){
		return location;
	}
	
	public double getAngle(){
		return angle;
	}

}
