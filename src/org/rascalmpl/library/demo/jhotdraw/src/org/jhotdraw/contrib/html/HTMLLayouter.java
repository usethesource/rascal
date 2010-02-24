/*
 * @(#)HTMLLayouter.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Insets;
import java.awt.Point;

import java.awt.Rectangle;
import java.io.IOException;
import org.jhotdraw.contrib.Layoutable;
import org.jhotdraw.contrib.Layouter;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

/**
 * HTMLLayouter implements the logic for laying out figures based on an
 * HTML template.
 *
 * @author  Eduardo Francos - InContext
 * @created 4 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class HTMLLayouter implements Layouter {

	/**Constructor for the HTMLLayouter object */
	public HTMLLayouter() { }

	/**
	 * Constructor which associates a HTMLLayouter with
	 * a certain Layoutable.
	 *
	 * @param newLayoutable  Layoutable to be laid out
	 */
	public HTMLLayouter(Layoutable newLayoutable) {
		this();
//		setLayoutable(newLayoutable);
	}

	/**
	 * Description of the Method
	 *
	 * @param origin  Description of the Parameter
	 * @param corner  Description of the Parameter
	 * @return        Description of the Return Value
	 */
	public Rectangle calculateLayout(Point origin, Point corner) {
		/**
		 * @todo:   Implement this org.jhotdraw.contrib.Layouter method
		 */
		throw new UnsupportedOperationException("Method calculateLayout() not yet implemented.");
	}

	/**
	 * Description of the Method
	 *
	 * @param origin  Description of the Parameter
	 * @param corner  Description of the Parameter
	 * @return        Description of the Return Value
	 */
	public Rectangle layout(Point origin, Point corner) {
		/**
		 * @todo:   Implement this org.jhotdraw.contrib.Layouter method
		 */
		throw new UnsupportedOperationException("Method layout() not yet implemented.");
	}

	/**
	 * Sets the insets attribute of the HTMLLayouter object
	 *
	 * @param newInsets  The new insets value
	 */
	public void setInsets(Insets newInsets) {
		/**
		 * @todo:   Implement this org.jhotdraw.contrib.Layouter method
		 */
		throw new UnsupportedOperationException("Method setInsets() not yet implemented.");
	}

	/**
	 * Gets the insets attribute of the HTMLLayouter object
	 *
	 * @return   The insets value
	 */
	public Insets getInsets() {
		/**
		 * @todo:   Implement this org.jhotdraw.contrib.Layouter method
		 */
		throw new UnsupportedOperationException("Method getInsets() not yet implemented.");
	}

	/**
	 * Description of the Method
	 *
	 * @param dw  Description of the Parameter
	 */
	public void write(StorableOutput dw) {
		/**
		 * @todo:   Implement this org.jhotdraw.util.Storable method
		 */
		throw new UnsupportedOperationException("Method write() not yet implemented.");
	}

	/**
	 * Description of the Method
	 *
	 * @param dr               Description of the Parameter
	 * @exception IOException  Description of the Exception
	 */
	public void read(StorableInput dr) throws IOException {
		/**
		 * @todo:   Implement this org.jhotdraw.util.Storable method
		 */
		throw new UnsupportedOperationException("Method read() not yet implemented.");
	}

	/**
	 * Create a new instance of this type and sets the layoutable
	 *
	 * @param newLayoutable  Description of the Parameter
	 * @return               Description of the Return Value
	 */
	public Layouter create(Layoutable newLayoutable) {
		return new HTMLLayouter(newLayoutable);
	}
}
