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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.util.KeySym;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;

public class FigureSWTApplet implements IFigureApplet {

	enum SHAPE {
		ELLIPSE, RECTANGLE
	};

	String name;

	int halign = FigureApplet.LEFT, valign = FigureApplet.TOP;

	private Device device;

	private int alphaStroke = 255, alphaFill = 255, alphaFont = 255;

	public Color getColor(final int which) {
		return device.getSystemColor(which);
	}

	public Color getRgbColor(final int c) {
		return new Color(device, FigureColorUtils.getRed(c),
				FigureColorUtils.getGreen(c), FigureColorUtils.getBlue(c));
	}

	private boolean shadow;
	private boolean mouseExited = true;
	private final int defaultWidth = 5000; // Default dimensions of canvas
	private final int defaultHeight = 5000;

	// private Figure figure; // The figure that is drawn on the canvas
	private IList primitives;
	private double figureWidth = defaultWidth;
	private double figureHeight = defaultHeight;

	private Figure focus = null;

	private boolean resized = false;
	private static boolean debug = false;
	@SuppressWarnings("unused")
	private boolean saveFigure = true;
	@SuppressWarnings("unused")
	private String file;
	@SuppressWarnings("unused")
	private double scale = 1.0f;
	private int left = 0;
	private int top = 0;
	private double width = 0;
	private double height = 0;
	private boolean layingOut = false;

	private Stack<PlacedFigure> mouseOverStack;
	private Stack<Figure> mouseOverCausesStack;
	private Vector<Figure> figuresUnderMouse; // deepest figure first
	private Vector<Figure> prevFiguresUnderMouse;
	private Vector<Figure> figuresUnderMouseSorted;
	private Vector<Figure> prevFiguresUnderMouseSorted;
	private PlacedFigure currentFig;
	private boolean mouseOverTop;
	volatile GC gc;
	private BoundingBox viewPort;

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
	final private boolean renderDisplay;
	IEvaluatorContext ctx;

	// private PGraphics canvas;
	// private PFont stdFont;

	public Composite getComp() {
		return comp;
	}

	@SuppressWarnings("unused")
	private int lastMouseX = 0;
	@SuppressWarnings("unused")
	private int lastMouseY = 0;
	private int mouseX = 0, mouseY = 0;

	private int shadowColor;

	private double shadowLeft;

	private double shadowTop;

	public FigureSWTApplet(Composite comp, IConstructor fig,
			IEvaluatorContext ctx) {
		this(comp, "Figure", fig, ctx);
	}

	public FigureSWTApplet(Composite comp, String name, Figure fig,
			IEvaluatorContext ctx) {
		this.comp = comp;
		this.device = comp.getDisplay();
		this.renderDisplay = !comp.getShell().equals(comp);
		this.ctx = ctx;
		initialize(comp, name, fig);
	}

	public FigureSWTApplet(Composite comp, String name, IConstructor fig,
			IEvaluatorContext ctx) {
		this.comp = comp;
		this.device = comp.getDisplay();
		this.renderDisplay = !comp.getShell().equals(comp);
		this.ctx = ctx;
		Figure figure = FigureFactory.make(this, fig, null, null, ctx);
		initialize(comp, name, figure);
	}

	public FigureSWTApplet(Composite comp, String name, IList primitives,
			IEvaluatorContext ctx) {
		this.primitives = primitives;
		this.comp = comp;
		this.renderDisplay = !comp.getShell().equals(comp);
		this.device = comp.getDisplay();
		this.ctx = ctx;
		initialize(comp, name, null);
	}

