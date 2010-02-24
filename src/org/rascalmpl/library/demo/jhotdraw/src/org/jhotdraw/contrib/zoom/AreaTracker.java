/*
 * @(#)AreaTracker.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.zoom;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.standard.AbstractTool;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A rubberband area tracker.  It can be extended to do anything with
 * the resulting area, e.g. select it or zoom it.  This code is
 * derived from SelectAreaTracker, which is a bit too specific to
 * allow for extension.
 *
 * @author Andre Spiegel <spiegel@gnu.org>
 * @version <$CURRENT_VERSION$>
 */
public abstract class AreaTracker extends AbstractTool {

	private Rectangle area;

	protected AreaTracker(DrawingEditor editor) {
		super(editor);
	}

	public Rectangle getArea() {
		return new Rectangle(area.x, area.y, area.width, area.height);
	}

	public void mouseDown(MouseEvent e, int x, int y) {
		// use event coordinates to supress any kind of
		// transformations like constraining points to a grid
		super.mouseDown(e, e.getX(), e.getY());
		rubberBand(getAnchorX(), getAnchorY(), getAnchorX(), getAnchorY());
	}

	public void mouseDrag(MouseEvent e, int x, int y) {
		super.mouseDrag(e, x, y);
		eraseRubberBand();
		rubberBand(getAnchorX(), getAnchorY(), x, y);
	}

	public void mouseUp(MouseEvent e, int x, int y) {
		super.mouseUp(e, x, y);
		eraseRubberBand();
	}

	private void rubberBand(int x1, int y1, int x2, int y2) {
		area = new Rectangle(new Point(x1, y1));
		area.add(new Point(x2, y2));
		drawXORRect(area);
	}

	private void eraseRubberBand() {
		drawXORRect(area);
	}

	private void drawXORRect(Rectangle r) {
		Graphics g = view().getGraphics();
		g.setXORMode(view().getBackground());
		g.setColor(Color.black);
		g.drawRect(r.x, r.y, r.width, r.height);
	}

}
