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
	private boolean inverted = false;
	private static boolean debug = true;
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, IPropertyManager properties, 
			IString fromName, IString toName,
			IConstructor toArrowCons, IConstructor fromArrowCons, 
			IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
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
		
		super(fpa, properties, ctx);
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
		return inverted ? to : from;
	}

	LayeredGraphNode getTo() {
		return inverted? from : to;
	}
	
	Figure getFromArrow(){
		return inverted ? toArrow : fromArrow;
	}
	
	Figure getToArrow(){
		return inverted ? fromArrow : toArrow;
	}

	void invert(){
		inverted = true;
	}
	
	boolean isInverted(){
		return inverted;
	}
	
	@Override
	public
	void draw(float left, float top) {
		applyProperties();
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().x + "," + getFrom().y + ") -> (" + 
								                 to.name + ": " + to.x + "," + to.y + ")");
		if(getFrom().isVirtual()){
			//System.err.println("Ignore");
			return;
		}
		if(getTo().isVirtual()){
			System.err.println("Drawing a shape");
			LayeredGraphNode currentNode = getTo();
			
			float dx = currentNode.figX() - getFrom().figX();
			float dy = currentNode.figY() - currentNode.layerHeight/2 - getFrom().figY();
			float midX = getFrom().figX() + dx/2;
			float midY = getFrom().figY() + dy/2;
			System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",
					getFrom().figX(), getFrom().figY(),
					currentNode.figX(), currentNode.figY(), midX, midY);
			
			if(getFromArrow() != null){
				System.err.println("Drawing from arrow");
				getFrom().figure.connectFrom(left, top, 
						getFrom().figX(), getFrom().figY(), 
						midX, midY,
						getFromArrow()
				);
				
			} else
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), left + midX, top + midY);
			
			fpa.noFill();
			fpa.beginShape();
			fpa.vertex(left + midX, top + midY);      										// V1
			fpa.bezierVertex(left + getFrom().figX() + dx*1.05f, top + getFrom().figY()  + dy, 	// C1
							 left + currentNode.figX(),    top + currentNode.figY(),      	// C2
							 left + currentNode.figX(),    top + currentNode.figY()      	// V2
					);
            
			LayeredGraphNode prevNode = currentNode;
			currentNode =  currentNode.out.get(0);
			while(currentNode.isVirtual()){
				System.err.println("Add vertex for " + currentNode.name);
				LayeredGraphNode nextNode = currentNode.out.get(0);
				dx = currentNode.figX() - prevNode.figX();
				dy = currentNode.figY() + currentNode.layerHeight/2 - prevNode.figY();
				if(nextNode.isVirtual()){
						fpa.bezierVertex(left + prevNode.figX(), top + prevNode.figY() - 100,
								left + currentNode.figX(), top + currentNode.figY(),
								left + currentNode.figX(), top + currentNode.figY()
								);
				}else{
					fpa.bezierVertex(left + currentNode.figX(), top + currentNode.figY()  - 100,
							left + nextNode.figX(), top + nextNode.figY() + 100,
							left + currentNode.figX(), top + currentNode.figY()
							);
				}	
				prevNode = currentNode;
				currentNode = nextNode;
			}
			midX = prevNode.figX() + (currentNode.figX() - prevNode.figX())/2;
			midY = prevNode.figY() + (currentNode.figY() - prevNode.figY())/2;
			
			System.err.printf("after loop: (%f,%f) -> (%f,%f), midX=%f, midY=%f\n",
					prevNode.figX(), prevNode.figY(),
					currentNode.figX(), currentNode.figY(), midX, midY);
			
			if(getToArrow() != null){
				
				//fpa.bezierVertex(left + prevNode.figX(), top + prevNode.figY(),
				//				left + currentNode.figX(), top + currentNode.figY(),
				//		        left + midX, top + midY
				//		         );
				fpa.endShape();
				
				System.err.println("Has a to arrow");
				//currentNode.figure.connectFrom(left, top, 
				//		currentNode.figX(), currentNode.figY(), 
				//		midX, midY,
				//		getToArrow());
			} else {
				dx = currentNode.figX() - prevNode.figX();
				dy = currentNode.figY() + prevNode.layerHeight/2 - prevNode.figY();
				midX = prevNode.figX() + dx/2;
				midY = prevNode.figY() + dy/2;
				
				fpa.bezierVertex(left + prevNode.figX(), top + prevNode.figY(),   // C1
								 left + currentNode.figX(), top + currentNode.figY(), // C2
								 left + midX, top + midY	    // V	
				);
//				fpa.bezierVertex(left + prevNode.figX(), top + prevNode.figY(), // C1
//						left + midX, top +  midY,                              // C2
//						left + currentNode.figX(), top + currentNode.figY()	    // V	
//				);
				
				fpa.endShape();
			}
			
		} else {
			//System.err.println("Drawing a line");
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
			if(getToArrow() != null){
				getTo().figure.connectFrom(left, top, 
						getTo().figX(), getTo().figY(), 
						getFrom().figX(), getFrom().figY(),
						getToArrow()
				);

				if(getFromArrow() != null)
					getFrom().figure.connectFrom(left, top, 
							getFrom().figX(), getFrom().figY(), 
							getTo().figX(), getTo().figY(),
							getFromArrow()
					);
			} else 
				fpa.line(left + getFrom().figX(), top + getFrom().figY(), 
						left + getTo().figX(), top + getTo().figY());
		}
	}

	@Override
	public
	void bbox() {
		// TODO Auto-generated method stub
	}
}
