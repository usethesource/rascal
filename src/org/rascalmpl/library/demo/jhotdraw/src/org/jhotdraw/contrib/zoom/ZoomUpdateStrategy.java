/*
 * @(#)ZoomUpdateStrategy.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.zoom;

import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.Painter;

import java.awt.*;

/**
 * A variant of the BufferedUpdateStrategy that handles clipping
 * rectangles correctly in the presence of zooming.
 * @see org.jhotdraw.standard.BufferedUpdateStrategy
 *
 * @author Andre Spiegel <spiegel@gnu.org>
 * @version <$CURRENT_VERSION$>
 */
public class ZoomUpdateStrategy implements Painter {

	/**
	 * The offscreen image
	 */
	transient private Image fOffscreen;
	private int fImagewidth = -1;
	private int fImageheight = -1;

	/**
	 * Draws the view contents.
	 */
	public void draw(Graphics g, DrawingView view) {
		// create the buffer if necessary
		Dimension d = view.getSize();
		if ((fOffscreen == null) || (d.width != fImagewidth)
				|| (d.height != fImageheight)) {
			fOffscreen = view.createImage(d.width, d.height);
			fImagewidth = d.width;
			fImageheight = d.height;
		}

		Graphics g2 = fOffscreen.getGraphics();
		Rectangle r = g.getClipBounds();

		if (g2 instanceof ScalingGraphics) {
			ScalingGraphics s2 = (ScalingGraphics) g2;

			// AWT sets clipping rectangles in screen coordinates, not user
			// coordinates.  Therefore, we scale the clipping rectangle to
			// user coordinates here, and then apply it to both buffers,
			// which scale it back to screen coordinates.
			if (r != null) {
				// Make the rectangle slightly larger, to compensate
				// for integer rounding errors.
				r = new Rectangle((int) ((r.x - 2) / s2.getScale()),
						(int) ((r.y - 2) / s2.getScale()),
						(int) ((r.width + 4) / s2.getScale()),
						(int) ((r.height + 4) / s2.getScale()));
				g.setClip(r);
			}
		}
		g2.setClip(r);

		view.drawAll(g2);
		g.drawImage(fOffscreen, 0, 0, view);
	}
}
