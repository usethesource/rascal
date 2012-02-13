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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;

import com.sun.tools.javac.util.Pair;
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
	Figure label = null;
	double labelX = 0;
	double labelY = 0;
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
		System.err.printf("addPoints(: %f,%f\n", x, y);
		if(useSplines){
			if(cp == points.length){
				double points1[] = new double[2*points.length];
				for(int i = 0; i < cp; i++)
					points1[i] = points[i];
				points = points1;
			}
			points[cp++] = globalLocation.getX() + x;
			points[cp++] = globalLocation.getY() + y;
		} else {
			gc.line(globalLocation.getX()+ x1, globalLocation.getY() + y1, globalLocation.getX() + x, globalLocation.getY() + y);
			x1 = x; y1 = y;
		}
	}
	
	private void endCurve(double x, double y,GraphicsContext gc){
		if(useSplines){
			addPointToCurve(x, y,gc);
			drawCurve(gc);
		} else
			gc.line(globalLocation.getX()+ x1, globalLocation.getY() + y1, globalLocation.getX() + x, globalLocation.getY() + y);	
	}
	/**
	 * Draw a bezier curve through a list of points. Inspired by a blog post "interpolating curves" by rj, which is in turn inspired by
	 * Keith Peter's "Foundations of Actionscript 3.0 Animation".
	 */
	
	private void drawCurve(GraphicsContext gc) {
		if (cp == 0)
			return;
		
		applyProperties(gc);
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
		if(debug) System.err.println("label: " + label);
		
		if(getFrom().isVirtual()){
			return;
		}
		
		applyProperties(gc);
		
		if(getTo().isVirtual()){
			ArrayList<LayeredGraphNode> virtualNodes = new ArrayList<LayeredGraphNode>();
			if(debug) System.err.println(getFrom().name + "->" + getTo().name + " label: " + label);
			
			for(LayeredGraphNode nv = getTo(); nv.isVirtual(); nv = nv.out.get(0)){
				if(debug) System.err.println(nv.name + " label: " + nv.label);
				virtualNodes.add(nv);
			}
			
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
		
			drawLastSegment( imX, imY, prevNode, currentNode, gc, visibleSWTElements);
			
		} else {
			if(debug)System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			if(getTo() == getFrom()){  // Drawing a self edge
				LayeredGraphNode node = getTo();
				double h = node.figure.minSize.getY();
				double w = node.figure.minSize.getX();
				double hgap = getFrom().graph.prop.getReal(HGAP);
				double vgap = getFrom().graph.prop.getReal(VGAP);
				System.err.printf("hgap=%f, vgap=%f\n", hgap, vgap);
				
				System.err.printf("Start self edge:\n");
				beginCurve(globalLocation.getX() + node.figX()+w/2,               globalLocation.getY() + node.figY()-h/4,gc);
				addPointToCurve(globalLocation.getX() + node.figX()+w/2+hgap/3,   globalLocation.getY() + node.figY()-(h/2),gc);
				addPointToCurve(globalLocation.getX() + node.figX()+w/2+hgap/3,   globalLocation.getY() + node.figY()-(h/2+vgap/3),gc);
				addPointToCurve(globalLocation.getX() + node.figX()+w/2,          globalLocation.getY() + node.figY()-(h/2+vgap/3),gc);
				endCurve(globalLocation.getX() + node.figX()+w/4,                 globalLocation.getY() + node.figY()-h/2,gc);

				
				if(toArrow != null){
					if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
					getTo().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
							getTo().figX(), getTo().figY(),
							getFrom().figX(),  getFrom().figY(),
							toArrow,gc, visibleSWTElements
					); 
					applyProperties(gc);
					return;
				}
			} else {
			
				gc.line(globalLocation.getX() + getFrom().figX(), globalLocation.getY() + getFrom().figY(), 
						globalLocation.getX() + getTo().figX(), globalLocation.getY() + getTo().figY());
			}
			
			if(fromArrow != null || toArrow != null){
				if(reversed){
					
					if(toArrow != null){
						if(debug)System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
						getFrom().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
								getFrom().figX(), getFrom().figY(),
								getTo().figX(), getTo().figY(), 
								toArrow,gc, visibleSWTElements
						);
						applyProperties(gc);
					}
						
					if(fromArrow != null){
						if(debug)System.err.println("[reversed] Drawing to arrow to " + getToOrg().name);
						getTo().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
								getTo().figX(), getTo().figY(),
								getFrom().figX(), getFrom().figY(), 
								fromArrow, gc, visibleSWTElements
						);
						applyProperties(gc);
					}
				} else {
					if(debug)System.err.println("Drawing to arrow to " + getTo().name);
					if(toArrow != null){
						getTo().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
							getTo().figX(), getTo().figY(), 
							getFrom().figX(), getFrom().figY(),
							toArrow, gc, visibleSWTElements
						);
						applyProperties(gc);
					}
					if(fromArrow != null){
						if(debug)System.err.println("Drawing from arrow from " + getFrom().name);
					    getFrom().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							fromArrow, gc, visibleSWTElements
					    );
					    applyProperties(gc);
					}
				}
			}
			
		} 
	}
	
	private void drawLastSegment( double startImX, double startImY, LayeredGraphNode prevNode, LayeredGraphNode currentNode,GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		double dx = currentNode.figX() - prevNode.figX();
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
			getFrom().figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
					                          getFrom().figX(), getFrom().figY(), 
					                          startImX, startImY, 
					                          getFromArrow(), gc, visibleSWTElements);
			applyProperties(gc);
		}
		if(getToArrow() != null){
			if(debug)System.err.println("Has a to arrow");
			currentNode.figure.connectArrowFrom(globalLocation.getX(), globalLocation.getY(), 
												currentNode.figX(), currentNode.figY(),
												imX, imY, 
												getToArrow(), gc, visibleSWTElements);
		}
		applyProperties(gc);
	}
	
	public void setLabelCoordinates(){
		
		setLabelCoordinates(0.4);
	}
	
	public void setLabelCoordinates(double perc){
		if(label != null){
			int dirX = getFrom().x < getTo().x ? 1 : -1;
			int dirY = getFrom().y < getTo().y ? 1 : -1;
			
			labelX = 5 + getFrom().x + dirX*(Math.abs(getTo().x - getFrom().x))*perc;
			labelY = getFrom().y + dirY*(Math.abs(getTo().y - getFrom().y))*perc;
			System.err.println("setLabelCoordinates: " + getFrom().name + "->" + getTo().name + ": " + labelX + ", " + labelY);
		}
	}

	public void placeLabel(double yLabel, int align){
		if(label != null){
			System.err.printf("%s->%s: placeLabel: %f, align=%d\n", getFrom().name, getTo().name, yLabel, align);
			int dirX = getFrom().x < getTo().x ? 1 : -1;
			
			labelY = yLabel;
			double perc = (labelY- Math.min(getFrom().y, getTo().y))/(Math.abs(getTo().y - getFrom().y));
			labelX = 5 + getFrom().x + dirX*(Math.abs(getTo().x - getFrom().x))*perc;
			if(align < 0)
				labelX -= label.minSize.getX();
		}
	}
	
	public boolean xoverlap(LayeredGraphEdge other){
		if(!yoverlap(other))
			return false;
		return (labelX < other.labelX) ? (labelX + label.minSize.getX() > other.labelX)
				                       : (other.labelX + label.minSize.getX() >labelX);
	}
	
	public boolean yoverlap(LayeredGraphEdge other){
		double top = labelY - label.minSize.getY()/2;
		double bot = labelY + label.minSize.getY()/2;
				
		double otop = other.labelY - other.label.minSize.getY()/2;
		double obot = other.labelY + other.label.minSize.getY()/2;
		
		return top < otop ? bot > otop : obot > top;
				
	}
	
	
	private boolean xoverlap(LinkedList<LayeredGraphEdge> layerLabels, int k){
		LayeredGraphEdge other = layerLabels.get(k);
		for(int i = 0; i < k; i++)
			if(layerLabels.get(i).xoverlap(other)){
				System.err.println("xoverlap: " + i + " and " + k);
				return true;
			}
		return false;
	}
	
	private static void optimize(LinkedList<LayeredGraphEdge> layerLabels, int k){
		if(k == 0){
			LayeredGraphEdge e0 = layerLabels.get(0);
			e0.labelX -= e0.label.minSize.getX()  + 10;
			return;
		}

		LayeredGraphEdge current = layerLabels.get(k);
		double h = current.label.minSize.getY();
		double baseY = current.labelY;
		
		int dir = k % 2 == 0 ? 1 : -1;
		double deltaY[] = {baseY, baseY + dir * h, baseY - dir * h};
		
		int align[] = {1, -1};
		for(int a : align){
			for(double dy : deltaY){
				current.placeLabel(dy, a);
				if(!current.xoverlap(layerLabels, k))
					return;
			}
		}
		current.placeLabel(baseY, 1);
		System.err.println("*** NO SOLUTION ***");
	}
	
	public static void optimizeLabels(LinkedList<LayeredGraphEdge> layerLabels){
		
		for(int i = 0; i < layerLabels.size(); i++){
			optimize(layerLabels, i);
		}
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

