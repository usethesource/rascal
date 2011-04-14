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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.graph.lattice;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;

import org.rascalmpl.library.vis.Figure;

/**
 * A GraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 * 
 */
public class LatticeGraphNode {
	private final boolean debug = false;
	int label = -1;
	int layer = -1;
	String name;
	Figure figure;
	LinkedList<LatticeGraphNode> in;
	LinkedList<LatticeGraphNode> out;
	HashSet<LatticeGraphNode> reached = new HashSet<LatticeGraphNode>();
	float x, cX[]; // Candidates of x
	float y;
	public int rank;
	public int rankTop;
	public int rankBottom;
	public boolean mousePressed = false;
	// private int fromX, fromY;

	LatticeGraphNode(String name, Figure fig) {
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LatticeGraphNode>();
		out = new LinkedList<LatticeGraphNode>();
	}

	public float figX() {
		return x;
	}

	public float figY() {
		return y;
	}

	void bbox() {
		if (figure != null) {
			figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
		}
	}

	float width() {
		return figure != null ? figure.width : 0;
	}

	float height() {
		return figure != null ? figure.height : 0;
	}

	void draw(float left, float top) {
		if (figure != null) {
			figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
			figure.draw(x + left - figure.width / 2, y + top - figure.height
					/ 2);
			// if (mousePressed) {
			// // System.err.println("Pressed");
			// IFigureApplet fpa = figure.fpa;
			// fpa.stroke(255, 0, 0);
			// fpa.strokeWeight(1);
			// // fpa.noFill();
			// fpa.rect(fromX + left - 10, fromY + top - 10, 20, 20);
			// fpa.stroke(0, 0, 0);
			// }
		}
	}

	public boolean mouseOver(int mousex, int mousey, boolean mouseInParent) {

		if (figure.mouseInside(mousex, mousey, figure.getCenterX(),
				figure.getCenterY())
				&& !mousePressed) {
			if (debug)
				System.err.println(""
						+ this
						+ " "
						+ mousePressed
						+ " "
						+ figure.getCenterX()
						+ " "
						+ figure.getCenterY()
						+ " "
						+ figure.mouseInside(mousex, mousey,
								figure.getCenterX(), figure.getCenterY()) + " "
						+ figure.getClass());
			return figure.mouseOver(mousex, mousey, mouseInParent);
		}
		figure.fpa.unRegisterMouseOver(this.figure);
		return false;
	}

	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e) {
		if (figure.mouseInside(mouseX, mouseY, figure.getCenterX(),
				figure.getCenterY())) {
			mousePressed = true;
			figure.fpa.unRegisterMouseOver(this.figure);
			// fromX = mouseX;
			// fromY = mouseY;
			if (debug)
				System.err.println("mousePressed");
			return true;
		} else
			return false;
		// return figure.mousePressed(mousex, mousey, e);
	}

	public boolean mouseReleased() {
		if (mousePressed) {
			mousePressed = false;
			if (debug)
				System.err.println("mouseReleased");
			return true;
		}
		return false;
		// return figure.mousePressed(mousex, mousey, e);
	}

	public void addIn(LatticeGraphNode n) {
		if (!in.contains(n))
			in.add(n);
	}

	public void addOut(LatticeGraphNode n) {
		if (!out.contains(n))
			out.add(n);
	}
	
	public boolean isConnected(LatticeGraphNode n) {
		return this.in.contains(n) || this.out.contains(n);
	}
}
