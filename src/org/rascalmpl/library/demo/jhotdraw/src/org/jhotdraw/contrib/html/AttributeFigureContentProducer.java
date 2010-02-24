/*
 * @(#)AttributeFigureContentProducer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.io.Serializable;
import org.jhotdraw.util.StorableOutput;
import org.jhotdraw.util.StorableInput;
import java.io.IOException;

/**
 * AttributeFigureContentProducer provides content for AttributeFigures.<br>
 * It gives priority to base class supplied values, and if none, then it
 * gets the value from the supplied AttributeContentProducerContext.
 *
 * @author  Eduardo Francos - InContext
 * @created 30 avril 2002
 * @version <$CURRENT_VERSION$>
 */
public class AttributeFigureContentProducer extends FigureDataContentProducer
		 implements Serializable {

	/**Constructor for the AttributeFigureContentProducer object */
	public AttributeFigureContentProducer() { }

	/**
	 * Produces the contents for the attribute
	 *
	 * @param context       the calling client context
	 * @param ctxAttrName   the attribute name
	 * @param ctxAttrValue  the attribute value that led to the call to this
	 * @return              The content value
	 */
	public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue) {
		// first chance to basic values
		Object attrValue = super.getContent(context, ctxAttrName, ctxAttrValue);
		if (attrValue != null) {
			return attrValue;
		}

		// no, return value from attributes
		return ((AttributeContentProducerContext)context).getAttribute(ctxAttrName);
	}

	/**
	 * Writes the storable
	 *
	 * @param dw  the storable output
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
	}

	/**
	 * Writes the storable
	 *
	 * @param dr               the storable input
	 * @exception IOException  thrown by called methods
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
	}
}
