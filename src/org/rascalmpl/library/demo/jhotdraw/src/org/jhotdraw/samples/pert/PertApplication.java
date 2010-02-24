/*
 * @(#)PertApplication.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.pert;

import javax.swing.JToolBar;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.figures.*;
import org.jhotdraw.application.*;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class PertApplication extends DrawApplication {

	static private final String PERTIMAGES = "/CH/ifa/draw/samples/pert/images/";

	public PertApplication() {
		super("PERT Editor");
	}

	protected void createTools(JToolBar palette) {
		super.createTools(palette);

		Tool tool = new TextTool(this, new TextFigure());
		palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		// the generic but slower version
		//tool = new CreationTool(new PertFigure());
		//palette.add(createToolButton(PERTIMAGES + "PERT", "Task Tool", tool));

		tool = new PertFigureCreationTool(this);
		palette.add(createToolButton(PERTIMAGES + "PERT", "Task Tool", tool));

		tool = new ConnectionTool(this, new PertDependency());
		palette.add(createToolButton(IMAGES + "CONN", "Dependency Tool", tool));

		tool = new CreationTool(this, new LineFigure());
		palette.add(createToolButton(IMAGES + "Line", "Line Tool", tool));
	}

	//-- main -----------------------------------------------------------

	public static void main(String[] args) {
		PertApplication pert = new PertApplication();
		pert.open();
	}
}
