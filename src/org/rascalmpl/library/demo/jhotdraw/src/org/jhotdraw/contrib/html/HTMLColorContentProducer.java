/*
 * @(#)HTMLColorContentProducer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Color;
import java.io.Serializable;

/**
 * HTMLColorContentProducer produces RGB color HTML encoded strings.<br>
 * Ex:
 * <code>Color.blue ==> #0000FF</code><br>
 * <code>Color.red  ==> #FF00FF</code><br>
 * It can either be specific if set for a specific color, or generic, encoding
 * any color passed to the getContents method.
 *
 * @author  Eduardo Francos - InContext
 * @created 4 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class HTMLColorContentProducer extends ColorContentProducer implements Serializable {

	/**
	 * Constructor for the HTMLColorContentProducer object
	 */
	public HTMLColorContentProducer() { }

	/**
	 * Produces the contents for the color
	 *
	 * @param context       the calling client context
	 * @param ctxAttrName   the color attribute name (FrameColor, TextColor, etc)
	 * @param ctxAttrValue  the color
	 * @return              The HTML encoded RBG value for the color
	 */
	public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue) {
		// if we have our own color then use it
		// otherwise use the one supplied
		Color color = (getColor() != null) ? getColor() : (Color)ctxAttrValue;
		return getHTMLColorCode(color);
	}

	public static String getHTMLColorCode(Color color) {
		String colorCode = Integer.toHexString(color.getRGB());
		return "#" + colorCode.substring(colorCode.length() - 6);
	}
}
