/*
 * @(#)LineConnection.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.awt.*;
import java.util.List;
import java.io.*;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;

/**
 * A LineConnection is a standard implementation of the
 * ConnectionFigure interface. The interface is implemented with PolyLineFigure.
 *
 * @see ConnectionFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class LineConnection extends PolyLineFigure implements ConnectionFigure {

	protected Connector    myStartConnector;
	protected Connector    myEndConnector;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 6883731614578414801L;
	private int lineConnectionSerializedDataVersion = 1;

	/**
	 * Constructs a LineConnection. A connection figure has
	 * an arrow decoration at the start and end.
	 */
	public LineConnection() {
		super(4);
		setStartDecoration(new ArrowTip());
		setEndDecoration(new ArrowTip());
	}

	/**
	 * Tests whether a figure can be a connection target.
	 * ConnectionFigures cannot be connected and return false.
	 */
	public boolean canConnect() {
		return false;
	}

	/**
	 * Ensures that a connection is updated if the connection
	 * was moved.
	 */
	protected void basicMoveBy(int dx, int dy) {
		// don't move the start and end point since they are connected
		for (int i = 1; i < fPoints.size()-1; i++) {
			pointAt(i).translate(dx, dy);
		}

		updateConnection(); // make sure that we are still connected
	}

	/**
	 * Sets the start figure of the connection.
	 */
	public void connectStart(Connector newStartConnector) {
		setStartConnector(newStartConnector);
		if (newStartConnector != null) {
			startFigure().addDependendFigure(this);
			startFigure().addFigureChangeListener(this);
		}
	}

	/**
	 * Sets the end figure of the connection.
	 */
	public void connectEnd(Connector newEndConnector) {
		setEndConnector(newEndConnector);
		if (newEndConnector != null) {
			endFigure().addDependendFigure(this);
			endFigure().addFigureChangeListener(this);
			handleConnect(startFigure(), endFigure());
		}
	}

	/**
	 * Disconnects the start figure.
	 */
	public void disconnectStart() {
		startFigure().removeFigureChangeListener(this);
		startFigure().removeDependendFigure(this);
		setStartConnector(null);
	}

	/**
	 * Disconnects the end figure.
	 */
	public void disconnectEnd() {
		handleDisconnect(startFigure(), endFigure());
		endFigure().removeFigureChangeListener(this);
		endFigure().removeDependendFigure(this);
		setEndConnector(null);
	}

	/**
	 * Tests whether a connection connects the same figures
	 * as another ConnectionFigure.
	 */
	public boolean connectsSame(ConnectionFigure other) {
		return other.getStartConnector() == getStartConnector()
			&& other.getEndConnector() == getEndConnector();
	}

	/**
	 * Handles the disconnection of a connection.
	 * Override this method to handle this event.
	 */
	protected void handleDisconnect(Figure start, Figure end) {}

	/**
	 * Handles the connection of a connection.
	 * Override this method to handle this event.
	 */
	protected void handleConnect(Figure start, Figure end) {}

	/**
	 * Gets the start figure of the connection.
	 */
	public Figure startFigure() {
		if (getStartConnector() != null) {
			return getStartConnector().owner();
		}
		return null;
	}

	/**
	 * Gets the end figure of the connection.
	 */
	public Figure endFigure() {
		if (getEndConnector() != null) {
			return getEndConnector().owner();
		}
		return null;
	}

	protected void setStartConnector(Connector newStartConnector) {
		myStartConnector = newStartConnector;
	}

	/**
	 * Gets the start figure of the connection.
	 */
	public Connector getStartConnector() {
		return myStartConnector;
	}

	protected void setEndConnector(Connector newEndConnector) {
		myEndConnector = newEndConnector;
	}

	/**
	 * Gets the end figure of the connection.
	 */
	public Connector getEndConnector() {
		return myEndConnector;
	}

	/**
	 * Tests whether two figures can be connected.
	 */
	public boolean canConnect(Figure start, Figure end) {
		return true;
	}

	/**
	 * Sets the start point.
	 */
	public void startPoint(int x, int y) {
		willChange();
		if (fPoints.size() == 0) {
			fPoints.add(new Point(x, y));
		}
		else {
			fPoints.set(0, new Point(x, y));
		}
		changed();
	}

	/**
	 * Sets the end point.
	 */
	public void endPoint(int x, int y) {
		willChange();
		if (fPoints.size() < 2) {
			fPoints.add(new Point(x, y));
		}
		else {
			fPoints.set(fPoints.size()-1, new Point(x, y));
		}
		changed();
	}

	/**
	 * Gets the start point.
	 */
	public Point startPoint() {
		Point p = pointAt(0);
		return new Point(p.x, p.y);
	}

	/**
	 * Gets the end point.
	 */
	public Point endPoint() {
		if (fPoints.size() > 0) {
			Point p = pointAt(fPoints.size()-1);
			return new Point(p.x, p.y);
		}
		else {
			return null;
		}
	}

	/**
	 * Gets the handles of the figure. It returns the normal
	 * PolyLineHandles but adds ChangeConnectionHandles at the
	 * start and end.
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList(fPoints.size());
		handles.add(new ChangeConnectionStartHandle(this));
		for (int i = 1; i < fPoints.size()-1; i++) {
			handles.add(new PolyLineHandle(this, locator(i), i));
		}
		handles.add(new ChangeConnectionEndHandle(this));
		return new HandleEnumerator(handles);
	}

	/**
	 * Sets the point and updates the connection.
	 */
	public void setPointAt(Point p, int i) {
		super.setPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Inserts the point and updates the connection.
	 */
	public void insertPointAt(Point p, int i) {
		super.insertPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Removes the point and updates the connection.
	 */
	public void removePointAt(int i) {
		super.removePointAt(i);
		layoutConnection();
	}

	/**
	 * Updates the connection.
	 */
	public void updateConnection() {
		if (getStartConnector() != null) {
			Point start = getStartConnector().findStart(this);

			if (start != null) {
				startPoint(start.x, start.y);
			}
		}
		if (getEndConnector() != null) {
			Point end = getEndConnector().findEnd(this);

			if (end != null) {
				endPoint(end.x, end.y);
			}
		}
	}

	/**
	 * Lays out the connection. This is called when the connection
	 * itself changes. By default the connection is recalculated
	 */
	public void layoutConnection() {
		updateConnection();
	}

	public void figureChanged(FigureChangeEvent e) {
		updateConnection();
	}

	public void figureRemoved(FigureChangeEvent e) {
	}

	public void figureRequestRemove(FigureChangeEvent e) {
	}

	public void figureInvalidated(FigureChangeEvent e) {
	}

	public void figureRequestUpdate(FigureChangeEvent e) {
	}

	public void release() {
		super.release();
		handleDisconnect(startFigure(), endFigure());
		if (getStartConnector() != null) {
			startFigure().removeFigureChangeListener(this);
			startFigure().removeDependendFigure(this);
		}
		if (getEndConnector() != null) {
			endFigure().removeFigureChangeListener(this);
			endFigure().removeDependendFigure(this);
		}
	}

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeStorable(getStartConnector());
		dw.writeStorable(getEndConnector());
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		Connector start = (Connector)dr.readStorable();
		if (start != null) {
			connectStart(start);
		}
		Connector end = (Connector)dr.readStorable();
		if (end != null) {
			connectEnd(end);
		}
		if ((start != null) && (end != null)) {
			updateConnection();
		}
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		if (getStartConnector() != null) {
			connectStart(getStartConnector());
		}
		if (getEndConnector() != null) {
			connectEnd(getEndConnector());
		}
	}

	public void visit(FigureVisitor visitor) {
		visitor.visitFigure(this);
	}

	/**
	 * @see org.jhotdraw.framework.Figure#removeFromContainer(org.jhotdraw.framework.FigureChangeListener)
	 */
	public void removeFromContainer(FigureChangeListener c) {
		super.removeFromContainer(c);
		release();
	}

}
