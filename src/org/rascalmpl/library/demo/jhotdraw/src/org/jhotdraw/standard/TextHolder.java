/*
 * @(#)TextHolder.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.*;
import org.jhotdraw.framework.*;

/**
 * The interface of a figure that has some editable text contents.
 *
 * @see Figure
 *
 * @version <$CURRENT_VERSION$>
 */

public interface TextHolder {

	public Rectangle textDisplayBox();

	/**
	 * Gets the text shown by the text figure.
	 */
	public String getText();

	/**
	 * Sets the text shown by the text figure.
	 */
	public void setText(String newText);

	/**
	 * Tests whether the figure accepts typing.
	 */
	public boolean acceptsTyping();

	/**
	 * Gets the number of columns to be overlaid when the figure is edited.
	 */
	public int overlayColumns();

	/**
	 * Connects a text holder to another figure.
	 */
	public void connect(Figure connectedFigure);

	/**
	 * Disconnects a text holder from a connect figure.
	 */
	public void disconnect(Figure disconnectFigure);
	
	/**
	 * Gets the font.
	 */
	public Font getFont();

	/**
	 * Usually, a TextHolders is implemented by a Figure subclass. To avoid casting
	 * a TextHolder to a Figure this method can be used for polymorphism (in this
	 * case, let the (same) object appear to be of another type).
	 * Note, that the figure returned is not the figure to which the TextHolder is
	 * (and its representing figure) connected.
	 * @return figure responsible for representing the content of this TextHolder
	 */
	public Figure getRepresentingFigure();
}
