package org.rascalmpl.library.vis.graphics;

import org.eclipse.swt.graphics.Color;

public interface GraphicsContext {
	
	public Color getRgbColor(int rgbColor);
	public Color getColor(int codeColor);
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
	public void strokeStyle(int style);
	public void textSize(double arg0);
	public void setBackground(Color color);
	public void setForeground(Color color);	
	public void setShadow(boolean shadow);
	public void setShadowColor(int color);
	public void setShadowLeft(double x);
	public void setShadowTop(double y);
	public void setFont(String fontName, double fontSize, FontStyle... styles);
	
	
	public double textWidth(String txt);
	public double textAscent();
	public double textDescent();

	public void pushMatrix();
	public void popMatrix();
	public void rotate(double arg0);
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



}
