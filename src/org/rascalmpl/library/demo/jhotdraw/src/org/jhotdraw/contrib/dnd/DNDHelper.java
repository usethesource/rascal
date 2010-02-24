/*
 * @(#)DNDHelper.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.dnd;

import org.jhotdraw.framework.*;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.*;
import java.util.List;

/**
 * Changes made in hopes of eventually cleaning up the functionality and 
 * distributing it sensibly. 1/10/02
 * @author  C.L.Gilbert <dnoyeb@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public abstract class DNDHelper {
	public static DataFlavor ASCIIFlavor = new DataFlavor("text/plain; charset=ascii", "ASCII text");
	private DragGestureRecognizer dgr;
	private DragGestureListener dragGestureListener;
	private DropTarget dropTarget;
	private DragSourceListener dragSourceListener;
	private DropTargetListener dropTargetListener;
	private boolean isDragSource = false;
	private boolean isDropTarget = false;

	public DNDHelper(boolean newIsDragSource, boolean newIsDropTarget){
		isDragSource = newIsDragSource;
		isDropTarget = newIsDropTarget;
	}
	/**
	 * Do not call this from the constructor.  its methods are overridable.
	 */
	public void initialize(DragGestureListener dgl) {
		if (isDragSource) {
			setDragGestureListener(dgl);
			setDragSourceListener(createDragSourceListener());
			setDragGestureRecognizer(createDragGestureRecognizer(getDragGestureListener()));
		}
		if (isDropTarget) {
			setDropTargetListener(createDropTargetListener());
			setDropTarget(createDropTarget());
		}
	}

	public void deinitialize(){
		if (getDragSourceListener() != null) {
			destroyDragGestreRecognizer();
			setDragSourceListener(null);
		}
		if (getDropTargetListener() != null) {
			setDropTarget(null);
			setDropTargetListener(null);
		}
	}
