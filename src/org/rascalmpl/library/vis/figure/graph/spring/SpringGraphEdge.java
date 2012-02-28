/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.spring;

import static org.rascalmpl.library.vis.properties.Properties.FROM_ARROW;
import static org.rascalmpl.library.vis.properties.Properties.TO_ARROW;

import java.util.List;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Rectangle;


/**
 * A GraphEdge is created for each "edge" constructor that occurs in a graph.
 * 
 * @author paulk
 *
 */
public class SpringGraphEdge extends Figure {
	private SpringGraphNode from;
	private SpringGraphNode to;
	private Figure toArrow;
	private Figure fromArrow;
	private static boolean debug = false;
	
	public SpringGraphEdge(SpringGraph G, IFigureConstructionEnv fpa, PropertyManager properties, 
						IString fromName, IString toName) {
		super(properties);
		this.from = G.getRegistered(fromName.getValue());
		
		if(getFrom() == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + fromName.getValue() + "\"",
					fromName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegistered(toName.getValue());
		if(to == null){
			throw RuntimeExceptionFactory.figureException("No node with id property + \"" + toName.getValue() + "\"", toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		
		toArrow = prop.getFig(TO_ARROW);
		fromArrow = prop.getFig(FROM_ARROW);
		
		this.children = childless;
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}

	SpringGraphNode getFrom() {
		return from;
	}

	SpringGraphNode getTo() {
		return to;
	}
	
	@Override
	public
	void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements) {
		applyProperties(gc);
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().getX() + "," + getFrom().getY() + ") -> (" + 
				to.name + ": " + to.getX() + "," + to.getY() + ")");

		double globalX = globalLocation.getX();
		double globalY = globalLocation.getY();
		Coordinate centerFrom = getFrom().figure.getGlobalCenter();
		Coordinate centerTo = getTo().figure.getGlobalCenter();
		
		gc.line(centerFrom.getX(), centerFrom.getY(), centerTo.getX(), centerTo.getY());
		
		
//		gc.line(globalX + getFrom().getX(), globalY + getFrom().getY(), 
//				globalX + getTo().getX(),   globalY + getTo().getY());
		if(toArrow != null){
			getTo().figure.connectArrowFrom(
					globalX + getTo().getX(), globalY + getTo().getY(), 
					globalX + getFrom().getX(), globalY + getFrom().getY(),
					toArrow,gc, visibleSWTElements
			);

			if(fromArrow != null)
				getFrom().figure.connectArrowFrom(
						globalX + getFrom().getX(), globalY + getFrom().getY(), 
						globalX + getTo().getX(), globalY + getTo().getY(),
						fromArrow,gc, visibleSWTElements
				);
		}	
	}

	@Override
	public void computeMinSize() {
		localLocation.set(0,0);
	}

	@Override
	public void resizeElement(Rectangle view) {
		//localLocation.set(0,0);
	}

}
