/*
 * @(#)ChangeConnectionStartHandle.java
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
 * Handle to reconnect the
 * start of a connection to another figure.
 *
 * @version <$CURRENT_VERSION$>
 */
public class ChangeConnectionStartHandle extends ChangeConnectionHandle {

	/**
	 * Constructs the connection handle for the given start figure.
	 */
	public ChangeConnectionStartHandle(ConnectionFigure owner) {
		super(owner);
	}

	/**
	 * Gets the start figure of a connection.
	 */
	protected Connector target() {
		return getConnection().getStartConnector();
	}

	/**
	 * Disconnects the start figure.
	 */
	protected void disconnect() {
		getConnection().disconnectStart();
	}

	/**
	 * Sets the start of the connection.
	 */
	protected void connect(Connector c) {
		getConnection().connectStart(c);
	}

	/**
	 * Sets the start point of the connection.
	 */
	protected void setPoint(int x, int y) {
		getConnection().startPoint(x, y);
	}

	/**
	 * Returns the start point of the connection.
	 */
	public Point locate() {
		return getConnection().startPoint();
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new ChangeConnectionStartHandle.UndoActivity(newView);
	}

	public static class UndoActivity extends ChangeConnectionHandle.UndoActivity {
		public UndoActivity(DrawingView newView) {
			super(newView);
		}
		
		protected Connector replaceConnector(ConnectionFigure connection) {
			Connector tempStartConnector = connection.getStartConnector();
			connection.connectStart(getOldConnector());
			return tempStartConnector;
		}
	}
    
    protected boolean canConnectTo(Figure figure) {
        return getConnection().canConnect(figure, source().owner());
}
}
