package org.rascalmpl.library.vis;

import java.util.Random;

import processing.core.PApplet;
import org.rascalmpl.library.vis.processing.Constants;

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
	
	final public static float PI = Constants.PI;
	
	final public static int OPEN = Constants.OPEN;
	
	final public static int CLOSE = Constants.CLOSE;

	public static float min(float x, float y) {
		return PApplet.min(x, y);
	}

	public static float max(float x, float y) {
		return PApplet.max(x, y);
	}

	public static float abs(float dlensq) {
		return PApplet.abs(dlensq);
	}

	public static float dist(float x, float y, float x2, float y2) {
		return PApplet.dist(x, y, x2, y2);
	}

	public static float mag(float x, float y) {
		return PApplet.mag(x, y);
	}

	public static float constrain(float x, float y, float z) {
		return PApplet.constrain(x, y, z);
	}

	public static float sqrt(float x) {
		return PApplet.sqrt(x);
	}

	public static float radians(float x) {
		// TODO Auto-generated method stub
		return PApplet.radians(x);
	}
	
	public static float degrees(float x) {
		// TODO Auto-generated method stub
		return PApplet.degrees(x);
	}

	public static float sin(float theta) {
		// TODO Auto-generated method stub
		return PApplet.sin(theta);
	}

	public static float cos(float theta) {
		// TODO Auto-generated method stub
		return PApplet.cos(theta);
	}

	public static float atan(float theta) {
		// TODO Auto-generated method stub
		return PApplet.atan(theta);
	}

	public static int round(float f) {
		// TODO Auto-generated method stub
		return PApplet.round(f);
	}

	public static float asin(float f) {
		// TODO Auto-generated method stub
		return PApplet.asin(f);
	}
	
	public static float random(float x,  float y) {
		int k = random.nextInt((int) (y-x));
		return x + k;
	}

}
