/*
 * @(#)JavaDrawApp.java
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
import org.jhotdraw.figures.*;
import org.jhotdraw.util.*;
import org.jhotdraw.application.*;
import org.jhotdraw.contrib.*;
import org.jhotdraw.contrib.html.HTMLTextAreaFigure;
import org.jhotdraw.contrib.html.HTMLTextAreaTool;
import org.jhotdraw.contrib.zoom.ZoomDrawingView;
import org.jhotdraw.contrib.zoom.ZoomTool;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * @version <$CURRENT_VERSION$>
 */
public  class JavaDrawApp extends MDI_DrawApplication {

	private Animator            fAnimator;
	private static String       fgSampleImagesPath = "/org/jhotdraw/samples/javadraw/sampleimages";
	private static String       fgSampleImagesResourcePath = fgSampleImagesPath + "/";

	JavaDrawApp() {
		super("JHotDraw");
	}

	/**
	 * Expose constructor for benefit of subclasses.
	 *
	 * @param title The window title for this application's frame.
	 */
	public JavaDrawApp(String title) {
		super(title);
	}

	/**
	 * Factory method which create a new instance of this
	 * application.
	 *
	 * @return	newly created application
	 */
	protected DrawApplication createApplication() {
		return new JavaDrawApp();
	}

	protected DrawingView createDrawingView(Drawing newDrawing) {
		Dimension d = getDrawingViewSize();
		DrawingView newDrawingView = new ZoomDrawingView(this, d.width, d.height);
		newDrawingView.setDrawing(newDrawing);
		// notify listeners about created view when the view is added to the desktop
		//fireViewCreatedEvent(newDrawingView);
		return newDrawingView;
	}

	//-- application life cycle --------------------------------------------

	public void destroy() {
		super.destroy();
		endAnimation();
	}

	//-- DrawApplication overrides -----------------------------------------

