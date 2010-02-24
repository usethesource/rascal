/*
 * @(#)PertFigureCreationTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.pert;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;

/**
 * A more efficient version of the generic Pert creation
 * tool that is not based on cloning.
 *
 * @version <$CURRENT_VERSION$>
 */
public  class PertFigureCreationTool extends CreationTool {

	public PertFigureCreationTool(DrawingEditor newDrawingEditor) {
		super(newDrawingEditor);
	}

	/**
	 * Creates a new PertFigure.
	 */
	protected Figure createFigure() {
		return new PertFigure();
	}
}
