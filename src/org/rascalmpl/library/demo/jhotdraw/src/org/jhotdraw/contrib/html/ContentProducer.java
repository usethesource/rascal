/*
 * @(#)ContentProducer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import org.jhotdraw.util.Storable;

/**
 * ContentProducer defines the interface for objects capable of producing
 * contents on behalf of a client context.<br>
 * Primarely based on the Strategy pattern, the purpose of ContentProducers
 * is twofold:<br>
 * <li> Detach the logic for producing generic content from interested parties so
 * as to maximize reuse, of special interest for complex content</li>
 * <li> Standardize contents by allowing for the automatic decentralization of
 * contents source and production logic. Used together with the
 * ContentProducerRegistry it is possible to globally modify the behaviour
 * required for producing a specific type of contents. For example,
 * a FieldContentProducer could be declined to get its contents from variables in
 * an in-memory object, a setting from a configuration file, or even a field in
 * a database record.</li>
 *
 * @author  Eduardo Francos - InContext
 * @created 30 avril 2002
 * @version <$CURRENT_VERSION$>
 * @todo    should entity names be merged with the attribute names defined
 * in FigureAttributeConstant?
 */
public interface ContentProducer extends Storable {

	/** Entity name for the figure's current x position in pixels */
	public final static String ENTITY_FIGURE_POSX = "FigurePosX";

	/** Entity name for the figure's current y position in pixels */
	public final static String ENTITY_FIGURE_POSY = "FigurePosY";

	/** Entity name for the figure's current width in pixels */
	public final static String ENTITY_FIGURE_WIDTH = "FigureWidth";

	/** Entity name for the figure's current height in pixels */
	public final static String ENTITY_FIGURE_HEIGHT = "FigureHeight";

	/** Entity name for the figure's current text color */
	public final static String ENTITY_FRAME_COLOR = "FrameColor";

	/** Entity name for the figure's current fill color */
	public final static String ENTITY_FILL_COLOR = "FillColor";

	/** Entity name for the figure's current arrow mode */
	public final static String ENTITY_ARROW_MODE = "ArrowMode";

	/** Entity name for the figure's current font name */
	public final static String ENTITY_FONT_NAME = "FontName";

	/** Entity name for the figure's current font size */
	public final static String ENTITY_FONT_SIZE = "FontSize";

	/** Entity name for the figure's current font style */
	public final static String ENTITY_FONT_STYLE = "FontStyle";

	/**
	 * Produces the contents
	 *
	 * @param context       the calling client context
	 * @param ctxAttrName   the attribute name
	 * @param ctxAttrValue  the attribute value that led to the call to this
	 * @return              The content value
	 */
	public Object getContent(ContentProducerContext context, String ctxAttrName, Object ctxAttrValue);
}
