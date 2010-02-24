/*
 * @(#)OrderFigureElement.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.Figure;

/**
 * @author WMG (INIT Copyright (C) 2000 All rights reserved)
 * @version <$CURRENT_VERSION$>
 */
class OrderedFigureElement implements Comparable {

	//_________________________________________________________VARIABLES

	private Figure  _theFigure;
	private int     _nZ;

	//______________________________________________________CONSTRUCTORS

	public OrderedFigureElement(Figure aFigure, int nZ) {
		_theFigure = aFigure;
		_nZ = nZ;
	}

	//____________________________________________________PUBLIC METHODS

	public Figure getFigure() {
		return _theFigure;
	}

	public int getZValue() {
		return _nZ;
	}

	public int compareTo(Object o) {
		OrderedFigureElement ofe = (OrderedFigureElement) o;
		if (_nZ == ofe.getZValue()) {
			return 0;
		}

		if (_nZ > ofe.getZValue()) {
			return 1;
		}

		return -1;
	}

	//_______________________________________________________________END

} //end of class OrderedFigureElement
