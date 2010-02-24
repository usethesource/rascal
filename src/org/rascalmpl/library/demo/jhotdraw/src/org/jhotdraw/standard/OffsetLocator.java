/*
 * @(#)OffsetLocator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.*;
import java.io.IOException;
import org.jhotdraw.framework.*;
import org.jhotdraw.util.*;

/**
 * A locator to offset another Locator.
 * @see Locator
 *
 * @version <$CURRENT_VERSION$>
 */
public  class OffsetLocator extends AbstractLocator {

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 2679950024611847621L;
	private int offsetLocatorSerializedDataVersion = 1;

	private Locator fBase;
	private int     fOffsetX;
	private int     fOffsetY;

	public OffsetLocator() {
		fBase = null;
		fOffsetX = 0;
		fOffsetY = 0;
	}

	public OffsetLocator(Locator base) {
		this();
		fBase = base;
	}

	public OffsetLocator(Locator base, int offsetX, int offsetY) {
		this(base);
		fOffsetX = offsetX;
		fOffsetY = offsetY;
	}

	public Point locate(Figure owner) {
		Point p = fBase.locate(owner);
		p.x += fOffsetX;
		p.y += fOffsetY;
		return p;
	}

	public void moveBy(int dx, int dy) {
		fOffsetX += dx;
		fOffsetY += dy;
	}

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fOffsetX);
		dw.writeInt(fOffsetY);
		dw.writeStorable(fBase);
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		fOffsetX = dr.readInt();
		fOffsetY = dr.readInt();
		fBase = (Locator)dr.readStorable();
	}
}

