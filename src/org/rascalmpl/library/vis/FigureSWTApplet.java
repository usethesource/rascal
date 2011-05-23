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
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.util.Coordinate;

public class FigureSWTApplet implements IFigureApplet {

	int halign = FigureApplet.LEFT, valign = FigureApplet.TOP;

	public enum Mode {
		CORNER, CORNERS, CENTER, RADIUS
	};

	Mode ellipseM = Mode.CORNER, rectM = Mode.CORNER;

	private final Device device;

	private int alphaStroke = 255, alphaFill = 255, alphaFont = 255;

	public Color getColor(final int which) {
	    return device.getSystemColor(which);
	}

	public Color getRgbColor(final int c) {
		return new Color(device, FigureColorUtils.getRed(c),
				FigureColorUtils.getGreen(c), FigureColorUtils.getBlue(c));
	}

	private final int defaultWidth = 5000; // Default dimensions of canvas
	private final int defaultHeight = 5000;

	private Figure figure; // The figure that is drawn on the canvas
	private double figureWidth = defaultWidth;
	private double figureHeight = defaultHeight;

	private Figure focus = null;

	private boolean computedValueChanged = true;

	private static boolean debug = false;
	@SuppressWarnings("unused")
	private boolean saveFigure = true;
	@SuppressWarnings("unused")
	private String file;
	@SuppressWarnings("unused")
	private double scale = 1.0f;
	private int left = 0;
	private int top = 0;
	
	private Stack<PlacedFigure> mouseOverStack; 
	private Stack<Figure> mouseOverCausesStack;
	private Vector<Figure> figuresUnderMouse; // deepest figure first
	private Vector<Figure> prevFiguresUnderMouse;
	private Vector<Figure> figuresUnderMouseSorted;
	private Vector<Figure> prevFiguresUnderMouseSorted;
	private boolean mouseOverTop;
	volatile GC gc;

	@SuppressWarnings("serial")
	class Route extends ArrayList<TypedPoint> {