//	public void setDragSourceState(boolean state) {
//		if(state == false){
//			getDragGestureRecognizer().setSourceActions(DnDConstants.ACTION_NONE);
//		}
//		else {
//			getDragGestureRecognizer().setSourceActions(getDragSourceActions());
//		}
//	}
	protected abstract DrawingView view();
	protected abstract DrawingEditor editor();
	
	protected static Object processReceivedData(DataFlavor flavor, Transferable transferable) {
		Object receivedData = null;
		if (transferable == null) {
			return null;
		}

		try {
		    if (flavor.equals(DataFlavor.stringFlavor)) {
				receivedData = transferable.getTransferData(DataFlavor.stringFlavor);
			}
			else if (flavor.equals(DataFlavor.javaFileListFlavor)) {
				List aList = (List)transferable.getTransferData(DataFlavor.javaFileListFlavor);
				File fList [] = new File[aList.size()];
				aList.toArray(fList);
				receivedData = fList;
			}
			else if (flavor.equals(ASCIIFlavor)) {
				/* this may be too much work for locally received data */
				InputStream is = (InputStream)transferable.getTransferData(ASCIIFlavor);
				int length = is.available();
				byte[] bytes = new byte[length];
				int n = is.read(bytes);
				if (n > 0) {
					/* seems to be a 0 tacked on the end of Windows strings.  I
					 * havent checked other platforms.  This does not happen
					 * with windows socket io.  strange?
					 */
					//for (int i = 0; i < length; i++) {
					//    if (bytes[i] == 0) {
					//        length = i;
					//        break;
					//    }
					//}
					receivedData = new String(bytes, 0, n);
				}
			}
			else if (flavor.equals(DNDFiguresTransferable.DNDFiguresFlavor)) {
				receivedData = transferable.getTransferData(DNDFiguresTransferable.DNDFiguresFlavor);
			}
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
		catch (UnsupportedFlavorException ufe) {
			System.err.println(ufe);
		}
		catch (ClassCastException cce) {
			System.err.println(cce);
		}

		return receivedData;
	}

	/**
	 * This must reflect the capabilities of the dragSsource, not your desired
	 * actions.  If you desire limited drag actions, then I suppose you need to
	 * make a new drag gesture recognizer?  I do know that if you put for instance
	 * ACTION_COPY but your device supports ACTION_COPY_OR_MOVE, then the receiving
	 * target may show the rejected icon, but will still be forced improperly to
	 * accept your MOVE since the system is not properly calling your MOVE a MOVE
	 * because you claimed incorrectly that you were incapable of MOVE.
	 */
	protected int getDragSourceActions() {
		return DnDConstants.ACTION_COPY_OR_MOVE;
	}

	protected int getDropTargetActions(){
		return DnDConstants.ACTION_COPY_OR_MOVE;
	}

	protected void setDragGestureListener(DragGestureListener dragGestureListener){
		this.dragGestureListener = dragGestureListener;
	}

	protected DragGestureListener getDragGestureListener(){
		return dragGestureListener;
	}

	protected void setDragGestureRecognizer(DragGestureRecognizer dragGestureRecognizer){
		dgr = dragGestureRecognizer;
	}

	protected DragGestureRecognizer getDragGestureRecognizer(){
		return dgr;
	}

	protected void setDropTarget(DropTarget newDropTarget){
		if ((newDropTarget == null) && (dropTarget != null)) {
			dropTarget.setComponent(null);
			dropTarget.removeDropTargetListener(getDropTargetListener());
		}
		dropTarget = newDropTarget;
	}

	protected DropTarget createDropTarget() {
		DropTarget dt = null;
		if (Component.class.isInstance(view())) {
			try {
				dt = new DropTarget((Component)view(), getDropTargetActions(), getDropTargetListener());
				//System.out.println(view().toString() + " Initialized to DND.");
			}
			catch (NullPointerException npe) {
				System.err.println("View Failed to initialize to DND.");
				System.err.println("Container likely did not have peer before the DropTarget was added");
				System.err.println(npe);
				npe.printStackTrace();
			}
		}
		return dt;
	}

	/**
	 * Used to create the gesture recognizer which in effect turns on draggability.
	 */
	protected DragGestureRecognizer createDragGestureRecognizer(DragGestureListener dgl) {
		DragGestureRecognizer aDgr = null;
		if (Component.class.isInstance(view())) {
			Component c = (Component)view();
			aDgr =	java.awt.dnd.DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
					c,
					getDragSourceActions(),
					dgl);
			//System.out.println("DragGestureRecognizer created: " + view());
		}
		return aDgr;
	}

	/**
	 * Used to destroy the gesture listener which ineffect turns off dragability.
	 */
	protected void destroyDragGestreRecognizer() {
		//System.out.println("Destroying DGR " + view());
		if (getDragGestureRecognizer() != null) {
			getDragGestureRecognizer().removeDragGestureListener(getDragGestureListener());
	    	getDragGestureRecognizer().setComponent(null);
			setDragGestureRecognizer(null);
		}
	}

	protected void setDropTargetListener(DropTargetListener dropTargetListener){
		this.dropTargetListener = dropTargetListener;
	}

	protected DropTargetListener getDropTargetListener(){
		return dropTargetListener;
		}

	protected DropTargetListener createDropTargetListener(){
		return new JHDDropTargetListener(editor(),view());
	}

	public DragSourceListener getDragSourceListener(){
		return dragSourceListener;
	}

	protected void setDragSourceListener(DragSourceListener dragSourceListener){
		this.dragSourceListener = dragSourceListener;
	}

	protected DragSourceListener createDragSourceListener(){
		return new JHDDragSourceListener(editor(),view());
	}
}
	/**
	 * These transferable objects are used to package your data when you want
	 * to initiate a transfer.  They are not used when you only want to receive
	 * data.  Formating the data is the responsibility of the sender primarily.
	 * Untested.  Used for dragging ASCII text out of JHotDraw
	 */
/*	public class ASCIIText implements Transferable
	{
		String s = new String("This is ASCII text");
		byte[] bytes;

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { ASCIIFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
			return dataFlavor.equals(ASCIIFlavor);
		}

		public Object getTransferData(DataFlavor dataFlavor)
			throws UnsupportedFlavorException, IOException  {
			if (!isDataFlavorSupported(dataFlavor))
						throw new UnsupportedFlavorException(dataFlavor);

			bytes = new byte[s.length() + 1];
			for (int i = 0; i < s.length(); i++)
				bytes = s.getBytes();
			bytes[s.length()] = 0;
			return new ByteArrayInputStream(bytes);
		}
	}*/