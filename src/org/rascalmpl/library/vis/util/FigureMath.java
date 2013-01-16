/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util;

import java.util.Random;

import org.eclipse.swt.SWT;

public class FigureMath {
	
	final static int seed = 22;
	static Random random = new Random(seed);

	final public static int CORNERS = 0;
	
	final public static int CORNER = 1;
	
	final public static int CENTER  = SWT.CENTER;
	
	final public static int RADIUS  = 2;
	
	final public static int LEFT = SWT.LEFT;

	final public static int RIGHT  = SWT.RIGHT;
	
	final public static int TOP = SWT.TOP;
	
	final public static int BOTTOM  = SWT.BOTTOM;
	
	final public static int BASELINE  = 3;
	
	final public static double PI = Math.PI;
	
	final public static int OPEN = SWT.OPEN;
	
	final public static int CLOSE = SWT.CLOSE;

	public static double min(double x, double y) {
		return Math.min(x, y);
	}

	public static double max(double x, double y) {
		return Math.max(x, y);
	}

	public static double abs(double dlensq) {
		return Math.abs(dlensq);
	}

	public static int signum(double a){
		if(a==0){
			return 0;
		} else if (a < 0){
			return -1;
		} else {
			return 1;
		}
	}
	
	public static double dist(double x, double y, double x2, double y2) {
		return Math.hypot(x-x2, y-y2);
	}

	public static double mag(double x, double y) {
		return Math.hypot(x, y);
	}

	public static double constrain(double value, double minimum, double maximum) {
		if (value<minimum) return  minimum;
		if (value>maximum) return  maximum;
		return value;
	}

	public static double sqrt(double x) {
		return Math.sqrt(x);
	}

	public static double radians(double x) {
		return Math.toRadians(x);
	}
	
	public static double degrees(double x) {
		return Math.toDegrees(x);
	}

	public static double sin(double theta) {
		return Math.sin(theta);
	}

	public static double cos(double theta) {
		return Math.cos(theta);
	}

	public static double atan(double theta) {
		return Math.atan(theta);
	}

	public static int round(double f) {
		return (int)( f + 0.5);
	}
	
	public static boolean isHalf(double f){
		int ffloor = (int)f;
		return f - ffloor == 0.5;
	}
	
	public static boolean isWhole(double f){
		int ffloor = (int)f;
		return f - ffloor == 0.0;
	}
	
	public static int roundDown(double f){
		if(isHalf(f)){
			return (int)f;
		} else {
			return round(f);
		}
	}
	
	public static int roundUp(double f){
		return round(f);
	}

	public static double asin(double f) {
		return Math.asin(f);
	}
	
	public static double random(double x,  double y) {
		return x + random.nextDouble() * (y-x);
	}
	
	public static int floor(double f){
		return (int)f;
	}
	
	public static int ceil(double f){
		if(isWhole(f)) return (int)f;
		else return (int)f +1;
	}

	public static boolean isEven(int b){
		return b % 2 == 0;
	}
	

	public static class QuadraticRoots{
		public int nrRoots;
		public double firstRoot, secondRoot;
		QuadraticRoots(){
			nrRoots = 0;
		}
		QuadraticRoots(double root){
			nrRoots = 1;
			firstRoot = root;
		}
		QuadraticRoots(double firstRoot,double secondRoot){
			nrRoots = 2;
			this.firstRoot = firstRoot;
			this.secondRoot = secondRoot;
		}
	}
	
	static public QuadraticRoots getQuadraticRoots(double a, double b, double c){
		double discriminant = b*b - 4 * a * c;
		if(discriminant < 0){
			return new QuadraticRoots();
		} else if(discriminant == 0){
			return new QuadraticRoots(-b / (2*a));
		} else {
			return new QuadraticRoots(
					(-b + Math.sqrt(discriminant))/(2*a),
					(-b - Math.sqrt(discriminant))/(2*a));
		}	
	}
}
