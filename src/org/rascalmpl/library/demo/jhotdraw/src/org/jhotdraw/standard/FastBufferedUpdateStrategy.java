/*
 * @(#)FastBufferedUpdateStrategy.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.jhotdraw.framework.Drawing;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.framework.Painter;

/**
 * @author WMG (INIT Copyright (C) 2000 All rights reserved)
 * @version <$CURRENT_VERSION$>
 */
public class FastBufferedUpdateStrategy implements Painter {

	//_________________________________________________________VARIABLES

	private BufferedImage  _doubleBufferedImage;
	private BufferedImage  _scratchPadBufferedImage;
	private int            _nImageWidth = 0;
	private int            _nImageHeight = 0;
	private boolean        _bRedrawAll = true;

	//______________________________________________________CONSTRUCTORS

	public FastBufferedUpdateStrategy() {
	}

	//____________________________________________________PUBLIC METHODS

	public void draw(Graphics g, DrawingView view) {
		_checkCaches(view);
		if (_bRedrawAll == true) {
			Graphics imageGraphics = _doubleBufferedImage.getGraphics();
			view.drawAll(imageGraphics);
		}
		else {
			Rectangle viewClipRectangle = g.getClipBounds();
			int nX1 = viewClipRectangle.x;
			int nY1 = viewClipRectangle.y;
			int nX2 = viewClipRectangle.x + viewClipRectangle.width;
			int nY2 = viewClipRectangle.y + viewClipRectangle.height;

			if (nX1 < 0) {
				nX1 = 0;
			}
			if (nY1 < 0) {
				nY1 = 0;
			}
			if (nX2 < 0) {
				nX2 = 0;
			}
			if (nY2 < 0) {
				nY2 = 0;
			}

			Rectangle viewClipRectangle2 = new Rectangle(nX1, nY1, nX2-nX1, nY2-nY1);

			Drawing theDrawing = view.drawing();
			FigureEnumeration fe = theDrawing.figures(viewClipRectangle2);

			Graphics imageGraphics = _scratchPadBufferedImage.getGraphics();
			imageGraphics.setColor(view.getBackground());
			imageGraphics.fillRect(nX1, nY1, nX2-nX1, nY2-nY1);
			view.draw(imageGraphics, fe);

			Graphics dbGraphics = _doubleBufferedImage.getGraphics();
			dbGraphics.drawImage(_scratchPadBufferedImage, nX1, nY1,
				nX2, nY2, nX1, nY1, nX2, nY2, view);

		}

		g.drawImage(_doubleBufferedImage, 0, 0, view);
		_bRedrawAll = false;
	}


	//___________________________________________________PRIVATE METHODS


	private void _checkCaches(DrawingView view) {
		Dimension d = view.getSize();

		if ((_doubleBufferedImage == null)
				|| (_nImageWidth != d.width)
				|| (_nImageHeight != d.height)) {
			_doubleBufferedImage = new BufferedImage(d.width,
			d.height, BufferedImage.TYPE_INT_RGB);
			_bRedrawAll = true;
		}

		if ((_scratchPadBufferedImage == null)
				|| (_nImageWidth != d.width)
				|| (_nImageHeight != d.height)) {
			_scratchPadBufferedImage = new BufferedImage(d.width,
				d.height, BufferedImage.TYPE_INT_RGB);
			Graphics imageGraphics = _scratchPadBufferedImage.getGraphics();
			view.drawBackground(imageGraphics);
			_bRedrawAll = true;
		}

		_nImageWidth = d.width;
		_nImageHeight = d.height;
	}

	//_______________________________________________________________END

} //end of class FastBufferedUpdateStrategy
