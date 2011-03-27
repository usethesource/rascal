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
		System.err.println("*** Before reverse ***");
		from.print();
		to.print();
		reversed = true;
//		from.out.remove(to);
//		to.in.remove(from);
//		from.in.add(to);
//		to.out.add(from);
		
		from.delOut(to);
		to.delIn(from);
		from.addIn(to);
		to.addOut(from);
		
		System.err.println("*** After reverse ***");
		from.print();
		to.print();
	}
	
	boolean isReversed(){
		return reversed;
	}
	
	@Override
	public
	void draw(float left, float top) {
		applyProperties();
		
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 getTo().name + ": " + getTo().x + "," + getTo().y + ")");
		if(getFrom().isVirtual()){
			//System.err.println("Ignore");
			return;
		}
		if(getTo().isVirtual()){
			System.err.println("Drawing a shape, inverted=" + reversed);
			LayeredGraphNode currentNode = getTo();
			float currentX = currentNode.figX();
			float currentY = currentNode.figY();
			
			if(fromArrow != null){
				System.err.println("Drawing from arrow from " + getFrom().name);
				getFrom().figure.connectFrom(left, top, 
						getFrom().figX(), getFrom().figY(), 
						currentX, currentY,
						fromArrow
				);
				
			} else
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), left + currentX, top + currentY);
            
			LayeredGraphNode prevNode = currentNode;
			currentNode =  currentNode.out.get(0);
			while(currentNode.isVirtual()){
				System.err.println("Add vertex for " + currentNode.name);
				fpa.line(left + prevNode.figX(), top + prevNode.figY(), left + currentNode.figX(), top + currentNode.figY());
				prevNode = currentNode;
				currentNode = prevNode.out.get(0);
			}
			
			drawLastSegment1(prevNode, currentNode);
			
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
	
	private void drawLastSegment1(LayeredGraphNode prevNode, LayeredGraphNode currentNode){
		
		Figure toArrow = getToArrow();
		if(toArrow != null){
			System.err.println("Has a to arrow");
			currentNode.figure.connectFrom(getLeft(), getTop(), 
					currentNode.figX(), currentNode.figY(), 
					prevNode.figX(), prevNode.figY(),
					toArrow);
		} else {
			fpa.line(getLeft() + prevNode.figX(), getTop() + prevNode.figY(), getLeft() + currentNode.figX(), getTop() + currentNode.figY());
		}
	}
	
	
	public
	void draw2(float left, float top) {
		applyProperties();
		
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 getTo().name + ": " + getTo().x + "," + getTo().y + ")");
		if(getFrom().isVirtual()){
			//System.err.println("Ignore");
			return;
		}
		if(getTo().isVirtual()){
			System.err.println("Drawing a shape, inverted=" + reversed);
			LayeredGraphNode currentNode = getTo();
			
			float dx = currentNode.figX() - getFrom().figX();
			//float dy = currentNode.figY() - currentNode.layerHeight/2 - getFrom().figY();
			float dy = 0.5f * (currentNode.figY() - getFrom().figY());
			float midX = getFrom().figX() + dx/2;
			float midY = getFrom().figY() + dy/2;
			System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",
					getFrom().figX(), getFrom().figY(),
					currentNode.figX(), currentNode.figY(), midX, midY);
			
			Figure fromArrow = getFromArrow();
			
			if(fromArrow != null){
				System.err.println("Drawing from arrow from " + getFrom().name);
				getFrom().figure.connectFrom(left, top, 
						getFrom().figX(), getFrom().figY(), 
						midX, midY,
						fromArrow
				);
				
			} else
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), left + midX, top + midY);
			
			fpa.noFill();
			
			fpa.beginShape();
			fpa.vertex(		 left + midX, 				top + midY);      					// V1
			fpa.bezierVertex(left + currentNode.figX(), top + midY + dy/2, 					// C1
					         left + currentNode.figX(), top + currentNode.figY() - dy/5,    // C2
					         left + currentNode.figX(), top + currentNode.figY()      		// V2
			);
			
            
			LayeredGraphNode prevNode = currentNode;
			currentNode =  currentNode.out.get(0);
			while(currentNode.isVirtual()){
				System.err.println("Add vertex for " + currentNode.name);
				LayeredGraphNode nextNode = currentNode.out.get(0);
				dx = currentNode.figX() - prevNode.figX();
				dy = 0.3f * (currentNode.figY() + prevNode.figY());
				if(nextNode.isVirtual()){
						fpa.bezierVertex(left + prevNode.figX(), 	top + prevNode.figY(),		//C1
										 left + currentNode.figX(), top + currentNode.figY(),	//C2
										 left + currentNode.figX(), top + currentNode.figY()	// C2
								);
				}
				//else{
//					fpa.bezierVertex(left + currentNode.figX(), top + currentNode.figY()  - 100,
//							left + nextNode.figX(), top + nextNode.figY() + 100,
//							left + currentNode.figX(), top + currentNode.figY()
//							);
//				}	
				prevNode = currentNode;
				currentNode = nextNode;
			}
			
			drawLastSegment(prevNode, currentNode);
			
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
	
	private void drawLastSegment(LayeredGraphNode prevNode, LayeredGraphNode currentNode){
		float dx = currentNode.figX() - prevNode.figX();
		float dy = 0.1f * (currentNode.figY() - prevNode.figY());
		float midX = prevNode.figX() + dx/2;
		float midY = prevNode.figY() + dy/2;
		
		System.err.printf("after loop: (%f,%f) -> (%f,%f), midX=%f, midY=%f\n",
				prevNode.figX(), prevNode.figY(),
				currentNode.figX(), currentNode.figY(), midX, midY);
		
		Figure toArrow = getToArrow();
		if(toArrow != null){
			
//			fpa.bezierVertex(getLeft() + prevNode.figX(), 	getTop() + prevNode.figY() + prevNode.layerHeight/2,   // C1
//					 	 	 getLeft() + prevNode.figX(), 	getTop() + prevNode.figY() + prevNode.layerHeight/2,   // C2
//					 		 getLeft() + 1.06f*midX, 				getTop() + midY	 +  prevNode.layerHeight								   // V	
//			);
			
			
			fpa.bezierVertex(getLeft() + prevNode.figX(), 	getTop() + prevNode.figY() + prevNode.layerHeight/2,   // C1
			 	 	 getLeft() + prevNode.figX(), 	getTop() + prevNode.figY() + prevNode.layerHeight/2,   // C2
			 		 getLeft() + midX, 				getTop() + midY	    								   // V	
	);
			
			fpa.endShape();
			
			System.err.println("Has a to arrow");
			currentNode.figure.connectFrom(getLeft(), getTop(), 
					currentNode.figX(), currentNode.figY(), 
					midX, midY,
					toArrow);
		} else {
			dx = currentNode.figX() - prevNode.figX();
			dy = currentNode.figY() + prevNode.layerHeight/2 - prevNode.figY();
			midX = prevNode.figX() + dx/2;
			midY = prevNode.figY() + dy/2;
			
			fpa.bezierVertex(getLeft() + prevNode.figX(), getTop() + prevNode.figY() + dy/2,   // C1
							 getLeft() + prevNode.figX(), getTop() + prevNode.figY() + dy/2,   // C2
							 getLeft() + currentNode.figX(), getTop() + currentNode.figY()	    // V	
			);
			fpa.endShape();
		}
	}

	@Override
	public
	void bbox() {
		// TODO Auto-generated method stub
	}
}
