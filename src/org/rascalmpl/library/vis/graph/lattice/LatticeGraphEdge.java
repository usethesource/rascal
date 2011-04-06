/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.graph.lattice;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureColorUtils;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.ConstantColorProperty;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.library.vis.properties.Property;

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

	public LatticeGraphEdge(LatticeGraph G, FigurePApplet fpa,
			IPropertyManager properties, IString fromName, IString toName,
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
	public void draw(float left, float top) {
		applyProperties();
		if (debug)
			System.err.println("edge: (" + getFrom().name + ": " + getFrom().x
					+ "," + getFrom().y + ") -> (" + to.name + ": " + to.x
					+ "," + to.y + ")");
		if (this.isCurved()) {
			float mx = (left + getFrom().figX() + left + getTo().figX()) / 2 + 20, my = (top
					+ getFrom().figY() + top + getTo().figY()) / 2;
			fpa.noFill();
			fpa.beginShape();
			fpa.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			fpa.curveVertex(left + getFrom().figX(), top + getFrom().figY());
			fpa.curveVertex(mx, my);
			fpa.curveVertex(left + getTo().figX(), top + getTo().figY());
			fpa.curveVertex(left + getTo().figX(), top + getTo().figY());
			fpa.endShape();
		} else
			fpa.line(left + getFrom().figX(), top + getFrom().figY(), left
					+ getTo().figX(), top + getTo().figY());
	}

	public void setColor(String s) {
		IInteger cl = FigureColorUtils.colorNames.get(s);
		if (cl != null)
			new ConstantColorProperty(Property.LINE_COLOR, cl.intValue());
	}

	@Override
	public void bbox() {
		// TODO Auto-generated method stub

	}

}
