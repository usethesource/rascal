/*
 * @(#)StandardDrawingView.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	? by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.*;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceListener;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jhotdraw.contrib.AutoscrollHelper;
import org.jhotdraw.contrib.dnd.DNDHelper;
import org.jhotdraw.contrib.dnd.DNDInterface;
import org.jhotdraw.framework.*;
import org.jhotdraw.framework.Cursor;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.UndoableCommand;

/**
 * The standard implementation of DrawingView.
 *
 * @see DrawingView
 * @see Painter
 * @see Tool
 *
 * @version <$CURRENT_VERSION$>
 */
public class StandardDrawingView
		extends JPanel
		implements DrawingView, DNDInterface, java.awt.dnd.Autoscroll {

	/**
	 * The DrawingEditor of the view.
	 * @see #tool
	 */
	transient private DrawingEditor   fEditor;

	/**
	 * the registered listeners for selection changes
	 */
	private transient List fSelectionListeners;

	/**
	 * The shown drawing.
	 */
	private Drawing         fDrawing;

	/**
	 * the accumulated damaged area
	 */
	private transient Rectangle fDamage;

	/**
	 * The list of currently selected figures.
	 */
	transient private List fSelection;

	/**
	 * The shown selection handles.
	 */
	transient private List fSelectionHandles;

	/**
	 * The preferred size of the view
	 */

	/**
	 * The position of the last mouse click
	 * inside the view.
	 */
	private Point fLastClick;

	/**
	 * A List of optional backgrounds. The list contains
	 * view painters that are drawn before the contents,
	 * that is in the background.
	 */
	private List fBackgrounds;

	/**
	 * A List of optional foregrounds. The list contains
	 * view painters that are drawn after the contents,
	 * that is in the foreground.
	 */
	private List fForegrounds;

	/**
	 * The update strategy used to repair the view.
	 */
	private Painter fUpdateStrategy;

	/**
	 * The grid used to constrain points for snap to
	 * grid functionality.
	 */
	private PointConstrainer fConstrainer;

	/**
	 * Scrolling increment
	 */
	public static final int MINIMUM_WIDTH = 400;
	public static final int MINIMUM_HEIGHT = 300;
	public static final int SCROLL_INCR = 100;
	public static final int SCROLL_OFFSET = 10;

	private static int counter;
	private int myCounter = counter;

	private DNDHelper dndh;

    /**
     * Listener for mouse clicks.
     */
    private MouseListener mouseListener;
    
    /**
     * Listener for mouse movements.
     */
    private MouseMotionListener motionListener;
    
    /**
     * Listener for the keyboard.
     */
    private KeyListener keyListener;

    /**
     * Reflects whether the drawing view is in read-only mode (from a user's
     * perspective).
     */
    private boolean myIsReadOnly;

	/*
	 * Serialization support. In JavaDraw only the Drawing is serialized.
	 * However, for beans support StandardDrawingView supports
	 * serialization
	 */
	private static final long serialVersionUID = -3878153366174603336L;
	private int drawingViewSerializedDataVersion = 1;

	/**
	 * Constructs the view.
	 */
	public StandardDrawingView(DrawingEditor editor) {
		this(editor, MINIMUM_WIDTH, MINIMUM_HEIGHT);
	}

	public StandardDrawingView(DrawingEditor editor, int width, int height) {
		setAutoscrolls(true);
		counter++;
		fEditor = editor;
		// ricardo_padilha: changed from setSize(int, int) because it is not
		// JScrollPane-friendly. 
		setPreferredSize(new Dimension(width, height));
		fSelectionListeners = CollectionsFactory.current().createList();
		addFigureSelectionListener(editor());
		setLastClick(new Point(0, 0));
		fConstrainer = null;
		fSelection = CollectionsFactory.current().createList();
		// JFC/Swing uses double buffering automatically as default
		setDisplayUpdate(createDisplayUpdate());
		// TODO: Test FastBufferedUpdateStrategy with JFC/Swing double buffering
		//setDisplayUpdate(new FastBufferedUpdateStrategy());
		setBackground(Color.lightGray);

		addMouseListener(createMouseListener());
		addMouseMotionListener(createMouseMotionListener());
		addKeyListener(createKeyListener());
    }

	protected MouseListener createMouseListener() {
        mouseListener = new DrawingViewMouseListener();
		return mouseListener;
	}

	protected MouseMotionListener createMouseMotionListener() {
        motionListener = new DrawingViewMouseMotionListener();
		return  motionListener;
	}

	protected KeyListener createKeyListener() {
        keyListener = new DrawingViewKeyListener();
		return keyListener;
	}

	/**
	 * Factory method which can be overriden by subclasses
	 */
	protected Painter createDisplayUpdate() {
		return new SimpleUpdateStrategy();
		//return new ClippingUpdateStrategy();
	}

	/**
	 * Sets the view's editor.
	 */
	public void setEditor(DrawingEditor editor) {
		fEditor = editor;
	}

	/**
	 * Gets the current tool.
	 */
	public Tool tool() {
		return editor().tool();
	}

	/**
	 * Gets the drawing.
	 */
	public Drawing drawing() {
		return fDrawing;
	}

	/**
	 * Sets and installs another drawing in the view.
	 */
	public void setDrawing(Drawing d) {
		if (drawing() != null) {
			clearSelection();
			drawing().removeDrawingChangeListener(this);
		}

		fDrawing = d;
		if (drawing() != null) {
			drawing().addDrawingChangeListener(this);
		}

		checkMinimumSize();
		repaint();
	}

	/**
	 * Gets the editor.
	 */
	public DrawingEditor editor() {
		return fEditor;
	}

	/**
	 * Adds a figure to the drawing.
	 * @return the added figure.
	 */
	public Figure add(Figure figure) {
		return drawing().add(figure);
	}

	/**
	 * Removes a figure from the drawing.
	 * @return the removed figure
	 */
	public Figure remove(Figure figure) {
		return drawing().remove(figure);
	}

	/**
	 * Adds a Collection of figures to the drawing.
	 */
	public void addAll(Collection figures) {
		FigureEnumeration fe = new FigureEnumerator(figures);
		while (fe.hasNextFigure()) {
			add(fe.nextFigure());
		}
	}

	/**
	 * Check existance of figure in the drawing
	 */
	public boolean figureExists(Figure inf, FigureEnumeration fe) {
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();

			if (figure.includes(inf)) {
				return true;
			}
		}

	  return false;
	}

	/**
     * Inserts a FigureEnumeration of figures and translates them by the
	 * given offset. This function is used to insert figures from clipboards (cut/copy)
	 *
	 * @return enumeration which has been added to the drawing. The figures in the enumeration
	 *         can have changed during adding them (e.g. they could have been decorated).
	 */
	public FigureEnumeration insertFigures(FigureEnumeration fe, int dx, int dy, boolean bCheck) {
		if (fe == null) {
			return FigureEnumerator.getEmptyEnumeration();
		}

		List vCF = CollectionsFactory.current().createList(10);
		InsertIntoDrawingVisitor visitor = new InsertIntoDrawingVisitor(drawing());

		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (figure instanceof ConnectionFigure) {
				vCF.add(figure);
			}
			else if (figure != null) {
				figure.moveBy(dx, dy);
				figure.visit(visitor);
			}
		}

		FigureEnumeration ecf = new FigureEnumerator(vCF);

		while (ecf.hasNextFigure()) {
			ConnectionFigure cf = (ConnectionFigure) ecf.nextFigure();
			Figure sf = cf.startFigure();
			Figure ef = cf.endFigure();

			if (figureExists(sf, drawing().figures())
				&& figureExists(ef, drawing().figures())
				&& (!bCheck || cf.canConnect(sf, ef))) {

				if (bCheck) {
					Point sp = sf.center();
					Point ep = ef.center();
					Connector fStartConnector = cf.startFigure().connectorAt(ep.x, ep.y);
					Connector fEndConnector = cf.endFigure().connectorAt(sp.x, sp.y);

					if (fEndConnector != null && fStartConnector != null) {
						cf.connectStart(fStartConnector);
						cf.connectEnd(fEndConnector);
						cf.updateConnection();
					}
				}

				cf.visit(visitor);
			}
		}

		addToSelectionAll(visitor.getInsertedFigures());
		return visitor.getInsertedFigures();
	}

	/**
	 * Returns a FigureEnumeration of connectionfigures attached to this figure
	 */
	public FigureEnumeration getConnectionFigures(Figure inFigure) {
		// If no figure or figure is non connectable, just return null
		if (inFigure == null || !inFigure.canConnect()) {
			return null;
		}

		// if (inFigure instanceof ConnectionFigure)
		//  return null;

		List result = CollectionsFactory.current().createList(5);
		FigureEnumeration figures = drawing().figures();

		// Find all connection figures
		while (figures.hasNextFigure()) {
			Figure f= figures.nextFigure();

			if ((f instanceof ConnectionFigure) && !(isFigureSelected(f))) {
				ConnectionFigure cf = (ConnectionFigure) f;

				if (cf.startFigure().includes(inFigure) || cf.endFigure().includes(inFigure)) {
					result.add(f);
				}
			}
		}

		return new FigureEnumerator(result);
   }

	/**
	 * Sets the current display update strategy.
	 * @see Painter
	 */
	public void setDisplayUpdate(Painter updateStrategy) {
		fUpdateStrategy = updateStrategy;
	}

	/**
	 * Sets the current display update strategy.
	 * @see Painter
	 */
	public Painter getDisplayUpdate() {
		return fUpdateStrategy;
	}

	/**
	 * Gets an enumeration over the currently selected figures.
	 * The selection is a snapshot of the current selection
	 * which does not get changed anymore
	 *
	 * @return an enumeration with the currently selected figures.
	 */
	public FigureEnumeration selection() {
		return selectionZOrdered();
	}

	/**
	 * Gets the currently selected figures in Z order.
	 * @see #selection
	 * @return a FigureEnumeration with the selected figures. The enumeration
	 * represents a snapshot of the current selection.
	 */
	public FigureEnumeration selectionZOrdered() {
		List result = CollectionsFactory.current().createList(selectionCount());

		result.addAll(fSelection);
		return new ReverseFigureEnumerator(result);
	}

	/**
	 * Gets the number of selected figures.
	 */
	public int selectionCount() {
		return fSelection.size();
	}

	/**
	 * Test whether a given figure is selected.
	 */
	public boolean isFigureSelected(Figure checkFigure) {
		return fSelection.contains(checkFigure);
	}

	/**
	 * Adds a figure to the current selection. The figure is only selected if
	 * it is also contained in the Drawing associated with this DrawingView.
	 */
	public void addToSelection(Figure figure) {
		if(addToSelectionImpl(figure) == true){
			fireSelectionChanged();			
		}
	}
	protected boolean addToSelectionImpl(Figure figure){
		boolean changed = false;
		if (!isFigureSelected(figure) && drawing().includes(figure)) {
			fSelection.add(figure);
			fSelectionHandles = null;
			figure.invalidate();
			changed = true;
		}
		return changed;
	}
	/**
	 * Adds a Collection of figures to the current selection.
	 */
	public void addToSelectionAll(Collection figures) {
		addToSelectionAll(new FigureEnumerator(figures));
	}

	/**
	 * Adds a FigureEnumeration to the current selection.
	 */
	public void addToSelectionAll(FigureEnumeration fe) {
		boolean changed = false;
		while (fe.hasNextFigure()) {
			changed |= addToSelectionImpl(fe.nextFigure());
		}
		if(changed == true){
			fireSelectionChanged();
		}
	}

	/**
	 * Removes a figure from the selection.
	 */
	public void removeFromSelection(Figure figure) {
		if (isFigureSelected(figure)) {
			fSelection.remove(figure);
			fSelectionHandles = null;
			figure.invalidate();
			fireSelectionChanged();
		}
	}

	/**
	 * If a figure isn't selected it is added to the selection.
	 * Otherwise it is removed from the selection.
	 */
	public void toggleSelection(Figure figure) {
		if (isFigureSelected(figure)) {
			removeFromSelection(figure);
		}
		else {
			addToSelection(figure);
		}
		fireSelectionChanged();
	}

	/**
	 * Clears the current selection.
	 */
	public void clearSelection() {
		// there is nothing selected - bug fix ID 628818
		if (selectionCount() == 0) {
			// avoid unnecessary selection changed event when nothing has to be cleared
			return;
		}

		FigureEnumeration fe = selection();
		while (fe.hasNextFigure()) {
			fe.nextFigure().invalidate();
		}
		fSelection = CollectionsFactory.current().createList();
		fSelectionHandles = null;
		fireSelectionChanged();
	}

	/**
	 * Gets an enumeration of the currently active handles.
	 */
	protected HandleEnumeration selectionHandles() {
		if (fSelectionHandles == null) {
			fSelectionHandles = CollectionsFactory.current().createList();
			FigureEnumeration fe = selection();
			while (fe.hasNextFigure()) {
				Figure figure = fe.nextFigure();
				HandleEnumeration kk = figure.handles();
				while (kk.hasNextHandle()) {
					fSelectionHandles.add(kk.nextHandle());
				}
			}
		}
		return new HandleEnumerator(fSelectionHandles);
	}

	/**
	 * Gets the current selection as a FigureSelection. A FigureSelection
	 * can be cut, copied, pasted.
	 */
	public FigureSelection getFigureSelection() {
		return new StandardFigureSelection(selectionZOrdered(), selectionCount());
	}

	/**
	 * Finds a handle at the given coordinates.
	 * @return the hit handle, null if no handle is found.
	 */
	public Handle findHandle(int x, int y) {
		Handle handle;

		HandleEnumeration he = selectionHandles();
		while (he.hasNextHandle()) {
			handle = he.nextHandle();
			if (handle.containsPoint(x, y)) {
				return handle;
			}
		}
		return null;
	}

	/**
	 * Informs that the current selection changed.
	 * By default this event is forwarded to the
	 * drawing editor.
	 */
	protected void fireSelectionChanged() {
		if (fSelectionListeners != null) {
			for (int i = 0; i < fSelectionListeners.size(); i++) {
				FigureSelectionListener l = (FigureSelectionListener)fSelectionListeners.get(i);
				l.figureSelectionChanged(this);
			}
		}
	}

    protected Rectangle getDamage() {
        return fDamage; // clone?
    }

    protected void setDamage(Rectangle r) {
        fDamage = r;
    }

	/**
	 * Gets the position of the last click inside the view.
	 */
	public Point lastClick() {
		return fLastClick;
	}

	protected void setLastClick(Point newLastClick) {
		fLastClick = newLastClick;
	}

	/**
	 * Sets the grid spacing that is used to constrain points.
	 */
	public void setConstrainer(PointConstrainer c) {
		fConstrainer = c;
	}

	/**
	 * Gets the current constrainer.
	 */
	public PointConstrainer getConstrainer() {
		return fConstrainer;
	}

	/**
	 * Constrains a point to the current grid.
	 */
	protected Point constrainPoint(Point p) {
		// constrain to view size
		Dimension size = getSize();
		//p.x = Math.min(size.width, Math.max(1, p.x));
		//p.y = Math.min(size.height, Math.max(1, p.y));
		p.x = Geom.range(1, size.width, p.x);
		p.y = Geom.range(1, size.height, p.y);

		if (fConstrainer != null ) {
			return fConstrainer.constrainPoint(p);
		}
		return p;
	}

	private void moveSelection(int dx, int dy) {
		FigureEnumeration figures = selection();
		while (figures.hasNextFigure()) {
			figures.nextFigure().moveBy(dx, dy);
		}
		checkDamage();
	}

	/**
	 * Refreshes the drawing if there is some accumulated damage
	 */
	public synchronized void checkDamage() {
		Iterator each = drawing().drawingChangeListeners();
		while (each.hasNext()) {
			Object l = each.next();
			if (l instanceof DrawingView) {
				((DrawingView)l).repairDamage();
			}
		}
	}

	public void repairDamage() {
		if (getDamage() != null) {
			repaint(getDamage().x, getDamage().y, getDamage().width, getDamage().height);
			setDamage(null);
		}
	}

	public void drawingInvalidated(DrawingChangeEvent e) {
		Rectangle r = e.getInvalidatedRectangle();
		if (getDamage() == null) {
			setDamage(r);
		}
		else {
			// don't manipulate rectangle returned by getDamage() directly
			// because it could be a cloned rectangle.
			Rectangle damagedR = getDamage();
			damagedR.add(r);
			setDamage(damagedR);
		}
	}

	public void drawingRequestUpdate(DrawingChangeEvent e) {
		repairDamage();
	}

	public void drawingTitleChanged(DrawingChangeEvent e){
	}

	/**
	 * Paints the drawing view. The actual drawing is delegated to
	 * the current update strategy.
	 * @see Painter
	 */
	protected void paintComponent(Graphics g) {
		if(getDisplayUpdate() != null) {
			getDisplayUpdate().draw(g, this);
		}
	}

	/**
	 * Draws the contents of the drawing view.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 */
	public void drawAll(Graphics g) {
		boolean isPrinting = g instanceof PrintGraphics;
		drawBackground(g);
		if ((fBackgrounds != null) && !isPrinting) {
			drawPainters(g, fBackgrounds);
		}
		drawDrawing(g);
		if ((fForegrounds != null) && !isPrinting) {
			drawPainters(g, fForegrounds);
		}
		if (!isPrinting) {
			drawHandles(g);
		}
	}

	/**
	 * Draws the given figures.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 * No background is drawn.
	 */
   public void draw(Graphics g, FigureEnumeration fe) {
		boolean isPrinting = g instanceof PrintGraphics;
		//drawBackground(g);
		if ((fBackgrounds != null) && !isPrinting) {
			drawPainters(g, fBackgrounds);
		}
		drawing().draw(g, fe);
		if ((fForegrounds != null) && !isPrinting) {
			drawPainters(g, fForegrounds);
		}
		if (!isPrinting) {
			drawHandles(g);
		}
	}

	/**
	 * Draws the currently active handles.
	 */
	public void drawHandles(Graphics g) {
		HandleEnumeration he = selectionHandles();
		while (he.hasNextHandle()) {
			(he.nextHandle()).draw(g);
		}
	}

	/**
	 * Draws the drawing.
	 */
	public void drawDrawing(Graphics g) {
		drawing().draw(g);
	}

	/**
	 * Draws the background. If a background pattern is set it
	 * is used to fill the background. Otherwise the background
	 * is filled in the background color.
	 */
	public void drawBackground(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getBounds().width, getBounds().height);
	}

	protected void drawPainters(Graphics g, List v) {
		for (int i = 0; i < v.size(); i++) {
			((Painter)v.get(i)).draw(g, this);
		}
	}

	/**
	 * Adds a background.
	 */
	public void addBackground(Painter painter)  {
		if (fBackgrounds == null) {
			fBackgrounds = CollectionsFactory.current().createList(3);
		}
		fBackgrounds.add(painter);
		repaint();
	}

	/**
	 * Removes a background.
	 */
	public void removeBackground(Painter painter)  {
		if (fBackgrounds != null) {
			fBackgrounds.remove(painter);
		}
		repaint();
	}

    protected List getBackgrounds() {
        return fBackgrounds;
    }

	/**
	 * Removes a foreground.
	 */
	public void removeForeground(Painter painter)  {
		if (fForegrounds != null) {
			fForegrounds.remove(painter);
		}
		repaint();
	}

	/**
	 * Adds a foreground.
	 */
	public void addForeground(Painter painter)  {
		if (fForegrounds == null) {
			fForegrounds = CollectionsFactory.current().createList(3);
		}
		fForegrounds.add(painter);
		repaint();
	}

    protected List getForegrounds() {
        return fForegrounds;
    }

	/**
	 * Freezes the view by acquiring the drawing lock.
	 * @see Drawing#lock
	 */
	public void freezeView() {
		drawing().lock();
	}

	/**
	 * Unfreezes the view by releasing the drawing lock.
	 * @see Drawing#unlock
	 */
	public void unfreezeView() {
		drawing().unlock();
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		fSelection = CollectionsFactory.current().createList(); // could use lazy initialization instead
		// could use lazy initialization instead
		if (drawing() != null) {
			drawing().addDrawingChangeListener(this);
		}
		fSelectionListeners= CollectionsFactory.current().createList();
	}

    protected void checkMinimumSize() {
        Dimension d = getDrawingSize();
		Dimension v = getPreferredSize();

		if (v.height < d.height || v.width < d.width) {
			v.height = d.height + SCROLL_OFFSET;
			v.width = d.width + SCROLL_OFFSET;
			setPreferredSize(v);
        }
    }

    /**
     * Return the size of the area occupied by the contained figures inside
     * the drawing. This method is called by checkMinimumSize().
     */
    protected Dimension getDrawingSize() {
		Dimension d = new Dimension(0, 0);
		// ricardo_padilha: this test had to be introduced because a drawing view
		// can be assigned a null drawing (see setDrawing() ).
		if (drawing() != null) {
			FigureEnumeration fe = drawing().figures();
			while (fe.hasNextFigure()) {
				Rectangle r = fe.nextFigure().displayBox();
				d.width = Math.max(d.width, r.x+r.width);
				d.height = Math.max(d.height, r.y+r.height);
			}
		}
        return d;
	}

	/**
	 * @see java.awt.Component#isFocusTraversable()
	 * @deprecated see super class
	 */
	public boolean isFocusTraversable() {
		return true;
	}

	public boolean isInteractive() {
		return true;
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	/**
	 * Add a listener for selection changes.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void addFigureSelectionListener(FigureSelectionListener fsl) {
		fSelectionListeners.add(fsl);
	}

	/**
	 * Remove a listener for selection changes.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void removeFigureSelectionListener(FigureSelectionListener fsl) {
		fSelectionListeners.remove(fsl);
	}

	public int getDefaultDNDActions() {
		return java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE;
	}

	/***** Autoscroll support *****/
	private ASH ash = new ASH(10);

	public void autoscroll(java.awt.Point p) {
		ash.autoscroll(p);
	}
	public Insets getAutoscrollInsets() {
		return ash.getAutoscrollInsets();
	}
	class ASH extends AutoscrollHelper {
		public ASH(int margin) {
			super(margin);
		}
		public Dimension getSize() {
			return StandardDrawingView.this.getSize();
		}
		public Rectangle getVisibleRect() {
			return StandardDrawingView.this.getVisibleRect();
		}
		public void scrollRectToVisible(Rectangle aRect) {
			StandardDrawingView.this.scrollRectToVisible(aRect);
		}
	}

	public String toString() {
		return "DrawingView Nr: " + myCounter;
	}

	/**
     * Default action when any uncaught exception bubbled from
     * the mouse event handlers of the tools. Subclass may override it
     * to provide other action.
     */
    protected void handleMouseEventException(Throwable t) {
		JOptionPane.showMessageDialog(
			this,
            t.getClass().getName() + " - " + t.getMessage(),
			"Error",
			JOptionPane.ERROR_MESSAGE);
		t.printStackTrace();
    }

	public class DrawingViewMouseListener extends MouseAdapter {
		 /**
		 * Handles mouse down events. The event is delegated to the
		 * currently active tool.
		 */
		public void mousePressed(MouseEvent e) {
			try {
				requestFocus(); // JDK1.1
				Point p = constrainPoint(new Point(e.getX(), e.getY()));
				setLastClick(new Point(e.getX(), e.getY()));
				tool().mouseDown(e, p.x, p.y);
				checkDamage();
			}
			catch (Throwable t) {
				handleMouseEventException(t);
			}
		}

		/**
		 * Handles mouse up events. The event is delegated to the
		 * currently active tool.
		 */
		public void mouseReleased(MouseEvent e) {
			try {
				Point p = constrainPoint(new Point(e.getX(), e.getY()));
				tool().mouseUp(e, p.x, p.y);
				checkDamage();
			}
			catch (Throwable t) {
				handleMouseEventException(t);
			}
		}
	}

	public class DrawingViewMouseMotionListener implements MouseMotionListener {
		/**
		 * Handles mouse drag events. The event is delegated to the
		 * currently active tool.
		 */
		public void mouseDragged(MouseEvent e) {
			try {
				Point p = constrainPoint(new Point(e.getX(), e.getY()));
				tool().mouseDrag(e, p.x, p.y);
				checkDamage();
			}
			catch (Throwable t) {
				handleMouseEventException(t);
			}
		}

		/**
		 * Handles mouse move events. The event is delegated to the
		 * currently active tool.
		 */
		public void mouseMoved(MouseEvent e) {
			try {
				tool().mouseMove(e, e.getX(), e.getY());
			}
			catch (Throwable t) {
				handleMouseEventException(t);
			}
		}
	}

	public class DrawingViewKeyListener implements KeyListener {
		private Command deleteCmd;

		public DrawingViewKeyListener() {
			deleteCmd = createDeleteCommand();
		}

		/**
		 * Handles key down events. Cursor keys are handled
		 * by the view the other key events are delegated to the
		 * currently active tool.
		 */
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			// Only act on nonModified keys...
			int modifiers = e.getModifiers();
			if (modifiers == 0 &&
			    ((code == KeyEvent.VK_BACK_SPACE) ||
			     (code == KeyEvent.VK_DELETE))) {
				if (deleteCmd.isExecutable()) {
					deleteCmd.execute();
					//deleteCmd.viewSelectionChanged(this);
				}
			}
			else if (modifiers == 0 && 
				 ((code == KeyEvent.VK_DOWN)
				  || (code == KeyEvent.VK_UP)
				  || (code == KeyEvent.VK_RIGHT)
				  || (code == KeyEvent.VK_LEFT))) {
				handleCursorKey(code);
			}
			else {
				tool().keyDown(e, code);
			}
			checkDamage();
		}

		/**
		 * Handles cursor keys by moving all the selected figures
		 * one grid point in the cursor direction.
		 */
		protected void handleCursorKey(int key) {
			int dx = 0, dy = 0;
			int stepX = 1, stepY = 1;
			// should consider Null Object.
			if (fConstrainer != null) {
				stepX = fConstrainer.getStepX();
				stepY = fConstrainer.getStepY();
			}

			switch (key) {
			case KeyEvent.VK_DOWN:
				dy = stepY;
				break;
			case KeyEvent.VK_UP:
				dy = -stepY;
				break;
			case KeyEvent.VK_RIGHT:
				dx = stepX;
				break;
			case KeyEvent.VK_LEFT:
				dx = -stepX;
				break;
			}
			moveSelection(dx, dy);
		}

        public void keyTyped(KeyEvent event) {
            // do nothing
        }

        public void keyReleased(KeyEvent event) {
            // do nothing
        }

		protected Command createDeleteCommand() {
			return new UndoableCommand(new DeleteCommand("Delete", editor()));
		}
    }

	protected DNDHelper createDNDHelper() {
		return new DNDHelper(true, true) {
			protected DrawingView view() {
				return StandardDrawingView.this;
			}
			protected DrawingEditor editor() {
				return StandardDrawingView.this.editor();
			}
		};
	}

	protected DNDHelper getDNDHelper() {
		if (dndh == null) {
			dndh = createDNDHelper();
		}
		return dndh;
	}

	public DragSourceListener getDragSourceListener(){
		return getDNDHelper().getDragSourceListener();
	}

	public void DNDInitialize(DragGestureListener dgl){
		getDNDHelper().initialize(dgl);
	}

	public void DNDDeinitialize() {
		getDNDHelper().deinitialize();
	}

    /**
     * Asks whether the drawing view is in read-only mode. If so, the user can't
     * modify it using mouse or keyboard actions. Yet, it can still be modified
     * from inside the program.
     */
    public boolean isReadOnly() {
        return myIsReadOnly;
    }
    
    /**
     * Determines whether the drawing view is in read-only mode. If so, the user can't
     * modify it using mouse or keyboard actions. Yet, it can still be modified
     * from inside the program.
     */
    public void setReadOnly(boolean newIsReadOnly) {
        if (newIsReadOnly != isReadOnly()) {
            if (newIsReadOnly) {
                removeMouseListener(mouseListener);
                removeMouseMotionListener(motionListener);
                removeKeyListener(keyListener);
            }
            else {
                addMouseListener(mouseListener);
                addMouseMotionListener(motionListener);
                addKeyListener(keyListener);
            }
            
            myIsReadOnly = newIsReadOnly;
        }
    }

	/**
	 * @see DrawingView#setCursor(Cursor)
	 * @see java.awt.Component#setCursor(java.awt.Cursor)
	 */
	public void setCursor(Cursor cursor) {
		if (cursor instanceof java.awt.Cursor) {
			super.setCursor((java.awt.Cursor) cursor);
		}
	}

	/**
	 * Gets the minimum dimension of the drawing.<br />
	 * Fixed version (JHotDraw version has a bug).
	 * @see StandardDrawingView#getMinimumSize()
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize() {
		Rectangle r = new Rectangle();
		FigureEnumeration k = drawing().figures();
		while (k.hasNextFigure()) {
			r.add(k.nextFigure().displayBox());
		}
		return new Dimension(r.width, r.height);
	}


}
