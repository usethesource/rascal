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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.vis.figure;



import static org.rascalmpl.library.vis.properties.Properties.ASPECT_RATIO;
import static org.rascalmpl.library.vis.properties.Properties.FILL_COLOR;
import static org.rascalmpl.library.vis.properties.Properties.FONT;
import static org.rascalmpl.library.vis.properties.Properties.FONT_BOLD;
import static org.rascalmpl.library.vis.properties.Properties.FONT_COLOR;
import static org.rascalmpl.library.vis.properties.Properties.FONT_ITALIC;
import static org.rascalmpl.library.vis.properties.Properties.FONT_SIZE;
import static org.rascalmpl.library.vis.properties.Properties.HALIGN;
import static org.rascalmpl.library.vis.properties.Properties.HGROW;
import static org.rascalmpl.library.vis.properties.Properties.HRESIZABLE;
import static org.rascalmpl.library.vis.properties.Properties.HSHADOWPOS;
import static org.rascalmpl.library.vis.properties.Properties.LINE_COLOR;
import static org.rascalmpl.library.vis.properties.Properties.LINE_STYLE;
import static org.rascalmpl.library.vis.properties.Properties.LINE_WIDTH;
import static org.rascalmpl.library.vis.properties.Properties.SHADOW;
import static org.rascalmpl.library.vis.properties.Properties.SHADOW_COLOR;
import static org.rascalmpl.library.vis.properties.Properties.VALIGN;
import static org.rascalmpl.library.vis.properties.Properties.VGROW;
import static org.rascalmpl.library.vis.properties.Properties.VRESIZABLE;
import static org.rascalmpl.library.vis.properties.Properties.VSHADOWPOS;
import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.MIRROR;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SIZE;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;
import static org.rascalmpl.library.vis.util.vector.Dimension.X;
import static org.rascalmpl.library.vis.util.vector.Dimension.Y;

import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.FontStyle;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.TransformMatrix;
import org.rascalmpl.library.vis.util.vector.TwoDimensional;


/**
 * Figures are the foundation of Rascal visualization. They are based on a
 * bounding box. The bounding box defines the maximal dimensions
 * of the element.
 * 
 * @author paulk
 */

public abstract class Figure implements Comparable<Figure> {
	
	public static enum ResizeMode{
		RESIZE, ZOOM;
	}
	
	public static enum RecomputeStatus{
		DID_NOT_CHANGE,
		CHANGED;
	}
	public static final Figure[] childless = new Figure[0];
	public static final Type[] noTypes = new Type[0];
	public static final IValue[] noArgs = new IValue[0];
	
	public static int sequencer = 0; // to impose arbitrary ordering on figures
	@SuppressWarnings("unused")
	private static final boolean debug = false;
	
	public int sequenceNr;
	public RecomputeStatus status;
	public Figure[] children;

	public boolean mouseOver; // set externally, only set when relevant (see MouseAndKeyboardHandler)
	public PropertyManager prop;
	public BoundingBox minSize;
	public BoundingBox size;
	public Coordinate localLocation; // the location of the left, top corner of this figure relative to the parent location
	public Coordinate globalLocation; //
	public TwoDimensional<Boolean> resizable;

	public Figure(PropertyManager properties) {
		this.prop = properties;
		minSize = new BoundingBox();
		size = new BoundingBox();
		localLocation = new Coordinate();
		globalLocation = new Coordinate();
		resizable = new TwoDimensional<Boolean>(true, true);
		sequenceNr = sequencer;
		sequencer++;
	}
	
	public final void registerIds(IFigureConstructionEnv env,NameResolver resolver){
		resolver.register(this);
		registerMore(env,resolver);
		setChildren(env,resolver);
		for(Figure child : children){
			child.registerIds(env,resolver);
		}
	}
	
	void registerMore(IFigureConstructionEnv env, NameResolver resolver) {}

	public void setChildren(IFigureConstructionEnv env, NameResolver resolver) {}
	
