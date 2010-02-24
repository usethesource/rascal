/*
 * @(#)PaletteIcon.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.awt.*;

/**
 * A three state icon that can be used in Palettes.
 *
 * @see PaletteButton
 *
 * @version <$CURRENT_VERSION$>
 */
public  class PaletteIcon extends Object {

	Image       fNormal;
	Image       fPressed;
	Image       fSelected;
	Dimension   fSize;

	public PaletteIcon(Dimension size, Image normal, Image pressed, Image selected) {
		fSize = size;
		fNormal = normal;
		fPressed = pressed;
		fSelected = selected;
	}

	public Image normal() { return fNormal; }
	public Image pressed() { return fPressed; }
	public Image selected() { return fSelected; }

	public int getWidth() { return fSize.width; }
	public int getHeight() { return fSize.height; }

}
