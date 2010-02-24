/*
 * @(#)MiniMapDesktop.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.samples.minimap;

import org.jhotdraw.contrib.SplitPaneDesktop;
import org.jhotdraw.contrib.MiniMapView;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.util.Iconkit;
import org.jhotdraw.figures.ImageFigure;

import javax.swing.*;
import java.awt.*;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class MiniMapDesktop extends SplitPaneDesktop {

	private String imageName = "/CH/ifa/draw/samples/javadraw/sampleimages/view.gif";

	protected Component createRightComponent(DrawingView view) {
		Image image = Iconkit.instance().registerAndLoadImage(
			(Component)view, imageName);
		view.add(new ImageFigure(image, imageName, new Point(0,0)));
		view.checkDamage();
//		((org.jhotdraw.standard.StandardDrawingView)view).checkMinimumSize();
		return super.createRightComponent(view);
	}

	protected Component createLeftComponent(DrawingView view) {
		JPanel blankPanel = new JPanel();
//		blankPanel.setPreferredSize(new Dimension(200, 200));

		MiniMapView mmv = new MiniMapView(view, (JScrollPane)getRightComponent());
//		mmv.setPreferredSize(new Dimension(200, 200));

		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blankPanel, mmv);
		leftSplitPane.setOneTouchExpandable(true);
		leftSplitPane.setDividerLocation(200);
//		leftSplitPane.setPreferredSize(new Dimension(200, 400));
//		leftSplitPane.resetToPreferredSizes();

		return leftSplitPane;
	}
}
