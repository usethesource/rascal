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
import java.util.Stack;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.KeySym;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.WhiteSpace;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.SWTWidgetFigure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.properties.IRunTimePropertyChanges;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.zorder.IHasZOrder;
import org.rascalmpl.library.vis.swt.zorder.IHasZOrderStableComparator;
import org.rascalmpl.library.vis.swt.zorder.SWTZOrderManager;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.ForBothDimensions;
import org.rascalmpl.library.vis.util.KeySymTranslate;
import org.rascalmpl.library.vis.util.Rectangle;
import org.rascalmpl.values.ValueFactoryFactory;

public class FigureSWTApplet extends Composite 
	implements IFigureConstructionEnv, PaintListener, MouseListener,MouseMoveListener, 
	ControlListener, MouseTrackListener, DisposeListener, KeyListener , SelectionListener,
	MouseWheelListener, IRunTimePropertyChanges{

	public static BoundingBox scrollableMinSize; 
	public static BoundingBox scrollbarSize; // width of vertical scrollbar, height of horizontal
	private static final int SWT_FLAGS = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.DOUBLE_BUFFERED ;
	private static boolean debug = false;
	private BoundingBox size;
	private Coordinate location;
	private Vector<FigureSWTApplet> children;
	public static IMap keyboardModifierMap = 
		ValueFactoryFactory.getValueFactory().map(KeySym.KeyModifier, TypeFactory.getInstance().boolType()); // there is only 1 keyboard , hence static
	private Coordinate mouseLocation;
	Figure figure; 
	private Vector<Figure> figuresUnderMouse; 
	private Vector<Figure> figuresUnderMousePrev;
	volatile GC gc;
	private FigureExecutionEnvironment env;
	private SWTZOrderManager zorderManager;
	private Vector<Overlap> overlapFigures;
	private Vector<MouseOver> mouseOverFigures;
	private Vector<IHasZOrder> visibleSWTElements;
	private Vector<IHasZOrder> prevVisibleSWTElements;
	private int fontSizeOffset;
	private double lineWidthOffset;
	private Coordinate zoom;


	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env){
		this(parent,cfig,env,true,true);
	}
	
	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env,boolean hscroll,boolean vscroll) {
		super(parent, SWT_FLAGS);
		this.env = env;
		zorderManager = new SWTZOrderManager(this,this);
		mouseLocation = new Coordinate();
		overlapFigures = new Vector<Overlap>();
		mouseOverFigures = new Stack<MouseOver>();
		children = new Vector<FigureSWTApplet>();
		figuresUnderMouse = new Vector<Figure>();
		figuresUnderMousePrev = new Vector<Figure>();
		location = new Coordinate(0,0);
		zoom = new Coordinate(1,1);
		size = new BoundingBox();
		visibleSWTElements = new Vector<IHasZOrder>();
		prevVisibleSWTElements = new Vector<IHasZOrder>();
		lineWidthOffset = 0;
		fontSizeOffset = 0;
		
		if(parent.getParent() instanceof FigureSWTApplet){
			((FigureSWTApplet)parent.getParent()).registerChild(this);
		}

		Figure fig = FigureFactory.make(this, cfig, null, null);
		fig = new WhiteSpace( fig, new PropertyManager());
		this.figure = fig;
		System.out.printf("Creating %s\n", this);
		getHorizontalBar().addSelectionListener(this);
		getVerticalBar().addSelectionListener(this);
		getHorizontalBar().setVisible(false);
		getVerticalBar().setVisible(false);
		addControlListener(this);
		addMouseListener(this);
		addMouseMoveListener(this);
		addPaintListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
	}

	private void draw(GC swtGC) {
		drawPart(new Rectangle(location, size),new SWTGraphicsContext(swtGC,visibleSWTElements));
	}
	
	public void drawPart(Rectangle part,GraphicsContext gc){
		long startTime = System.nanoTime();
		gc.translate(-location.getX(), -location.getY());
		figure.drawPart(part,gc);

		for(Overlap f : overlapFigures){
			if(!(f.nonLocalFigure instanceof SWTWidgetFigure) && f.nonLocalFigure.overlapsWith(part)){
				f.nonLocalFigure.drawPart(part,gc);
			}
		}
		gc.dispose();
		if(FigureExecutionEnvironment.profile) System.out.printf("Drawing (part) took %f\n", (System.nanoTime() - startTime) / 1000000.0);
		makeOffscreenElementsInvisble();
	}
	
	public void layoutForce(){
		layoutFigures();
		for(FigureSWTApplet child : children){
			child.layoutForce();
		}
		redraw();
	}

	private void layoutFigures() {
		overlapFigures.clear();
		BoundingBox minViewSize = figure.getMinViewingSize();
		BoundingBox viewPort = 
			new BoundingBox(	Math.max(size.getWidth(), minViewSize.getWidth()),
								Math.max(size.getHeight(), minViewSize.getHeight()));
		for (boolean flip : Figure.BOTH_DIMENSIONS) {
			figure.takeDesiredWidth(flip,viewPort.getWidth(flip));
		}
		
		figure.layout();
		figure.globalLocation.set(0,0);
		figure.setLocationOfChildren();
		handleZOrder();	
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
				figuresUnderMouse.get(i).executeMouseMoveHandlers(env, ValueFactory.getInstance().bool(true), Properties.ON_MOUSEMOVE);
				int index = Collections.binarySearch(mouseOverFigures, figuresUnderMouse.get(i));
				if(index >= 0){
					mouseOverFigures.get(index).setMouseOver();
					mouseOverChanged = true;
				}
				i++;
			} else if(cmp > 0){
				figuresUnderMousePrev.get(j).executeMouseMoveHandlers(env, ValueFactory.getInstance().bool(false), Properties.ON_MOUSEMOVE);
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
			redraw();
		}
		env.endCallbackBatch(true);
	}
	
	public void registerMouseOver(MouseOver mouseOver) {
		mouseOverFigures.add(mouseOver);
	}
	
	
	private boolean handleKey(KeyEvent e,boolean down){
		env.beginCallbackBatch();
		IValue keySym = KeySymTranslate.toRascalKey(e, env.getRascalContext());
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e, keyboardModifierMap, env.getRascalContext());
		IBool keyDown = ValueFactory.getInstance().bool(down);
		boolean captured = false;
		for(int i = figuresUnderMouse.size()-1 ; i >= 0; i--){
			if(figuresUnderMouse.get(i).executeKeyHandlers(env, keySym, keyDown, keyboardModifierMap)){
				captured = true;
				break;
			}
		}
		env.endCallbackBatch();
		return captured;
	}
	
	private void adjustRunTimePropertyOffsets(){

		env.computeFigures();
		layoutFigures();
		setScrollBars();
		redraw();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		boolean captured = handleKey(e,true);
		if(!captured){
			if((e.stateMask & SWT.CONTROL) != 0){
				if(e.keyCode == '='){
					fontSizeOffset+=1;
					adjustRunTimePropertyOffsets();
				} else if(e.keyCode == '-'){
					fontSizeOffset-=1;
					adjustRunTimePropertyOffsets();
				}
			} else if((e.stateMask & SWT.SHIFT) != 0){
				if(e.keyCode == '='){
					lineWidthOffset+=0.5;
					adjustRunTimePropertyOffsets();
				} else if(e.keyCode == '-'){
					lineWidthOffset-=0.5;
					adjustRunTimePropertyOffsets();
				}
			} else {
				if(e.keyCode == '='){
					fontSizeOffset+=1;
					adjustRunTimePropertyOffsets();
				} else if(e.keyCode == '-'){
					fontSizeOffset-=1;
					adjustRunTimePropertyOffsets();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		handleKey(e,false);
	}
	
	private void resize(){
		Point s = getSize();
		size.set(s.x,s.y);
		boolean oldHScroll = getHorizontalBar().isVisible();
		boolean oldVScroll = getVerticalBar().isVisible();
		enableScrollBarsIfNeeded();
		scaleScrollBars();
		if(	!getHorizontalBar().isVisible() 
			|| !getVerticalBar().isVisible() 
			|| (getHorizontalBar().isVisible() && !oldHScroll)
			|| (getVerticalBar().isVisible() && !oldVScroll)){
			layoutFigures();
		}
		redraw();
		
	}
	
	private void setScrollBars(){
		enableScrollBarsIfNeeded();
		scaleScrollBars();
	}
	
	private void scaleScrollBars(){
		ForBothDimensions<ScrollBar> bars = new ForBothDimensions<ScrollBar>(getHorizontalBar(),getVerticalBar());
		for(boolean flip : Figure.BOTH_DIMENSIONS){
			ScrollBar bar = bars.getForX(flip);
			double diff = figure.getMinViewingSize().getWidth(flip) - size.getWidth(flip);
			location.setX(flip,Math.max(0,Math.min(diff,location.getX(flip))));
			if(!bar.isVisible()) {
				bar.setMaximum(0);
			}
			bar.setMinimum(0);
			bar.setMaximum(FigureMath.ceil( figure.getMinViewingSize().getWidth(flip)));
			bar.setIncrement(50);
			int selSize = FigureMath.floor(size.getWidth(flip));
			bar.setPageIncrement(selSize);
			bar.setThumb(selSize);
			
		}
	}

	private void enableScrollBarsIfNeeded() {
		BoundingBox minViewSize = figure.getMinViewingSize();
		if(size.getWidth() < minViewSize.getWidth()){
			getHorizontalBar().setVisible(true);
			if(size.getHeight() - getHorizontalBar().getSize().y < minViewSize.getHeight()){
				getVerticalBar().setVisible(true);
			} else {
				getVerticalBar().setVisible(false);
			}
		} else {
			if(size.getHeight()  < minViewSize.getHeight()){
				getVerticalBar().setVisible(true);
				if(size.getWidth() - getVerticalBar().getSize().x < minViewSize.getWidth()){
					getHorizontalBar().setVisible(true);
				} else {
					getHorizontalBar().setVisible(false);
				}
			} else {
				getHorizontalBar().setVisible(false);
				getVerticalBar().setVisible(false);
			}
		}
		org.eclipse.swt.graphics.Rectangle s = getClientArea();
		size.set(s.width,s.height);
	}
	

	public void makeOffscreenElementsInvisble(){
		Collections.sort(visibleSWTElements, IHasZOrderStableComparator.instance);
		int i = 0; int j = 0;
		while(i < prevVisibleSWTElements.size() ){
			IHasZOrder prev = prevVisibleSWTElements.get(i);
			if(j >= visibleSWTElements.size() || prev.getStableOrder() < visibleSWTElements.get(j).getStableOrder()){
				prev.setVisible(false);
				i++;
			} else if(prev.getStableOrder() > visibleSWTElements.get(j).getStableOrder()){
				j++;
			} else {
				i++;
				j++;
			}
		}
		Vector<IHasZOrder> tmp = prevVisibleSWTElements;
		prevVisibleSWTElements = visibleSWTElements;
		visibleSWTElements = tmp;
		visibleSWTElements.clear();
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
		draw(e.gc);
	}
	
	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		resize();
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
	
	public void addOverlapFigure(Overlap overlap){
		overlapFigures.add(overlap);
	}
	
	public String toString(){
		return "FigureSWTApplet" + ((WhiteSpace)figure).innerFig.toString();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		ForBothDimensions<ScrollBar> bars = new ForBothDimensions<ScrollBar>(getHorizontalBar(), getVerticalBar());
		for(boolean flip : Figure.BOTH_DIMENSIONS){
			ScrollBar bar = bars.getForX(flip);
			location.setX(flip,bar.getSelection());
		}
		redraw();
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public  Vector<IHasZOrder> getVisibleSWTElementsVector(){
		return visibleSWTElements;
	}

	@Override
	public Object adoptPropertyVal(Properties prop, Object val) {
		switch(prop){
		case FONT_SIZE : return Math.max(1,((Integer)val) + fontSizeOffset); 
		case LINE_WIDTH : return Math.max(1,((Double)val) + lineWidthOffset); 
		default : return val;
		}
	}

	@Override
	public IRunTimePropertyChanges getRunTimePropertyChanges() {
		return this;
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		e.data = 0;
		System.out.printf("Wheel! %s %s %s %s\n", e.button, e.count, e.data, e.stateMask);
		
	}


}
