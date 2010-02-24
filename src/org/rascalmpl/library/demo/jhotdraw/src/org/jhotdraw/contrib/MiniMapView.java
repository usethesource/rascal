/*
 * @(#)MiniMapView.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.DrawingChangeEvent;
import org.jhotdraw.framework.DrawingChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Utility component for enhancing component scrolling.  It provides a "minature" view of the entire contents of a JScrollPane
 * and the means to scroll that JScrollPane by moving a rectangle representing the viewable area of the JScrollPane.  If the
 * user clicks on an area inside of the MiniMapView, the JScrollPane's view is centered on that area.  The user may also drag
 * the rectangle representation of the JScrollPane's view around the MiniMap to scroll.
 *
 * @author	S. Ruman (sruman@rogers.com)
 * @version <$CURRENT_VERSION$>
 */
public class MiniMapView extends JComponent {
// Instance Variables
	private JScrollPane m_subject;
	private DrawingView myMappedDrawingView;
	private SubjectListener m_subjectListener;
	private DrawingChangeListener myDrawingChangeListener;
	private Color m_viewBoxColor = Color.red;

// Constructors
	public MiniMapView(DrawingView newMappedDrawingView, JScrollPane subject) {
		m_subjectListener = new SubjectListener();
		setSubject(subject);
		setMappedDrawingView(newMappedDrawingView);
		myDrawingChangeListener = new MappedDrawingChangeListener();
		getMappedDrawingView().drawing().addDrawingChangeListener(myDrawingChangeListener);
		MouseListener ml = new MouseListener();
		addMouseListener(new MouseListener());
		addMouseMotionListener(ml);
	}

	protected void setMappedDrawingView(DrawingView newMappedDrawingView) {
		myMappedDrawingView = newMappedDrawingView;
	}

	public DrawingView getMappedDrawingView() {
		return myMappedDrawingView;
	}

// accessors
	protected void setSubject(JScrollPane subject) {
		if (m_subject != null) {
			m_subject.getViewport().removeChangeListener(m_subjectListener);
		}

		m_subject = subject;
		if (m_subject != null) {
			m_subject.getViewport().addChangeListener(m_subjectListener);
		}

		repaint();
	}

	public JScrollPane getSubject() {
		return m_subject;
	}

	public Color getViewBowColor() {
		return m_viewBoxColor;
	}

	public void setViewBoxColor(Color c) {
		m_viewBoxColor = c;
		repaint();
	}

	/**
	 * @return	The component that is actually being "mini-mapped", that is the component inside the scroll pane
	 */
	protected Component getMappedComponent() {
		return (Component)getMappedDrawingView();
	}

// Overridden
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		// Paint a small map representation of the subjects contents
		Component mappedComponent = getMappedComponent();
		AffineTransform at = getViewToMiniMapTransform(mappedComponent);
		g2d.transform(at);

		getMappedDrawingView().drawAll(g2d);

