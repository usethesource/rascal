/*
 * @(#)InsertImageCommand.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * Command to insert a named image.
 *
 * @version <$CURRENT_VERSION$>
 */
public class InsertImageCommand extends AbstractCommand {

	private String  myImageName;

	/**
	 * Constructs an insert image command.
	 * @param name the command name
	 * @param newImageName the pathname of the image
	 * @param newDrawingEditor the DrawingEditor which manages the views
	 */
	public InsertImageCommand(String name, String newImageName, DrawingEditor newDrawingEditor) {
		super(name, newDrawingEditor);
		myImageName = newImageName;
	}

	public void execute() {
		super.execute();
		setUndoActivity(createUndoActivity());
		((InsertImageCommand.UndoActivity)getUndoActivity()).insertImage();
		view().checkDamage();
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity() {
		return new InsertImageCommand.UndoActivity(view(), myImageName);
	}

	public class UndoActivity extends UndoableAdapter {

		/**
		 * Use weak reference so if the command is the last one which references
		 * the ImageFigure then it might be garbage collected. Usually, the ImageFigure
		 * is referenced from the drawing it is inserted and might be only garbage
		 * collected after an undo operation (which removes the ImageFigure from
		 * the drawing). If it has been garbage collected but is requested again
		 * (e.g. during a redo operation) it is restored if possible.
		 */
		WeakReference	myAffectedImageFigure;
		private String  myAffectedImageName;
		
		UndoActivity(DrawingView newDrawingView, String newAffectedImageName) {
			super(newDrawingView);
			myAffectedImageName = newAffectedImageName;
			setUndoable(true);
			setRedoable(true);			
		}
		
		protected void setImageFigure(ImageFigure newImageFigure) {
			myAffectedImageFigure = new WeakReference(newImageFigure);
		}
		
		protected ImageFigure getImageFigure() {
			// load image if it has not been loaded so far
			if ((myAffectedImageFigure == null) || (myAffectedImageFigure.get() == null)) {
				// ugly cast to component, but AWT wants a Component instead of an ImageObserver...
				Image image = Iconkit.instance().registerAndLoadImage(
					(Component)getDrawingView(), myAffectedImageName);
				setImageFigure(new ImageFigure(
					image, myAffectedImageName, getDrawingView().lastClick()));
			}
	
			return (ImageFigure)myAffectedImageFigure.get();
		}
	
		public boolean undo() {
			if (super.undo()) {
				getDrawingView().clearSelection();
	
				getDrawingView().drawing().orphan(getImageFigure());
	
				return true;
			}
			
			return false;
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (isRedoable()) {
				insertImage();
				return true;
			}
			
			return false;
		}

		protected void insertImage() {
			getDrawingView().add(getImageFigure());
			getDrawingView().clearSelection();
			getDrawingView().addToSelection(getImageFigure());
		}
	}
}
