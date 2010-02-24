/*
 * @(#)AttributeContentProducerContext.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

/**
 * AttributeContentProducerContext defines the interface required of clients
 * requesting contents from AttributeFigures oriented ContentProducers.<br>
 *
 * @author  Eduardo Francos - InContext
 * @created 30 avril 2002
 * @version <$CURRENT_VERSION$>
 */
public interface AttributeContentProducerContext extends FigureContentProducerContext {

	/**
	 * Gets an attribute from the ContentProducerContext object
	 *
	 * @param name  the name of the attribute
	 * @return      The attribute value
	 */
	public Object getAttribute(String name);
}
