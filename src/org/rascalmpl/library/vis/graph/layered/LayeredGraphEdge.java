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
	private static boolean debug = true;
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
	
	float lastX;
	float lastY;
	
	private void beginCurve(float x, float y){
		if(useSplines){
			fpa.smooth();
			fpa.noFill();
			fpa.beginShape();
			fpa.curveVertex(x, y);
			fpa.curveVertex(x, y);
		} else{
			lastX = x; lastY = y;
		}
	}
	
	private void endCurve(float x, float y){
		if(useSplines){
			fpa.curveVertex(x, y);
			fpa.curveVertex(x, y);
			fpa.endShape();
		} else
			fpa.line(getLeft()+ lastX, getTop() + lastY, getLeft() + x, getTop() + y);	
	}
	
	private void addPointToCurve(float x, float y){
		if(useSplines)
			fpa.curveVertex(x, y);
		else
			fpa.line(getLeft()+ lastX, getTop() + lastY, getLeft() + x, getTop() + y);
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
			
			System.err.println("Drawing a shape, inverted=" + reversed);
			LayeredGraphNode currentNode = getTo();
			
			float dx = currentNode.figX() - getFrom().figX();
			float dy = (currentNode.figY() - getFrom().figY());
			float imScale = 0.5f;
			float imX = getFrom().figX() + dx/2;
			float imY = getFrom().figY() + dy * imScale;
			System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",	getFrom().figX(), getFrom().figY(),	currentNode.figX(), currentNode.figY(), imX, imY);
			
			
			
			if(getFromArrow() != null){
				System.err.println("Drawing from arrow from " + getFrom().name);
				getFrom().figure.connectFrom(left, top, getFrom().figX(), getFrom().figY(), imX, imY, getFromArrow());
				beginCurve(imX, imY);
			} else {
				beginCurve(getFrom().figX(), getFrom().figY());
				addPointToCurve(imX, imY);
			}

			LayeredGraphNode nextNode = currentNode.out.get(0);
			
			addPointToCurve(currentNode.figX(), currentNode.figY());
			
			LayeredGraphNode prevprevNode = getFrom();
			LayeredGraphNode prevNode = currentNode;
			currentNode =  nextNode;
			
			while(currentNode.isVirtual()){
				System.err.println("Add vertex for " + currentNode.name);
				nextNode = currentNode.out.get(0);
				addPointToCurve(currentNode.figX(), currentNode.figY());
				prevprevNode = prevNode;
				prevNode = currentNode;
				currentNode = nextNode;
			}
		
			drawLastSegment(left, top, prevNode, currentNode);
			
		} else {
			System.err.println("Drawing a line " + getFrom().name + " -> " + getTo().name + "; inverted=" + reversed);
			if(getTo() == getFrom()){  // Drawing a self edge
				LayeredGraphNode node = getTo();
				float h = node.figure.height;
				float w = node.figure.width;
				float hgap = getHGapProperty();
				float vgap = getVGapProperty();
				
				fpa.beginShape();
				fpa.curveVertex(left + node.figX(),              top + node.figY()-h/2);
				fpa.curveVertex(left + node.figX(),              top + node.figY()-h/2);
				fpa.curveVertex(left + node.figX(),              top + node.figY()-h/2-vgap);
				fpa.curveVertex(left + node.figX() + w/2 + hgap, top + node.figY()-h/2-vgap);
				fpa.curveVertex(left + node.figX() + w/2 + hgap, top + node.figY()-h/2);
				fpa.curveVertex(left + node.figX() + w/2 + hgap, top + node.figY());
				fpa.curveVertex(left + node.figX() + w/2,        top + node.figY());
				fpa.curveVertex(left + node.figX() + w/2,        top + node.figY());
				fpa.endShape();
			}
			
			if(fromArrow != null || toArrow != null){
				if(reversed){
					
					if(toArrow != null)
						System.err.println("[reversed] Drawing from arrow from " + getFrom().name);
						getFrom().figure.connectFrom(left, top, 
								getFrom().figX(), getFrom().figY(),
								getTo().figX(), getTo().figY(), 
								toArrow
					);
						
					if(fromArrow != null){
						System.err.println("[reversed] Drawing to arrow to " + getToOrg().name);
						getTo().figure.connectFrom(left, top, 
								getTo().figX(), getTo().figY(),
								getFrom().figX(), getFrom().figY(), 
								fromArrow
						);
					}
				} else {
					System.err.println("Drawing to arrow to " + getToOrg().name);
					getTo().figure.connectFrom(left, top, 
							getTo().figX(), getTo().figY(), 
							getFrom().figX(), getFrom().figY(),
							toArrow
					);
					if(fromArrow != null)
						System.err.println("Drawing from arrow from " + getFrom().name);
					   getFrom().figure.connectFrom(left, top, 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							fromArrow
					);
			}
			} else {
				System.err.println("Drawing lines without arrows");
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
		
		System.err.printf("drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
				prevNode.figX(), prevNode.figY(),
				currentNode.figX(), currentNode.figY(), imX, imY);
	
		if(getToArrow() != null){
			System.err.println("Has a to arrow");
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