	void initialize(Composite comp, String name, Figure fig) {

		viewPort = new BoundingBox();
		comp.getShell().setText(name);
		gc = createGC(comp);
		int colnum = (Integer) Properties.FILL_COLOR.stdDefault;
		Color color = new Color(device, FigureColorUtils.getRed(colnum),
				FigureColorUtils.getGreen(colnum),
				FigureColorUtils.getBlue(colnum));
		comp.setBackground(color);
		if (renderDisplay) {
			comp.addPaintListener(new MyPaintListener());
			comp.getParent().addControlListener(new ControlListener() {
				public void controlResized(ControlEvent e) {
					redraw();
				}

				public void controlMoved(ControlEvent e) {
					redraw();
				}
			});
			comp.addKeyListener(new MyKeyListener());
		}
		if (fig != null) {
			comp.addMouseMoveListener(new MyMouseMoveListener());
			comp.addMouseListener(new MyMouseListener());
			comp.addMouseTrackListener(new MyMouseTrackListener());
			mouseOverStack = new Stack<PlacedFigure>();
			mouseOverCausesStack = new Stack<Figure>();
			mouseOverStack.push(new PlacedFigure(new Coordinate(0, 0),
					new BoundingBox(), fig));
			figuresUnderMouse = new Vector<Figure>();
			prevFiguresUnderMouse = new Vector<Figure>();
			figuresUnderMouseSorted = new Vector<Figure>();
			prevFiguresUnderMouseSorted = new Vector<Figure>();
			mouseOverTop = true;
			layoutFigures();
		} else {
			figureWidth = width = 800;
			figureHeight = height = 800;
		}
		this.name = name;
	}

	public class PlacedFigure {
		public Coordinate coordinate;
		public Coordinate offset;
		public BoundingBox bounds;
		public Figure figure;
		public boolean computedValueChanged;

		PlacedFigure(Coordinate coordinate, BoundingBox bounds, Figure figure) {
			// System.out.printf("Created : %s %s %s\n",coordinate,bounds,figure);
			this.coordinate = coordinate;
			this.figure = figure;
			this.bounds = bounds;
			offset = new Coordinate();
			computedValueChanged = true;
		}
	}

	private GC setPrinter(Printer printer) {
		synchronized (gc) {
			GC gc0 = this.gc;
			this.gc = new GC(printer);
			this.gc.setAntialias(SWT.ON);
			this.gc.setTextAntialias(SWT.ON);
			this.gc.setBackground(getColor(SWT.COLOR_WHITE));
			this.device = printer;
			return gc0;
		}
	}

	private void unsetPrinter(GC gc) {
		synchronized (gc) {
			if (gc.isDisposed())
				gc = createGC(comp);
			this.gc = gc;
			this.device = gc.getDevice();
		}
	}

	private GC createGC(Composite comp) {
		GC g = new GC(comp);
		g.setAntialias(SWT.ON);
		g.setTextAntialias(SWT.ON);
		g.setBackground(getColor(SWT.COLOR_WHITE));
		return g;
	}

	public void redraw() {
		comp.redraw();
	}

	private synchronized void drawFigure() {
		// System.err.println("draw:" + this.getClass() + " "
		// + computedValueChanged+" "+mouseOver);
		if (layingOut) {
			System.out.print("Will not draw while laying out!");
			return;
		}
		layoutFigures();

		// System.out.printf("Compcomp!!!!!!!!!!!! %s %s\n",comp,this.comp);
		gc.fillRectangle(0, 0, (int) figureWidth, (int) figureHeight);

		// figure.draw(left, top);
		/*
		 * System.out.printf("Mouseover stack:");
		 */
		for (PlacedFigure fig : mouseOverStack) {

			// System.out.printf("draw %s %s %s\n", fig.figure,fig.coordinate
			// ,fig.offset);
			Coordinate place = new Coordinate();
			for (boolean flip : Figure.BOTH_DIMENSIONS) {
				place.setX(flip,
						fig.coordinate.getX(flip) + fig.offset.getX(flip));

			}

			place.setX(Math.min(place.getX(), viewPort.getWidth()
					- fig.figure.size.getWidth()));
			place.setY(Math.min(place.getY(), viewPort.getHeight()
					- fig.figure.size.getHeight()));
			place.setX(Math.max(0, place.getX()));
			place.setY(Math.max(0, place.getY()));

			fig.figure.draw(place.getX(), place.getY());
		}
		// System.out.printf("\n");
		// System.out.printf("Done drawing!\n");
		/*
		 * if (mouseOver != null) mouseOver
		 * .drawWithMouseOver(mouseOver.getLeft(), mouseOver.getTop()); if
		 * (focus != null && focusSelected) focus.drawFocus();
		 */
	}

