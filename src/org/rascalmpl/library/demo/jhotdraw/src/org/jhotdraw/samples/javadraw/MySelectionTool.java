/*
 * @(#)MySelectionTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.javadraw;

import java.awt.event.MouseEvent;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;

/**
 * A SelectionTool that interprets double clicks to inspect the clicked figure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class MySelectionTool extends SelectionTool {

	public MySelectionTool(DrawingEditor newDrawingEditor) {
		super(newDrawingEditor);
	}

	/**
	 * Handles mouse down events and starts the corresponding tracker.
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
		setView((DrawingView)e.getSource());
		if (e.getClickCount() == 2) {
			Figure figure = drawing().findFigure(e.getX(), e.getY());
			if (figure != null) {
				inspectFigure(figure);
				return;
			}
		}
		super.mouseDown(e, x, y);
	}

	protected void inspectFigure(Figure f) {
		System.out.println("inspect figure"+f);
	}
}
