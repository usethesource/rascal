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
package org.rascalmpl.library.vis.swt;

import java.util.Collections;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.KeySymFactory;
import org.rascalmpl.library.vis.containers.Box;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.KeySymTranslate;
import org.rascalmpl.values.ValueFactoryFactory;

public class FigureSWTApplet extends ScrolledComposite 
	implements IFigureConstructionEnv, ISWTZOrdering, PaintListener, MouseListener,MouseMoveListener,  ControlListener, MouseTrackListener, DisposeListener, KeyListener {

	private static final int SWT_FLAGS = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
	private static boolean debug = false;
	private Vector<FigureSWTApplet> children;
	private Composite inner;
	public static IMap keyboardModifierMap = 
		ValueFactoryFactory.getValueFactory().map(KeySymFactory.KeyModifier, TypeFactory.getInstance().boolType()); // there is only 1 keyboard , hence static
	private Coordinate mouseLocation;
	Figure figure; 
	private Vector<Figure> figuresUnderMouse; 
	private Vector<Figure> figuresUnderMousePrev;
	private BoundingBox lastSize;
	volatile GC gc;
	private FigureExecutionEnvironment env;

	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env){
		this(parent,cfig,env,true,true);
	}
	
	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env,boolean hscroll,boolean vscroll) {
		super(parent, SWT_FLAGS);
		this.env = env;
		inner = new Composite(this, 0);
		figuresUnderMouse = new Vector<Figure>();
		figuresUnderMousePrev = new Vector<Figure>();
		inner.addPaintListener(this);
		inner.addMouseMoveListener(this);
		inner.addMouseListener(this);
		inner.addKeyListener(this);
		addControlListener(this);
		mouseLocation = new Coordinate();
		setContent(inner);
		setAlwaysShowScrollBars(false);
		setExpandHorizontal(hscroll);
		setExpandVertical(vscroll);
		children = new Vector<FigureSWTApplet>();
		lastSize = null;
		Figure fig = FigureFactory.make(this, cfig, null, null);
		fig = new Box( fig, new PropertyManager());
		this.figure = fig;
	}

	private void draw(GC swtGC) {
		swtGC.setBackground(SWTFontsAndColors.getColor(SWT.COLOR_WHITE));
		figure.draw(new SWTGraphicsContext(swtGC));
	}
	
	public void layoutForce(){
		figure.setSWTZOrder(this);
		layoutFigures(true);
		for(FigureSWTApplet child : children){
			child.layoutForce();
		}
		inner.redraw();
	}

	private void layoutFigures(boolean force) {
		Rectangle r = getBounds();
		BoundingBox curSize = new BoundingBox(r.width,r.height);
		boolean resized = !(curSize.isEq(lastSize));
		lastSize = curSize;
		
		if (resized || force) {
			BoundingBox viewPort = 
				new BoundingBox(
						Math.max(curSize.getWidth(),
								figure.minSize.getWidth() / figure.getHShrinkProperty()),
						Math.max(curSize.getHeight(),
									figure.minSize.getHeight() / figure.getVShrinkProperty()));
			for (boolean flip : Figure.BOTH_DIMENSIONS) {
				figure.takeDesiredWidth(flip,viewPort.getWidth(flip));
			}
			
			figure.layout();
			setSize((int) Math.ceil(viewPort.getWidth()),
				(int) Math.ceil(viewPort.getHeight()));

		}
		updateFiguresUnderMouse();
	}

	private void updateFiguresUnderMouse() {
		Vector<Figure> swp = figuresUnderMousePrev;
		figuresUnderMousePrev = figuresUnderMouse;
		figuresUnderMouse = swp;
		figuresUnderMouse.clear();
		figure.getFiguresUnderMouse(mouseLocation, figuresUnderMouse);
		int i = 0; int j = 0;
		Collections.sort(figuresUnderMouse);
		// compute the added and removed elements in a fast way
		env.beginCallbackBatch();
		while(i < figuresUnderMouse.size() || j < figuresUnderMousePrev.size()){
			int cmp;
			if(i >= figuresUnderMouse.size()){
				cmp = 1;
			} else if(j >= figuresUnderMousePrev.size()){
				cmp = -1;
			} else {
				cmp = figuresUnderMouse.get(i).sequenceNr - figuresUnderMousePrev.get(j).sequenceNr;
			}
			if(cmp < 0){
				figuresUnderMouse.get(i).executeMouseOverHandlers(env);
				i++;
			} else if(cmp > 0){
				figuresUnderMousePrev.get(j).executeMouseOffHandlers(env);
				j++;
			} else {
				i++; j++;
			}
		}
		env.endCallbackBatch();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		env.beginCallbackBatch();
		IValue keySym = KeySymTranslate.toRascalKey(e, env.getRascalContext());
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e, keyboardModifierMap, env.getRascalContext());
		for (Figure fig : figuresUnderMouse) {
			fig.executeKeyDownHandlers(env,keySym, keyboardModifierMap);
		}
		env.endCallbackBatch();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		env.beginCallbackBatch();
		IValue keySym = KeySymTranslate.toRascalKey(e, env.getRascalContext());
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e, keyboardModifierMap, env.getRascalContext());
		for (Figure fig : figuresUnderMouse) {
			fig.executeKeyUpHandlers(env,keySym, keyboardModifierMap);
		}
		env.endCallbackBatch();
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		mouseLocation.set(e.x,e.y);
		updateFiguresUnderMouse();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}

	@Override
	public void mouseDown(MouseEvent e) {
		env.beginCallbackBatch();
		for(Figure fig : figuresUnderMouse){
			fig.executeOnClick(env);
		}
		env.endCallbackBatch();
	}

	@Override
	public void mouseUp(MouseEvent e) {}

	@Override
	public void paintControl(PaintEvent e) {
		if(lastSize == null){
			lastSize = new BoundingBox();
			layoutFigures(true);
		}
		draw(e.gc);
	}
	
	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		layoutFigures(false);
		redraw();
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		forceFocus();
	}

	@Override
	public void mouseExit(MouseEvent e) {
		mouseLocation.set(-100,-100); // not on figure!
		updateFiguresUnderMouse();
	}

	@Override
	public void mouseHover(MouseEvent e) {}
	
	
	public FigureExecutionEnvironment getExectutionEnv(){ 
		return env;
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		gc.dispose();
	}
	
	
	public void registerChild(FigureSWTApplet child){
		children.add(child);
	}

	@Override
	public void pushOverlap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popOverlap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Figure fig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Composite getSWTParent() {
		return this;
	}

	@Override
	public ICallbackEnv getCallBackEnv() {
		return env;
	}

	@Override
	public IEvaluatorContext getRascalContext() {
		return env.getRascalContext();
	}

	@Override
	public FigureExecutionEnvironment getFigureExecEnv() {
		return env;
	}
	
	public Figure getFigure(){
		return figure;
	}

	

	/*
		public void write(OutputStream out, int mode) {
			Image image = new Image(comp.getDisplay(), getFigureWidth(),
					getFigureHeight());
			GC gc = new GC(image);
			drawFigure(gc);
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { image.getImageData() };
			loader.save(out, mode);
			gc.dispose();
			image.dispose();
		}
	*/
	
}