	private void layoutFigures() {

		synchronized (this) {
			// System.out.printf("Layout \n");
			// System.out.printf("Compcomp!!!!!!!!!!!! %s %s\n",comp,this.comp);
			layingOut = true;
			NameResolver resolver = new NameResolver(this, ctx);
			for (PlacedFigure fig : mouseOverStack) {
				currentFig = fig;
				if (fig.computedValueChanged) {
					// System.out.printf("compute on %s \n", fig.figure);
					fig.figure.init();
					fig.figure.computeFiguresAndProperties();
					fig.figure.registerNames(resolver);
					fig.figure.registerValues(resolver);
					fig.figure.getLikes(resolver);
					fig.figure.finalize();
					fig.figure.bbox();

				}

			}
			// TODO: fix this hack;
			if (mouseOverStack.elementAt(0).computedValueChanged
					&& mouseOverStack.size() == 2) {
				mouseOverStack.pop();
			}

			viewPort = new BoundingBox(width, height);

			// System.out.printf("drawing inside %s \n", viewPort);
			PlacedFigure bottom = mouseOverStack.get(0);
			// System.out.printf("drawing inside %s \n", bottom.bounds);
			bottom.bounds.set(viewPort);
			boolean bottomChanged = false;
			for (int i = 0; i < mouseOverStack.size(); i++) {
				PlacedFigure fig = mouseOverStack.get(i);
				if (resized || fig.computedValueChanged) {
					currentFig = fig;
					// System.out.printf("Bbox on %s\n", fig);

					for (boolean flip : Figure.BOTH_DIMENSIONS) {
						// System.out.printf("blab bla %f %s\n",
						// fig.coordinate.getX(flip) +
						// fig.figure.minSize.getWidth(flip) /
						// fig.figure.getHShrinkProperty(flip), flip);
						fig.figure.takeDesiredWidth(flip, Math.max(
								fig.bounds.getWidth(flip)
										* fig.figure.getHShrinkProperty(flip),
								fig.figure.minSize.getWidth(flip)
										* fig.figure.getHShrinkProperty(flip)));
						if (i == 0) {
							bottomChanged = true;
							viewPort.setWidth(flip, Math.max(
									viewPort.getWidth(flip),
									fig.figure.size.getWidth(flip)));
						}
					}
					if (i == 0) {
						fig.bounds.set(viewPort);
					}
					fig.figure.layout();
					for (boolean flip : Figure.BOTH_DIMENSIONS) {
						double margin = (fig.bounds.getWidth(flip) - fig.figure.size
								.getWidth(flip));
						fig.offset.setX(flip,
								margin * fig.figure.getHAlignProperty(flip));
					}
					fig.computedValueChanged = false;
					// System.out.printf("Placed %d %s %s %s %s %s\n",i,
					// fig.coordinate,fig.offset,fig.bounds,fig.figure.minSize,viewPort);
				}

			}
			if (bottomChanged) {
                if (renderDisplay) {
					((ScrolledComposite) comp.getParent()).setMinSize(
						(int) Math.ceil(viewPort.getWidth()),
						(int) Math.ceil(viewPort.getHeight()));
                }
				figureWidth = viewPort.getWidth();
				figureHeight = viewPort.getHeight();

				comp.layout();
			}

			mouseMoved();
			layingOut = false;

		}
		// this.notifyAll();
		// comp.setSize(2000,800);
		// System.out.printf("Setting %s %f %f\n",this, viewPort.getWidth(),
		// viewPort.getHeight());
	}

	public int getFigureWidth() {
		return FigureApplet.round(figureWidth);
	}

