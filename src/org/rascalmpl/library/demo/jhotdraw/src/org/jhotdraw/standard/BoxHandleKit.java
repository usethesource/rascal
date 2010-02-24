/*
 * @(#)BoxHandleKit.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;
import java.awt.*;
import java.util.List;

/**
 * A set of utility methods to create Handles for the common
 * locations on a figure's display box.
 *
 * @see Handle
 *
 * @version <$CURRENT_VERSION$>
 */

 // TBD: use anonymous inner classes (had some problems with JDK 1.1)

public class BoxHandleKit {

	/**
	 * Fills the given collection with handles at each corner of a
	 * figure.
	 */
	static public void addCornerHandles(Figure f, List handles) {
		handles.add(southEast(f));
		handles.add(southWest(f));
		handles.add(northEast(f));
		handles.add(northWest(f));
	}

	/**
	 * Fills the given collection with handles at each corner
	 * and the north, south, east, and west of the figure.
	 */
	static public void addHandles(Figure f, List handles) {
		addCornerHandles(f, handles);
		handles.add(south(f));
		handles.add(north(f));
		handles.add(east(f));
		handles.add(west(f));
	}

	static public Handle south(Figure owner) {
		return new SouthHandle(owner);
	}

	static public Handle southEast(Figure owner) {
		return new SouthEastHandle(owner);
	}

	static public Handle southWest(Figure owner) {
		return new SouthWestHandle(owner);
	}

	static public Handle north(Figure owner) {
		return new NorthHandle(owner);
	}

	static public Handle northEast(Figure owner) {
		return new NorthEastHandle(owner);
	}

	static public Handle northWest(Figure owner) {
		return new NorthWestHandle(owner);
	}

	static public Handle east(Figure owner) {
		return new EastHandle(owner);
	}

	static public Handle west(Figure owner) {
		return new WestHandle(owner);
	}
}

class ResizeHandle extends LocatorHandle {
	ResizeHandle(Figure owner, Locator loc) {
		super(owner, loc);
	}

	public void invokeStart(int  x, int  y, DrawingView view) {
		setUndoActivity(createUndoActivity(view));
		getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(owner()));
		((ResizeHandle.UndoActivity)getUndoActivity()).setOldDisplayBox(owner().displayBox());
	}

	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle oldDisplayBox = ((ResizeHandle.UndoActivity)getUndoActivity()).getOldDisplayBox();
		if (owner().displayBox().equals(oldDisplayBox)) {
			// display box hasn't change so there is nothing to undo
			setUndoActivity(null);
		}
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView view) {
		return new ResizeHandle.UndoActivity(view);
	}

	public static class UndoActivity extends UndoableAdapter {
		private Rectangle myOldDisplayBox;

		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}

		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			return resetDisplayBox();
		}

		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			return resetDisplayBox();
		}

		private boolean resetDisplayBox() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasNextFigure()) {
				return false;
			}
			Figure currentFigure = fe.nextFigure();

			Rectangle figureDisplayBox = currentFigure.displayBox();
			currentFigure.displayBox(getOldDisplayBox());
			setOldDisplayBox(figureDisplayBox);
			return true;
		}

		protected void setOldDisplayBox(Rectangle newOldDisplayBox) {
			myOldDisplayBox = newOldDisplayBox;
		}

		public Rectangle getOldDisplayBox() {
			return myOldDisplayBox;
		}
	}
}

class NorthEastHandle extends ResizeHandle {
	NorthEastHandle(Figure owner) {
		super(owner, RelativeLocator.northEast());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(r.x, Math.min(r.y + r.height, y)),
			new Point(Math.max(r.x, x), r.y + r.height)
		);
	}
}

class EastHandle extends ResizeHandle {
	EastHandle(Figure owner) {
		super(owner, RelativeLocator.east());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(r.x, r.y), new Point(Math.max(r.x, x), r.y + r.height)
		);
	}
}

class NorthHandle extends ResizeHandle {
	NorthHandle(Figure owner) {
		super(owner, RelativeLocator.north());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(r.x, Math.min(r.y + r.height, y)),
			new Point(r.x + r.width, r.y + r.height)
		);
	}
}

class NorthWestHandle extends ResizeHandle {
	NorthWestHandle(Figure owner) {
		super(owner, RelativeLocator.northWest());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(Math.min(r.x + r.width, x), Math.min(r.y + r.height, y)),
			new Point(r.x + r.width, r.y + r.height)
		);
	}
}

class SouthEastHandle extends ResizeHandle {
	SouthEastHandle(Figure owner) {
		super(owner, RelativeLocator.southEast());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(r.x, r.y),
			new Point(Math.max(r.x, x), Math.max(r.y, y))
		);
	}
}

class SouthHandle extends ResizeHandle {
	SouthHandle(Figure owner) {
		super(owner, RelativeLocator.south());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(r.x, r.y),
			new Point(r.x + r.width, Math.max(r.y, y))
		);
	}
}

class SouthWestHandle extends ResizeHandle {
	SouthWestHandle(Figure owner) {
		super(owner, RelativeLocator.southWest());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(Math.min(r.x + r.width, x), r.y),
			new Point(r.x + r.width, Math.max(r.y, y))
		);
	}
}

class WestHandle extends ResizeHandle {
	WestHandle(Figure owner) {
		super(owner, RelativeLocator.west());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		Rectangle r = owner().displayBox();
		owner().displayBox(
			new Point(Math.min(r.x + r.width, x), r.y),
			new Point(r.x + r.width, r.y + r.height)
		);
	}
}
