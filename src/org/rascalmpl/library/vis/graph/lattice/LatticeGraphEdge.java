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
package org.rascalmpl.library.vis.graph.lattice;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureExecutionEnvironment;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

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

	public LatticeGraphEdge(LatticeGraph G, IFigureExecutionEnvironment fpa,
			PropertyManager properties, IString fromName, IString toName,
			IEvaluatorContext ctx) {
		super(fpa, properties);
		this.from = G.getRegistered(fromName.getValue());
		if (getFrom() == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + fromName.getValue()
							+ "\"", fromName, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		to = G.getRegistered(toName.getValue());
		if (to == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + toName.getValue() + "\"",
					toName, ctx.getCurrentAST(), ctx.getStackTrace());
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
	public void draw(double left, double top, GraphicsContext gc) {
		applyProperties(gc);
		if (debug)
			System.err.println("edge: (" + getFrom().name + ": " + getFrom().x
					+ "," + getFrom().y + ") -> (" + to.name + ": " + to.x
					+ "," + to.y + ")");
		if (getCurvedProperty()) {
			double mx = (left + getFrom().figX() + left + getTo().figX()) / 2 + 20, my = (top
					+ getFrom().figY() + top + getTo().figY()) / 2;
			gc.noFill();
			gc.beginShape();
			gc.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			gc.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			gc.curveVertex(mx, my);
			gc.curveVertex(left + getTo().figX(), top + getTo().figY());
			gc.curveVertex(left + getTo().figX(), top + getTo().figY());
			gc.endShape();
		} else
			gc.line(left + getFrom().figX(), top + getFrom().figY(), left
					+ getTo().figX(), top + getTo().figY());
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
