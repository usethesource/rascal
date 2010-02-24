/*
 * @(#)DNDInterface.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.dnd;

import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceListener;

/**
 * Interface for Components which wish to participate in drag and drop.
 *
 * A Component which wishes to participate in drag and drop should implement
 * this interface.  Once done the DragBDropTool will be able to transfer data
 * to and from the Component.  The Component will also be activated to receive
 * drops from extra-JVM sources according to the conditions the Component
 * specifies.
 *
 * @author  CL.Gilbert <dnoyeb@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public interface DNDInterface {
	public void DNDInitialize(DragGestureListener dgl);
	public void DNDDeinitialize();

	/**
	 * When the DNDTool starts a drag action, it queries the interface for the
	 * dragSourceListener of the source view.  It uses this listener to create
	 * the dragGestureEvent.
	 */
	public DragSourceListener getDragSourceListener();
}