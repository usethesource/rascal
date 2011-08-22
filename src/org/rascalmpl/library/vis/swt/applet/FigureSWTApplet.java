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
package org.rascalmpl.library.vis.swt.applet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.WhiteSpace;
import org.rascalmpl.library.vis.properties.IRunTimePropertyChanges;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.FigureExecutionEnvironment;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Mutable;
import org.rascalmpl.library.vis.util.vector.Coordinate;

public class FigureSWTApplet extends Composite 
	implements IFigureConstructionEnv, DisposeListener{


	private static final int SWT_FLAGS = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.NO_BACKGROUND | SWT.NO_MERGE_PAINTS ;
	private static boolean debug = false;
	private List<FigureSWTApplet> children;
	private Figure figure; 
	private FigureExecutionEnvironment env;
	private InputHandler inputHandler;
	private ViewPortHandler viewPortHandler;
	private RunTimePropertyAdjuster runTimePropertyAdjuster;
	private List<Overlap> overlapFigures; // shared by inputhandler and viewport handler
	private boolean redrawRequested;
	private boolean busy;
	private int computeClock = -1;
	
	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env){
		this(parent,cfig,env,true,true);
	}
	
	public FigureSWTApplet(Composite parent, IConstructor cfig, FigureExecutionEnvironment env,boolean hscroll,boolean vscroll) {
		super(parent, SWT_FLAGS);
		this.env = env;
		
		children = new ArrayList<FigureSWTApplet>();
		overlapFigures = new LinkedList<Overlap>();
	
		Figure fig = FigureFactory.make(this, cfig, null, null);
		if(!fig.widthDependsOnHeight()){
			fig = new WhiteSpace( fig, new PropertyManager());
		}
		this.figure = fig;
		inputHandler = new InputHandler(this, overlapFigures);
		viewPortHandler = new ViewPortHandler(this,overlapFigures);
		runTimePropertyAdjuster = new RunTimePropertyAdjuster(this);
		addPaintListener(viewPortHandler);
		addControlListener(viewPortHandler);
		getHorizontalBar().addSelectionListener(viewPortHandler);
		getVerticalBar().addSelectionListener(viewPortHandler);
		addMouseListener(inputHandler);
		addMouseMoveListener(inputHandler);
		addMouseTrackListener(inputHandler);
		addKeyListener(inputHandler);
	}
	
	public boolean isUpToDate(){
		return computeClock == env.getComputeClock();
	}
	
	public void triggerRecompute(){
		redrawRequested = false;
		busy = true;
		viewPortHandler.beforeInitialise();
		overlapFigures.clear();
		for(FigureSWTApplet child : children){
			child.triggerRecompute();
		}
		figure.initializePhase(this, env.getNameResolver(), null, new Mutable<Boolean>(false));
		viewPortHandler.notifyFigureChanged();  
	}

	public void notifyLayoutChanged(){
		inputHandler.notifyFigureChanged();
		runTimePropertyAdjuster.notifyFigureChanged();
		computeClock = env.getComputeClock();
		busy = false;
		if(redrawRequested){
			redraw();
		}
	}

	public void requestRedraw() {
		if(busy){
			redrawRequested = true;
		} else {
			redraw();
		}
	}
	
	public void handleNonCapturedKeyPress(KeyEvent e){
		runTimePropertyAdjuster.handleKeyPress(e);
	}
	
	public FigureExecutionEnvironment getExectutionEnv(){ 
		return env;
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		figure.destroy(this);
		viewPortHandler.dispose();
		inputHandler.dispose();
		runTimePropertyAdjuster.dispose();
		for(FigureSWTApplet child : children){
			child.dispose();
		}
	}
	
	public void registerChild(FigureSWTApplet child){
		System.out.printf("registering child %s\n", child);
		children.add(child);
	}

	@Override
	public FigureSWTApplet getSWTParent() {
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
		return "FigureSWTApplet" + figure.toString();
	}

	

	@Override
	public IRunTimePropertyChanges getRunTimePropertyChanges() {
		return runTimePropertyAdjuster;
	}

	public void translateFromViewPortToFigure(Coordinate screenLocation) {
		viewPortHandler.translateFromViewPortToFigure(screenLocation);
	}
	
	public InputHandler getInputHandler(){
		return inputHandler;
	}



	public void mouseMove(MouseEvent e) {
		inputHandler.mouseMove(e);
	
	}

	@Override
	public void registerOverlap(Overlap o) {
		overlapFigures.add(o);
		
	}

	@Override
	public void unregisterOverlap(Overlap o) {
		overlapFigures.remove(o);
	}

	@Override
	public void addSWTElement(Control c) {
		viewPortHandler.addSWTElement(c);
		
	}

	@Override
	public void addAboveSWTElement(Figure fig) {
		viewPortHandler.addAboveSWTElement(fig);
		
	}

	public Image getFigureImage() {
		return viewPortHandler.getFigureImage();
	}



	/*
	private void zoom(double factorX, double factorY){
		BoundingBox zoomFactors = new BoundingBox(factorX, factorY);
		BoundingBox oldFigureSize = new BoundingBox(figureSize);
		zoom.setWidth(zoom.getWidth() * factorX);
		zoom.setHeight(zoom.getHeight() * factorY);
		resize();
		for(boolean flip : Figure.BOTH_DIMENSIONS){
			if(zoomFactors.getWidth(flip) != 1.0  && oldFigureSize.getWidth(flip) == figureSize.getWidth(flip)){
				zoom.setWidth(flip,zoom.getWidth(flip) / zoomFactors.getWidth(flip));
			}
		}
		if(factorX != 1.0 && factorY != 1.0 &&
				(oldFigureSize.getWidth() == figureSize.getWidth() 
						|| oldFigureSize.getHeight() == figureSize.getHeight())){
			for(boolean flip : Figure.BOTH_DIMENSIONS){
					zoom.setWidth(flip,zoom.getWidth(flip) / factorX);
			}
		} else {
			for(boolean flip : Figure.BOTH_DIMENSIONS){
				if(zoomFactors.getWidth(flip) != 1.0  && oldFigureSize.getWidth(flip) == figureSize.getWidth(flip)){
					zoom.setWidth(flip,zoom.getWidth(flip) / zoomFactors.getWidth(flip));
				}
			}
		}
		for(boolean flip : Figure.BOTH_DIMENSIONS){
			double diff = figureSize.getWidth(flip) - viewPortSize.getWidth(flip);
			viewPortLocation.setX(flip,(viewPortLocation.getX(flip) + mouseLocation.getX(flip)) *  zoomFactors.getWidth(flip) - mouseLocation.getX(flip));
			viewPortLocation.setX(flip,Math.max(0,Math.min(diff,viewPortLocation.getX(flip))));
		}
		
		if(!oldFigureSize.isEq(figureSize)){
			redraw();
		}
	} */

	
	/*
	private void scrollOnMouseDrag() {
		double factor = 1.0;
		if(!mouseDown){
			long newTime = System.nanoTime();
			long diff = newTime - lastTime;
			factor = (double)diff / 10000000;
			lastTime = newTime;
		}
		for(boolean flip : Figure.BOTH_DIMENSIONS){
			double diff = figureSize.getWidth(flip) - viewPortSize.getWidth(flip);
			viewPortLocation.addX(flip, -(mouseSpeed.getX(flip) * factor));
			
			if(viewPortLocation.getX(flip) > diff || viewPortLocation.getX(flip) < 0){
				mouseSpeed.setX(flip,-mouseSpeed.getX(flip));
			}
			viewPortLocation.capXBelow(flip, diff);
			
			viewPortLocation.capXAbove(flip,0);
		}
		getHorizontalBar().setSelection((int)viewPortLocation.getX());
		getVerticalBar().setSelection((int)viewPortLocation.getY());
		redraw();
	}
	*/
}
