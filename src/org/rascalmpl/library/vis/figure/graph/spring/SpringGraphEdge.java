///*******************************************************************************
// * Copyright (c) 2009-2011 CWI
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
//
// *   * Paul Klint - Paul.Klint@cwi.nl - CWI
//*******************************************************************************/
//package org.rascalmpl.library.vis.figure.graph.spring;
//
//import org.eclipse.imp.pdb.facts.IConstructor;
//import org.eclipse.imp.pdb.facts.IString;
//import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
//import org.rascalmpl.library.vis.figure.Figure;
//import org.rascalmpl.library.vis.figure.FigureFactory;
//import org.rascalmpl.library.vis.graphics.GraphicsContext;
//import org.rascalmpl.library.vis.properties.PropertyManager;
//import org.rascalmpl.library.vis.swt.ICallbackEnv;
//import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
//import org.rascalmpl.library.vis.util.FigureMath;
//import org.rascalmpl.library.vis.util.NameResolver;
//
//
///**
// * A GraphEdge is created for each "edge" constructor that occurs in a graph.
// * 
// * @author paulk
// *
// */
//public class SpringGraphEdge extends Figure {
//	private SpringGraphNode from;
//	private SpringGraphNode to;
//	private Figure toArrow;
//	private Figure fromArrow;
//	private boolean inverted = false;
//	private static boolean debug = true;
//	
//	public SpringGraphEdge(SpringGraph G, IFigureConstructionEnv fpa, PropertyManager properties, 
//						IString fromName, IString toName, 
//						IConstructor toArrowCons, IConstructor fromArrowCons) {
//		super( properties);
//		this.from = G.getRegistered(fromName.getValue());
//		
//		if(getFrom() == null){
//			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
//					fromName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
//		}
//		to = G.getRegistered(toName.getValue());
//		if(to == null){
//			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
//		}
//		if(toArrowCons != null){
//			 toArrow = FigureFactory.make(fpa, toArrowCons, properties, null);
//		}
//		if(fromArrowCons != null){
//			 fromArrow = FigureFactory.make(fpa, fromArrowCons, properties, null);
//		}
//		
//		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
//	}
//	
//
//	SpringGraphNode getFrom() {
//		return inverted ? to : from;
//	}
//
//	SpringGraphNode getTo() {
//		return inverted? from : to;
//	}
//
//	void invert(){
//		inverted = true;
//	}
//	
//	boolean isInverted(){
//		return inverted;
//	}
//	
//	void relax(SpringGraph G){
//		double vx = to.xdistance(getFrom());
//		double vy = to.ydistance(getFrom());
//		
//		double dlen = FigureMath.mag(vx, vy);
//		//dlen = (dlen == 0) ? .0001f : dlen;
//
//		//double attract = G.attract(dlen);
//		double attract = dlen * dlen / G.springConstant;
//		double dx = (vx / dlen) * attract;
//		double dy = (vy / dlen) * attract;
//
//		to.dispx += -dx;
//		to.dispy += -dy;
//		getFrom().dispx += dx;
//		getFrom().dispy += dy;
//
//		if(debug)System.err.printf("edge: %s -> %s: dx=%f, dy=%f\n", getFrom().name, to.name, dx, dy);
//}
//
//	@Override
//	public
//	void draw(GraphicsContext gc) {
//		applyProperties(gc);
//		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().getX() + "," + getFrom().getY() + ") -> (" + 
//				to.name + ": " + to.getX() + "," + to.getY() + ")");
//
//		gc.line(getLeft() + getFrom().figX(), getTop() + getFrom().figY(), 
//				getLeft() + getTo().figX(), getTop() + getTo().figY());
//		if(toArrow != null){
//			getTo().figure.connectArrowFrom(getLeft(), getTop(), 
//					getTo().figX(), getTo().figY(), 
//					getFrom().figX(), getFrom().figY(),
//					toArrow,gc
//			);
//
//			if(fromArrow != null)
//				getFrom().figure.connectArrowFrom(getLeft(), getTop(), 
//						getFrom().figX(), getFrom().figY(), 
//						getTo().figX(), getTo().figY(),
//						fromArrow,gc
//				);
//		}	
//	}
//
//	@Override
//	public
//	void bbox() {
//		setNonResizable();
//		super.bbox();
//		
//	}
//	
//	public void computeFiguresAndProperties(ICallbackEnv env){
//		super.computeFiguresAndProperties(env);
//		if(fromArrow!=null)fromArrow.computeFiguresAndProperties(env);
//		if(toArrow!=null)toArrow.computeFiguresAndProperties(env);
//	}
//	
//
//	public void registerNames(NameResolver resolver){
//		super.registerNames(resolver);
//		if(fromArrow!=null)fromArrow.registerNames(resolver);
//		if(toArrow!=null)toArrow.registerNames(resolver);
//	}
//
//
//	@Override
//	public void layout() {
//		size.set(minSize);
//		if(fromArrow!=null) {
//			fromArrow.setToMinSize();
//			fromArrow.layout();
//		}
//		if(toArrow!=null) {
//			toArrow.setToMinSize();
//			toArrow.layout();
//		}
//	}
//
//
//
//}
