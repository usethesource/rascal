/*
 * @(#)MiniMapZoomableView.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.zoom;

import org.jhotdraw.contrib.MiniMapView;
import org.jhotdraw.framework.DrawingView;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.JScrollPane;

/**
 * Specialized sub-class of MiniMapView to handle the zooming ability of the ZoomDrawingView in JHotDraw.  This subclass has been enhanced
 * to take into consideration that the scrollpane's content may itself be altered by a transform (namely a scaling transform).
 *
 * @author	S. Ruman (sruman@rogers.com)
 * @version <$CURRENT_VERSION$>
 */
public class MiniMapZoomableView extends MiniMapView {
	public MiniMapZoomableView(DrawingView newMappedDrawingView, JScrollPane subject) {
		super(newMappedDrawingView, subject);
	}

// Overridden
	public AffineTransform getInverseSubjectTransform() {
		double subjectsScale = ((ZoomDrawingView)getMappedComponent()).getScale();

		AffineTransform at = null;
		try {
			at = AffineTransform.getScaleInstance(subjectsScale, subjectsScale).createInverse();   // undo the zoom of the zoomable drawing view
		}
		catch (NoninvertibleTransformException nte) {
			// all scale-only transforms should be invertable
		}

		return at;
	}
}
