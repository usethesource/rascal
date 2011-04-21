/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class FigureSWTApplet implements IFigureApplet {

	int halign = FigureApplet.LEFT, valign = FigureApplet.TOP;

	private int alphaStroke = 255, alphaFill = 255, alphaFont = 255;

	private static Color getColor(final int which) {
		Display display = Display.getCurrent();
		if (display != null)
			return display.getSystemColor(which);
		display = Display.getDefault();
		final Color result[] = new Color[1];
		display.syncExec(new Runnable() {
			public void run() {
				synchronized (result) {
					result[0] = Display.getCurrent().getSystemColor(which);
				}
			}
		});
		synchronized (result) {
			return result[0];
		}
	}

	// private int width; // Current dimensions of canvas
	// private int height;

	private final int defaultWidth = 5000; // Default dimensions of canvas
	private final int defaultHeight = 5000;

	private Figure figure; // The figure that is drawn on the canvas
	private float figureWidth = defaultWidth;
	private float figureHeight = defaultHeight;

	private Figure focus = null;
	private boolean focusSelected = false;

	private Figure mouseOver = null;
	private boolean computedValueChanged = true;

	private static boolean debug = false;
	private boolean saveFigure = true;
	private String file;
	private float scale = 1.0f;
	private int left = 0;
	private int top = 0;
	volatile GC gc;

	@SuppressWarnings("serial")
	class Route extends ArrayList<TypedPoint> {

		void add(float x, float y, TypedPoint.kind curved) {
			super.add(new TypedPoint(x, y, curved));
		}
	}

	private Stack<Transform> stackMatrix = new Stack<Transform>();
	private Stack<Route> stackPath = new Stack<Route>();

	private boolean fill = false, stroke = true;

	final private Composite comp;

	// private PGraphics canvas;
	// private PFont stdFont;

	public Composite getComp() {
		return comp;
	}

	private int depth = 0;

	@SuppressWarnings("unused")
	private int lastMouseX = 0;
	@SuppressWarnings("unused")
	private int lastMouseY = 0;
	private int mouseX = 0, mouseY = 0;

	public FigureSWTApplet(Composite comp, IConstructor fig,
			IEvaluatorContext ctx) {
		this(comp, "Figure", fig, ctx);
	}

	public FigureSWTApplet(Composite comp, String name, IConstructor fig,
			IEvaluatorContext ctx) {
		this.comp = comp;
		saveFigure = false;
		comp.getShell().setText(name);
		this.figure = FigureFactory.make(this, fig, null, null, ctx);
		gc = new GC(comp);
		bbox();
		figureWidth = (int) figure.width + 1;
		figureHeight = (int) figure.height + 1;
		comp.addMouseMoveListener(new MyMouseMoveListener());
		comp.addMouseListener(new MyMouseListener());
		comp.addPaintListener(new MyPaintListener());
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void setup() {
		// TODO Auto-generated method stub

	}

	public void bbox() {
		if (computedValueChanged) {
			figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
			figureWidth = figure.width;
			figureHeight = figure.height;
			// computedValueChanged = false;
		}
	}

	public void draw() {
		// System.err.println("draw:" + this.getClass() + " "
		// + computedValueChanged+" "+mouseOver);
		if (computedValueChanged) {
			figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
			figureWidth = figure.width;
			figureHeight = figure.height;
			computedValueChanged = false;
		}
		figure.draw(left, top);
		if (mouseOver != null)
			mouseOver
					.drawWithMouseOver(mouseOver.getLeft(), mouseOver.getTop());
		if (focus != null && focusSelected)
			focus.drawFocus();
	}

	public int getFigureWidth() {
		// System.err.println("getFigureWidth: " + figureWidth);
		return FigureApplet.round(figureWidth);
	}

	public int getFigureHeight() {
		// System.err.println("getFigureHeight: " + figureHeight);
		return FigureApplet.round(figureHeight);
	}

	public void incDepth() {
		depth++;

	}

	public void decDepth() {
		depth--;
	}

	public boolean isVisible(int d) {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerId(String id, Figure fig) {
		// TODO Auto-generated method stub

	}

	public Figure getRegisteredId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerFocus(Figure f) {
		focus = f;
		if (debug)
			System.err.println("registerFocus:" + f);

	}

	public boolean isRegisteredAsFocus(Figure f) {
		return focus == f;
	}

	public void unRegisterFocus(Figure f) {
		if (debug)
			System.err.println("unRegisterFocus:" + f);
		focus = null;
		focusSelected = false;

	}

	public void registerMouseOver(Figure f) {
		mouseOver = f;
		if (debug)
			System.err.println("registerMouseOver:" + f);
	}

	public boolean isRegisteredAsMouseOver(Figure f) {
		return mouseOver == f;
	}

	public void unRegisterMouseOver(Figure f) {
		if (debug)
			System.err.println("unRegisterMouseOver:" + f);
		mouseOver = null;
	}

	public void keyPressed() {
		// TODO Auto-generated method stub

	}

	public void mouseReleased() {
		if (debug)
			System.err.println("========= mouseReleased");
		// focusSelected = false;
		figure.mouseReleased();

	}

	public void mouseMoved() {
		if (debug)
			System.err.println("========= mouseMoved: " + mouseX + ", "
					+ mouseY);
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		if (!figure.mouseOver(mouseX, mouseY, false))
			unRegisterMouseOver(mouseOver);
		comp.redraw();
	}

	public void mouseDragged() {
		if (debug)
			System.err.println("========= mouseDragged: " + mouseX + ", "
					+ mouseY);

		// lastMouseX = mouseX;
		// lastMouseY = mouseY;

		// figure.mouseOver(mouseX, mouseY, false);
		figure.mouseDragged(mouseX, mouseY);
		comp.redraw();

	}

	public void mousePressed() {
		if (debug)
			System.err.println("=== FigurePApplet.mousePressed: " + mouseX
					+ ", " + mouseY);
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		unRegisterMouseOver(mouseOver);
		if (figure.mousePressed(mouseX, mouseY, null)) {
			focusSelected = true;
			if (debug)
				System.err.println("" + this.getClass() + " " + focusSelected);
		} else
			unRegisterFocus(focus);
		comp.redraw();

	}

	public void setComputedValueChanged() {
		// TODO Auto-generated method stub

	}

	public void line(float arg0, float arg1, float arg2, float arg3) {
		gc.drawLine((int) arg0, (int) arg1, (int) arg2, (int) arg3);
	}

	public void rect(float arg0, float arg1, float arg2, float arg3) {
		int alpha0 = gc.getAlpha();
		if (fill) {
			gc.setAlpha(alphaFill);
			gc.fillRectangle((int) arg0, (int) arg1, (int) arg2, (int) arg3);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawRectangle((int) arg0, (int) arg1, (int) arg2, (int) arg3);
			gc.setAlpha(alpha0);
		}

	}

	public void ellipse(float arg0, float arg1, float arg2, float arg3) {
		// CORNERS
		int alpha0 = gc.getAlpha();
		if (fill) {
			gc.setAlpha(alphaFill);
			gc.fillOval((int) arg0, (int) arg1, (int) (arg2 - arg0),
					(int) (arg3 - arg1));
			gc.setAlpha(alpha0);

		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawOval((int) arg0, (int) arg1, (int) (arg2 - arg0),
					(int) (arg3 - arg1));
			gc.setAlpha(alpha0);
		}

	}

	public void rectMode(int arg0) {
		// TODO Auto-generated method stub

	}

	public void ellipseMode(int arg0) {
		// TODO Auto-generated method stub

	}

	public void fill(int arg0) {
		alphaFill = FigureColorUtils.getAlpha(arg0);
		Color color = new Color(comp.getDisplay(),
				FigureColorUtils.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0));
		gc.setBackground(color);
		fill = true;
	}

	public void stroke(int arg0) {
		alphaStroke = FigureColorUtils.getAlpha(arg0);
		gc.setForeground(new Color(comp.getDisplay(), FigureColorUtils
				.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0)));
		stroke = true;
	}

	public void strokeWeight(float arg0) {
		int d = (int) arg0;
		stroke =(d!=0);
		if (gc.isDisposed())
			gc = new GC(comp);
		gc.setLineWidth(d);
	}

	public void textSize(float arg0) {
		if (gc.isDisposed())
			gc = new GC(comp);
		if (gc.getFont().getFontData().length < 1)
			return;
		gc.getFont().getFontData()[0].setHeight((int) arg0);

	}

	public void textAlign(int arg0, int arg1) {
		halign = arg0;
		valign = arg1;
	}

	public void textAlign(int arg0) {
		halign = arg0;
	}

	public void textFont(Object arg0) {
		if (gc.isDisposed())
			gc = new GC(comp);
		gc.setFont((Font) arg0);
	}

	public void textColor(int arg0) {
		alphaFont = FigureColorUtils.getAlpha(arg0);
		gc.setForeground(new Color(comp.getDisplay(), FigureColorUtils
				.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0)));
	}

	public float textWidth(String txt) {
		if (gc.isDisposed())
			gc = new GC(comp);
		return gc.textExtent(txt).x;
	}

	public float textAscent() {
		if (gc.isDisposed())
			gc = new GC(comp);
		return gc.getFontMetrics().getAscent();
	}

	public float textDescent() {
		// TODO Auto-generated method stub
		if (gc.isDisposed())
			gc = new GC(comp);
		return gc.getFontMetrics().getDescent();
	}

	public void text(String arg0, float x, float y) {
		// TODO Auto-generated method stub
		float width = textWidth(arg0);
		String[] lines = arg0.split("\n");
		int nlines = lines.length;
		float topAnchor = textAscent(), bottomAnchor = textDescent();
		float height = nlines > 1 ? (nlines * (topAnchor + bottomAnchor) + bottomAnchor)
				: (topAnchor + bottomAnchor);
		if (halign == FigureApplet.CENTER)
			x -= width / 2;
		else if (halign == FigureApplet.RIGHT)
			x -= width;
		if (valign == FigureApplet.CENTER)
			y -= height / 2;
		else if (valign == FigureApplet.BOTTOM)
			y -= height;
		int alpha0 = gc.getAlpha();
		gc.setAlpha(alphaFont);
		gc.drawText(arg0, (int) x, (int) y);
		gc.setAlpha(alpha0);
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

	public void rotate(float angle) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.rotate(FigureApplet.degrees(angle));
		gc.setTransform(transform);
	}

	public void translate(float x, float y) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.translate(x, y);
		gc.setTransform(transform);
	}

	public void scale(float scaleX, float scaleY) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.scale(scaleX, scaleY);
		gc.setTransform(transform);
	}

	public void bezierVertex(float cx1, float cy1, float cx2, float cy2,
			float x, float y) {
		Route r = stackPath.peek();
		r.add(cx1, cy1, TypedPoint.kind.BEZIER);
		r.add(cx2, cy2, TypedPoint.kind.BEZIER);
		r.add(x, y, TypedPoint.kind.BEZIER);
	}

	public void vertex(float x, float y) {
		Route r = stackPath.peek();
		r.add(x, y, TypedPoint.kind.NORMAL);
	}

	public void curveVertex(float x, float y) {
		Route r = stackPath.peek();
		r.add(x, y, TypedPoint.kind.CURVED);
	}

	public void noFill() {
		fill = false;
	}

	public void arc(float arg0, float arg1, float arg2, float arg3, float arg4,
			float arg5) {
		// TODO Auto-generated method stub

	}

	public void beginShape() {
		Route p = new Route();
		stackPath.push(p);
	}

	public void beginShape(int arg0) {
		// TODO Auto-generated method stub

	}

	private void drawNotCurved(Route r, Path p) {
		// System.err.println("drawNotCurved:" + r.size());
		while (!r.isEmpty()) {
			TypedPoint z = r.get(0);
			// System.err.println("Curved:" + z.curved);
			if (z.curved == TypedPoint.kind.NORMAL) {
				p.lineTo(z.x, z.y);
				r.remove(0);
			} else if (z.curved == TypedPoint.kind.BEZIER) {
				float c1x = z.x, c1y = z.y;
				r.remove(0);
				z = r.remove(0);
				float c2x = z.x, c2y = z.y;
				z = r.remove(0);
				float x = z.x, y = z.y;
				p.cubicTo(c1x, c1y, c2x, c2y, x, y);
			} else {
				break;
			}
		}
	}

	private void drawCurved(Route r, Path p, boolean closed) {
		System.err.println("drawCurved:" + r.size());
		if (r.size() < 3)
			return;
		Interpolation.solve(r, closed);
		int n = Interpolation.P0.length;
		for (int i = 0; i < n; i++)
			p.cubicTo(Interpolation.P1[i].x, Interpolation.P1[i].y,
					Interpolation.P2[i].x, Interpolation.P2[i].y,
					Interpolation.P3[i].x, Interpolation.P3[i].y);
	}

	public void endShape() {
		endShape(FigurePApplet.OPEN);
	}

	public void endShape(int arg0) {
		Route r = stackPath.pop();
		Path p = new Path(gc.getDevice());
		if (debug)
			System.err.println("endShape1:" + r.size());
		if (arg0 == FigureApplet.CLOSE) {
			// r.remove(0); // Remove Origin Vertex
			// r.remove(1); // Remove Start Vertex
			// r.remove(r.size() - 1); // Remove Origin Vertex
			if (debug)
				System.err.println("endShape:" + r.get(r.size() - 1).curved);
			if (r.get(2).curved == TypedPoint.kind.CURVED)
				r.remove(2);
			if (r.get(r.size() - 2).curved == TypedPoint.kind.CURVED)
				r.remove(r.size() - 2);
		} else {
			if (r.get(0).curved == TypedPoint.kind.CURVED)
				r.remove(0);
			if (r.get(r.size() - 1).curved == TypedPoint.kind.CURVED)
				r.remove(r.size() - 1);
		}
		TypedPoint q = r.get(0);
		if (q.curved != TypedPoint.kind.CURVED)
			r.remove(0);
		p.moveTo(q.x, q.y);
		if (debug)
			System.err.println("q=(" + q.x + "," + q.y + " " + q.curved + ")");
		if (arg0 == FigureApplet.CLOSE) {
			r.add(new TypedPoint(q.x, q.y, TypedPoint.kind.NORMAL));
		}
		while (!r.isEmpty()) {
			drawNotCurved(r, p);
			drawCurved(r, p, arg0 == FigureApplet.CLOSE);
		}
		int alpha0 = gc.getAlpha();
		if (fill /*arg0 == FigureApplet.CLOSE*/) {
			gc.setAlpha(alphaFill);
			gc.fillPath(p);
			gc.setAlpha(alpha0);
		}
		gc.setAlpha(alphaStroke);
		gc.drawPath(p);
		gc.setAlpha(alpha0);
		p.dispose();
	}

	public void print() {
		// TODO Auto-generated method stub

	}

	public float random(float lub, float hub) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object createFont(String fontName, float fontSize) {
		// TODO Auto-generated method stub
		FontData fd = new FontData(fontName, (int) fontSize, SWT.NORMAL);
		return new Font(comp.getDisplay(), fd);
	}

	public void smooth() {
		// TODO Auto-generated method stub

	}

	public void setCursor(Object cursor) {
		// TODO Auto-generated method stub

	}

	public void add(Object comp) {
		// TODO Auto-generated method stub

	}

	public void remove(Object comp) {
		// TODO Auto-generated method stub

	}

	public Object getFont(Object font) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBackground(Object color) {
		// TODO Auto-generated method stub
		gc.setBackground((Color) color);

	}

	public void setForeground(Object color) {
		// TODO Auto-generated method stub
		gc.setForeground((Color) color);

	}

	public void invalidate() {
		// TODO Auto-generated method stub

	}

	public void validate() {
		// TODO Auto-generated method stub

	}

	public void stroke(float arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getFont() {
		// TODO Auto-generated method stub
		return gc.getFont();
	}

	class MyMouseMoveListener implements MouseMoveListener {

		public void mouseMove(MouseEvent e) {
			mouseX = e.x;
			mouseY = e.y;
			mouseMoved();
		}

	}

	class MyMouseListener implements MouseListener {

		public void mouseDown(MouseEvent e) {
			mouseX = e.x;
			mouseY = e.y;
			System.err.println("mouseDown:(" + mouseX + "," + mouseY + ")");
			mousePressed();
		}

		public void mouseDoubleClick(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseUp(MouseEvent e) {
			mouseX = e.x;
			mouseY = e.y;
			mouseReleased();
		}

	}

	class MyPaintListener implements PaintListener {

		public void paintControl(PaintEvent e) {
			gc = e.gc;
			gc.setTextAntialias(SWT.ON);
			gc.setAntialias(SWT.ON);
			gc.setAdvanced(true);
			gc.setBackground(getColor(SWT.COLOR_WHITE));
			gc.fillRectangle(0, 0, (int) figureWidth, (int) figureHeight);
			FigureSWTApplet.this.draw();
		}
	}

	class StartPaintListener implements PaintListener {

		public void paintControl(PaintEvent e) {
			if (gc == null) {
				gc = e.gc;
				gc.setTextAntialias(SWT.ON);
				gc.setAntialias(SWT.ON);
				gc.setAdvanced(true);
				bbox();
				System.err.println("StartPaintListener:" + figure.width + " "
						+ figure.height + " " + gc);
				figureWidth = (int) figure.width + 1;
				figureHeight = (int) figure.height + 1;
				gc.setBackground(getColor(SWT.COLOR_WHITE));
				gc.fillRectangle(0, 0, (int) figureWidth, (int) figureHeight);
			}
		}
	}

}
