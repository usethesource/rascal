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

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;

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
	Figure label;
	double labelX;
	double labelMinX;
	double labelMaxX;
	double labelMinY;
	double labelMaxY;
	double labelY;
	boolean reversed = false;
	private static boolean debug = true;
	private static boolean useSplines = true;
	
	public LayeredGraphEdge(LayeredGraph G, IFigureApplet fpa, PropertyManager properties, 
			IString fromName, IString toName, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.from = G.getRegisteredNodeId(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		toArrow = super.getToArrow();
		
		fromArrow = super.getFromArrow(); 
		
		label = getLabel();
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue() +
				", arrows (to/from): " + toArrow + " " + fromArrow + " " + label);
	}
	
	public LayeredGraphEdge(LayeredGraph G, IFigureApplet fpa, PropertyManager properties, 
			IString fromName, IString toName, Figure toArrow, Figure fromArrow, IEvaluatorContext ctx){
		
		super(fpa, properties);
		this.from = G.getRegisteredNodeId(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
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
	
	public Figure getFromArrow(){
		return fromArrow;
	}
	
	public Figure getToArrow(){
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
	
	double points[];
	int cp;
	double x1;
	double y1;
	
	private void beginCurve(double x, double y){
		if(useSplines){
			points = new double[20];
			cp = 0;
			addPointToCurve(x, y);
		} else {
			x1 = x; y1 = y;
		}
	}
	
	private void addPointToCurve(double x, double y){
		if(useSplines){
			if(cp == points.length){
				double points1[] = new double[2*points.length];
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
	
	private void endCurve(double x, double y){
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
		
		fpa.noFill();
		fpa.beginShape();
		double x1 = points[0];
		double y1 = points[1];
		double xc = 0.0f;
		double yc = 0.0f;
		double x2 = 0.0f;
		double y2 = 0.0f;
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
	void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		applyProperties();
		
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 getTo().name + ": " + getTo().x + "," + getTo().y + ")");
		if(getFrom().isVirtual()){
			return;
		}
		if(getTo().isVirtual()){
			
			if(debug)System.err.println("Drawing a shape, inverted=" + reversed);
			LayeredGraphNode currentNode = getTo();
			
			double dx = currentNode.figX() - getFrom().figX();
			double dy = (currentNode.figY() - getFrom().figY());
			double imScale = 0.4f;
			double imX = getFrom().figX() + dx/2;
			double imY = getFrom().figY() + dy * imScale;
			
			if(debug)System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",	getFrom().figX(), getFrom().figY(),	currentNode.figX(), currentNode.figY(), imX, imY);
			
			beginCurve(getFrom().figX(), getFrom().figY());
			addPointToCurve(imX, imY);

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
		
			drawLastSegment(left, top, imX, imY, prevNode, currentNode);
			
		} else {
			if(debug)System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			if(getTo() == getFrom()){  // Drawing a self edge
				LayeredGraphNode node = getTo();
				double h = node.figure.minSize.getHeight();
				double w = node.figure.minSize.getWidth();
				double hgap = getHGapProperty();
				double vgap = getVGapProperty();
				
//				beginCurve(left + node.figX(),                   top + node.figY()-h/3);
//				addPointToCurve(left + node.figX(),              top + node.figY()-h/3);
//				addPointToCurve(left + node.figX(),              top + node.figY()-h/3-vgap);
//				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY()-h/3-vgap);
//				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY()-h/3);
//				addPointToCurve(left + node.figX() + w/2 + hgap, top + node.figY());
//				addPointToCurve(left + node.figX() + w/2,        top + node.figY());
//				endCurve(left + node.figX() + w/2,        		 top + node.figY());
				
				beginCurve(left + node.figX(),                   top + node.figY()-h/2);
				addPointToCurve(left + node.figX()+w/4,          top + node.figY()-(h/2+vgap/4));
				addPointToCurve(left + node.figX()+w/2,          top + node.figY()-(h/2+vgap/2));
				addPointToCurve(left + node.figX(),              top + node.figY()-(h+vgap));
				addPointToCurve(left + node.figX(),              top + node.figY()-(h/2+vgap/4));
				endCurve(left + node.figX(),                     top + node.figY()-h/2);
				
				if(toArrow != null){
					if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
					getTo().figure.connectArrowFrom(left, top, 
							getTo().figX(), getTo().figY(),
							node.figX(),  node.figY()-(h/2+vgap/4),
							toArrow
					);
					return;
				}
			} else {
			
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), 
					 left + getTo().figX(), top + getTo().figY());
			}
			
			if(fromArrow != null || toArrow != null){
				if(reversed){
					
					if(toArrow != null){
						if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
						getFrom().figure.connectArrowFrom(left, top, 
								getFrom().figX(), getFrom().figY(),
								getTo().figX(), getTo().figY(), 
								toArrow
						);
					}
						
					if(fromArrow != null){
						if(debug)System.err.println("[reversed] Drawing to arrow to " + getToOrg().name);
						getTo().figure.connectArrowFrom(left, top, 
								getTo().figX(), getTo().figY(),
								getFrom().figX(), getFrom().figY(), 
								fromArrow
						);
					}
				} else {
					if(debug)System.err.println("Drawing to arrow to " + getTo().name);
					if(toArrow != null){
						getTo().figure.connectArrowFrom(left, top, 
							getTo().figX(), getTo().figY(), 
							getFrom().figX(), getFrom().figY(),
							toArrow
						);
					}
					if(fromArrow != null){
						if(debug)System.err.println("Drawing from arrow from " + getFrom().name);
					    getFrom().figure.connectArrowFrom(left, top, 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							fromArrow
					    );
					}
				}
			}
			if(label != null){
				label.draw(left + labelX - label.minSize.getWidth()/2, top + labelY - label.minSize.getHeight()/2);
			}
		}
	}
	
	private void drawLastSegment(double left, double top, double startImX, double startImY, LayeredGraphNode prevNode, LayeredGraphNode currentNode){
		double dx = currentNode.figX() - prevNode.figX();
		double dy = (currentNode.figY() - prevNode.figY());
		double imScale = 0.6f;
		double imX = prevNode.figX() + dx / 2;
		double imY = prevNode.figY() + dy * imScale;
		
		if(debug)
			System.err.printf("drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
					prevNode.figX(), prevNode.figY(),
					currentNode.figX(), currentNode.figY(), imX, imY);
		
		addPointToCurve(imX, imY);
		endCurve(currentNode.figX(), currentNode.figY());
		
		// Finally draw the arrows on both sides of the edge
		
		if(getFromArrow() != null){
			getFrom().figure.connectArrowFrom(left, top, getFrom().figX(), getFrom().figY(), startImX, startImY, getFromArrow());
		}
		if(getToArrow() != null){
			if(debug)System.err.println("Has a to arrow");
			currentNode.figure.connectArrowFrom(left, top, currentNode.figX(), currentNode.figY(), imX, imY, getToArrow());
		}
	}
	
	public void setLabelCoordinates(){
		if(label != null){
			labelX = getFrom().x + (getTo().x - getFrom().x)/2;
			labelY = getFrom().y + (getTo().y - getFrom().y)/2;
			
			labelMinX = labelX - 1.5f * label.minSize.getWidth();
			labelMaxX = labelX + 1.5f * label.minSize.getWidth();
			
			labelMinY = labelY - 1.5f * label.minSize.getHeight();
			labelMaxY = labelY + 1.5f * label.minSize.getHeight();
			
			System.err.printf("edge %s->%s: labelX=%f, labelY=%f\n", from.name, to.name, labelX, labelY);
		}
	}
	
	public void shiftLabelCoordinates(double dx, double dy){
		if(label != null){
			System.err.printf("shiftLabelCoordinates %s-> %s: %f, %f\n", from.name, to.name, dx, dy);
			labelX = Math.min(Math.max(labelMinX, labelX + dx), labelMaxX);
			labelY = Math.min(Math.max(labelMinY, labelY + dy), labelMaxY);
		}
	}
	
	public void reduceOverlap(LayeredGraphEdge other){
		double ax1 = labelX - label.minSize.getWidth()/2;
		double ax2 = labelX + label.minSize.getWidth()/2;
		double bx1 = other.labelX - other.label.minSize.getWidth()/2;
		double bx2 = other.labelX + other.label.minSize.getWidth()/2;
		double distX;
		
		if(ax1 < bx1){
			distX = bx1 - ax2;
		} else
			distX = ax1 - bx2;
		
		double ay1 = labelY - label.minSize.getHeight()/2;
		double ay2 = labelY + label.minSize.getHeight()/2;
		double by1 = other.labelY - other.label.minSize.getHeight()/2;
		double by2 = other.labelY + other.label.minSize.getHeight()/2;
		
		double distY;
		
		if(ay1 < by1){
			distY = by1 - ay2 - 2;
		} else
			distY = ay1 - by2 - 2;
		
		System.err.printf("reduceOverlap %s->%s, %s->%s: distX=%f, distY=%f\n", from.name, to.name, other.from.name, other.to.name, distX, distY);
		if(distX > 0)
			return;
		
		distX = distX > 0 ? 0 : -distX;
		distY = distY > 0 ? 0 : -distY;
		shiftLabelCoordinates(-distX/2, -distY/2);
		other.shiftLabelCoordinates(distX/2, distY/2);
	}

	@Override
	public
	void bbox() {
		if(fromArrow != null)
			fromArrow.bbox();
		if(toArrow != null)
			toArrow.bbox();
		if(label != null)
			label.bbox();
		setNonResizable();
		super.bbox();
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		return (fromArrow!=null && fromArrow.getFiguresUnderMouse(c,result)) ||
				(label!=null && label.getFiguresUnderMouse(c,result)) ||
				(toArrow!=null && toArrow.getFiguresUnderMouse(c,result));
	}
	
	public void computeFiguresAndProperties(){
		super.computeFiguresAndProperties();
		if(fromArrow!=null)fromArrow.computeFiguresAndProperties();
		if(toArrow!=null)toArrow.computeFiguresAndProperties();
		if(label!=null)label.computeFiguresAndProperties();
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		if(fromArrow!=null)fromArrow.registerNames(resolver);
		if(toArrow!=null)toArrow.registerNames(resolver);
		if(label!=null)label.registerNames(resolver);
	}

	@Override
	public void layout() {
		size.set(minSize);
		if(fromArrow != null) {
			fromArrow.setToMinSize();
			fromArrow.layout();
		}
		if(toArrow != null) {
			toArrow.setToMinSize();
			toArrow.layout();
		}
		if(label != null){
			label.setToMinSize();
			label.layout();
		}
		
	}
}
