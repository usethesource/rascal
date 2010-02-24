/*
 * @(#)AbstractContentProducer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.io.IOException;
import java.io.Serializable;

import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

/**
 * Base class for the ContentProducers hierarchy<br>
 * Doesn't do a thing now, but we may need this one later to add
 * generic behaviour.
 *
 * @author  Eduardo Francos - InContext
 * @created 7 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public abstract class AbstractContentProducer implements ContentProducer, Serializable {

	final static long serialVersionUID = -2715253447095419531L;

	/**
	 * Constructor for the AbstractContentProducer object
	 */
	public AbstractContentProducer() { }

	/**
	 * Writes the storable
	 *
	 * @param dw  the storable output
	 */
	public void write(StorableOutput dw) { }

	/**
	 * Writes the storable
	 *
	 * @param dr               the storable input
	 * @exception IOException  thrown by called methods
	 */
	public void read(StorableInput dr) throws IOException { }
}
