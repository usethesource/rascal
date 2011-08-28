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

package org.rascalmpl.library.vis.util.vector;

import static org.rascalmpl.library.vis.util.vector.Dimension.X;
import static org.rascalmpl.library.vis.util.vector.Dimension.Y;

import java.util.Comparator;
import java.util.EnumMap;
public final class Coordinate extends TwoDimensionalDouble{
	
	 
	

	public Coordinate(double x, double y){
		super(x,y);
	}
	
	public Coordinate(Dimension majorDimension,double major, double minor){
		this();
		set(majorDimension,major);
		set(majorDimension.other(),minor);
	}
	
	
	public Coordinate(){
		super();
	}
	
	public Coordinate(Coordinate rhs){
		super(rhs);
	}
	

	public String toString(){
		return String.format("(%f,%f)", x,y);
	}
	
	public static class CoordinateDimensionComparator implements Comparator<Coordinate>{
		public static final EnumMap<Dimension, CoordinateDimensionComparator> INSTANCES = new EnumMap<Dimension, Coordinate.CoordinateDimensionComparator>(Dimension.class);
		static {
			INSTANCES.put(X, new CoordinateDimensionComparator(X));
			INSTANCES.put(Y, new CoordinateDimensionComparator(Y));
		}
		Dimension major;
		
		CoordinateDimensionComparator(Dimension major){
			this.major = major;
		}
		
		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			return (int)Math.signum(o1.get(major) - o2.get(major));
		}
		
	}
}
