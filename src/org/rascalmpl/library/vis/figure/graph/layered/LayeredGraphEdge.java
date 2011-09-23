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
package org.rascalmpl.library.vis.figure.graph.layered;


import static org.rascalmpl.library.vis.properties.Properties.FROM_ARROW;
import static org.rascalmpl.library.vis.properties.Properties.HGAP;
import static org.rascalmpl.library.vis.properties.Properties.LABEL;
import static org.rascalmpl.library.vis.properties.Properties.TO_ARROW;
import static org.rascalmpl.library.vis.properties.Properties.VGAP;

import java.util.List;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;
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
	
	public LayeredGraphEdge(LayeredGraph G, IFigureConstructionEnv fpa, PropertyManager properties, 
			IString fromName, IString toName) {
		super(properties);
		this.from = G.getRegisteredNodeId(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		toArrow = prop.getFig(TO_ARROW);
		
		fromArrow = prop.getFig(FROM_ARROW);
		
		children = new Figure[2];
		children[0] = toArrow;
		children[1] = fromArrow;
		
		label = prop.getFig(LABEL);
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue() +
				", arrows (to/from): " + toArrow + " " + fromArrow + " " + label);
	}
	
	public LayeredGraphEdge(LayeredGraph G, IFigureConstructionEnv fpa, PropertyManager properties, 
			IString fromName, IString toName, Figure toArrow, Figure fromArrow){
		
		super( properties);
		this.from = G.getRegisteredNodeId(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
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
	
	private void beginCurve(double x, double y,GraphicsContext gc){
		if(useSplines){
			points = new double[20];
			cp = 0;
			addPointToCurve(x, y,gc);
		} else {
			x1 = x; y1 = y;
		}
	}
	
	private void addPointToCurve(double x, double y,GraphicsContext gc){
		if(useSplines){
			if(cp == points.length){
				double points1[] = new double[2*points.length];
				for(int i = 0; i < cp; i++)
					points1[i] = points[i];
				points = points1;
			}
			points[cp++] = location.getX() + x;
			points[cp++] = location.getY() + y;
		} else {
			gc.line(location.getX()+ x1, location.getY() + y1, location.getX() + x, location.getY() + y);
			x1 = x; y1 = y;
		}
	}
	
	private void endCurve(double x, double y,GraphicsContext gc){
		if(useSplines){
			addPointToCurve(x, y,gc);
			drawCurve(gc);
		} else
			gc.line(location.getX()+ x1, location.getY() + y1, location.getX() + x, location.getY() + y);	
	}
	/**
	 * Draw a bezier curve through a list of points. Inspired by a blog post "interpolating curves" by rj, which is in turn inspired by
	 * Keith Peter's "Foundations of Actionscript 3.0 Animation".
	 */
	
	private void drawCurve(GraphicsContext gc) {
		if (cp == 0)
			return;
		
		gc.noFill();
		gc.beginShape();
		double x1 = points[0];
		double y1 = points[1];
		double xc = 0.0f;
		double yc = 0.0f;
		double x2 = 0.0f;
		double y2 = 0.0f;
		gc.vertex(x1, y1);
		for (int i = 2; i < cp - 4; i += 2) {
			xc = points[i];
			yc = points[i + 1];
			x2 = (xc + points[i + 2]) * 0.5f;
			y2 = (yc + points[i + 3]) * 0.5f;
			gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
					         (2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		xc = points[cp - 4];
		yc = points[cp - 3];
		x2 = points[cp - 2];
		y2 = points[cp - 1];
		gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
				         (2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
		gc.endShape();
		points = null;
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
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
			
			beginCurve(getFrom().figX(), getFrom().figY(),gc);
			addPointToCurve(imX, imY,gc);

			LayeredGraphNode nextNode = currentNode.out.get(0);
			
			addPointToCurve(currentNode.figX(), currentNode.figY(),gc);
		
			LayeredGraphNode prevNode = currentNode;
			currentNode =  nextNode;
			
			while(currentNode.isVirtual()){
				if(debug)System.err.println("Add vertex for " + currentNode.name);
				nextNode = currentNode.out.get(0);
				addPointToCurve(currentNode.figX(), currentNode.figY(),gc);
				prevNode = currentNode;
				currentNode = nextNode;
			}
		
			drawLastSegment( imX, imY, prevNode, currentNode,gc);
			
		} else {
			if(debug)System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			if(getTo() == getFrom()){  // Drawing a self edge
				LayeredGraphNode node = getTo();
				double h = node.figure.minSize.getY();
				double w = node.figure.minSize.getX();
				@SuppressWarnings("unused")
				double hgap = prop.getReal(HGAP);
				double vgap = prop.getReal(VGAP);
				
//				beginCurve(location.getX() + node.figX(),                   location.getY() + node.figY()-h/3);
//				addPointToCurve(location.getX() + node.figX(),              location.getY() + node.figY()-h/3);
//				addPointToCurve(location.getX() + node.figX(),              location.getY() + node.figY()-h/3-vgap);
//				addPointToCurve(location.getX() + node.figX() + w/2 + hgap, location.getY() + node.figY()-h/3-vgap);
//				addPointToCurve(location.getX() + node.figX() + w/2 + hgap, location.getY() + node.figY()-h/3);
//				addPointToCurve(location.getX() + node.figX() + w/2 + hgap, location.getY() + node.figY());
//				addPointToCurve(location.getX() + node.figX() + w/2,        location.getY() + node.figY());
//				endCurve(location.getX() + node.figX() + w/2,        		 location.getY() + node.figY());
				
				beginCurve(location.getX() + node.figX(),                   location.getY() + node.figY()-h/2,gc);
				addPointToCurve(location.getX() + node.figX()+w/4,          location.getY() + node.figY()-(h/2+vgap/4),gc);
				addPointToCurve(location.getX() + node.figX()+w/2,          location.getY() + node.figY()-(h/2+vgap/2),gc);
				addPointToCurve(location.getX() + node.figX(),              location.getY() + node.figY()-(h+vgap),gc);
				addPointToCurve(location.getX() + node.figX(),              location.getY() + node.figY()-(h/2+vgap/4),gc);
				endCurve(location.getX() + node.figX(),                     location.getY() + node.figY()-h/2,gc);
				
				if(toArrow != null){
					if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
					//getTo().figure.connectArrowFrom(location.getX(), location.getY(), 
					//		getTo().figX(), getTo().figY(),
					////		node.figX(),  node.figY()-(h/2+vgap/4),
					//		toArrow,gc
					//);
					return;
				}
			} else {
			
				gc.line(location.getX() + getFrom().figX(), location.getY() + getFrom().figY(), 
					 location.getX() + getTo().figX(), location.getY() + getTo().figY());
			}
			
			if(fromArrow != null || toArrow != null){
				if(reversed){
					
					if(toArrow != null){
						if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
						getFrom().figure.connectArrowFrom(location.getX(), location.getY(), 
								getFrom().figX(), getFrom().figY(),
								getTo().figX(), getTo().figY(), 
								toArrow,gc, visibleSWTElements
						);
					}
						
					if(fromArrow != null){
						if(debug)System.err.println("[reversed] Drawing to arrow to " + getToOrg().name);
						getTo().figure.connectArrowFrom(location.getX(), location.getY(), 
								getTo().figX(), getTo().figY(),
								getFrom().figX(), getFrom().figY(), 
								fromArrow,gc, visibleSWTElements
						);
					}
				} else {
					if(debug)System.err.println("Drawing to arrow to " + getTo().name);
					if(toArrow != null){
						getTo().figure.connectArrowFrom(location.getX(), location.getY(), 
							getTo().figX(), getTo().figY(), 
							getFrom().figX(), getFrom().figY(),
							toArrow,gc, visibleSWTElements
						);
					}
					if(fromArrow != null){
						if(debug)System.err.println("Drawing from arrow from " + getFrom().name);
					    getFrom().figure.connectArrowFrom(location.getX(), location.getY(), 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							fromArrow,gc, visibleSWTElements
					    );
					}
				}
			}
			if(label != null){
				label.drawElement(gc,visibleSWTElements);
			}
			
		} 
		
	}
	
	private void drawLastSegment( double startImX, double startImY, LayeredGraphNode prevNode, LayeredGraphNode currentNode,GraphicsContext gc){		double dx = currentNode.figX() - prevNode.figX();
		double dy = (currentNode.figY() - prevNode.figY());
		double imScale = 0.6f;
		double imX = prevNode.figX() + dx / 2;
		double imY = prevNode.figY() + dy * imScale;
		
		if(debug)
			System.err.printf("drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
					prevNode.figX(), prevNode.figY(),
					currentNode.figX(), currentNode.figY(), imX, imY);
		
		addPointToCurve(imX, imY,gc);
		endCurve(currentNode.figX(), currentNode.figY(),gc);
		
		// Finally draw the arrows on both sides of the edge
		
		if(getFromArrow() != null){
		//	getFrom().figure.connectArrowFrom(location.getX(), location.getY(), getFrom().figX(), getFrom().figY(), startImX, startImY, getFromArrow(),gc);
		}
		if(getToArrow() != null){
			if(debug)System.err.println("Has a to arrow");
		//	currentNode.figure.connectArrowFrom(location.getX(), location.getY(), currentNode.figX(), currentNode.figY(), imX, imY, getToArrow(),gc);
		}
	}
	
	public void setLabelCoordinates(){
		if(label != null){
			labelX = getFrom().x + (getTo().x - getFrom().x)/2;
			labelY = getFrom().y + (getTo().y - getFrom().y)/2;
			
			labelMinX = labelX - 1.5f * label.minSize.getX();
			labelMaxX = labelX + 1.5f * label.minSize.getX();
			
			labelMinY = labelY - 1.5f * label.minSize.getY();
			labelMaxY = labelY + 1.5f * label.minSize.getY();
			
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
		double ax1 = labelX - label.minSize.getX()/2;
		double ax2 = labelX + label.minSize.getX()/2;
		double bx1 = other.labelX - other.label.minSize.getX()/2;
		double bx2 = other.labelX + other.label.minSize.getX()/2;
		double distX;
		
		if(ax1 < bx1){
			distX = bx1 - ax2;
		} else
			distX = ax1 - bx2;
		
		double ay1 = labelY - label.minSize.getY()/2;
		double ay2 = labelY + label.minSize.getY()/2;
		double by1 = other.labelY - other.label.minSize.getY()/2;
		double by2 = other.labelY + other.label.minSize.getY()/2;
		
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

	public Figure getLabel() {
		return label;
	}

	@Override
	public void computeMinSize() {
		
	}

	@Override
	public void resizeElement(Rectangle view) {
		
	}


	
}