	public final void registerConverts(NameResolver resolver){
		prop.registerMeasures(resolver);
		for(Figure child : children){
			child.registerConverts(resolver);
		}
	}
	
	/*
	 * There are three major phases in the processing of Figures:
	 * - init: compute minimal size
	 * - resize: compute actual size
	 * - draw: draw it.
	 */
	
	/* PHASE: init
	 * 
	 * Down: 
	 * - initialize (computefigure, register controls and other stuff) (*)
	 * - registerNames (defined by id property).
	 * Up:
	 * - register measures
	 * - compute min size (*)
	 * - finalize (*)
	 * 
	 * (*) : extension point for subclasses.
	 */
	
	/**
	 * 
	 * @param env		the callback environment for computing stuff when initializing
	 * @param resolver  the name resolver
	 * @param swtSeen 
	 * @param visible 
	 * @param overlaps  the current set of overlaps (figure not abiding to strict inclusive layout), add overlapping figures here
	 * @param zparent   the parent in the zorder tree, this is a partial view of the figure tree describing only the swt elements and hence their z order
	 * @return 
	 */
	public final boolean init(IFigureConstructionEnv env,NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible){
		resizable.set(prop.getBool(HRESIZABLE), prop.getBool(VRESIZABLE));
		initElem(env, mparent, swtSeen, visible, resolver);
		swtSeen = initChildren(env, resolver, mparent, swtSeen, visible);
		swtSeen = swtSeen || containsSWTElement();
		computeMinSize();
		adjustMinSize();
		finalize(true);
		return swtSeen;
	}

