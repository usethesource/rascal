/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.leveled;


import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;

import java.util.Vector;
/**
 * A GraphEdge is created for each "edge" constructor that occurs in a graph.
 * 
 * @author paulk
 * 
 */
public class LeveledGraphEdge extends Figure {
	private LeveledGraphNode from;
	private LeveledGraphNode to;
	private LeveledGraphNode oldFrom;
	final private LeveledGraph G;
	IFigureConstructionEnv fpa;
	
	public LeveledGraphNode getOldFrom() {
		return oldFrom;
	}

	public void setOldFrom(LeveledGraphNode oldFrom) {
		this.oldFrom = oldFrom;
	}

	public LeveledGraphNode getOldTo() {
		return oldTo;
	}

	public void setOldTo(LeveledGraphNode oldTo) {
		this.oldTo = oldTo;
	}

	private LeveledGraphNode oldTo;
	Figure toArrow;
	Figure fromArrow;
	Figure label;
	double labelX;
	double labelMinX;
	double labelMaxX;
	double labelMinY;
	double labelMaxY;
	double labelY;
	boolean reversed = false;
	private static boolean debug = false;
	private static boolean useSplines = true;

	public LeveledGraphEdge(LeveledGraph G, IFigureConstructionEnv fpa,
			PropertyManager properties, IString fromName, IString toName) {
		super(properties);
		this.fpa = fpa;
		this.G = G;
		this.from = G.getRegisteredNodeId(fromName.getValue());

		if (getFrom() == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + fromName.getValue()
							+ "\"", fromName, fpa.getRascalContext().getCurrentAST(),
					fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
		if (to == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + toName.getValue() + "\"",
					toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		toArrow = super.getToArrow();

		fromArrow = super.getFromArrow();

		label = getLabel();

		if (debug)
			System.err.println("edge: " + fromName.getValue() + " -> "
					+ toName.getValue() + ", arrows (to/from): " + toArrow
					+ " " + fromArrow + " " + label);
	}

	public LeveledGraphEdge(LeveledGraph G, IFigureConstructionEnv fpa,
			PropertyManager properties, IString fromName, IString toName,
			Figure toArrow, Figure fromArrow) {

		super( properties);
		this.G = G;
		this.from = G.getRegisteredNodeId(fromName.getValue());

		if (getFrom() == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + fromName.getValue()
							+ "\"", fromName, fpa.getRascalContext().getCurrentAST(),
					fpa.getRascalContext().getStackTrace());
		}
		to = G.getRegisteredNodeId(toName.getValue());
		if (to == null) {
			throw RuntimeExceptionFactory.figureException(
					"No node with id property + \"" + toName.getValue() + "\"",
					toName, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
		}
		this.toArrow = toArrow;
		this.fromArrow = fromArrow;
	}

	LeveledGraphNode getFrom() {
		return reversed ? to : from;
	}

	LeveledGraphNode getFromOrg() {
		return from;
	}

	LeveledGraphNode getTo() {
		return reversed ? from : to;
	}

	LeveledGraphNode getToOrg() {
		return to;
	}

	public Figure getFromArrow() {
		return fromArrow;
	}

	public Figure getToArrow() {
		return toArrow;
	}

	void reverse() {
		if (debug) {
			System.err.println("*** Before reverse ***");
			from.print();
			to.print();
		}
		reversed = true;
		from.delOut(to);
		to.delIn(from);
		from.addIn(to);
		to.addOut(from);
		if (debug) {
			System.err.println("*** After reverse ***");
			from.print();
			to.print();
		}
	}

	boolean isReversed() {
		return reversed;
	}

	/*
	 * Primitives for drawing a multi-vertex edge
	 */

	double points[];
	int cp;
	double x1;
	double y1;

	private void beginCurve(double x, double y,GraphicsContext gc) {
		if (useSplines) {
			points = new double[20];
			cp = 0;
			addPointToCurve(x, y,gc);
		} else {
			x1 = x;
			y1 = y;
		}
	}

	private void addPointToCurve(double x, double y,GraphicsContext gc) {
		if (useSplines) {
			if (cp == points.length) {
				double points1[] = new double[2 * points.length];
				for (int i = 0; i < cp; i++)
					points1[i] = points[i];
				points = points1;
			}
			points[cp++] = getLeft() + x;
			points[cp++] = getTop() + y;
		} else {
			gc.line(getLeft() + x1, getTop() + y1, getLeft() + x, getTop() + y);
			x1 = x;
			y1 = y;
		}
	}

	private void endCurve(double x, double y,GraphicsContext gc) {
		if (useSplines) {
			addPointToCurve(x, y,gc);
			drawCurve(gc);
		} else
			gc.line(getLeft() + x1, getTop() + y1, getLeft() + x, getTop() + y);
	}

	/**
	 * Draw a bezier curve through a list of points. Inspired by a blog post
	 * "interpolating curves" by rj, which is in turn inspired by Keith Peter's
	 * "Foundations of Actionscript 3.0 Animation".
	 */

	private void drawCurve(GraphicsContext gc) {
		if (cp == 0)
			return;

		gc.noFill();
		gc.beginShape();
		double x1 = points[0];
		double y1 = points[1];
		double xc = 0.0f;
		double yc = 0.0f;
		double x2 = 0.0f;
		double y2 = 0.0f;
		gc.vertex(x1, y1);
		for (int i = 2; i < cp - 4; i += 2) {
			xc = points[i];
			yc = points[i + 1];
			x2 = (xc + points[i + 2]) * 0.5f;
			y2 = (yc + points[i + 3]) * 0.5f;
			gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
					(2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		xc = points[cp - 4];
		yc = points[cp - 3];
		x2 = points[cp - 2];
		y2 = points[cp - 1];
		gc.bezierVertex((x1 + 2.0f * xc) / 3.0f, (y1 + 2.0f * yc) / 3.0f,
				(2.0f * xc + x2) / 3.0f, (2.0f * yc + y2) / 3.0f, x2, y2);
		gc.endShape();
		points = null;
	}

	@Override
	public void draw(GraphicsContext gc) {
		applyProperties(gc);
		if (debug)
			System.err.println("edge: (" + getFrom().name + ": " + getFrom().x
					+ "," + getFrom().y + ") -> (" + getTo().name + ": "
					+ getTo().x + "," + getTo().y + ")");
		if (getFrom().isVirtual()) {
			return;
		}
		if (getTo().isVirtual()) {

			if (debug)
				System.err.println("Drawing a shape, inverted=" + reversed);
			LeveledGraphNode currentNode = getTo();

			double dx = currentNode.figX() - getFrom().figX();
			double dy = (currentNode.figY() - getFrom().figY());
			double imScale = 0.4f;
			double imX = getFrom().figX() + dx / 2;
			double imY = getFrom().figY() + dy * imScale;

			if (debug)
				System.err.printf("(%f,%f) -> (%f,%f), midX=%f, midY=%f\n",
						getFrom().figX(), getFrom().figY(), currentNode.figX(),
						currentNode.figY(), imX, imY);

			beginCurve(getFrom().figX(), getFrom().figY(),gc);
			addPointToCurve(imX, imY,gc);

			LeveledGraphNode nextNode = currentNode.out.get(0);

			addPointToCurve(currentNode.figX(), currentNode.figY(),gc);

			LeveledGraphNode prevNode = currentNode;
			currentNode = nextNode;

			while (currentNode.isVirtual()) {
				if (debug)
					System.err.println("Add vertex for " + currentNode.name);
				nextNode = currentNode.out.get(0);
				addPointToCurve(currentNode.figX(), currentNode.figY(),gc);
				prevNode = currentNode;
				currentNode = nextNode;
			}

			drawLastSegment(imX, imY, prevNode, currentNode,gc);

		} else {
			if (debug)
				System.err.println("Drawing a line " + getFrom().name + " -> "
						+ getTo().name + "; inverted=" + reversed);
			if (getTo() == getFrom()) { // Drawing a self edge
				LeveledGraphNode node = getTo();
				double h = node.figure.minSize.getHeight();
				double w = node.figure.minSize.getWidth();
				double hgap = getHGapProperty();
				double vgap = getVGapProperty();

				// beginCurve(getLeft() + node.figX(), getTop() + node.figY()-h/3);
				// addPointToCurve(getLeft() + node.figX(), getTop() + node.figY()-h/3);
				// addPointToCurve(getLeft() + node.figX(), getTop() +
				// node.figY()-h/3-vgap);
				// addPointToCurve(getLeft() + node.figX() + w/2 + hgap, getTop() +
				// node.figY()-h/3-vgap);
				// addPointToCurve(getLeft() + node.figX() + w/2 + hgap, getTop() +
				// node.figY()-h/3);
				// addPointToCurve(getLeft() + node.figX() + w/2 + hgap, getTop() +
				// node.figY());
				// addPointToCurve(getLeft() + node.figX() + w/2, getTop() + node.figY());
				// endCurve(getLeft() + node.figX() + w/2, getTop() + node.figY());

				beginCurve(getLeft() + node.figX(), getTop() + node.figY() - h / 2,gc);
				addPointToCurve(getLeft() + node.figX() + w / 4, getTop() + node.figY()
						- (h / 2 + vgap / 4),gc);
				addPointToCurve(getLeft() + node.figX() + w / 2, getTop() + node.figY()
						- (h / 2 + vgap / 2),gc);
				addPointToCurve(getLeft() + node.figX(), getTop() + node.figY()
						- (h + vgap),gc);
				addPointToCurve(getLeft() + node.figX(), getTop() + node.figY()
						- (h / 2 + vgap / 4),gc);
				endCurve(getLeft() + node.figX(), getTop() + node.figY() - h / 2,gc);

				if (toArrow != null) {
					if (debug)
						System.err
								.println("[reversed] Drawing from arrow from "
										+ getFrom().name);
					getTo().figure.connectArrowFrom(getLeft(), getTop(), getTo().figX(),
							getTo().figY(), node.figX(), node.figY()
									- (h / 2 + vgap / 4), toArrow,gc);
					return;
				}
			} else {

				gc.line(getLeft() + getFrom().figX(), getTop() + getFrom().figY(), getLeft()
						+ getTo().figX(), getTop() + getTo().figY());
			}

			if (fromArrow != null || toArrow != null) {
				if (reversed) {

					if (toArrow != null) {
						if (debug)
							System.err
									.println("[reversed] Drawing from arrow from "
											+ getFrom().name);
						getFrom().figure.connectArrowFrom(getLeft(), getTop(), getFrom()
								.figX(), getFrom().figY(), getTo().figX(),
								getTo().figY(), toArrow,gc);
					}

					if (fromArrow != null) {
						if (debug)
							System.err
									.println("[reversed] Drawing to arrow to "
											+ getToOrg().name);
						getTo().figure.connectArrowFrom(getLeft(), getTop(), getTo()
								.figX(), getTo().figY(), getFrom().figX(),
								getFrom().figY(), fromArrow,gc);
					}
				} else {
					if (debug)
						System.err.println("Drawing to arrow to "
								+ getTo().name);
					if (toArrow != null) {
						getTo().figure.connectArrowFrom(getLeft(), getTop(), getTo()
								.figX(), getTo().figY(), getFrom().figX(),
								getFrom().figY(), toArrow,gc);
					}
					if (fromArrow != null) {
						if (debug)
							System.err.println("Drawing from arrow from "
									+ getFrom().name);
						getFrom().figure.connectArrowFrom(getLeft(), getTop(), getFrom()
								.figX(), getFrom().figY(), getTo().figX(),
								getTo().figY(), fromArrow,gc);
					}
				}
			}
			if (label != null) {
				label.draw(gc);
			}
		}
	}

	private void drawLastSegment(double startImX,
			double startImY, LeveledGraphNode prevNode,
			LeveledGraphNode currentNode,GraphicsContext gc) {
		double dx = currentNode.figX() - prevNode.figX();
		double dy = (currentNode.figY() - prevNode.figY());
		double imScale = 0.6f;
		double imX = prevNode.figX() + dx / 2;
		double imY = prevNode.figY() + dy * imScale;

		if (debug)
			System.err.printf(
					"drawLastSegment: (%f,%f) -> (%f,%f), imX=%f, imY=%f\n",
					prevNode.figX(), prevNode.figY(), currentNode.figX(),
					currentNode.figY(), imX, imY);

		addPointToCurve(imX, imY,gc);
		endCurve(currentNode.figX(), currentNode.figY(),gc);

		// Finally draw the arrows on both sides of the edge

		if (getFromArrow() != null) {
			getFrom().figure.connectArrowFrom(getLeft(), getTop(), getFrom().figX(),
					getFrom().figY(), startImX, startImY, getFromArrow(),gc);
		}
		if (getToArrow() != null) {
			if (debug)
				System.err.println("Has a to arrow");
			currentNode.figure.connectArrowFrom(getLeft(), getTop(), currentNode.figX(),
					currentNode.figY(), imX, imY, getToArrow(),gc);
		}
	}

	public void setLabelCoordinates() {
		if (label != null) {
			labelX = getFrom().x + (getTo().x - getFrom().x) / 2;
			labelY = getFrom().y + (getTo().y - getFrom().y) / 2;

			labelMinX = labelX - 1.5f * label.minSize.getWidth();
			labelMaxX = labelX + 1.5f * label.minSize.getWidth();

			labelMinY = labelY - 1.5f * label.minSize.getHeight();
			labelMaxY = labelY + 1.5f * label.minSize.getHeight();

			System.err.printf("edge %s->%s: labelX=%f, labelY=%f\n", from.name,
					to.name, labelX, labelY);
		}
	}

	public void shiftLabelCoordinates(double dx, double dy) {
		if (label != null) {
			System.err.printf("shiftLabelCoordinates %s-> %s: %f, %f\n",
					from.name, to.name, dx, dy);
			labelX = Math.min(Math.max(labelMinX, labelX + dx), labelMaxX);
			labelY = Math.min(Math.max(labelMinY, labelY + dy), labelMaxY);
		}
	}

	public void reduceOverlap(LeveledGraphEdge other) {
		double ax1 = labelX - label.minSize.getWidth() / 2;
		double ax2 = labelX + label.minSize.getWidth() / 2;
		double bx1 = other.labelX - other.label.minSize.getWidth() / 2;
		double bx2 = other.labelX + other.label.minSize.getWidth() / 2;
		double distX;

		if (ax1 < bx1) {
			distX = bx1 - ax2;
		} else
			distX = ax1 - bx2;

		double ay1 = labelY - label.minSize.getHeight() / 2;
		double ay2 = labelY + label.minSize.getHeight() / 2;
		double by1 = other.labelY - other.label.minSize.getHeight() / 2;
		double by2 = other.labelY + other.label.minSize.getHeight() / 2;

		double distY;

		if (ay1 < by1) {
			distY = by1 - ay2 - 2;
		} else
			distY = ay1 - by2 - 2;

		System.err.printf("reduceOverlap %s->%s, %s->%s: distX=%f, distY=%f\n",
				from.name, to.name, other.from.name, other.to.name, distX,
				distY);
		if (distX > 0)
			return;

		distX = distX > 0 ? 0 : -distX;
		distY = distY > 0 ? 0 : -distY;
		shiftLabelCoordinates(-distX / 2, -distY / 2);
		other.shiftLabelCoordinates(distX / 2, distY / 2);
	}

	@Override
	public void bbox() {
		if (fromArrow != null)
			fromArrow.bbox();
		if (toArrow != null)
			toArrow.bbox();
		if (label != null)
			label.bbox();
		setNonResizable();
		super.bbox();
	}

	public boolean getFiguresUnderMouse(Coordinate c, Vector<Figure> result) {
		return (fromArrow != null && fromArrow.getFiguresUnderMouse(c, result))
				|| (label != null && label.getFiguresUnderMouse(c, result))
				|| (toArrow != null && toArrow.getFiguresUnderMouse(c, result));
	}

	public void computeFiguresAndProperties(ICallbackEnv env) {
		super.computeFiguresAndProperties(env);
		if (fromArrow != null)
			fromArrow.computeFiguresAndProperties(env);
		if (toArrow != null)
			toArrow.computeFiguresAndProperties(env);
		if (label != null)
			label.computeFiguresAndProperties(env);
	}

	public void registerNames(NameResolver resolver) {
		super.registerNames(resolver);
		if (fromArrow != null)
			fromArrow.registerNames(resolver);
		if (toArrow != null)
			toArrow.registerNames(resolver);
		if (label != null)
			label.registerNames(resolver);
	}

	@Override
	public void layout() {
		size.set(minSize);
		if (fromArrow != null) {
			fromArrow.setToMinSize();
			fromArrow.layout();
		}
		if (toArrow != null) {
			toArrow.setToMinSize();
			toArrow.layout();
		}
		if (label != null) {
			label.setToMinSize();
			label.layout();
		}
	}

	public void pushFrom(LeveledGraphNode from) {
		this.oldFrom = this.from;
		this.getTo().delIn(this.from);
		this.from = from;
		this.getTo().addIn(this.from);
	}

	public void pushTo(LeveledGraphNode to) {
		this.oldTo = this.to;
		this.getFrom().delOut(to);
		this.to = to;
		this.getFrom().addIn(to);
	}

	public LeveledGraphNode popFrom() {
		if (this.oldFrom != null) {
			LeveledGraphNode f = this.from;
			this.getTo().delIn(f);
			this.from = this.oldFrom;
			this.oldFrom = null;
			this.getTo().addIn(this.from);
			return this.from;
		}
		return null;
	}

	public LeveledGraphNode popTo() {
		if (this.oldTo != null) {
			LeveledGraphNode f = this.to;
			this.getFrom().delOut(f);
			this.to = this.oldTo;
			this.oldTo = null;
			this.getFrom().addOut(this.getTo());
			return this.to;
		}
		return null;
	}


}