		void add(double x, double y, TypedPoint.kind curved) {
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

	public FigureSWTApplet(Printer printer, IConstructor fig,
			IEvaluatorContext ctx) {
		this.comp = null;
		this.device = printer;
		this.figure = FigureFactory.make(this, fig, null, null, ctx);
		this.gc = new GC(printer);
	}
	
	public FigureSWTApplet(Composite comp, String name, Figure fig,
			IEvaluatorContext ctx) {
		this.comp = comp;
		this.device = comp.getDisplay();
		this.figure = fig;
		initialize(comp, name);
	}

	public FigureSWTApplet(Composite comp, String name, IConstructor fig,
			IEvaluatorContext ctx) {
		this.comp = comp;
		this.device = comp.getDisplay();
		this.figure = FigureFactory.make(this, fig, null, null, ctx);
		initialize(comp, name);
	}

	void initialize(Composite comp, String name) {
		
		comp.getShell().setText(name);
		gc = createGC(comp);
		int colnum = (Integer) Properties.FILL_COLOR.stdDefault;
		Color color = new Color(device,
				FigureColorUtils.getRed(colnum),
				FigureColorUtils.getGreen(colnum),
				FigureColorUtils.getBlue(colnum));
		comp.setBackground(color);
		comp.addMouseMoveListener(new MyMouseMoveListener());
		comp.addMouseListener(new MyMouseListener());
		comp.addPaintListener(new MyPaintListener());
		mouseOverStack = new Stack<PlacedFigure>();
		mouseOverCausesStack = new Stack<Figure>();
		mouseOverStack.push(new PlacedFigure(new Coordinate(0, 0),figure));
		figuresUnderMouse = new Vector<Figure>();
		prevFiguresUnderMouse = new Vector<Figure>();
		figuresUnderMouseSorted = new Vector<Figure>();
		prevFiguresUnderMouseSorted = new Vector<Figure>();
		mouseOverTop = true;
		computedValueChanged = true;
		layoutFigures();
	}

	private GC createGC(Composite comp) {
		GC g = new GC(comp);
		g.setAntialias(SWT.ON);
		g.setTextAntialias(SWT.ON);
		g.setBackground(getColor(SWT.COLOR_WHITE));
		return g;
	}
	
	public void init() {
		// TODO Auto-generated method stub

	}

	public void setup() {
		// TODO Auto-generated method stub

	}
	

	public void redraw() {
		comp.redraw();
	}


	private void draw() {
		// System.err.println("draw:" + this.getClass() + " "
		// + computedValueChanged+" "+mouseOver);
		layoutFigures();
		
		gc.fillRectangle(0, 0, (int) figureWidth, (int) figureHeight);
		
		//figure.draw(left, top);
		for(PlacedFigure fig : mouseOverStack){
			fig.figure.draw(left + fig.coordinate.getX(), top + fig.coordinate.getY());
		}
		//System.out.printf("Done drawing!\n");
		/*if (mouseOver != null)
			mouseOver
					.drawWithMouseOver(mouseOver.getLeft(), mouseOver.getTop());
		if (focus != null && focusSelected)
			focus.drawFocus();
		*/
	}

	void layoutFigures() {
		if (computedValueChanged) {
			
			for(PlacedFigure fig : mouseOverStack){
				fig.figure.computeFiguresAndProperties();
				fig.figure.registerNames();
				
			}
			double maxWidth, maxHeight;
			maxWidth = maxHeight = 0;
			for(PlacedFigure fig : mouseOverStack){
				fig.figure.bbox();
				maxWidth = Math.max(fig.coordinate.getX() + fig.figure.width, maxWidth);
				maxHeight = Math.max(fig.coordinate.getY() + fig.figure.height,maxHeight);
			}
			figureWidth = maxWidth;
			figureHeight = maxHeight;
			computedValueChanged = false;
			//comp.setSize((int)Math.ceil(maxWidth), (int)Math.ceil(maxHeight));
			//comp.setSize(2000,800);
			System.out.printf("Setting %s %f %f\n",this,maxWidth,maxHeight);
			
		}
	}

	public int getFigureWidth() {
		 System.err.println("getFigureWidth: " + figureWidth);
		return FigureApplet.round(figureWidth);
	}

	public int getFigureHeight() {
		 System.err.println("getFigureHeight: " + figureHeight);
		return FigureApplet.round(figureHeight);
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
	}
	
	private void executeMouseOverOffHandlers(){
		int i, j;
		i = j = 0;
		while(i < figuresUnderMouseSorted.size() || j < prevFiguresUnderMouseSorted.size()){
			if(i < figuresUnderMouseSorted.size() &&  j < prevFiguresUnderMouseSorted.size() 
				&&  figuresUnderMouseSorted.get(i) == prevFiguresUnderMouseSorted.get(j)){
				i++;
				j++;
			} else if(i < figuresUnderMouseSorted.size() && (j >= prevFiguresUnderMouseSorted.size() || 
					figuresUnderMouseSorted.get(i).sequenceNr < prevFiguresUnderMouseSorted.get(j).sequenceNr)) {
				if(!figuresUnderMouseSorted.get(i).isMouseOverSet()){
					if(figuresUnderMouseSorted.get(i).isHandlerPropertySet(Properties.ON_MOUSEOVER)){
						computedValueChanged = true;
						//System.out.printf("Mouse over %s \n",figuresUnderMouseSorted.get(i),figuresUnderMouseSorted.get(i).sequenceNr);
						figuresUnderMouseSorted.get(i).executeMouseOverHandlers();
					}
					
				}
				i++;
			} else { // i >=  figuresUnderMouseSorted.size()  || newFig.sequenceNr > oldFig.sequenceNr
				if(!prevFiguresUnderMouseSorted.get(j).isMouseOverSet()){
					if(prevFiguresUnderMouseSorted.get(j).isHandlerPropertySet(Properties.ON_MOUSEOFF)){
						computedValueChanged = true;
						prevFiguresUnderMouseSorted.get(j).executeMouseOffHandlers();
					}
				}
				j++;
			}
		}
	}
	
	private void updateFiguresUnderMouse(){
		Vector<Figure> swp = prevFiguresUnderMouse;
		prevFiguresUnderMouse = figuresUnderMouse;
		figuresUnderMouse = prevFiguresUnderMouse;
		figuresUnderMouse.clear();
		PlacedFigure topPlacedFigure = mouseOverStack.peek();
		topPlacedFigure.figure
    		.getFiguresUnderMouse(new Coordinate(mouseX,mouseY), figuresUnderMouse);
		if(figuresUnderMouse.isEmpty() && mouseOverStack.size()!=1){
			PlacedFigure topPlacedFigurePrev = mouseOverStack.get(mouseOverStack.size() -2);
			topPlacedFigurePrev.figure
    		.getFiguresUnderMouse(new Coordinate(mouseX,mouseY), figuresUnderMouse);
			mouseOverTop = false;
		} else {
			mouseOverTop = true;
		}
		swp = prevFiguresUnderMouseSorted;
		prevFiguresUnderMouseSorted = figuresUnderMouseSorted;
		figuresUnderMouseSorted = swp;
		figuresUnderMouseSorted.clear();
		figuresUnderMouseSorted.addAll(figuresUnderMouse);
		Collections.sort(figuresUnderMouseSorted);
		executeMouseOverOffHandlers();
	}
	
	void setMouseOverFigure(){
		updateFiguresUnderMouse();
		if(!mouseOverTop){ // mouse not on top figure, do we need to pop?
			boolean donotPop = false;
			for(Figure fig : figuresUnderMouse){
				if(fig.isMouseOverSet() && fig.getMouseOverProperty() == mouseOverStack.peek().figure){
					donotPop = true;
					break;
				}
			}
			if(!donotPop){
				if(!mouseOverCausesStack.isEmpty()){
					mouseOverCausesStack.peek().executeMouseOffHandlers();
					//System.out.printf("Mouse off %s %d\n",mouseOverCausesStack.peek(),mouseOverCausesStack.peek().sequenceNr);
					mouseOverCausesStack.pop();
				}
				mouseOverStack.pop();
				computedValueChanged = true;
			}
			return;
		}
		for(Figure fig : figuresUnderMouse){
			if(fig.isMouseOverSet()){
				fig.executeMouseOverHandlers();
				//System.out.printf("Mouse over %s %d\n",fig,fig.sequenceNr);
				mouseOverCausesStack.push(fig);
				Figure mouseOver = fig.getMouseOverProperty();
				mouseOver.bbox();
				double left = fig.getLeft() + fig.width * fig.getRealProperty(Properties.MOUSEOVER_HALIGN);
				left-=mouseOver.leftAlign();
				if(left < 0) left = 0;
				if(left + fig.width > comp.getBounds().width){
					left = comp.getBounds().width - fig.width;
				}
				double top = fig.getTop() + fig.height * fig.getRealProperty(Properties.MOUSEOVER_VALIGN);
				top-=mouseOver.topAlign();
				if(top < 0) top = 0;
				if(top + fig.height > comp.getBounds().height){
					top = comp.getBounds().height - fig.height;
				}
				mouseOverStack.push(new PlacedFigure(new Coordinate(left,top),mouseOver ));
				computedValueChanged = true;
				return;
			}
		}
	}
	
	void handleMouseClick() {
		//System.out.printf("Handling mouse click2!\n");
		for(Figure fig : figuresUnderMouse){
			if(fig.isHandlerPropertySet(Properties.MOUSE_CLICK)){
				//System.out.printf("MOUSE click on %s!\n",fig);
				fig.executeOnClick();
				computedValueChanged = true;
				comp.redraw();
				return;
			} else {
				//System.out.printf("no mouse click on %s!\n",fig);
			}
		}
		
	}

	public void keyPressed() {
		// TODO Auto-generated method stub

	}

	public void mouseReleased() {
		if (debug)
			System.err.println("========= mouseReleased");

	}

	public void mouseMoved() {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		setMouseOverFigure();
		if(computedValueChanged){
			comp.redraw();
		}
		/*figure.getFiguresUnderMouse(new Coordinate(mouseX,mouseY), figuresUnderMouse);
		if (debug)
			System.err.println("========= mouseMoved: " + mouseX + ", "
					+ mouseY);
		if (mousePressed) {
			figure.mouseDragged(mouseX, mouseY);
		} else {
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			if (!figure.mouseOver(mouseX, mouseY, false))
				unRegisterMouseOver(mouseOver);
		}
		comp.redraw(); */
	}

	public void mouseDragged() {
		if (debug)
			System.err.println("========= mouseDragged: " + mouseX + ", "
					+ mouseY);

		// lastMouseX = mouseX;
		// lastMouseY = mouseY;

		// figure.mouseOver(mouseX, mouseY, false);
		//figure.mouseDragged(mouseX, mouseY);
		//comp.redraw();

	}

	public void mousePressed() {
		handleMouseClick();
		/*if (debug)
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
		mousePressed = true;
		comp.redraw();
		*/

	}

	public void setComputedValueChanged() {
		computedValueChanged = true;

	}

	public void line(double arg0, double arg1, double arg2, double arg3) {
		gc.drawLine((int) arg0, (int) arg1, (int) arg2, (int) arg3);
	}

	public void rect(double x, double y, double width, double height) {
		int alpha0 = gc.getAlpha();
		int arg0 = FigureApplet.round(x), arg1 = FigureApplet.round(y), arg2 = FigureApplet
				.round(width), arg3 = FigureApplet.round(height);
		if (fill) {
			gc.setAlpha(alphaFill);
			paintShape(new FillRectangle(), arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			paintShape(new DrawRectangle(), arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}

	}

	private void paintShape(PaintShape p, int arg0, int arg1, int arg2, int arg3) {
		switch (p.getMode()) {
		case CORNERS:
			p.paintShape(arg0, arg1, (arg2 - arg0), (arg3 - arg1));
			return;
		case CORNER:
			p.paintShape(arg0, arg1, arg2, arg3);
			return;
		case CENTER:
			p.paintShape(arg0 - arg2 / 2, arg1 - arg3 / 2, arg2, arg3);
			return;
		case RADIUS:
			p.paintShape(arg0 - arg2, arg1 - arg3, 2 * arg2, 2 * arg3);
			return;
		}
	}

	public void ellipse(double x1, double y1, double x2, double y2) {
		// CORNERS
		int arg0 = FigureApplet.round(x1), arg1 = FigureApplet.round(y1), arg2 = FigureApplet
				.round(x2), arg3 = FigureApplet.round(y2);
		int alpha0 = gc.getAlpha();
		if (fill) {
			gc.setAlpha(alphaFill);
			paintShape(new FillOval(), arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);

		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			paintShape(new DrawOval(), arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}

	}

	public void rectMode(int arg0) {
		switch (arg0) {
		case FigureApplet.CORNER:
			rectM = Mode.CORNER;
			return;
		case FigureApplet.CORNERS:
			rectM = Mode.CORNERS;
			return;
		}

	}

	public void ellipseMode(int arg0) {
		switch (arg0) {
		case FigureApplet.CORNER:
			ellipseM = Mode.CORNER;
			return;
		case FigureApplet.CORNERS:
			ellipseM = Mode.CORNERS;
			return;
		}
	}

	public void fill(int arg0) {
		alphaFill = FigureColorUtils.getAlpha(arg0);
		Color color = new Color(device,
				FigureColorUtils.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0));
		gc.setBackground(color);
		fill = true;
	}

	public void stroke(int arg0) {
		alphaStroke = FigureColorUtils.getAlpha(arg0);
		gc.setForeground(new Color(device, FigureColorUtils
				.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0)));
		stroke = true;
	}

	public void strokeWeight(double arg0) {
		int d = (int) arg0;
		stroke = (d != 0);
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		gc.setLineWidth(d);
	}

	public void textSize(double arg0) {
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
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
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		gc.setFont((Font) arg0);
	}

	public void textColor(int arg0) {
		alphaFont = FigureColorUtils.getAlpha(arg0);
		gc.setForeground(new Color(device, FigureColorUtils
				.getRed(arg0), FigureColorUtils.getGreen(arg0),
				FigureColorUtils.getBlue(arg0)));
	}

	public double textWidth(String txt) {
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		return gc.textExtent(txt).x;
	}

	public double textAscent() {
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		return gc.getFontMetrics().getAscent();
	}

	public double textDescent() {
		// TODO Auto-generated method stub
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		return gc.getFontMetrics().getDescent();
	}


	public void text(String arg0, double x, double y) {
		// TODO Auto-generated method stub
		double width = textWidth(arg0);
		String[] lines = arg0.split("\n");
		int nlines = lines.length;
		double topAnchor = textAscent(), bottomAnchor = textDescent();
		double height = nlines > 1 ? (nlines * (topAnchor + bottomAnchor) + bottomAnchor)
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
		gc.drawText(arg0, (int) x, (int) y, true);
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

	public void rotate(double angle) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.rotate((float) FigureApplet.degrees(angle));
		gc.setTransform(transform);
	}

	public void translate(double x, double y) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.translate((float) x, (float) y);
		gc.setTransform(transform);
	}

	public void scale(double scaleX, double scaleY) {
		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);
		transform.scale((float) scaleX, (float) scaleY);
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
				(int) FigureApplet.degrees(startAngle),
				(int) FigureApplet.degrees(stopAngle));

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
		endShape(FigureApplet.OPEN);
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
		p.moveTo((float) q.x, (float) q.y);
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
		if (fill /* arg0 == FigureApplet.CLOSE */) {
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
		figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
		figureWidth = figure.width;
		figureHeight = figure.height;
		figure.draw(left, top);

	}

