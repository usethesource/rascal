/*
 * @(#)ChangeConnectionEndHandle.java
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
import org.jhotdraw.util.Undoable;
import java.awt.Point;

/**
 * A handle to reconnect the end point of
 * a connection to another figure.
 *
 * @version <$CURRENT_VERSION$>
 */
public class ChangeConnectionEndHandle extends ChangeConnectionHandle {

	/**
	 * Constructs the connection handle.
	 */
	public ChangeConnectionEndHandle(ConnectionFigure owner) {
		super(owner);
	}

	/**
	 * Gets the end figure of a connection.
	 */
	protected Connector target() {
		return getConnection().getEndConnector();
	}

	/**
	 * Disconnects the end figure.
	 */
	protected void disconnect() {
		getConnection().disconnectEnd();
	}

	/**
	 * Sets the end of the connection.
	 */
	protected void connect(Connector c) {
		getConnection().connectEnd(c);
	}

	/**
	 * Sets the end point of the connection.
	 */
	protected void setPoint(int x, int y) {
		getConnection().endPoint(x, y);
	}

	/**
	 * Returns the end point of the connection.
	 */
	public Point locate() {
		return getConnection().endPoint();
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new ChangeConnectionEndHandle.UndoActivity(newView);
	}

	public static class UndoActivity extends ChangeConnectionHandle.UndoActivity {
		public UndoActivity(DrawingView newView) {
			super(newView);
		}

		protected Connector replaceConnector(ConnectionFigure connection) {
			Connector tempEndConnector = connection.getEndConnector();
			connection.connectEnd(getOldConnector());
			return tempEndConnector;
		}
	}

    protected boolean canConnectTo(Figure figure) {
        return getConnection().canConnect(source().owner(), figure);
	}
}
