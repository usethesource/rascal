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
import java.util.Set;
import java.util.Stack;
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
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.KeySymFactory;
import org.rascalmpl.library.vis.containers.Box;
import org.rascalmpl.library.vis.containers.Overlap;
import org.rascalmpl.library.vis.containers.Space;
import org.rascalmpl.library.vis.containers.WhiteSpace;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.interaction.MouseOver;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.zorder.SWTZOrderManager;
import org.rascalmpl.library.vis.swtwidgets.SWTWidgetFigure;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.KeySymTranslate;
import org.rascalmpl.library.vis.util.Rectangle;
import org.rascalmpl.values.ValueFactoryFactory;

public class FigureSWTApplet extends ScrolledComposite 
	implements IFigureConstructionEnv, PaintListener, MouseListener,MouseMoveListener,  ControlListener, MouseTrackListener, DisposeListener, KeyListener {

	private static final int SWT_FLAGS = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER ;
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
	private SWTZOrderManager zorderManager;
	private Vector<Overlap> overlapFigures;
	private Vector<MouseOver> mouseOverFigures;


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
		fig = new WhiteSpace( fig, new PropertyManager());
		this.figure = fig;
		zorderManager = new SWTZOrderManager(this,inner);
		overlapFigures = new Vector<Overlap>();
		mouseOverFigures = new Stack<MouseOver>();
	}

	private void draw(GC swtGC) {
		long startTime = System.nanoTime();
		swtGC.setBackground(SWTFontsAndColors.getColor(SWT.COLOR_WHITE));
		GraphicsContext gc = new SWTGraphicsContext(swtGC);
		figure.draw(gc);
		for(Overlap f : overlapFigures){
			if(!(f.nonLocalFigure instanceof SWTWidgetFigure)) f.nonLocalFigure.draw(gc);
		}
		gc.dispose();
		if(FigureExecutionEnvironment.profile) System.out.printf("Drawing took %f\n", ((double)(System.nanoTime() - startTime)) / 1000000.0);
	}
	
	public void drawPart(Rectangle part,GraphicsContext gc){
		long startTime = System.nanoTime();
		figure.drawPart(part,gc);
		
		for(Overlap f : overlapFigures){
			if(f.nonLocalFigure.overlapsWith(part)){
				f.nonLocalFigure.drawPart(part,gc);
			}
		}
		gc.dispose();
		if(FigureExecutionEnvironment.profile) System.out.printf("Drawing (part) took %f\n", ((double)(System.nanoTime() - startTime)) / 1000000.0);
	}
	
	public void layoutForce(){
		layoutFigures(true);
		for(FigureSWTApplet child : children){
			child.layoutForce();
		}
		inner.redraw();
	}

	private void layoutFigures(boolean force) {
		overlapFigures.clear();
		org.eclipse.swt.graphics.Rectangle r = getClientArea();
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
			setMinSize((int) Math.ceil(viewPort.getWidth()),
				(int) Math.ceil(viewPort.getHeight()));
			handleZOrder();	
		}
		updateFiguresUnderMouse(true);
	}

	private void handleZOrder() {
		overlapFigures.clear();
		zorderManager.begin();
		figure.setSWTZOrder(zorderManager);
		zorderManager.end();
	}

	private void updateFiguresUnderMouse(boolean partOfLayout) {
		Vector<Figure> swp = figuresUnderMousePrev;
		figuresUnderMousePrev = figuresUnderMouse;
		figuresUnderMouse = swp;
		figuresUnderMouse.clear();
		figure.getFiguresUnderMouse(mouseLocation, figuresUnderMouse);
		for(Overlap f : overlapFigures){
			f.nonLocalFigure.getFiguresUnderMouse(mouseLocation, figuresUnderMouse);
		}
		int i = 0; int j = 0;
		Collections.sort(figuresUnderMouse);
		Collections.sort(mouseOverFigures);
		// compute the added and removed elements in a fast way
		boolean mouseOverChanged = false;
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
				int index = Collections.binarySearch(mouseOverFigures, figuresUnderMouse.get(i));
				if(index >= 0){
					mouseOverFigures.get(index).setMouseOver();
					mouseOverChanged = true;
				}
				i++;
			} else if(cmp > 0){
				figuresUnderMousePrev.get(j).executeMouseOffHandlers(env);
				int index = Collections.binarySearch(mouseOverFigures, figuresUnderMousePrev.get(j));
				if(index >= 0 ){
					mouseOverFigures.get(index).unsetMouseOver();
					mouseOverChanged = true;
				}
				j++;
			} else {
				i++; j++;
			}
		}
		if(mouseOverChanged && env.isBatchEmpty()) {
			handleZOrder(); // rehandle z ordering now that we now what is under mouse
			inner.redraw();
		}
		env.endCallbackBatch(true);
	}
	
	public void registerMouseOver(MouseOver mouseOver) {
		mouseOverFigures.add(mouseOver);
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
		updateFiguresUnderMouse(false);
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
		updateFiguresUnderMouse(false);
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
	public Composite getSWTParent() {
		return inner;
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
	
	public void addOverlapFigure(Overlap overlap){
		overlapFigures.add(overlap);
	}
}