	protected void createTools(JToolBar palette) {
		super.createTools(palette);

		Tool tool = new ZoomTool(this);
		palette.add(createToolButton(IMAGES + "ZOOM", "Zoom Tool", tool));

		tool = new UndoableTool(new TextTool(this, new TextFigure()));
		palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		tool = new UndoableTool(new ConnectedTextTool(this, new TextFigure()));
		palette.add(createToolButton(IMAGES + "ATEXT", "Connected Text Tool", tool));

		tool = new URLTool(this);
		palette.add(createToolButton(IMAGES + "URL", "URL Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new RectangleFigure()));
		palette.add(createToolButton(IMAGES + "RECT", "Rectangle Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new RoundRectangleFigure()));
		palette.add(createToolButton(IMAGES + "RRECT", "Round Rectangle Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new EllipseFigure()));
		palette.add(createToolButton(IMAGES + "ELLIPSE", "Ellipse Tool", tool));

		tool = new UndoableTool(new PolygonTool(this));
		palette.add(createToolButton(IMAGES + "POLYGON", "Polygon Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new TriangleFigure()));
		palette.add(createToolButton(IMAGES + "TRIANGLE", "Triangle Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new DiamondFigure()));
		palette.add(createToolButton(IMAGES + "DIAMOND", "Diamond Tool", tool));

		tool = new UndoableTool(new CreationTool(this, new LineFigure()));
		palette.add(createToolButton(IMAGES + "LINE", "Line Tool", tool));

		tool = new UndoableTool(new ConnectionTool(this, new LineConnection()));
		palette.add(createToolButton(IMAGES + "CONN", "Connection Tool", tool));

		tool = new UndoableTool(new ConnectionTool(this, new ElbowConnection()));
		palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool", tool));

		tool = new UndoableTool(new ScribbleTool(this));
		palette.add(createToolButton(IMAGES + "SCRIBBL", "Scribble Tool", tool));

		tool = new UndoableTool(new BorderTool(this));
		palette.add(createToolButton(IMAGES + "BORDDEC", "Border Tool", tool));

		Component button = new JButton("Hello World");
		tool = new CreationTool(this, new ComponentFigure(button));
		palette.add(createToolButton(IMAGES + "RECT", "Component Tool", tool));

		tool = new TextAreaTool(this, new TextAreaFigure());
		palette.add(createToolButton(IMAGES + "TEXTAREA", "TextArea Tool", tool));

		GraphicalCompositeFigure fig = new GraphicalCompositeFigure();
		fig.setLayouter(new SimpleLayouter(fig));
		tool = new CreationTool(this, fig);
		palette.add(createToolButton(IMAGES + "RECT", "Container Figure Tool", tool));

		tool = new CompositeFigureCreationTool(this, new RectangleFigure());
		palette.add(createToolButton(IMAGES + "RECT", "Nested Figure Tool", tool));

		tool = new HTMLTextAreaTool(this, new HTMLTextAreaFigure());
		palette.add(createToolButton(IMAGES + "TEXTAREA", "HTML TextArea Tool", tool));

		LineConnection lineConnection = new LineConnection();
		lineConnection.setStartDecoration(null);
		tool = new UndoableTool(new SplitConnectionTool(this, lineConnection));
		palette.add(createToolButton(IMAGES + "OCONN", "Split Connection Tool", tool));
	}

	protected Tool createSelectionTool() {
		return new MySelectionTool(this);
	}

	protected void createMenus(JMenuBar mb) {
		super.createMenus(mb);
		addMenuIfPossible(mb, createAnimationMenu());
		addMenuIfPossible(mb, createImagesMenu());
		addMenuIfPossible(mb, createWindowMenu());
	}

	protected JMenu createAnimationMenu() {
		CommandMenu menu = new CommandMenu("Animation");
		Command cmd = new AbstractCommand("Start Animation", this) {
			public void execute() {
				startAnimation();
			}
		};
		menu.add(cmd);

		cmd = new AbstractCommand("Stop Animation", this) {
			public void execute() {
				endAnimation();
			}
		};
		menu.add(cmd);
		return menu;
	}

	protected JMenu createWindowMenu() {
		CommandMenu menu = new CommandMenu("Window");
		Command cmd = new AbstractCommand("New View", this) {
			public void execute() {
				newView();
			}
		};
		menu.add(cmd);

		cmd = new AbstractCommand("New Window", this, false) {
			public void execute() {
				newWindow(createDrawing());
			}
		};
		menu.add(cmd);

		menu.addSeparator();
		menu.add(new WindowMenu("Window List", (MDIDesktopPane)getDesktop(), this));
		return menu;
	}

	protected JMenu createImagesMenu() {
		CommandMenu menu = new CommandMenu("Images");
		URL url = getClass().getResource(fgSampleImagesPath);
		if (url == null) {
			throw new JHotDrawRuntimeException("Could not locate images: " + fgSampleImagesPath);
		}
		File imagesDirectory = new File(url.getFile());

		try {
			String[] list = imagesDirectory.list();
			for (int i = 0; i < list.length; i++) {
				String name = list[i];
				String path = fgSampleImagesResourcePath+name;
				menu.add(new UndoableCommand(
					new InsertImageCommand(name, path, this)));
			}
		}
		catch (Exception e) {
			// do nothing
		}
		return menu;
	}

	protected Drawing createDrawing() {
		Drawing dwg = new BouncingDrawing();
        dwg.setTitle(getDefaultDrawingTitle());
		return dwg;
		//return new StandardDrawing();
	}

	//---- animation support --------------------------------------------

	public void startAnimation() {
		if (view().drawing() instanceof Animatable && fAnimator == null) {
			fAnimator = new Animator((Animatable)view().drawing(), view());
			fAnimator.start();
		}
	}

	public void endAnimation() {
		if (fAnimator != null) {
			fAnimator.end();
			fAnimator = null;
		}
	}

	protected JMenu createDebugMenu() {
		CommandMenu menu = (CommandMenu)super.createDebugMenu();

		Command cmd = new AbstractCommand("Clipping Update", this) {
			public void execute() {
				this.view().setDisplayUpdate(new ClippingUpdateStrategy());
			}
		};
		menu.add(cmd);

		return menu;
	}

	//-- main -----------------------------------------------------------

	public static void main(String[] args) {
		JavaDrawApp window = new JavaDrawApp();
		window.open();
	}
}
