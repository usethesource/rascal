/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.lattice;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;

/**
 * A GraphEdge is created for each "edge" constructor that occurs in a graph.
 * 
 * @author paulk
 * 
 */
public class LatticeGraphEdge extends Figure {
	private LatticeGraphNode from;
	private LatticeGraphNode to;
	private static boolean debug = false;

	public LatticeGraphEdge(LatticeGraph G, IFigureConstructionEnv fpa,
			PropertyManager properties, IString fromName, IString toName) {
		super( properties);
		this.from = G.getRegistered(fromName.getValue());
		if (getFrom() == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + fromName.getValue()
							+ "\"", fromName, fpa.getRascalContext().getCurrentAST(),
					fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegistered(toName.getValue());
		if (to == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + toName.getValue() + "\"",
					toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}

		if (debug)
			System.err.println("edge: " + fromName.getValue() + " -> "
					+ toName.getValue());
	}

	LatticeGraphNode getFrom() {
		return from;
	}

	LatticeGraphNode getTo() {
		return to;
	}

	@Override
	public void draw(GraphicsContext gc) {
		applyProperties(gc);
		if (debug)
			System.err.println("edge: (" + getFrom().name + ": " + getFrom().x
					+ "," + getFrom().y + ") -> (" + to.name + ": " + to.x
					+ "," + to.y + ")");
		if (getCurvedProperty()) {
			double mx = (getLeft() + getFrom().figX() + getLeft() + getTo().figX()) / 2 + 20, my = (getTop()
					+ getFrom().figY() + getTop() + getTo().figY()) / 2;
			gc.noFill();
			gc.beginShape();
			gc.curveVertex(getLeft() + getFrom().figX(), getTop() + getFrom().figY());
			gc.curveVertex(getLeft() + getFrom().figX(), getTop() + getFrom().figY());
			gc.curveVertex(mx, my);
			gc.curveVertex(getLeft() + getTo().figX(), getTop() + getTo().figY());
			gc.curveVertex(getLeft() + getTo().figX(), getTop() + getTo().figY());
			gc.endShape();
		} else
			gc.line(getLeft() + getFrom().figX(), getTop() + getFrom().figY(), getLeft()
					+ getTo().figX(), getTop() + getTo().figY());
	}

	public void setColor(String s) {
		//IInteger cl = FigureColorUtils.colorNames.get(s);
		//if (cl != null)
			//new ConstantColorProperty( cl.intValue());
	}

	@Override
	public void bbox() {
		setNonResizable();
		super.bbox();
	}

	@Override
	public void layout() {
		size.set(minSize);
	}


}
