/*
 * @(#)DNDFiguresTransferable.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.dnd;

import java.awt.datatransfer.*;
import java.io.*;

/**
 * @author  C.L.Gilbert <dnoyeb@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class DNDFiguresTransferable implements Transferable , Serializable {
	public static DataFlavor DNDFiguresFlavor = new DataFlavor(DNDFigures.class,"DNDFigures");
	private Object o;

	public DNDFiguresTransferable(Object newObject) {
		//if object is not serializable throw exception
		o = newObject;
	}
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor [] {DNDFiguresFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DNDFiguresFlavor);
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if ( isDataFlavorSupported(flavor) == false) {
			throw new UnsupportedFlavorException( flavor );
		}
		return o;
	}
}