/*
 * @(#)DragNDropTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.dnd;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DragGestureListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;

/**
 * This is a tool which handles drag and drop between Components in
 * JHotDraw and drags from JHotDraw.  It also indirectly
 * handles management of Drops from extra-JVM sources.
 *
 *
 * Drag and Drop is about information moving, not images or objects.  Its about
 * moving a JHD rectangle to another application and that application understanding
 * both its shape, color, attributes, and everything about it. not how it looks.
 *
 * There can be only 1 such tool in an application.  A view can be registered
 * with only a single DropSource as far as I know (maybe not).
 *
 * @todo    For intra JVM transfers we need to pass Point origin as well, and not
 * assume it will be valid which currently will cause a null pointer exception.
 * or worse, will be valid with some local value.
 * The dropSource will prevent simultaneous drops.
 *
 * For a Container to be initialized to support Drag and Drop, it must first
 * have a connection to a heavyweight component.  Or more precisely it must have
 * a peer.  That means new Component() is not capable of being initiated until
 * it has attachment to a top level component i.e. JFrame.add(comp);  If you add
 * a Component to a Container, that Container must be the child of some
 * Container which is added in its heirachy to a topmost Component.  I will
 * refine this description with more appropriate terms as I think of new ways to
 * express this.  It won't work until setVisible(true) is called.  then you can 
 * initialize DND.
 *
 * note: if drop target is same as dragsource then we should draw the object.
 *
 *
 * @author C.L.Gilbert <dnoyeb@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class DragNDropTool extends AbstractTool {

	private Tool            fChild;
	private DragGestureListener dragGestureListener;
	private boolean dragOn;

	public DragNDropTool(DrawingEditor editor) {
		super(editor);
		setDragGestureListener(createDragGestureListener());
		dragOn = false;
	}

	/**
	 * Sent when a new view is created
	 */
	protected void viewCreated(DrawingView view) {
		super.viewCreated(view);
		if (DNDInterface.class.isInstance(view)) {
			DNDInterface dndi = (DNDInterface)view;
			dndi.DNDInitialize( getDragGestureListener() );
		}
	}

	/**
	 * Send when an existing view is about to be destroyed.
	 */
	protected void viewDestroying(DrawingView view) {
		if (DNDInterface.class.isInstance(view)) {
			DNDInterface dndi = (DNDInterface)view;
			dndi.DNDDeinitialize();
		}
		super.viewDestroying(view);
	}

	/**
	 * Turn on drag by adding a DragGestureRegognizer to all Views which are
	 * based on Components.
	 */
	public void activate() {
		super.activate();
//		setDragSourceActive(true);
		//System.out.println("DNDTool Activation");
		setDragOn(true);
	}

	public void deactivate() {
		//System.out.println("DNDTool deactivation.");
		setDragOn(false);
//		setDragSourceActive(false);//if its not turned off other tools will have problems since drag will start
		super.deactivate();
	}

