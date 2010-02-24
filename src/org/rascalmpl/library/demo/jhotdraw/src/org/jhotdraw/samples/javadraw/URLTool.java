/*
 * @(#)URLTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.javadraw;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.FloatingTextField;
import java.awt.*;
import java.awt.event.*;

/**
 * A tool to attach URLs to figures.
 * The URLs are stored in the figure's "URL" attribute.
 * The URL text is entered with a FloatingTextField.
 *
 * @see org.jhotdraw.util.FloatingTextField
 *
 * @version <$CURRENT_VERSION$>
 */
public  class URLTool extends AbstractTool {

	private FloatingTextField   fTextField;
	private Figure              fURLTarget;

	public URLTool(DrawingEditor newDrawingEditor) {
		super(newDrawingEditor);
	}

	public void mouseDown(MouseEvent e, int x, int y)
	{
		super.mouseDown(e,x,y);
		Figure pressedFigure = drawing().findFigureInside(x, y);
		if (pressedFigure != null) {
			beginEdit(pressedFigure);
			return;
		}
		endEdit();
		editor().toolDone();
	}

	public void mouseUp(MouseEvent e, int x, int y) {
	}

	public void deactivate(DrawingView view) {
		super.deactivate();
		endEdit();
	}

	private void beginEdit(Figure figure) {
		if (fTextField == null) {
			fTextField = new FloatingTextField();
			fTextField.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						endEdit();
					}
				}
			);
		}

		if ((figure != fURLTarget) && (fURLTarget != null)) {
			endEdit();
		}
		if (figure != fURLTarget) {
			fTextField.createOverlay((Container)view());
			fTextField.setBounds(fieldBounds(figure), getURL(figure));
			fURLTarget = figure;
		}
	}

	private void endEdit() {
		if (fURLTarget != null) {
			setURL(fURLTarget, fTextField.getText());
			fURLTarget = null;
			fTextField.endOverlay();
		}
	}

	private Rectangle fieldBounds(Figure figure) {
		Rectangle box = figure.displayBox();
		int nChars = Math.max(20, getURL(figure).length());
		Dimension d = fTextField.getPreferredSize(nChars);
		box.x = Math.max(0, box.x + (box.width - d.width)/2);
		box.y = Math.max(0, box.y + (box.height - d.height)/2);
		return new Rectangle(box.x, box.y, d.width, d.height);
	}

	private String getURL(Figure figure) {
		String url = (String) figure.getAttribute(FigureAttributeConstant.URL);
		if (url == null) {
			url = "";
		}
		return url;
	}

	private void setURL(Figure figure, String url) {
		figure.setAttribute(FigureAttributeConstant.URL, url);
	}
}
