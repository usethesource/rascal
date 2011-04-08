package org.rascalmpl.library.vis;

import processing.core.PApplet;
import processing.core.PConstants;
import org.rascalmpl.library.vis.processing.Constants;

public class FigureApplet {

	final public static int CORNERS = Constants.CORNERS;
	
	final public static int LEFT = Constants.LEFT;

	final public static int RIGHT  = Constants.RIGHT;
	
	final public static int CENTER  = Constants.CENTER;
	
	final public static float PI = PConstants.PI;

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

}