	public Object createFont(String fontName, double fontSize) {
		FontData fd = new FontData(fontName, (int) fontSize, SWT.NORMAL);
		return new Font(device, fd);
	}

	public void smooth() {
		// TODO Auto-generated method stub

	}

	
	public Cursor getCursor() {
		return comp.getCursor();
	}

	public void setCursor(Cursor cursor) {
	    comp.setCursor(cursor);
	}

	public Object getFont(Object font) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBackground(Color color) {
		// TODO Auto-generated method stub
		gc.setBackground(color);

	}

	public void setForeground(Color color) {
		// TODO Auto-generated method stub
		gc.setForeground(color);

	}

	public void invalidate() {
		// TODO Auto-generated method stub

	}

	public void validate() {

	}

	public void stroke(double arg0, double arg1, double arg2) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return "Rascal Figure";
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
			//System.err.println("mouseDown:(" + mouseX + "," + mouseY + ")");
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
			Rectangle r = comp.getBounds();
			//System.out.printf("max size %d %d %d %d\n",r.x,r.y,r.width,r.height);
			
			/*try{
				new Exception().printStackTrace();
			} catch(Exception d){
				
			}*/
			//System.out.printf("Paint event! %s %s %d %d %d %d\n",this,e.widget,e.width,e.height,e.x,e.y);
//			gc.setTextAntialias(SWT.ON);
//			gc.setAntialias(SWT.ON);
//			gc.setAdvanced(true);
//			gc.setBackground(getColor(SWT.COLOR_WHITE));
			if(gc.isDisposed()){
				gc = new GC(comp);
				FigureSWTApplet.this.draw();
			} else {
				FigureSWTApplet.this.draw();
			}
		}
	}

	interface PaintShape {
		public Mode getMode();

		public void paintShape(int x1, int y1, int x2, int y2);
	}

	class DrawOval implements PaintShape {
		Mode mode = ellipseM;

		@Override
		public void paintShape(int x, int y, int width, int height) {
			gc.drawOval(x, y, width, height);
		}

		@Override
		public Mode getMode() {
			return mode;
		}
	}

	class DrawRectangle implements PaintShape {
		Mode mode = rectM;

		@Override
		public void paintShape(int x, int y, int width, int height) {
			gc.drawRectangle(x, y, width, height);
		}

		@Override
		public Mode getMode() {
			return mode;
		}
	}

	class FillOval implements PaintShape {
		Mode mode = ellipseM;

		@Override
		public void paintShape(int x, int y, int width, int height) {
			gc.fillOval(x, y, width, height);
		}

		@Override
		public Mode getMode() {
			return mode;
		}
	}

	class FillRectangle implements PaintShape {
		Mode mode = rectM;

		@Override
		public void paintShape(int x, int y, int width, int height) {
			gc.fillRectangle(x, y, width, height);
		}

		@Override
		public Mode getMode() {
			return mode;
		}
	}

	public void checkIfIsCallBack(IValue fun, IEvaluatorContext ctx) {
		if (!(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunctionResult)))) {
			throw RuntimeExceptionFactory.illegalArgument(fun, ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}

	public Result<IValue> executeRascalCallBack(IValue callback, Type[] argTypes, IValue[] argVals) {
		assert (callback instanceof ICallableValue);
		Cursor cursor0 = comp.getCursor();
		Cursor cursor = new Cursor(device, SWT.CURSOR_WAIT);
		comp.setCursor(cursor);
		Result<IValue> result = ((ICallableValue)callback).call(argTypes, argVals);
		comp.setCursor(cursor0);
		cursor.dispose();
		return result;

	}

	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) {
		Type[] argTypes = {};
		IValue[] argVals = {};
		return executeRascalCallBack(callback, argTypes, argVals);
	}

	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback, Type type, IValue arg) {
		Type[] argTypes = { type };
		IValue[] argVals = { arg };
		return executeRascalCallBack(callback, argTypes, argVals);
	}
	
	public void dispose() {
		gc.dispose();
	}
	
	public GC getPrinterGC(){
		// TODO Implement
		return null;
	}
	
	public void print(Printer printer){
		// TODO Implement
	}

}
