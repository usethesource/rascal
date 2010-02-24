/*
 * @(#)RectangleFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;
import org.jhotdraw.framework.HandleEnumeration;


/**
 * A rectangle figure.
 *
 * @version <$CURRENT_VERSION$>
 */
public class RectangleFigure extends AttributeFigure {

	private Rectangle   fDisplayBox;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 184722075881789163L;
	private int rectangleFigureSerializedDataVersion = 1;

	public RectangleFigure() {
		this(new Point(0,0), new Point(0,0));
	}

	public RectangleFigure(Point origin, Point corner) {
		basicDisplayBox(origin,corner);
	}

	public void basicDisplayBox(Point origin, Point corner) {
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);
	}

	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		BoxHandleKit.addHandles(this, handles);
		return new HandleEnumerator(handles);
	}

	public Rectangle displayBox() {
		return new Rectangle(
			fDisplayBox.x,
			fDisplayBox.y,
			fDisplayBox.width,
			fDisplayBox.height);
	}

	protected void basicMoveBy(int x, int y) {
		fDisplayBox.translate(x,y);
	}

	public void drawBackground(Graphics g) {
		Rectangle r = displayBox();
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	public void drawFrame(Graphics g) {
		Rectangle r = displayBox();
		g.drawRect(r.x, r.y, r.width-1, r.height-1);
	}

	//-- store / load ----------------------------------------------

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fDisplayBox.x);
		dw.writeInt(fDisplayBox.y);
		dw.writeInt(fDisplayBox.width);
		dw.writeInt(fDisplayBox.height);
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		fDisplayBox = new Rectangle(
			dr.readInt(),
			dr.readInt(),
			dr.readInt(),
			dr.readInt());
	}

}