		// Draw a rectangle representing the viewable area
		drawViewRectangle(g2d, getViewRectangle());
	}

	// side-effect of setting the transform on g2d to identity
	protected void drawViewRectangle(Graphics2D g2d, Rectangle viewPortRectangle) {
		AffineTransform at = new AffineTransform();
		at.setToIdentity();
		g2d.setTransform(at);

		g2d.setColor(m_viewBoxColor);
		g2d.draw(viewPortRectangle);
	}

	protected AffineTransform getViewToMiniMapTransform(Component mappedComponent) {
		double scaleX = ((double)getWidth()) / ((double)mappedComponent.getWidth());
		double scaleY = ((double)getHeight()) / ((double)mappedComponent.getHeight());

		AffineTransform at = getInverseSubjectTransform();		// for subclass flexibility
		at.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
		return at;
	}

	/**
	 * Allows subclasses to modify the transformation used in creating the mini-map
	 */
	protected AffineTransform getInverseSubjectTransform() {
		AffineTransform at = new AffineTransform();
		at.setToIdentity();
		return at;
	}

	/**
	 * @return	The rectangle (in Mini-Map world-coordinates) representing the area being viewed inside of the scroll pane subject
	 */
	protected Rectangle getViewRectangle() {
		Rectangle visiblePortion = m_subject.getViewportBorderBounds();
		Point upperLeftViewPos = m_subject.getViewport().getViewPosition();
		double [] srcRecCorners = new double[4];
		double [] dstRecCorners = new double[4];

		srcRecCorners[0] = upperLeftViewPos.x + visiblePortion.getX(); srcRecCorners[1] = upperLeftViewPos.y + visiblePortion.getY(); srcRecCorners[2] = upperLeftViewPos.x + visiblePortion.getX() + visiblePortion.getWidth(); srcRecCorners[3] = upperLeftViewPos.y + visiblePortion.getY() + visiblePortion.getHeight();
		getViewToMiniMapTransform(getMappedComponent()).transform(srcRecCorners, 0, dstRecCorners, 0, srcRecCorners.length/2);	// transform the coordinates to MiniMapView coordinates

		return new Rectangle((int)dstRecCorners[0], (int)dstRecCorners[1], (int)(dstRecCorners[2] - dstRecCorners[0]), (int)(dstRecCorners[3] - dstRecCorners[1]));
	}

	/**
	 * Scrolls the subject scroll pane to the coordinates specified.
	 * @param		upperLeftX		The new upper left corner X-coordinate (in subject world-coordinates) of the subject scroll pane
	 * @param		upperLeftY		The new upper left corner Y-coordinate (in subject world-coordinates) of the subject scroll pane
	 */
	protected void scrollSubjectTo(int upperLeftX, int upperLeftY) {
		AffineTransform at = null;
		try {
			at = getViewToMiniMapTransform(getMappedComponent()).createInverse();
		}
		catch (NoninvertibleTransformException nite) {
			nite.printStackTrace();
			return;
		}

		double [] srcPoints = new double[2];
		double [] destPoints = new double[2];
		srcPoints[0] = upperLeftX;
		srcPoints[1] = upperLeftY;
		at.transform(srcPoints, 0, destPoints, 0, 1);

		if (destPoints[0] < 0) {
			destPoints[0] = 0;
		}
		if (destPoints[1] < 0) {
			destPoints[1] = 0;
		}

		m_subject.getViewport().setViewPosition(new Point((int)destPoints[0], (int)destPoints[1]));
	}

	protected int [] getUpperLeftPointsFromCenter(int centerX, int centerY) {
		int [] upperLeft = new int[2];
		Rectangle oldRectangle = getViewRectangle();
		upperLeft[0] = centerX - oldRectangle.width/2;
		upperLeft[1] = centerY - oldRectangle.height/2;

        /*
         * JP, 25-May-03: Avoid positioning of the rectangle outside the
         * available area. Resulted in very strange artifacts on the screen.
         */
        if (upperLeft[0] + oldRectangle.width > getX() + getWidth()) {
            upperLeft[0] = getX() + getWidth() - oldRectangle.width;
        }

        if (upperLeft[1] + oldRectangle.height > getY() + getHeight()) {
            upperLeft[1] = getY() + getHeight() - oldRectangle.height;
        }
        
		return upperLeft;
	}

	// Inner Classes
	public class MouseListener extends MouseAdapter implements MouseMotionListener  {
		public void mousePressed(MouseEvent e) {
			int [] rectangleUpperLeft = getUpperLeftPointsFromCenter(e.getX(), e.getY());
			scrollSubjectTo(rectangleUpperLeft[0], rectangleUpperLeft[1]);
		}

		public void mouseDragged(MouseEvent e) {
			int [] rectangleUpperLeft = getUpperLeftPointsFromCenter(e.getX(), e.getY());
			scrollSubjectTo(rectangleUpperLeft[0], rectangleUpperLeft[1]);
		}

		public void mouseMoved(MouseEvent e) {
			// empty implementation
		}
	}

	class SubjectListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			repaint();
		}
	}

	class MappedDrawingChangeListener implements DrawingChangeListener {
		/**
		 *  Sent when an area is invalid
		 */
		public void drawingInvalidated(DrawingChangeEvent e) {
			repaint();
		}

		/**
		 *  Sent when the drawing wants to be refreshed
		 */
		public void drawingRequestUpdate(DrawingChangeEvent e) {
			repaint();
		}
        
        /**  
         *  Sent when the drawing Title has changed
         */
        public void drawingTitleChanged(DrawingChangeEvent e) {
        }
	}
}