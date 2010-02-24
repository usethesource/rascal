/*
 * JHDDropTargetListener.java
 *
 * Created on January 28, 2003, 4:23 PM
 */

package org.jhotdraw.contrib.dnd;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.DeleteFromDrawingVisitor;
import java.io.File;
import org.jhotdraw.util.Undoable;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;


/**
 *
 * @author  Administrator
 */
public class JHDDropTargetListener implements java.awt.dnd.DropTargetListener {
	private int     fLastX=0, fLastY=0;      // previous mouse position
	private Undoable targetUndoable;
	private DrawingView dv;
	private DrawingEditor editor;
	/** Creates a new instance of JHDDropTargetListener */
	public JHDDropTargetListener(DrawingEditor drawingEditor, DrawingView drawingView) {
		dv = drawingView;
		editor = drawingEditor;
	}
	protected DrawingView view(){
		return dv;
	}
	protected DrawingEditor editor(){
		return editor;
	}
	
	
	
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Called when a drag operation has encountered the DropTarget.
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		log("DropTargetDragEvent-dragEnter");
		supportDropTargetDragEvent(dtde);
		if (fLastX == 0) {
			fLastX = dtde.getLocation().x;
		}
		if (fLastY == 0) {
			fLastY = dtde.getLocation().y;
		}
	}

	/**
	 * The drag operation has departed the DropTarget without dropping.
	 */
	public void dragExit(java.awt.dnd.DropTargetEvent dte) {
		log("DropTargetEvent-dragExit");
	}

	/**
	 * Called when a drag operation is ongoing on the DropTarget.
	 */
	 public void dragOver(DropTargetDragEvent dtde) {
		//log("DropTargetDragEvent-dragOver");
		if (supportDropTargetDragEvent(dtde)==true) {
			int x=dtde.getLocation().x;
			int y=dtde.getLocation().y;
			if ((Math.abs(x - fLastX) > 0) || (Math.abs(y - fLastY) > 0) ) {
				//FigureEnumeration fe = view().selectionElements();
				//while (fe.hasNextFigure()) {
				//	fe.nextFigure().moveBy(x - fLastX, y - fLastY);
				//	System.out.println("moving Figures " + view());
				//}
				//view().drawing().update();
				fLastX = x;
				fLastY = y;
			}
		}
	 }

	/**
	 * The drag operation has terminated with a drop on this DropTarget.
	 * Be nice to somehow incorporate FigureTransferCommand here.
	 */
	 public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
		System.out.println("DropTargetDropEvent-drop");

		if (dtde.isDataFlavorSupported(DNDFiguresTransferable.DNDFiguresFlavor) == true) {
			log("DNDFiguresFlavor");
			if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0 ) {
				log("copy or move");
				if (dtde.isLocalTransfer() == false) {
					System.err.println("Intra-JVM Transfers not implemented for figures yet.");
					dtde.rejectDrop();
					return;
				}
				dtde.acceptDrop(dtde.getDropAction());
				try { /* protection from a malicious dropped object */
					setTargetUndoActivity( createTargetUndoActivity( view() ) );
					DNDFigures ff = (DNDFigures)DNDHelper.processReceivedData(DNDFiguresTransferable.DNDFiguresFlavor, dtde.getTransferable());
					getTargetUndoActivity().setAffectedFigures( ff.getFigures() );
					Point theO = ff.getOrigin();
					view().clearSelection();
					Point newP = dtde.getLocation();
					/** origin is where the figure thinks it is now
					  * newP is where the mouse is now.
					  * we move the figure to where the mouse is with this equation
					  */
					int dx = newP.x - theO.x;  /* distance the mouse has moved */
					int dy = newP.y - theO.y;  /* distance the mouse has moved */
					log("mouse at " + newP);
					FigureEnumeration fe = view().insertFigures( getTargetUndoActivity().getAffectedFigures() ,  dx, dy, false );
					getTargetUndoActivity().setAffectedFigures( fe );

					if (dtde.getDropAction() == DnDConstants.ACTION_MOVE) {
						view().addToSelectionAll( getTargetUndoActivity().getAffectedFigures() );
					}

					view().checkDamage();
					editor().getUndoManager().pushUndo( getTargetUndoActivity() );
					editor().getUndoManager().clearRedos();
					// update menus
					editor().figureSelectionChanged( view() );
					dtde.dropComplete(true);
				}
				catch (NullPointerException npe) {
					npe.printStackTrace();
					dtde.dropComplete(false);
				}
			}
			else {
				dtde.rejectDrop();
			}
		}
		else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			log("String flavor dropped.");
			dtde.acceptDrop(dtde.getDropAction());
			Object o = DNDHelper.processReceivedData(DataFlavor.stringFlavor, dtde.getTransferable());
			if (o != null) {
				log("Received string flavored data.");
				dtde.getDropTargetContext().dropComplete(true);
			}
			else {
				dtde.getDropTargetContext().dropComplete(false);
			}
		}
		else if (dtde.isDataFlavorSupported(DNDHelper.ASCIIFlavor) == true) {
			log("ASCII Flavor dropped.");
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			Object o = DNDHelper.processReceivedData(DNDHelper.ASCIIFlavor, dtde.getTransferable());
			if (o!= null) {
				log("Received ASCII Flavored data.");
				dtde.getDropTargetContext().dropComplete(true);
				//System.out.println(o);
			}
			else {
				dtde.getDropTargetContext().dropComplete(false);
			}
		}
		else if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			log("Java File List Flavor dropped.");
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			File [] fList = (File[]) DNDHelper.processReceivedData(DataFlavor.javaFileListFlavor, dtde.getTransferable());
			if (fList != null) {
				log("Got list of files.");
				for (int x=0; x< fList.length; x++ ) {
					System.out.println(fList[x].getAbsolutePath());
				}
				dtde.getDropTargetContext().dropComplete(true);
			}
			else {
				dtde.getDropTargetContext().dropComplete(false);
			}
		}
		fLastX = 0;
		fLastY = 0;
	}

	/**
	 * Called if the user has modified the current drop gesture.
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
		log("DropTargetDragEvent-dropActionChanged");
		supportDropTargetDragEvent(dtde);
	}

	
	
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Tests wether the Drag event is of a type that we support handling
	 * Check the DND interface and support the events it says it supports
	 * if not a dnd interface comp, then dont support! because we dont even
	 * really know what kind of view it is.
	 */
	protected boolean supportDropTargetDragEvent(DropTargetDragEvent dtde) {
		if (dtde.isDataFlavorSupported(DNDFiguresTransferable.DNDFiguresFlavor) == true) {
			if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
				dtde.acceptDrag(dtde.getDropAction());
				return true;
			}
			else {
				dtde.rejectDrag();
				return false;
			}
		}
		else if (dtde.isDataFlavorSupported(DNDHelper.ASCIIFlavor) == true) {
			dtde.acceptDrag(dtde.getDropAction());//accept everything because i am too lazy to fix yet
			return true;
		}
		else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor) == true) {
			dtde.acceptDrag(dtde.getDropAction());//accept everything because i am too lazy to fix yet
			return true;
		}
		else if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor) == true) {
			dtde.acceptDrag(dtde.getDropAction());//accept everything because i am too lazy to fix yet
			return true;
		}
		else {
			dtde.rejectDrag();
			return false;
		}
	}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Factory method for undo activity
	 */
	protected Undoable createTargetUndoActivity(DrawingView view) {
		return new AddUndoActivity( view );
	}
	protected void setTargetUndoActivity(Undoable undoable){
		targetUndoable = undoable;
	}
	protected Undoable getTargetUndoActivity(){
		return targetUndoable;
	}
	public static class AddUndoActivity extends org.jhotdraw.util.UndoableAdapter {
		private boolean undone=false;
		public AddUndoActivity(DrawingView newDrawingView) {
			super(newDrawingView);
			log("AddUndoActivity created " + newDrawingView);			
			setUndoable(true);
			setRedoable(true);
		}

		public boolean undo() {
			if (!super.undo()) {
				return false;
			}
			//undo of add really shouldnt need visitor !?!dnoyeb!?!
			log("AddUndoActivity AddUndoActivity undo");
			DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor(getDrawingView().drawing());
			FigureEnumeration fe = getAffectedFigures();
			while (fe.hasNextFigure()) {
	    		org.jhotdraw.framework.Figure f = fe.nextFigure();
				f.visit(deleteVisitor);
			}
			setAffectedFigures( deleteVisitor.getDeletedFigures() );
			getDrawingView().clearSelection();
			undone = true;
			return true;
		}

		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}
			log("AddUndoActivity redo");
			getDrawingView().clearSelection();
			setAffectedFigures(getDrawingView().insertFigures(
				getAffectedFigures(), 0, 0, false));
			undone = false;
			return true;
		}
		/**
		 *	Since this is an add operation, figures can only be released if it
		 *  has been undone.
		 */
		public void release() {
			if(undone == true){
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					Figure f = fe.nextFigure();
					getDrawingView().drawing().remove(f);
					f.release();
				}
			}
			setAffectedFigures(org.jhotdraw.standard.FigureEnumerator.getEmptyEnumeration());
		}
	}
	private static void log(String message){
		//System.out.println("JHDDropTargetListener: " + message);
	}
}
