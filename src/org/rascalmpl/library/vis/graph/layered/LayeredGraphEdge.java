package org.rascalmpl.library.vis.graph.layered;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
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
	private boolean inverted = false;
	private static boolean debug = false;
	
	public LayeredGraphEdge(LayeredGraph G, FigurePApplet fpa, PropertyManager properties, IString fromName, IString toName, IEvaluatorContext ctx) {
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
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}
	
	LayeredGraphNode getFrom() {
		return inverted ? to : from;
	}

	LayeredGraphNode getTo() {
		return inverted? from : to;
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
			//System.err.println("Drawing a shape");
			fpa.noFill();
			fpa.beginShape();
			fpa.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			fpa.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			LayeredGraphNode g = getTo();
			while(g.isVirtual()){
				//System.err.println("Add vertex for " + g.name);
				fpa.curveVertex(left+g.figX(), top + g.figY());
				g = g.out.get(0);
			}
			//System.err.println("Add vertex for " + g.name);
			fpa.curveVertex(left+g.figX(), top + g.figY());
			fpa.curveVertex(left+g.figX(), top + g.figY());
			fpa.endShape();
		} else {
			//System.err.println("Drawing a line");
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
