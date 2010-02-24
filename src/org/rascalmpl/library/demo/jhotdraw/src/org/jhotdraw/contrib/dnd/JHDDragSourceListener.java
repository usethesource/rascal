/*
 * JHDDragSource.java
 *
 * Created on January 28, 2003, 4:49 PM
 */

package org.jhotdraw.contrib.dnd;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.DeleteFromDrawingVisitor;
import org.jhotdraw.util.Undoable;
import java.awt.Component;
import java.awt.dnd.*;
import javax.swing.JComponent;

/**
 *
 * @author  Administrator
 */
public class JHDDragSourceListener implements java.awt.dnd.DragSourceListener {
	private Undoable sourceUndoable;
	private Boolean autoscrollState;
	private DrawingEditor editor;
	
	/** Creates a new instance of JHDDragSource */
	public JHDDragSourceListener(DrawingEditor newEditor, DrawingView newView) {
		this.editor = newEditor;
	}
//	protected DrawingView view(){
//		return dv;
//	}
	protected DrawingEditor editor(){
		return editor;
	}
	/**
	 * This method is invoked to signify that the Drag and Drop operation is complete.
	 * This is the last method called in the process.
	 */
	public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
		DrawingView view = (DrawingView) dsde.getDragSourceContext().getComponent();
		log("DragSourceDropEvent-dragDropEnd");
		if (dsde.getDropSuccess() == true) {
			if (dsde.getDropAction() == DnDConstants.ACTION_MOVE) {
                log("DragSourceDropEvent-ACTION_MOVE");
				//get the flavor in order of ease of use here.
				setSourceUndoActivity(  createSourceUndoActivity( view ) );
				DNDFigures df = (DNDFigures)DNDHelper.processReceivedData(DNDFiguresTransferable.DNDFiguresFlavor, dsde.getDragSourceContext().getTransferable());
				getSourceUndoActivity().setAffectedFigures( df.getFigures() );

				//all this visitation needs to be hidden in a view method.
				DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor(view.drawing());
				FigureEnumeration fe = getSourceUndoActivity().getAffectedFigures();
				while (fe.hasNextFigure()) {
					fe.nextFigure().visit(deleteVisitor);
				}
				view.clearSelection();
				view.checkDamage();

				editor().getUndoManager().pushUndo( getSourceUndoActivity() );
				editor().getUndoManager().clearRedos();
				// update menus
				editor().figureSelectionChanged( view );
			}
			else if (dsde.getDropAction() == DnDConstants.ACTION_COPY) {
				log("DragSourceDropEvent-ACTION_COPY");
			}
		}

		if (autoscrollState != null) {
			Component c = dsde.getDragSourceContext().getComponent();
			if (JComponent.class.isInstance( c )) {
				JComponent jc = (JComponent)c;
				jc.setAutoscrolls(autoscrollState.booleanValue());
				autoscrollState= null;
			}
		}
	}
	/**
	 * Called as the hotspot enters a platform dependent drop site.
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
		log("DragSourceDragEvent-dragEnter");
		if (autoscrollState == null) {
			Component c = dsde.getDragSourceContext().getComponent();
			if (JComponent.class.isInstance( c )) {
				JComponent jc = (JComponent)c;
				autoscrollState= new Boolean(jc.getAutoscrolls());
				jc.setAutoscrolls(false);//why turn it off???
			}
		}
	}
	/**
	 * Called as the hotspot exits a platform dependent drop site.
	 */
	public void dragExit(java.awt.dnd.DragSourceEvent dse) {
	}
	/**
	 * Called as the hotspot moves over a platform dependent drop site.
	 */
	public void dragOver(DragSourceDragEvent dsde) {
		//log("DragSourceDragEvent-dragOver");
	}
	/**
	 * Called when the user has modified the drop gesture.
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
		log("DragSourceDragEvent-dropActionChanged");
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Factory method for undo activity
	 */
	protected Undoable createSourceUndoActivity(DrawingView drawingView) {
		return new RemoveUndoActivity( drawingView );
	}
	protected void setSourceUndoActivity(Undoable undoable){
		sourceUndoable = undoable;
	}
	protected Undoable getSourceUndoActivity(){
		return sourceUndoable;
	}
	public static class RemoveUndoActivity extends org.jhotdraw.util.UndoableAdapter {
		private boolean undone = false;
		public RemoveUndoActivity(DrawingView view) {
			super( view );
			log("RemoveUndoActivity created " + view);
			setUndoable(true);
			setRedoable(true);
		}

		public boolean undo() {
			if (isUndoable()) {
				if(getAffectedFigures().hasNextFigure()) {
					log("RemoveUndoActivity undo");
					getDrawingView().clearSelection();
					setAffectedFigures( getDrawingView().insertFigures(getAffectedFigures(), 0, 0,false));
					undone = true;
					return true;
				}
			}
			return false;
		}

		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (isRedoable()) {
				log("RemoveUndoActivity redo");
				DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor( getDrawingView().drawing());
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					fe.nextFigure().visit(deleteVisitor); //orphans figures
				}
				getDrawingView().clearSelection();
				setAffectedFigures( deleteVisitor.getDeletedFigures() );
				undone = false;
				return true;
			}
			return false;
		}
		/**
		 * Since this is a delete activity, figures can only be released if the
		 * action has not been undone.
		 */
		public void release() {
			if(undone == false){//we have figures that used to be in the drawing, but were not adding back
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
		//System.out.println("JHDDragSourceListener: " + message);
	}
	
	
	
	
	
	
	
}
