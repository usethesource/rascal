/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt.applet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.rascalmpl.library.vis.KeySym;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.swt.FigureExecutionEnvironment;
import org.rascalmpl.library.vis.util.BogusList;
import org.rascalmpl.library.vis.util.KeySymTranslate;
import org.rascalmpl.library.vis.util.Util;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class InputHandler implements MouseListener,MouseMoveListener, MouseTrackListener, KeyListener, IFigureChangedListener{
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static final BogusList<Figure> bogusFigureList = (BogusList<Figure>)BogusList.instance;
	private static IMap keyboardModifierMap = 
			ValueFactoryFactory.getValueFactory().map(KeySym.KeyModifier, TypeFactory.getInstance().boolType()); // there is only 1 keyboard , hence static
	private List<Figure> figuresUnderMouse;  // the figures under mouse from front to back
	private List<Figure> figuresUnderMouseSorted; // figures under mouse sorted in an arbitrary stable ordering (stable as in the order does not change)
	private List<Figure> figuresUnderMouseSortedPrev; // the figures under mouse sorted on the previous mouse location
	private List<Figure> newUnderMouse; 
	private List<Figure> noLongerUnderMouse;
	private Figure figure;
	private Coordinate mouseLocation; // location on figure, not in viewport
	private FigureExecutionEnvironment env;
	private List<Overlap> overlapFigures; // this is silently mutated by the FigureSWTApplet
	private FigureSWTApplet parent;
	
	public InputHandler(FigureSWTApplet parent, List<Overlap> overlapFigures){
		figuresUnderMouse = new ArrayList<Figure>();
		figuresUnderMouseSorted = new ArrayList<Figure>();
		figuresUnderMouseSortedPrev = new ArrayList<Figure>();
		newUnderMouse = new ArrayList<Figure>();
		noLongerUnderMouse = new ArrayList<Figure>();
		
		mouseLocation = new Coordinate(-10,-10);
		this.figure = parent.getFigure();
		this.env = parent.getExectutionEnv();
		this.overlapFigures = overlapFigures;
		this.parent = parent;
		
	}
	
	public void notifyFigureChanged(){
		handleMouseMove();
	}
	
	private void setFiguresUnderMouse(){

		figuresUnderMouse.clear();
		
		figure.getFiguresUnderMouse(mouseLocation, figuresUnderMouse);
		for(Overlap f : overlapFigures){
			f.over.getFiguresUnderMouse(mouseLocation, figuresUnderMouse);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void handleMouseOvers(){
		//new Exception().printStackTrace();
		List<Figure> swp = figuresUnderMouseSortedPrev;
		figuresUnderMouseSortedPrev = figuresUnderMouseSorted;
		figuresUnderMouseSorted = swp;
		figuresUnderMouseSorted.clear();
		figuresUnderMouseSorted.addAll(figuresUnderMouse);
		Collections.sort(figuresUnderMouseSorted);
		newUnderMouse.clear();
		noLongerUnderMouse.clear();
		// compute the added and removed elements in a fast way
		Util.diffSorted(figuresUnderMouseSortedPrev, figuresUnderMouseSorted, noLongerUnderMouse, BogusList.instance, newUnderMouse);
		/*
		if(!noLongerUnderMouse.isEmpty() || !newUnderMouse.isEmpty()){
			System.out.printf("Now under mouse:\n");
			 for(Figure fig : figuresUnderMouseSorted){
				System.out.printf("%s \n",fig);
			}
			 
			 
			System.out.printf("Prev under mouse:\n");
			for(Figure fig : figuresUnderMouseSortedPrev){
				System.out.printf("%s \n",fig);
			}
		}
		
		
		if(!noLongerUnderMouse.isEmpty()){
			System.out.printf("No longer under mouse:\n");
			for(Figure fig : noLongerUnderMouse){
				System.out.printf("%s %s %s \n",fig,fig.location, fig.size);
			}
		}
		if(!newUnderMouse.isEmpty()){
			System.out.printf("New under mouse:\n");
			for(Figure fig : newUnderMouse){
				System.out.printf("%s %s %s\n",fig,fig.location, fig.size);
			}
		}
		*/
		
		env.beginCallbackBatch();
		for(Figure fig : noLongerUnderMouse){
			fig.mouseOver = false;
			fig.executeMouseMoveHandlers(env, false);
		}
		for(Figure fig : newUnderMouse){
			fig.mouseOver = true;
			fig.executeMouseMoveHandlers(env, true);
		}
		env.endCallbackBatch(false);
	
	}
	
	private void handleMouseMove(){
		//System.out.printf("Checking for mouseover for transloc %s\n", mouseLocation);
		setFiguresUnderMouse();
		handleMouseOvers();
	}
	

	private void handleKey(KeyEvent e,boolean down){
		boolean captured = false;
		env.beginCallbackBatch();
		IValue keySym = KeySymTranslate.toRascalKey(e, env.getRascalContext());
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e.stateMask, keyboardModifierMap, env.getRascalContext());
		for(Figure fig : figuresUnderMouse){
			if(fig.executeKeyHandlers(env, keySym, down, keyboardModifierMap)){
				captured = true;
				break;
			}
		}
		if(!captured){
			parent.handleNonCapturedKeyPress(e);
		}
		env.endCallbackBatch();
		
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		handleKey(e, true);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		handleKey(e,false);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		mouseLocation.set(e.x,e.y);
		
		parent.translateFromViewPortToFigure(mouseLocation);
		handleMouseMove();
		
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		env.beginCallbackBatch();
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e.stateMask, keyboardModifierMap, env.getRascalContext());
		for(Figure fig : figuresUnderMouse){
			if(fig.executeOnClick(env,e.button,keyboardModifierMap,true)){
				break;
			}
		}
		env.endCallbackBatch();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		env.beginCallbackBatch();
		keyboardModifierMap = KeySymTranslate.toRascalModifiers(e.stateMask, keyboardModifierMap, env.getRascalContext());
		for(Figure fig : figuresUnderMouse){
			if(fig.executeOnClick(env,e.button,keyboardModifierMap,false)){
				break;
			}
		}
		env.endCallbackBatch();
	}
	
	@Override
	public void mouseEnter(MouseEvent e) {
		parent.forceFocus();
	}

	@Override
	public void mouseExit(MouseEvent e) {
		//System.out.printf("Mouse exit %d %d | %d %d\n",e.x,e.y,parent.getSize().x,parent.getSize().y);
		mouseLocation.set(-100,-100); // not on figure!
		handleMouseMove();
	}

	@Override
	public void mouseHover(MouseEvent e) {}

	public void dispose() {
		
	}
}
