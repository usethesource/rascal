/*
 * @(#)Filler.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import javax.swing.JPanel;
import java.awt.*;

/**
 * A component that can be used to reserve white space in a layout.
 *
 * @version <$CURRENT_VERSION$>
 */
public  class Filler
		extends JPanel {

	private int     fWidth;
	private int     fHeight;
	private Color   fBackground;


	public Filler(int width, int height) {
		this(width, height, null);
	}

	public Filler(int width, int height, Color background) {
		fWidth = width;
		fHeight = height;
		fBackground = background;
	}

	public Dimension getMinimumSize() {
		return new Dimension(fWidth, fHeight);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Color getBackground() {
		if (fBackground != null) {
			return fBackground;
		}
		return super.getBackground();
	}
}

