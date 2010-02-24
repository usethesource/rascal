/*
 * @(#)SingleFigureEnumerator.java
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

/**
 * An Enumeration that contains only a single Figures. An instance of this
 * enumeration can be used only once to retrieve the figure as the figure
 * is forgotten after the first retrieval.
 *
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public final class SingleFigureEnumerator implements FigureEnumeration {
	private Figure mySingleFigure;
	private Figure myInitialFigure;

	public SingleFigureEnumerator(Figure newSingleFigure) {
		myInitialFigure = newSingleFigure;
		reset();
	}

	/**
	 * Returns true if the enumeration contains more elements; false
	 * if its empty.
	 */
	public boolean hasNextFigure() {
		return mySingleFigure != null;
	}

	/**
	 * Returns the next element of the enumeration. Calls to this
	 * method will enumerate successive elements.
	 * @exception java.util.NoSuchElementException If no more elements exist.
	 */
	public Figure nextFigure() {
		Figure returnFigure = mySingleFigure;
		mySingleFigure = null;
		return returnFigure;
	}

	/**
	 * Reset the enumeration so it can be reused again. However, the
	 * underlying collection might have changed since the last usage
	 * so the elements and the order may vary when using an enumeration
	 * which has been reset.
	 */
	public void reset() {
		mySingleFigure = myInitialFigure;
	}
}