	public boolean containsSWTElement() {
		return false;
	}

	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		boolean swtSeenResult = false;
		for(int i = 0; i < children.length ; i++){
			boolean here =  children[i].init(env, resolver,mparent, swtSeen, visible);
			swtSeenResult = swtSeenResult || here;
		}
		return swtSeenResult;
	}

	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){}
	
	public abstract void computeMinSize() ;
	
	public void finalize(boolean needsRecompute){}
	
	/* 
	 * PHASE: resize
	 * 
	 * Down: 
	 * - resize *
	 * 		- Size has been set by parent.
	 * 		- Set size of all children and their locallocations (globalocations are set automatically in the step below).
	 * - register the zorder elements (SWT related) (*)
	 * - set the global location of children (transform locallocation to globallocation)
	 * Up:
	 * - onResizeUp (do some stuff if the clients wants to) (*)
	 * 
	 * (*) : extension point for subclasses.
	 */
	public final void resize(Rectangle view,TransformMatrix transform){
		adjustSizeAndLocation();
		resizeElement(view);
		resizeChildren(view, transform);
		onResizeUp();
	}

	public void resizeChildren(Rectangle view, TransformMatrix transform) {
		for(Figure child : children){
			for(Dimension d : HOR_VER){
				if(prop.get2DBool(d, MIRROR)){
					child.localLocation.set(d,size.get(d) - (child.localLocation.get(d) + child.size.get(d)));
				}
			}
			child.globalLocation.set(globalLocation);
			child.resize(view,transform);
		}
	}
	
	public abstract void resizeElement(Rectangle view) ;

	public void applyTransformation(TransformMatrix transform) {}
	public void reverseTransformation(TransformMatrix transform) {}
	public void onResizeUp() { }

	public void updateGlobalLocation(){
		
		for(Figure child : children){
			child.globalLocation.set(globalLocation);
			child.globalLocation.add(child.localLocation);
			child.updateGlobalLocation();
		}
	}
	
	/*
	 * PHASE: draw
	 * 
	 * Down:
	 * - beforeDraw (optional preprocessing) (*)
	 * - applyProperties
	 * - drawElement (draw the current figure)
	 * - drawChildren (draw its children).
	 * Up: there is no up phase.
	 * 
	 * (*) : extension point for subclasses.
	 *
	 */
	
	public final void draw(Coordinate zoom, GraphicsContext gc,Rectangle part, List<IHasSWTElement> visibleSWTElements) {
		// TODO: iets met rotate en transformaties
//		Coordinate offset = new Coordinate();
//		double oldZoomX, oldZoomY;
//		oldZoomX = zoom.getX();
//		oldZoomY = zoom.getY();
//		for(Dimension d : HOR_VER){
//			if(!prop.get2DBool(d, ZOOMABLE)){
//				offset.set(d, size.get(d) * (zoom.get(d) - 1.0) * prop.get2DReal(d, ALIGN) );
//				zoom.set(d, 1.0);
//			}
//		}
//		if(zoom.getX() != zoom.getY() && prop.isSet(ASPECT_RATIO)){
//			double minZoom = Math.min(zoom.getX(),zoom.getY());
//			zoom.set(minZoom,minZoom);
//		}
//		gc.translate(offset.getX(), offset.getY());
		
		beforeDraw(zoom);
		applyProperties(gc);
		drawElement(gc, visibleSWTElements);
		drawChildren(zoom, gc, part, visibleSWTElements);
		// gc.translate(-offset.getX(), -offset.getY());
		//zoom.set(oldZoomX, oldZoomY);
	}

	public void drawChildren(Coordinate zoom, GraphicsContext gc,
			Rectangle part, List<IHasSWTElement> visibleSWTElements) {
		for(Figure f : children){
			//if(f.overlapsWith(part)){
				Rectangle npart = f.isContainedIn(part) ? null : part;
				f.draw(zoom, gc, npart, visibleSWTElements);
			//}
		}
	}

	public void beforeDraw(Coordinate zoom) {} 

	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){}
	

	public boolean widthDependsOnHeight(){
		return false;
	}

	public Dimension getMajorDimension(){
		return X;
	}
	
	/**
	 * Give a figure the opportunity to remove allocated components, etc.
	 */
	public void destroy(IFigureConstructionEnv env) {
		
		for(Figure child : children){
			child.destroy(env);
		}
		destroyElement(env);
	}
	
	public void destroyElement(IFigureConstructionEnv env) { }
	
	public void hide(IFigureConstructionEnv env) {
		for(Figure child : children){
			child.hide(env);
		}
		hideElement(env);
	}
	
	public void hideElement(IFigureConstructionEnv env) {
		destroyElement(env);
	}

	public void getFiguresUnderMouse(Coordinate c,List<Figure> result){
		if(!mouseInside(c)){
			return;
		}
		for(Figure child : children){
			child.getFiguresUnderMouse(c, result); // TODO: overlap
		}
		if(handlesInput()){
			result.add(this);
		}
	}

	public boolean handlesInput(){
		return prop.hasHandlerProperties();
	}
	

	private void adjustMinSize() {
		if(prop.isSet(ASPECT_RATIO)){
			double ar = prop.getReal(ASPECT_RATIO);
			minSize.setMax(X, minSize.getY() * ar);
			minSize.setMax(Y, minSize.getX() / ar);
		}
		for(Dimension d : HOR_VER){
			minSize.setMax(d,prop.get2DReal(d, SIZE));
		}
	}

	public void adjustSizeAndLocation(){
		for(Dimension d : HOR_VER){
			if(!resizable.get(d)){
				localLocation.add(d, (size.get(d) - minSize.get(d)) * prop.get2DReal(d, ALIGN) );
				size.set(d,minSize.get(d));
			}
		}
		if(prop.isSet(ASPECT_RATIO)){
			double ar = prop.getReal(Properties.ASPECT_RATIO);
			double car = size.getX() / size.getY();
			if(car > ar) { // too wide
				double newWidth = size.getY() * ar;
				localLocation.add(X, (size.getX() - newWidth) * prop.getReal(HALIGN));
				size.setX(newWidth);
			} else if( car < ar){ // too tall
				double newHeight = size.getX() / ar;
				localLocation.add(Y, (size.getY() - newHeight) * prop.getReal(VALIGN));
				size.setY(newHeight);
			}
		}
		globalLocation.add(localLocation);
	}
	

	/*
	 * Compare two Figures using an arbitrary ordering
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Figure o) {
		return sequenceNr - o.sequenceNr;
	}

	public BoundingBox getMinViewingSize() {
		BoundingBox minViewSize = new BoundingBox();
		minViewSize.set(minSize.getX() / prop.getReal(HGROW), minSize.getY() /prop.getReal(VGROW) );
		return minViewSize;
	}
	
	public boolean overlapsWith(Rectangle r){
		return r == null || r.overlapsWith(globalLocation, size);
	}
	
	public boolean isContainedIn(Rectangle r){
		return  r == null || r.contains(globalLocation,size);
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(globalLocation, size);
	}
	
	public Rectangle getRectangleIncludingOuterLines(){
		double hlw = 0.5 * prop.getReal(LINE_WIDTH);
		return new Rectangle(globalLocation.getX() - hlw, globalLocation.getY() -hlw , size.getX() + 2*hlw, size.getY() + 2*hlw);
	}
	
	public boolean executeKeyHandlers(ICallbackEnv env,IValue keySym, boolean keyDown, IMap modifiers){
		Type[] types = {keySym.getType(),modifiers.getType()};
		IValue[] args = {keySym,modifiers};
		if(keyDown) return executeHandlerProperty(env,Properties.ON_KEY_DOWN,types,args);
		else  return executeHandlerProperty(env,Properties.ON_KEY_UP,types,args);
	}

	public void executeMouseMoveHandlers(ICallbackEnv env, boolean enter) {
		Type[] types = {};
		IValue[] args = {};
		if(enter) executeHandlerProperty(env,Properties.ON_MOUSE_OVER,types,args);
		else executeHandlerProperty(env,Properties.ON_MOUSE_OFF,types,args);
	}


	public boolean mouseInside(Coordinate c) {
		return c.getX() >= globalLocation.getX() && c.getX() <= globalLocation.getX() + size.getX()
				&& c.getY() >= globalLocation.getY() && c.getY() <= globalLocation.getY()+ size.getY();
	}

	public boolean executeOnClick(ICallbackEnv env, int button, IMap modifiers, boolean down) {
		Type[] types = {TypeFactory.getInstance().integerType(),modifiers.getType()};
		IValue[] args = {ValueFactory.getInstance().integer(button),modifiers};
		if(down) return executeHandlerProperty(env, Properties.ON_MOUSE_DOWN, types, args);
		else return executeHandlerProperty(env, Properties.ON_MOUSE_UP, types, args);
	}
	
	// returns if the event is captured (i.e. not propagated further)
	public boolean executeHandlerProperty(ICallbackEnv env, Properties property, Type[] types, IValue[] args){
		if(prop.isSet(property)){
			IValue v = prop.executeHandler(env, property, types, args);
			if(v instanceof IBool){
				return ((IBool)v).getValue();
			} else {
				return false;
			}
		} else {
			return false; // event not consumed, so not captured
		}
	}
	

	public void applyProperties(GraphicsContext gc) {
		gc.fill(prop.getColor(FILL_COLOR));
		gc.stroke(prop.getColor(LINE_COLOR));
		gc.strokeWeight(prop.getReal(LINE_WIDTH));
		gc.strokeStyle(prop.getStr(LINE_STYLE));
		boolean shadow = prop.getBool(SHADOW);
		gc.setShadow(shadow);
		if (shadow) {
			gc.setShadowColor(prop.getColor(SHADOW_COLOR));
			gc.setShadowLeft(prop.getReal(HSHADOWPOS));
			gc.setShadowTop(prop.getReal(VSHADOWPOS));
		}

		gc.setFont(prop.getStr(FONT), prop.getInt(FONT_SIZE), 
				FontStyle.getFontStyles(prop.getBool(FONT_BOLD), prop.getBool(FONT_ITALIC)));
		gc.font(prop.getColor(FONT_COLOR));
	}
	
	public double getTextAscent(){
		return SWTFontsAndColors.textAscent(
				prop.getStr(FONT),
				prop.getInt(FONT_SIZE),FontStyle.getFontStyles(prop.getBool(FONT_BOLD), prop.getBool(FONT_ITALIC)));
	}
	
	public double getTextDescent(){
		return SWTFontsAndColors.textDescent(
				prop.getStr(FONT),
				prop.getInt(FONT_SIZE),FontStyle.getFontStyles(prop.getBool(FONT_BOLD), prop.getBool(FONT_ITALIC)));
	}
	
	public double getTextHeight(){
		return getTextAscent() + getTextDescent();
	}
	
	public double getTextWidth(String s){
		return SWTFontsAndColors.textWidth(s, 
				prop.getStr(FONT),
				prop.getInt(FONT_SIZE),FontStyle.getFontStyles(prop.getBool(FONT_BOLD), prop.getBool(FONT_ITALIC)));
	}
	
	
	/**
	 * Draw an arrow from an external position (fromX, fromY) directed to the
	 * center (X,Y) of the current figure. The arrow is placed at At the
	 * intersection with the border of the current figure and it is
	 * appropriately rotated.
	 * @param X
	 *            X of center of current figure
	 * @param Y
	 *            Y of center of current figure
	 * @param fromX
	 *            X of center of figure from which connection is to be drawn
	 * @param fromY
	 *            Y of center of figure from which connection is to be drawn
	 * @param toArrow
	 *            the figure to be used as arrow (the upperside will act as arrow head)
	 */
	public void connectArrowFrom(double X, double Y, double fromX, double fromY,
			Figure toArrow, GraphicsContext gc, List<IHasSWTElement> visibleSWTElements ) {
	
		System.out.printf("Niet gespecialiseerd %s!!\n",this);
		for(Dimension d : HOR_VER){
			toArrow.minSize.set(d,toArrow.prop.get2DReal(d, SIZE));
		}
		toArrow.size.set(toArrow.minSize);
		toArrow.globalLocation.set(0,0);
		toArrow.localLocation.set(0,0);
		toArrow.resize(null, new TransformMatrix());

		if (fromX == X)
			fromX += 0.00001;
		double s = (fromY - Y) / (fromX -X);

		double rotd = Math.toDegrees(Math.atan(s));

		double IX;
		double IY;
		double h2 = minSize.getY() / 2;
		double w2 = minSize.getX() / 2;

		if ((-h2 <= s * w2) && (s * w2 <= h2)) {
			if (fromX > X) { 	// right
				IX = X + w2;
				IY = Y + s * w2;
				rotd += (fromY < Y ? -90 : -90);
			} else { 			// left
				IX = X - w2;
				IY = Y - s * w2;
				rotd += 90;
			}
		} else {
			if (fromY > Y) { 	// bottom
				IX = X + h2 / s;
				IY = Y + h2;
				rotd += (fromX < X ? 90 : -90);
			} else { 			// top
				IX = X - h2 / s;
				IY = Y - h2;
				rotd += (fromX < X ? 90 : -90);
			}
		}
		
		gc.pushMatrix();
		gc.translate(IX , IY);

		gc.rotate(rotd);
		gc.translate(-toArrow.size.getX()/2.0,0);
		toArrow.applyProperties(gc);
		toArrow.drawElement(gc,visibleSWTElements); 
		gc.popMatrix();
	}
	
	public Coordinate getGlobalCenter(){
		return new Coordinate(globalLocation.getX() + size.getX()/2, globalLocation.getY() + size.getY()/2);
	}
	
	public Coordinate getLocalCenter(){
		return new Coordinate(localLocation.getX() + size.getX()/2, localLocation.getY() + size.getY()/2);
	}
	
	
}
