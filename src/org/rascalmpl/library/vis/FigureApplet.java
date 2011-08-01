package org.rascalmpl.library.vis;

import java.util.Random;

import org.rascalmpl.library.vis.swt.Constants;

public class FigureApplet {
	
	final static int seed = 22;
	static Random random = new Random(seed);

	final public static int CORNERS = Constants.CORNERS;
	
	final public static int CORNER = Constants.CORNER;
	
	final public static int CENTER  = Constants.CENTER;
	
	final public static int RADIUS  = Constants.RADIUS;
	
	final public static int LEFT = Constants.LEFT;

	final public static int RIGHT  = Constants.RIGHT;
	
	final public static int TOP = Constants.TOP;
	
	final public static int BOTTOM  = Constants.BOTTOM;
	
	final public static int BASELINE  = Constants.BASELINE;
	
	final public static double PI = Math.PI;
	
	final public static int OPEN = Constants.OPEN;
	
	final public static int CLOSE = Constants.CLOSE;

	public static double min(double x, double y) {
		return Math.min(x, y);
	}

	public static double max(double x, double y) {
		return Math.max(x, y);
	}

	public static double abs(double dlensq) {
		return Math.abs(dlensq);
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
		// TODO Auto-generated method stub
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
		int k = random.nextInt((int) (y-x));
		return x + k;
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
	
}
