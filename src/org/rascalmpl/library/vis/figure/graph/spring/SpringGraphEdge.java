/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import static org.rascalmpl.library.vis.properties.Properties.LABEL;
import static org.rascalmpl.library.vis.properties.Properties.TO_ARROW;

import java.util.List;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.value.IString;


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
	private boolean visible = true;
	private Figure label;
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
		label = prop.getFig(LABEL);

		if(label != null){
			this.children = new Figure[1];
			this.children[0] = label;
		} else
			this.children = childless;
		
		if(debug)System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}

	SpringGraphNode getFrom() {
		return from;
	}

	SpringGraphNode getTo() {
		return to;
	}
	
	public void setVisible(boolean b){
		visible = b;
	}
	
	@Override
	public
	void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements) {
		if(!visible)
			return;
		
		if(debug) System.err.println("edge: (" + getFrom().name + ": " + getFrom().getCenterX() + "," + getFrom().getCenterY() + ") -> (" + 
				to.name + ": " + to.getCenterX() + "," + to.getCenterY() + ")");

		double globalX = globalLocation.getX();
		double globalY = globalLocation.getY();
		Coordinate centerFrom = getFrom().figure.getGlobalCenter();
		Coordinate centerTo = getTo().figure.getGlobalCenter();
		
		applyProperties(gc);
		gc.line(centerFrom.getX(), centerFrom.getY(), centerTo.getX(), centerTo.getY());
		
		if(toArrow != null){
			getTo().figure.connectArrowFrom(
					globalX + getTo().getCenterX(), globalY + getTo().getCenterY(), 
					globalX + getFrom().getCenterX(), globalY + getFrom().getCenterY(),
					toArrow,gc, visibleSWTElements
			);

			if(fromArrow != null)
				getFrom().figure.connectArrowFrom(
						globalX + getFrom().getCenterX(), globalY + getFrom().getCenterY(), 
						globalX + getTo().getCenterX(), globalY + getTo().getCenterY(),
						fromArrow,gc, visibleSWTElements
				);
		}	
	}

	@Override
	public void computeMinSize() {
		localLocation.set(0,0);
		if(label != null){
			int dirX = getFrom().getCenterX() < getTo().getCenterX() ? 1 : -1;
			int dirY = getFrom().getCenterY() < getTo().getCenterY()? 1 : -1;

			double labelX = 5 + getFrom().getCenterX() + dirX*(Math.abs(getTo().getCenterX() - getFrom().getCenterY()));
			double labelY = getFrom().getCenterY() + dirY*(Math.abs(getTo().getCenterY() - getFrom().getCenterY()));

			label.localLocation.set(labelX, labelY);
		}
	}

	@Override
	public void resizeElement(Rectangle view) {
		//localLocation.set(0,0);
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
		
		
	}

}
