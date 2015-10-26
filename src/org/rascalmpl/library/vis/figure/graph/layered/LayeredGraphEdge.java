/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.value.IString;

/**
 * A LayeredGraphEdge is created for each "edge" constructor that occurs in a graph:
 * - An optional label is a child of LayeredGraphEdge
 * - Optional to and from arrows are handled outside the default Figure framework (they cannot have mouseOvers etc).
 * 
 * @author paulk
 *
 */
public class LayeredGraphEdge extends Figure {
	private LayeredGraphNode from;
	private LayeredGraphNode to;
	private LayeredGraphNode endNode; // End node of a chain of virtual nodes that start in this node.
	Figure toArrow;
	Figure fromArrow;
	Figure label = null;
	double labelX = 0;
	double labelY = 0;
	boolean reversed = false;
	private static boolean debug = true;
	
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
		label = prop.getFig(LABEL);

		if(label != null){
			this.children = new Figure[1];
			this.children[0] = label;
		} else
			this.children = childless;
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue() +
				", arrows (to/from): " + toArrow + " " + fromArrow + " " + label);
	}
	
	public LayeredGraphEdge(LayeredGraph G, IFigureConstructionEnv fpa, PropertyManager properties, 
			IString fromName, IString toName, Figure toArrow, Figure fromArrow){
		
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
		this.toArrow = toArrow;
		this.fromArrow = fromArrow;
		
		if(label != null){
			this.children = new Figure[1];
			this.children[0] = label;
		} else
			this.children = childless;
	}
	
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {

		if(fromArrow != null){
			fromArrow.init(env, resolver, mparent, swtSeen, visible);
		}
		if(toArrow != null){
			toArrow.init(env, resolver, mparent, swtSeen, visible);
		}
//		if(label!=null){
//			label.init(env, resolver, mparent, swtSeen, visible);
//		}
		return false;
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
	//double x1;
	//double y1;
	
	private void beginCurve(double x, double y){
		points = new double[20];
		cp = 0;
		addPointToCurve(x, y);
	}
	
	private void addPointToCurve(double x, double y){
		if(cp == points.length){
			double points1[] = new double[2*points.length];
			for(int i = 0; i < cp; i++)
				points1[i] = points[i];
			points = points1;
		}
		points[cp++] =  x;
		points[cp++] =  y;
	}
	
	private void endCurve(double x, double y){
		addPointToCurve(x, y);
	}
	
	private void addLastSegment(double startImX, double startImY, LayeredGraphNode prevNode, LayeredGraphNode currentNode){
		double dx = currentNode.figX() - prevNode.figX();
		double dy = (currentNode.figY() - prevNode.figY());
		double imScale = 0.6f;
		double imX = prevNode.figX() + dx / 2;
		double imY = prevNode.figY() + dy * imScale;
		endNode = currentNode;
		
		if(debug)
			System.err.printf("drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
					prevNode.figX(), prevNode.figY(),
					currentNode.figX(), currentNode.figY(), imX, imY);
		
		addPointToCurve(imX, imY);
		endCurve(currentNode.figX(), currentNode.figY());
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
		double globalX = globalLocation.getX();
		double globalY = globalLocation.getY();
		double x1 = globalX + points[0];
		double y1 = globalY + points[1];
		double xc = 0.0f;
		double yc = 0.0f;
		double x2 = 0.0f;
		double y2 = 0.0f;
		gc.vertex(x1, y1);
		for (int i = 2; i < cp - 4; i += 2) {
			xc = globalX + points[i];
			yc = globalY + points[i + 1];
			x2 = (xc + globalX + points[i + 2]) * 0.5f;
			y2 = (yc + globalY + points[i + 3]) * 0.5f;
			gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
					         (2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		xc = globalX + points[cp - 4];
		yc = globalY + points[cp - 3];
		x2 = globalX + points[cp - 2];
		y2 = globalY + points[cp - 1];
		gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
				         (2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
		gc.endShape();
	}
	
	private void drawCurveAndArrows(GraphicsContext gc,  List<IHasSWTElement> visibleSWTElements){
		drawCurve(gc);
		double globalX = globalLocation.getX();
		double globalY = globalLocation.getY();
		// Recover the intermediate points for directing the arrows
		double startImX = globalX + points[2];
		double startImY = globalY + points[3];
		double endImX = globalX + points[cp - 4];
		double endImY = globalY + points[cp - 3];
		
		if(getFromArrow() != null){
			getFrom().figure.connectArrowFrom(globalX + getFrom().figX(), globalY + getFrom().figY(), 
											  startImX,  startImY,
					                          getFromArrow(), gc, 
					                          visibleSWTElements);
			applyProperties(gc);
		}
		if(getToArrow() != null && endNode != null){
			if(debug)System.err.println("Has a to arrow");
			
			endNode.figure.connectArrowFrom(globalX + endNode.figX(), globalY + endNode.figY(),
											endImX,  endImY,
											getToArrow(), gc, 
											visibleSWTElements);
		}
		applyProperties(gc);
	}
	
	private void minSizeCurve(){
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		for (int i = 0; i < cp - 2; i += 2) {
			double xp = points[i];
			double yp = points[i + 1];
			if(xp > maxX)
				maxY = xp;
			if(xp < minX)
				minX = xp;
			if(yp > maxY)
				maxY = yp;
			if(yp < minY)
				minY = yp;
		}
		if(label != null){
			minX = Math.min(minX, labelX);
			maxX = Math.max(maxX, labelX + label.minSize.getX());
			minY = Math.min(minY, labelY - label.minSize.getY()/2);
			maxY = Math.max(maxY, labelY + label.minSize.getY()/2);
		}
	}
	
	@Override
	public void computeMinSize() {
		
		if(debug){
				System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 getTo().name + ": " + getTo().x + "," + getTo().y + ")");
				System.err.println("label: " + label);
		}
		
		resizable.set(false,false);
		
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
			
			if(debug)System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n", getFrom().figX(), getFrom().figY(),	currentNode.figX(), currentNode.figY(), imX, imY);
			
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
			addLastSegment(imX, imY, prevNode, currentNode);
			minSizeCurve();
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
				beginCurve(node.figX()+w/2,               node.figY()-h/4);
				addPointToCurve(node.figX()+w/2+hgap/3,   node.figY()-(h/2));
				addPointToCurve(node.figX()+w/2+hgap/3,   node.figY()-(h/2+vgap/3));
				addPointToCurve(node.figX()+w/2,          node.figY()-(h/2+vgap/3));
				endCurve(node.figX()+w/4,                 node.figY()-h/2);

				minSizeCurve();
				return;
			} else {
				/*
				double minX = Math.min(getTo().figX(), getFrom().figX());
				double maxX = Math.max(getTo().figX() + getTo().minSize.getX(), getFrom().figX() +  getFrom().minSize.getX());
				
				double minY = Math.min(getTo().figY() - getTo().minSize.getY()/2, getFrom().figY() -  getFrom().minSize.getY()/2);
				double maxY = Math.max(getTo().figY() + getTo().minSize.getY()/2, getFrom().figY() +  getFrom().minSize.getY()/2);
				
				if(label != null){
					minX = Math.min(minX, labelX);
					maxX = Math.max(maxX, labelX + label.minSize.getX());
					minY = Math.min(minY, labelY - label.minSize.getY()/2);
					maxY = Math.max(maxY, labelY + label.minSize.getY()/2);
				}
				*/
				//minSize.set(maxX - minX, maxY - minY);
			}
		} 
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0, 0);
/*
		double minX = Math.min(getTo().figX(), getFrom().figX());
		double maxX = Math.max(getTo().figX() + getTo().minSize.getX(), getFrom().figX() +  getFrom().minSize.getX());

		double minY = Math.min(getTo().figY() - getTo().minSize.getY()/2, getFrom().figY() -  getFrom().minSize.getY()/2);
		double maxY = Math.max(getTo().figY() + getTo().minSize.getY()/2, getFrom().figY() +  getFrom().minSize.getY()/2);

		if(label != null){
			minX = Math.min(minX, labelX);
			maxX = Math.max(maxX, labelX + label.minSize.getX());
			minY = Math.min(minY, labelY - label.minSize.getY()/2);
			maxY = Math.max(maxY, labelY + label.minSize.getY()/2);
		}

		//size.set(maxX - minX, maxY - minY);
*/
		if(fromArrow!=null){
			fromArrow.localLocation.set(0,0);
			fromArrow.globalLocation.set(0,0);
			fromArrow.size.set(minSize);
			//fromArrow.resize(view,transform);
		}
		if(toArrow!=null){
			toArrow.localLocation.set(0,0);
			toArrow.globalLocation.set(0,0);
			toArrow.size.set(minSize);
			//toArrow.resize(view,transform);
		}

		if(label != null){
			label.localLocation.setX(labelX);
			label.localLocation.setY(labelY - label.minSize.getY()/2);
		}
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
		System.err.println("Drawing edge " +  getFrom().name + " -> " + getTo().name);
		if(getFrom().isVirtual()){
			return;
		}
		
		applyProperties(gc);
		
		if(getTo().isVirtual()){			
			drawCurveAndArrows(gc, visibleSWTElements);
		} else {
			if(debug)System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			double globalX = globalLocation.getX();
			double globalY = globalLocation.getY();
			if(getTo() == getFrom()){  // Drawing a self edge
				drawCurve(gc);
				LayeredGraphNode node = getTo();
				double h = node.figure.minSize.getY();
				double w = node.figure.minSize.getX();
				drawSelfArrow(toArrow,   globalX + node.figX()+w/4, globalY + node.figY()-h/2, gc, visibleSWTElements);
				drawSelfArrow(fromArrow, globalX + node.figX()+w/2, globalY + node.figY()-h/4, gc, visibleSWTElements);
				return;
			} else {
				gc.line(globalX + getFrom().figX(), globalY + getFrom().figY(), 
						globalX + getTo().figX(),   globalY + getTo().figY());
				drawArrow(toArrow, !reversed, gc, visibleSWTElements);
				drawArrow(fromArrow, reversed, gc, visibleSWTElements);
			}
		} 
	}
	
	private void drawArrow(Figure arrow, boolean towards, GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		if(arrow != null){
			LayeredGraphNode from = towards ? getFrom() : getTo();
			LayeredGraphNode to   = towards ? getTo() : getFrom();
			to.figure.connectArrowFrom(globalLocation.getX() + to.figX(),   globalLocation.getY() + to.figY(), 
					      			   globalLocation.getX() + from.figX(), globalLocation.getY() + from.figY(), 
					      			   arrow, gc, visibleSWTElements);
			applyProperties(gc);
		}
	}
	
	private void drawSelfArrow(Figure arrow, double cx, double cy, GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		if(arrow != null){
			LayeredGraphNode to = getTo();
			to.figure.connectArrowFrom(globalLocation.getX() + to.figX(), globalLocation.getY() + to.figY(), 
									   cx, cy,
									   arrow, gc, visibleSWTElements); 
			applyProperties(gc);
		}
	}
	
	/*
	 * Placement of labels.
	 */
	
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
			label.localLocation.setX(labelX);
			label.localLocation.setY(labelY - label.minSize.getY()/2);
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
}

