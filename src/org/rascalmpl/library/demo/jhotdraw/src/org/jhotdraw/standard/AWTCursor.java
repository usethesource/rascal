/*
 * @(#)AWTCursor.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.standard;

import java.awt.Cursor;

/**
 * Default implementation of the {@link org.jhotdraw.framework.Cursor} interface
 * for AWT/Swing.
 * 
 * <p>Created on: 08/05/2003.</p>
 * 
 * @version $Revision: 1.3 $
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo 
 * Sangoi Padilha</a>
 * @see org.jhotdraw.framework.Cursor
 */
public class AWTCursor extends Cursor implements org.jhotdraw.framework.Cursor {

	/**
	 * Constructor for <code>AWTCursor</code>.
	 * @param type
	 * @see Cursor#Cursor(int)
	 */
	public AWTCursor(int type) {
		super(type);
	}

	/**
	 * Constructor for <code>AWTCursor</code>.
	 * @param name
	 * @see Cursor#Cursor(java.lang.String)
	 */
	public AWTCursor(String newName) {
		super(newName);
	}

}
