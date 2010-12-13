package org.rascalmpl.library.vis.graph.layered;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

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
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, PropertyManager properties, 
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
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, PropertyManager properties, 
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
			LayeredGraphNode toNode = getTo();
			float midX = getFrom().figX() + (toNode.figX() - getFrom().figX())/2;
			float midY = getFrom().figY() + (toNode.figY() - toNode.layerHeight/2 - getFrom().figY())/2;
			
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
			
			fpa.curveVertex(left + midX, top + midY);
			fpa.curveVertex(left + midX, top + midY);
			
			LayeredGraphNode lastVnode = toNode;
			while(toNode.isVirtual()){
				System.err.println("Add vertex for " + toNode.name);
				LayeredGraphNode next = toNode.out.get(0);
				
				if(next.isVirtual()){
					fpa.curveVertex(left + toNode.figX(), top + toNode.figY());
				} else {
					fpa.curveVertex(left + toNode.figX(), top + toNode.figY() - toNode.layerHeight/2);
					fpa.curveVertex(left + toNode.figX(), top + toNode.figY() + toNode.layerHeight/2);
				}
				lastVnode = toNode;
				toNode = next;
			}
			
			if(getToArrow() != null){
				midX = lastVnode.figX() + (toNode.figX() - lastVnode.figX())/2;
				midY = lastVnode.figY() + (toNode.figY() + toNode.layerHeight/2 - lastVnode.figY())/2;
				
				fpa.curveVertex(left + midX, top + midY);
				fpa.curveVertex(left + midX, top + midY);
				fpa.endShape();
				
				System.err.println("Has a to arrow");
				toNode.figure.connectFrom(left, top, 
						toNode.figX(), toNode.figY(), 
						midX, midY,
						getToArrow());
			} else {
				fpa.curveVertex(left + toNode.figX(), top + toNode.figY());
				fpa.curveVertex(left + toNode.figX(), top + toNode.figY());
				fpa.endShape();
			}
			
		} else {
			//System.err.println("Drawing a line");
			if(getTo() == getFrom()){
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
