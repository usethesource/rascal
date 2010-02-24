/*
 * @(#)HTMLTextAreaTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Font;
import org.jhotdraw.contrib.TextAreaTool;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.standard.TextHolder;

/**
 * HTMLTextAreaTool creates or edits HTMLTextAreaFigures.<br>
 * Only diffreence with TextAreaFigure is that this allows to edit HTML code
 * using a more suitable font than the one defined by the figure.
 *
 * @author  Eduardo Francos - InContext
 * @created 28 avril 2002
 * @version <$CURRENT_VERSION$>
 */
public class HTMLTextAreaTool extends TextAreaTool {

	/**
	 * Constructor for the TextAreaTool object
	 *
	 * @param newDrawingEditor  the managing drawing editor
	 * @param prototype         the prototype for the figure
	 */
	public HTMLTextAreaTool(DrawingEditor newDrawingEditor, Figure prototype) {
		super(newDrawingEditor, prototype);
	}

	/**
	 * Gets the font to be used for editing the figure.<br>
	 * Use a simple editing font, easier for HTML editing
	 *
	 * @param figure  the figure
	 * @return        The font
	 */
	protected Font getFont(TextHolder figure) {
		return new Font("Helvetica", Font.PLAIN, 12);
	}
}