	public int getFigureHeight() {
		return FigureApplet.round(figureHeight);
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

	private void executeMouseOverOffHandlers() {
		int i, j;
		i = j = 0;
		currentFig = mouseOverStack.peek();
		while (i < figuresUnderMouseSorted.size()
				|| j < prevFiguresUnderMouseSorted.size()) {
			if (i < figuresUnderMouseSorted.size()
					&& j < prevFiguresUnderMouseSorted.size()
					&& figuresUnderMouseSorted.get(i) == prevFiguresUnderMouseSorted
							.get(j)) {
				i++;
				j++;
			} else if (i < figuresUnderMouseSorted.size()
					&& (j >= prevFiguresUnderMouseSorted.size() || figuresUnderMouseSorted
							.get(i).sequenceNr < prevFiguresUnderMouseSorted
							.get(j).sequenceNr)) {
				if (!figuresUnderMouseSorted.get(i).isMouseOverSet()) {
					if (figuresUnderMouseSorted.get(i).isHandlerPropertySet(
							Properties.ON_MOUSEOVER)) {
						// System.out.printf("Mouse over %s \n",figuresUnderMouseSorted.get(i),figuresUnderMouseSorted.get(i).sequenceNr);
						figuresUnderMouseSorted.get(i)
								.executeMouseOverHandlers();
						comp.redraw();
					}

				}
				i++;
			} else { // i >= figuresUnderMouseSorted.size() || newFig.sequenceNr
						// > oldFig.sequenceNr
				if (!prevFiguresUnderMouseSorted.get(j).isMouseOverSet()) {
					if (prevFiguresUnderMouseSorted.get(j)
							.isHandlerPropertySet(Properties.ON_MOUSEOFF)) {
						prevFiguresUnderMouseSorted.get(j)
								.executeMouseOffHandlers();
						comp.redraw();
					}
				}
				j++;
			}
		}
	}

	private void updateFiguresUnderMouse() {

		Vector<Figure> swp = prevFiguresUnderMouse;
		prevFiguresUnderMouse = figuresUnderMouse;
		figuresUnderMouse = prevFiguresUnderMouse;
		figuresUnderMouse.clear();
		PlacedFigure topPlacedFigure = mouseOverStack.peek();
		if (mouseExited) {
			figuresUnderMouse.clear();
		} else {
			topPlacedFigure.figure.getFiguresUnderMouse(new Coordinate(mouseX,
					mouseY), figuresUnderMouse);
		}
		if (figuresUnderMouse.isEmpty() && mouseOverStack.size() != 1) {
			PlacedFigure topPlacedFigurePrev = mouseOverStack
					.get(mouseOverStack.size() - 2);
			topPlacedFigurePrev.figure.getFiguresUnderMouse(new Coordinate(
					mouseX, mouseY), figuresUnderMouse);
			mouseOverTop = false;
		} else {
			mouseOverTop = true; // mouseOverStack.size() <= 1 ||
									// mouseOverStack.peek().figure.properties.isBooleanPropertySet(Properties.MOUSE_STICK)
									// ;
		}
		swp = prevFiguresUnderMouseSorted;
		prevFiguresUnderMouseSorted = figuresUnderMouseSorted;
		figuresUnderMouseSorted = swp;
		figuresUnderMouseSorted.clear();
		figuresUnderMouseSorted.addAll(figuresUnderMouse);
		Collections.sort(figuresUnderMouseSorted);
		executeMouseOverOffHandlers();
		// comp.redraw();
		/*
		 * System.out.printf("under mouse:"); for(Figure fig :
		 * figuresUnderMouseSorted) { System.out.printf("%s ", fig); }
		 * 
		 * System.out.printf("\n");
		 */
	}

	void setMouseOverFigure() {
		updateFiguresUnderMouse();
		if (!mouseOverTop) { // mouse not on top figure, do we need to pop?
			boolean donotPop = false;
			for (Figure fig : figuresUnderMouse) {
				if (fig.isMouseOverSet()
						&& fig.getMouseOverProperty() == mouseOverStack.peek().figure) {
					donotPop = true;
					break;
				}
			}
			if (!donotPop) {
				if (!mouseOverCausesStack.isEmpty()) {
					mouseOverCausesStack.peek().executeMouseOffHandlers();
					/*
					 * System.out.printf("Mouse off %s %d\n",
					 * mouseOverCausesStack.peek(),
					 * mouseOverCausesStack.peek().sequenceNr);
					 */
					mouseOverCausesStack.pop();
					comp.redraw();
				}
				mouseOverStack.pop();
				// computedValueChanged = true;
			}
			return;
		}
		for (Figure fig : figuresUnderMouse) {
			if (fig.isMouseOverSet()) {
				fig.executeMouseOverHandlers();
				// System.out.printf("Mouse over %s %d\n", fig, fig.sequenceNr);
				mouseOverCausesStack.push(fig);
				Figure mouseOver = fig.getMouseOverProperty();
				// mouseOver.bbox();
				double left = fig.getLeft()
						+ fig.size.getWidth()
						* (0.5 - fig
								.getRealProperty(Properties.MOUSEOVER_HALIGN));
				if (left < 0)
					left = 0;
				/*
				 * if(left + fig.minSize.getWidth() > comp.getBounds().width){
				 * left = comp.getBounds().width - fig.minSize.getWidth(); }
				 */
				double top = fig.getTop()
						+ fig.size.getHeight()
						* (0.5 - fig
								.getRealProperty(Properties.MOUSEOVER_VALIGN));
				if (top < 0)
					top = 0;
				/*
				 * if(top + fig.minSize.getHeight() > vie.height){ top =
				 * comp.getBounds().height - fig.minSize.getHeight(); }
				 */
				/*
				 * System.out.printf("Pushed %s %s %s\n", new Coordinate(left,
				 * top), fig.size, mouseOver);
				 */
				mouseOverStack.push(new PlacedFigure(new Coordinate(left, top),
						fig.size, mouseOver));
				layoutFigures();
				comp.redraw();
				// computedValueChanged = true;
				return;
			}
		}
	}

	void handleMouseClick() {
		// System.out.printf("Handling mouse click2!\n");
		synchronized (this) {
			for (Figure fig : figuresUnderMouse) {
				if (mouseOverTop) {
					currentFig = mouseOverStack.peek();
				} else {
					if (mouseOverStack.size() >= 1) {
						currentFig = mouseOverStack.elementAt(mouseOverStack
								.size() - 2);
					}
				}
				if (fig.isHandlerPropertySet(Properties.MOUSE_CLICK)) {
					// System.out.printf("MOUSE click on %s!\n",fig);
					fig.executeOnClick();
					comp.redraw();
					return;
				} else {
					// System.out.printf("no mouse click on %s!\n",fig);
				}
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

	boolean computedValueChanged() {
		for (PlacedFigure fig : mouseOverStack) {
			if (fig.computedValueChanged)
				return true;
		}
		return false;
	}

	public void mouseMoved() {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		setMouseOverFigure();
		/*
		 * if (computedValueChanged()) { comp.redraw(); }
		 */
		/*
		 * figure.getFiguresUnderMouse(new Coordinate(mouseX,mouseY),
		 * figuresUnderMouse); if (debug)
		 * System.err.println("========= mouseMoved: " + mouseX + ", " +
		 * mouseY); if (mousePressed) { figure.mouseDragged(mouseX, mouseY); }
		 * else { lastMouseX = mouseX; lastMouseY = mouseY; if
		 * (!figure.mouseOver(mouseX, mouseY, false))
		 * unRegisterMouseOver(mouseOver); } comp.redraw();
		 */
	}

	public void mouseDragged() {
		if (debug)
			System.err.println("========= mouseDragged: " + mouseX + ", "
					+ mouseY);

		// lastMouseX = mouseX;
		// lastMouseY = mouseY;

		// figure.mouseOver(mouseX, mouseY, false);
		// figure.mouseDragged(mouseX, mouseY);
		// comp.redraw();

	}

	public void mousePressed() {

		handleMouseClick();
		/*
		 * if (debug) System.err.println("=== FigurePApplet.mousePressed: " +
		 * mouseX + ", " + mouseY); lastMouseX = mouseX; lastMouseY = mouseY;
		 * unRegisterMouseOver(mouseOver); if (figure.mousePressed(mouseX,
		 * mouseY, null)) { focusSelected = true; if (debug)
		 * System.err.println("" + this.getClass() + " " + focusSelected); }
		 * else unRegisterFocus(focus); mousePressed = true; comp.redraw();
		 */

	}

	public void setComputedValueChanged() {
		synchronized (this) {

			for (PlacedFigure fig : mouseOverStack) {
				fig.computedValueChanged = true;
			}
		}
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
			if (shadow) {
				drawShadowFigure(SHAPE.RECTANGLE, arg0, arg1, arg2, arg3);
			}
			gc.fillRectangle(arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawRectangle(arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}

	}

	public void ellipse(double x1, double y1, double width, double height) {
		int arg0 = FigureApplet.round(x1), arg1 = FigureApplet.round(y1), arg2 = FigureApplet
				.round(width), arg3 = FigureApplet.round(height);
		int alpha0 = gc.getAlpha();
		if (fill) {
			gc.setAlpha(alphaFill);
			if (shadow) {
				drawShadowFigure(SHAPE.ELLIPSE, arg0, arg1, arg2, arg3);
			}
			gc.fillOval(arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawOval(arg0, arg1, arg2, arg3);
			gc.setAlpha(alpha0);
		}
	}

	public void fill(int arg0) {
		alphaFill = FigureColorUtils.getAlpha(arg0);
		Color color = new Color(device, FigureColorUtils.getRed(arg0),
				FigureColorUtils.getGreen(arg0), FigureColorUtils.getBlue(arg0));
		gc.setBackground(color);
		fill = true;
	}

	public void stroke(int arg0) {
		alphaStroke = FigureColorUtils.getAlpha(arg0);
		gc.setForeground(new Color(device, FigureColorUtils.getRed(arg0),
				FigureColorUtils.getGreen(arg0), FigureColorUtils.getBlue(arg0)));
		stroke = true;
	}

	public void strokeWeight(double arg0) {
		int d = (int) arg0;
		stroke = (d != 0);
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		gc.setLineWidth(d);
	}

	public void strokeStyle(int style) {
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		gc.setLineStyle(style);
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
		gc.setForeground(new Color(device, FigureColorUtils.getRed(arg0),
				FigureColorUtils.getGreen(arg0), FigureColorUtils.getBlue(arg0)));
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
		if (gc == null || gc.isDisposed())
			gc = createGC(comp);
		return gc.getFontMetrics().getDescent();
	}

	public void text(String arg0, double x, double y) {
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
		if (stroke) {
			gc.setAlpha(alphaStroke);
			gc.drawPath(p);
			gc.setAlpha(alpha0);
		}
		p.dispose();
	}

	private void print() {
		/*
		 * if (figure != null) { figure.bbox(); figureWidth =
		 * figure.minSize.getWidth(); figureHeight = figure.minSize.getHeight();
		 * figure.draw(left, top); }
		 */
		this.drawFigure();
	}

	public Object createFont(String fontName, double fontSize) {
		FontData fd = new FontData(fontName, (int) fontSize, SWT.NORMAL);
		return new Font(device, fd);
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

	public String getName() {
		return name;
	}

	public Object getFont() {
		// TODO Auto-generated method stub
		return gc.getFont();
	}

	class MyKeyListener implements KeyListener {
		IMap modifierMap;

		public MyKeyListener() {
			Type[] empty = {};
			Type modType = ctx.getCurrentEnvt().abstractDataType("KeyModifier",
					empty);
			modifierMap = ValueFactory.getInstance().map(modType,
					TypeFactory.getInstance().boolType());
		}

		public void keyPressed(KeyEvent e) {
			IValue keySym = KeySym.toRascalKey(e, ctx);
			modifierMap = KeySym.toRascalModifiers(e, modifierMap, ctx);
			for (Figure fig : figuresUnderMouse) {
				fig.executeKeyDownHandlers(keySym, modifierMap);
			}
		}

		public void keyReleased(KeyEvent e) {
			IValue keySym = KeySym.toRascalKey(e, ctx);
			modifierMap = KeySym.toRascalModifiers(e, modifierMap, ctx);
			for (Figure fig : figuresUnderMouse) {
				fig.executeKeyUpHandlers(keySym, modifierMap);
			}
		}

	}

	class MyMouseTrackListener implements MouseTrackListener {

		public void mouseEnter(MouseEvent e) {
			comp.forceFocus();
			mouseExited = false;
		}

		public void mouseExit(MouseEvent e) {
			mouseExited = true;
			mouseMoved();
		}

		public void mouseHover(MouseEvent e) {

		}

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
			// System.err.println("mouseDown:(" + mouseX + "," + mouseY + ")");
			mousePressed();
			// computedValueChanged = true;
			redraw();
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

	void changeRes() {
		// Composite p = comp.getParent().getParent().getParent().getParent();
		// Point size = p.getSize();

		// p.getClientArea().width;

		double newXsize = ((double) comp.getParent().getClientArea().width);
		;
		double newYsize = (double) comp.getParent().getClientArea().height;
		if (newXsize != width || newYsize != height) {
			resized = true;
			width = newXsize;
			height = newYsize;
			// System.out.printf("Resized %f %f\n", width, height);
			layoutFigures();

		}
	}

	class MyPaintListener implements PaintListener {

		public void paintControl(PaintEvent e) {
			if (e.count > 0)
				return;
			gc = e.gc;
			gc.setAntialias(SWT.ON);
			gc.setTextAntialias(SWT.ON);
			gc.setAdvanced(true);
			// if (figure != null) {
			changeRes();
			FigureSWTApplet.this.drawFigure();
			/*
			 * } else if (primitives != null) {
			 * FigureSWTApplet.this.drawPrimitives(); }
			 */
			resized = false;
		}
	}

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

	public void checkIfIsCallBack(IValue fun, IEvaluatorContext ctx) {
		if (!(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunctionResult)))) {
			throw RuntimeExceptionFactory.illegalArgument(fun,
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}

	public Result<IValue> executeRascalCallBack(IValue callback,
			Type[] argTypes, IValue[] argVals) {

		synchronized (this) {
			assert (callback instanceof ICallableValue);
			Cursor cursor0 = comp.getCursor();
			Cursor cursor = new Cursor(device, SWT.CURSOR_WAIT);
			comp.setCursor(cursor);
			// System.err.printf("doing callBack: %s \n", callback);

			Result<IValue> result = null;
			try {
				result = ((ICallableValue) callback).call(argTypes, argVals);
			} catch (Throw e) {
				e.printStackTrace();
				System.err.printf("Callback error: " + e.getMessage() + ""
						+ e.getTrace());
			}

			// System.err.printf("done callBack: \n");
			comp.setCursor(cursor0);
			cursor.dispose();
			return result;
		}

	}

	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) {
		Type[] argTypes = {};
		IValue[] argVals = {};

		return executeRascalCallBack(callback, argTypes, argVals);
	}

	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback,
			Type type, IValue arg) {
		Type[] argTypes = { type };
		IValue[] argVals = { arg };
		return executeRascalCallBack(callback, argTypes, argVals);
	}

	public void dispose() {
		gc.dispose();
	}

	public GC getPrinterGC() {
		if (this.device instanceof Printer)
			return gc;
		return null;
	}

	public void print(Printer printer) {
		synchronized (gc) {
			GC gc0 = setPrinter(printer);
			print();
			unsetPrinter(gc0);
		}
	}

	public void write(OutputStream out, int mode) {
		Image image = new Image(comp.getDisplay(), getFigureWidth(),
				getFigureHeight());
		gc = new GC(image);
		drawFigure();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(out, mode);
		gc.dispose();
		image.dispose();
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

	private void drawPrimitives() {
		IList elems = this.primitives;
		for (int i = 0; i < elems.length(); i++) {
			IConstructor c = (IConstructor) elems.get(i);
			PRIMITIVE.valueOf(c.getName()).draw(this, c);
		}
	}

}
