/*
 * @(#)Storable.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.io.*;

/**
 * Interface that is used by StorableInput and StorableOutput
 * to flatten and resurrect objects. Objects that implement
 * this interface and that are resurrected by StorableInput
 * have to provide a default constructor with no arguments.
 *
 * @see StorableInput
 * @see StorableOutput
 *
 * @version <$CURRENT_VERSION$>
 */
public interface Storable {
	/**
	 * Writes the object to the StorableOutput.
	 */
	public void write(StorableOutput dw);

	/**
	 * Reads the object from the StorableInput.
	 */
	public void read(StorableInput dr) throws IOException;
}
