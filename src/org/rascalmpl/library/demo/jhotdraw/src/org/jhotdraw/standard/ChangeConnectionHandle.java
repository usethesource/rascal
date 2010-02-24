/*
 * @(#)ChangeConnectionHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;
import java.awt.*;

/**
 * ChangeConnectionHandle factors the common code for handles
 * that can be used to reconnect connections.
 *
 * @see ChangeConnectionEndHandle
 * @see ChangeConnectionStartHandle
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class ChangeConnectionHandle extends AbstractHandle {

	private Connector         fOriginalTarget;
	private Figure            myTarget;
	private ConnectionFigure  myConnection;
	private Point             fStart;

	/**
	 * Initializes the change connection handle.
	 */
	protected ChangeConnectionHandle(ConnectionFigure owner) {
		super(owner);
		setConnection(owner);
		setTargetFigure(null);
	}

	/**
	 * Returns the target connector of the change.
	 */
	protected abstract Connector target();

	/**
	 * Disconnects the connection.
	 */
	protected abstract void disconnect();

	/**
	 * Connect the connection with the given figure.
	 */
	protected abstract void connect(Connector c);

	/**
	 * Sets the location of the target point.
	 */
	protected abstract void setPoint(int x, int y);

	/**
	 * Gets the side of the connection that is unaffected by
	 * the change.
	 */
	protected Connector source() {
		if (target() == getConnection().getStartConnector()) {
			return getConnection().getEndConnector();
		}
		return getConnection().getStartConnector();
	}

	/**
	 * Disconnects the connection.
	 */
	public void invokeStart(int  x, int  y, DrawingView view) {
		fOriginalTarget = target();
		fStart = new Point(x, y);

		setUndoActivity(createUndoActivity(view));
		((ChangeConnectionHandle.UndoActivity)getUndoActivity()).setOldConnector(target());

		disconnect();
	}

	/**
	 * Finds a new target of the connection.
	 */
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Point p = new Point(x, y);
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
		setPoint(p.x, p.y);
	}

	/**
	 * Connects the figure to the new target. If there is no
	 * new target the connection reverts to its original one.
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		Connector target = findConnectionTarget(x, y, view.drawing());
		if (target == null) {
			target = fOriginalTarget;
		}

		setPoint(x, y);
		connect(target);
		getConnection().updateConnection();

		Connector oldConnector = ((ChangeConnectionHandle.UndoActivity)
			getUndoActivity()).getOldConnector();
		// there has been no change so there is nothing to undo
		if ((oldConnector == null)
				|| (target() == null)
				|| (oldConnector.owner() == target().owner())) {
			setUndoActivity(null);
		}
		else {
			getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(getConnection()));
		}

		if (getTargetFigure() != null) {
			getTargetFigure().connectorVisibility(false, null);
			setTargetFigure(null);
		}
	}

	private Connector findConnectionTarget(int x, int y, Drawing drawing) {
		Figure target = findConnectableFigure(x, y, drawing);

		if ((target != null)
			&& target.canConnect()
			 && target != fOriginalTarget
			 && !target.includes(owner())
            /*
    		 * JP, 25-May-03: Fix for ignored direction when checking
    		 * connectability. Old version didn't take direction of
             * connection into account, which could result in incorrect
             * drawing if syntax rules were a concern.
             * 
             * See also new canConnectTo() method below.
             * 
             * Was:
             * 
    		 * && getConnection().canConnect(source().owner(), target)) {
    		 */
			&& canConnectTo(target)) {
				return findConnector(x, y, target);
		}
		return null;
	}

    /**
     * Called to check whether this end of the connection can connect to the
     * given target figure. Needs to be overriden by the start and end changers
     * to take the connection's direction into account during the check. JHD 5.4
     * beta and before did not do this.
     */
    protected abstract boolean canConnectTo(Figure figure);
    
	protected Connector findConnector(int x, int y, Figure f) {
		return f.connectorAt(x, y);
	}

	/**
	 * Draws this handle.
	 */
	public void draw(Graphics g) {
		Rectangle r = displayBox();

		g.setColor(Color.green);
		g.fillRect(r.x, r.y, r.width, r.height);

		g.setColor(Color.black);
		g.drawRect(r.x, r.y, r.width, r.height);
	}

	private Figure findConnectableFigure(int x, int y, Drawing drawing) {
		FigureEnumeration fe = drawing.figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (!figure.includes(getConnection()) && figure.canConnect()) {
				if (figure.containsPoint(x, y)) {
					return figure;
				}
			}
		}
		return null;
	}
	
	protected void setConnection(ConnectionFigure newConnection) {
		myConnection = newConnection;
	}
	
	protected ConnectionFigure getConnection() {
		return myConnection;
	}
	
	protected void setTargetFigure(Figure newTarget) {
		myTarget = newTarget;
	}
	
	protected Figure getTargetFigure() {
		return myTarget;
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected abstract Undoable createUndoActivity(DrawingView newView);
	
	public static abstract class UndoActivity extends UndoableAdapter {
		private Connector myOldConnector;
		
		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}
		
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			swapConnectors();
			return true;
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			swapConnectors();
			return true;
		}

		private void swapConnectors() {
			FigureEnumeration fe = getAffectedFigures();
			if (fe.hasNextFigure()) {
				ConnectionFigure connection = (ConnectionFigure)fe.nextFigure();
				setOldConnector(replaceConnector(connection));
				connection.updateConnection();
			}
		}

		protected abstract Connector replaceConnector(ConnectionFigure connection);
				
		public void setOldConnector(Connector newOldConnector) {
			myOldConnector = newOldConnector;
		}
		
		public Connector getOldConnector() {
			return myOldConnector;
		}
	}
}
