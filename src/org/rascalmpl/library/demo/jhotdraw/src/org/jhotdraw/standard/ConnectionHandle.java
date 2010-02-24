/*
 * @(#)ConnectionHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.Undoable;

/**
 * A handle to connect figures.
 * The connection object to be created is specified by a prototype.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld029.htm>Prototype</a></b><br>
 * ConnectionHandle creates the connection by cloning a prototype.
 * <hr>
 *
 * @see ConnectionFigure
 * @see Object#clone
 *
 * @version <$CURRENT_VERSION$>
 */
public  class ConnectionHandle extends LocatorHandle {

	/**
	 * the currently created connection
	 */
	private ConnectionFigure myConnection;

	/**
	 * the prototype of the connection to be created
	 */
	private ConnectionFigure fPrototype;

	/**
	 * the current target
	 */
	private Figure myTargetFigure;

	/**
	 * Constructs a handle with the given owner, locator, and connection prototype
	 */
	public ConnectionHandle(Figure owner, Locator l, ConnectionFigure prototype) {
		super(owner, l);
		fPrototype = prototype;
	}

	/**
	 * Creates the connection
	 */
	public void invokeStart(int  x, int  y, DrawingView view) {
		setConnection(createConnection());

		setUndoActivity(createUndoActivity(view));
		Vector v = new Vector();
		v.add(getConnection());
		getUndoActivity().setAffectedFigures(new FigureEnumerator(v));

		Point p = locate();
		getConnection().startPoint(p.x, p.y);
		getConnection().endPoint(p.x, p.y);
		view.drawing().add(getConnection());
	}

	/**
	 * Tracks the connection.
	 */
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Point p = new Point(x,y);
		Figure f = findConnectableFigure(x, y, view.drawing());
		// track the figure containing the mouse
		if (f != getTargetFigure()) {
			if (getTargetFigure() != null) {
				getTargetFigure().connectorVisibility(false, null);
			}
			setTargetFigure(f);
			if (getTargetFigure() != null) {
				getTargetFigure().connectorVisibility(true, getConnection());
			}
		}

		Connector target = findConnectionTarget(p.x, p.y, view.drawing());
		if (target != null) {
			p = Geom.center(target.displayBox());
		}
		getConnection().endPoint(p.x, p.y);
	}

	/**
	 * Connects the figures if the mouse is released over another
	 * figure.
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		Connector target = findConnectionTarget(x, y, view.drawing());
		if (target != null) {
			getConnection().connectStart(startConnector());
			getConnection().connectEnd(target);
			getConnection().updateConnection();
		}
		else {
			view.drawing().remove(getConnection());
			setUndoActivity(null);
		}
		setConnection(null);
		if (getTargetFigure() != null) {
			getTargetFigure().connectorVisibility(false, null);
			setTargetFigure(null);
		}
	}

	private Connector startConnector() {
		Point p = locate();
		return owner().connectorAt(p.x, p.y);
	}

	/**
	 * Creates the ConnectionFigure. By default the figure prototype is
	 * cloned.
	 */
	protected ConnectionFigure createConnection() {
		return (ConnectionFigure)fPrototype.clone();
	}

	/**
	 * Factory method for undo activity.
	 */
	protected Undoable createUndoActivity(DrawingView view) {
		return new PasteCommand.UndoActivity(view);
	}

	/**
	 * Finds a connection end figure.
	 */
	protected Connector findConnectionTarget(int x, int y, Drawing drawing) {
		Figure target = findConnectableFigure(x, y, drawing);
		if ((target != null) && target.canConnect()
			 && !target.includes(owner())
			 && getConnection().canConnect(owner(), target)) {
				return findConnector(x, y, target);
		}
		return null;
	}

	private Figure findConnectableFigure(int x, int y, Drawing drawing) {
		FigureEnumeration fe = drawing.figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (!figure.includes(getConnection()) && figure.canConnect() 
				&& figure.containsPoint(x, y)) {
				return figure;
			}
		}
		return null;
	}

	protected Connector findConnector(int x, int y, Figure f) {
		return f.connectorAt(x, y);
	}


	/**
	 * Draws the connection handle, by default the outline of a
	 * blue circle.
	 */
	public void draw(Graphics g) {
		Rectangle r = displayBox();
		g.setColor(Color.blue);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	protected void setConnection(ConnectionFigure newConnection) {
		myConnection = newConnection;
	}
	
	protected ConnectionFigure getConnection() {
		return myConnection;
	}

	protected Figure getTargetFigure() {
		return myTargetFigure;
	}

	protected void setTargetFigure(Figure newTargetFigure) {
		myTargetFigure = newTargetFigure;
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getCursor()
	 */
	public Cursor getCursor() {
		return new AWTCursor(java.awt.Cursor.HAND_CURSOR);
	}

}
