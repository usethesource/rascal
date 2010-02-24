/*
 * @(#)HTMLContentProducer.java
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

/**
 * HTMLContentProducer produces HTML suitable  values and encoded strings for
 * various types of values.<br>
 * It can also transform values into their HTML equivalents, like for instance
 * a mapping between font sizes in points and the HTML valid font size values.<br>
 *
 * @author  Eduardo Francos - InContext
 * @created 30 avril 2002
 * @version <$CURRENT_VERSION$>
 */
public class HTMLContentProducer extends AttributeFigureContentProducer
		 implements Serializable {

	/** Table of font size equivalents between points and HTML font sizes */
	protected final static int[][] htmlFontSizeEquivalences =
			{
	// 0 to 9 points ==> size 1
			{1, 0, 9},
	// 10 to 11 points ==> size 2
			{2, 10, 11},
	// 12 to 13 points ==> size 3
			{3, 12, 13},
	// 14 to 17 points ==> size 4
			{4, 14, 17},
	// 18 to 23 points ==> size 5
			{5, 18, 23},
	// 24 to 35 points ==> size 6
			{6, 24, 35},
	// 36 and higher points ==> size 7
			{7, 36, Integer.MAX_VALUE}
			};

	/**
	 * Constructor for the HTMLContentProducer object
	 */
	public HTMLContentProducer() { }

	/**
	 * Gets the HTML content value for the requested attribute
	 *
	 * @param context       the calling client context
	 * @param ctxAttrName   the attribute name (FontSize, etc...)
	 * @param ctxAttrValue  the attribute value that led to the call to this
	 * @return              The content value
	 */
	public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue) {
		HTMLContentProducerContext htmlContext = (HTMLContentProducerContext)context;

		if (ctxAttrName.compareTo(ContentProducer.ENTITY_FONT_SIZE) == 0) {
			return Integer.toString(getHTMLFontSizeEquivalent(htmlContext.getFont().getSize()));
		}

		return super.getContent(context, ctxAttrName, ctxAttrValue);
	}

	/**
	 * Gets the hTMLFontSizeEquivalent attribute of the HTMLTextAreaFigure object
	 *
	 * @param pointSize  the font size in points
	 * @return           The hTMLFontSizeEquivalent value
	 */
	public int getHTMLFontSizeEquivalent(int pointSize) {
		for (int i = 0; i < htmlFontSizeEquivalences.length; i++) {
			if (pointSize >= htmlFontSizeEquivalences[i][1] &&
					pointSize <= htmlFontSizeEquivalences[i][2]) {
				return htmlFontSizeEquivalences[i][0];
			}
		}
		// not found?!?!?! return the "normal" font size
		return 3;
	}
}
