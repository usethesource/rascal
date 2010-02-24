/*
 * @(#)MDI_DrawApplication.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.application.*;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.contrib.dnd.DragNDropTool;

import javax.swing.*;

/**
 * Many applications have the ability to deal with multiple internal windows.
 * MDI_DrawApplications provides the basic facilities to make use of MDI in
 * JHotDraw. Its main tasks are to create a content for DrawApplications, which
 * is embedded in internal frames, to maintain a list with all internal frames
 * and to manage the switching between them.
 *
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class MDI_DrawApplication extends DrawApplication {

	/**
	 * Constructs a drawing window with a default title.
	 */
	public MDI_DrawApplication() {
		this("JHotDraw");
	}

	/**
	 * Constructs a drawing window with the given title.
	 */
	public MDI_DrawApplication(String title) {
		super(title);
	}

	/**
	 * Factory method which can be overriden by subclasses to
	 * create an instance of their type.
	 *
	 * @return	newly created application
	 */
	protected DrawApplication createApplication() {
		return new MDI_DrawApplication();
	}

	/**
	 * Creates the tools. By default only the selection tool is added.
	 * Override this method to add additional tools.
	 * Call the inherited method to include the selection tool.
	 * @param palette the palette where the tools are added.
	 */
	protected void createTools(JToolBar palette) {
		super.createTools(palette);
		Tool tool = new DragNDropTool(this);
		ToolButton tb = createToolButton(IMAGES+"SEL", "Drag N Drop Tool", tool);
		palette.add( tb );
	}

	/**
	 * Opens a new internal frame containing a new drawing.
	 */
	public void promptNew() {
		newWindow(createDrawing());
	}

	/**
	 * Method to create a new internal frame.  Applications that want
	 * to create a new internal drawing view should call this method.
	 */
	public void newWindow(Drawing newDrawing) {
		DrawingView newView = createDrawingView(newDrawing);
		getDesktop().addToDesktop(newView, Desktop.PRIMARY);
		toolDone();
	}

	/**
	 * Create the DrawingView that is active when the application is started.
	 * This initial DrawingView might be different from DrawingView created
	 * by the application, so subclasses can override this method to provide
	 * a special drawing view for application startup time, e.g. a NullDrawingView
	 * which does not display an internal frame in a multiple document interface
	 * (MDI) application.
	 *
	 * @return drawing view that is active at application startup time
	 */
	protected DrawingView createInitialDrawingView() {
		return NullDrawingView.getManagedDrawingView(this);
	}

	/**
	 * Open a new view for this application containing a
	 * view of the drawing of the currently activated window.
	 */
	public void newView() {
		if (!view().isInteractive()) {
			return;
		}

		// create new window with view to an existing drawing
		newWindow(view().drawing());

		String copyTitle = getDrawingTitle();
		if (copyTitle != null) {
			setDrawingTitle(copyTitle);
		}
		else {
			setDrawingTitle(getDefaultDrawingTitle());
		}
	}

	/**
	 * Factory method to create a specialized desktop (manager) for MDI applications
	 */
	protected Desktop createDesktop() {
		return new MDIDesktopPane(this);
	}

	/**
	 * Returns all the views in the application
	 */
	public DrawingView[] views() {
		return getDesktop().getAllFromDesktop(Desktop.PRIMARY);
	}

	public String getDefaultDrawingTitle() {
		return super.getDefaultDrawingTitle() + views().length;
	}

	/**
	 * Set the title of the currently selected drawing
	 */
	protected void setDrawingTitle(String drawingTitle) {
		getDesktop().updateTitle(drawingTitle);
	}
}