//	private void setDragSourceActive(boolean newState) {
//		Iterator it = comps.iterator();
//		while (it.hasNext()) {
//			DNDInterface dndi = (DNDInterface)it.next();
//			dndi.setDragSourceState(newState);
//		}
//	}

	/**
	 * Sets the type of cursor based on what is under the coordinates in the
	 * active view.
	 */
	public static void setCursor(int x, int y, DrawingView view) {
		if (view == null) {   //shouldnt need this
			return;
		}
		Handle handle = view.findHandle(x, y);
		Figure figure = view.drawing().findFigure(x, y);

		if (handle != null) {
			view.setCursor(handle.getCursor());
		}
		else if (figure != null) {
			view.setCursor(new AWTCursor(java.awt.Cursor.MOVE_CURSOR));
		}
		else {
			view.setCursor(new AWTCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Handles mouse moves (if the mouse button is up).
	 * Switches the cursors depending on whats under them.
     * Don't use x, y use getX and getY so get the real unlimited position
	 * Part of the Tool interface.
	 */
	public void mouseMove(MouseEvent evt, int x, int y) {
		if (evt.getSource() == getActiveView()) {
			setCursor(x, y, getActiveView());
		}
	}

	/**
	 * Handles mouse up events. The events are forwarded to the
	 * current tracker.
	 * Part of the Tool interface.
	 */
	public void mouseUp(MouseEvent e, int x, int y) {
		if (fChild != null) { // JDK1.1 doesn't guarantee mouseDown, mouseDrag, mouseUp
			fChild.mouseUp(e, x, y);
			fChild = null;
		}
		setDragOn(true);
		view().unfreezeView();
		//get undo actions and push into undo stack?
	}

	/**
	 * Handles mouse down events and starts the corresponding tracker.
	 * Part of the Tool interface.
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
		super.mouseDown(e, x, y);
		// on MS-Windows NT: AWT generates additional mouse down events
		// when the left button is down && right button is clicked.
		// To avoid dead locks we ignore such events
		if (fChild != null) {
			return;
		}

		view().freezeView();

		Handle handle = view().findHandle(getAnchorX(), getAnchorY());
		if (handle != null) {
			//Turn off DND
			setDragOn(false);
			fChild = createHandleTracker(handle);
		}
		else {
			Figure figure = drawing().findFigure(getAnchorX(), getAnchorY());
			if (figure != null) {
				//fChild = createDragTracker(editor(), figure);
				//fChild.activate();
				fChild = null;
				if (e.isShiftDown()) {
				   view().toggleSelection(figure);
				}
				else if (!view().isFigureSelected(figure)) {
					view().clearSelection();
					view().addToSelection(figure);
				}
			}
			else {
				//Turn off DND
				setDragOn(false);
				if (!e.isShiftDown()) {
					view().clearSelection();
				}
				fChild = createAreaTracker();
			}
		}
		if (fChild != null) {
			fChild.mouseDown(e, x, y);
		}
	}

	/**
	 * Handles mouse drag events. The events are forwarded to the
	 * current tracker.
	 * Part of the Tool interface.
	 */
	public void mouseDrag(MouseEvent e, int x, int y) {
		if (fChild != null) { // JDK1.1 doesn't guarantee mouseDown, mouseDrag, mouseUp
			fChild.mouseDrag(e, x, y);
		}
	}

	/**
	 * Factory method to create an area tracker. It is used to select an
	 * area.
	 */
	protected Tool createAreaTracker() {
		return new SelectAreaTracker(editor());
	}

	/**
	 * Factory method to create a Drag tracker. It is used to drag a figure.
	 */
	protected Tool createDragTracker(DrawingEditor editor, Figure f) {
		return new DragTracker(editor, f);
	}

	/**
	 * Factory method to create a Handle tracker. It is used to track a handle.
	 */
	protected Tool createHandleTracker(Handle handle) {
		return new HandleTracker(editor(), handle);
	}

	private DragGestureListener getDragGestureListener(){
		return dragGestureListener;
	}

	private void setDragGestureListener(DragGestureListener dragGestureListener){
		this.dragGestureListener = dragGestureListener;
	}

	protected boolean isDragOn(){
		return dragOn;
	}

	protected void setDragOn(boolean isNewDragOn){
		this.dragOn = isNewDragOn;
	}

	private DragGestureListener createDragGestureListener() {
		
		return new DragGestureListener() {
			
			public void dragGestureRecognized(final java.awt.dnd.DragGestureEvent dge) {
				Component c = dge.getComponent();
				//System.out.println("Drag Gesture Recognized for " + c);
				if (isDragOn() == false) {
					return;
				}

				if (c instanceof DrawingView) {
					boolean found = false;
					DrawingView dv = (DrawingView)c;
					/* Send the drawing view which inspired the action a mouseUp to clean
					up its current tool.  This is because mouse up will otherwise never
					be sent and the tool will be stuck with only mouse down which means
					it will likely stay activated.  solve later for now just make
					but report. */
					/* this is a list of cloned figures */
					FigureEnumeration selectedElements = dv.selection();

					if (selectedElements.hasNextFigure() == false) {
						return;
					}

					Point p = dge.getDragOrigin();
		//				System.out.println("origin at " + p);
					while (selectedElements.hasNextFigure()) {
						Figure f = selectedElements.nextFigure();
						if (f.containsPoint(p.x, p.y)) {
		/*              Rectangle r = figgy.displayBox();
							sx = r.width;
							sy = r.height;*/
							//System.out.println("figure is " + figgy);
							found = true;
							break;
						}
					}
					if (found == true) {
						DNDFigures dndff = new DNDFigures(dv.selection(), p);
						DNDFiguresTransferable trans = new DNDFiguresTransferable(dndff);

						/* SAVE FOR FUTURE DRAG IMAGE SUPPORT */
						/* drag image support that I need to test on some supporting platform.
						windows is not supporting this on NT so far. Ill test 98 and 2K next

						boolean support = dragSource.isDragImageSupported();
						java.awt.image.BufferedImage  bi = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_RGB);
						Graphics2D g = bi.createGraphics();
						Iterator itr2 = selectedElements.iterator();
						while ( itr2.hasNext() ) {
							Figure fig = (Figure) itr2.next();
							fig = (Figure)fig.clone();
							Rectangle rold = fig.displayBox();
							fig.moveBy(-rold.x,-rold.y);
							fig.draw(g);
						}
						g.setBackground(Color.red);
						dge.getDragSource().startDrag(
										dge,
										DragSource.DefaultMoveDrop,
										bi,
										new Point(0,0),
										trans,
										this);
						*/
						if (c instanceof JComponent) {
							((JComponent)c).setAutoscrolls(false);
						}
						dge.getDragSource().startDrag(
										dge,
										null,
										trans,
										((DNDInterface)dv).getDragSourceListener());
					}
				}
			}
		};
	}
}
