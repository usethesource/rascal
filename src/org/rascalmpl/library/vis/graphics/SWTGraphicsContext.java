package org.rascalmpl.library.vis.graphics;

import static org.rascalmpl.library.vis.util.FigureMath.CLOSE;
import static org.rascalmpl.library.vis.util.FigureMath.OPEN;
import static org.rascalmpl.library.vis.util.FigureMath.degrees;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.graphics.Transform;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class SWTGraphicsContext implements GraphicsContext {
	

	
	private volatile GC gc;
	private int shadowColor;
	private double shadowLeft;
	private double shadowTop;
	private Stack<Transform> stackMatrix = new Stack<Transform>();
	private Stack<Route> stackPath = new Stack<Route>();
	private int alphaStroke = FigureColorUtils.OPAQUE, alphaFill = FigureColorUtils.OPAQUE, alphaFont = FigureColorUtils.OPAQUE;
	private boolean shadow;
	private boolean fill = true, stroke = true;
	private boolean debug;
	private Font currentFont;
	private FontData currentFontData;
	private Device device;
	private Color foregroundColor;
	private Color backgroundColor;
	private Color fontColor;
	double translateX, translateY;
	
	
	FontData fontData;
	int foreGroundCI, backgroundCI, fontCI;
	

	
	public SWTGraphicsContext() {
	}
	
	public void setGC(GC gc){
		this.gc = gc;
		this.device = gc.getDevice();
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		disposeIfNessary(currentFont);
		currentFontData = null;
		currentFont = null;
		translateX = translateY = 0;
		stackMatrix.clear();
		gc.setTransform(null);
	}
	
	public GC getGC(){
		return gc;
	}
	
	

	@SuppressWarnings("serial")
	class Route extends ArrayList<TypedPoint> {
		void add(double x, double y, TypedPoint.kind curved) {
			super.add(new TypedPoint(x, y, curved));
		}
	}

	
	enum SHAPE {
		ELLIPSE, RECTANGLE
	}


	public void line(double arg0, double arg1, double arg2, double arg3) {
		gc.drawLine((int) arg0, (int) arg1, (int) arg2, (int) arg3);
	}

	public void rect(double x, double y, double width, double height) {
		int alpha0 = gc.getAlpha();
		int xi, yi, wi,hi;
		xi = (int)(x);
		yi = (int)(y);
		wi = (int)(x + width) - (int)x;
		hi = (int)(y + height) - (int)y;

		if (fill) {
			gc.setAlpha(alphaFill);
			if (shadow) {
				drawShadowFigure(SHAPE.RECTANGLE, xi, yi, wi, hi);
			}
			gc.fillRectangle( xi, yi, wi, hi);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			
			gc.drawRectangle(xi, yi, wi, hi);
			gc.setAlpha(alpha0);
		}

	}

	public void ellipse(double x, double y, double width, double height) {
		int xi, yi, wi,hi;
		xi = (int)(x);
		yi = (int)(y);
		wi = (int)(x + width) - (int)x;
		hi = (int)(y + height) - (int)y;
		int alpha0 = gc.getAlpha();
		if (fill) {
			gc.setAlpha(alphaFill);
			if (shadow) {
				drawShadowFigure(SHAPE.ELLIPSE, xi, yi, wi, hi);
			}
			gc.fillOval(xi, yi, wi, hi);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawOval(xi, yi, wi, hi);
			gc.setAlpha(alpha0);
		}
	}
	
	private void disposeIfNessary(Resource r){
		if(!(r == null || r.isDisposed())){
			r.dispose();
		}
	}

	public void fill(int arg0) {
		fill = true;
		alphaFill = FigureColorUtils.getAlpha(arg0);
		int backgroundCI = FigureColorUtils.withoutAlpha(arg0);
		if(this.backgroundCI == backgroundCI  && backgroundColor != null) return;
		this.backgroundCI = backgroundCI;
		disposeIfNessary(backgroundColor);
		backgroundColor = SWTFontsAndColors.getRgbColor(arg0);
		gc.setBackground(backgroundColor);
		
	}

	public void stroke(int arg0) {
		stroke = true;
		alphaStroke = FigureColorUtils.getAlpha(arg0);
		int foreGroundCI = FigureColorUtils.withoutAlpha(arg0);
		if(this.foreGroundCI == foreGroundCI && foregroundColor != null) return;
		this.foreGroundCI = foreGroundCI;
		disposeIfNessary(foregroundColor);
		foregroundColor = SWTFontsAndColors.getRgbColor(arg0);
		gc.setForeground(foregroundColor);
		
	}
	
	public void font(int color){
		alphaFont = FigureColorUtils.getAlpha(color);
		int fontCI = FigureColorUtils.withoutAlpha(color);
		if(this.fontCI == fontCI && fontColor != null) return;
		this.fontCI = fontCI;
		disposeIfNessary(fontColor);
		fontColor = SWTFontsAndColors.getRgbColor(color);
	}

	public void strokeWeight(double arg0) {
		int d = (int) arg0;
		stroke = (d != 0);
		gc.setLineWidth(d);
	}

	 int getSWTLineStyle(String s) {
         if (s.equals("dash")) return SWT.LINE_DASH;
         if (s.equals("dot")) return SWT.LINE_DOT;
         if (s.equals("dashdot")) return SWT.LINE_DASHDOT;
         if (s.equals("dashdotdot")) return SWT.LINE_DASHDOTDOT;
         return SWT.LINE_SOLID;
     }
	
	public void strokeStyle(String style) {
		gc.setLineStyle(getSWTLineStyle(style));
	}

	public void textSize(double arg0) {
		if (gc.getFont().getFontData().length < 1)
			return;
		gc.getFont().getFontData()[0].setHeight((int) arg0);

	}
	
	public void text(String arg0, double x, double y) {
		gc.setAlpha(alphaFont);
		gc.setForeground(fontColor);
		gc.drawText(arg0, (int) x, (int) y, true);
		gc.setForeground(foregroundColor);
		gc.setAlpha(alphaStroke);
	}

	public void pushMatrix() {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		stackMatrix.push(transform);
	}

	public void popMatrix() {
		Transform transform = stackMatrix.pop();
		gc.setTransform(transform);
	}

	public void rotate(double angle) {
		Transform transform = new Transform(gc.getDevice());
		Transform transform2 = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform2.rotate((float)angle);
		transform.multiply(transform2);
		gc.setTransform(transform);
	}

	public void translate(double x, double y) {
		translateX+=x;
		translateY+=y;
		Transform transform = new Transform(gc.getDevice());
		Transform transform2 = new Transform(gc.getDevice());
		gc.getTransform(transform2);
		transform2.translate((float) x, (float) y);
		transform.multiply(transform2);
		gc.setTransform(transform);
	}

	public void scale(double scaleX, double scaleY) {
		Transform transform = new Transform(gc.getDevice());
		Transform transform2 = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform2.scale((float) scaleX, (float) scaleY);
		transform.multiply(transform2);
		gc.setTransform(transform);
	}

	public void bezierVertex(double cx1, double cy1, double cx2, double cy2,
			double x, double y) {
		Route r = stackPath.peek();
		r.add(cx1, cy1, TypedPoint.kind.BEZIER);
		r.add(cx2, cy2, TypedPoint.kind.BEZIER);
		r.add(x, y, TypedPoint.kind.BEZIER);
	}

	public void vertex(double x, double y) {
		Route r = stackPath.peek();
		r.add(x, y, TypedPoint.kind.NORMAL);
	}

	public void curveVertex(double x, double y) {
		Route r = stackPath.peek();
		r.add(x, y, TypedPoint.kind.CURVED);
	}

	public void noFill() {
		fill = false;
	}

	public void arc(double x, double y, double width, double height,
			double startAngle, double stopAngle) {
		gc.drawArc((int) x, (int) y, (int) width, (int) height,
				(int) degrees(startAngle),
				(int) degrees(stopAngle));

	}

	public void beginShape() {
		stackPath.clear();
		Route p = new Route();
		stackPath.push(p);
	}

	public void beginShape(int arg0) {
		beginShape();

	}

	private void drawNotCurved(Route r, Path p) {
		// System.err.println("drawNotCurved:" + r.size());
		while (!r.isEmpty()) {
			TypedPoint z = r.get(0);
			// System.err.println("Curved:" + z.curved);
			if (z.curved == TypedPoint.kind.NORMAL) {
				p.lineTo((float) z.x, (float) z.y);
				r.remove(0);
			} else if (z.curved == TypedPoint.kind.BEZIER) {
				double c1x = z.x, c1y = z.y;
				r.remove(0);
				z = r.remove(0);
				double c2x = z.x, c2y = z.y;
				z = r.remove(0);
				double x = z.x, y = z.y;
				p.cubicTo((float) c1x, (float) c1y, (float) c2x, (float) c2y,
						(float) x, (float) y);
			} else {
				break;
			}
		}
	}

	private void drawCurved(Route r, Path p, boolean closed) {
		// System.err.println("drawCurved:" + r.size());
		if (r.size() < 3)
			return;
		Interpolation.solve(r, closed);
		int n = Interpolation.P0.length;
		for (int i = 0; i < n; i++)
			p.cubicTo((float) Interpolation.P1[i].x,
					(float) Interpolation.P1[i].y,
					(float) Interpolation.P2[i].x,
					(float) Interpolation.P2[i].y,
					(float) Interpolation.P3[i].x,
					(float) Interpolation.P3[i].y);
	}

	public void endShape() {
		endShape(OPEN);
	}

	public void endShape(int arg0) {
		Route r = stackPath.pop();
		Path p = new Path(gc.getDevice());
		if (debug)
			System.err.println("endShape1:" + r.size());
		TypedPoint q = r.get(0);
		if (q.curved != TypedPoint.kind.CURVED)
			r.remove(0);
		p.moveTo((float) q.x, (float) q.y);
		if (debug)
			System.err.println("q=(" + q.x + "," + q.y + " " + q.curved + ")");
		if (arg0 == CLOSE) {
			r.add(new TypedPoint(q.x, q.y, TypedPoint.kind.NORMAL));
		}
		while (!r.isEmpty()) {
			drawNotCurved(r, p);
			drawCurved(r, p, arg0 == CLOSE);
		}
		int alpha0 = gc.getAlpha();
		if (fill /* arg0 == CLOSE */) {
			gc.setAlpha(alphaFill);
			gc.fillPath(p);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawPath(p);
			gc.setAlpha(alpha0);
		}
		p.dispose();
	}


	public void setShadow(boolean shadow) {
		this.shadow = shadow;

	}

	public void setShadowColor(int color) {
		this.shadowColor = color;
	}

	public void setShadowLeft(double x) {
		this.shadowLeft = x;

	}

	public void setShadowTop(double y) {
		this.shadowTop = y;
	}

	/*
	enum PRIMITIVE {
		rect, ellipse, line, fill, stroke, strokeWeight, text, textColor, textFont, textSize;
		void draw(FigureSWTApplet p, IConstructor c) {
			double[] d = new double[4];
			int[] n = new int[4];
			String[] s = new String[4];
			int z = c.arity();
			for (int i = 0; i < z; i++) {
				if (c.get(i) instanceof INumber)
					d[i] = ((INumber) c.get(i)).toReal().doubleValue();
				if (c.get(i) instanceof IInteger)
					n[i] = ((IInteger) c.get(i)).intValue();
				if (c.get(i) instanceof IString)
					s[i] = ((IString) c.get(i)).getValue();
			}
			switch (this) {
			case line:
				p.line(d[0], d[1], d[2], d[3]);
				return;
			case rect:
				p.rect(d[0], d[1], d[2], d[3]);
				return;
			case ellipse:
				p.ellipse(d[0], d[1], d[2], d[3]);
				return;
			case stroke:
				p.stroke(n[0]);
				return;
			case strokeWeight:
				p.strokeWeight(d[0]);
				return;
			case fill:
				p.fill(n[0]);
				return;
			case text:
				p.text(s[0], d[1], d[2]);
				return;
			case textFont:
				p.textFont(p.createFont(s[0], d[1]));
				return;
			case textSize:
				p.textSize(d[0]);
				return;
			case textColor:
				p.textColor(n[0]);
				return;
			}
		}
	}
	*/

	private void drawShadowFigure(SHAPE shape, int x, int y, int width,
			int height) {
		translate(shadowLeft, shadowTop);
		int arg0 = shadowColor;
		int alpha0 = gc.getAlpha();
		Color color0 = gc.getBackground();
		int alpha = FigureColorUtils.getAlpha(arg0);
		gc.setAlpha(alpha);
		Color color = new Color(device, FigureColorUtils.getRed(arg0),
				FigureColorUtils.getGreen(arg0), FigureColorUtils.getBlue(arg0));
		gc.setBackground(color);
		switch (shape) {
		case RECTANGLE:
			gc.fillRectangle(x, y, width, height);
			break;
		case ELLIPSE:
			gc.fillOval(x, y, width, height);
			break;
		}
		translate(-shadowLeft, -shadowTop);
		gc.setAlpha(alpha0);
		gc.setBackground(color0);
	}

	
	
	public void setFont(String fontName, int fontSize, FontStyle... styles) {
		int styleMask = FontStyle.toStyleMask(styles);
		if(currentFontData != null && currentFontData.name.equals(fontName) && currentFontData.height == (int)fontSize && currentFontData.style == styleMask){
			return;
		}
		FontData fd = new FontData(fontName, (int) fontSize, styleMask );
		currentFontData = fd;
		//if(fd.equals(this.fontData)) return;
		this.fontData = fd;
		disposeIfNessary(currentFont);
		currentFont = new Font(device, fd);
		gc.setFont(currentFont);
	}
	
	public void dispose(){
		gc.dispose();
	}

	@Override
	public double getTranslateX() {
		return translateX;
	}

	@Override
	public double getTranslateY() {
		return translateY;
	}

	
}
