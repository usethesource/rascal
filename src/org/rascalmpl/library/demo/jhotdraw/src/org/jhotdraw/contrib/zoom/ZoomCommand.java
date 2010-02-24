/*
 *  @(#)ZoomCommand.java
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
import org.jhotdraw.framework.JHotDrawRuntimeException;

import org.jhotdraw.standard.AbstractCommand;

/**
 * A ZoomCommand allows for applying a zoom factor to a ZoomDrawingView.<br>
 * Several ZoomCommand objects can be created in a menu or toolbar, set to various
 * predefined zoom factors
 *
 * @author    Eduardo Francos
 * @created   26 april 2002
 * @version   <CURRENT_VERSION>
 */
public class ZoomCommand extends AbstractCommand {
	/** The scale factor to apply */
	protected float scale = 1.0f;

	/**
	 * Constructor for the ZoomCommand object
	 *
	 * @param name              the command name
	 * @param scale             Description of the Parameter
	 * @param newDrawingEditor  the DrawingEditor which manages the views
	 */
	public ZoomCommand(String newSame, float newScale, DrawingEditor newDrawingEditor) {
		super(newSame, newDrawingEditor, true);
		scale = newScale;
	}


	/** Executes the command */
	public void execute() {
		super.execute();
		zoomView().zoom(scale);
	}


	/**
	 * Sets the zoom factor of the view
	 *
	 * @return   ZoomDrawingView currently active in the editor
	 */
	public ZoomDrawingView zoomView() {
		Object view = super.view();
		if (view instanceof ZoomDrawingView) {
			return (ZoomDrawingView)view;
		}
		throw new JHotDrawRuntimeException("execute should NOT be getting called when view not instanceof ZoomDrawingView");
	}


	/**
	 * Gets the scale attribute of the ZoomCommand object
	 *
	 * @return   The scale value
	 */
	public float getScale() {
		return scale;
	}


	/**
	 * Sets the scale attribute of the ZoomCommand object
	 *
	 * @param newScale  The new scale value
	 */
	public void setScale(float newScale) {
		scale = newScale;
	}


	/**
	 * Returns true if the command is executable with the current view
	 *
	 * @return   true iff the view is a ZoomDrawingView
	 */
	protected boolean isExecutableWithView() {
		return (view() instanceof ZoomDrawingView);
	}
}
