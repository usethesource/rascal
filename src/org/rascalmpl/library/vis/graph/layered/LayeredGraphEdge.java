/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.graph.layered;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * A GraphEdge is created for each "edge" constructor that occurs in a graph.
 * 
 * @author paulk
 *
 */
public class LayeredGraphEdge extends Figure {
	private LayeredGraphNode from;
	private LayeredGraphNode to;
	Figure toArrow;
	Figure fromArrow;
	boolean reversed = false;
	private static boolean debug = false;
	private static boolean useSplines = true;
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, IPropertyManager properties, 
			IString fromName, IString toName,
			IConstructor toArrowCons, IConstructor fromArrowCons, 
			IEvaluatorContext ctx) {
		super(fpa, properties);
		this.from = G.getRegistered(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		to = G.getRegistered(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
		if(toArrowCons != null){
			 toArrow = FigureFactory.make(fpa, toArrowCons, properties, ctx);
		}
		if(fromArrowCons != null){
			 fromArrow = FigureFactory.make(fpa, fromArrowCons, properties, ctx);
		}
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue() +
				", arrows (to/from): " + toArrow + " " + fromArrow);
	}
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, IPropertyManager properties, 
			IString fromName, IString toName, Figure toArrow, Figure fromArrow, IEvaluatorContext ctx){
		
		super(fpa, properties);
		this.from = G.getRegistered(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		to = G.getRegistered(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		this.toArrow = toArrow;
		this.fromArrow = fromArrow;
	}
	
	LayeredGraphNode getFrom() {
		return reversed ? to : from;
	}
	
	LayeredGraphNode getFromOrg() {
		return from;
	}

	LayeredGraphNode getTo() {
		return reversed? from : to;
	}
	
	LayeredGraphNode getToOrg() {
		return to;
	}
	
	Figure getFromArrow(){
		return fromArrow;
	}
	
	Figure getToArrow(){
		return toArrow;
	}

	void reverse(){
		if(debug){
			System.err.println("*** Before reverse ***");
			from.print();
			to.print();
		}
		reversed = true;		
		from.delOut(to);
		to.delIn(from);
		from.addIn(to);
		to.addOut(from);
		if(debug){
			System.err.println("*** After reverse ***");
			from.print();
			to.print();
		}
	}
	
	boolean isReversed(){
		return reversed;
	}

	/*
	 * Primitives for drawing a multi-vertex edge
	 */
	
	float points[];
	int cp;
	float x1;
	float y1;
	
	private void beginCurve(float x, float y){
		if(useSplines){
			points = new float[20];
			cp = 0;
			addPointToCurve(x, y);
		} else{
			x1 = x; y1 = y;
		}
	}
	
	private void addPointToCurve(float x, float y){
		if(useSplines){
			if(cp == points.length){
				float points1[] = new float[2*points.length];
				for(int i = 0; i < cp; i++)
					points1[i] = points[i];
				points = points1;
			}
			points[cp++] = getLeft() + x;
			points[cp++] = getTop() + y;
		} else {
			fpa.line(getLeft()+ x1, getTop() + y1, getLeft() + x, getTop() + y);
			x1 = x; y1 = y;
		}
	}
	
	private void endCurve(float x, float y){
		if(useSplines){
			addPointToCurve(x, y);
			drawCurve();
		} else
			fpa.line(getLeft()+ x1, getTop() + y1, getLeft() + x, getTop() + y);	
	}
	/**
	 * Draw a bezier curve through a list of points. Inspired by a blog post "interpolating curves" by rj, which is in turn inspired by
	 * Keith Peter's "Foundations of Actionscript 3.0 Animation".
	 */
	
	private void drawCurve() {
		if (cp == 0)
			return;
		
		fpa.smooth();
		fpa.noFill();
		fpa.beginShape();
		float x1 = points[0];
		float y1 = points[1];
		float xc = 0.0f;
		float yc = 0.0f;
		float x2 = 0.0f;
		float y2 = 0.0f;
		fpa.vertex(x1, y1);
		for (int i = 2; i < cp - 4; i += 2) {
			xc = points[i];
			yc = points[i + 1];
			x2 = (xc + points[i + 2]) * 0.5f;
			y2 = (yc + points[i + 3]) * 0.5f;
			fpa.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
					(2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		xc = points[cp - 4];
		yc = points[cp - 3];
		x2 = points[cp - 2];
		y2 = points[cp - 1];
		fpa.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
				(2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
		fpa.endShape();
		points = null;
	}
	
	@Override
	public
	void draw(float left, float top) {
		applyProperties();
		
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 getTo().name + ": " + getTo().x + "," + getTo().y + ")");
		if(getFrom().isVirtual()){
			return;
		}
		if(getTo().isVirtual()){
			
			if(debug)System.err.println("Drawing a shape, inverted=" + reversed);
			LayeredGraphNode currentNode = getTo();
			
			float dx = currentNode.figX() - getFrom().figX();
			float dy = (currentNode.figY() - getFrom().figY());
			float imScale = 0.4f;
			float imX = getFrom().figX() + dx/2;
			float imY = getFrom().figY() + dy * imScale;
			if(debug)System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",	getFrom().figX(), getFrom().figY(),	currentNode.figX(), currentNode.figY(), imX, imY);
			
			if(getFromArrow() != null){
				if(debug)System.err.println("Drawing from arrow from " + getFrom().name);
				getFrom().figure.connectFrom(left, top, getFrom().figX(), getFrom().figY(), imX, imY, getFromArrow());
				beginCurve(imX, imY);
			} else {
				beginCurve(getFrom().figX(), getFrom().figY());
				addPointToCurve(imX, imY);
			}

			LayeredGraphNode nextNode = currentNode.out.get(0);
			
			addPointToCurve(currentNode.figX(), currentNode.figY());
		
			LayeredGraphNode prevNode = currentNode;
			currentNode =  nextNode;
			
			while(currentNode.isVirtual()){
				if(debug)System.err.println("Add vertex for " + currentNode.name);
				nextNode = currentNode.out.get(0);
				addPointToCurve(currentNode.figX(), currentNode.figY());
				prevNode = currentNode;
				currentNode = nextNode;
			}
		
			drawLastSegment(left, top, prevNode, currentNode);
			
		} else {
			if(debug)System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			if(getTo() == getFrom()){  // Drawing a self edge
				LayeredGraphNode node = getTo();
				float h = node.figure.height;
				float w = node.figure.width;
				float hgap = getHGapProperty();
				float vgap = getVGapProperty();
				
				beginCurve(left + node.figX(),                   top + node.figY()-h/3);
				addPointToCurve(left + node.figX(),              top + node.figY()-h/3);
				addPointToCurve(left + node.figX(),              top + node.figY()-h/3-vgap);
				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY()-h/3-vgap);
				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY()-h/3);
				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY());
				addPointToCurve(left + node.figX() + w/2,        top + node.figY());
				endCurve(left + node.figX() + w/2,        		 top + node.figY());
			}
			
			if(fromArrow != null || toArrow != null){
				if(reversed){
					
					if(toArrow != null)
						if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
						getFrom().figure.connectFrom(left, top, 
								getFrom().figX(), getFrom().figY(),
								getTo().figX(), getTo().figY(), 
								toArrow
					);
						
					if(fromArrow != null){
						if(debug)System.err.println("[reversed] Drawing to arrow to " + getToOrg().name);
						getTo().figure.connectFrom(left, top, 
								getTo().figX(), getTo().figY(),
								getFrom().figX(), getFrom().figY(), 
								fromArrow
						);
					}
				} else {
					if(debug)System.err.println("Drawing to arrow to " + getToOrg().name);
					getTo().figure.connectFrom(left, top, 
							getTo().figX(), getTo().figY(), 
							getFrom().figX(), getFrom().figY(),
							toArrow
					);
					if(fromArrow != null)
						if(debug)System.err.println("Drawing from arrow from " + getFrom().name);
					   getFrom().figure.connectFrom(left, top, 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							fromArrow
					);
			}
			} else {
				if(debug)System.err.println("Drawing lines without arrows");
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), 
						 left + getTo().figX(), top + getTo().figY());
			}
		}
	}
	
	private void drawLastSegment(float left, float top, LayeredGraphNode prevNode, LayeredGraphNode currentNode){
		float dx = currentNode.figX() - prevNode.figX();
		float dy = (currentNode.figY() - prevNode.figY());
		float imScale = 0.6f;
		float imX = prevNode.figX() + dx / 2;
		float imY = prevNode.figY() + dy * imScale;
		
		if(debug)
			System.err.printf("drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
					prevNode.figX(), prevNode.figY(),
					currentNode.figX(), currentNode.figY(), imX, imY);
	
		if(getToArrow() != null){
			if(debug)System.err.println("Has a to arrow");
			endCurve(imX, imY);
			currentNode.figure.connectFrom(getLeft(), getTop(), currentNode.figX(), currentNode.figY(), imX, imY, getToArrow());
		} else {
			addPointToCurve(imX, imY);
			endCurve(currentNode.figX(), currentNode.figY());
		}
	}

	@Override
	public
	void bbox() {
		// TODO Auto-generated method stub
	}
}
