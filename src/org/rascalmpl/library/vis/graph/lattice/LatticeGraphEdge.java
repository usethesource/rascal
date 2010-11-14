package org.rascalmpl.library.vis.graph.lattice;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;
import org.rascalmpl.library.vis.graph.spring.SpringGraph;

import processing.core.PApplet;

/**
 * A GraphEdge is created for each "edge" constructor that occurs in a graph.
 * 
 * @author paulk
 *
 */
public class LatticeGraphEdge extends Figure {
	private LatticeGraphNode from;
	private LatticeGraphNode to;
	private boolean inverted = false;
	private static boolean debug = false;
	
	public LatticeGraphEdge(LatticeGraph G, FigurePApplet fpa, PropertyManager properties, IString fromName, IString toName, IEvaluatorContext ctx) {
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
	

	LatticeGraphNode getFrom() {
		return inverted ? to : from;
	}

	LatticeGraphNode getTo() {
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
	
			//System.err.println("Drawing a line");
		    fpa.line(left + getFrom().figX(), top + getFrom().figY(), 
				     left + getTo().figX(), top + getTo().figY());
	}

	@Override
	public
	void bbox() {
		// TODO Auto-generated method stub
		
	}

}
