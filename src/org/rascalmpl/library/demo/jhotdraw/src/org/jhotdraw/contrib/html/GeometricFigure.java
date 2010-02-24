/*
 * @(#)GeometricFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Shape;

import org.jhotdraw.framework.Figure;

/**
 * GeometricFigure defines required of figures that can be used as shape providers
 * for other figures. See HTMLTextArea for an example.
 *
 * @author  Eduardo Francos - InContext
 * @created 1 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public interface GeometricFigure extends Figure {

	/**
	 * Returns a shape with the figures area outline
	 *
	 * @return   The shape
	 */
	public Shape getShape();
}
