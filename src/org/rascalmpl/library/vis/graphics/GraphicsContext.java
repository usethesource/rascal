package org.rascalmpl.library.vis.graphics;

import org.eclipse.swt.graphics.GC;

public interface GraphicsContext {
	
	public void line(double arg0, double arg1, double arg2, double arg3);
	public void rect(double arg0, double arg1, double arg2, double arg3);
	public void ellipse(double arg0, double arg1, double arg2, double arg3);
	public void text(String arg0, double arg1, double arg2);
	public void arc(double arg0, double arg1, double arg2, double arg3, double arg4,
			double arg5);
	
	public void fill(int arg0);
	public void stroke(int arg0);
	public void font(int arg0);
	public void strokeWeight(double arg0);
	public void strokeStyle(String style);
	public void textSize(double arg0);
	public void setShadow(boolean shadow);
	public void setShadowColor(int color);
	public void setShadowLeft(double x);
	public void setShadowTop(double y);
	public void setFont(String fontName, int fontSize, FontStyle... styles);

	public void pushMatrix();
	public void popMatrix();
	public void rotate(double arg0);
	public double getTranslateX();
	public double getTranslateY();
	public void translate(double arg0, double arg1);
	public void scale(double arg0, double arg1);
	
	public void bezierVertex(double arg0, double arg1, double arg2, double arg3,
			double arg4, double arg5);
	public void vertex(double arg0, double arg1);
	public void curveVertex(double arg0, double arg1);
	public void noFill();
	public void beginShape();
	public void beginShape(int arg0);
	public void endShape();
	public void endShape(int arg0 );

	public void dispose();
	

	public void setGC(GC gc);

}
